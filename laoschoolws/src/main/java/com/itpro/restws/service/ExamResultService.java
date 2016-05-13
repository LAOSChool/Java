package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.List;

import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.User;

public interface ExamResultService {
	
	ExamResult findById(Integer id);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	int countByStudentID(Integer user_id);
	ArrayList<ExamResult> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<ExamResult> findByClass(Integer class_id,int from_num, int max_result);
	ArrayList<ExamResult> findByStudent(Integer user_id,int from_num, int max_result);
	
	
	void validUpdateExam(User teacher,ExamResult examResult,boolean is_update);
	ExamResult updateExamResult(ExamResult examResult); 
	ExamResult inputExam(ExamResult examResult);// new or update
	 
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
			Integer from_row_id);
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
			Integer from_row_id);
}