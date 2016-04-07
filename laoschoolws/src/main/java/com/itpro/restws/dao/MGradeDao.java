package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MGrade;


public interface MGradeDao {

	int countBySchool(int school_id);
	MGrade findById(int id);
	List<MGrade> findBySchool(int school_id,int from_row,int max_result) ;
	void saveGrade(MGrade mgrade);
	
	public void updateGrade(MGrade mgrade);
}

