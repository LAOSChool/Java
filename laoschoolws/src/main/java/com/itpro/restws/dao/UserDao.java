package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.User;

public interface UserDao {

	User findById(int id);
	User findBySSO(String sso);
	int countUserBySchool(int school_id);
	List<User> findBySchool(int school_id,int from_row,int to_row) ;
	List<User> findByClass(int class_id,int from_row,int to_row) ;
	void saveUser(User user);
	void updateUser(User user);
}

