package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.User;

public interface UserDao {

	User findById(Integer id);
	User findBySSO(String sso);
	int countUserBySchool(Integer school_id);
	int countUserByClass(Integer class_id);
	List<User> findBySchool(Integer school_id,int from_row,int to_row) ;
	List<User> findByClass(Integer class_id,int from_row,int to_row) ;
	void saveUser(User user);
	void updateUser(User user);
	
	Integer countUserExt(Integer school_id,Integer class_id,String role,Integer state,Integer from_row_id);
	List<User>  findUserExt(Integer school_id,int from_row,int max_result,Integer class_id,String role,Integer state,Integer from_row_id);
	
	Integer countAvailableUser(Integer school_id);
	List<User>  findAvailableUser(Integer school_id,int from_row,int max_result);

}

