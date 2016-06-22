package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.SchoolYear;

public interface EduProfileService {
	ArrayList<SchoolYear> findSchoolYearByStudentID(Integer student_id);
	
}