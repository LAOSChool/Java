package com.itpro.restws.service;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.AttendanceDao;
import com.itpro.restws.dao.MSessionDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_MSG_CHANNEL;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Attendance;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.MSession;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.SysTemplate;
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
	private SchoolTermService schoolTermService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private SysTblService sysTblService;
	@Autowired
	private MSessionDao  msessionDao; 
	@Autowired
	private MSubjectDao  msubjectDao;
	
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
	public Attendance insertAttendance(User me,Attendance attendance) {
		
		
		if (attendance.getStudent_id() == null ){
			throw new ESchoolException("student_id = NULL", HttpStatus.BAD_REQUEST);
		}
		User student = userService.findById(attendance.getStudent_id());
		if (student.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("student.school_id is not similar with teacher school_id", HttpStatus.BAD_REQUEST);
		}
		
		attendance.setAuditor(me.getId());
		attendance.setAuditor_name(me.getFullname());
		attendance.setStudent_name(me.getFullname());
		
		valid_insert_attendance(me,attendance);
		attendanceDao.saveAttendance(me,attendance);
		// Send message 
		send_msg_attendance(attendance);

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
	public Attendance updateTransAttendance(User me, Attendance attendance) {
		if (attendance == null || attendance.getId() == null){
			throw new ESchoolException("attendance == null || attendance.id = NULL", HttpStatus.BAD_REQUEST);
		}
		
		if (attendance.getStudent_id() == null){
			throw new ESchoolException("student_id is NULL", HttpStatus.BAD_REQUEST);
		}
		
		User student = userService.findById(attendance.getStudent_id());

		if (student == null){
			throw new ESchoolException("student_id is not existing", HttpStatus.BAD_REQUEST);
		}
		
		Attendance curr_db = findById(attendance.getId());
		
		if (curr_db != null ){

		  try {
			  attendanceDao.setFlushMode(FlushMode.MANUAL);
			  curr_db = Attendance.updateChanges(curr_db, attendance);
				
			  curr_db.setAuditor(me.getId());
			  curr_db.setAuditor_name(me.getFullname());
			  curr_db.setStudent_name(student.getFullname());
		
				valid_attendance_info(me,curr_db);
			  	
	        } catch (Exception e){
	        	attendanceDao.clearChange();
	        	throw e;
	        }
		   finally {
			   attendanceDao.setFlushMode(FlushMode.AUTO);
	        }
			  
			attendanceDao.updateAttendance(me,curr_db);
		}else{
			throw new ESchoolException("Error: cannot find attendace_id:"+attendance.getId(), HttpStatus.BAD_REQUEST);
		}
		return curr_db;
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
	public Attendance requestAttendance(User me, Attendance request,boolean in_range, boolean is_sent_msg) {
		
		// more valid
		valid_insert_attendance(me, request);
		
		boolean is_valid = validAttendanceRequest(me, request,in_range); 
		if (is_valid){
			//SchoolTerm term = schoolYearService.findLatestTermBySchool(user.getSchool_id());
//			request.setTerm_val(term.getTerm_val());
//			request.setYear_id(term.getYear_id());
			
			request.setExcused(1);
			request.setSession_id(null);
			request.setSubject_id(null);
			request.setIs_requested(1);
			request.setRequested_dt(Utils.now());
			
			attendanceDao.saveAttendance(me,request);
			// Send Message
			if (!is_sent_msg){
				// sendAttendMessage(request);
				send_msg_request_ext(request, null);
			}
			return request;
		}
		return null;
	}
	
	private boolean validAttendanceRequest(User me, Attendance request,boolean in_range){
		if (me.getSchool_id() != request.getSchool_id()){
			throw new ESchoolException("User and request attendance is not in same school", HttpStatus.BAD_REQUEST);
		}
		
		if (!(userService.isBelongToClass(me.getId(), request.getClass_id()))){
			throw new ESchoolException("UserID:"+me.getId() +" is not belong to class_id:"+ request.getClass_id(), HttpStatus.BAD_REQUEST);
		}
		
		if (me.getId() != request.getStudent_id()){
			throw new ESchoolException("StudentID:"+me.getId()+ " is differ from request attendance student_id = "+request.getStudent_id(), HttpStatus.BAD_REQUEST);
		}
		
		if (!in_range && request.getAtt_dt() == null ){
			throw new ESchoolException("Must input mandatory att_dt", HttpStatus.BAD_REQUEST);
		}
		// Attendance Date
		Date att_dt = Utils.parsetDateAll(request.getAtt_dt());
		if (att_dt == null ){
			throw new ESchoolException("att_dt is not valide datetime format:"+request.getAtt_dt()+", plz correct to: yyyy-MM-dd HH:mm:ss", HttpStatus.BAD_REQUEST);
		}else{
			String correct_att_dt = Utils.dateToString(att_dt);// yyyy-MM-dd HH:mm:ss
			request.setAtt_dt(correct_att_dt);
		}
		if (request.getState() == null  ){
			request.setState(1);// 1: Absent, 2: Late
			//throw new ESchoolException("Must input valid mandatory State", HttpStatus.BAD_REQUEST);
		}

		
		int cnt = countAttendanceExt(request.getSchool_id(), request.getClass_id(), request.getStudent_id(),null,request.getAtt_dt(),null,null,null,null,null);
		if (cnt > 0){
			// throw new ESchoolException("Request already existing:"+curr_user.getId()+ ",  date="+request.getAtt_dt(), HttpStatus.TOO_MANY_REQUESTS);
			logger.error("Request already existing:"+me.getId());
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<Attendance>  requestAttendanceEx(User user, Attendance request,String from_dt, String to_dt) {
		 ArrayList<Attendance>  list = new ArrayList<Attendance>();
		 
		 if (	from_dt == null || 
				 from_dt.trim().equals("") || 
				 to_dt == null|| 
				 to_dt.trim().equals("")) 
		 {
			 throw new ESchoolException("Both from_dt and to_dt are required", HttpStatus.BAD_REQUEST);
		 }
		 // Check from dt
		 Date dt = Utils.parsetDateAll(from_dt);
		 if (dt == null ){
			 throw new ESchoolException("Cannot parsing from_dt", HttpStatus.BAD_REQUEST);
		 }else{
			 from_dt = Utils.dateToString(dt);
		 }
		 // check to dt
		 dt = Utils.parsetDateAll(to_dt);
		 if (dt == null ){
			 throw new ESchoolException("Cannot parsing to_dt", HttpStatus.BAD_REQUEST);
		 }else{
			 to_dt = Utils.dateToString(dt);
		 }
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
			//boolean is_sent_msg = false;
			String date_info = "";
			while( !start.after(end)){
			    Date targetDay = start.getTime();
			    String att_dt = Utils.dateToStringDateOnly(targetDay);
			    
			    Attendance new_request = request.clone();
			    new_request.setAtt_dt(att_dt);
			    // Request attendance for each day
			    Attendance att = requestAttendance(user,new_request,true,true);
			    if (att != null ){
			    	list.add(att);
			    	date_info += att_dt+"\n";
			    }
			    
			    // Next day
			    //is_sent_msg = true;
			    start.add(Calendar.DATE, 1);
			}
			if (list.size() > 0){
				send_msg_request_ext(request,date_info);
			}
			
		}else{
			throw new ESchoolException("Cannot parsing date from from_dt & to_dt", HttpStatus.BAD_REQUEST);
		}
		return list;
	}
	

	private void send_msg_request_ext(Attendance request, String date_info) {
		String msg_content = get_message_sample(true);
		/***
		 * Request Attendance
			Date:
			[DATE]
			
			Reason:
			[REASON]
			Thank you!
		 */
		if (date_info == null || date_info.trim().length() == 0){
			date_info = request.getAtt_dt();
			Date dt = Utils.parsetDateAll(date_info);
			if (dt != null ){
				date_info = Utils.dateToStringDateOnly(dt);
			}
		}
		
		msg_content = msg_content.replaceFirst("\\[DATE\\]", date_info==null?"":date_info);
		msg_content = msg_content.replaceFirst("\\[REASON\\]", request.getNotice()==null?"":request.getNotice());
		
		Integer class_id = request.getClass_id();
		EClass eclass = classService.findById(class_id);
		if (eclass == null){
			throw new ESchoolException("request.class_id is not existing", HttpStatus.BAD_REQUEST);
		}
		Integer head_teacher_id = eclass.getHead_teacher_id();
		if (head_teacher_id == null){
			throw new ESchoolException("class_id:"+class_id.intValue()+" dont have head_teacher_id to send message", HttpStatus.BAD_REQUEST);
		}
		
//		Message msg = messageService.newSimpleMessage(request.getStudent_id(), head_teacher_id, msg_content);
//		messageService.insertMessageExt(msg);
		messageService.newSimpleMessage(request.getStudent_id(), head_teacher_id, msg_content, new Integer(E_MSG_CHANNEL.FIREBASE.getValue()),class_id);
		
	}

	private void valid_insert_attendance(User me, Attendance attendace){
		if (attendace.getId() != null ){
			throw new ESchoolException("Cannot create new attendance, id != null", HttpStatus.BAD_REQUEST);
		}
		if (attendace.getSchool_id() == null || attendace.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("SchoolId is not correct", HttpStatus.BAD_REQUEST);
		}
		if (attendace.getClass_id() == null || attendace.getClass_id().intValue() == 0){
			throw new ESchoolException("ClassID is NULL", HttpStatus.BAD_REQUEST);
		}
		if (attendace.getStudent_id() == null || attendace.getStudent_id().intValue() ==0){
			throw new ESchoolException("SchoolId is not correct", HttpStatus.BAD_REQUEST);
		}
		if (attendace.getAtt_dt() == null ){
			throw new ESchoolException("att_dt is not correct", HttpStatus.BAD_REQUEST);
		}
		String att_dt = attendace.getAtt_dt();
		Date dt = Utils.parsetDateAll(att_dt);
		if (dt == null ){
			throw new ESchoolException("attt_dt is not correct format, plz input yyyy-MM-dd", HttpStatus.BAD_REQUEST);
		}
		att_dt = Utils.dateToString(dt);
		attendace.setAtt_dt(att_dt);
		// Count existing
		int cnt = countAttendanceExt(attendace.getSchool_id(), attendace.getClass_id(), attendace.getStudent_id(),null,attendace.getAtt_dt(),null,null,attendace.getSession_id(),null,null);
		if (cnt > 0){
			// throw new ESchoolException("Request already existing:"+curr_user.getId()+ ",  date="+request.getAtt_dt(), HttpStatus.TOO_MANY_REQUESTS);
			String err="Similar attendance already existing/// school_id:"+attendace.getSchool_id() + "///class_id:"+ attendace.getClass_id()+"///student_id:"+ attendace.getStudent_id()+"///att_dt:"+attendace.getAtt_dt();
			logger.error(err);
			throw new ESchoolException(err, HttpStatus.BAD_REQUEST);
		}
		valid_attendance_info(me,attendace);
	}
	private void valid_attendance_info(User me, Attendance attendace) {
		if ((attendace.getSchool_id() == null || attendace.getSchool_id().intValue() == 0) ||
			( attendace.getStudent_id() == null || attendace.getStudent_id().intValue() == 0) ||
			( attendace.getClass_id() == null || attendace.getClass_id().intValue() == 0) ||
			( attendace.getAtt_dt() == null ) 
			)
		{
			throw new ESchoolException("school_id, student_id, class_id,att_dt are required", HttpStatus.BAD_REQUEST);
		}
		
		// check user
	
		
		if (me.getSchool_id().intValue() != attendace.getSchool_id().intValue()){
			throw new ESchoolException("Current User.SchoolID() != Attendance.school_id ", HttpStatus.BAD_REQUEST);
		}
		
		
		User student = userService.findById(attendace.getStudent_id());
		EClass eclass = classService.findById(attendace.getClass_id());
		
		if (me.getSchool_id().intValue() != attendace.getSchool_id().intValue()){
			throw new ESchoolException("Current User.SchoolID() != Attendance.school_id ", HttpStatus.BAD_REQUEST);
		}
		
		if (me.getSchool_id().intValue() != student.getSchool_id().intValue()){
			throw new ESchoolException("Current User.SchoolID() != Student.school_id ", HttpStatus.BAD_REQUEST);
		}
		
		if (me.getSchool_id().intValue() != eclass.getSchool_id().intValue()){
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
			SchoolTerm term  = schoolTermService.findMaxActiveTermBySchool(student.getSchool_id());
			if (term == null ){
				throw new ESchoolException("Latest SchoolTerm is NULL ( school_id="+student.getSchool_id().intValue() +")", HttpStatus.BAD_REQUEST);
			}
			attendace.setTerm_val(term.getTerm_val());
		}else{
			ArrayList<SchoolTerm> terms = schoolTermService.findAllTermByYear(student.getSchool_id(), schoolYear.getId());
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
		// Attendance Date
		Date att_dt = Utils.parsetDateAll(attendace.getAtt_dt());
		if (att_dt == null ){
			throw new ESchoolException("att_dt is not valide datetime format:"+attendace.getAtt_dt()+", plz correct to: yyyy-MM-dd HH:mm:ss", HttpStatus.BAD_REQUEST);
		}else{
			String correct_att_dt = Utils.dateToString(att_dt);// yyyy-MM-dd HH:mm:ss
			attendace.setAtt_dt(correct_att_dt);
		}
		
		
	}
	
//	void sendAttendMessage(Attendance att){
//		Integer class_id = att.getClass_id();
//		EClass eclass = classService.findById(class_id);
//		if (eclass == null){
//			throw new ESchoolException("request.class_id is not existing", HttpStatus.BAD_REQUEST);
//		}
//		Integer head_teacher_id = eclass.getHead_teacher_id();
//		if (head_teacher_id == null){
//			throw new ESchoolException("class_id:"+class_id.intValue()+" dont have head_teacher_id to send message", HttpStatus.BAD_REQUEST);
//		}
//		String content =  att.getNotice()==null?"- Student is recorded a absence from class -  ":"- Student is recorded a absence from class -  "+att.getNotice();
//		if (att.getIs_requested() != null && att.getIs_requested().intValue() == 1){
//			content =  att.getNotice()==null?"- Request Attendance -  ":"- Request Attendance -  "+att.getNotice();	
//		}
//		Message msg = messageService.newMessage(att.getStudent_id(), head_teacher_id, content);
//		
//		messageService.insertMessageExt(msg);
//	}
	String get_message_sample(boolean is_request){
		String notice_type = "-ATTENDANCE-";
		if (is_request){
			notice_type = "-REQUEST-";
		}
		String ret="New attendance default message";
		ArrayList<SysTemplate> list = sysTblService.findAll("sys_msg_samp",0,99999);
		for (SysTemplate samp: list){
			if (samp.getNotice() != null && samp.getNotice().equalsIgnoreCase(notice_type)){
				if (samp.getLval() != null && samp.getLval().trim().length() > 0){
					ret = samp.getLval();
				}else{
					ret = samp.getSval();
				}
			}
		}
		return ret;
	}
	
	private void send_msg_attendance(Attendance attendace) {
		
		String day_in_week_info= "";
		String date_info = attendace.getAtt_dt();
		
		String session_name_info="";
		String session_period_info="";
		
		String subject_info = "";
		String auditor_info ="";
		String reason_info = "";
		// Thong tin ngay thang
		Date dt = Utils.parsetDateAll(attendace.getAtt_dt());
		if (dt != null ){
			// Get day of week
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			Integer dayofWeek = cal.get(Calendar.DAY_OF_WEEK);
			Integer id_sys_week_day = Utils.convertDayOfWeekToSyWeekDayID(dayofWeek);
			
			ArrayList<SysTemplate> list_dow = sysTblService.findAll("sys_weekday", 0, 99999);
			for (SysTemplate stl:list_dow){
				if (stl.getId().intValue() == id_sys_week_day.intValue()){
					day_in_week_info = stl.getLval();
					if (day_in_week_info == null || day_in_week_info.trim().length() == 0){
						day_in_week_info = stl.getSval();	
					}
					break;
				}
			}
			
			// Change date format to dd-MM-yyyy
			Format formatter = new SimpleDateFormat("dd-MM-yyyy");
			date_info = formatter.format(dt);
			
		}
		
		// Thong tin tiet hoc
		if (attendace.getSession_id() != null && attendace.getSession_id().intValue() > 0){
			MSession session = msessionDao.findById(attendace.getSession_id() );
			if (session != null ){
				session_name_info= session.getLval();
				if (session_name_info == null || session_name_info.trim().length() == 0){
					session_name_info= session.getSval();
				}
				session_period_info = session.getNotice();
			}
		}
		
		// Thong tin mon hoc
		
		if (attendace.getSubject_id() != null && attendace.getSubject_id().intValue() > 0){
			MSubject subject = msubjectDao.findById(attendace.getSubject_id() );
			if (subject != null ){
				subject_info= subject.getLval();
				if (subject_info == null || subject_info.trim().length() == 0){
					subject_info= subject.getSval();
				}
				
			}
		}
		// Thong tin auditor
		auditor_info = attendace.getAuditor_name();
		// Thong tin auditor
		reason_info = attendace.getNotice();
	
		String msg_content = get_message_sample(false);
		/***
		 * [DAY_OF_WEEK], [DATE]
			[SESSION](PERIOD)
			Subject:[SUBJECT]
			Teacher: [AUDITOR]
			
			Absent
			Reason: [REASON]
		 */
		
		msg_content = msg_content.replaceFirst("\\[DAY_OF_WEEK\\]", day_in_week_info==null?"":day_in_week_info);
		msg_content = msg_content.replaceFirst("\\[DATE\\]", date_info==null?"":date_info);
		if (attendace.getSession_id() == null || attendace.getSession_id().intValue() == 0){
			msg_content = msg_content.replaceFirst("\\[SESSION\\]","");
			msg_content = msg_content.replaceFirst("\\(\\[PERIOD\\]\\)", "");
			msg_content = msg_content.replaceFirst("\\[SUBJECT\\]","");
		}else{
			msg_content = msg_content.replaceFirst("\\[SESSION\\]", session_name_info==null?"":session_name_info);
			msg_content = msg_content.replaceFirst("\\[PERIOD\\]", session_period_info==null?"":session_period_info);
			msg_content = msg_content.replaceFirst("\\[SUBJECT\\]", subject_info==null?"":subject_info);	
		}
		
		msg_content = msg_content.replaceFirst("\\[AUDITOR\\]", auditor_info==null?"":auditor_info);
		msg_content = msg_content.replaceFirst("\\[REASON\\]", reason_info==null?"":reason_info);
		
		Integer class_id = attendace.getClass_id();
		EClass eclass = classService.findById(class_id);
		if (eclass == null){
			throw new ESchoolException("request.class_id is not existing", HttpStatus.BAD_REQUEST);
		}
		Integer head_teacher_id = eclass.getHead_teacher_id();
		if (head_teacher_id == null){
			throw new ESchoolException("class_id:"+class_id.intValue()+" dont have head_teacher_id to send message", HttpStatus.BAD_REQUEST);
		}
		
//		Message msg = messageService.newMessage(attendace.getAuditor(), attendace.getStudent_id(), msg_content);
//		messageService.insertMessageExt(msg);
		messageService.newSimpleMessage(attendace.getAuditor(), attendace.getStudent_id(), msg_content, new Integer(E_MSG_CHANNEL.FIREBASE.getValue()),class_id);


		
	}

	@Override
	public Attendance updateAttachedAttendance(User teacher, Attendance attendance) {
		attendanceDao.updateAttendance(teacher, attendance);
		return attendance;
	}	
	
	
}
