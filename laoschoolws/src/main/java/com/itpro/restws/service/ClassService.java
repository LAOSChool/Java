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
	ArrayList<EClass> findByUser(Integer user_id,int from_num, int max_result);
	
	ArrayList<SchoolExam> findExamOfClass(User current_user,Integer class_id, SchoolTerm term);
	
	
	
	EClass insertClass(EClass eClass);
	EClass updateClass(EClass eClass);
	SchoolYear getSchoolYear(EClass eClass);
}