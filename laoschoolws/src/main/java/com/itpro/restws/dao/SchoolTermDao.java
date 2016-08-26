package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.SchoolTerm;


public interface SchoolTermDao {

	SchoolTerm findById(Integer id);
	List<SchoolTerm> findBySchoolId(Integer school_id,Integer active);
	List<SchoolTerm> findBySchoolAndYear(Integer school_id, Integer year_id, Integer actived);
	
	void saveSchoolTerm(SchoolTerm schoolTerm);
	void updateSchoolTerm(SchoolTerm schoolTerm);
	
	void setFlushMode(FlushMode mode);
	void clearChange();
	int countTermsBySchool(Integer school_id, Integer actived);	
	 
}

