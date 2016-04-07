package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.FinalResult;

public interface FinalResultService {
	
	FinalResult findById(int id);
	int countBySchoolID(int school_id);
	int countByClassID(int class_id);
	int countByStudentID(int user_id);
	ArrayList<FinalResult> findBySchool(int school_id,int from_num, int max_result);
	ArrayList<FinalResult> findByClass(int class_id,int from_num, int max_result);
	ArrayList<FinalResult> findByStudent(int user_id,int from_num, int max_result);
	 
	
	
	FinalResult insertFinalResult(FinalResult finalResult);
	FinalResult updateFinalResult(FinalResult finalResult);
	
}