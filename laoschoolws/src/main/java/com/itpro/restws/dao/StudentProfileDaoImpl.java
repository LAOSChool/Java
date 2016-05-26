package com.itpro.restws.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.StudentProfile;


@Repository("studentProfileDao")
@Transactional
public class StudentProfileDaoImpl extends AbstractDao<Integer, StudentProfile> implements StudentProfileDao {

	
	@Override
	public StudentProfile findByID(Integer id) {
		return getByKey(id);
	}

	

	
	@Override
	public void saveStudentProfile(StudentProfile pro) {
		pro.setActflg("A");
		pro.setCtdusr("HuyNQ-test");
		pro.setCtddtm(Utils.now());
		pro.setCtdpgm("RestWS");
		pro.setCtddtm(Utils.now());
		persist(pro);
		
	}

	@Override
	public void updateStudentProfile(StudentProfile pro) {
		pro.setMdfusr("HuyNQ-test");
		pro.setLstmdf(Utils.now());
		pro.setMdfpgm("RestWS");
		update(pro);
		
	}




	@Override
	public ArrayList<StudentProfile> findBySchoolID(Integer school_id,int from_row, int max_result) {
		
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("school_id", school_id));
		crit_list.setMaxResults(max_result);
        crit_list.setFirstResult(from_row);
	     @SuppressWarnings("unchecked")
	     ArrayList<StudentProfile>results = (ArrayList<StudentProfile>) crit_list.list();
	     return  results;
	}




	@Override
	public ArrayList<StudentProfile> findByStudentID(Integer student_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("student_id", student_id));
	     @SuppressWarnings("unchecked")
	     ArrayList<StudentProfile>results = (ArrayList<StudentProfile>) crit_list.list();
	     return  results;
	}




	@Override
	public
	ArrayList<StudentProfile> findEx(Integer student_id, Integer school_id, Integer class_id, Integer school_year){
		Criteria crit_list = createEntityCriteria();
		if (student_id != null){
			crit_list.add(Restrictions.eq("student_id", student_id));
		}
		if (school_id != null){
			crit_list.add(Restrictions.eq("school_id", school_id));
		}
		if (class_id != null){
			crit_list.add(Restrictions.eq("class_id", class_id));
		}
		if (school_year != null){
			crit_list.add(Restrictions.eq("school_year", school_year));
		}
		
		
	     @SuppressWarnings("unchecked")
	     ArrayList<StudentProfile>results = (ArrayList<StudentProfile>) crit_list.list();
	     return  results;
	}

	
}
