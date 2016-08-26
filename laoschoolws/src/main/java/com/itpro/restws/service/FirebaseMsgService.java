package com.itpro.restws.service;

import com.itpro.restws.model.FirebaseMsg;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.Notify;

public interface FirebaseMsgService {
	FirebaseMsg create_from_message(Message msg);
	FirebaseMsg create_from_notify(Notify notify);
	
}