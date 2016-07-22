package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.Timetable;
import com.itpro.restws.model.User;

public interface TimetableService {
	
	Timetable findById(Integer id);
	int countBySchoolID(Integer school_id);
	ArrayList<Timetable> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<Timetable> findByClass(Integer class_id,int from_num, int max_result);
	
	
	
	ArrayList<MSubject> findSubjectOfClass(Integer class_id);
	
	
	Timetable insertTimetable(User user,Timetable timetable);
	Timetable updateTimetable(User user, Timetable timetable);
	void delTimetableById(User user , Integer id);
	
	ArrayList<Timetable>  findByDate(Integer class_id,String dt);	
	
}