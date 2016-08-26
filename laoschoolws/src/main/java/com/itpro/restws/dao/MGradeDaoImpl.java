package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MGrade;

@Repository("mgradeDao")
@Transactional
public class MGradeDaoImpl extends AbstractDao<Integer, MGrade> implements MGradeDao {
	
	
	final String ModelName = "MGrade";
	public MGrade findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE actflg ='A' AND school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MGrade> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
        
        crit_list.addOrder(Order.asc("id"));
        
	     @SuppressWarnings("unchecked")
		List<MGrade> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveGrade(MGrade mgrade) {
		mgrade.setActflg("A");
		mgrade.setCtdusr("HuyNQ-test");
		mgrade.setCtddtm(Utils.now());
		mgrade.setCtdpgm("RestWS");
		mgrade.setCtddtm(Utils.now());
		this.persist(mgrade);
		
	}

	@Override
	public void updateGrade(MGrade mgrade) {
		mgrade.setMdfusr("HuyNQ-test");
		mgrade.setLstmdf(Utils.now());
		mgrade.setMdfpgm("RestWS");
		update(mgrade);
		
	}

	@Override
	public void delGrade(MGrade mgrade) {
		delete(mgrade);
		
	}

	
}
