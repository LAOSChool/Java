package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.User;

public interface SchoolExamService {
	
	SchoolExam findById(Integer id);
	ArrayList<SchoolExam> findBySchool(Integer school_id);
	SchoolExam findBySchoolAndKey(Integer school_id, String ex_key);
	
	SchoolExam insertSchoolExam(User me, SchoolExam schoolExam);
	
	SchoolExam updateTransSchoolExam(User me,SchoolExam schoolExam);
	
	boolean valid_ex_key(User me,String ex_key);
	
	void delById(User user,Integer id);
	 
}