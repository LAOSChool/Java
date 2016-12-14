package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.User;
import com.itpro.restws.service.ApiKeyService;
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
public class ApiKeyController  extends BaseController {
	private static final Logger logger = Logger.getLogger(ApiKeyController.class);
	
	@Autowired
	protected ApiKeyService apiKeyService;
	
	
	@Secured({ "ROLE_ADMIN","ROLE_SYS_ADMIN"})
	@RequestMapping(value = "/api/api_keys/{sso_id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	 public RespInfo getApiKeys(
			 @PathVariable String sso_id,
			 
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 ) {
		logger.info(" *** getApiKeys Start, sso_id="+sso_id);
		User me = getCurrentUser();
		
		apiKeyService.validGetApiKey(me, sso_id);
		
		ArrayList<ApiKey> list = apiKeyService.findBySsoID(sso_id);
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(list);
	    return rsp;	
	 }

		
	@RequestMapping(value="/api/tokens/save",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo saveToke(
			@RequestHeader String token,
			@RequestHeader String api_key,
			@RequestHeader String auth_key,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** saveToke START");
		User user = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		apiKeyService.saveFireBaseToken(user.getSso_id(), api_key, auth_key, token);
		rsp.setMessageObject("SUCCESS");
		
	    return rsp;	
		 
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value="/api/api_keys",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getUserInfo(
			@RequestParam(value="filter_role",required =false) String filter_role,
			@RequestParam(value="filter_sso_id",required =false) String filter_sso_id,
			@RequestParam(value="filter_class_id", required =false) Integer filter_class_id,
			
			@RequestParam(value="from_row",required =false) Integer filter_from_row,
			@RequestParam(value="max_result",required =false) Integer filter_max_result,

			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** getUserInfo() START");
		
		
		User me = getCurrentUser();
		
	  
    	// Count user
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
    	Integer total_row = apiKeyService.countByExt(me, filter_class_id, filter_sso_id, filter_role, 1, null,null);
    	
    	if( (total_row <=  0) || (from_row > total_row) ||  max_result<=0) {
    		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
    		rsp.setMessageObject(null);
    	    return rsp;
    	}
    	
    	if ((from_row + max_result > total_row)){
    		max_result = total_row-from_row;
    	}
    	logger.info("total_row : "+total_row);
    	logger.info("from_row : "+from_row);
    	logger.info("max_result : "+max_result);
		
	  
    	ArrayList<ApiKey> list = apiKeyService.findByExt(me, filter_class_id, filter_sso_id, filter_role, 1, null, null, from_row, max_result);
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(list);
		
	    return rsp;	

	}
	
}
