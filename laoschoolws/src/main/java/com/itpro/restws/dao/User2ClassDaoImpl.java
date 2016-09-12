package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.User;
import com.itpro.restws.model.User2Class;

@Repository("user2ClassDao")
@Transactional
public class User2ClassDaoImpl extends AbstractDao<Integer, User2Class> implements User2ClassDao {

	public User2Class findById(Integer id) {
		return getByKey(id.intValue());
	}


	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User2Class> findBySchoolId(Integer school_id,int from_row,int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     List<User2Class> list = crit_list.list();
	     
		return  list;
		
	}

	@Override
	public List<User2Class> findByClassId(Integer class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<User2Class> list = crit_list.list();
	     
		return  list;
	}

	@Override
	public void saveUser2Class(User me,User2Class user2Class) {
		user2Class.setActflg("A");
		user2Class.setCtdusr(Constant.USER_SYS);
		user2Class.setCtddtm(Utils.now());
		user2Class.setCtdpgm(Constant.PGM_REST);
		
		if (me != null ){
			user2Class.setCtdusr(me.getSso_id());	
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				user2Class.setCtdwks(device);
			}
			
		}
		
		
		save(user2Class);

		
	}

	@Override
	public void updateUser2Class(User me,User2Class user2Class) {
		user2Class.setMdfusr(Constant.USER_SYS);
		user2Class.setLstmdf(Utils.now());
		user2Class.setMdfpgm(Constant.PGM_REST);
		
		if (me != null ){
			user2Class.setMdfusr(me.getSso_id());
			ApiKey apiKey = me.getApi_key();
			String device = null;
			if (apiKey != null && apiKey.getApi_key() != null ){
				device = apiKey.getApi_key();
			}
			if (device != null ){
				user2Class.setMdfwks(device);
			}
		}
		update(user2Class);
	}

	@Override
	public int countByClassId(Integer class_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setProjection(Projections.rowCount());
		Long resultCount = (Long)crit_list.uniqueResult();
	
	     return resultCount.intValue();
	}

	@Override
	public int countBySchoolId(Integer school_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setProjection(Projections.rowCount());
		Long resultCount = (Long)crit_list.uniqueResult();
	
	     return resultCount.intValue();
	}




	@Override
	public List<User2Class> findByUserId(Integer user_id,boolean is_running) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("user_id", user_id));
		if (is_running){
			crit_list.add(Restrictions.eq("closed", 0));
		}
	     @SuppressWarnings("unchecked")
		List<User2Class> list = crit_list.list();
	     
		return  list;
	}




	@Override
	public List<User2Class> findByUserAndClass(Integer user_id, Integer class_id, Integer closed) {
		Criteria crit_list = createEntityCriteria();
		boolean is_valid = false;
		if (user_id != null && user_id.intValue() >= 0){
			crit_list.add(Restrictions.eq("user_id", user_id));
			is_valid = true;
		}
		
		
		if (class_id != null && class_id.intValue() >= 0){
			crit_list.add(Restrictions.eq("class_id", class_id));
			is_valid = true;
		}
		
		
		if (closed != null && closed.intValue() >= 0){
			crit_list.add(Restrictions.eq("closed", closed));
		}
		if (is_valid){
			@SuppressWarnings("unchecked")
			List<User2Class> list = crit_list.list();
			return list;
		}
	     
	     
		return  null;
	}



	@Override
	public void deleteUser2Class(User2Class user2Class) {
		user2Class.setActflg("D");
		update(user2Class);
		
	}
	

	@Override
	public void clearChange() {
		getSession().clear();
		
	}
	@Override
	public void setFlushMode(FlushMode mode){
		getSession().setFlushMode(mode);
		
	}
}
