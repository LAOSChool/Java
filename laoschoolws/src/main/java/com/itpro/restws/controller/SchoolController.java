package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.dao.SchoolExamDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.School;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
import com.itpro.restws.service.SchoolExamService;
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
	
	protected static final Logger logger = Logger.getLogger(SchoolController.class);
	@Autowired
	protected SchoolExamDao schoolExamDao;
	@Autowired
	SchoolExamService schoolExamService;
	
	@Autowired
	protected SchoolYearService schoolYearService;
	
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_STUDENT","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/schools",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo  getSchools(
			@Context final HttpServletRequest request
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		User me = getCurrentUser();
		 RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		School school = schoolService.findById(me.getSchool_id());
		rsp.setMessageObject(school);
	   	return rsp;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_STUDENT","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value = "/api/schools/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public School getSchool(
				@PathVariable int  id,
				@Context final HttpServletResponse response) {
		logger.info(" *** getSchool() START /{id}:"+id);
		
		User me = getCurrentUser();
		if (me.getSchool_id().intValue() == id){
			School school = schoolService.findById(Integer.valueOf(id));
		    return school;
		}else{
			throw new ESchoolException("Current user cannot get other school info, user.school_id="+me.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
		}
	 }
	
	//@Secured("ROLE_ANONYMOUS")
	@RequestMapping("/non-secure/test")
	public String test() {
		System.out.println(" *** UserController.test");
		// Spring Security dependency is unwanted in controller, typically some @Component (UserContext) hides it.
		// Not that we don't use Spring Security annotations anyway...
		return "SecurityContext: " + SecurityContextHolder.getContext();
	}
	
	
	//@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(value = "/non-secure/schools/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public School nonsecureGetSchool(
				@PathVariable int  id,
				@Context final HttpServletResponse response) {
		
		logger.info(" *** nonsecureGetSchool() START /{id}:"+id);
		School school= schoolService.findById(Integer.valueOf(id));
	    return school;
	 }

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/schools/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public School updateSchool(
			@RequestBody School school,
			@Context final HttpServletResponse response
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		if (school.getId() == null ){
			throw new ESchoolException("schoo.id = null", HttpStatus.BAD_REQUEST);
		}
		
		User me = getCurrentUser();
		
		if (me.getSchool_id().intValue() != school.getId().intValue()){
			throw new ESchoolException("school.id is not belong to current user - current_user.school_id:"+me.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		 return schoolService.updateTransSchool(me,school);
	}
	
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_STUDENT","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/schools/exams",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getSchoolExams(
			@RequestParam(value="filter_school_id",required =false) Integer filter_school_id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
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
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_STUDENT","ROLE_CLS_PRESIDENT" })	
	@RequestMapping(value="/api/schools/exams/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getSchoolExam(
			 @PathVariable Integer  id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) 
	{
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		//	 Get User info 
			User user = getCurrentUser();
			SchoolExam ret= schoolExamService.findById(id);
			if (ret == null ){
				throw new ESchoolException("id not existing", HttpStatus.BAD_REQUEST);
			}
			if (ret.getSchool_id().intValue() != user.getSchool_id().intValue()){
				throw new ESchoolException("SchooID of user is not same with exam", HttpStatus.BAD_REQUEST);
			}	
		
			
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			rsp.setMessageObject(ret);
		    return rsp;
		}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_STUDENT","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/schools/years/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getSchoolYear(
			 @PathVariable Integer  id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		//	 Get User info 
			User user = getCurrentUser();
			SchoolYear ret= schoolYearService.findById(id);
			if (ret == null ){
				throw new ESchoolException("id not existing", HttpStatus.BAD_REQUEST);
			}
			if (ret.getSchool_id().intValue() != user.getSchool_id().intValue()){
				throw new ESchoolException("SchooID of user is not same with SchoolID of exam", HttpStatus.BAD_REQUEST);
			}
			
			
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			rsp.setMessageObject(ret);
		    return rsp;
		}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_STUDENT","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/schools/years",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getSchoolYears(
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
			User current_user = getCurrentUser();
			
			ArrayList<SchoolYear> years = schoolYearService.findBySchool(current_user.getSchool_id());
				
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			
			rsp.setMessageObject(years);
		    return rsp;
		}
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_STUDENT","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/schools/terms",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getCurrentTerm(
			@RequestParam(value="filter_year_id",required =false) Integer filter_year_id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
	    User user = getCurrentUser();
	    ArrayList<SchoolTerm> terms = new ArrayList<SchoolTerm>();
	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
	    
	    if (filter_year_id == null || filter_year_id.intValue() <=0){
	    	SchoolTerm schoolTerm = schoolTermService.findMaxActiveTermBySchool(user.getSchool_id());
	    	if (schoolTerm != null ){
	    		terms.add(schoolTerm);
	    	}
	    }else{
	    	terms = schoolTermService.findAllTermByYear(user.getSchool_id(), filter_year_id);
	    }
	    rsp.setMessageObject(terms);
		
	    return rsp;
	    
	 }
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/schools/exams/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo createSchoolExam(
			@RequestBody SchoolExam schoolExam,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		User user = getCurrentUser();
		SchoolExam ret = schoolExamService.insertSchoolExam(user, schoolExam);
		rsp.setMessageObject(ret);
		
	    return rsp;
		 
	}
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/schools/exams/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo updateSchoolExam(
			@RequestBody SchoolExam schoolExam,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		User user = getCurrentUser();
		SchoolExam ret = schoolExamService.updateTransSchoolExam(user, schoolExam);
		rsp.setMessageObject(ret);
		
	    return rsp;
		 
	}
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/api/schools/exams/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo deleteSchoolExam(
			 @PathVariable Integer  id,
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 
			 ) {
		
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		User user = getCurrentUser();
		schoolExamService.delById(user, id);
		rsp.setMessageObject("SUCCESS");
		
	    return rsp;
	 }
	
	
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/schools/years/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo createSchoolYear(
			@RequestBody SchoolYear schoolYear,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		User user = getCurrentUser();
		SchoolYear ret = schoolYearService.insertSchoolYear(user, schoolYear);
		rsp.setMessageObject(ret);
		
	    return rsp;
		 
	}
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/schools/years/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo updateSchoolYear(
			@RequestBody SchoolYear schoolYear,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		User user = getCurrentUser();
		if (schoolYear.getId() == null ){
			throw new ESchoolException("schoolYear.id = NULL", HttpStatus.BAD_REQUEST);
		}
		if (schoolYear.getSchool_id().intValue() != user.getSchool_id().intValue()){
			throw new ESchoolException("schoolYear.school_id != user.schoool_id", HttpStatus.BAD_REQUEST);
		}
		SchoolYear ret = schoolYearService.updateTransSchoolYear(user, schoolYear);
		rsp.setMessageObject(ret);
		
	    return rsp;
		 
	}
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/api/schools/years/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo deleteSchoolYear(
			 @PathVariable Integer  id,
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 
			 ) {
		
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		User user = getCurrentUser();
		schoolYearService.delSchoolYear(user, id);
		rsp.setMessageObject("SUCCESS");
		
	    return rsp;
	 }
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/schools/upload_photo",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo upload_photo(
			@RequestParam(value = "file",required =false) MultipartFile[] files,
			 @Context final HttpServletRequest request)
			 {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		User me = getCurrentUser();
		
		schoolService.saveUploadPhoto(me, files);
		
		return rsp;
		 
	}
	

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/schools/terms/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo createTerm(
			@RequestBody SchoolTerm schoolTerm,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		User admin = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		schoolTermService.insertSchoolTerm(admin, schoolTerm);
		rsp.setMessageObject(schoolTerm);
		return rsp;
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/schools/terms/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo updateTerm(
			@RequestBody SchoolTerm schoolTerm,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		User admin = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		schoolTermService.updateTransSchoolTerm(admin, schoolTerm);
		rsp.setMessageObject(schoolTerm);
		return rsp;
	}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_STUDENT","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value = "/api/schools/terms/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo getTerm(@PathVariable Integer  id,
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 ) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		User me = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		SchoolTerm term = schoolTermService.findTermById(me, id);
		if (term == null ){
			throw new ESchoolException("id not existing", HttpStatus.BAD_REQUEST);
		}
		if (term.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("SchooID of user is not same with term", HttpStatus.BAD_REQUEST);
		}	
		rsp.setMessageObject(term);
		return rsp;
	 }

	
}
