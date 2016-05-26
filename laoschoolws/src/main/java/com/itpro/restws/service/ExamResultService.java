package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.User;

public interface ExamResultService {
	

	ExamResult findById(Integer id);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	int countByStudentID(Integer user_id);
	// Get list result
	ArrayList<ExamResult> findBySchoolID(Integer school_id,int from_num, int max_result);
	ArrayList<ExamResult> findByClassID(Integer class_id,int from_num, int max_result);
	ArrayList<ExamResult> findByStudentID(Integer user_id,int from_num, int max_result);

	// Input/delete
	void deleteExamResult(ExamResult exam);
	void validUpdateExam(User teacher,ExamResult examResult,boolean is_update);
	ExamResult updateExamResult(ExamResult examResult); 
	ExamResult inputExam(ExamResult examResult);
	// User Profile
	//int countUserProfile(User user,Integer class_id);
	ArrayList<ExamResult> findUserProfile(User user,Integer class_id);

	
	//	ArrayList<ExamResult> findByUser(User user, int from_num, int max_result);
	int countExamResultExt(Integer school_id,
			Integer class_id, 
			Integer student_id, 
			Integer subject_id,
			Integer term_id,
			Integer exam_year,
			Integer exam_month,
			String exam_dt,
			String dateFrom, 
			String dateTo,
			Integer from_row_id,
			Integer exam_type
			);
	ArrayList<ExamResult>  findExamResultExt(Integer school_id, int from_row, int max_result,
			Integer class_id, 
			Integer student_id, 
			Integer subject_id,
			Integer term_id,
			Integer exam_year,
			Integer exam_month,
			String exam_dt,
			String dateFrom, 
			String dateTo,
			Integer from_row_id,
			Integer exam_type);
	
	void initStudentExamResult(User user,Integer class_id);
	
	 
}