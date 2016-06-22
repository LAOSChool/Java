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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
import com.itpro.restws.service.EduProfileService;
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
public class SchoolYearController extends BaseController {
	@Autowired
	protected EduProfileService eduProfileService;
	@Secured({"ROLE_STUDENT"})
	@RequestMapping(value="/api/school_years/myprofile",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo getSchoolYears(
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			
			) {
			
			User student = getCurrentUser();
			ArrayList<SchoolYear> years = eduProfileService.findSchoolYearByStudentID(student.getId());
				
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
			
			rsp.setMessageObject(years);
		    return rsp;
		}
	
	 
}