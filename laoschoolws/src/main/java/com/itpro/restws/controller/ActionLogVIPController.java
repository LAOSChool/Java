package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.Date;

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

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ActionLogVIP;
import com.itpro.restws.model.User;
import com.itpro.restws.service.ActionLogVIPService;
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
public class ActionLogVIPController extends BaseController {
	private static final Logger logger = Logger.getLogger(ActionLogVIPController.class);
	
	@Autowired
	private ActionLogVIPService actionLogVIPService;;
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/logs",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getLogs(
			@RequestParam(value="from_row",required =false) Integer filter_from_row,
			@RequestParam(value="max_result",required =false) Integer filter_max_result,
						
			@RequestParam(value="filter_sso_id",required =false) String filter_sso_id,			
			@RequestParam(value="filter_from_dt", required =false) String filter_from_dt,
			@RequestParam(value="filter_to_dt", required =false) String filter_to_dt,
			
			@RequestParam(value="filter_from_time", required =false) Long filter_from_time,
			@RequestParam(value="filter_to_time", required =false) Long filter_to_time,			
			
			@RequestParam(value="filter_type", required =false) String filter_type,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath().toString(), "Successful");
		// Load user
		User me = getCurrentUser();
		
		// Parsing params
		int total_row = 0;
		
	  
    	// Count user
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
		// Validation
		// From date
		if (filter_from_time != null && filter_from_time.longValue() > 0){
			if (filter_from_dt != null ){
				throw new ESchoolException("Cannot not input both filter_from_dt AND filter_from_time", HttpStatus.BAD_REQUEST);
			}
			filter_from_dt = Utils.numberToDateTime(filter_from_time);
		}else if (filter_from_dt != null ){
			Date dt = Utils.parsetDateAll(filter_from_dt);// YYYY-MM-DD
			if (dt == null){
				throw new ESchoolException("Invalide filter_from_dt: "+filter_from_dt,HttpStatus.BAD_REQUEST);
			}else{
				filter_from_dt = Utils.dateToString(dt);
			}

		}
		// To date
		
		if (filter_to_time != null && filter_to_time.longValue() > 0){
			if (filter_to_dt != null ){
				throw new ESchoolException("Cannot not input both filter_to_dt AND filter_to_time", HttpStatus.BAD_REQUEST);
			}
			filter_to_dt = Utils.numberToDateTime(filter_to_time);
		}else if (filter_to_dt != null ){
			Date dt = Utils.parsetDateAll(filter_to_dt);// YYYY-MM-DD
			if (dt == null){
				throw new ESchoolException("Invalide filter_to_dt: "+filter_to_dt,HttpStatus.BAD_REQUEST);
			}else{
				filter_to_dt = Utils.dateToString(dt);
			}

		}

		// Count
    	total_row = actionLogVIPService.countActionLogExt(me.getSchool_id(),filter_sso_id,filter_from_dt,filter_to_dt,filter_type);
    	if( (total_row <=  0) || (from_row > total_row) ||  max_result<=0) {
    		rsp.setMessageObject(null);
    		return rsp;
    	}
    	
    	if ((from_row + max_result > total_row)){
    		max_result = total_row-from_row;
    	}
    	logger.info("total_row : "+total_row);
    	logger.info("from_row : "+from_row);
    	logger.info("max_result : "+max_result);
    	
    		
    	ListEnt lstEnt = new ListEnt();
		// Query class by school id
		ArrayList<ActionLogVIP> list = actionLogVIPService.findActionLogExt(me.getSchool_id(),from_row,max_result, filter_sso_id,filter_from_dt,filter_to_dt,filter_type);
		
		lstEnt.setList(list);
		lstEnt.setFrom_row(from_row);
		lstEnt.setTo_row(from_row + max_result);
		lstEnt.setTotal_count(total_row);
	    rsp.setMessageObject(lstEnt);
	  
	    return rsp;

	}
	
	
}
