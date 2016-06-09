package com.itpro.restws.dao;

import java.util.ArrayList;

import com.itpro.restws.model.SchoolTerm;


public interface TermDao {

	int countBySchool(Integer school_id);
	SchoolTerm findById(Integer id);
	ArrayList<SchoolTerm> findBySchool(Integer school_id,int from_row,int max_result) ;
	 //ArrayList<SchoolTerm> getLatestTerm(Integer school_id) ;
	SchoolTerm getCurrentTerm(Integer school_id);	
	void saveTerm(SchoolTerm term);
	void updateTerm(SchoolTerm term);

}

