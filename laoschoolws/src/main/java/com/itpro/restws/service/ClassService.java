package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.EClass;

public interface ClassService {
	
	EClass findById(int id);
	int countBySchoolID(int school_id);
	ArrayList<EClass> findBySchool(int school_id,int from_num, int max_result);
	ArrayList<EClass> findByUser(int user_id,int from_num, int max_result);
	
	
	
	EClass insertClass(EClass eClass);
	EClass updateClass(EClass eClass);
	 
}