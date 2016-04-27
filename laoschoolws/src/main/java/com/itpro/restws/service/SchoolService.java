package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.School;

public interface SchoolService {
	
	School findById(Integer id);
	ArrayList<School> findActive();
	
	School insertSchool(School school);
	School updateSchool(School school);
	 
}