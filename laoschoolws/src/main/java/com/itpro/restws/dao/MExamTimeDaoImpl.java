package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;

import com.itpro.restws.model.MExamTime;

@Repository("mexamTimeDao")
@Transactional
public class MExamTimeDaoImpl extends AbstractDao<Integer, MExamTime> implements MExamTimeDao {
	final String ModelName = "MExamTime";
	public MExamTime findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from " + ModelName+  " WHERE school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MExamTime> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<MExamTime> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveExamTime(MExamTime mexamTime) {
		mexamTime.setActflg("A");
		mexamTime.setCtdusr("HuyNQ-test");
		mexamTime.setCtddtm(Utils.now());
		mexamTime.setCtdpgm("RestWS");
		mexamTime.setCtddtm(Utils.now());
		save(mexamTime);
	}

	@Override
	public void updateExamTime(MExamTime mexamTime) {
		mexamTime.setMdfusr("HuyNQ-test");
		mexamTime.setLstmdf(Utils.now());
		mexamTime.setMdfpgm("RestWS");
		update(mexamTime);
	}
	
	
}
