package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.Attendance;

public interface AttendanceService {
	
	Attendance findById(int id);
	int countBySchoolID(int school_id);
	int countByClassID(int class_id);
	int countByUserID(int user_id);
	ArrayList<Attendance> findBySchool(int school_id,int from_num, int max_result);
	ArrayList<Attendance> findByClass(int class_id,int from_num, int max_result);
	ArrayList<Attendance> findByUser(int user_id,int from_num, int max_result);
	
	Attendance insertAttendance(Attendance attendance);
	Attendance updateAttendance(Attendance attendance);
	 
}