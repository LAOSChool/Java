package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.helper.RollUp;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Attendance;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.Timetable;
import com.itpro.restws.model.User;
import com.itpro.restws.service.SchoolYearService;
/**
 * Controller with REST API. Access to login is generally permitted, stuff in
 * /secure/ sub-context is protected by configuration. Some security annotations are
 * thrown in just to make a point.
 * Notice:
 * Consume should be the type of data that the web service expects to receive
 * Produces should be the type of data that the web service will return
 */
//Here @RestController is shorthand of = @Controller + @ResponseBody
// Where every method returns a domain object instead of a view
@RestController 
public class AttendanceController extends BaseController {
	
	
	@Autowired
	private SchoolYearService schoolYearService;
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/attendances",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getAttendances(
			@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			@RequestParam(value="filter_user_id",required =false) Integer filter_user_id,
			
			@RequestParam(value="from_row",required =false) Integer filter_from_row,
			@RequestParam(value="max_result",required =false) Integer filter_max_result,
			
			@RequestParam(value="filter_from_id", required =false) Integer filter_from_id,
			@RequestParam(value="filter_from_dt", required =false) String filter_from_dt,
			@RequestParam(value="filter_to_dt", required =false) String filter_to_dt,
			@RequestParam(value="filter_year_id", required =false) Integer filter_year_id,
			@RequestParam(value="filter_term_val", required =false) Integer filter_term_val,
			@RequestParam(value="filter_from_time", required =false) Long filter_from_time,
			@RequestParam(value="filter_to_time", required =false) Long filter_to_time,			
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** getAttendances() START");
		
		
		User me = getCurrentUser();
		Integer school_id = me.getSchool_id();
		Integer class_id =  filter_class_id;
		Integer student_id = filter_user_id;
		Integer year_id = filter_year_id;
		Integer term_val = filter_term_val;
		
		if (me.hasRole(E_ROLE.ADMIN.getRole_short())){
			
    	}else if (me.hasRole(E_ROLE.TEACHER.getRole_short()) || me.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short()) ){
    		if (class_id == null || class_id == 0 ){
    			throw new ESchoolException("User is not Admin, require filter_class_id to get Attendance Info ",HttpStatus.BAD_REQUEST);
    		}
    		if (!userService.isBelongToClass(me.getId(), class_id)){
    			throw new ESchoolException("User ID="+me.getId()+" is not belong to the class id = "+class_id,HttpStatus.BAD_REQUEST);
    		}
    		
    		EClass eclass = classService.findById(class_id);
    		if (eclass.getSchool_id().intValue() != school_id.intValue()){
    			if (!userService.isBelongToClass(me.getId(), class_id)){
        			throw new ESchoolException("User ID="+me.getId()+" and Class are not in same school,class_id= "+class_id,HttpStatus.BAD_REQUEST);
        		}
    		}
    		
    	}else{
    		// STUDENT
    		student_id = me.getId();
    	}
    	
		if (filter_from_time != null && filter_from_time.longValue() > 0){
			if (filter_from_dt != null ){
				throw new ESchoolException("Cannot not input both filter_from_dt AND filter_from_time", HttpStatus.BAD_REQUEST);
			}
			filter_from_dt = Utils.numberToDateTime(filter_from_time);
		}else if (filter_from_dt != null ){
			Date dt = Utils.parsetDateAll(filter_from_dt);// YYYY-MM-DD
			if (dt == null){
				throw new ESchoolException("Invalide filter_from_dt: "+filter_from_dt,HttpStatus.BAD_REQUEST);
			}else{
				filter_from_dt = Utils.dateToString(dt);
			}

		}
		
		
		if (filter_to_time != null && filter_to_time.longValue() > 0){
			if (filter_to_dt != null ){
				throw new ESchoolException("Cannot not input both filter_to_dt AND filter_to_time", HttpStatus.BAD_REQUEST);
			}
			filter_to_dt = Utils.numberToDateTime(filter_to_time);
		}else if (filter_to_dt != null ){
			Date dt = Utils.parsetDateAll(filter_to_dt);// YYYY-MM-DD
			if (dt == null){
				throw new ESchoolException("Invalide filter_to_dt: "+filter_to_dt,HttpStatus.BAD_REQUEST);
			}else{
				filter_to_dt = Utils.dateToString(dt);
			}

		}
		
		
		
		List<Attendance> attendances = null;
		int total_row = 0;
		
		
	
		
		if (year_id == null){
			SchoolYear schoolYear = schoolYearService.findLatestYearBySchool(me.getSchool_id());
			if (schoolYear == null ){
				throw new ESchoolException("Cannot get schoolyear of school_id: "+me.getSchool_id().intValue(),HttpStatus.BAD_REQUEST);
			}
			year_id= schoolYear.getId();
		}
				
		ListEnt rspEnt = new ListEnt();
	  
    	// Count user
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
    	total_row = attendanceService.countAttendanceExt(school_id, class_id, student_id, filter_from_id,null,filter_from_dt,filter_to_dt,null,term_val,year_id);
    	if( (total_row <=  0) || (from_row > total_row) ||  max_result<=0) {
    		rspEnt.setList(null);
    		rspEnt.setFrom_row(0);
    		rspEnt.setTo_row(0);
    		rspEnt.setTotal_count(0);
    		return rspEnt;
    	}
    	
    	if ((from_row + max_result > total_row)){
    		max_result = total_row-from_row;
    	}
    	logger.info("Attendance count: total_row : "+total_row);
    	logger.info("Attendance count: from_row : "+from_row);
    	logger.info("Attendance count: max_result : "+max_result);
    	
    		
		
		// Query class by school id
		attendances = attendanceService.findAttendanceExt(school_id, class_id, student_id, filter_from_id, from_row, max_result,null,filter_from_dt,filter_to_dt,null,term_val,year_id);
	    rspEnt.setList(attendances);
	    rspEnt.setFrom_row(from_row);
	    rspEnt.setTo_row(from_row + max_result);
	    rspEnt.setTotal_count(total_row);
		    
	  
	    return rspEnt;

	}
	
	@Secured({ "ROLE_STUDENT"})
	@RequestMapping(value = "/api/attendances/myprofile", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public ListEnt getAttendanceProfile(
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 ) {
		logger.info(" *** MainRestController.getAttendanceProfile Start");
		ListEnt rspEnt = new ListEnt();
		
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
		User student = getCurrentUser();

		// Count user
    	int total_row = attendanceService.countByStudent(student.getId());
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
    	
		ArrayList<Attendance> list = attendanceService.findByStudent(student.getId(), from_row, max_result);
        
		rspEnt.setList(list);
	    rspEnt.setFrom_row(from_row);
	    rspEnt.setTo_row(from_row + max_result);
	    rspEnt.setTotal_count(total_row);
	    
	    return rspEnt;
	 }
	
	// @Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value = "/api/attendances/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public Attendance getAttendance(
			 @PathVariable int  id,
			 @Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getAttendance/{id}:"+id);
		
    	User cur_user = getCurrentUser();
    	Attendance attendance  = attendanceService.findById(Integer.valueOf(id));
    	
    	if (attendance == null){
    		throw new ESchoolException("Not found", HttpStatus.NOT_FOUND);
    	}
    	
    	if (cur_user.getSchool_id().intValue() != attendance.getSchool_id().intValue()){
    		throw new ESchoolException("Current user and attendance_id are not in same School", HttpStatus.BAD_REQUEST);
    	}
    	
    	if ((cur_user.hasRole(E_ROLE.STUDENT.getRole_short())) && 
    			(attendance.getStudent_id().intValue() != cur_user.getId().intValue())){
    		throw new ESchoolException("AttendanceID:"+id+" is not belong to current Student, user_id:"+cur_user.getId().intValue(), HttpStatus.BAD_REQUEST);
    	}
			
    	if ((cur_user.hasRole(E_ROLE.TEACHER.getRole_short())) && 
    			!userService.isBelongToClass(cur_user.getId(), attendance.getClass_id())){
    		throw new ESchoolException("AttendanceID:"+id+" is not belong to current Student, user_id:"+cur_user.getId().intValue(), HttpStatus.BAD_REQUEST);
    	}
    	
    	
    	
	    return attendance;
	 }
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/attendances/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo createAttendance(
			@RequestBody Attendance attendance,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.create");
		
		User auditor = getCurrentUser();
		
		if (auditor.getSchool_id().intValue() != attendance.getSchool_id().intValue()){
    		throw new ESchoolException("Current user and attendance_id are not in same School", HttpStatus.BAD_REQUEST);
    	}
		
			
    	if (	(auditor.hasRole(E_ROLE.TEACHER.getRole_short()) || auditor.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short()) ) && 
    			(!userService.isBelongToClass(auditor.getId(), attendance.getClass_id()))
    		){
    		throw new ESchoolException("Invalid Class_ID:"+attendance.getClass_id()+", is not belong to current user_id:"+auditor.getId().intValue(), HttpStatus.BAD_REQUEST);
    	}
		
    	RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		Attendance ret= attendanceService.insertAttendance(auditor,attendance);
		rsp.setMessageObject(ret);
		return rsp;
		 
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/attendances/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public Attendance updateAttendances(
			@RequestBody Attendance attendance,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.attendances.update");
		User teacher = getCurrentUser();
		
		if (teacher.getSchool_id().intValue() != attendance.getSchool_id().intValue()){
    		throw new ESchoolException("Current user and attendance_id are not in same School", HttpStatus.BAD_REQUEST);
    	}
		
			
    	if (	(teacher.hasRole(E_ROLE.TEACHER.getRole_short()) || teacher.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short()) ) && 
    			(!userService.isBelongToClass(teacher.getId(), attendance.getClass_id()))
    		){
    		throw new ESchoolException("Invalid Class_ID:"+attendance.getClass_id()+", is not belong to current user_id:"+teacher.getId().intValue(), HttpStatus.BAD_REQUEST);
    	}
    	
    	return attendanceService.updateTransAttendance(teacher,attendance);
		 
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value = "/api/attendances/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delAttendance(
			 @PathVariable int  id,
			@Context final HttpServletResponse response
			 
			 ) {
		logger.info(" *** MainRestController.delAttendance/{attendance_id}:"+id);

		User auditor = getCurrentUser();
		Attendance attendance = attendanceService.findById(id);
		if (attendance == null){
			throw new ESchoolException("Not found", HttpStatus.NOT_FOUND);
		}
		if (auditor.getSchool_id().intValue() != attendance.getSchool_id().intValue()){
    		throw new ESchoolException("Current user and attendance_id are not in same School", HttpStatus.BAD_REQUEST);
    	}
		
			
    	if (	(auditor.hasRole(E_ROLE.TEACHER.getRole_short()) || auditor.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short()) ) && 
    			(!userService.isBelongToClass(auditor.getId(), attendance.getClass_id()))
    		){
    		throw new ESchoolException("Invalid Class_ID:"+attendance.getClass_id()+", is not belong to current user_id:"+auditor.getId().intValue(), HttpStatus.BAD_REQUEST);
    	}
    	attendance.setActflg("D");
    	attendanceService.updateAttachedAttendance(auditor,attendance);
	    return "Request was successfully, delete attendance of id: "+id;
	 }
	
	@Secured({ "ROLE_STUDENT"})
	@RequestMapping(value = "/api/attendances/request", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo requestAttendance(
				@RequestBody Attendance attendance,
				@RequestParam(value="from_dt", required =false) String from_dt,
				@RequestParam(value="to_dt", required =false) String to_dt,
				
				@Context final HttpServletRequest request,
				@Context final HttpServletResponse response			 
			 ) {
		logger.info(" *** MainRestController.requestAttendance Start");

		User student = getCurrentUser();
		attendance.setStudent_id(student.getId());
		attendance.setStudent_name(student.getFullname());
		
		if (from_dt != null ){
			Date dt = Utils.parsetDateAll(from_dt);// YYYY-MM-DD
			if (dt == null){
				throw new ESchoolException("Invalide filter_from_dt: "+from_dt,HttpStatus.BAD_REQUEST);
			}else{
				from_dt = Utils.dateToString(dt);
			}

		}
		if (to_dt != null ){
			Date dt = Utils.parsetDateAll(to_dt);// YYYY-MM-DD
			if (dt == null){
				throw new ESchoolException("Invalide to_dt: "+to_dt,HttpStatus.BAD_REQUEST);
			}else{
				to_dt = Utils.dateToString(dt);
			}

		}
		
		
		if (from_dt != null || to_dt != null){
			attendanceService.requestAttendanceEx(student,attendance,from_dt,to_dt);
		}else{
			attendanceService.requestAttendance(student,attendance,false,false);//inrange=false,is_sent_msg:false
		}
    	
    	RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
    	
		rsp.setMessageObject("Request was successfully");
		
	    return rsp;
	 }
	

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/attendances/rollup",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getRollUp(
			@RequestParam(value="filter_class_id",required =true) String filter_class_id,
			@RequestParam(value="filter_date", required =true) String filter_date,
			@RequestParam(value="filter_session_id", required =false) String filter_session_id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getRollUp");
		
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		Integer class_id =  Utils.parseInteger(filter_class_id);
		Integer session_id =  Utils.parseInteger(filter_session_id);
		// Get current school year
		SchoolYear schoolYear = schoolYearService.findLatestYearBySchool(user.getSchool_id());
		if (schoolYear == null ){
			throw new ESchoolException("Cannot get schoolyear of school_id: "+user.getSchool_id().intValue(),HttpStatus.BAD_REQUEST);
		}
		
	
		Date dt = Utils.parsetDateAll(filter_date);// YYYY-MM-DD
		if (dt == null){
			throw new ESchoolException("Invalide filter_date ",HttpStatus.BAD_REQUEST);
		}else{
			filter_date = Utils.dateToString(dt);
		}
		
		if (user.hasRole(E_ROLE.ADMIN.getRole_short())){
			
    	}else if (user.hasRole(E_ROLE.TEACHER.getRole_short()) || user.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short()) ){
    		if (class_id == null || class_id == 0 ){
    			throw new ESchoolException("User is not Admin, require filter_class_id to get Attendance Info ",HttpStatus.BAD_REQUEST);
    		}
    		if (!userService.isBelongToClass(user.getId(), class_id)){
    			throw new ESchoolException("User ID="+user.getId()+" is not belong to the class id = "+class_id,HttpStatus.BAD_REQUEST);
    		}
    		
    		EClass eclass = classService.findById(class_id);
    		if (eclass.getSchool_id().intValue() != school_id.intValue()){
    			if (!userService.isBelongToClass(user.getId(), class_id)){
        			throw new ESchoolException("User ID="+user.getId()+" and Class are not in same school,class_id= "+class_id,HttpStatus.BAD_REQUEST);
        		}
    		}
    		
    	}
    	
		
		RollUp rollup = new RollUp();
		
		ArrayList<Timetable> timetables = timetableService.findByDate(class_id, filter_date);
		ArrayList<Attendance> attendances = attendanceService.findAttendanceExt(school_id, class_id, null, null, 0,99999,null,filter_date,filter_date,session_id,null,schoolYear.getId());
		ArrayList<User> students = userService.findUserExt(school_id, 0, 99999, class_id, E_ROLE.STUDENT.getRole_short(), null, null);
		
		
		
		rollup.setAttendances(attendances);
		rollup.setTimetables(timetables);
		rollup.setStudents(students);
		
			// Query class by school id
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(rollup);
		
		    
	    return rsp;

	}
}
