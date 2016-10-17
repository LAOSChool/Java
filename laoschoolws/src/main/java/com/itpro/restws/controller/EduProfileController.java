package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.dao.EduProfileDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.EduProfile;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
import com.itpro.restws.service.EduProfileService;
import com.itpro.restws.service.ExamResultService;
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
public class EduProfileController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(EduProfileController.class);
	
	@Autowired
	protected ExamResultService examResultService;
	
	@Autowired
	protected EduProfileDao eduProfileDao;
	
	@Autowired
	protected EduProfileService eduProfileService;
	
	@Autowired
	protected SchoolYearService schoolYearService;
	
	// Hien thi ca du lieu Ao va co trong DB
	@Secured({ "ROLE_STUDENT"})
	@RequestMapping(value="/api/edu_profiles/myprofile",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getMyProfile(
			@RequestParam(value="filter_year_id",required =false) Integer filter_year_id,
			
			 @Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		//	 Get User info 
			User me = getCurrentUser();
			ArrayList<EduProfile> profiles =  eduProfileService.getUserProfile(me, filter_year_id);
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			rsp.setMessageObject(profiles);
		    return rsp;
		}

	/***
	 * Do tu dong Generate Profile neu ko ton tain
	 * Neu ko co filter from_row/max_result
	 * 
	 * @param filter_student_id
	 * @param filter_class_id
	 * @param filter_year_id
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured({ "ROLE_ADMIN","ROLE_TEACHER"})
	@RequestMapping(value="/api/edu_profiles",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getEduProfile(
			@RequestParam(value="filter_student_id",required =false) Integer filter_student_id,
			@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			@RequestParam(value="filter_year_id",required =false) Integer filter_year_id,
			
			 @Context final HttpServletRequest request,
			 @Context final HttpServletResponse response
			
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
			User teacher = getCurrentUser();
			
			ArrayList<EduProfile> list = eduProfileService.getClassProfile(teacher, filter_class_id, filter_student_id, filter_year_id);
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			rsp.setMessageObject(list);
		    return rsp;
		}
	
	@Secured({"ROLE_ADMIN","ROLE_TEACHER","ROLE_STUDENT"})
	@RequestMapping(value="/api/edu_profiles/years",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getSchoolYears(
			@RequestParam(value="filter_user_id",required =false) Integer filter_user_id,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
			
			User current_user = getCurrentUser();
			
			if (current_user.hasRole(E_ROLE.STUDENT.getRole_short())){
//				throw new ESchoolException(" user:"+current_user.getId().intValue()+" is a STUDENT, cannot access to this data of other user_id: "+filter_user_id.intValue(), HttpStatus.BAD_REQUEST);
				filter_user_id = current_user.getId();
			}else{
				if (filter_user_id == null){
					throw new ESchoolException(" filter_user_id is required", HttpStatus.BAD_REQUEST);
				}
			}
			
			User filter_user= userService.findById(filter_user_id);
			if (filter_user == null){
				throw new ESchoolException(" filter_user_id is not existing:"+filter_user_id.intValue(), HttpStatus.BAD_REQUEST);
			}
			if (filter_user.getSchool_id().intValue() != current_user.getSchool_id().intValue()){
				throw new ESchoolException(" Current user school_id:"+current_user.getSchool_id().intValue()+" is differ from filter_user: "+filter_user.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
			}
			
			if (!filter_user.hasRole(E_ROLE.STUDENT.getRole_short())){
				throw new ESchoolException(" user is not STUDENT:"+filter_user_id.intValue(), HttpStatus.BAD_REQUEST);
			}
			
			ArrayList<SchoolYear> years = eduProfileService.findSchoolYearByStudentID(filter_user_id);
				
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			
			rsp.setMessageObject(years);
		    return rsp;
		}

	
	
}
