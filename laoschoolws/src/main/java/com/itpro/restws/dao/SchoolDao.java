package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.School;
import com.itpro.restws.model.User;


public interface SchoolDao {

	School findById(Integer id);
	List<School> findAll() ;
//	School create(School sh);
	void saveSchool(User me,School school);
	void updateSchool(User me,School school);
	void setFlushMode(FlushMode mode);
	void clearChange();
}

