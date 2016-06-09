package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.SchoolExam;


@Repository("schoolExamDao")
@Transactional
public class SchoolExamDaoImpl extends AbstractDao<Integer, SchoolExam> implements SchoolExamDao {

	@Override
	public SchoolExam findById(Integer id) {
		return getByKey(id.intValue());
	}


	@Override
	public List<SchoolExam> findBySchool(Integer school_id, int from_row, int max_result) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
		List<SchoolExam> list = crit_list.list();
	     
		return  list;
	}

	

	@Override
	public void saveSchoolExam(SchoolExam schoolExam) {
		schoolExam.setActflg("A");
		schoolExam.setCtdusr("HuyNQ-test");
		schoolExam.setCtddtm(Utils.now());
		schoolExam.setCtdpgm("RestWS");
		schoolExam.setCtddtm(Utils.now());
		save(schoolExam);

		
	}

	@Override
	public void updateSchoolExam(SchoolExam schoolExam) {
		schoolExam.setMdfusr("HuyNQ-test");
		schoolExam.setLstmdf(Utils.now());
		schoolExam.setMdfpgm("RestWS");
		update(schoolExam);

		
	}


	@Override
	public int countBySchool(Integer school_id) {
		int count = ((Long)getSession().createQuery("select count(*) from ExamMonth WHERE school_id= '" + school_id+ "'").uniqueResult()).intValue();
		return count;
		
	}


	@Override
	public List<SchoolExam> findByMonth(Integer school_id, Integer ex_year, Integer ex_month) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		if (ex_month != null){
			crit_list.add(Restrictions.eq("ex_month", ex_month));
		}
		if (ex_year != null) {
			crit_list.add(Restrictions.eq("ex_year", ex_year));
		}
	     @SuppressWarnings("unchecked")
		List<SchoolExam> list = crit_list.list();
	     
		return  list;
	}


	
	
}
