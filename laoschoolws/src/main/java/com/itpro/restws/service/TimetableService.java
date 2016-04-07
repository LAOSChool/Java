package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.Timetable;

public interface TimetableService {
	
	Timetable findById(int id);
	int countBySchoolID(int school_id);
	ArrayList<Timetable> findBySchool(int school_id,int from_num, int max_result);
	ArrayList<Timetable> findByClass(int class_id,int from_num, int max_result);
	
	
	Timetable insertTimetable(Timetable timetable);
	Timetable updateTimetable(Timetable timetable);
}