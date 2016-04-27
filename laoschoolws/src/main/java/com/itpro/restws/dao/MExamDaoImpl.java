package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MExam;

@Repository("mexamDao")
@Transactional
public class MExamDaoImpl extends AbstractDao<Integer, MExam> implements MExamDao {
	final String ModelName = "MExam";
	public MExam findById(Integer id) {
		return getByKey(id.intValue());
	}

	@Override
	public int countBySchool(Integer school_id) {
		// Get row count
		int count = ((Integer)getSession().createQuery("select count(*) from " + ModelName+  " WHERE school_id = '" + school_id + "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public List<MExam> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<MExam> result = crit_list.list();
	     return result;
	}

	@Override
	public void saveExam(MExam mexam) {
		mexam.setActflg("A");
		mexam.setCtdusr("HuyNQ-test");
		mexam.setCtddtm(Utils.now());
		mexam.setCtdpgm("RestWS");
		mexam.setCtddtm(Utils.now());
		save(mexam);
	}

	@Override
	public void updateExam(MExam mexam) {
		mexam.setMdfusr("HuyNQ-test");
		mexam.setLstmdf(Utils.now());
		mexam.setMdfpgm("RestWS");
		update(mexam);
	}
	
	
}
