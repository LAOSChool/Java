package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;


public interface SchoolYearDao {

	SchoolYear findById(Integer id);
	List<SchoolYear> findBySchoolId(Integer school_id);
	List<SchoolYear> findBySchoolAndYear(Integer school_id, Integer year_id);
	//SchoolYear findLastestOfSchoolId(Integer school_id);
	
	void saveSchoolYear(User me,SchoolYear schoolYear);
	void updateSchoolYear(User me,SchoolYear schoolYear);
	 List<SchoolYear> findFromOrTo(Integer school_id, Integer frm_year, Integer to_year) ;
	 void setFlushMode(FlushMode mode);
		void clearChange();
}

