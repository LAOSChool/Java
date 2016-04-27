package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.FinalResult;


public interface FinalResultDao {

	int countBySchool(Integer school_id);
	int countByClass(Integer class_id);
	int countByUser(Integer user_id);
	FinalResult findById(Integer id);
	List<FinalResult> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<FinalResult> findByClass(Integer class_id,int from_row,int max_result) ;
	List<FinalResult> findByStudent(Integer user_id,int from_row,int max_result) ;
	void saveFinalResult(FinalResult finalResult);
	void updateFinalResult(FinalResult finalResult);
}

