package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.AttendanceDao;
import com.itpro.restws.model.Attendance;

@Service("attendanceService")
@Transactional
public class AttendanceServiceImpl implements AttendanceService{

	@Autowired
	private AttendanceDao attendanceDao;

	@Override
	public Attendance findById(int id) {
		return attendanceDao.findById(id);
	}

	@Override
	public int countBySchoolID(int school_id) {
		return attendanceDao.countAttendanceBySchool(school_id);
	}

	@Override
	public int countByClassID(int class_id) {
		return attendanceDao.countAttendanceByClass(class_id);
	}

	@Override
	public int countByUserID(int user_id) {
		return attendanceDao.countAttendanceByUser(user_id);
	}

	@Override
	public ArrayList<Attendance> findBySchool(int school_id, int from_num, int max_result) {
		return (ArrayList<Attendance>) attendanceDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<Attendance> findByClass(int class_id, int from_num, int max_result) {
		return (ArrayList<Attendance>) attendanceDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public ArrayList<Attendance> findByUser(int user_id, int from_num, int max_result) {
		return (ArrayList<Attendance>) attendanceDao.findByClass(user_id, from_num, max_result);
	}

	@Override
	public Attendance insertAttendance(Attendance attendance) {
		attendanceDao.saveAttendance(attendance);
		return attendance;
	}

	@Override
	public Attendance updateAttendance(Attendance attendance) {
		
		Attendance attendanceDB = attendanceDao.findById(attendance.getId());
		attendanceDB = Attendance.updateChanges(attendanceDB, attendance);
		attendanceDao.updateAttendance(attendanceDB);
		
		
		return attendanceDB;
	}

	


}
