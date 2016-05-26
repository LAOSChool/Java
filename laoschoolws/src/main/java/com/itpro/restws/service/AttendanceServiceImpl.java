package com.itpro.restws.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.controller.BaseController;
import com.itpro.restws.dao.AttendanceDao;
import com.itpro.restws.dao.TermDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Attendance;

import com.itpro.restws.model.Term;
import com.itpro.restws.model.User;

import sun.util.logging.resources.logging_pt_BR;

@Service("attendanceService")
@Transactional
public class AttendanceServiceImpl implements AttendanceService{
	protected static final Logger logger = Logger.getLogger(AttendanceServiceImpl.class);
	@Autowired
	private AttendanceDao attendanceDao;
	@Autowired
	private TermDao termDao;
	
	
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
			Integer from_row_id,String att_dt,String from_dt, String to_dt) {
		
		return attendanceDao.countAttendanceExt(school_id, class_id, user_id, from_row_id,att_dt,from_dt, to_dt);
	}

	@Override
	public ArrayList<Attendance> findAttendanceExt(Integer school_id, Integer class_id, Integer user_id,
			Integer from_row_id, int from_num, int max_result,String att_dt, String from_dt, String to_dt) {
		
		return (ArrayList<Attendance>) attendanceDao.findAttendanceExt(school_id, class_id, user_id, from_row_id, from_num, max_result,att_dt, from_dt, to_dt);
	}

	@Override
	public Attendance requestAttendance(User user, Attendance request,boolean in_range) {
		boolean is_valid = validAttendanceRequest(user, request,in_range);
		ArrayList<Term> terms = termDao.getLatestTerm(user.getSchool_id());
		Term term = terms.get(terms.size()-1);
		if (is_valid){
			request.setTerm_id(term.getId());
			request.setExcused(1);
			request.setSession_id(null);
			request.setSubject_id(null);
			request.setIs_requested(1);
			request.setRequested_dt(Utils.now());
			
			attendanceDao.saveAttendance(request);
			return request;
		}
		return null;
	}
	
	private boolean validAttendanceRequest(User curr_user, Attendance request,boolean in_range){
		if (curr_user.getSchool_id() != request.getSchool_id()){
			throw new ESchoolException("User and request attendance is not in same school", HttpStatus.BAD_REQUEST);
		}
		
		if (!(userService.isBelongToClass(curr_user.getId(), request.getClass_id()))){
			throw new ESchoolException("UserID:"+curr_user.getId() +" is not belong to class_id:"+ request.getClass_id(), HttpStatus.BAD_REQUEST);
		}
		
		if (curr_user.getId() != request.getStudent_id()){
			throw new ESchoolException("StudentID:"+curr_user.getId()+ " is differ from request attendance student_id = "+request.getStudent_id(), HttpStatus.BAD_REQUEST);
		}
		
		if (!in_range && request.getAtt_dt() == null ){
			throw new ESchoolException("Must input mandatory att_dt", HttpStatus.BAD_REQUEST);
		}
		
		if (request.getState() == null  ){
			request.setState(1);// 1: Absent, 2: Late
			//throw new ESchoolException("Must input valid mandatory State", HttpStatus.BAD_REQUEST);
		}

		
		int cnt = countAttendanceExt(request.getSchool_id(), request.getClass_id(), request.getStudent_id(),null,request.getAtt_dt(),null,null);
		if (cnt > 0){
			// throw new ESchoolException("Request already existing:"+curr_user.getId()+ ",  date="+request.getAtt_dt(), HttpStatus.TOO_MANY_REQUESTS);
			logger.error("Request already existing:"+curr_user.getId());
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<Attendance>  requestAttendanceEx(User user, Attendance request,String from_dt, String to_dt) {
		 ArrayList<Attendance>  list = new ArrayList<Attendance>();
		// Check from to
		if (Utils.checkDateFormat(from_dt) &&
				Utils.checkDateFormat(to_dt)
				){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate =null;
			Date endDate = null;
			
			try {
				startDate = formatter.parse(from_dt);
				endDate = formatter.parse(to_dt);
				
				
			} catch (ParseException e) {
				throw new ESchoolException("Cannot parsing date from from_dt & to_dt", HttpStatus.BAD_REQUEST);
			}
			// Check date after today
			Calendar now = Calendar.getInstance();
			Date today = Utils.fullTimeToDate(now.getTime());
			if ((startDate.before(today))  || (endDate.before(today))) {
				String st = Utils.dateToString(startDate);
				String tt = Utils.dateToString(endDate);
				String td = Utils.dateToString(today);
				throw new ESchoolException("invalid from_dt:"+st+" & to_dt:"+tt+", need to be after today:"+td, HttpStatus.BAD_REQUEST);
			}
			
			Calendar start = Calendar.getInstance();
			start.setTime(startDate);

			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			while( !start.after(end)){
			    Date targetDay = start.getTime();
			    String att_dt = Utils.dateToString(targetDay);
			    Attendance new_request = request.clone();
			    new_request.setAtt_dt(att_dt);
			    // Request attendance for each day
			    Attendance att = requestAttendance(user,new_request,true);
			    if (att != null ){
			    	list.add(att);
			    }
			    
			    // Next day
			    start.add(Calendar.DATE, 1);
			}
			
		}else{
			throw new ESchoolException("Cannot parsing date from from_dt & to_dt", HttpStatus.BAD_REQUEST);
		}
		return list;
	}
	


}
