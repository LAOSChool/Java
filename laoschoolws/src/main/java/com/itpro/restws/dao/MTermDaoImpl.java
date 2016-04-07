package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MTerm;

@Repository("mtermDao")
@Transactional
public class MTermDaoImpl extends AbstractDao<Integer, MTerm> implements MTermDao {

	
	final String ModelName = "MTerm";
	public MTerm findById(int id) {
		return getByKey(id);
	}

	@Override
	public int countBySchool(int school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MTerm> findBySchool(int school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<MTerm> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveTerm(MTerm mterm) {
		mterm.setActflg("A");
		mterm.setCtdusr("HuyNQ-test");
		mterm.setCtddtm(Utils.now());
		mterm.setCtdpgm("RestWS");
		mterm.setCtddtm(Utils.now());
		this.persist(mterm);
		
	}

	@Override
	public void updateTerm(MTerm mterm) {
		mterm.setMdfusr("HuyNQ-test");
		mterm.setLstmdf(Utils.now());
		mterm.setMdfpgm("RestWS");
		update(mterm);
		
	}

	
	
}
