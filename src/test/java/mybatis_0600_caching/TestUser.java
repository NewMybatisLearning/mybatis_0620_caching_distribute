package mybatis_0600_caching;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.ymd.learn.mapper.UserMapper;
import com.ymd.learn.model.User;


public class TestUser {
	
	SqlSessionFactory sqlSessionFactory = null;
	
	@Before
	public void init() throws Exception {
		String resource = "mybatis-configuration.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}
	
	@Test
	public void testUser() {
		SqlSession sqlSession1 = sqlSessionFactory.openSession();
		SqlSession sqlSession2 = sqlSessionFactory.openSession();
		UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
	    UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
		
	    User u1 = userMapper1.getUserById(1);
	    System.out.println(u1);
	    sqlSession1.close();//第一次查询完后关闭sqlSession
		
	    User u2 = userMapper2.getUserById(1);
	    System.out.println(u2);
	    sqlSession2.close();
	    
	}
	
	// insert/update/delete would clear level 2 cache as well
	//　在mapper的同一个namespace中，如果有其它insert、update、delete操作数据后需要刷新缓存，如果不执行刷新缓存会出现脏读
	// 设置statement配置中的flushCache=”true” 属性，默认情况下为true，即刷新缓存，如果改成false则不会刷新。使用缓存时如果手动修改数据库表中的查询数据会出现脏读。
	// 一般下执行完commit操作都需要刷新缓存，flushCache=true表示刷新缓存，这样可以避免数据库脏读。所以我们不用设置，默认即可
	@Test

	public void testUser2() {
		SqlSession sqlSession1 = sqlSessionFactory.openSession();
		SqlSession sqlSession2 = sqlSessionFactory.openSession();
		UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
	    UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
		
	    User u1 = userMapper1.getUserById(1);
	    System.out.println(u1);
	    
	  //when execute any insert/update/delete operation . level one cache will be clear
	    u1.setName("michael111");
  		userMapper1.updateUserById(u1);
  		sqlSession1.commit();
	    
	    sqlSession1.close();//第一次查询完后关闭sqlSession
	    
	    User u2 = userMapper2.getUserById(1);
	    System.out.println(u2);
	    sqlSession2.close();
	    
	}
	
}
