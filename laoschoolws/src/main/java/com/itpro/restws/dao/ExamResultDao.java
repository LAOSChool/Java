package com.itpro.restws.dao;

import java.util.ArrayList;
import java.util.List;

import com.itpro.restws.model.ExamResult;


public interface ExamResultDao {

	int countExamBySchool(Integer school_id);
	int countExamBySclass(Integer class_id);
	int countExamByUser(Integer user_id);
	ExamResult findById(Integer id);
	List<ExamResult> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<ExamResult> findByClass(Integer class_id,int from_row,int max_result) ;
	List<ExamResult> findByStudent(Integer user_id,int from_row,int max_result) ;
	
	
	List<ExamResult> findSameExam(
			Integer school_id,
			Integer student_id, 
			Integer class_id, 
			Integer subject_id,
			Integer exam_id, 
			Integer term_id,
			Integer exam_year, 
			Integer school_year_id);
	
	void saveExamResult(ExamResult examResult);
	void updateExamResult(ExamResult examResult);
	void deleteExamResult(ExamResult examResult);
	
	
	
	int countExamResultExt(Integer school_id, Integer class_id, Integer user_id, Integer subject_id,
			Integer term_id, Integer exam_year, Integer exam_month, String exam_dt, String dateFrom, String dateTo,
			Integer from_row_id,Integer exam_type, Integer sch_year_id) ;

	
	ArrayList<ExamResult> findExamResultExt(Integer school_id, int from_row, int max_result, Integer class_id,
			Integer user_id, Integer subject_id, Integer term_id, Integer exam_year, Integer exam_month,
			String exam_dt, String dateFrom, String dateTo, Integer from_row_id,Integer exam_type, Integer sch_year_id) ;

	
}

