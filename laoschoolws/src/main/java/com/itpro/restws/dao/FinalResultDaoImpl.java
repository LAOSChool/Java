package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.FinalResult;


@Repository("finalResultDao")
@Transactional
public class FinalResultDaoImpl extends AbstractDao<Integer, FinalResult> implements FinalResultDao {

	@Override
	public int countBySchool(int school_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from FinalResult WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;

	}

	@Override
	public int countByClass(int class_id) {
		// Get row count
		int count = ((Long)getSession().createQuery("select count(*) from FinalResult WHERE class_id= '" + class_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public int countByUser(int student_user_id) {
		int count = ((Long)getSession().createQuery("select count(*) from FinalResult WHERE student_id= '" + student_user_id+ "'").uniqueResult()).intValue();
		return count;
	}

	@Override
	public FinalResult findById(int id) {
		return this.getByKey(id);
	}

	@Override
	public List<FinalResult> findBySchool(int school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		@SuppressWarnings("unchecked")
		List<FinalResult> ret = crit_list.list();
		return ret;
	}

	@Override
	public List<FinalResult> findByClass(int class_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		@SuppressWarnings("unchecked")
		List<FinalResult> ret = crit_list.list();
		return ret;
	}

	@Override
	public List<FinalResult> findByStudent(int user_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("student_id", user_id));
		crit_list.setMaxResults(max_result);
		crit_list.setFirstResult(from_row);
		@SuppressWarnings("unchecked")
		List<FinalResult> ret = crit_list.list();
		return ret;
	}

	@Override
	public void saveFinalResult(FinalResult finalResult) {
		finalResult.setActflg("A");
		finalResult.setCtdusr("HuyNQ-test");
		finalResult.setCtddtm(Utils.now());
		finalResult.setCtdpgm("RestWS");
		finalResult.setCtddtm(Utils.now());
		save(finalResult);
		
	}

	@Override
	public void updateFinalResult(FinalResult finalResult) {
		finalResult.setMdfusr("HuyNQ-test");
		finalResult.setLstmdf(Utils.now());
		finalResult.setMdfpgm("RestWS");
		update(finalResult);
		
	}

	



	
	
}
