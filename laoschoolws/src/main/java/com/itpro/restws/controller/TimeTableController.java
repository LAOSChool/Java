package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.http.HttpStatus;
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
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.MSubject;
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
public class TimeTableController extends BaseController {
	

	@RequestMapping(value="/api/timetables",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt  getTimetables() {
		logger.info(" *** MainRestController.getTimetables");
		
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		int school_id = 1;//TODO: get from token => user info
		ListEnt listResp = new ListEnt();
		
    	// Count user
    	total_row = timetableService.countBySchoolID(school_id);
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
    		
		logger.info("Timetable count: total_row : "+total_row);
		// Query class by school id
		ArrayList<Timetable> examResults = timetableService.findBySchool(school_id, from_row, max_result);
		
		listResp.setList(examResults);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
	    return listResp;

	}

	
	@RequestMapping(value="/api/timetables/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public Timetable getTimetable(@PathVariable int  id) 
	{
		logger.info(" *** MainRestController.getTimetable/{id}:"+id);
		return timetableService.findById(id);
	 }
	
	
	
	@RequestMapping(value="/api/timetables/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public Timetable createTimetable(
			@RequestBody Timetable timetable
			) {
		logger.info(" *** MainRestController.ceateTimetable.create");
		//timetable.setId(100);//TODO:Test
		return timetableService.insertTimetable(timetable);
		 
		 
	}
	
	@RequestMapping(value="/api/timetables/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public Timetable updateTimetable(
			@RequestBody Timetable timetable
			) {
		logger.info(" *** MainRestController.updateTimetable.update");
		 //return timetable;
		return timetableService.updateTimetable(timetable);
		 
	}
	
	@RequestMapping(value = "/api/timetables/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delTimetable(
			 @PathVariable int  id
			 ) {
		logger.info(" *** MainRestController.delTimetable/{id}:"+id);

	    return "Request was successfully, delete delTimetable of id: "+id;
	 }
	
	
	@RequestMapping(value="/api/timetables/subjects",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo  getSubjects(
			@RequestParam(value="filter_class_id",required =true) String filter_class_id,
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getSubjects START");
		
		User user = getCurrentUser();
		
		
		Integer class_id = Utils.parseInteger(filter_class_id);
		
		if (class_id == null || class_id.intValue() == 0){
			throw new ESchoolException("Cannot parse filter_class_id to Integer", HttpStatus.BAD_REQUEST);
		}
		
		EClass eclass = classService.findById(class_id);
		if (eclass == null || eclass.getSchool_id() != user.getSchool_id()){
			throw new ESchoolException("Class_ID is not correct, not belong to school of current user:"+ filter_class_id, HttpStatus.BAD_REQUEST);
		}
		
		if (!user.hasRole(E_ROLE.ADMIN.getRole_short())) {
			if (!userService.isBelongToClass(user.getId(), class_id)){
				throw new ESchoolException("User is not belong to class_id:"+ filter_class_id, HttpStatus.BAD_REQUEST);
			}
		}
		
		// Query class by school id
		ArrayList<MSubject> list_subjects = timetableService.findSubjectOfClass(class_id);
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		rsp.setMessageObject(list_subjects);
	    return rsp;

	}
	

}
