package com.itpro.restws.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.UserDao;
import com.itpro.restws.helper.Password;
import com.itpro.restws.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDao userDao;

	@Autowired
	private PermitService permitService;
	
	public User findById(int id) {
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
	ArrayList<User> findBySchool(int school_id,int from_num, int max_result){
		
		ArrayList<User> list = (ArrayList<User>)userDao.findBySchool(school_id, from_num, max_result);
		for (User user : list){
			if (user != null){
				permitService.loadPermit(user);
			}
		}
		return list;
	}

	@Override
	public int countBySchoolID(int school_id) {
		return userDao.countUserBySchool(school_id);
	}

	@Override
	public ArrayList<User> findByClass(int class_id, int from_num, int max_result) {
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
	public String changePassword(User user, String new_pass) {
		String saltedPass =new_pass;
		try {
			saltedPass = Password.getSaltedHash(new_pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setPassword(saltedPass);
		return saltedPass;
	}

}
