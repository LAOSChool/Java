package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MGrade;


public interface MGradeDao {

	int countBySchool(Integer school_id);
	MGrade findById(Integer id);
	List<MGrade> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveGrade(MGrade mgrade);
	
	void updateGrade(MGrade mgrade);
	void delGrade(MGrade mgrade);
}

