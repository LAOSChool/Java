package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MExam;


public interface MExamDao {

	int countBySchool(int school_id);
	MExam findById(int id);
	List<MExam> findBySchool(int school_id,int from_row,int max_result) ;
	void saveExam(MExam mexam);
	void updateExam(MExam mexam) ;
	
}

