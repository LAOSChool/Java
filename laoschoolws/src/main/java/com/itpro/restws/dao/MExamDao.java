package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MExam;


public interface MExamDao {

	int countBySchool(Integer school_id);
	MExam findById(Integer id);
	List<MExam> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveExam(MExam mexam);
	void updateExam(MExam mexam) ;
	
}

