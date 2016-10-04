package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.RespInfo;
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
	public ListEnt getClasses(
			    @RequestParam(value="from_row",required =false) Integer filter_from_row,
	            @RequestParam(value="max_result",required =false) Integer filter_max_result,			
			@Context final HttpServletResponse response,
			@Context final HttpServletRequest request
			) {
		logger.info(" *** ClassController.getClasses");
		List<EClass> classes = null;
		int total_row = 0;
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		
		ListEnt rspEnt = new ListEnt();
	    try {
	    	
	    	 // Count user
            int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
            int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
             
            // Count user
            total_row = classService.countBySchoolID(school_id);
             
            if( (total_row <=  0) || (from_row > total_row) ||   max_result<=0) {
                rspEnt.setList(null);
                rspEnt.setFrom_row(0);
                rspEnt.setTo_row(0);
                rspEnt.setTotal_count(0);
                return rspEnt;
            }
            if ((from_row + max_result > total_row)){
                max_result = total_row-from_row;
            }
            logger.info("ClassController : total_row : "+total_row);
            logger.info("ClassController : from_row : "+from_row);
            logger.info("ClassController: max_result : "+max_result);
                 
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
	    
    	User user = getCurrentUser();
    	eclass = classService.findById(Integer.valueOf(id));
    	
    	if (eclass == null ){
    		throw new ESchoolException("id not existing",HttpStatus.BAD_REQUEST);
    	}
    	if (user.getSchool_id().intValue() != eclass.getSchool_id().intValue()){
    		throw new ESchoolException("Eclass is not in same school with current user",HttpStatus.BAD_REQUEST);
    	}
	   
	    return eclass;
	 }
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/classes/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public EClass createClass(
			@RequestBody EClass eclass,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.create");
		
		
		if (eclass.getId() != null ){
			
			throw new ESchoolException("Cannot create new Class, id != null", HttpStatus.BAD_REQUEST);
		}
		User admin = getCurrentUser();
		 return classService.newClass(admin, eclass);
		 
	}
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/classes/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public EClass updateClass(
			@RequestBody EClass eclass,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.classes.update");
		User admin = getCurrentUser();
		// eclass = classService.findById(1);
		 return classService.updateTransClass(admin,eclass);
		 
	}
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/api/classes/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public EClass delClass(
			@PathVariable int id,
			
			@Context final HttpServletResponse response
			 
			 ) {
		
		logger.info(" *** MainRestController.delClass/{class_id}:"+id);
		User me = getCurrentUser();
		return classService.delClass(me,id);
	 }
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/api/classes/users", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo getUsers(
			 @RequestParam(value="class_id", required =true) Integer class_id,
				@RequestParam(value="filter_role", required =false) String filter_role,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			 
			 ) {
		
		logger.info(" *** MainRestController.getUsers/{class_id}:"+class_id.intValue());
		User me = getCurrentUser();
		
		EClass eclass = classService.findById(class_id);
		if (eclass == null ){
			throw new ESchoolException("id not existing:"+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (eclass.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("eclass.school_id != me.school_id", HttpStatus.BAD_REQUEST);
		}
		ArrayList<User> users = new ArrayList<User>();
		Set<User> setUsers = eclass.getUserByRoles(filter_role);
		if (setUsers != null && setUsers.size() > 0){
			for (User usr : setUsers){
				users.add(usr);
			}	
		}
		
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(users);
		return rsp;
	 }
	
	 
}