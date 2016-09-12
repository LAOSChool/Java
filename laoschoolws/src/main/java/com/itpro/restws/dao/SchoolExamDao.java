package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.User;


public interface SchoolExamDao {

	int countBySchool(Integer school_id);
	SchoolExam findById(Integer id);
	
	SchoolExam findByExKey(Integer school_id, String ex_key);
	
	List<SchoolExam> findByMonth(Integer school_id,Integer ex_year, Integer ex_month) ;
	
	List<SchoolExam> findByExamType(Integer school_id,Integer ex_type, Integer term_val) ;
	
	List<SchoolExam> findBySchool(Integer school_id,int from_row,int max_result) ;
	
	void saveSchoolExam(User me,SchoolExam schoolExam);
	void updateSchoolExam(User me,SchoolExam schoolExam);
	void setFlushMode(FlushMode mode);
	void clearChange();
	
		
}

