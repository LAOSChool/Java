package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.ExamResult;


public interface ExamResultDao {

	int countExamBySchool(int school_id);
	int countExamBySclass(int class_id);
	int countExamByUser(int user_id);
	ExamResult findById(int id);
	List<ExamResult> findBySchool(int school_id,int from_row,int max_result) ;
	List<ExamResult> findByClass(int class_id,int from_row,int max_result) ;
	List<ExamResult> findByStudent(int user_id,int from_row,int max_result) ;
	
	void saveExamResult(ExamResult examResult);
	void updateExamResult(ExamResult examResult);
}

