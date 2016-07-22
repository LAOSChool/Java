package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Timetable;


public interface TimetableDao {

	int countBySchool(Integer school_id);
	Timetable findById(Integer id);
	
	List<Timetable> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<Timetable> findByClass(Integer class_id,int from_row,int max_result) ;
	List<Timetable> findByWeekDay(Integer class_id,Integer weekday_id) ;
	
	void saveTimeTable(Timetable timetable);
	void updateTimetable(Timetable timetable);
	void delTimetable(Timetable timetable);
}

