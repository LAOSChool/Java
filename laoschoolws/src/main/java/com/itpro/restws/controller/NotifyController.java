package com.itpro.restws.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.http.HttpStatus;
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
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Notify;
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
public class NotifyController extends BaseController {
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
	
	@RequestMapping(value="/api/notifies",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getNotifies(
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
		logger.info(" *** MainRestController.getNotifies");
		
	
		
		ListEnt listResp = new ListEnt();
		// Parsing params
		int total_row = 0;
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
		
		// Load user
		User user = getCurrentUser();
		
//		// Get data access right for entity
		MessageFilter secure_filter = notifyService.secureGetMessages(user);
		
		
    	// Count messages
//    	total_row = 100;//TODO:Test
//    	if (total_row <=  0){
//    		listResp.setList(null);
//    		listResp.setFrom_row(0);
//    		listResp.setTo_row(0);
//    		listResp.setTotal_count(0);
//    		return listResp;
//    	}
//    	
//    	if (total_row > Constant.MAX_RESP_ROW){
//    		max_result = Constant.MAX_RESP_ROW;
//    	}else{
//    		max_result = total_row;
//    	}
//    		
//		logger.info("Notify count: total_row : "+total_row);
//		// Query message
//		//ArrayList<com.itpro.restws.model.Notify> notifies = notifyService.findBySchool(user.getSchool_id(), from_row, max_result);
//		ArrayList<com.itpro.restws.model.Notify> notifies = notifyService.findTomUser(user.getId(), from_row, max_result);
//		for (Notify notify : notifies){
//			if (notify != null  && notify.getTask_id() != null && notify.getTask_id() > 0){
//				notify.setNotifyImages(notifyService.findImgByTaskID(notify.getTask_id()));
//			}
//		}
//		listResp.setList(notifies);
//		listResp.setFrom_row(from_row);
//		listResp.setTo_row(from_row + max_result);
//		listResp.setTotal_count(total_row);
	    
		
		
		
		// Count messages
    	total_row = notifyService.countNotifyExt(
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
    	logger.info("Notify count: total_row : "+total_row);
    	// Query message
		ArrayList<com.itpro.restws.model.Notify> notifies = notifyService.findNotifyExt(
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
	
		
		listResp.setList(notifies);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
    	return listResp;

	}
	
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		User user = getCurrentUser();
		
		//Notify notify =  notifyService.saveUploadData(user, files, captions, content, title, json_in_string);
		Notify notify =  notifyService.saveUploadData(user, files, captions,orders, json_in_string);
		// If upload success (task_id > 0)
		if (notify.getTask_id() >0 && notify.getNotifyImages().size() > 0 ){
			notifyService.broadcastNotify(user, notify, filter_roles);
			rsp.setMessageObject("Done");
		}else{
			rsp.setDeveloperMessage("cannot upload images");
			rsp.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			rsp.setMessage("Notify.getTask() ID = 0");
			rsp.setMessageObject("Upload failed");
		}
		
		return rsp;
		 
	}
	
//	
//	@RequestMapping(value="/api/notifies/update",method = RequestMethod.POST)
//	@ResponseStatus(value=HttpStatus.OK)	
//	public Notify updateNotify(
//			@RequestBody Notify notify
//			) {
//		logger.info(" *** MainRestController.updateNotify.update");
//		// return notify;
//		return notifyService.updateNotify(notify);
//		 
//	}

	@RequestMapping(value = "/api/notifies/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delNotify(
			 @PathVariable int  id
			 ) {
		logger.info(" *** MainRestController.delNotify/{id}:"+id);

	    return "Request was successfully, delNotify of id: "+id;
	 }
	
//	/**
//	 * Upload multiple file using Spring Controller
//	 */
//	@RequestMapping(value = "/api/notifies/uploadMultipleFile", method = RequestMethod.POST)
//	public String uploadMultipleFileHandler(
//			@RequestParam("title") String title,
//			@RequestParam("content") String content,
//			@RequestParam("caption") String[] captions,
//			@RequestParam("file") MultipartFile[] files) {
//		 String UPLOAD_LOCATION="/usr/local/src/apache-tomcat-8.0.0-RC1/webapps/eschool_content/";
//
//		if (files.length != captions.length)
//			return "Mandatory information missing";
//
//		String message = "";
//		for (int i = 0; i < files.length; i++) {
//			MultipartFile file = files[i];
//			String caption = captions[i];
//			String filename = file.getOriginalFilename();
//			try {
//				byte[] bytes = file.getBytes();
//
//				// Creating the directory to store file
//				String rootPath = UPLOAD_LOCATION;// System.getProperty("catalina.home");
//				File dir = new File(rootPath + File.separator + "tmpFiles");
//				if (!dir.exists())
//					dir.mkdirs();
//
//				// Create the file on server
//				File serverFile = new File(dir.getAbsolutePath() + File.separator + filename);
//				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//				stream.write(bytes);
//				stream.close();
//
//				logger.info("Server File Location="+ serverFile.getAbsolutePath());
//
//				message = message + "You successfully uploaded file=" + filename
//						+ "<br />";
//			} catch (Exception e) {
//				return "You failed to upload " + filename + " => " + e.getMessage();
//			}
//		}
//		return message;
//	}
	

	
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
		
		Notify notify = notifyService.findById(Integer.valueOf(id));
		if (notify != null ){
			if (is_read != null && Utils.parseInteger(is_read) != null ){
				notify.setIs_read( Utils.parseInteger(is_read));
			}
			if (imp_flg != null && Utils.parseInteger(imp_flg) != null ){
				notify.setImp_flg( Utils.parseInteger(imp_flg));
			}
		}
		notifyService.updateNotify(notify);
		rsp.setMessageObject(notify);
		
		return rsp;
	}
	 
}