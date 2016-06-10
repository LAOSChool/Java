package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

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
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EduProfile;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.User;
import com.itpro.restws.service.ExamResultService;
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
	
	
	
	@Autowired
	protected ExamResultService examResultService;
	
	@Autowired
	protected EduProfileDao eduProfileDao;
	
	
	
	@Secured({ "ROLE_STUDENT"})
	@RequestMapping(value="/api/edu_profile/myprofile",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getMyProfile(
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			 @Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
			// Valid class ID
			Integer class_id = Utils.parseInteger(filter_class_id);
			if (class_id == null){
				throw new ESchoolException(" filter_class_id  is required !", HttpStatus.BAD_REQUEST);
				
			}
			
			User student = getCurrentUser();
			if (!student.is_belong2class(class_id)){
				throw new ESchoolException(" user:"+student.getId()+" is not belong to class_id: "+class_id.intValue(), HttpStatus.BAD_REQUEST);
			}
			// Get school year
			// SchoolYear school_year = schoolYearDao.findLastestOfSchoolId(student.getSchool_id());
			
			EduProfile profile = eduProfileDao.findLatestProfile(student.getId(), student.getSchool_id(), class_id);
			if (profile != null){
				ArrayList<ExamResult> list = examResultService.getUserProfile_Mark(student, class_id, null, true);
				profile.setExamProfiles(list);
			}
			
					
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			
			rsp.setMessageObject(profile);
		    return rsp;
		}
	
}