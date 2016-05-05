package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.Attendance;

public interface AttendanceService {
	
	Attendance findById(Integer id);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	int countByUserID(Integer user_id);
	
	ArrayList<Attendance> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<Attendance> findByClass(Integer class_id,int from_num, int max_result);
	ArrayList<Attendance> findByUser(Integer user_id,int from_num, int max_result);
	
	int countAttendanceExt(Integer school_id,Integer class_id,Integer user_id,Integer from_row_id);
	ArrayList<Attendance> findAttendanceExt(Integer school_id,Integer class_id,Integer user_id,Integer from_row_id,int from_num, int max_result);
	
	Attendance insertAttendance(Attendance attendance);
	Attendance updateAttendance(Attendance attendance);
	 
}