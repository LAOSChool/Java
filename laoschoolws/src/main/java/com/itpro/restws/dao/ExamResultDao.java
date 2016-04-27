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
}

