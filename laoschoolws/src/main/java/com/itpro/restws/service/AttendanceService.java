package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.Attendance;
import com.itpro.restws.model.User;

public interface AttendanceService {
	
	Attendance findById(Integer id);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	int countByStudent(Integer user_id);
	
	ArrayList<Attendance> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<Attendance> findByClass(Integer class_id,int from_num, int max_result);
	ArrayList<Attendance> findByStudent(Integer user_id,int from_num, int max_result);
	
	int countAttendanceExt(Integer school_id,Integer class_id,Integer student_id,Integer from_row_id,String att_dt,String from_dt, String to_dt);
	ArrayList<Attendance> findAttendanceExt(Integer school_id,Integer class_id,Integer student_id,Integer from_row_id,int from_num, int max_result, String att_dt,String from_dt, String to_dt);
	
	
	Attendance insertAttendance(User teacher,Attendance attendance);
	Attendance updateAttendance(User teacher,Attendance attendance);
	Attendance requestAttendance(User student, Attendance attendance);
	 
}