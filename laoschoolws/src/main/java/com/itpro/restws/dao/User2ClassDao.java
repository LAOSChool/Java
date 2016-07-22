package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.User2Class;

public interface User2ClassDao {

	User2Class findById(Integer id);
	List<User2Class> findByUserId(Integer user_id,boolean is_running);
	void deleteUser2Class(User2Class user2Class);
	
	
	
	int countBySchoolId(Integer school_id);
	int countByClassId(Integer class_id);
	
	List<User2Class> findBySchoolId(Integer school_id,int from_row,int max_result);
	List<User2Class> findByClassId(Integer class_id,int from_row,int max_result);
	
	List<User2Class> findByUserAndClass(Integer user_id, Integer class_id, boolean is_running);
	
	
	void saveUser2Class(User2Class user2Class);
	void updateUser2Class(User2Class user2Class);
	
}

