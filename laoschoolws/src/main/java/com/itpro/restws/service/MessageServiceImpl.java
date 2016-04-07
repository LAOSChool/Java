package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.MessageDao;
import com.itpro.restws.model.Message;

@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService{


	@Autowired
	private MessageDao messageDao;

	@Override
	public Message findById(int id) {
		return messageDao.findById(id);
		
	}

	@Override
	public int countMsgFromUser(int from_user_id) {
		return messageDao.countByFromUser(from_user_id);
	}

	@Override
	public int countMsgToUser(int to_user_id) {
		return messageDao.countByToUser(to_user_id);
	}

	@Override
	public ArrayList<Message> findMsgFromUser(int from_userid, int from_num, int max_result) {
		
		return (ArrayList<Message>) messageDao.findByFromUser(from_userid, from_num, max_result);
	}

	@Override
	public ArrayList<Message> findMsgTomUser(int to_userid, int from_num, int max_result) {
		
		return (ArrayList<Message>) messageDao.findByToUser(to_userid, from_num, max_result);
	}

	@Override
	public int countMsgBySchool(int school_id) {
		
		return messageDao.countBySchool(school_id);
	}

	@Override
	public int countMsgByClass(int class_id) {
		return messageDao.countByClass(class_id);
	}

	@Override
	public ArrayList<Message> findMsgBySchool(int school_id, int from_num, int max_result) {
		return (ArrayList<Message>) messageDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<Message> findMsgByClass(int class_id, int from_num, int max_result) {
		return (ArrayList<Message>) messageDao.findBySchool(class_id, from_num, max_result);
	}

	@Override
	public Message insertMessage(Message message) {
		messageDao.saveMessage(message);
		return message;
	}

	@Override
	public Message updateMessage(Message message) {
		
		Message messageDB = messageDao.findById(message.getId());
		messageDB = Message.updateChanges(messageDB, message);
		messageDao.updateMessage(messageDB);
		return messageDB;
		
	}





}
