package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.Timetable;

public interface TimetableService {
	
	Timetable findById(Integer id);
	int countBySchoolID(Integer school_id);
	ArrayList<Timetable> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<Timetable> findByClass(Integer class_id,int from_num, int max_result);
	
	
	Timetable insertTimetable(Timetable timetable);
	Timetable updateTimetable(Timetable timetable);
	
	ArrayList<Timetable>  findByDate(Integer class_id,String dt);	
	
}