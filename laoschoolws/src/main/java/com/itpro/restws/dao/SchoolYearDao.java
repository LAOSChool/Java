package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SchoolYear;


public interface SchoolYearDao {

	SchoolYear findById(Integer id);
	List<SchoolYear> findBySchoolId(Integer school_id);
	List<SchoolYear> findBySchoolAndYear(Integer school_id, Integer year_id);
	//SchoolYear findLastestOfSchoolId(Integer school_id);
	
	void saveSchoolYear(SchoolYear schoolYear);
	void updateSchoolYear(SchoolYear schoolYear);
	 List<SchoolYear> findFromOrTo(Integer school_id, Integer frm_year, Integer to_year) ;
}

