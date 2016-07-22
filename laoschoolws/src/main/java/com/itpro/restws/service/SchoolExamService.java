package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.User;

public interface SchoolExamService {
	
	SchoolExam findById(Integer id);
	ArrayList<SchoolExam> findBySchool(Integer school_id);
	
	SchoolExam insertSchoolExam(User user, SchoolExam schoolExam);
	SchoolExam updateSchoolExam(User user,SchoolExam schoolExam);
	void delById(User user,Integer id);
	 
}