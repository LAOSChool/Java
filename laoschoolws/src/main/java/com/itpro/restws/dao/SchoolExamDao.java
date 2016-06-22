package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SchoolExam;


public interface SchoolExamDao {

	int countBySchool(Integer school_id);
	SchoolExam findById(Integer id);
	
	List<SchoolExam> findByMonth(Integer school_id,Integer ex_year, Integer ex_month) ;
	
	List<SchoolExam> findByEx(Integer school_id,Integer ex_type, Integer term_val) ;
	
	List<SchoolExam> findBySchool(Integer school_id,int from_row,int max_result) ;
	
	void saveSchoolExam(SchoolExam schoolExam);
	void updateSchoolExam(SchoolExam schoolExam);
}

