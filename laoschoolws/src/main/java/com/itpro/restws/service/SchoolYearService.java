package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.helper.SchoolTerm;
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
	SchoolYear updateSchoolYear(User user,SchoolYear schoolYear);
	void delSchoolYear(User user, Integer id);
///////////////////////// TERM 
	ArrayList<SchoolTerm> findTermByYear(Integer school_id,Integer year_id);
	ArrayList<SchoolTerm> findTermBySchool(Integer school_id);
	SchoolTerm findLatestTermBySchool(Integer school_id);
	
	boolean valid_year_id(Integer school_id,Integer year_id);
	boolean valid_term_val(Integer school_id,Integer year_id, Integer term_val);
	 
}