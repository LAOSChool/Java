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
	public Attendance findById(Integer id) {
		return attendanceDao.findById(id);
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		return attendanceDao.countAttendanceBySchool(school_id);
	}

	@Override
	public int countByClassID(Integer class_id) {
		return attendanceDao.countAttendanceByClass(class_id);
	}

	@Override
	public int countByUserID(Integer user_id) {
		return attendanceDao.countAttendanceByUser(user_id);
	}

	@Override
	public ArrayList<Attendance> findBySchool(Integer school_id, int from_num, int max_result) {
		return (ArrayList<Attendance>) attendanceDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<Attendance> findByClass(Integer class_id, int from_num, int max_result) {
		return (ArrayList<Attendance>) attendanceDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public ArrayList<Attendance> findByUser(Integer user_id, int from_num, int max_result) {
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

	@Override
	public int countAttendanceExt(Integer school_id, Integer class_id, Integer user_id,
			Integer from_row_id) {
		
		return attendanceDao.countAttendanceExt(school_id, class_id, user_id, from_row_id);
	}

	@Override
	public ArrayList<Attendance> findAttendanceExt(Integer school_id, Integer class_id, Integer user_id,
			Integer from_row_id, int from_num, int max_result) {
		
		return (ArrayList<Attendance>) attendanceDao.findAttendanceExt(school_id, class_id, user_id, from_row_id, from_num, max_result);
	}

	


}
