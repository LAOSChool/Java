package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.School;


public interface SchoolDao {

	School findById(int id);
	List<School> findAll() ;
//	School create(School sh);
	void saveSchool(School school);
	void updateSchool(School school);
}

