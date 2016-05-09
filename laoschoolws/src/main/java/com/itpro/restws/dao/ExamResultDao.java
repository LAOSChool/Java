package com.itpro.restws.dao;

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
	
	void saveExamResult(ExamResult examResult);
	void updateExamResult(ExamResult examResult);
	
	
	int countExamExt(Integer school_id,Integer class_id,Integer student_id,Integer from_row_id, String from_dt,String to_dt);
	List<ExamResult> findExamExt(Integer school_id,Integer class_id,Integer student_id,Integer from_row_id,int from_num, int max_result,String from_dt, String to_dt);

	
}

