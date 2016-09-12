package com.itpro.restws.dao;

import java.util.ArrayList;

import org.hibernate.FlushMode;

import com.itpro.restws.model.EduProfile;
import com.itpro.restws.model.User;


public interface EduProfileDao {
	
	EduProfile findByID(Integer id);
	ArrayList<EduProfile> findBySchoolID(Integer school_id,int from_row, int max_result); 
	
	ArrayList<EduProfile> findByStudentID(Integer student_id);
	ArrayList<EduProfile> findEx(Integer student_id, Integer school_id, Integer class_id, Integer year_id);
	EduProfile findLatestProfile(Integer student_id, Integer school_id);
	
	
	
	void saveStudentProfile(User me, EduProfile profile);
	void updateStudentProfile(User me, EduProfile profile);
	void setFlushMode(FlushMode mode);
	void clearChange();
}

