package com.itpro.restws.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
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
	protected static final Logger logger = Logger.getLogger(NotifyController.class);
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
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
	
		
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
		logger.info(" *** getNotify/{id}:"+id);
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

			@RequestParam(value="filter_roles",required =false) String filter_roles,
			@RequestParam(value = "order",required =false) String[] orders,
			@RequestParam(value = "caption",defaultValue="NoCaption",required =false) String[] captions,
			@RequestParam(value = "file",required =false) MultipartFile[] files,
			@RequestParam(value = "json_in_string",required =false) String json_in_string,
			
			 @Context final HttpServletRequest request)
			 {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
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
	public RespInfo updateNotify(
			@PathVariable int  id,
			@RequestParam(value="is_read",required =false) String is_read,
			@RequestParam(value="imp_flg",required =false) String imp_flg,
			@Context final HttpServletRequest request
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
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
		
		notifyService.updateAttachedNotify(me,notify);
		rsp.setMessageObject(notify);
		
		return rsp;
	}

	@Secured({"ROLE_ADMIN","ROLE_TEACHER"})
	@RequestMapping(value="/api/notifies/create_php",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo createNotifyPHP(
			
			
			@RequestParam(value = "order1",required =false) String order1,
			@RequestParam(value = "order2",required =false) String order2,
			@RequestParam(value = "order3",required =false) String order3,
			@RequestParam(value = "order4",required =false) String order4,
			@RequestParam(value = "order5",required =false) String order5,
			
			@RequestParam(value = "caption1",required =false) String caption1,
			@RequestParam(value = "caption2",required =false) String caption2,
			@RequestParam(value = "caption3",required =false) String caption3,
			@RequestParam(value = "caption4",required =false) String caption4,
			@RequestParam(value = "caption5",required =false) String caption5,
			
			
			
			@RequestParam(value = "file1",required =false) MultipartFile file1,
			@RequestParam(value = "file2",required =false) MultipartFile file2,
			@RequestParam(value = "file3",required =false) MultipartFile file3,
			@RequestParam(value = "file4",required =false) MultipartFile file4,
			@RequestParam(value = "file5",required =false) MultipartFile file5,
			
			@RequestParam(value="filter_roles",required =false) String filter_roles,
			@RequestParam(value = "json_in_string",required =false) String json_in_string,
			 @Context final HttpServletRequest request)
			 {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		logger.info(" *** json_in_string:"+json_in_string);
		
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ESchoolException("Cannot convert notify data to UTF-8"+e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		String err_msg = "";
		
		String[] orders = new String[5];
		String[] captions = new String[5];
		MultipartFile[] files = new MultipartFile[5];
		
		// Get order
		if (order1 != null ){
			orders[0] = order1;
			
		}
		if (order2 != null ){
			orders[1] = order2;
		}
		if (order3 != null ){
			orders[2] = order3;
		}
		if (order4 != null ){
			orders[3] = order4;
		}
		if (order5 != null ){
			orders[4] = order5;
		}
		
		// Get caption
		// 
		if (caption1 != null ){
			captions[0] = caption1;
		}
		if (caption2 != null ){
			captions[1] = caption2;
		}
		if (caption3 != null ){
			captions[2] = caption3;
		}
		if (caption4 != null ){
			captions[3] = caption4;
		}
		if (caption5 != null ){
			captions[4] = caption5;
		}
			
		// Get File
		// 
		if (file1 != null ){
			files[0] = file1;
		}
		if (file2 != null ){
			files[1] = file2;
		}
		if (file3 != null ){
			files[2] = file3;
		}
		if (file4 != null ){
			files[3] = file4;
		}
		if (file5 != null ){
			files[4] = file5;
		}

		
		// Correct input data
		String[] new_orders = null;
		int cnt = 0;
		for (int i=0;i< orders.length;i++){
			if (orders[i] != null){
				cnt ++;
			}
		}
		if (cnt > 0){
			new_orders = new String[cnt];
			for (int i=0;i< cnt;i++){
				new_orders[i] = orders[i];
			}
		}
		
		// Correct input data
		String[] new_captions = null;
		cnt = 0;
		for (int i=0;i< captions.length;i++){
			if (captions[i] != null){
				cnt ++;
			}
		}
		if (cnt > 0){
			new_captions = new String[cnt];
			for (int i=0;i< cnt;i++){
				new_captions[i] = captions[i];
			}
		}
	
		
		// Correct input data
		MultipartFile[] new_files = null;
		cnt = 0;
		for (int i=0;i< files.length;i++){
			if (files[i] != null){
				cnt ++;
			}
		}
		if (cnt > 0){
			new_files = new MultipartFile[cnt];
			for (int i=0;i< cnt;i++){
				new_files[i] = files[i];
			}
		}
		
		
		if (new_orders == null ){
			err_msg = "order is NULL,";
//			throw new ESchoolException("Orders is NULL", HttpStatus.BAD_REQUEST);
		}

		if (new_captions == null ){
//			throw new ESchoolException("captions is NULL", HttpStatus.BAD_REQUEST);
			err_msg = err_msg+" captions is NULL,";
		}

		if (new_files == null || files.length <= 0){
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
		
	
		RespInfo rsp = createNotify(filter_roles, new_orders, new_captions, new_files, json_in_string, request);
		
		
		
		return rsp;
		 
	}
}