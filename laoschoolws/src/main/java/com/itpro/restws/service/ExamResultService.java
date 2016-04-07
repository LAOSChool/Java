package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.ExamResult;

public interface ExamResultService {
	
	ExamResult findById(int id);
	int countBySchoolID(int school_id);
	int countByClassID(int class_id);
	int countByStudentID(int user_id);
	ArrayList<ExamResult> findBySchool(int school_id,int from_num, int max_result);
	ArrayList<ExamResult> findByClass(int class_id,int from_num, int max_result);
	ArrayList<ExamResult> findByStudent(int user_id,int from_num, int max_result);
	
	
	ExamResult insertExamResult(ExamResult examResult);
	ExamResult updateExamResult(ExamResult examResult);
	 
}