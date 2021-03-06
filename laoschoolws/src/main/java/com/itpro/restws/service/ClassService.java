package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.EClass;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

public interface ClassService {
	
	EClass findById(Integer id);
	int countBySchoolID(Integer school_id);
	ArrayList<EClass> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<EClass> findActiveBySchool(Integer school_id);
	
	ArrayList<EClass> findByUser(Integer user_id,int from_num, int max_result);
	
	ArrayList<SchoolExam> findExamOfClass(User current_user,Integer class_id, SchoolTerm term);
	
	void updateTermVal(EClass eClass);
	void updateTermVals(ArrayList<EClass> classes);
	 
	SchoolTerm getCurrentTerm(EClass eClass);
	 
	EClass newClass(User admin, EClass eClass);
	EClass delClass(User admin, Integer class_id);
	
	EClass updateTransClass(User admin, EClass eClass);
	SchoolYear getSchoolYear(EClass eClass);
	
}