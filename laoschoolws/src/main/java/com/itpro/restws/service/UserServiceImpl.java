package com.itpro.restws.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.dao.AuthenKeyDao;
import com.itpro.restws.dao.UserDao;
import com.itpro.restws.helper.CSVUtils;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_MSG_CHANNEL;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.E_STATE;
import com.itpro.restws.helper.Password;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.AuthenKey;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
import com.itpro.restws.model.User2Class;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private PermitService permitService;
	@Autowired
    private Environment environment;
	@Autowired
	private MessageService messageService;
	
	@Autowired
	protected EduProfileService eduProfileService;
	@Autowired
	protected ClassService classService;
	
	@Autowired
	private AuthenKeyDao authenKeyDao;
	@Autowired
	protected ApiKeyService apiKeyService;
	@Autowired
	protected User2ClassService user2ClassService;
	
	public User findById(Integer id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		User user = userDao.findById(id);
		if (user != null){
			permitService.loadPermit(user);
		}
		return user;
	}

	public User findBySso(String sso) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		User user = userDao.findBySSO(sso);
		if (user != null){
			permitService.loadPermit(user);
		}
		return user;
	}

	@Override
	public
	ArrayList<User> findBySchool(Integer school_id,int from_num, int max_result){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("school_id:"+school_id.intValue());
		logger.info("from_num:"+from_num);
		logger.info("max_result:"+max_result);
		
		ArrayList<User> list = (ArrayList<User>)userDao.findBySchool(school_id, from_num, max_result);
		for (User user : list){
			if (user != null){
				permitService.loadPermit(user);
			}
		}
		return list;
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		return userDao.countUserBySchool(school_id);
	}

	@Override
	public int countByClassID(Integer class_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		return userDao.countUserByClass(class_id);
	}

	
	@Override
	public ArrayList<User> findByClass(Integer class_id, int from_num, int max_result) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("class_id:"+class_id.intValue());
		logger.info("from_num:"+from_num);
		logger.info("max_result:"+max_result);
		
		ArrayList<User> list = (ArrayList<User>) userDao.findByClass(class_id, from_num, max_result);
		for (User user : list){
			if (user != null){
				permitService.loadPermit(user);
			}
		}
		return list;
	}

//	@Override
//	public User insertUser(User me, User user) {
//		
//		valid_user(user, true);
//		userDao.saveUser(user);
//		return user;
//	}

	@Override
	public User updateTransientUser(User me, User transient_user,boolean ignore_pass) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		if (transient_user.getId() == null ){
			throw new ESchoolException("user.id is null", HttpStatus.BAD_REQUEST);
		}
		User userDB = userDao.findById(transient_user.getId());
		if (userDB == null ){
			throw new ESchoolException("user.id is not exising: "+transient_user.getId().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		if (userDB.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("term_db.SchooId is not same with me.school_id", HttpStatus.BAD_REQUEST);
		}
		
		
		if (userDB.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			throw new ESchoolException("Cannot update SYS_ADMIN info", HttpStatus.BAD_REQUEST);
		}
		// For sure, disable update school, sso_id, roles
		transient_user.setSchool_id(userDB.getSchool_id()); // Cannot change school_id
		transient_user.setSso_id(userDB.getSso_id());		// Cannot change SSO_ID
		transient_user.setRoles(userDB.getRoles());		// Cannot change role
		// Cannot change state of Admin
		
		if (userDB.hasRole(E_ROLE.ADMIN.getRole_short())){
			if (transient_user.getState() != null ){
				throw new ESchoolException("Only SYS_ADMIN can change state of Admin", HttpStatus.BAD_REQUEST);
			}
		}
	
		
		if (ignore_pass ){
			transient_user.setPassword(null);
		}

		  try {
			  userDao.setFlushMode(FlushMode.MANUAL);
			  userDB = User.updateChanges(userDB, transient_user);
			  valid_user(userDB, false);
	        } catch (Exception e){
	        	userDao.clearChange();
	        	throw e;
	        }
		   finally {
			   userDao.setFlushMode(FlushMode.AUTO);
	        }
		  
		
		  userDao.updateUser(me,userDB);
		  // Logout user if not active
		  if (userDB.getState().intValue() != E_STATE.ACTIVE.value()){
			  List<AuthenKey> list  =  authenKeyDao.findBySsoID(userDB.getSso_id());
				if (list != null && list.size() > 0){
					for (AuthenKey authKey: list){
						authenKeyDao.deleteToken(authKey);
						apiKeyService.logoutByAuthKey(authKey.getAuth_key());	
					}
				}
		  }
		return userDB;
		
		
	}

	@Override
	public boolean isValidState(int State) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		if (State == E_STATE.PENDING.value()){
			return true;
		}
		if (State == E_STATE.ACTIVE.value()){
			return true;
		}
		if (State == E_STATE.SUSPENSE.value()){
			return true;
		}
		if (State == E_STATE.CLOSED.value()){
			return true;
		}
		
		return false;
	}
	@Override
	public void logout(User me, User user){
		
	}

	@Override
	public boolean isValidPassword(String pass) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		if (pass != null && !pass.equals("") && (!pass.contains(" "))){
			if (pass.length() >=4 &&  pass.length()<= 20){
				return true;
			}
		}
		return false;
	}
	@Override
	public void validSSO_ID(String sso,E_ROLE role) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		 
		if (sso == null || sso.trim().length() == 0){
			throw new ESchoolException("SSO is blank or NULL",HttpStatus.BAD_REQUEST);
		}
		
		
		if (role.getRole_short().equalsIgnoreCase("TEACHER")){
			if (!sso.toUpperCase().startsWith("TEA")){
				throw new ESchoolException("SSO of teacher must start by \"tea\" ",HttpStatus.BAD_REQUEST);
			}
		}else{
			if (sso.toUpperCase().startsWith("TEA")){
				throw new ESchoolException("\"TEA\" is prefix of TEACHER user only",HttpStatus.BAD_REQUEST);
			}	
		}
		
		
		
		
		if (role.getRole_short().equalsIgnoreCase("ADMIN")){
			if (!sso.toUpperCase().startsWith("ADM")){
				throw new ESchoolException("SSO of admin must start by \"adm\" ",HttpStatus.BAD_REQUEST);
			}
		}else{
			if (sso.toUpperCase().startsWith("ADM")){
				throw new ESchoolException("\"ADM\" is prefix of ADMIN user only",HttpStatus.BAD_REQUEST);
			}	
		}

		
		
		User tmp = userDao.findBySSO(sso);
		if (tmp != null ){
			throw new ESchoolException("User already existing sso_id:"+sso,HttpStatus.BAD_REQUEST);
		}
		
		if ( (sso.length() >= 4 ) &&
				(sso.length() <= 20 ) && 
				!Character.isDigit(sso.charAt(0)  
						)
				){
		}else{
			throw new ESchoolException("Username is not in correct format ( Correct format: 4<= name.length<=20 and not start by a Number)",HttpStatus.BAD_REQUEST);
		}
		String USERNAME_PATTERN = "^[a-z0-9_-]{4,20}$";
		 Pattern  pattern = Pattern.compile(USERNAME_PATTERN);
		  Matcher matcher = pattern.matcher(sso);
		  if (!matcher.matches()){
			  throw new ESchoolException("Invaid user name, only accept characters in ( 0-9 or a-z, A-Z -, _), max length = 20, min length = 4 ",HttpStatus.BAD_REQUEST);  
		  }
		

		
		
	}

	@Override
	public String encryptPass(String new_pass) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
				
		String saltedPass =new_pass;
		try {
			saltedPass = Password.getSaltedHash(new_pass);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return saltedPass;
		
	}

	@Override
	public String changePassword(User me, String sso_id, String old_pass, String new_pass) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		if (!isValidPassword(new_pass) ){
			throw new ESchoolException("Input Password length is not correct - expected length should be >= 4 AND <= 20",HttpStatus.BAD_REQUEST);
		}
		User user_db = userDao.findBySSO(sso_id);
		if (user_db != null ){
			if (user_db.getSchool_id().intValue() != me.getSchool_id().intValue()){
				if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
				// do nothing	
				}else{
					throw new ESchoolException("me.school_id != user_db.school_id",HttpStatus.BAD_REQUEST);
				}
			}
			
			try {
				if (!Password.check(old_pass, user_db.getPassword())){
					throw new ESchoolException("Input current password is not correct",HttpStatus.BAD_REQUEST);
				}
			}catch (ESchoolException es){
				throw es;
			}
			catch (Exception e) {
				throw new RuntimeException("Unknow error Password Check");
			}
			user_db.setPassword(encryptPass(new_pass));
			userDao.updateUser(me, user_db);
		}else{
			throw new ESchoolException("User is NULL, sso="+sso_id,HttpStatus.BAD_REQUEST);
		}
		return new_pass;
	}

	@Override
	public String resetPassword(User me, String sso_id, boolean is_forgot_request) {
		logger.info("resetPassword START, sso_id="+sso_id);
		
		int randomNum = 1111 + (int)(Math.random() * 9999);
		String newPass = randomNum+ "";
		User user_db = userDao.findBySSO(sso_id);
		if (user_db == null ){
			throw new ESchoolException("sso_id is not found",HttpStatus.BAD_REQUEST);
		}
		// Check school
		if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			// not check
		}else{
			if (user_db.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("sso_id:"+sso_id+ " is not in same school with me.school_id",HttpStatus.BAD_REQUEST);
			}
		}
		
		user_db.setPassword(encryptPass(newPass));
		userDao.updateUser(me, user_db);
		// Send message
		 Message msg = new Message();

		 msg.setFrom_user_id(Integer.valueOf(1));
		 msg.setFrom_user_name("SYS_ADMIN");
		 msg.setTo_user_id(user_db.getId());
		 String content = "Your password has been reset by Admin, new pass: "+newPass;
		 if (is_forgot_request){
			 content = "Your password has been reset due to forgot-pass request, new pass: "+newPass;	 
		 }
		 
		 msg.setContent(content);
		 
		 msg.setChannel(E_MSG_CHANNEL.SMS.getValue());
		 
		 messageService.sendUserMessageWithCC(me, msg);
		logger.info("resetPassword END, sso_id="+sso_id);
		return newPass;
	}

	@Override
	public User createUser(User me, User user, E_ROLE role) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		// Update Student sso_id to unique value
		if (role == E_ROLE.STUDENT){
			user.setSso_id(user.getSso_id()+Utils.now());
		}
		else{
			validSSO_ID(user.getSso_id(),role);
		}
		valid_user(user,true);
		// Insert into DB
		userDao.saveUser(me,user);
		// Change sso_id to DB ID
		if (role == E_ROLE.STUDENT){
			if (user.getId().intValue() <= 9999999 ){
				user.setSso_id(String.format("%08d", user.getId()));
			//}
//			else if (user.getId().intValue() <= 999999999 ){
//				user.setSso_id(String.format("%010d", user.getId()));
			}else{
				user.setSso_id(""+user.getId());
			}
			//updateUser(user);
			userDao.saveUser(me,user);
		}
		
		return user;
	}

	@Override
	public String forgotPassword(User me, String sso_id, String phone) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("sso_id:"+sso_id);
		logger.info("phone:"+phone);
		
		User forgot_user = userDao.findBySSO(sso_id);
		
		if (forgot_user == null ){
			throw new ESchoolException("FAILED, cannot find user for sso_id"+sso_id,HttpStatus.BAD_REQUEST);
		}
		if (forgot_user.getPhone() == null ){
			throw new ESchoolException("FAILED, user's phone is not registered,sso_id:"+sso_id,HttpStatus.BAD_REQUEST);
		}
		if (forgot_user.getPhone() != null && 
				phone != null && 
				phone.equalsIgnoreCase(forgot_user.getPhone())){
			String newpass = resetPassword(me,sso_id,true);
			 logger.info("forgotPass SUCCESS,sso_id="+sso_id+"///phone="+phone+"///new pass="+newpass);
			 return "forgot_pass SUCCESS,sso_id="+sso_id+"///phone="+phone+"///new pass="+newpass;
		}
		throw new ESchoolException("FAILED, input phone is not mapped with registerd-phone,sso_id:"+sso_id,HttpStatus.BAD_REQUEST);
	}

	@Override
	public boolean isSameClass(User user1, User user2) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		if (user1 == null || user2 == null || user1.getClasses() == null || user2.getClasses() == null ){
			return false;
		}
		Set<EClass>  setClasses1 = user1.getClasses();
		Set<EClass>  setClasses2 = user2.getClasses();
		for (EClass e1 : setClasses1 ){
			for (EClass e2 : setClasses2 ){
				if (e1.getId() == e2.getId()){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isSameClass(Integer id, List<Integer> list) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
				
		User user1 = findById(id);
		for (Integer user_id: list){
			User user2 = findById(user_id);
			if (!isSameClass(user1,user2)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isSameSChool(User user1, User user2) {
		
		
		if (user1== null || user2 == null || user1.getSchool_id() == 0 || user2.getSchool_id() == 0){
			return false;
		}
		if (user1.getSchool_id() == user2.getSchool_id()){
			return true;
		}
		return false;
	}

	@Override
	public boolean isSameSChool(Integer id, List<Integer> list) {
		User user1 = findById(id);
		for (Integer user_id: list){
			User user2 = findById(user_id);
			if (!isSameSChool(user1,user2)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isHeadTeacherOf(User user, Integer teacher_id) {
		if (user == null || user.getClasses() == null || teacher_id == null || teacher_id.intValue() == 0){
			return false;
		}
		for (EClass e : user.getClasses()){
    		if (e.getHead_teacher_id() == teacher_id.intValue()){
    			return true;
    		}
    	}
		return false;
	}

	@Override
	public boolean isBelongToClass(Integer user_id, Integer class_id) {
		if (user_id == null || class_id == null ||user_id.intValue() == 0 ||  class_id.intValue() == 0){
			return false;
		}
		User user = findById(user_id);
		for (EClass e : user.getClasses()){
    		if (e.getId() == class_id.intValue()){
    			return true;
    		}
    	}
		
		return false;
	}

	@Override
	public User createAdmin(String sso_id, String pass,Integer school_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("sso_id:"+sso_id);
		logger.info("school_id:"+school_id.intValue());
		
		validSSO_ID(sso_id, E_ROLE.ADMIN);
		
		User user = new User();
		user.setActflg("A");
		user.setSchool_id(Integer.valueOf(0));
		user.setSso_id(sso_id);
		user.setPassword(encryptPass(pass));
		user.setRoles(E_ROLE.ADMIN.getRole_short());
		user.setState(1);
		user.setSchool_id(school_id);
		userDao.saveUser(null,user);
		return user;
	}

	/***
	 * Filter list user by calss_id
	 * @param list
	 * @param filter_class_id
	 * @return
	 */
	public  ArrayList<User> filterByClasses(ArrayList<User> list, String filter_classes ){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		ArrayList<User> new_list = new ArrayList<>();
		// Filter user by class
		if (filter_classes != null && filter_classes.length() > 0){
			for (User user : list){
				for (String str_id : filter_classes.split(",")){
					Integer cls_id = Utils.parseInteger(str_id);
					if (cls_id != null && cls_id > 0){
						if (isBelongToClass(user.getId(), cls_id)){
							new_list.add(user);
							break;
						}
					}
				}
			}
		}else{
			return list;
		}
		return new_list;
	}
	
	public  ArrayList<User> filterByRoles(ArrayList<User> list, String filter_roles ){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		ArrayList<User> new_list = new ArrayList<>();
		// Filter user by class
		if (filter_roles != null && filter_roles.length() > 0){
			for (User user : list){
				if (user.hasRole(filter_roles)){
					new_list.add(user);
				}
			}
		}else{
			return list;
		}
		return new_list;
	}
	public  ArrayList<User> filterByStatus(ArrayList<User> list, String filter_state ){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		ArrayList<User> new_list = new ArrayList<>();
		Integer state = Utils.parseInteger(filter_state);
		if (state == null || state == 0){
			return list;
		}
		
		for (User user : list){
				if (user.getState() == state.intValue()){
					new_list.add(user);
				}
		}
		return new_list;
	}

	@Override
	public int countUserExt(Integer school_id, Integer filter_class_id, String filter_user_role, Integer filter_sts,Integer from_row_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		return userDao.countUserExt(school_id,filter_class_id, filter_user_role, filter_sts,from_row_id);
		
	}

	@Override
	public ArrayList<User> findUserExt(Integer school_id, int from_num, int max_result, Integer filter_class_id,
			String filter_user_role, Integer filter_sts,Integer from_row_id) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		return (ArrayList<User>) userDao.findUserExt(school_id, from_num, max_result, filter_class_id, filter_user_role, filter_sts, from_row_id);
	}

	@Override
	public SchoolYear getLatestSchoolYear(User student) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		SchoolYear max = null;
		if (student.hasRole(E_ROLE.STUDENT.getRole_short())){
			ArrayList<SchoolYear> years = eduProfileService.findSchoolYearByStudentID(student.getId());
			if (years != null && years.size() > 0){
				max = years.get(0);
				for (SchoolYear year: years){
					if (max.getId().intValue() < year.getId().intValue()){
						max = year;
					}
				}
			}
		}else{
			throw new ESchoolException("User must be student to access this API", HttpStatus.BAD_REQUEST);
		}
		return max;
		
	}

	@Override
	public ArrayList<SchoolYear> getSchoolYears(User student) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		if (student.hasRole(E_ROLE.STUDENT.getRole_short())){
			ArrayList<SchoolYear> years = eduProfileService.findSchoolYearByStudentID(student.getId());
			return years;
		}else{
			throw new ESchoolException("User must be student to access this API", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public int countAvailableUser(Integer school_id, String filter_user_role) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		return userDao.countAvailableUser(school_id,filter_user_role);
	}

	@Override
	public ArrayList<User> findAvailableUser(Integer school_id, int from_num, int max_result, String filter_user_role) {
		return (ArrayList<User>) userDao.findAvailableUser(school_id, from_num, max_result,filter_user_role);
	}

	@Override
	public void updateClassTerm(User user) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		Set<EClass> classes = user.getClasses();
		if (classes != null && classes.size() > 0){
			for (EClass eclass : classes){
				classService.updateTermVal(eclass);
			}
		}
		
	}
	
	
		@Override
		public void saveUploadPhoto(User me, Integer user_id,MultipartFile []files) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			
			String err_msg = "";
			
			// Validation Data
			if (files == null  || files.length<=0 ){
				err_msg = "file is mandatory";
			}
			if (files.length >1 ){
				err_msg = "Cannot upload multiples files";
			}
			
			if (err_msg.length() > 0){
				throw new ESchoolException(err_msg,HttpStatus.BAD_REQUEST);
			}
		
			
			String upload_dir = environment.getRequiredProperty("avatar_upload_base");
			Utils.ensureFolder(upload_dir);
			String urlbase = environment.getRequiredProperty("avatar_file_url_base");
			String fileName="";
			String filePath ="";
			
			User user = userDao.findById(user_id);
			if (user == null ){
				throw new ESchoolException("user_id not existing", HttpStatus.BAD_REQUEST);
			}
			if (user.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("user_id is not belong to current user school", HttpStatus.BAD_REQUEST);
			}
			try {
				MultipartFile file = files[0];
				byte[] bytes = file.getBytes();
				String str_dir = Utils.makeFolderByTime(upload_dir);
				//fileName = file.getOriginalFilename();
				fileName = Utils.getFileName("USER_"+user.getSso_id(),file.getOriginalFilename());            	
	            filePath = str_dir+ "/" + fileName;
	            
				// Create the file on server
				File serverFile = new File(filePath);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				Utils.resizeImage(filePath, Constant.BIG_SIZE, Constant.BIG_SIZE);
				Utils.resizeImage(filePath, Constant.SMALL_SIZE, Constant.SMALL_SIZE);
				
				// Save NotifyImg to DB
				user.setPhoto(filePath.replaceFirst(upload_dir, urlbase));
				
				
				userDao.updateUser(me,user);
				
				
				
			} catch (Exception e) {
				throw new ESchoolException("You failed to upload " + fileName + " => " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
		
			
		}

		private void valid_user( User user, boolean is_new ) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			
			if (!is_new){
				if (user.getId() == null || user.getId().intValue() <= 0 ){
					throw new ESchoolException("user.id is NULL",HttpStatus.BAD_REQUEST);
				}
			}
			
			if (user.getSchool_id() == null || user.getSchool_id().intValue() == 0){
				throw new ESchoolException("User.school_id is required",HttpStatus.BAD_REQUEST);
			}
			if (user.getFullname() == null || user.getFullname().trim().length()==0){
				throw new ESchoolException("User.FullName is required",HttpStatus.BAD_REQUEST);
			}
			if (user.getSso_id() == null || user.getSso_id().trim().length()==0){
				throw new ESchoolException("User.sso_id is required",HttpStatus.BAD_REQUEST);
			}
			if (user.getRoles() == null || user.getRoles().trim().length()==0){
				throw new ESchoolException("User.roles is required",HttpStatus.BAD_REQUEST);
			}
			if (user.getAddr1() == null || user.getAddr1().trim().length()==0){
				throw new ESchoolException("User.addr1 is required",HttpStatus.BAD_REQUEST);
			}
			if (user.getBirthday() == null || user.getBirthday().trim().length()==0){
				throw new ESchoolException("User.birthday is required",HttpStatus.BAD_REQUEST);
			}
			if (user.getGender() == null || user.getGender().trim().length()==0){
				throw new ESchoolException("User.gender is required",HttpStatus.BAD_REQUEST);
			}
			if (user.hasRole(E_ROLE.STUDENT.getRole_short())){
				if (user.getCls_level() == null || user.getCls_level() == 0){
					throw new ESchoolException("cls_level (NUMBER TYPE) is required",HttpStatus.BAD_REQUEST);
				}
			}
			if (!isValidState(user.getState())){
				 throw new ESchoolException("Invalid State="+user.getState(),HttpStatus.BAD_REQUEST);
			 }
			
		}

		@Override
		public User updateAttachedUser(User me, User attached_user) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			
			userDao.updateUser(me, attached_user);
			return attached_user;
		}

		@Override
		public String saveUploadUsers(User me, MultipartFile[] files, Integer class_id) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			
			// Validation Data
			if (files == null || files.length ==0){
				throw new ESchoolException("file is null",HttpStatus.BAD_REQUEST);
			}
			if (files.length > 1){
				throw new ESchoolException("Cannot upload multiple files",HttpStatus.BAD_REQUEST);
			}
			
			if (class_id == null ){
				throw new ESchoolException("class_id is NULL",HttpStatus.BAD_REQUEST);
			}
			
			EClass eclass = classService.findById(class_id);
			if (eclass == null ){
				throw new ESchoolException("class_id not exist",HttpStatus.BAD_REQUEST);
			}
			if (eclass.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("eclass.school_id != me.school_id",HttpStatus.BAD_REQUEST);
			}
			
			
			if (! me.hasRole(E_ROLE.ADMIN.getRole_short())){
				throw new ESchoolException("me is not ADMIN",HttpStatus.BAD_REQUEST);
			}
			
			
			String upload_dir = environment.getRequiredProperty("upload_base");
			String fileName="";
			String orgName="";
			String filePath ="";
			
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				// convert to UTF-8
				if (file.isEmpty() ){
					throw new ESchoolException("File is emtpy", HttpStatus.BAD_REQUEST);
				}
				try {
					byte[] bytes = file.getBytes();
					String str_dir = Utils.makeFolderByTime(upload_dir);
					orgName = file.getOriginalFilename();
					fileName = Utils.getFileName(me.getSso_id(),file.getOriginalFilename());            	
		            filePath = str_dir+ "/" + fileName;
		            
					// Create the file on server
					File serverFile = new File(filePath);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
					logger.info("Server File Location="+ serverFile.getAbsolutePath());
					// Read file into Users
					ArrayList<User> list = impFileToClass(me,eclass,filePath,orgName);
					logger.info("Finish import list.size = "+ (list==null?"0":list.size()));
					
					
				}catch (ESchoolException es){
					throw es;
				}catch (RuntimeException er){
					throw er;
				}catch (Exception e) {
					throw new ESchoolException("You failed to upload " + fileName + " => " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return fileName;
			
		}

		private ArrayList<User>  impFileToClass(User me,EClass eclass,String filePath,String fileName) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			
			ArrayList<User> users = new ArrayList<User>();
			BufferedReader bufferedReader = null;
			
			String fullname = null;
			String nickname = null;
			String addr1 = null;
			String addr2 = null;
			String phone = null;
			String birthday = null;
			String gender = null;
			String email = null;
			String std_parent_name = null;
			
			
			if (eclass != null){
				if (eclass.getSchool_id().intValue() != me.getSchool_id().intValue()){
					throw new ESchoolException("eclass.school_id != me.school_id", HttpStatus.BAD_REQUEST);
				}
			}
			if (!me.hasRole(E_ROLE.ADMIN.getRole_short())){
				throw new ESchoolException("me.id="+me.getId().intValue()+" is not ADMIN role", HttpStatus.BAD_REQUEST);
			}
			
			
			
			try {
				String strLine;
				bufferedReader =  new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF8"));
				
				// CVS line
				// fullname,nickname,addr1,addr2,phone,birthday,gender,email,std_parent_name	
				int i=0;
				while ((strLine = bufferedReader.readLine()) != null) {
					i++;
					if (i == 1){
						continue;
					}
					logger.info("line["+i+"]:" + strLine);
					ArrayList<String> contents = (ArrayList<String>) CSVUtils.parseLine(strLine);
					if (contents != null && contents.size() >= Constant.UserCVSheader.length){
						fullname = (contents.get(0).trim().length()==0?null:contents.get(0).trim());
						nickname = (contents.get(1).trim().length()==0?null:contents.get(1).trim());
						addr1 = (contents.get(2).trim().length()==0?null:contents.get(2).trim());
						addr2 = (contents.get(3).trim().length()==0?null:contents.get(3).trim());
						phone = (contents.get(4).trim().length()==0?null:contents.get(4).trim());
						birthday = (contents.get(5).trim().length()==0?null:contents.get(5).trim());
						gender = (contents.get(6).trim().length()==0?null:contents.get(6).trim());
						email = (contents.get(7).trim().length()==0?null:contents.get(7).trim());
						std_parent_name = (contents.get(8).trim().length()==0?null:contents.get(8).trim());
						
						// validate
						if (fullname == null){
							throw new ESchoolException("fileName:"+fileName+"["+i+"], fullname is required",HttpStatus.BAD_REQUEST);
						}
						
						if (phone != null ){
							phone = Utils.validMobilePhoneNo(phone);
							if (phone == null ){
								// 0302000010 or 02029999250	
								throw new ESchoolException("fileName:"+fileName+"["+i+"], incorrect phone, only accept: 030xxx (x: 7 digits from 0-9) Or 020yyy (y: 8 digits from 0-9)",HttpStatus.BAD_REQUEST);
							}
						}
						
						if (birthday != null){
							Date dt = Utils.parsetDateAll(birthday);// YYYY-MM-DD
							if (dt == null){
								throw new ESchoolException("fileName:"+fileName+"["+i+"], birthday is invalid format",HttpStatus.BAD_REQUEST);
							}else{
								birthday = Utils.dateToString(dt);
							}
						}

						if (gender != null ){
							if ( gender.equals("male") ||gender.equals("female") ){
								// do nothing
							}else{
								throw new ESchoolException("fileName:"+fileName+"["+i+"], gender must be male, female",HttpStatus.BAD_REQUEST);
							}
						}
						
						
						// Parsing CLS_LEVLE
						
						User user = new User();
						// Default value
						user.setSchool_id(eclass.getSchool_id());
						user.setCls_level(eclass.getLevel());
						user.setState(E_STATE.ACTIVE.value());
						user.setSso_id("STUDENT");
						user.setRoles(E_ROLE.STUDENT.getRole_short());
						////////////
						user.setFullname(fullname);
						user.setNickname(nickname);
						user.setAddr1(addr1);
						user.setAddr2(addr2);
						user.setPhone(phone);
						user.setBirthday(birthday);
						user.setGender(gender);
						user.setEmail(email);
						user.setStd_parent_name(std_parent_name);
						
						// Save to list
						users.add(user);
					}else{
						throw new ESchoolException("fileName:"+fileName+"["+i+"], invalid CVS content, not enough header lenght! ",HttpStatus.BAD_REQUEST);
					}
				}
				if (i <= 1){
					throw new ESchoolException("fileName:"+fileName+"["+i+"], invalid CVS content, blank file",HttpStatus.BAD_REQUEST);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("readFileToUsers() exception:"+e.getMessage());
			} finally {
				try {
					if (bufferedReader != null) bufferedReader.close();
				} catch (IOException crunchifyException) {
					crunchifyException.printStackTrace();
				}
			}
			// Save users list to DB
			for (User user: users){
				// Save to DB
				E_ROLE role ;
				if (user.hasRole(E_ROLE.TEACHER.getRole_short())){
					role = E_ROLE.TEACHER;
				}else if (user.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short())){
					role = E_ROLE.CLS_PRESIDENT;
				}else{
					role = E_ROLE.STUDENT;
				}
				createUser(me, user, role);
				if (user.getId() != null && eclass != null ){
					user2ClassService.assignUserToClass(me, user.getId(),eclass.getId(),"IMPORT file: "+fileName);
				}
			}
			return users;
			
		}

		@Override
		public User2Class assignUser2Class(User me, Integer user_id, Integer class_id, String notice) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			logger.info("user_id:"+user_id.intValue());
			logger.info("class_id:"+class_id.intValue());
			if (user_id == null || user_id.intValue() == 0){
				throw new ESchoolException("user_id is required", HttpStatus.BAD_REQUEST);
			}
			
			if (class_id == null || class_id.intValue() == 0){
				throw new ESchoolException("class_id is required", HttpStatus.BAD_REQUEST);
			}
			
			User2Class user2Class = user2ClassService.assignUserToClass(me, user_id, class_id, notice);
			return user2Class;
		}

		@Override
		public void deleteUser(User me, Integer user_id) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");

			if (!me.hasRole(E_ROLE.ADMIN.getRole_short())){
				throw new ESchoolException("me is not ADMIN role, cannot call this API", HttpStatus.FORBIDDEN);
			}
			
			if (user_id == null ){
				throw new ESchoolException("user_id is null", HttpStatus.BAD_REQUEST);
			}
			
			User del_user = userDao.findById(user_id);
			if (del_user == null ){
				throw new ESchoolException("Cannot find user id:"+user_id.intValue(), HttpStatus.BAD_REQUEST);
			}
			if (me.getId().intValue() == user_id.intValue()){
				throw new ESchoolException("Cannot del himself", HttpStatus.BAD_REQUEST);
			}
			if (	del_user.hasRole(E_ROLE.ADMIN.getRole_short()) ){
				throw new ESchoolException("Cannot del Admin account", HttpStatus.BAD_REQUEST);
			}
			
			if (del_user.hasRole(E_ROLE.SYS_ADMIN.getRole_short()) ){
				throw new ESchoolException("Cannot del SysAdmin account", HttpStatus.BAD_REQUEST);
			}

			
			if (!isSameSChool(me, del_user)){
				throw new ESchoolException("me and del_user are not in the same School", HttpStatus.BAD_REQUEST);
			}
			// Delete relationship user <===> class
			user2ClassService.delUser(me, user_id);
			// Delete user info
			del_user.setActflg("D");
			userDao.updateUser(me, del_user);
			
		}

		@Override
		public void removeUser2Class(User me, Integer user_id, Integer class_id, String notice) {
			String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(" *** " + method_name + "() START");
			logger.info("user_id:"+user_id.intValue());
			logger.info("class_id:"+class_id.intValue());
			
			user2ClassService.removeUserToClass(me, user_id, class_id, notice);
			
		}

	
		
}
