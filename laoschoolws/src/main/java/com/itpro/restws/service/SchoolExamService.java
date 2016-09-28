package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.User;

public interface SchoolExamService {
	
	SchoolExam findById(Integer id);
	ArrayList<SchoolExam> findBySchool(Integer school_id);
	
	SchoolExam insertSchoolExam(User me, SchoolExam schoolExam);
	
	SchoolExam updateSchoolExam(User me,SchoolExam schoolExam);
	
	boolean valid_ex_key(User me,String ex_key);
	
	void delById(User user,Integer id);
	 
}