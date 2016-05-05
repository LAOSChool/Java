package com.itpro.restws.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Attendance;
import com.itpro.restws.model.EClass;
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
public class AttedanceController extends BaseController {
	
	@RequestMapping(value="/api/attendances",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getAttendances(
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			@RequestParam(value="filter_user_id",required =false) String filter_user_id,			
			@RequestParam(value="filter_from_id", required =false) String filter_from_id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getAttendances");
		
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		Integer class_id =  Utils.parseInteger(filter_class_id);
		Integer user_id = Utils.parseInteger(filter_user_id);
		
		
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
    		user_id = user.getId();
    	}
    	
		
		List<Attendance> attendances = null;
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
				
		ListEnt rspEnt = new ListEnt();
	    try {
	    	// Count user
	    	total_row = attendanceService.countAttendanceExt(school_id, class_id, user_id, Utils.parseInteger(filter_from_id));
	    	if (total_row > Constant.MAX_RESP_ROW){
	    		max_result = Constant.MAX_RESP_ROW;
	    	}else{
	    		max_result = total_row;
	    	}
	    		
			logger.info("Attendance count: total_row : "+total_row);
			// Query class by school id
			attendances = attendanceService.findAttendanceExt(school_id, class_id, user_id, Utils.parseInteger(filter_from_id), from_row, max_result);
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
	
	
	@RequestMapping(value = "/api/attendances/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public Attendance getAttendance(
			 @PathVariable int  id,
			 @Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getAttendance/{id}:"+id);
		Attendance attendance = null;
	    try {
	    	attendance = attendanceService.findById(Integer.valueOf(id));
			logger.info("attendance: "+attendance.toString());
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController  ERROR:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }
	    finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    return attendance;
	 }
	
	
	@RequestMapping(value="/api/attendances/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public Attendance createAttendance(
			@RequestBody Attendance attendance,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.create");
		
//		 attendance = attendanceService.findById(1);
		 return attendanceService.insertAttendance(attendance);
		 
	}
	
	@RequestMapping(value="/api/attendances/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public Attendance updateAttendances(
			@RequestBody Attendance attendance,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.attendances.update");
			 //attendance = attendanceService.findById(1);
			 return attendanceService.updateAttendance(attendance);
		 
	}
	
	@RequestMapping(value = "/api/attendances/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delAttendance(
			 @PathVariable int  id,
			@Context final HttpServletResponse response
			 
			 ) {
		logger.info(" *** MainRestController.delAttendance/{attendance_id}:"+id);

	    return "Request was successfully, delete attendance of id: "+id;
	 }
	
}
