package com.itpro.restws.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.FirebaseMsgDao;
import com.itpro.restws.model.FirebaseMsg;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.Notify;

@Service("firebaseMsgService")
@Transactional
public class FirebaseMsgServiceImpl implements FirebaseMsgService{
	private static final Logger logger = Logger.getLogger(FirebaseMsgServiceImpl.class);
	
	@Autowired
	protected FirebaseMsgDao firebaseMsgDao;
	
	@Override
	public FirebaseMsg create_from_message(Message e) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		FirebaseMsg frebaseMsg =  new FirebaseMsg();;
		
		frebaseMsg.setOrg_id(e.getId());
		frebaseMsg.setOrg_tbl("message");
		
		frebaseMsg.setSchool_id(e.getSchool_id());
		
		frebaseMsg.setFrom_user_id(e.getFrom_user_id());
		frebaseMsg.setFrom_user_name(e.getFrom_user_name());
		
		frebaseMsg.setTo_user_id(e.getTo_user_id());
		frebaseMsg.setTo_user_name(e.getTo_user_name());
		frebaseMsg.setTo_sso_id(e.getTo_sso_id());
		
		frebaseMsg.setTitle(e.getTitle());
		frebaseMsg.setContent(e.getContent());
		frebaseMsg.setIs_sent(0);
		
		firebaseMsgDao.saveMsg(frebaseMsg);
		return frebaseMsg;
			
	}

	@Override
	public FirebaseMsg create_from_notify(Notify e) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		FirebaseMsg frebaseMsg =  new FirebaseMsg();;
		
		frebaseMsg.setOrg_id(e.getId());
		frebaseMsg.setOrg_tbl("notify");
		frebaseMsg.setSchool_id(e.getSchool_id());
		frebaseMsg.setFrom_user_id(e.getFrom_user_id());
		frebaseMsg.setFrom_user_name(e.getFrom_user_name());
		
		frebaseMsg.setTo_user_id(e.getTo_user_id());
		frebaseMsg.setTo_user_name(e.getTo_user_name());
		frebaseMsg.setTo_sso_id(e.getTo_sso_id());
		
		frebaseMsg.setTitle(e.getTitle());
		frebaseMsg.setContent(e.getContent());
		frebaseMsg.setIs_sent(0);
		
		firebaseMsgDao.saveMsg(frebaseMsg);
		return frebaseMsg;
	}
	
}
