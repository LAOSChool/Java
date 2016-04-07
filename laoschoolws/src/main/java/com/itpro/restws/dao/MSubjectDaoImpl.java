package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MSubject;

@Repository("msubjectDao")
@Transactional
public class MSubjectDaoImpl extends AbstractDao<Integer, MSubject> implements MSubjectDao {

	

	final String ModelName = "MSubject";
	public MSubject findById(int id) {
		return getByKey(id);
	}

	@Override
	public int countBySchool(int school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MSubject> findBySchool(int school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<MSubject> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveSubject(MSubject msubject) {
		msubject.setActflg("A");
		msubject.setCtdusr("HuyNQ-test");
		msubject.setCtddtm(Utils.now());
		msubject.setCtdpgm("RestWS");
		msubject.setCtddtm(Utils.now());
		this.persist(msubject);
		
	}

	@Override
	public void updateSubject(MSubject msubject) {
		msubject.setMdfusr("HuyNQ-test");
		msubject.setLstmdf(Utils.now());
		msubject.setMdfpgm("RestWS");
		update(msubject);
		
	}

	
}
