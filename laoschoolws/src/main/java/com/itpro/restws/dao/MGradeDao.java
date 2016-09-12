package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MGrade;
import com.itpro.restws.model.User;


public interface MGradeDao {

	int countBySchool(Integer school_id);
	MGrade findById(Integer id);
	List<MGrade> findBySchool(Integer school_id,int from_row,int max_result) ;
	
	void saveGrade(User me,MGrade mgrade);
	void updateGrade(User me,MGrade mgrade);
	void delGrade(User me,MGrade mgrade);
}

