package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MYear;


public interface MYearDao {

	int countBySchool(Integer school_id);
	MYear findById(Integer id);
	List<MYear> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveYear(MYear myear);
	void updateYear(MYear myear) ;
	
}

