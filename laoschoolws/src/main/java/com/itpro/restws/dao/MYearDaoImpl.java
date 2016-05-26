package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MYear;

@Repository("myearDao")
@Transactional
public class MYearDaoImpl extends AbstractDao<Integer, MYear> implements MYearDao {
	final String ModelName = "MYear";
	public MYear findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MYear> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<MYear> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveYear(MYear myear) {
		myear.setActflg("A");
		myear.setCtdusr("HuyNQ-test");
		myear.setCtddtm(Utils.now());
		myear.setCtdpgm("RestWS");
		myear.setCtddtm(Utils.now());
		save(myear);
	}

	@Override
	public void updateYear(MYear myear) {
		myear.setMdfusr("HuyNQ-test");
		myear.setLstmdf(Utils.now());
		myear.setMdfpgm("RestWS");
		update(myear);
	}
	
	
}
