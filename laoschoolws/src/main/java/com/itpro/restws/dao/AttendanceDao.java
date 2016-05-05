package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Attendance;


public interface AttendanceDao {

	int countAttendanceBySchool(Integer school_id);
	int countAttendanceByClass(Integer class_id);
	int countAttendanceByUser(Integer user_id);
	Attendance findById(Integer id);
	List<Attendance> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<Attendance> findByClass(Integer class_id,int from_row,int max_result) ;
	List<Attendance> findByUser(Integer user_id,int from_row,int max_result) ;
	
	int countAttendanceExt(Integer school_id,Integer class_id,Integer user_id,Integer from_row_id);
	List<Attendance> findAttendanceExt(Integer school_id,Integer class_id,Integer user_id,Integer from_row_id,int from_num, int max_result);
	
	void saveAttendance(Attendance attendance);
	void updateAttendance(Attendance attendance);
}

