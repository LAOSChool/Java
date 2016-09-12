package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.Timetable;
import com.itpro.restws.model.User;


public interface TimetableDao {

	int countBySchool(Integer school_id);
	int countTimetableExt(Integer school_id, Integer class_id,Integer weekday_id, Integer session_id,Integer year_id, Integer term_val);
	List<Timetable> findTimetableExt(Integer school_id, Integer class_id,Integer weekday_id, Integer session_id,Integer year_id, Integer term_val);
	
	Timetable findById(Integer id);
	
	List<Timetable> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<Timetable> findByClass(Integer class_id,int from_row,int max_result) ;
	List<Timetable> findByWeekDay(Integer class_id,Integer weekday_id) ;
	
	void saveTimeTable(User me,Timetable timetable);
	void updateTimetable(User me,Timetable timetable);
	void delTimetable(User me,Timetable timetable);
	void setFlushMode(FlushMode mode);
	void clearChange();
}

