package com.itpro.restws.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.FirebaseMsg;

@Repository("firebaseMsgDao")
@Transactional
public class FirebaseMsgDaoImpl extends AbstractDao<Integer, FirebaseMsg> implements FirebaseMsgDao {

	public FirebaseMsg findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public void saveMsg(FirebaseMsg msg) {
		msg.setActflg("A");
		msg.setCtdusr("HuyNQ-test");
		msg.setCtddtm(Utils.now());
		msg.setCtdpgm("RestWS");
		msg.setCtddtm(Utils.now());
		//Integer id = save(message);
		save(msg);
		// message.setId(id);

	}

	@Override
	public void updateMsg(FirebaseMsg msg) {
		msg.setMdfusr("HuyNQ-test");
		msg.setLstmdf(Utils.now());
		msg.setMdfpgm("RestWS");
		update(msg);

	}
}
