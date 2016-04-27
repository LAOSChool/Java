package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.EClass;

public interface ClassService {
	
	EClass findById(Integer id);
	int countBySchoolID(Integer school_id);
	ArrayList<EClass> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<EClass> findByUser(Integer user_id,int from_num, int max_result);
	
	
	
	EClass insertClass(EClass eClass);
	EClass updateClass(EClass eClass);
	 
}