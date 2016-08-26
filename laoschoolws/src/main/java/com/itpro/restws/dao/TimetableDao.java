package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.Timetable;


public interface TimetableDao {

	int countBySchool(Integer school_id);
	int countTimetableExt(Integer school_id, Integer class_id,Integer weekday_id, Integer session_id,Integer year_id, Integer term_val);
	List<Timetable> findTimetableExt(Integer school_id, Integer class_id,Integer weekday_id, Integer session_id,Integer year_id, Integer term_val);
	
	Timetable findById(Integer id);
	
	List<Timetable> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<Timetable> findByClass(Integer class_id,int from_row,int max_result) ;
	List<Timetable> findByWeekDay(Integer class_id,Integer weekday_id) ;
	
	void saveTimeTable(Timetable timetable);
	void updateTimetable(Timetable timetable);
	void delTimetable(Timetable timetable);
	void setFlushMode(FlushMode mode);
	void clearChange();
}

