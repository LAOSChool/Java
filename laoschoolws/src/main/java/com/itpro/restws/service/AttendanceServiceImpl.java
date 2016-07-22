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

import com.itpro.restws.dao.AttendanceDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.SchoolTerm;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Attendance;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

@Service("attendanceService")
@Transactional
public class AttendanceServiceImpl implements AttendanceService{
	protected static final Logger logger = Logger.getLogger(AttendanceServiceImpl.class);
	@Autowired
	private AttendanceDao attendanceDao;
//	@Autowired
//	private TermDao termDao;
	
	@Autowired
	private SchoolYearService schoolYearService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private MessageService messageService;
	
	
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
		
		valid_insert_attendance(teacher,attendance);
		
		attendanceDao.saveAttendance(attendance);
		return attendance;
	}

//	@Override
//	public Attendance auditAttendance(User teacher, Attendance attendance) {
//		
//		User student = userService.findById(attendance.getStudent_id());
//		Attendance curr = findById(attendance.getId());
//		
//		// Keep first create date time
//		attendance.setActflg("A");
//		attendance.setCtddtm(curr.getCtddtm());
//		attendance.setCtdusr(curr.getCtdusr());
//		///
//		attendance.setAuditor(teacher.getId());
//		attendance.setAuditor_name(teacher.getFullname());
//		attendance.setStudent_name(student.getFullname());
//		
//		//attendanceDB = Attendance.updateChanges(attendanceDB, attendance);
//		attendanceDao.updateAttendance(attendance);
//		
//		return attendance;
//	}

	@Override
	public Attendance updateAttendance(User teacher, Attendance attendance) {
		if (attendance.getId() == null){
			throw new ESchoolException("attendance.ID = NULL", HttpStatus.BAD_REQUEST);
		}
		User student = userService.findById(attendance.getStudent_id());
		Attendance curr = findById(attendance.getId());
		
		if (curr != null ){
			curr = Attendance.updateChanges(curr, attendance);
			
			curr.setAuditor(teacher.getId());
			curr.setAuditor_name(teacher.getFullname());
			curr.setStudent_name(student.getFullname());
	
			valid_attendance_info(teacher,attendance);
			
			attendanceDao.updateAttendance(curr);
		}else{
			throw new ESchoolException("Error: cannot find attendace_id:"+attendance.getId(), HttpStatus.BAD_REQUEST);
		}
		return curr;
	}

	@Override
	public int countAttendanceExt(Integer school_id, Integer class_id, Integer user_id,
			Integer from_row_id,String att_dt,String from_dt, String to_dt,Integer session_id,Integer term_val, Integer year_id) {
		
		return attendanceDao.countAttendanceExt(school_id, class_id, user_id, from_row_id,att_dt,from_dt, to_dt,session_id,term_val, year_id);
	}

	@Override
	public ArrayList<Attendance> findAttendanceExt(Integer school_id, Integer class_id, Integer user_id,
			Integer from_row_id, int from_num, int max_result,String att_dt, String from_dt, String to_dt,Integer session_id, Integer term_val,Integer year_id) {
		
		return (ArrayList<Attendance>) attendanceDao.findAttendanceExt(school_id, class_id, user_id, from_row_id, from_num, max_result,att_dt, from_dt, to_dt,session_id,term_val,year_id);
	}

	@Override
	public Attendance requestAttendance(User user, Attendance request,boolean in_range, boolean is_sent_msg) {
		
		// more valid
		valid_insert_attendance(user, request);
		
		boolean is_valid = validAttendanceRequest(user, request,in_range); 
		if (is_valid){
			//SchoolTerm term = schoolYearService.findLatestTermBySchool(user.getSchool_id());
//			request.setTerm_val(term.getTerm_val());
//			request.setYear_id(term.getYear_id());
			
			request.setExcused(1);
			request.setSession_id(null);
			request.setSubject_id(null);
			request.setIs_requested(1);
			request.setRequested_dt(Utils.now());
			
			attendanceDao.saveAttendance(request);
			// Send Message
			if (!is_sent_msg){
				sendAttendRequestMessage(request);
			}
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

		
		int cnt = countAttendanceExt(request.getSchool_id(), request.getClass_id(), request.getStudent_id(),null,request.getAtt_dt(),null,null,null,null,null);
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
			boolean is_sent_msg = false;
			while( !start.after(end)){
			    Date targetDay = start.getTime();
			    String att_dt = Utils.dateToString(targetDay);
			    Attendance new_request = request.clone();
			    new_request.setAtt_dt(att_dt);
			    // Request attendance for each day
			    Attendance att = requestAttendance(user,new_request,true,is_sent_msg);
			    if (att != null ){
			    	list.add(att);
			    }
			    
			    // Next day
			    is_sent_msg = true;
			    start.add(Calendar.DATE, 1);
			}
			
		}else{
			throw new ESchoolException("Cannot parsing date from from_dt & to_dt", HttpStatus.BAD_REQUEST);
		}
		return list;
	}
	

	private void valid_insert_attendance(User curr_user, Attendance attendace){
		int cnt = countAttendanceExt(attendace.getSchool_id(), attendace.getClass_id(), attendace.getStudent_id(),null,attendace.getAtt_dt(),null,null,attendace.getSession_id(),null,null);
		if (cnt > 0){
			// throw new ESchoolException("Request already existing:"+curr_user.getId()+ ",  date="+request.getAtt_dt(), HttpStatus.TOO_MANY_REQUESTS);
			logger.error("Request already existing:"+curr_user.getId());
			throw new ESchoolException("Attendance already existing ! ", HttpStatus.BAD_REQUEST);
		}
		valid_attendance_info(curr_user,attendace);
	}
	private void valid_attendance_info(User curr_user, Attendance attendace) {
		if ((attendace.getSchool_id() == null || attendace.getSchool_id().intValue() == 0) ||
			( attendace.getStudent_id() == null || attendace.getStudent_id().intValue() == 0) ||
			( attendace.getClass_id() == null || attendace.getClass_id().intValue() == 0) ||
			( attendace.getAtt_dt() == null ) 
			)
		{
			throw new ESchoolException("school_id, student_id, class_id,att_dt are required", HttpStatus.BAD_REQUEST);
		}
		
		// check user
	
		
		if (curr_user.getSchool_id().intValue() != attendace.getSchool_id().intValue()){
			throw new ESchoolException("Current User.SchoolID() != Attendance.school_id ", HttpStatus.BAD_REQUEST);
		}
		
		
		User student = userService.findById(attendace.getStudent_id());
		EClass eclass = classService.findById(attendace.getClass_id());
		
		if (curr_user.getSchool_id().intValue() != attendace.getSchool_id().intValue()){
			throw new ESchoolException("Current User.SchoolID() != Attendance.school_id ", HttpStatus.BAD_REQUEST);
		}
		
		if (curr_user.getSchool_id().intValue() != student.getSchool_id().intValue()){
			throw new ESchoolException("Current User.SchoolID() != Student.school_id ", HttpStatus.BAD_REQUEST);
		}
		
		if (curr_user.getSchool_id().intValue() != eclass.getSchool_id().intValue()){
			throw new ESchoolException("Current User.SchoolID() != Eclass.school_id ", HttpStatus.BAD_REQUEST);
		}
		
		if (!student.is_belong2class(eclass.getId())){
			throw new ESchoolException("Student_ID:"+student.getId().intValue()+" is not belong to class_id: "+eclass.getId().intValue(), HttpStatus.BAD_REQUEST);
		}
		// Year 
		SchoolYear schoolYear = null;
		if (attendace.getYear_id() == null) {
			schoolYear = schoolYearService.findLatestYearBySchool(student.getSchool_id());
			if (schoolYear == null ){
				throw new ESchoolException("SchoolYear is NULL ( school_id="+student.getSchool_id().intValue() +")", HttpStatus.BAD_REQUEST);
			}
			attendace.setYear_id(schoolYear.getId());
		}else{
			schoolYear = schoolYearService.findById(attendace.getYear_id());
			if (schoolYear == null ){
				throw new ESchoolException("SchoolYearID is not existing:"+attendace.getYear_id().intValue(), HttpStatus.BAD_REQUEST);
			}
		}
		// TERM
		
		if (attendace.getTerm_val() == null) {
			SchoolTerm term  = schoolYearService.findLatestTermBySchool(student.getSchool_id());
			if (term == null ){
				throw new ESchoolException("Latest SchoolTerm is NULL ( school_id="+student.getSchool_id().intValue() +")", HttpStatus.BAD_REQUEST);
			}
			attendace.setTerm_val(term.getTerm_val());
		}else{
			ArrayList<SchoolTerm> terms = schoolYearService.findTermByYear(student.getSchool_id(), schoolYear.getId());
			boolean valid_term = false;
			for (SchoolTerm term :  terms){
				if (term.getTerm_val().intValue() == attendace.getTerm_val().intValue()){
					valid_term = true;
					break;
				}
			}
			if (!valid_term){
				throw new ESchoolException("TermVal is not existing for year_id:"+schoolYear.getId().intValue(), HttpStatus.BAD_REQUEST);
			}
		}
		
		
	}
	void sendAttendRequestMessage(Attendance request){
		Integer class_id = request.getClass_id();
		EClass eclass = classService.findById(class_id);
		if (eclass == null){
			throw new ESchoolException("request.class_id is not existing", HttpStatus.BAD_REQUEST);
		}
		Integer head_teacher_id = eclass.getHead_teacher_id();
		if (head_teacher_id == null){
			throw new ESchoolException("class_id:"+class_id.intValue()+" dont have head_teacher_id to send message", HttpStatus.BAD_REQUEST);
		}
		Message msg = messageService.newMessage(request.getStudent_id(), head_teacher_id, request.getNotice()==null?"- Request Attendance -  ":"- Request Attendance -  "+request.getNotice());
		
		messageService.insertMessageExt(msg);
	}
}
