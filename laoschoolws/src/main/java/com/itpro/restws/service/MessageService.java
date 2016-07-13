package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.User;

public interface MessageService {
	
	void secureCheckNewMessage(User user,Message msg);
	void secureCheckClassMessage(User user,Message msg,String class_list, String filter_roles);
	
	MessageFilter secureGetMessages(User user);
	
	Message findById(Integer id);
	int countMsgBySchool(Integer school_id);
	int countMsgByClass (Integer class_id);
	int countMsgFromUser(Integer from_user_id);
	int countMsgToUser(Integer to_user_id);
	
	ArrayList<Message> findMsgBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<Message> findMsgByClass(Integer school_id,Integer class_id,int from_num, int max_result);
	ArrayList<Message> findMsgFromUser(Integer from_userid,int from_num, int max_result);
	ArrayList<Message> findMsgTomUser(Integer to_userid,int from_num, int max_result);
	
	ArrayList<Message> findMsgExt(Integer school_id, int from_row, int max_result,MessageFilter filter,
			// User filter
			Integer class_id, 
			String dateFrom, 
			String dateTo,
			Integer fromUserID, 
			Integer toUserID, 
			Integer channel, 
			Integer is_read,
			Integer from_row_id) ;
	
	int countMsgExt(Integer school_id, 
			int from_row, int max_result,MessageFilter filter,
			Integer class_id, 
			String dateFrom, 
			String dateTo,
			Integer fromUserID, 
			Integer toUserID, 
			Integer channel, 
			Integer is_read,
			Integer from_row_id) ;
	
	
	Message insertMessageExt(Message message);
	//void insertClassMessageExt(Message message,String class_list,String filter_role);
	
//	Message insertMessage(Message message);
	Message updateMessage(Message message);
	//void broadcastMessage(User user, Message msg, String filter_class_list, String filter_roles);
	ArrayList<Message> broadcastMessage(User user, Message message, String filter_roles) ;
	Message newMessage(Integer from_user,Integer to_user, String content);
}