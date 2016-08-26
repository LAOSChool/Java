package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.School;


public interface SchoolDao {

	School findById(Integer id);
	List<School> findAll() ;
//	School create(School sh);
	void saveSchool(School school);
	void updateSchool(School school);
	void setFlushMode(FlushMode mode);
	void clearChange();
}

