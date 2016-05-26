package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ExamMonth;


@Repository("examMonthDao")
@Transactional
public class ExamMonthDaoImpl extends AbstractDao<Integer, ExamMonth> implements ExamMonthDao {

	@Override
	public ExamMonth findById(Integer id) {
		return getByKey(id.intValue());
	}


	@Override
	public List<ExamMonth> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<ExamMonth> list = crit_list.list();
	     
		return  list;
	}

	

	@Override
	public void saveExamMonth(ExamMonth examMonth) {
		examMonth.setActflg("A");
		examMonth.setCtdusr("HuyNQ-test");
		examMonth.setCtddtm(Utils.now());
		examMonth.setCtdpgm("RestWS");
		examMonth.setCtddtm(Utils.now());
		save(examMonth);

		
	}

	@Override
	public void updateExamMonth(ExamMonth examMonth) {
		examMonth.setMdfusr("HuyNQ-test");
		examMonth.setLstmdf(Utils.now());
		examMonth.setMdfpgm("RestWS");
		update(examMonth);

		
	}


	@Override
	public int countBySchool(Integer school_id) {
		int count = ((Long)getSession().createQuery("select count(*) from ExamMonth WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
		
	}


	@Override
	public List<ExamMonth> findByMonth(Integer school_id, Integer ex_year, Integer ex_month) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		if (ex_month != null){
			crit_list.add(Restrictions.eq("ex_month", ex_month));
		}
		if (ex_year != null) {
			crit_list.add(Restrictions.eq("ex_year", ex_year));
		}
	     @SuppressWarnings("unchecked")
		List<ExamMonth> list = crit_list.list();
	     
		return  list;
	}


	
}
