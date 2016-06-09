package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
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
	public void saveUser2Class(User2Class user2Class) {
		user2Class.setActflg("A");
		user2Class.setCtdusr("HuyNQ-test");
		user2Class.setCtddtm(Utils.now());
		user2Class.setCtdpgm("RestWS");
		
		save(user2Class);

		
	}

	@Override
	public void updateUser2Class(User2Class user2Class) {
		user2Class.setMdfusr("HuyNQ-test");
		user2Class.setMdfpgm("RestWS");
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
	public List<User2Class> findByUserId(Integer user_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("user_id", user_id));
	     @SuppressWarnings("unchecked")
		List<User2Class> list = crit_list.list();
	     
		return  list;
	}




	@Override
	public List<User2Class> findByUserAndClass(Integer class_id, Integer user_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("user_id", user_id));
		crit_list.add(Restrictions.eq("class_id", class_id));
		
	     @SuppressWarnings("unchecked")
		List<User2Class> list = crit_list.list();
	     
		return  list;
	}
	


}
