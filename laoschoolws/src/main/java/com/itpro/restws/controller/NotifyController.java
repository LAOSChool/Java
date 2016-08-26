package com.itpro.restws.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Notify;
import com.itpro.restws.model.User;
import com.itpro.restws.service.CommandService;
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
public class NotifyController extends BaseController {
	@Autowired
	protected CommandService commandService;
	
	/***
	 * It will convert String to String[] without using separator (null param), 
	 * with Spring class org.springframework.beans.propertyeditors.StringArrayPropertyEditor. 
	 * If someone in same project will use new default conversion way, it will be ok.
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(null));
	}	
	
	@Secured({"ROLE_ADMIN","ROLE_TEACHER","ROLE_STUDENT"})
	@RequestMapping(value="/api/notifies",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getNotifies(
			@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			@RequestParam(value="filter_from_user_id",required =false) Integer filter_from_user_id,			
			@RequestParam(value="filter_to_user_id",required =false) Integer filter_to_user_id,
			@RequestParam(value="filter_channel",required =false) Integer filter_channel,
			@RequestParam(value="filter_is_read", required=false) Integer filter_is_read,
			@RequestParam(value="filter_from_dt",required =false) String filter_from_dt,			
			@RequestParam(value="filter_to_dt",required =false) String filter_to_dt	,
			@RequestParam(value="from_row",required =false) Integer filter_from_row,
			@RequestParam(value="max_result",required =false) Integer filter_max_result,
			@RequestParam(value="filter_from_id",required =false) Integer filter_from_id,
			@RequestParam(value="filter_from_time",required =false) Long filter_from_time,			
			@RequestParam(value="filter_to_time",required =false) Long filter_to_time
			) {
		logger.info(" *** MainRestController.getNotifies");
		
	
		
		ListEnt listResp = new ListEnt();
		// Parsing params
		int total_row = 0;
		
		// Load user
		User user = getCurrentUser();
		
//		// Get data access right for entity
		MessageFilter secure_filter = notifyService.secureGetMessages(user);
		if (filter_from_time != null && filter_from_time.longValue() > 0){
			if (filter_from_dt != null ){
				throw new ESchoolException("Cannot not input both filter_from_dt AND filter_from_time", HttpStatus.BAD_REQUEST);
			}
			filter_from_dt = Utils.numberToDateTime(filter_from_time);
		}
		if (filter_to_time != null && filter_to_time.longValue() > 0){
			if (filter_to_dt != null ){
				throw new ESchoolException("Cannot not input both filter_to_dt AND filter_to_time", HttpStatus.BAD_REQUEST);
			}
			filter_to_dt = Utils.numberToDateTime(filter_to_time);
		}
		
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
		if (max_result <= 0){
			max_result = Constant.MAX_RESP_ROW;
		}		
		// Count messages
    	total_row = notifyService.countNotifyExt(
    			user.getSchool_id(), 
    			//from_row, 
    			//max_result, 
    			// Security filter
    			secure_filter,
    			// User filter    			
    			filter_class_id,
    			filter_from_dt, 
    			filter_to_dt, 
    			filter_from_user_id, 
    			filter_to_user_id,
    			filter_channel, 
    			filter_is_read,
    			filter_from_id
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
    	 
    	logger.info("Notify count: total_row : "+total_row);
    	// Query message
		ArrayList<com.itpro.restws.model.Notify> notifies = notifyService.findNotifyExt(
				user.getSchool_id(), 
    			from_row, 
    			max_result, 
    			// Security filter
    			secure_filter,
    			// User filter    			
    			filter_class_id,
    			filter_from_dt, 
    			filter_to_dt, 
    			filter_from_user_id, 
    			filter_to_user_id,
    			filter_channel, 
    			filter_is_read,
    			filter_from_id
    			);
	
		
		listResp.setList(notifies);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
    	return listResp;

	}
	
	@Secured({"ROLE_ADMIN","ROLE_TEACHER","ROLE_STUDENT"})
	@RequestMapping(value="/api/notifies/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public Notify getNotify(@PathVariable int  id) 
	{
		logger.info(" *** MainRestController.getNotify/{id}:"+id);
		Notify notify= notifyService.findById(Integer.valueOf(id));
		if (notify != null  && notify.getTask_id() != null && notify.getTask_id() > 0){
			notify.setNotifyImages(notifyService.findImgByTaskID(notify.getTask_id()));
		}
		return notify;
	 }
	
	@Secured({"ROLE_ADMIN","ROLE_TEACHER"})
	@RequestMapping(value="/api/notifies/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo createNotify(
//			@RequestParam("title") String title,
//			@RequestParam("content") String content,
			@RequestParam(value="filter_roles",required =false) String filter_roles,
			@RequestParam(value = "order",required =false) String[] orders,
			@RequestParam(value = "caption",defaultValue="NoCaption",required =false) String[] captions,
			@RequestParam(value = "file",required =false) MultipartFile[] files,
			@RequestParam(value = "json_in_string",required =false) String json_in_string,
			 @Context final HttpServletRequest request)
			 {
		logger.info(" *** MainRestController.createNotify");
		logger.info(" *** json_in_string:"+json_in_string);
		
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ESchoolException("Cannot convert notify data to UTF-8"+e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		String err_msg = "";
		if (orders == null ){
			err_msg = "order is NULL,";
//			throw new ESchoolException("Orders is NULL", HttpStatus.BAD_REQUEST);
		}

		if (captions == null ){
//			throw new ESchoolException("captions is NULL", HttpStatus.BAD_REQUEST);
			err_msg = err_msg+" captions is NULL,";
		}

		if (files == null || files.length <= 0){
//			throw new ESchoolException("file is NULL", HttpStatus.BAD_REQUEST);
			err_msg = err_msg+" file is empty,";
		}
		if (json_in_string == null ){
//			throw new ESchoolException("json_in_string is NULL", HttpStatus.BAD_REQUEST);
			err_msg = err_msg+" json_in_string is NULL,";
		}
		
		if (err_msg.length() > 0){
			throw new ESchoolException(err_msg, HttpStatus.BAD_REQUEST);
		}
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		
		User me = getCurrentUser();
		
		//Notify notify =  notifyService.saveUploadData(user, files, captions, content, title, json_in_string);
		Notify notify =  notifyService.saveUploadData(me, files, captions,orders, json_in_string);
		// If upload success (task_id > 0)
		if (notify.getTask_id() >0 && notify.getNotifyImages().size() > 0 ){
			// notifyService.broadcastNotify(me, notify, filter_roles); //20160823
			commandService.create_notify_cmd(me, notify, filter_roles);
			
			rsp.setMessageObject("Done, tasks created for background processing, task_id:"+notify.getTask_id().intValue());
		}else{
			rsp.setDeveloperMessage("cannot upload images");
			rsp.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			rsp.setMessage("Notify.getTask() ID = 0");
			rsp.setMessageObject("Upload failed");
		}
		
		return rsp;
		 
	}
	
	@Secured({"ROLE_ADMIN","ROLE_TEACHER","ROLE_STUDENT"})
	@RequestMapping(value="/api/notifies/update/{id}",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo updateMessage(
			@PathVariable int  id,
			@RequestParam(value="is_read",required =false) String is_read,
			@RequestParam(value="imp_flg",required =false) String imp_flg,
			@Context final HttpServletRequest request
			) {
		logger.info(" *** MainRestController.updateMessage");
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		User me =getCurrentUser();
		Notify notify = notifyService.findById(Integer.valueOf(id));
		
		if (notify == null ){
			throw new ESchoolException("id is not existing:"+id, HttpStatus.BAD_REQUEST);
		}
		if (me.getSchool_id().intValue() != notify.getSchool_id().intValue()){
			throw new ESchoolException("Current User- and notify not in same school", HttpStatus.BAD_REQUEST);
		}
		
		if (me.hasRole(E_ROLE.ADMIN.getRole_short())){
			// ok
		}
		else if (me.hasRole(E_ROLE.TEACHER.getRole_short())){
			if (notify.getTo_user_id() != null && notify.getTo_user_id().intValue() == me.getId().intValue()){
				//OK
			}
			else if (notify.getFrom_user_id() != null && notify.getFrom_user_id().intValue() == me.getId().intValue()){
				//OK
			}else{
				throw new ESchoolException("Users (TEACHER) cannot update notify not belong to himself", HttpStatus.BAD_REQUEST);
			}
		}else{
			if (notify.getTo_user_id() != null && notify.getTo_user_id().intValue() != me.getId().intValue()){
				throw new ESchoolException("Users (STUDENT) cannot update notify sent to other user", HttpStatus.BAD_REQUEST);
			}
		}
			
		if (is_read != null && Utils.parseInteger(is_read) != null ){
			notify.setIs_read( Utils.parseInteger(is_read));
		}
		if (imp_flg != null && Utils.parseInteger(imp_flg) != null ){
			notify.setImp_flg( Utils.parseInteger(imp_flg));
		}
		
		notifyService.updateNotify(notify);
		rsp.setMessageObject(notify);
		
		return rsp;
	}
	 
}