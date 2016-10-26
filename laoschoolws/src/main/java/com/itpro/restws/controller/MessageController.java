
package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
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

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_DEST_TYPE;
import com.itpro.restws.helper.E_MSG_CHANNEL;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Message;
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
public class MessageController extends BaseController {
	
	@Autowired
	private ActionLogVIPService actionLogVIPService;
	
	protected static final Logger logger = Logger.getLogger(MessageController.class);
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
			@RequestParam(value="filter_from_id",required =false) String filter_from_id,
			@RequestParam(value="filter_from_time",required =false) Long filter_from_time,
			@RequestParam(value="filter_to_time",required =false) Long filter_to_time
			
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		ListEnt listResp = new ListEnt();
		// Parsing params
		int total_row = 0;
		
		
		// Load user
		User user = getCurrentUser();
		
		// Get data access right for entity
		MessageFilter secure_filter = messageService.secureGetMessages(user);
		
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
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
		if (max_result <= 0){
			max_result = Constant.MAX_RESP_ROW;
		}			
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
    	
    	if( (total_row <=  0) || (from_row > total_row) ||  max_result<=0) {
    		listResp.setList(null);
    		listResp.setFrom_row(0);
    		listResp.setTo_row(0);
    		listResp.setTotal_count(0);
    		return listResp;
    	}
    	
    	if ((from_row + max_result > total_row)){
    		max_result = total_row-from_row;
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
		logger.info(" *** getMessage/{id}:"+id);
		
		
		
		Message msg =  messageService.findById(Integer.valueOf(id));
		return msg;
	 }
	
	
	/***
	 * This method to send message fore User
	 * Support many users: in cc_list
	 * @param message
	 * @param request
	 * @return
	 */
	// @Secured({"ROLE_ADMIN","ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	// Enable all to support STUDENT send message to head teacher
	@RequestMapping(value="/api/messages/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo createMessage(
			@RequestBody Message message,
			@Context final HttpServletRequest request
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		// Load user
		User me = getCurrentUser();
		
		if (message.getDest_type()== null){
			message.setDest_type(E_DEST_TYPE.PERSON.getValue());
		}
		if (message.getDest_type() != E_DEST_TYPE.PERSON.getValue()){
			throw new ESchoolException("message.dest_type must be 0 ONLY to send to a User, keep other users in cc_list", HttpStatus.BAD_REQUEST);
		}

		// Check user permission
		messageService.secureCheckNewMessage(me, message);
		message.setChannel(E_MSG_CHANNEL.FIREBASE.getValue());// disable send SMS
		Message msg = messageService.sendUserMessageWithCC(me, message);
		// 20160823 end		
		// Log action
		actionLogVIPService.logAction_url(me, request.getServletPath(), msg);
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(msg);
		return rsp;	
	}
	/***
	 *  This method used to send CLASS message
	 *  Support many classes in cc_list
	 * 	Only ADMIN role can execute
	 * @param message
	 * @param filter_roles
	 * @param request
	 * @return
	 */
	@Secured({"ROLE_ADMIN" })
	@RequestMapping(value="/api/messages/create_ext",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo createMessage_ext(
			@RequestBody Message message,
			@RequestParam(value="filter_roles",required =false) String filter_roles,
			@Context final HttpServletRequest request
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
//		String actlog = message.printActLog();
//		logger.info(actlog);
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath().toString(), "Successful");
		// Load user
		User me = getCurrentUser();
		
		// 2016-08-22 DEL
		// ArrayList<Message> list = messageService.broadcastMessage(me, message, filter_roles);
		// rsp.setMessageObject("Done, count= "+list.size());
		// 2016-08-22 Start
		if (message.getDest_type()== null){
			//message.setDest_type(E_DEST_TYPE.CLASS.getValue());
			throw new ESchoolException("message.dest_type cannot be NULL, must set to 1 ONLY (sending to CLASS), other classes can be put in cc_list", HttpStatus.BAD_REQUEST);
		}
		ArrayList<Message> list = null;
		if (message.getDest_type() == E_DEST_TYPE.CLASS.getValue()){
			message.setChannel(E_MSG_CHANNEL.FIREBASE.getValue());// disable send SMS
			list = messageService.createClassMessageTaskWithCC(me,message,filter_roles);
		}else {
			throw new ESchoolException("message.dest_type must be 1 ONLY to send to a CLASS, keep other class_id in cc_list", HttpStatus.BAD_REQUEST);
		}
		String ids = "";
		if (list != null && list.size() > 0){
			for (Message msg: list){
				ids += msg.getId().intValue()+",";
			}
			ids = Utils.removeTxtLastComma(ids);
		}
		// Log action
		actionLogVIPService.logAction_url(me, request.getServletPath(), message);
				
		rsp.setMessageObject("Done, tasks created for background processing, ids:"+ids);
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
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		User me = getCurrentUser();
		
		Message msg = messageService.findById(Integer.valueOf(id));
		if (msg == null ){
			throw new ESchoolException("message_id is not existing:"+id, HttpStatus.BAD_REQUEST);
		}
		
		if (is_read != null && Utils.parseInteger(is_read) != null ){
			msg.setIs_read( Utils.parseInteger(is_read));
		}
		if (imp_flg != null && Utils.parseInteger(imp_flg) != null ){
			msg.setImp_flg( Utils.parseInteger(imp_flg));
		}
		
		if (me.hasRole(E_ROLE.STUDENT.getRole_short())){
			if (msg.getTo_user_id() != null && msg.getTo_user_id().intValue() == me.getId()){
				//OK do nothing
			}else{
				throw new ESchoolException("current user is STUDENT, cannot update message sent to other User, to_user_id:"+msg.getTo_user_id()==null?"":(msg.getTo_user_id().intValue()+""), HttpStatus.BAD_REQUEST);
			}
		}
		
		
		messageService.updateAttachedMessage(me,msg);
		rsp.setMessageObject(msg);
		
		return rsp;
	}

	@Secured({"ROLE_ADMIN","ROLE_SYS_ADMIN" })
	@RequestMapping(value="/api/messages/open_sms",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getUnProcSMS(
			@Context final HttpServletRequest request
			) 
	{
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		String api_key = request.getHeader(Constant.HEADER_API_KEY);
		User me = getCurrentUser();
		
		ArrayList<Message> list =  messageService.findUnProcSMS(me, api_key);
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		rsp.setMessageObject(list);
		return rsp;
	 }	
	
	@Secured({"ROLE_ADMIN","ROLE_SYS_ADMIN" })
	@RequestMapping(value="/api/messages/sms_done/{id}",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo sms_done
	(
			@PathVariable int  id,
			@Context final HttpServletRequest request
			) 
	{
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		String api_key = request.getHeader(Constant.HEADER_API_KEY);
		User me = getCurrentUser();
		
		messageService.smsDone(me,api_key, id);
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		rsp.setMessageObject("DONE");
		return rsp;
	 }	
}
