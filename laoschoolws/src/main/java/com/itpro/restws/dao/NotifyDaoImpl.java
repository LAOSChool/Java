package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Notify;

@Repository("notifyDao")
@Transactional
public class NotifyDaoImpl extends AbstractDao<Integer, Notify> implements NotifyDao {

	public Notify findById(int id) {
		return getByKey(id);
	}

	
	
	@Override
	public int countByFromUser(int from_user) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from Notify WHERE from_user_id= '" + from_user+ "'").uniqueResult()).intValue();
		return count;

	}


	@Override
	public int countByToUser(int to_user) {
		// Get row count
				int count = ((Long)getSession().createQuery("select count(*) from Notify WHERE to_user_id= '" + to_user+ "'").uniqueResult()).intValue();
				return count;
	}


	@Override
	public List<Notify> findByFromUser(int from_user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("from_user_id", from_user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Notify> notifies = crit_list.list();
	     return notifies;
	}


	@Override
	public List<Notify> findByToUser(int to_user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("to_user_id", to_user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Notify> notifies = crit_list.list();
	     return notifies;
	}



	@Override
	public int countBySchool(int school_id) {
		
		int count = ((Long)getSession().createQuery("select count(*) from Notify WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
	}



	@Override
	public int countByClass(int class_id) {
		
		int count = ((Long)getSession().createQuery("select count(*) from Notify WHERE class_id= '" + class_id+ "'").uniqueResult()).intValue();
		return count;
	}



	@Override
	public List<Notify> findBySchool(int school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Notify> notifies = crit_list.list();
	     return notifies;
	}



	@Override
	public List<Notify> findByClass(int class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<Notify> notifies = crit_list.list();
	     return notifies;
	}



	@Override
	public void saveNotify(Notify notify) {
		notify.setActflg("A");
		notify.setCtdusr("HuyNQ-test");
		notify.setCtddtm(Utils.now());
		notify.setCtdpgm("RestWS");
		notify.setCtddtm(Utils.now());
		save(notify);
		
	}



	@Override
	public void updateNotify(Notify notify) {
		notify.setMdfusr("HuyNQ-test");
		notify.setLstmdf(Utils.now());
		notify.setMdfpgm("RestWS");
		update(notify);
		
	}



	
}
