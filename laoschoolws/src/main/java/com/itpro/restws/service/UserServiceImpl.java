package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.UserDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.Password;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.Message;
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
	private MessageService messageService;
	
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
	public ArrayList<User> findByClass(Integer class_id, int from_num, int max_result) {
		ArrayList<User> list = (ArrayList<User>) userDao.findByClass(class_id, from_num, max_result);
		for (User user : list){
			if (user != null){
				permitService.loadPermit(user);
			}
		}
		return list;
	}

	@Override
	public User insertUser(User user) {
		userDao.saveUser(user);
		return user;
	}

	@Override
	public User updateUser(User user) {
		User userDB = userDao.findById(user.getId());
		userDB = User.updateChanges(userDB, user);
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
	public String changePassword(String sso_id, String old_pass, String new_pass) {
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
			updateUser(user);
		}else{
			throw new RuntimeException("User is NULL, sso="+sso_id);
		}
		return new_pass;
	}

	@Override
	public String resetPassword(String sso_id) {
		int randomNum = 1111 + (int)(Math.random() * 9999);
		String newPass = randomNum+ "";
		User user = userDao.findBySSO(sso_id);
		user.setPassword(encryptPass(newPass));
		updateUser(user);
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
		// Insert into DB
		user = insertUser(user);
		// Change sso_id to DB ID
		if (role == E_ROLE.STUDENT){
			user.setSso_id(String.format("%08d", user.getId()));
			updateUser(user);
		}
		
		return user;
	}

	@Override
	public String forgotPassword(String sso_id, String phone) {
		User user = userDao.findBySSO(sso_id);
		if (user == null ){
			return "FAILED, cannot find user for sso_id"+sso_id;
		}
		if (user.getPhone() == null ){
			return "FAILED, user's phone is not registered";
		}
		if (user.getPhone() != null && phone != null && phone.equalsIgnoreCase(user.getPhone())){
			String newpass = resetPassword(sso_id);
			 Message msg = new Message();
			 msg.setFrom_usr_id(Integer.valueOf(1));
			 msg.setTo_usr_id(user.getId());
			 msg.setTitle("Thong bao forgot password");
			 msg.setContent("You has been reset password due to forgot pass requirement, new pass: "+newpass);
			 messageService.insertMessageExt(msg);
			 return "SUCCESS,new pass="+newpass;
		}
		return "FAILED, input phone is not mapped with user's phone";
	}

	@Override
	public boolean isSameClass(User user1, User user2) {
		if (user1 == null || user2 == null || user1.getClasses() == null || user2.getClasses() == null ){
			return false;
		}
		HashSet<EClass>  setClasses1 = (HashSet<EClass>) user1.getClasses();
		HashSet<EClass>  setClasses2 = (HashSet<EClass>) user2.getClasses();
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

}
