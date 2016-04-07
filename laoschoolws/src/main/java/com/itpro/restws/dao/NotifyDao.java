package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Notify;


public interface NotifyDao {

	int countBySchool(int school_id);
	int countByClass(int class_id);
	int countByFromUser(int from_user);
	int countByToUser(int to_user);
	
	Notify findById(int id);
	
	List<Notify> findByFromUser(int from_user_id,int from_row,int max_result) ;
	List<Notify> findByToUser(int to_user_id,int from_row,int max_result) ;
	List<Notify> findBySchool(int school_id,int from_row,int max_result) ;
	List<Notify> findByClass(int class_id,int from_row,int max_result) ;
	void saveNotify(Notify notify);
	void updateNotify(Notify notify);
}

