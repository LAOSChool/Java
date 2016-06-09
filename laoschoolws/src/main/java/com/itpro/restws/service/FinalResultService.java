package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.FinalResult;
import com.itpro.restws.model.StudentProfile;
import com.itpro.restws.model.User;

public interface FinalResultService {
	
	FinalResult findById(Integer id);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	int countByStudentID(Integer user_id);
	ArrayList<FinalResult> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<FinalResult> findByClass(Integer class_id,int from_num, int max_result);
	ArrayList<FinalResult> findByStudent(Integer user_id,int from_num, int max_result);
	 
	
	
	FinalResult insertFinalResult(FinalResult finalResult);
	FinalResult updateFinalResult(FinalResult finalResult);
	ArrayList<StudentProfile> findUserProfile(User student, Integer school_year);
	
}