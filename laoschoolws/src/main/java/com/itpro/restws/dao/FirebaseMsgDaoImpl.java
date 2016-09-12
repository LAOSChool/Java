package com.itpro.restws.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
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
		msg.setCtdusr(Constant.USER_SYS);
		msg.setCtddtm(Utils.now());
		msg.setCtdpgm(Constant.PGM_REST);
		
		
		//Integer id = save(message);
		save(msg);
		// message.setId(id);

	}

	@Override
	public void updateMsg(FirebaseMsg msg) {
		msg.setMdfusr(Constant.PGM_REST);
		msg.setLstmdf(Utils.now());
		msg.setMdfusr(Constant.USER_SYS);
		update(msg);

	}
}
