package com.itpro.restws.email.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.tirp.restws.email.model.SendMail;


@Repository("sendMailDao")
@Transactional(value="email_transactionManager")
public class SendMailDaoImpl extends AbstractDaoEmail<Integer, SendMail> implements SendMailDao {



	@Override
	public void saveEmail(SendMail sendMail) {
		sendMail.setDate_time(Utils.now());
		save(sendMail);
		
	}


}
