package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.AttendanceDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Attendance;
import com.itpro.restws.model.User;

@Service("attendanceService")
@Transactional
public class AttendanceServiceImpl implements AttendanceService{

	@Autowired
	private AttendanceDao attendanceDao;

	@Autowired
	private UserService userService;
	
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
	public int countByStudent(Integer student) {
		return attendanceDao.countAttendanceByStudent(student);
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
	public ArrayList<Attendance> findByStudent(Integer student, int from_num, int max_result) {
		return (ArrayList<Attendance>) attendanceDao.findByStudent(student, from_num, max_result);
	}

	@Override
	public Attendance insertAttendance(User teacher,Attendance attendance) {
		
		User student = userService.findById(attendance.getStudent_id());
		
		attendance.setAuditor(teacher.getId());
		attendance.setAuditor_name(teacher.getFullname());
		attendance.setStudent_name(student.getFullname());
		
		attendanceDao.saveAttendance(attendance);
		return attendance;
	}

	@Override
	public Attendance updateAttendance(User teacher, Attendance attendance) {
		
		User student = userService.findById(attendance.getStudent_id());
		Attendance curr = findById(attendance.getId());
		// Keep first create date time
		attendance.setActflg("A");
		attendance.setCtddtm(curr.getCtddtm());
		attendance.setCtdusr(curr.getCtdusr());
		///
		attendance.setAuditor(teacher.getId());
		attendance.setAuditor_name(teacher.getFullname());
		attendance.setStudent_name(student.getFullname());
		
		//attendanceDB = Attendance.updateChanges(attendanceDB, attendance);
		attendanceDao.updateAttendance(attendance);
		
		return attendance;
	}

	@Override
	public int countAttendanceExt(Integer school_id, Integer class_id, Integer user_id,
			Integer from_row_id,String from_dt, String to_dt) {
		
		return attendanceDao.countAttendanceExt(school_id, class_id, user_id, from_row_id,from_dt, to_dt);
	}

	@Override
	public ArrayList<Attendance> findAttendanceExt(Integer school_id, Integer class_id, Integer user_id,
			Integer from_row_id, int from_num, int max_result, String from_dt, String to_dt) {
		
		return (ArrayList<Attendance>) attendanceDao.findAttendanceExt(school_id, class_id, user_id, from_row_id, from_num, max_result, from_dt, to_dt);
	}

	@Override
	public Attendance requestAttendance(User user, Attendance request) {
		
		validAttendanceRequest(user, request);
		
		
		request.setExcused(1);
		request.setSession_id(null);
		request.setSubject_id(null);
		request.setIs_requested(1);
		request.setRequested_dt(Utils.now());
		
		
		attendanceDao.saveAttendance(request);
		return request;
	}
	
	private void validAttendanceRequest(User curr_user, Attendance request){
		if (curr_user.getSchool_id() != request.getSchool_id()){
			throw new ESchoolException("User and request attendance is not in same school", HttpStatus.BAD_REQUEST);
		}
		
		if (!(userService.isBelongToClass(curr_user.getId(), request.getClass_id()))){
			throw new ESchoolException("UserID:"+curr_user.getId() +" is not belong to class_id:"+ request.getClass_id(), HttpStatus.BAD_REQUEST);
		}
		
		if (curr_user.getId() != request.getStudent_id()){
			throw new ESchoolException("StudentID:"+curr_user.getId()+ " is differ from request attendance student_id = "+request.getStudent_id(), HttpStatus.BAD_REQUEST);
		}
		
		if (request.getAtt_dt() == null ){
			throw new ESchoolException("Must input mandatory att_dt", HttpStatus.BAD_REQUEST);
		}
		
		if (request.getState() == null  ){
			throw new ESchoolException("Must input valid mandatory State", HttpStatus.BAD_REQUEST);
		}

		
		int cnt = countAttendanceExt(request.getSchool_id(), request.getClass_id(), request.getStudent_id(),null,request.getAtt_dt(),null);
		if (cnt > 0){
			throw new ESchoolException("Request already existing:"+curr_user.getId()+ ",  date="+request.getAtt_dt(), HttpStatus.BAD_REQUEST);
		}
	}

	


}
