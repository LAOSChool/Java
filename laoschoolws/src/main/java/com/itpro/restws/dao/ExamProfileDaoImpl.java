package com.itpro.restws.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ExamProfile;


@Repository("examProfileDao")
@Transactional
public class ExamProfileDaoImpl extends AbstractDao<Integer, ExamProfile> implements ExamProfileDao {

	
	@Override
	public ExamProfile findByID(Integer id) {
		return getByKey(id);
	}

	

	
	@Override
	public void saveExamEval(ExamProfile eval) {
		eval.setActflg("A");
		eval.setCtdusr("HuyNQ-test");
		eval.setCtddtm(Utils.now());
		eval.setCtdpgm("RestWS");
		eval.setCtddtm(Utils.now());
		persist(eval);
		
	}

	@Override
	public void updateExamEval(ExamProfile eval) {
		eval.setMdfusr("HuyNQ-test");
		eval.setLstmdf(Utils.now());
		eval.setMdfpgm("RestWS");
		update(eval);
		
	}




	@Override
	public ArrayList<ExamProfile> findBySchoolID(Integer school_id,int from_row, int max_result) {
		
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
	     ArrayList<ExamProfile>results = (ArrayList<ExamProfile>) crit_list.list();
	     return  results;
	}




	@Override
	public ArrayList<ExamProfile> findByStudentID(Integer student_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("student_id", student_id));
	     @SuppressWarnings("unchecked")
	     ArrayList<ExamProfile>results = (ArrayList<ExamProfile>) crit_list.list();
	     return  results;
	}


	@Override
	public ArrayList<ExamProfile> findByClassID(Integer class_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("class_id", class_id));
	     @SuppressWarnings("unchecked")
	     ArrayList<ExamProfile>results = (ArrayList<ExamProfile>) crit_list.list();
	     return  results;
	}




	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<ExamProfile> findExt(Integer school_id, Integer class_id, Integer student_id, Integer school_year,
			Integer subject_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.add(Restrictions.eq("class_id", class_id));
		crit_list.add(Restrictions.eq("student_id", student_id));
		crit_list.add(Restrictions.eq("school_year", school_year));
		crit_list.add(Restrictions.eq("subject_id", subject_id));
		
		
	     
	     return (ArrayList<ExamProfile>) crit_list.list();
	     
	}


	
}
