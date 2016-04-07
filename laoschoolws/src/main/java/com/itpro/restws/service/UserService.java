package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.User;

public interface UserService {
	User findById(int id);
	User findBySso(String sso);
	int countBySchoolID(int school_id);
	ArrayList<User> findBySchool(int school_id,int from_num, int max_result); 
	ArrayList<User> findByClass(int class_id,int from_num, int max_result);
	
	User insertUser(User user);
	User updateUser(User user);
	public boolean isValidState(int State);
	public String changePassword(User user, String password);
}