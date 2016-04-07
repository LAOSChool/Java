package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Timetable;


public interface TimetableDao {

	int countBySchool(int school_id);
	Timetable findById(int id);
	
	List<Timetable> findBySchool(int school_id,int from_row,int max_result) ;
	List<Timetable> findByClass(int class_id,int from_row,int max_result) ;
	void saveTimeTable(Timetable timetable);
	void updateTimetable(Timetable timetable);
}

