package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Attendance;


public interface AttendanceDao {

	int countAttendanceBySchool(int school_id);
	int countAttendanceByClass(int class_id);
	int countAttendanceByUser(int user_id);
	Attendance findById(int id);
	List<Attendance> findBySchool(int school_id,int from_row,int max_result) ;
	List<Attendance> findByClass(int class_id,int from_row,int max_result) ;
	List<Attendance> findByUser(int user_id,int from_row,int max_result) ;
	void saveAttendance(Attendance attendance);
	void updateAttendance(Attendance attendance);
}

