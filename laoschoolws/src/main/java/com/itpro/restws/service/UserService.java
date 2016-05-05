package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.List;

import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.model.User;

public interface UserService {
	User findById(Integer id);
	User findBySso(String sso);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	
	ArrayList<User> findBySchool(Integer school_id,int from_num, int max_result); 
	ArrayList<User> findByClass(Integer class_id,int from_num, int max_result);
	
	int countUserExt(Integer school_id,Integer filter_class_id,String filter_user_role,Integer filter_sts,Integer from_row_id);
	ArrayList<User> findUserExt(Integer school_id,int from_num, int max_result,Integer filter_class_id,String filter_user_role,Integer filter_sts,Integer from_row_id);
	
	
	User insertUser(User user);
	User updateUser(User user);
	public boolean isValidState(int State);
	public boolean isValidPassword(String pass);
	public boolean isValidUserName(String username);
	public String encryptPass(String password);
	public String changePassword(String sso_id, String old_pass, String new_pass);
	public String forgotPassword(String sso_id, String phone);
	public String resetPassword(String sso_id);
	
	public User createUser(User user,E_ROLE role);
	public User createAdmin(String sso_id,String pass,Integer school_id);
	
	boolean isSameClass(User user1, User user2);
	boolean isSameClass(Integer id, List<Integer> list);
	
	boolean isSameSChool(User user1, User user2);
	boolean isSameSChool(Integer id, List<Integer> list);
	
	boolean isHeadTeacherOf(User user, Integer teacher_id);
	boolean isBelongToClass(Integer user_id, Integer class_id);
	ArrayList<User> filterByStatus(ArrayList<User> list, String filter_state );
	ArrayList<User> filterByRoles(ArrayList<User> list, String filter_roles );
	ArrayList<User> filterByClasses(ArrayList<User> list, String filter_classes );
	
	
}