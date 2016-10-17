package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.SchoolTerm;
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
public class SchoolTermController extends BaseController {
	protected static final Logger logger = Logger.getLogger(SchoolTermController.class);
	
	@RequestMapping(value="/api/terms",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getTerms(
			
			@RequestParam(value="filter_year_id",required =false) Integer filter_year_id,
			@RequestParam(value="filter_actived",required =false) Integer filter_actived,			
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		User me = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		ArrayList<SchoolTerm> list = schoolTermService.findTermExt(me, filter_year_id,filter_actived);
		rsp.setMessageObject(list);
		return rsp;
	}
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/api/terms/activate/{id}/{actived}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo activateTerm(
			 @PathVariable Integer  id,
			 @PathVariable Integer  actived,
			 
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 ) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		User me = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		SchoolTerm term = schoolTermService.activeSchoolTerm(me, id, actived);
		rsp.setMessageObject(term);
		return rsp;
	 }
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/terms/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo createTerm(
			@RequestBody SchoolTerm schoolTerm,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		User me = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		schoolTermService.insertSchoolTerm(me, schoolTerm);
		rsp.setMessageObject(schoolTerm);
		return rsp;
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/terms/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo updateTerm(
			@RequestBody SchoolTerm schoolTerm,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		User me = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		schoolTermService.updateTransSchoolTerm(me, schoolTerm);
		rsp.setMessageObject(schoolTerm);
		return rsp;
	}

	@RequestMapping(value = "/api/terms/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo getTerm(@PathVariable Integer  id,
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 ) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		User me = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		SchoolTerm term = schoolTermService.findTermById(me, id);;
		rsp.setMessageObject(term);
		return rsp;
	 }
	
	 
}