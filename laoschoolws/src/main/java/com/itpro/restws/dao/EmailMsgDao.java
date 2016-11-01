package com.itpro.restws.dao;

import com.itpro.restws.model.EmailMsg;


public interface EmailMsgDao {

	
	
	EmailMsg findById(Integer id);
	void saveMsg(EmailMsg msg);
	void updateMsg(EmailMsg msg);
}

