package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.E_STATE;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.helper.Password;
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
public class UserController extends BaseController {
	
	@RequestMapping(value="/api/users",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public ListEnt getUsers(
			@RequestHeader(value="auth_key",required =true) String auth_key,
			
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			@RequestParam(value="filter_user_role",required =false) String filter_user_role,			
			@RequestParam(value="filter_sts", defaultValue="Active",required =false) String filter_sts,
			
			@Context final HttpServletResponse response,
			@Context final HttpServletRequest request
			) {
		logger.info(" *** MainRestController.getUsers");
		
		List<User> users = null;
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		
		logger.info(" *** MainRestController.getUsers-filter_class_id: "+filter_class_id);
		logger.info(" *** MainRestController.getUsers-filter_user_role: "+filter_user_role);
		logger.info(" *** MainRestController.getUsers-filter_sts: "+filter_sts);

		ListEnt rspEnt = new ListEnt();
	    try {
	    	// Count user
	    	total_row = userService.countBySchoolID(school_id);
	    	if (total_row > Constant.MAX_RESP_ROW){
	    		max_result = Constant.MAX_RESP_ROW;;	    	
	    	}else{
	    		max_result = total_row;
	    	}
	    		
			logger.info("user count: total_row : "+total_row);
			// Query user
			users = userService.findBySchool(school_id, from_row, max_result);
			
			
		    rspEnt.setList(users);
		    rspEnt.setFrom_row(from_row);
		    rspEnt.setTo_row(from_row + max_result);
		    rspEnt.setTotal_count(total_row);
		    
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController.getUsers() Message:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    
	    return rspEnt;
	}
	
	@RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	 public User getUser(@PathVariable int  id,@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getUser/{id}:"+id);
		User user = getCurrentUser();
		User load_user = null;
	    try {
	    	
		    load_user = userService.findById(Integer.valueOf(id));
		    if (load_user != null && load_user.getSchool_id() != user.getSchool_id() ){
		    	load_user = null;
		    	logger.info("user.id is not in same schoool with current user");
		    }
		    
			logger.info("Load User : "+load_user.toString());
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController.Exception Message:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }
	    finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    return load_user;
	 }

	@RequestMapping(value="/api/users/myprofile",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public User myprofile(@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.myprofile ***");
		User user = getCurrentUser();
	    
	    return user;
	}
	@Secured({"ROLE_SYS_ADMIN"})
	@RequestMapping(value="/api/sysadmin/users/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public User sysCreateAdmin(
			@RequestHeader(value="name",required=true) String name,
			@RequestHeader(value="pass",required=true) String pass,
			@RequestHeader(value="school_id",required=true) int school_id,
			@Context final HttpServletResponse response
			) {
		
		logger.info(" *** MainRestController.users.sysAdminCreateUser");
		
		User user = userService.createAdmin(name, pass, Integer.valueOf(school_id));
		return user;
	}
	
	
	
	@Secured({ "ROLE_ADMIN","ROLE_SYS_ADMIN"})
	@RequestMapping(value="/api/users/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public User createUser(
			@RequestBody User user,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.create");
		String random_pass = Password.getRandomPass();
		try {
			user.setPassword(Password.getSaltedHash(random_pass));
			user.setDefault_pass(random_pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String type = user.getRoles().split(",")[0];
		 //return userService.insertUser(user);
		E_ROLE role = E_ROLE.STUDENT;
		if (type.equalsIgnoreCase("admin")){
			if (hasRole(new String[]{E_ROLE.SYS_ADMIN.getRole()})){
				role = E_ROLE.ADMIN;
			}else{
				throw new RuntimeException("Only Sys Admin can creat Admin for School");				
			}
		}else if (type.equalsIgnoreCase("teacher")){
			role = E_ROLE.TEACHER;
		}else if (type.equalsIgnoreCase("cls_president")){
			role = E_ROLE.CLS_PRESIDENT;
		}else if (type.equalsIgnoreCase("student")){
			role = E_ROLE.STUDENT;
			user.setSso_id("STD");//Default name for student
		}else{
			throw new RuntimeException("Invalid user type="+type);
		}
		return userService.createUser(user, role);
		 
	}
	
	@RequestMapping(value="/api/users/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public User updateUser(
			@RequestBody User user,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.update");
		
		 if (userService.isValidState(user.getState())){
			 return userService.updateUser(user);
		 }else{
			 throw new RuntimeException("Invalid State="+user.getState());
		 }
	}
	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value="/api/users/reset_pass/{sso}",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public String resetPass(
			@PathVariable String sso,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.reset_pass");
		String newpass= userService.resetPassword(sso);
		 User user= userService.findBySso(sso);
		 Message msg = new Message();
		 msg.setFrom_usr_id(Integer.valueOf(1));
		 msg.setTo_usr_id(user.getId());
		 msg.setTitle("Thong bao reset password");
		 msg.setContent("Reset pass success to : "+newpass);
		 messageService.insertMessageExt(msg);
		 return "Reset password success, new pass:"+newpass;
	}
	
	//@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value="/api/users/change_pass",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public String changePass(
			@RequestParam(value="username",required=true) String username,
			@RequestParam(value="old_pass",required=true) String old_pass,
			@RequestParam(value="new_pass",required=true) String new_pass,
			@Context final HttpServletResponse response
			) {
		
		logger.info(" *** MainRestController.users.reset_pass");
		if (!username.equals(getPrincipal())){
			throw new RuntimeException("username is not mapped with logined sso_id");
		}
		return "newpass:"+userService.changePassword(username, old_pass,new_pass);
	}
	
	
	
	//@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value="/forgot_pass",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public RespInfo forgotPass(
			@RequestParam(value="sso_id",required=true) String sso_id,
			@RequestParam(value="phone",required=true) String phone,
			@Context final HttpServletResponse response,
			@Context final HttpServletRequest request
			) {
		
		logger.info(" *** MainRestController.users.forgotPass");
		User user = userService.findBySso(sso_id);
		if (user == null || (user.getState() != E_STATE.ACTIVE.value())){
			throw new ESchoolException("sso_id:("+sso_id+") is not exising",HttpStatus.BAD_REQUEST);
		}
		if (user.getPhone() == null ||  (!user.getPhone().equals(phone))){
			throw new ESchoolException("phone:("+phone+") is not mapped with user's phone",HttpStatus.BAD_REQUEST);
		}
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		userService.forgotPassword(sso_id, phone);
		return rsp;
	}
	
	
	
	@RequestMapping(value = "/api/users/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	 public String delUser(
			 @PathVariable int  id,
			@Context final HttpServletResponse response
			 
			 ) {
		logger.info(" *** MainRestController.delUser/{user_id}:"+id);
	    return "Request was successfully, deleted user of id:"+id;
	 }
	
	
	
	

	
}
