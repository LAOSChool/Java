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
	
	
	//Message insertMessageExt(User me, Message message);
	Message updateMessage(User me, Message message);
	Message newSimpleMessage(Integer from_user,Integer to_user, String content);
	//ArrayList<Message> broadcastMessage(User me, Message message, String filter_roles) ;
	ArrayList<Message> findUnProcSMS(User me, String api_key);
	void smsDone(User me, String api_key,Integer id);
	// 20160822 START
	Message sendUserMessageWithCC(User me, Message message);// Support CC list
	ArrayList<Message> sendClassMessage(User me, Message task, String filter_roles) ;// using by Crontabab
	ArrayList<Message> createClassMessageTaskWithCC(User me, Message message, String filter_roles) ;
	// 20160822 END
	
}