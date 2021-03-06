package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.AuthenKey;


@Repository("authenKeyDao")
@Transactional
public class AuthenKeyDaoImpl extends AbstractDao<Integer, AuthenKey> implements AuthenKeyDao {

	@Override
	public List<AuthenKey> findBySsoID(String sso_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("sso_id", sso_id));
		crit_list.addOrder(Order.asc("id") );// Last item is the small list => khi remove se di tu cuoi len
		
//		crit_list.add(Restrictions.or(
//		        Restrictions.isNull("expired_dt"),
//		        Restrictions.gt("expired_dt",Utils.now())));// Greater than NOW()
	     @SuppressWarnings("unchecked")
		List<AuthenKey> keylist = crit_list.list();
	     
		return  keylist;
	}

	@Override
	public void saveToken(AuthenKey authenKey) {
		authenKey.setActflg("A");
		authenKey.setCtdusr(Constant.USER_SYS);
		authenKey.setCtddtm(Utils.now());
		authenKey.setCtdpgm(Constant.PGM_REST);
		
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
		authenKey.setMdfusr(Constant.USER_SYS);
		authenKey.setLstmdf(Utils.now());
		authenKey.setMdfpgm(Constant.PGM_REST);
		update(authenKey);
		
	}

	@Override
	public List<AuthenKey> getNonExpired() {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.or(
			        Restrictions.isNull("expired_dt"),
			        Restrictions.gt("expired_dt",Utils.now())));// Greater than NOW()
		 
	     @SuppressWarnings("unchecked")
		List<AuthenKey> keylist = crit_list.list();
	     
		return  keylist;
	}

	@Override
	public void deleteToken(AuthenKey authenKey) {
		delete(authenKey);
		
	}

	@Override
	public int deleteBySso(String sso) {

		Query query = getSession().createQuery("delete AuthenKey where sso_id = :sso");
		query.setParameter("sso", sso);
		 
		int result = query.executeUpdate();
		 
		if (result > 0) {
		    System.out.println("AuthenKey was removed");
		}
		return result;
		
	}



	
	
}
