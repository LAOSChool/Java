package com.itpro.restws.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.dao.UserDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.Password;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

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
	
	public User findById(Integer id) {
		User user = userDao.findById(id);
		if (user != null){
			permitService.loadPermit(user);
		}
		return user;
	}

	public User findBySso(String sso) {
		User user = userDao.findBySSO(sso);
		if (user != null){
			permitService.loadPermit(user);
		}
		return user;
	}

	@Override
	public
	ArrayList<User> findBySchool(Integer school_id,int from_num, int max_result){
		
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
		return userDao.countUserBySchool(school_id);
	}

	@Override
	public int countByClassID(Integer class_id) {
		return userDao.countUserByClass(class_id);
	}

	
	@Override
	public ArrayList<User> findByClass(Integer class_id, int from_num, int max_result) {
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
	public User updateUser(User me, User user,boolean ignore_pass) {
		
		if (user.getId() == null ){
			throw new ESchoolException("user.id is null", HttpStatus.BAD_REQUEST);
		}
		User userDB = userDao.findById(user.getId());
		if (userDB == null ){
			throw new ESchoolException("user.id is not exising: "+user.getId().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		if (userDB.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("term_db.SchooId is not same with me.school_id", HttpStatus.BAD_REQUEST);
		}
		if (userDB.getSchool_id().intValue() != user.getSchool_id().intValue()){
			throw new ESchoolException("term_db.SchooId is not same with user.school_id", HttpStatus.BAD_REQUEST);
		}
		
		if (ignore_pass ){
			user.setPassword(null);
		}

		  try {
			  userDao.setFlushMode(FlushMode.MANUAL);
			  userDB = User.updateChanges(userDB, user);
			  valid_user(userDB, false);
	        } catch (Exception e){
	        	userDao.clearChange();
	        	throw e;
	        }
		   finally {
			   userDao.setFlushMode(FlushMode.AUTO);
	        }
		  
		
		  userDao.updateUser(userDB);
		return userDB;
		
		
		
		
		
		
		
	}

	@Override
	public boolean isValidState(int State) {
		if (State  >= 0 && State <= 4 ){
			return true;
		}
		return false;
	}



	@Override
	public boolean isValidPassword(String pass) {
		if (pass != null && !pass.equals("") && (!pass.contains(" "))){
			if (pass.length() >=4 &&  pass.length()<= 20){
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean isValidUserName(String username) {
		
		if (username != null && !username.equals("")){
			User tmp = userDao.findBySSO(username);
			if (tmp != null ){
				throw new RuntimeException("User already existing:"+username);
			}
			
			if ( (username.length() >= 4 ) &&
					(username.length() <= 20 ) && 
					!Character.isDigit(username.charAt(0))
					){
				return true;
				
			}
		}
		return false;
	}

	@Override
	public String encryptPass(String new_pass) {
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
		if (!isValidPassword(new_pass) ){
			throw new ESchoolException("Input Password length is not correct - expected length should be >= 4 AND <= 20",HttpStatus.BAD_REQUEST);
		}
		User user = userDao.findBySSO(sso_id);
		if (user != null ){
			try {
				if (!Password.check(old_pass, user.getPassword())){
					throw new RuntimeException("Input current password is not correct");
				}
			} catch (Exception e) {
				throw new ESchoolException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			user.setPassword(encryptPass(new_pass));
			updateUser(me,user,false);
		}else{
			throw new RuntimeException("User is NULL, sso="+sso_id);
		}
		return new_pass;
	}

	@Override
	public String resetPassword(User me, String sso_id, boolean is_forgot_request) {
		logger.info("resetPassword START, sso_id="+sso_id);
		
		int randomNum = 1111 + (int)(Math.random() * 9999);
		String newPass = randomNum+ "";
		User reset_user = userDao.findBySSO(sso_id);
		if (reset_user == null ){
			throw new RuntimeException("sso_id is not found");
		}
		// Check school
		if (me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			// not check
		}else{
			if (reset_user.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new RuntimeException("sso_id:"+sso_id+ " is not in same school with current admin");
			}
		}
		
		reset_user.setPassword(encryptPass(newPass));
		updateUser(me,reset_user,false);
		// Send message
		 Message msg = new Message();

		 msg.setFrom_usr_id(Integer.valueOf(1));
		 msg.setFrom_user_name("SYS_ADMIN");
		 msg.setTo_usr_id(reset_user.getId());
		 String content = "[LaoSchool] Your password has been reset by Admin, new pass: "+newPass;
		 if (is_forgot_request){
			 content = "[LaoSchool]Your password has been reset due to forgot-pass request, new pass: "+newPass;	 
		 }
		 
		 msg.setContent(content);
		 
		 msg.setChannel(Constant.SMS_CHANNEL);
		 
		 messageService.sendUserMessageWithCC(me, msg);
		logger.info("resetPassword END, sso_id="+sso_id);
		return newPass;
	}

	@Override
	public User createUser(User user, E_ROLE role) {
	
		// Update Student sso_id to unique value
		if (role == E_ROLE.STUDENT){
			user.setSso_id(user.getSso_id()+Utils.now());
		}else if (!isValidUserName(user.getSso_id())){
			throw new RuntimeException("Username is not in correct format ( Correct format: 4<= name.length<=20 and not start by a Number)");
		}
		valid_user(user,true);
		// Insert into DB
		userDao.saveUser(user);
		// Change sso_id to DB ID
		if (role == E_ROLE.STUDENT){
			user.setSso_id(String.format("%08d", user.getId()));
			//updateUser(user);
			userDao.saveUser(user);
		}
		
		return user;
	}

	@Override
	public String forgotPassword(User me, String sso_id, String phone) {
		
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
		User user = new User();
		user.setActflg("A");
		user.setSchool_id(Integer.valueOf(0));
		user.setSso_id(sso_id);
		user.setPassword(encryptPass(pass));
		user.setRoles(E_ROLE.ADMIN.getRole_short());
		user.setPhone("1234567890");
		user.setState(1);
		user.setSchool_id(school_id);
		userDao.saveUser(user);
		return user;
	}

	/***
	 * Filter list user by calss_id
	 * @param list
	 * @param filter_class_id
	 * @return
	 */
	public  ArrayList<User> filterByClasses(ArrayList<User> list, String filter_classes ){
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
		
		return userDao.countUserExt(school_id,filter_class_id, filter_user_role, filter_sts,from_row_id);
		
	}

	@Override
	public ArrayList<User> findUserExt(Integer school_id, int from_num, int max_result, Integer filter_class_id,
			String filter_user_role, Integer filter_sts,Integer from_row_id) {
		return (ArrayList<User>) userDao.findUserExt(school_id, from_num, max_result, filter_class_id, filter_user_role, filter_sts, from_row_id);
	}

	@Override
	public SchoolYear getLatestSchoolYear(User student) {
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
		if (student.hasRole(E_ROLE.STUDENT.getRole_short())){
			ArrayList<SchoolYear> years = eduProfileService.findSchoolYearByStudentID(student.getId());
			return years;
		}else{
			throw new ESchoolException("User must be student to access this API", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public int countAvailableUser(Integer school_id) {
		return userDao.countAvailableUser(school_id);
	}

	@Override
	public ArrayList<User> findAvailableUser(Integer school_id, int from_num, int max_result) {
		return (ArrayList<User>) userDao.findAvailableUser(school_id, from_num, max_result);
	}

	@Override
	public void updateClassTerm(User user) {
		Set<EClass> classes = user.getClasses();
		if (classes != null && classes.size() > 0){
			for (EClass eclass : classes){
				classService.updateTermVal(eclass);
			}
		}
		
	}
	
	
		@Override
		public void saveUploadPhoto(User me, Integer user_id,MultipartFile []files) {
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
				fileName = Utils.getFileName("USER"+user.getSso_id(),file.getOriginalFilename());            	
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
				
				
				userDao.updateUser(user);
				
				
				
			} catch (Exception e) {
				throw new ESchoolException("You failed to upload " + fileName + " => " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
		
			
		}

		private void valid_user( User user, boolean is_new ) {
			if (!is_new){
				if (user.getId() == null ){
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
			
		}
}
