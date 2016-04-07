package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.EClass;


public interface ClassDao {

	int countClassBySchool(int school_id);
	EClass findById(int id);
	List<EClass> findBySchool(int school_id,int from_row,int max_result) ;
	List<EClass> findByUser(int user_id,int from_row,int max_result) ;
	void saveClass(EClass eClass);
	void updateClass(EClass eClass);

}

