package com.ymd.learn.mapper;

import com.ymd.learn.model.User;

public interface UserMapper {

	public User getUserById(int userId);

	public void updateUserById(User user);

	public void insertUser(User user);
	
}	
