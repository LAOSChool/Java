package com.itpro.restws.controller;

import java.util.ArrayList;
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

import com.itpro.restws.dao.SchoolExamDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.helper.SchoolTerm;
import com.itpro.restws.model.School;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.SchoolYear;
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
public class SchoolController extends BaseController {
	
	
	@Autowired
	protected SchoolExamDao schoolExamDao;
	
	@Autowired
	protected SchoolYearService schoolYearService;
	
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(value="/api/schools",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public List<School> getSchools(
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			@RequestParam(value="filter_user_id",required =false) String filter_user_id,			
			@RequestParam(value="filter_sts", defaultValue="Active",required =false) String filter_sts
			) {
		
		logger.info(" *** MainRestController.getSchools");
	   	return schoolService.findActive();
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(value = "/api/schools/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public School getSchool(
				@PathVariable int  id,
				@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getSchool/{id}:"+id);
		School school = null;

	    response.setStatus(HttpServletResponse.SC_OK);
	    try {
	    	school = schoolService.findById(Integer.valueOf(id));
			logger.info("Schoo : "+school.toString());
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController.ERROR:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }
	    finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    return school;
	 }

	@Secured({ "ROLE_SYS_ADMIN" })
	@RequestMapping(value="/api/schools/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public School createSchool(
			@RequestBody School school,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.school.create");

		 return schoolService.insertSchool(school);
		 
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/schools/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public School updateSchool(
			@RequestBody School school,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.schools.update");
		User user = getCurrentUser();
		
		if (user.getSchool_id().intValue() != school.getId().intValue()){
			throw new ESchoolException("school.id is not belong to current user - current_user.school_id:"+user.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		 return schoolService.updateSchool(school);
	}
	
	
	
	
	@Secured({ "ROLE_SYS_ADMIN" })
	@RequestMapping(value = "/api/schools/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delSchool(
			 @PathVariable int  id,
			@Context final HttpServletResponse response
			 
			 ) {
		logger.info(" *** MainRestController.delSchool/{school_id}:"+id);
		School school = schoolService.findById(id);
		school.setActflg("D");
		schoolService.updateSchool(school);
	    return "Request was successfully, delete school of id: "+id;
	 }
	

	@RequestMapping(value="/api/schools/exams",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getSchoolExams(
			@RequestParam(value="filter_school_id",required =false) Integer filter_school_id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
		//	 Get User info 
			User user = getCurrentUser();
			Integer school_id = null;
			if (user.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
				school_id = filter_school_id;
				if (school_id == null) {
					throw new ESchoolException("filter_school_id is required", HttpStatus.BAD_REQUEST);
				}
			}else{
				school_id = user.getSchool_id();
			}
					
			
			ArrayList<SchoolExam> schoolExams =  (ArrayList<SchoolExam>) schoolExamDao.findBySchool(school_id, 0,999999);
			
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			rsp.setMessageObject(schoolExams);
		    return rsp;
		}


	
	
	@RequestMapping(value="/api/schools/years",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getSchoolYears(
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
			
			User current_user = getCurrentUser();
			
			ArrayList<SchoolYear> years = schoolYearService.findBySchool(current_user.getSchool_id());
				
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			
			rsp.setMessageObject(years);
		    return rsp;
		}
	
	@RequestMapping(value="/api/schools/terms",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getCurrentTerm(
			@RequestParam(value="filter_year_id",required =true) Integer filter_year_id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		
		
		logger.info(" *** getCurrentTerm Start");
	    User user = getCurrentUser();
	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
	    
	    ArrayList<SchoolTerm> terms = schoolYearService.findTermByYear(user.getSchool_id(), filter_year_id);
	    rsp.setMessageObject(terms);
		
	    return rsp;
	    
	 }
	
}
