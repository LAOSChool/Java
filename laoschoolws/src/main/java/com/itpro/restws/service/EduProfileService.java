package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.EduProfile;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

public interface EduProfileService {
	ArrayList<SchoolYear> findSchoolYearByStudentID(Integer student_id);
	ArrayList<EduProfile> getUserProfile(User student, Integer year_id);
	ArrayList<EduProfile> getClassProfile(User me, Integer class_id, Integer student_id, Integer year_id);
	
	
}