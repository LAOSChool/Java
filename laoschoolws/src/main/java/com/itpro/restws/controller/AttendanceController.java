package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

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
import com.itpro.restws.model.Timetable;
import com.itpro.restws.model.User;
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
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/attendances",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getAttendances(
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			@RequestParam(value="filter_user_id",required =false) String filter_user_id,			
			@RequestParam(value="filter_from_id", required =false) String filter_from_id,
			@RequestParam(value="filter_from_dt", required =false) String filter_from_dt,
			@RequestParam(value="filter_to_dt", required =false) String filter_to_dt,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getAttendances");
		
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		Integer class_id =  Utils.parseInteger(filter_class_id);
		Integer student_id = Utils.parseInteger(filter_user_id);
		
		
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
    		
    	}else{
    		// STUDENT
    		student_id = user.getId();
    	}
    	
		
		List<Attendance> attendances = null;
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
				
		ListEnt rspEnt = new ListEnt();
	    try {
	    	// Count user
	    	total_row = attendanceService.countAttendanceExt(school_id, class_id, student_id, Utils.parseInteger(filter_from_id),null,filter_from_dt,filter_to_dt,null);
	    	if (total_row > Constant.MAX_RESP_ROW){
	    		max_result = Constant.MAX_RESP_ROW;
	    	}else{
	    		max_result = total_row;
	    	}
	    		
			logger.info("Attendance count: total_row : "+total_row);
			// Query class by school id
			attendances = attendanceService.findAttendanceExt(school_id, class_id, student_id, Utils.parseInteger(filter_from_id), from_row, max_result,null,filter_from_dt,filter_to_dt,null);
		    rspEnt.setList(attendances);
		    rspEnt.setFrom_row(from_row);
		    rspEnt.setTo_row(from_row + max_result);
		    rspEnt.setTotal_count(total_row);
		    
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController.getAttendances() ERROR:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    
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
    	
    	return attendanceService.updateAttendance(teacher,attendance);
		 
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
    	attendanceService.updateAttendance(auditor,attendance);
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
		
		if (from_dt != null || to_dt != null){
			attendanceService.requestAttendanceEx(student,attendance,from_dt,to_dt);
		}else{
			attendanceService.requestAttendance(student,attendance,false);
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
		
		Date att_dt = Utils.parsetDate(filter_date);// YYYY-MM-DD
		if (att_dt == null){
			throw new ESchoolException("Invalide filter_date ",HttpStatus.BAD_REQUEST);
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
		ArrayList<Attendance> attendances = attendanceService.findAttendanceExt(school_id, class_id, null, null, 0,99999,null,filter_date,filter_date,session_id);
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
