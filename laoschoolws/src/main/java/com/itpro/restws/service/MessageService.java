package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.Message;

public interface MessageService {
	
	Message findById(int id);
	int countMsgBySchool(int school_id);
	int countMsgByClass (int class_id);
	int countMsgFromUser(int from_user_id);
	int countMsgToUser(int to_user_id);
	
	ArrayList<Message> findMsgBySchool(int school_id,int from_num, int max_result);
	ArrayList<Message> findMsgByClass(int class_id,int from_num, int max_result);
	ArrayList<Message> findMsgFromUser(int from_userid,int from_num, int max_result);
	ArrayList<Message> findMsgTomUser(int to_userid,int from_num, int max_result);
	 
	
	Message insertMessage(Message message);
	Message updateMessage(Message message);
	
}