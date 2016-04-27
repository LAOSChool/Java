package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.ExamResult;

public interface ExamResultService {
	
	ExamResult findById(Integer id);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	int countByStudentID(Integer user_id);
	ArrayList<ExamResult> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<ExamResult> findByClass(Integer class_id,int from_num, int max_result);
	ArrayList<ExamResult> findByStudent(Integer user_id,int from_num, int max_result);
	
	
	ExamResult insertExamResult(ExamResult examResult);
	ExamResult updateExamResult(ExamResult examResult);
	 
}