package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
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
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Message;
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
public class MessageController extends BaseController {
	@RequestMapping(value="/api/messages",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	/***
	 * api/message
	- get user info (from context)
	- validation input parameters
	- get user's Permit

	- Create security filter 
	    if SCOPE = SCHOOL  then
	      filterMessage = School
	    if SCOPE = CLASS  then
	      filterMessage = School
	    if SCOPE = PERSON then
	      filterMessage = Person
	
	- Update more user filters conditions
	   

	 * @param filter_class_id
	 * @param filter_from_user_id
	 * @param filter_to_user_id
	 * @param filter_channel
	 * @param filter_is_read
	 * @param filter_from_dt
	 * @param filter_to_dt
	 * @param filter_from_row : paging
	 * @param filter_max_result : paging
	 * @param filter_from_id : from row ID
	 * @return
	 */
	public ListEnt getMessages(
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			@RequestParam(value="filter_from_user_id",required =false) String filter_from_user_id,			
			@RequestParam(value="filter_to_user_id",required =false) String filter_to_user_id,
			@RequestParam(value="filter_channel",required =false) String filter_channel,
			@RequestParam(value="filter_is_read", defaultValue="Active",required =false) String filter_is_read,
			@RequestParam(value="filter_from_dt",required =false) String filter_from_dt,			
			@RequestParam(value="filter_to_dt",required =false) String filter_to_dt	,
			@RequestParam(value="from_row",required =false) String filter_from_row,
			@RequestParam(value="max_result",required =false) String filter_max_result,
			@RequestParam(value="filter_from_id",required =false) String filter_from_id
			
			) {
		logger.info(" *** MainRestController.getMessages");
		
		ListEnt listResp = new ListEnt();
		// Parsing params
		int total_row = 0;
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
		
		// Load user
		User user = getCurrentUser();
		
		// Get data access right for entity
		MessageFilter secure_filter = messageService.secureGetMessages(user);
		
		
    	// Count messages
    	total_row = messageService.countMsgExt(
    			user.getSchool_id(), 
    			from_row, 
    			max_result, 
    			// Security filter
    			secure_filter,
    			// User filter    			
    			Utils.parseLong(filter_class_id),
    			filter_from_dt, 
    			filter_to_dt, 
    			Utils.parseLong(filter_from_user_id), 
    			Utils.parseLong(filter_to_user_id),
    			Utils.parseInteger(filter_channel), 
    			Utils.parseInteger(filter_is_read),
    			Utils.parseLong(filter_from_id)
    			);
    	
    	if (total_row <=  0){
    		listResp.setList(null);
    		listResp.setFrom_row(0);
    		listResp.setTo_row(0);
    		listResp.setTotal_count(0);
    		return listResp;
    	}
    	
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
    		
		logger.info("Message count: total_row : "+total_row);
		// Query message
		ArrayList<com.itpro.restws.model.Message> messages = messageService.findMsgExt(
				user.getSchool_id(), 
    			from_row, 
    			max_result, 
    			// Security filter
    			secure_filter,
    			// User filter    			
    			Utils.parseLong(filter_class_id),
    			filter_from_dt, 
    			filter_to_dt, 
    			Utils.parseLong(filter_from_user_id), 
    			Utils.parseLong(filter_to_user_id),
    			Utils.parseInteger(filter_channel), 
    			Utils.parseInteger(filter_is_read),
    			Utils.parseLong(filter_from_id)
    			);
		listResp.setList(messages);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
	    return listResp;

	}

	@RequestMapping(value="/api/messages/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public Message getMessage(@PathVariable int  id) 
	{
		logger.info(" *** MainRestController.getMessage/{id}:"+id);
		
		
		
		return messageService.findById(Integer.valueOf(id));
	 }
	
	
	
	
	@RequestMapping(value="/api/messages/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo createMessage(
			@RequestBody Message message,
			@Context final HttpServletRequest request
			) {
		logger.info(" *** MainRestController.createMessage");
		
		// Load user
		User user = getCurrentUser();
		// Check user permission
		messageService.secureCheckNewMessage(user, message);
		// Create message
		Message msg = messageService.insertMessageExt(message);
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(msg);
		return rsp;	
	}
	// Create message for School & Class
	@Secured({"ROLE_ADMIN" })
	@RequestMapping(value="/api/messages/create_ext",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo createMessage_ext(
			@RequestBody Message message,
			@RequestParam(value="filter_class_list",required =false) String filter_class_list,
			//@RequestParam(value="filter_roles",defaultValue="STUDENT",required =false) String filter_roles,
			@RequestParam(value="filter_roles",required =false) String filter_roles,
			@Context final HttpServletRequest request
			) {
		logger.info(" *** MainRestController.createClassMessage");
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath().toString(), "Successful");
		// Load user
		User user = getCurrentUser();
//		// Check user permission
//		messageService.secureCheckClassMessage(user, message, filter_class_list,filter_roles);
//		// Create message
//		//return messageService.insertClassMessageExt(message, filter_class_list);
//		messageService.insertClassMessageExt(message, filter_class_list, filter_roles);
//		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath().toString(), "Successful");
//		rsp.setMessageObject(null);
//		return rsp;
		
		ArrayList<Message> list = messageService.broadcastMessage(user, message, filter_roles);
		rsp.setMessageObject("Done, count= "+list.size());
		return rsp;
				
	}
	
	
	
	@RequestMapping(value="/api/messages/update/{id}",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo updateMessage(
			@PathVariable int  id,
			@RequestParam(value="is_read",required =false) String is_read,
			@RequestParam(value="imp_flg",required =false) String imp_flg,
			@Context final HttpServletRequest request
			) {
		logger.info(" *** MainRestController.updateMessage");
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		Message msg = messageService.findById(Integer.valueOf(id));
		if (msg != null ){
			if (is_read != null && Utils.parseInteger(is_read) != null ){
				msg.setIs_read( Utils.parseInteger(is_read));
			}
			if (imp_flg != null && Utils.parseInteger(imp_flg) != null ){
				msg.setImp_flg( Utils.parseInteger(imp_flg));
			}
		}
		messageService.updateMessage(msg);
		rsp.setMessageObject(msg);
		
		return rsp;
	}

//	@RequestMapping(value = "/api/messages/delete/{id}", method = RequestMethod.POST)
//	@ResponseStatus(value=HttpStatus.OK)	
//	 public String delMessage(
//			 @PathVariable int  id
//			 ) {
//		logger.info(" *** MainRestController.delMessage/{id}:"+id);
//
//	    return "Request was successfully, delete delMessage of id: "+id;
//	 }
//	
	
	

	
}