package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MExamTime;


public interface MExamTimeDao {

	int countBySchool(Integer school_id);
	MExamTime findById(Integer id);
	List<MExamTime> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveExamTime(MExamTime mexamTime);
	void updateExamTime(MExamTime mexamTime) ;
	
}

