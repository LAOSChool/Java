package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.AuthenKey;


@Repository("authenKeyDao")
@Transactional
public class AuthenKeyDaoImpl extends AbstractDao<Integer, AuthenKey> implements AuthenKeyDao {

	@Override
	public List<AuthenKey> findBySsoID(String sso_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("sso_id", sso_id));
	     @SuppressWarnings("unchecked")
		List<AuthenKey> keylist = crit_list.list();
	     
		return  keylist;
	}

	@Override
	public void saveToken(AuthenKey authenKey) {
		authenKey.setActflg("A");
		authenKey.setCtdusr("HuyNQ-test");
		authenKey.setCtddtm(Utils.now());
		authenKey.setCtdpgm("RestWS");
		authenKey.setCtddtm(Utils.now());
		persist(authenKey);
	}

	@Override
	public AuthenKey findByToken(String token) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("auth_key", token));
		return (AuthenKey) crit.uniqueResult();
	}

	@Override
	public void updateToken(AuthenKey authenKey) {
		authenKey.setMdfusr("HuyNQ-test");
		authenKey.setLstmdf(Utils.now());
		authenKey.setMdfpgm("RestWS");
		update(authenKey);
		
	}



	
	
}
