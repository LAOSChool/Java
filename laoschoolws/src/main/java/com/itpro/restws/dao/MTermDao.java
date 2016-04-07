package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MTerm;
public interface MTermDao {
	int countBySchool(int school_id);
	MTerm findById(int id);
	List<MTerm> findBySchool(int school_id,int from_row,int max_result) ;
	void saveTerm(MTerm mterm);
	void updateTerm(MTerm mterm);
}
