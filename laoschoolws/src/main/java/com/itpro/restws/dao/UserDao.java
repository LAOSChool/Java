package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.User;

public interface UserDao {

	User findById(Integer id);
	User findBySSO(String sso);
	int countUserBySchool(Integer school_id);
	List<User> findBySchool(Integer school_id,int from_row,int to_row) ;
	List<User> findByClass(Integer class_id,int from_row,int to_row) ;
	void saveUser(User user);
	void updateUser(User user);
}

