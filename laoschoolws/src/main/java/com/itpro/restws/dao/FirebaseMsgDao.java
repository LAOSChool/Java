package com.itpro.restws.dao;

import com.itpro.restws.model.FirebaseMsg;


public interface FirebaseMsgDao {

	
	
	FirebaseMsg findById(Integer id);
	void saveMsg(FirebaseMsg msg);
	void updateMsg(FirebaseMsg msg);
}

