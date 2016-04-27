package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;


@Repository("classesDao")
@Transactional
public class ClassDaoImpl extends AbstractDao<Integer, EClass> implements ClassDao {

	@Override
	public EClass findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countClassBySchool(Integer school_id) {
		int count = ((Integer)getSession().createQuery("select count(*) from EClass WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<EClass> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<EClass> classes = crit_list.list();
	     
		return  classes;
	}

	@Override
	public List<EClass> findByUser(Integer user_id, int from_row, int max_result) {
		
		Criteria crit_list = createEntityCriteria();
		crit_list.createAlias("users", "usersAlias");
		crit_list.add(Restrictions.eq("usersAlias.id", user_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<EClass> classes = crit_list.list();
	     
		return  classes;
	}

	@Override
	public void saveClass(EClass eClass) {
		eClass.setActflg("A");
		eClass.setCtdusr("HuyNQ-test");
		eClass.setCtddtm(Utils.now());
		eClass.setCtdpgm("RestWS");
		eClass.setCtddtm(Utils.now());
		save(eClass);

		
	}

	@Override
	public void updateClass(EClass eClass) {
		eClass.setMdfusr("HuyNQ-test");
		eClass.setLstmdf(Utils.now());
		eClass.setMdfpgm("RestWS");
		update(eClass);

		
	}

	
	
}
