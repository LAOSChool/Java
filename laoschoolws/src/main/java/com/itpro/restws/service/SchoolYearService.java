package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.helper.SchoolTerm;
import com.itpro.restws.model.SchoolYear;

public interface SchoolYearService {
	
	SchoolYear findById(Integer id);
	ArrayList<SchoolYear> findBySchool(Integer school_id);
	ArrayList<SchoolYear> findByStudent(Integer user_id);
	SchoolYear findLatestYearBySchool(Integer school_id);
	SchoolYear findLatestYearByStudent(Integer user_id);
	
	SchoolYear findByClass(Integer class_id);	
	
	
	SchoolYear insertSchoolYear(SchoolYear schoolYear);
	SchoolYear updateSchoolYear(SchoolYear schoolYear);
///////////////////////// TERM 
	ArrayList<SchoolTerm> findTermByYear(Integer school_id,Integer year_id);
	ArrayList<SchoolTerm> findTermBySchool(Integer school_id);
	SchoolTerm findLatestTermBySchool(Integer school_id);
	 
}