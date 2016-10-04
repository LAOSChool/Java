package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

public interface SchoolYearService {
	
	SchoolYear findById(Integer id);
	ArrayList<SchoolYear> findBySchool(Integer school_id);
	ArrayList<SchoolYear> findByStudent(Integer user_id);
	SchoolYear findLatestYearBySchool(Integer school_id);
	SchoolYear findLatestYearByStudent(Integer user_id);
	
	SchoolYear findByClass(Integer class_id);	
	
	
	SchoolYear insertSchoolYear(User user,SchoolYear schoolYear);
	SchoolYear updateTransSchoolYear(User user,SchoolYear schoolYear);
	void delSchoolYear(User user, Integer id);
	boolean valid_year_id(Integer school_id,Integer year_id);
///////////////////////// TERM 
//	ArrayList<SchoolTerm> findAllTermByYear(Integer school_id,Integer year_id);
//	ArrayList<SchoolTerm> findAllTermBySchool(Integer school_id);
//	SchoolTerm findMaxActiveTermBySchool(Integer school_id);
//	SchoolTerm findMaxActiveTermByYear(Integer school_id, Integer year_id) ;
//	SchoolTerm findTermById(User me, Integer term_id);
//	boolean valid_term_val(Integer school_id,Integer year_id, Integer term_val);	
//	SchoolTerm insertSchoolTerm(User me,SchoolTerm schoolTerm);
//	SchoolTerm updateSchoolTerm(User me,SchoolTerm schoolTerm);
	
	 
}