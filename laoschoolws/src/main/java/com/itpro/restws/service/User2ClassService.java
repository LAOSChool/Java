package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.User;
import com.itpro.restws.model.User2Class;

public interface User2ClassService {
	User2Class findById(Integer id);
	ArrayList<User2Class> findByUserId(Integer user_id);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	
	ArrayList<User2Class> findBySchool(Integer school_id,int from_num, int max_result); 
	ArrayList<User2Class> findByClass(Integer class_id,int from_num, int max_result);
	
	User2Class assignUserToClass(User admin,Integer user_id, Integer class_id,String notice);
	boolean isExistingUserToClass(Integer user_id, Integer class_id);
	
	
}