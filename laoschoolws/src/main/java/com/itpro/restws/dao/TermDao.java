package com.itpro.restws.dao;

import java.util.ArrayList;

import com.itpro.restws.model.Term;


public interface TermDao {

	int countBySchool(Integer school_id);
	Term findById(Integer id);
	ArrayList<Term> findBySchool(Integer school_id,int from_row,int max_result) ;
	 ArrayList<Term> getLatestTerm(Integer school_id) ;
	
	void saveTerm(Term term);
	void updateTerm(Term term);

}

