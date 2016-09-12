package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.Message;
import com.itpro.restws.model.User;


public interface MessageDao {

	int countBySchool(Integer school_id);
	int countByClass(Integer class_id);
	int countByFromUser(Integer from_user);
	int countByToUser(Integer to_user);
	
	Message findById(Integer id);
	
	List<Message> findByFromUser(Integer from_user_id,int from_row,int max_result) ;
	List<Message> findByToUser(Integer to_user_id,int from_row,int max_result) ;
	List<Message> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<Message> findByClass(Integer school_id,Integer class_id,int from_row,int max_result) ;
	
	List<Message> findMessagesExt(
			Integer school_id,
			int from_row,
			int max_result,
			// Security filter
			List<Integer> permit_classes,
			List<Integer> permit_users,
			// User filter
			Integer class_id, 
			String dateFrom, 
			String dateTo,
			Integer fromUserID, 
			Integer toUserID, 
			Integer channel, 
			Integer is_read,
			Integer from_row_id
			) ;
	
	Integer countMessagesExt(Integer school_id,int from_row,int max_result,
			// Security filter
			List<Integer> permit_classes,
			List<Integer> permit_users,
			// User filter
			Integer class_id, 
			String dateFrom, 
			String dateTo,
			Integer fromUserID, 
			Integer toUserID, 
			Integer channel, 
			Integer is_read,
			Integer from_row_id
			) ;
	
	
	void saveMessage(User me,Message message);
	void updateMessage(User me,Message message);
	void setFlushMode(FlushMode mode);
	void clearChange();
	
	List<Message> findUnProcSms() ;
		
	
}

