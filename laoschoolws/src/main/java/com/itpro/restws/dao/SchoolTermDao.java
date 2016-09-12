package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.User;


public interface SchoolTermDao {

	SchoolTerm findById(Integer id);
	List<SchoolTerm> findBySchoolId(Integer school_id,Integer active);
	List<SchoolTerm> findBySchoolAndYear(Integer school_id, Integer year_id, Integer actived);
	
	void saveSchoolTerm(User me,SchoolTerm schoolTerm);
	void updateSchoolTerm(User me,SchoolTerm schoolTerm);
	
	void setFlushMode(FlushMode mode);
	void clearChange();
	int countTermsBySchool(Integer school_id, Integer actived);	
	 
}

