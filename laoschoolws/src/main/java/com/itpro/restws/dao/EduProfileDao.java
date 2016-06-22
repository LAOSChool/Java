package com.itpro.restws.dao;

import java.util.ArrayList;

import com.itpro.restws.model.EduProfile;
import com.itpro.restws.model.SchoolYear;


public interface EduProfileDao {
	
	EduProfile findByID(Integer id);
	ArrayList<EduProfile> findBySchoolID(Integer school_id,int from_row, int max_result); 
	
	ArrayList<EduProfile> findByStudentID(Integer student_id);
	ArrayList<EduProfile> findEx(Integer student_id, Integer school_id, Integer class_id, Integer year_id);
	EduProfile findLatestProfile(Integer student_id, Integer school_id);
	
	
	
	void saveStudentProfile(EduProfile profile);
	void updateStudentProfile(EduProfile profile);
}

