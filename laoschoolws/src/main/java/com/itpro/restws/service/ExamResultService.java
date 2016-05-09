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
	
	
	
	int countExamResultExt(Integer school_id,Integer class_id,Integer student_id,Integer from_row_id,String from_dt, String to_dt);
	ArrayList<ExamResult> findExamResultExt(Integer school_id,Integer class_id,Integer student_id,Integer from_row_id,int from_num, int max_result, String from_dt, String to_dt);
	
	
	ExamResult insertExamResult(ExamResult examResult);
	ExamResult updateExamResult(ExamResult examResult);
	 
}