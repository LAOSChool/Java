package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Message;


public interface MessageDao {

	int countBySchool(int school_id);
	int countByClass(int class_id);
	int countByFromUser(int from_user);
	int countByToUser(int to_user);
	
	Message findById(int id);
	
	List<Message> findByFromUser(int from_user_id,int from_row,int max_result) ;
	List<Message> findByToUser(int to_user_id,int from_row,int max_result) ;
	List<Message> findBySchool(int school_id,int from_row,int max_result) ;
	List<Message> findByClass(int class_id,int from_row,int max_result) ;
	void saveMessage(Message message);
	void updateMessage(Message message);
}

