package com.itpro.restws.dao;

import java.util.ArrayList;

import com.itpro.restws.model.StudentProfile;


public interface StudentProfileDao {
	
	StudentProfile findByID(Integer id);
	ArrayList<StudentProfile> findBySchoolID(Integer school_id,int from_row, int max_result); 
	
	ArrayList<StudentProfile> findByStudentID(Integer id);
	ArrayList<StudentProfile> findEx(Integer student_id, Integer school_id, Integer class_id, Integer year_id);

	void saveStudentProfile(StudentProfile profile);
	void updateStudentProfile(StudentProfile profile);
}

