package com.itpro.restws.helper;

import java.util.ArrayList;

import com.itpro.restws.model.Attendance;
import com.itpro.restws.model.Timetable;
import com.itpro.restws.model.User;

public class RollUp {
	ArrayList<Attendance> attendances;
	ArrayList<User> students; 
	public ArrayList<User> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<User> students) {
		this.students = students;
	}

	public ArrayList<Attendance> getAttendances() {
		return attendances;
	}
	
	public void setAttendances(ArrayList<Attendance> attendances) {
		this.attendances = attendances;
	}

	ArrayList<Timetable> timetables;
	public ArrayList<Timetable> getTimetables() {
		return timetables;
	}

	public void setTimetables(ArrayList<Timetable> timetables) {
		this.timetables = timetables;
	}
}