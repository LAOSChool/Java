package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.FinalResult;


public interface FinalResultDao {

	int countBySchool(int school_id);
	int countByClass(int class_id);
	int countByUser(int user_id);
	FinalResult findById(int id);
	List<FinalResult> findBySchool(int school_id,int from_row,int max_result) ;
	List<FinalResult> findByClass(int class_id,int from_row,int max_result) ;
	List<FinalResult> findByStudent(int user_id,int from_row,int max_result) ;
	void saveFinalResult(FinalResult finalResult);
	void updateFinalResult(FinalResult finalResult);
}

