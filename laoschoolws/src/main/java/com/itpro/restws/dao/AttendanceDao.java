package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.Attendance;


public interface AttendanceDao {

	int countAttendanceBySchool(Integer school_id);
	int countAttendanceByClass(Integer class_id);
	int countAttendanceByStudent(Integer user_id);
	Attendance findById(Integer id);
	
	List<Attendance> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<Attendance> findByClass(Integer class_id,int from_row,int max_result) ;
	List<Attendance> findByStudent(Integer student_id,int from_row,int max_result) ;
	List<Attendance> findByAuditor(Integer auditor_id,int from_row,int max_result) ;
	
	int countAttendanceExt(Integer school_id,Integer class_id,Integer student_id,Integer from_row_id, String att_dt,String from_dt,String to_dt,Integer session_id,Integer term_val, Integer year_id);
	List<Attendance> findAttendanceExt(Integer school_id,Integer class_id,Integer student_id,Integer from_row_id,int from_num, int max_result,String att_dt,String from_dt, String to_dt,Integer session_id, Integer term_val, Integer year_id);
	
	void saveAttendance(Attendance attendance);
	void updateAttendance(Attendance attendance);
	
	void setFlushMode(FlushMode mode);
	void clearChange();
	
}

