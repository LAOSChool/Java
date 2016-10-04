package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.Timetable;
import com.itpro.restws.model.User;

public interface TimetableService {
	
	Timetable findById(Integer id);
	int countBySchoolID(Integer school_id);
	ArrayList<Timetable> findBySchool(Integer school_id,int from_num, int max_result);
	int countTimetableExt (Integer school_id, Integer class_id,Integer weekday_id, Integer session_id,Integer year_id, Integer term_val);
	ArrayList<Timetable> findTimetableExt(Integer school_id, Integer class_id,Integer weekday_id, Integer session_id,Integer year_id, Integer term_val);
	
	ArrayList<Timetable> findByClass(Integer class_id,int from_num, int max_result);
	
	
	
	ArrayList<MSubject> findSubjectOfClass(Integer class_id);
	
	
	Timetable insertTimetable(User user,Timetable timetable);
	Timetable updateTransTimetable(User user, Timetable timetable);
	void delTimetableById(User user , Integer id);
	
	Timetable reloadTimetable(Timetable timetable);
	
	ArrayList<Timetable>  findByDate(Integer class_id,String dt);	
	
}