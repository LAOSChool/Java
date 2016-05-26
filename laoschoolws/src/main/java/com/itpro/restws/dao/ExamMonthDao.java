package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.ExamMonth;


public interface ExamMonthDao {

	int countBySchool(Integer school_id);
	ExamMonth findById(Integer id);
	
	List<ExamMonth> findByMonth(Integer school_id,Integer ex_year, Integer ex_month) ;
	
	List<ExamMonth> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveExamMonth(ExamMonth examMonth);
	void updateExamMonth(ExamMonth examMonth);
}

