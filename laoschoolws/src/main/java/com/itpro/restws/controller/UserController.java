package com.itpro.restws.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.itpro.restws.dao.CommandDao;
import com.itpro.restws.helper.ChangePassInfo;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.E_STATE;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
import com.itpro.restws.model.User2Class;
import com.itpro.restws.service.CommandService;
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
public class UserController extends BaseController {
//	@Autowired
//    private Environment environment;
	@Autowired
	protected CommandDao commandDao;
	
	
	@Autowired
	protected EduProfileService eduProfileService;
	
	
	@Autowired
	protected CommandService commandService;
	
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/users",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public ListEnt getUsers(
			@RequestHeader(value="auth_key",required =true) String auth_key,
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			@RequestParam(value="filter_user_role",required =false) String filter_user_role,			
			@RequestParam(value="filter_sts", defaultValue="Active",required =false) String filter_sts,
			@RequestParam(value="filter_from_id", required =false) Integer filter_from_id,
			@RequestParam(value="from_row",required =false) Integer filter_from_row,
			@RequestParam(value="max_result",required =false) Integer filter_max_result,
			
			@Context final HttpServletResponse response,
			@Context final HttpServletRequest request
			) {
		logger.info(" *** MainRestController.getUsers");
		
		List<User> users = null;
		int total_row = 0;
		
		
		User me = getCurrentUser();
		Integer school_id = me.getSchool_id();
		Integer class_id =  Utils.parseInteger(filter_class_id);
		
		logger.info(" *** MainRestController.getUsers-filter_class_id: "+filter_class_id);
		logger.info(" *** MainRestController.getUsers-filter_user_role: "+filter_user_role);
		logger.info(" *** MainRestController.getUsers-filter_sts: "+filter_sts);

		ListEnt rspEnt = new ListEnt();
		
	    
    	if (me.hasRole(E_ROLE.ADMIN.getRole_short())){
    	}else{
    		if (class_id == null || class_id.intValue() == 0 ){
    			throw new ESchoolException("User is not Admin, require filter_class_id to get Users ",HttpStatus.BAD_REQUEST);
    		}
    		if (!userService.isBelongToClass(me.getId(), class_id)){
    			throw new ESchoolException("User ID="+me.getId()+" is not belong to the class id = "+class_id,HttpStatus.BAD_REQUEST);
    		}
    	}
    	
    	// Count user
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
		if (max_result <= 0){
			max_result = Constant.MAX_RESP_ROW;
		}
		
    	// Count user
    	total_row = userService.countUserExt(school_id, class_id, filter_user_role, Utils.parseInteger(filter_sts), filter_from_id);
    	if( (total_row <=  0) || (from_row > total_row) ||  max_result<=0) {
    		rspEnt.setList(null);
    		rspEnt.setFrom_row(0);
    		rspEnt.setTo_row(0);
    		rspEnt.setTotal_count(0);
    		return rspEnt;
    	}
    	if ((from_row + max_result > total_row)){
    		max_result = total_row-from_row;
    	}
    	logger.info("UserControl : total_row : "+total_row);
    	logger.info("UserControl : from_row : "+from_row);
    	logger.info("UserControl : max_result : "+max_result);
    	
    	
		// Query user
		users = userService.findUserExt(school_id, 
				from_row, 
				max_result, 
				class_id, 
				filter_user_role,  
				Utils.parseInteger(filter_sts), 
				filter_from_id);
		if (users != null && users.size() > 0){
			for (User usr: users){
				userService.updateClassTerm(usr);
			}
		}
		
		rspEnt.setList(users);
	    rspEnt.setFrom_row(from_row);
	    rspEnt.setTo_row(from_row + max_result);
	    rspEnt.setTotal_count(total_row);

		    
	   
	    return rspEnt;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	 public User getUser(@PathVariable int  id,@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getUser/{id}:"+id);
		User user = getCurrentUser();
		
		if (user.getId().intValue() == id){
			return user;
		}
		
		User load_user = userService.findById(Integer.valueOf(id));
		
		if (load_user == null){
			throw new ESchoolException("Cannot find user of id:"+id, HttpStatus.NOT_FOUND);
		}
		if (load_user.getSchool_id() != user.getSchool_id()){
			throw new ESchoolException("Teacher and user are not in the same School"+id, HttpStatus.BAD_REQUEST);
		}
		
	    if (user.hasRole(E_ROLE.ADMIN.getRole_short())){
	    	
	    }else{
	    	if (!userService.isSameClass(user, load_user)){
	    		throw new ESchoolException("Teacher and user are not in the same Class"+id, HttpStatus.BAD_REQUEST);
	    	}
	    }
		if (load_user != null){
			userService.updateClassTerm(load_user);
		}
	    return load_user;
	 }

	@RequestMapping(value="/api/users/myprofile",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public User myprofile(@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.myprofile ***");
		User user = getCurrentUser();
		if (user != null && (user.getPermisions() == null) ){
			permitService.loadPermit(user);
			
			userService.updateClassTerm(user);
			
		}
		
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
	
	
	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value="/api/users/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public User createUser(
			@RequestBody User user,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.create");
		
		User me = getCurrentUser();
		
		if (user.getSchool_id() == null || user.getSchool_id().intValue() == 0 ){
			throw new ESchoolException("user.school_id is required", HttpStatus.BAD_REQUEST);
		}
		if (me.getSchool_id().intValue() != user.getSchool_id().intValue()){
			throw new ESchoolException("cannot create user of other school_id", HttpStatus.BAD_REQUEST);
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
		if (user.getId() != null ){
			throw new ESchoolException("Canot create user - user.id != null",HttpStatus.BAD_REQUEST);
		}
		return userService.createUser(me,user, role);
		 
	}
	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value="/api/users/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public User updateUser(
			@RequestBody User user,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.update");

		if (user.getId() == null || user.getId().intValue() == 0 ){
			throw new ESchoolException("user.Id is required", HttpStatus.BAD_REQUEST);
		}
		
		User me = getCurrentUser();
		
		return userService.updateTransientUser(me,user,true);
		 
	}
	


	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value="/api/users/reset_pass/{sso}",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public String resetPass(
			@PathVariable String sso,
			@Context final HttpServletResponse response
			) {
		User me = getCurrentUser();
		if (sso == null || sso.trim().length() == 0){
			throw new ESchoolException("sso is required", HttpStatus.BAD_REQUEST);
		}
		if (me.getSso_id().equalsIgnoreCase(sso)){
			throw new ESchoolException("sso is same with current user (me) cannot reset password himselft !", HttpStatus.BAD_REQUEST);
		}
		logger.info(" *** MainRestController.users.reset_pass");
		String newpass= userService.resetPassword(me,sso,false);
 	    return  "Reset password success, new_pass:"+newpass;
	}
	
	//@Secured({ "ROLE_ADMIN"})
		@RequestMapping(value="/api/users/change_pass",method = RequestMethod.POST)
		@ResponseStatus(value=HttpStatus.OK)
		public RespInfo changePass(
				@RequestBody ChangePassInfo data,
				
				
				@Context final HttpServletResponse response,
				@Context final HttpServletRequest request
				) {
			
			logger.info(" *** MainRestController.users.change");
			User me = getCurrentUser();
			
			String username = data.getUsername();
			String old_pass = data.getOld_pass();
			String new_pass = data.getNew_pass();
			
			if (username == null || username.trim().length()==0 ){
				throw new ESchoolException("data.username is NULL or Blank", HttpStatus.BAD_REQUEST) ;
			}
			if (old_pass == null|| old_pass.trim().length()==0  ){
				throw new ESchoolException("data.old_pass is NULL  or Blank", HttpStatus.BAD_REQUEST) ;
			}
			if (new_pass == null|| new_pass.trim().length()==0  ){
				throw new ESchoolException("data.new_pass is NULL or Blank", HttpStatus.BAD_REQUEST) ;
			}
			
			if (!username.equals(getPrincipal())){
				throw new ESchoolException("username is not mapped with logined sso_id", HttpStatus.BAD_REQUEST);
			}
			String msg =  "Request was successfully, newpass:"+userService.changePassword(me,username, old_pass,new_pass);
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
	    	
			rsp.setMessageObject(msg);
			
		    return rsp;
		}
		
	
	//@Secured({ "ROLE_ADMIN"})
	 
//	@RequestMapping(value="/forgot_pass",method = RequestMethod.POST)
//	@ResponseStatus(value=HttpStatus.OK)
//	@Async
//	public RespInfo forgotPass(
//			@RequestParam(value="sso_id",required=true) String sso_id,
//			@RequestParam(value="phone",required=true) String phone,
//			@Context final HttpServletResponse response,
//			@Context final HttpServletRequest request
//			) {
//		
//		logger.info(" *** MainRestController.users.forgotPass");
//		User user = userService.findBySso(sso_id);
//		if (user == null || (user.getState() != E_STATE.ACTIVE.value())){
//			throw new ESchoolException("sso_id:("+sso_id+") is not exising",HttpStatus.BAD_REQUEST);
//		}
//		if (user.getPhone() == null ||  (!user.getPhone().equals(phone))){
//			throw new ESchoolException("phone:("+phone+") is not mapped with user's phone",HttpStatus.BAD_REQUEST);
//		}
//		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
//		userService.forgotPassword(sso_id, phone);
//		return rsp;
//	}
//	
	@RequestMapping(value="/forgot_pass",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	//@Async
	public RespInfo forgotPass(
			@RequestParam(value="sso_id",required=true) String sso_id,
			@RequestParam(value="phone",required=true) String phone,
			@Context final HttpServletResponse response,
			@Context final HttpServletRequest request
			) {
			
		logger.info(" *** MainRestController.users.forgotPass START");
		User user = userService.findBySso(sso_id);
		if (user == null || (user.getState() != E_STATE.ACTIVE.value())){
			throw new ESchoolException("sso_id:("+sso_id+") is not exising",HttpStatus.BAD_REQUEST);
		}
		if (user.getPhone() == null ||  (!user.getPhone().equals(phone))){
			throw new ESchoolException("phone:("+phone+") is not mapped with user's phone",HttpStatus.BAD_REQUEST);
		}
		//userService.forgotPassword(sso_id, phone);
//		Command cmd = new Command();
//		cmd.setCommand(Constant.CMD_FOROT_PASS);
//		cmd.setParams("sso_id="+sso_id+"&phone="+phone);
//		cmd.setCmd_dt(Utils.now());
//		cmd.setProcessed(0);
//		cmd.setMessage("Waiting");
		// commandDao.saveCommand(cmd);
		
		commandService.create_user_forgot_pass_cmd(user,sso_id, phone);
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Plz wait, your request is in processing");
		logger.info(" *** MainRestController.users.forgotPass END");
		return rsp;
	}
	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/api/users/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	 public String delUser(
			 @PathVariable Integer  id,
			@Context final HttpServletResponse response
			 
			 ) {
		User me = getCurrentUser();
		if (me.getId().intValue() == id.intValue()){
			throw new ESchoolException("Cannot del him selft", HttpStatus.BAD_REQUEST);
		}
		
		userService.deleteUser(me, id);
		
		logger.info(" *** MainRestController.delUser/{user_id}:"+id.intValue());
	    return "Request was successfully, deleted user of id:"+id.intValue();
	 }
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/api/users/assign_to_class", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	 public RespInfo assignToClass(
			@RequestParam(value="user_id",required=false) Integer user_id,
			@RequestParam(value="class_id",required=false) Integer class_id,
			@RequestParam(value="notice",required=false) String notice,
			
		    @Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			 
			 ) {
		
		User me = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		
		User2Class user2Class = userService.assignUser2Class(me, user_id, class_id, notice);
		rsp.setMessageObject(user2Class);
	    return rsp;
	 }
	
	

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/api/users/remove_frm_class", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	 public RespInfo removeFrmClass(
			@RequestParam(value="user_id",required=true) Integer user_id,
			@RequestParam(value="class_id",required=true) Integer class_id,
			@RequestParam(value="notice",required=false) String notice,
			
		    @Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			 
			 ) {
		User me  = getCurrentUser();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		
		userService.removeUser2Class(me, user_id, class_id, notice);
		rsp.setMessageObject("Done");
	    return rsp;
	 }
	
	
	@Secured({"ROLE_STUDENT"})
	@RequestMapping(value="/api/users/my_years",method = RequestMethod.GET)
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

	@Secured({ "ROLE_ADMIN"})
		@RequestMapping(value="/api/users/available",method = RequestMethod.GET)
		@ResponseStatus(value=HttpStatus.OK)
		public ListEnt getAvailableUsers(
				@RequestHeader(value="auth_key",required =true) String auth_key,
				
				@RequestParam(value="filter_user_role",required =false) String filter_user_role,
				
				@Context final HttpServletResponse response,
				@Context final HttpServletRequest request
				) {
			logger.info(" *** MainRestController.getAvailableUsers");
			
			List<User> users = null;
			int total_row = 0;
			int from_row = 0;
			int max_result = Constant.MAX_RESP_ROW;
			
			User user = getCurrentUser();
			Integer school_id = user.getSchool_id();
			
			ListEnt rspEnt = new ListEnt();
			
		    try {
		    	// Count user
		    	total_row = userService.countAvailableUser(school_id,filter_user_role);
		    	if (total_row > Constant.MAX_RESP_ROW){
		    		max_result = Constant.MAX_RESP_ROW;    	
		    	}else{
		    		max_result = total_row;
		    	}
		    		
				logger.info("user count: total_row : "+total_row);
				// Query user
				users = userService.findAvailableUser(school_id,from_row,max_result,filter_user_role); 
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
	
//	/**
//	 * Upload multiple file using Spring Controller
//	 */
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value="/api/users/upload_photo",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo uploadPhoto(
			@RequestParam(value = "user_id",required =false) Integer user_id,
			@RequestParam(value = "file",required =false) MultipartFile[] files,
			 @Context final HttpServletRequest request)
			 {
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		User me = getCurrentUser();
		userService.saveUploadPhoto(me, user_id, files);
		return rsp;
	}
	
	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value="/api/users/upload_file",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo uploadUsers(
			@RequestParam(value = "file",required =false) MultipartFile[] files,
			@RequestParam(value = "class_id",required =false) Integer class_id,
			 @Context final HttpServletRequest request)
			 {
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getServletPath(), "Successful");
		User me = getCurrentUser();
		if (files == null ){
			throw new ESchoolException("files is required", HttpStatus.BAD_REQUEST);
		}
		if (class_id == null ){
			throw new ESchoolException("class_id is required", HttpStatus.BAD_REQUEST);
		}
		
		String fileName = userService.saveUploadUsers(me, files,class_id);
		rsp.setDeveloperMessage("saved file name: "+fileName);
		return rsp;
	}
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/api/users/download_csv")
	@ResponseStatus(value=HttpStatus.OK)		
    public void downloadCSV(HttpServletResponse response) throws IOException {
 
        String csvFileName = "users.csv";
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",csvFileName);
        response.setHeader(headerKey, headerValue);
 
        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        User user = new User();
        user.setFullname("Full Name");
        user.setNickname("Short Name");
        user.setAddr1("Main address");
        user.setAddr2("Optional address");
        user.setPhone("02097015757");
        user.setBirthday("1978-05-01");
        user.setGender("male");
        user.setEmail("huynq@itpro.vn");
        user.setStd_parent_name("Name of STUDENT parent");
        
        
        String[] header = Constant.UserCVSheader;
        csvWriter.writeHeader(header);
        csvWriter.write(user, header);
        csvWriter.close();
    }

}
