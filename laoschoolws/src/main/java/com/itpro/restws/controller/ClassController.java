package com.itpro.restws.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ListEnt;
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
public class ClassController extends BaseController {
	
	
	@RequestMapping(value="/api/classes",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getClasses(@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getClasses");
		List<EClass> classes = null;
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		
		ListEnt rspEnt = new ListEnt();
	    try {
	    	// Count user
	    	total_row = classService.countBySchoolID(school_id);
	    	if (total_row > Constant.MAX_RESP_ROW){
	    		max_result = Constant.MAX_RESP_ROW;;
	    	}else{
	    		max_result = total_row;
	    	}
	    		
			logger.info("Class count: total_row : "+total_row);
			// Query class by school id
			classes = classService.findBySchool(school_id, from_row, max_result);
		    rspEnt.setList(classes);
		    rspEnt.setFrom_row(from_row);
		    rspEnt.setTo_row(from_row + max_result);
		    rspEnt.setTotal_count(total_row);
		    
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController.getClasses() ERROR:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    
	    return rspEnt;

	}
	
	
	@RequestMapping(value = "/api/classes/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public EClass getClass(@PathVariable int  id,@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getClass/{id}:"+id);
		EClass eclass = null;
	    try {
	    	User user = getCurrentUser();
	    	eclass = classService.findById(Integer.valueOf(id));
	    	if (eclass != null && user.getSchool_id() != eclass.getSchool_id()){
	    		logger.info("Eclass is not in same school with current user");
	    		eclass = null;
	    	}
			logger.info("eclass: "+eclass.toString());
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
	    return eclass;
	 }
	
	
	@RequestMapping(value="/api/classes/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public EClass createClass(
			@RequestBody EClass eclass,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.create");
		// eclass = classService.findById(1);
		User admin = getCurrentUser();
		 return classService.newClass(admin, eclass);
		 
	}
	
	@RequestMapping(value="/api/classes/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public EClass updateClass(
			@RequestBody EClass eclass,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.classes.update");
		User admin = getCurrentUser();
		// eclass = classService.findById(1);
		 return classService.updateClass(admin,eclass);
		 
	}
	
	@RequestMapping(value = "/api/classes/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delClass(
			@PathVariable int id,
			@Context final HttpServletResponse response
			 
			 ) {
		logger.info(" *** MainRestController.delUser/{class_id}:"+id);
	    return "Request was successfully, delete class of id: "+id;
	 }
	
	
	 
}