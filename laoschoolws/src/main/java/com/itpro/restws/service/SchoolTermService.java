package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.User;

public interface SchoolTermService {
	
	SchoolTerm findById(Integer id);
	ArrayList<SchoolTerm> findBySchool(Integer school_id);
	
	
	SchoolTerm insertSchoolTerm(User me,SchoolTerm SchoolTerm);
	SchoolTerm updateTransSchoolTerm(User me,SchoolTerm SchoolTerm);
	
	SchoolTerm activeSchoolTerm(User me,Integer term_id, Integer active);
	
	
	void delSchoolTerm(User me, Integer id);
///////////////////////// TERM 
	ArrayList<SchoolTerm> findAllTermByYear(Integer school_id,Integer year_id);
	ArrayList<SchoolTerm> findAllTermBySchool(Integer school_id);
	
	SchoolTerm findMaxActiveTermBySchool(Integer school_id);
	SchoolTerm findTermById(User me, Integer term_id);
	boolean valid_term_val(Integer school_id, Integer year_id, Integer term_val);
	ArrayList<SchoolTerm> findTermExt(User me, Integer filter_year_id, Integer filter_actived);
	 
}