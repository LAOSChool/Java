package com.itpro.restws.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
import com.itpro.restws.model.User2Class;

public interface UserService {
	User findById(Integer id);
	User findBySso(String sso);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	
	ArrayList<User> findBySchool(Integer school_id,int from_num, int max_result); 
	ArrayList<User> findByClass(Integer class_id,int from_num, int max_result);
	
	int countUserExt(Integer school_id,Integer filter_class_id,String filter_user_role,Integer filter_sts,Integer from_row_id);
	ArrayList<User> findUserExt(Integer school_id,int from_num, int max_result,Integer filter_class_id,String filter_user_role,Integer filter_sts,Integer from_row_id);
	
	
	//User insertUser(User me, User user);
	User updateTransientUser(User me, User user,boolean ignore_pass);
	User updateAttachedUser(User me, User user);
	
	public boolean isValidState(int State);
	public boolean isValidPassword(String pass);
	public void validSSO_ID(String username,E_ROLE role);
	public String encryptPass(String password);
	public String changePassword(User me, String sso_id, String old_pass, String new_pass);
	public String forgotPassword(User me, String sso_id, String phone);
	public String resetPassword(User me, String sso_id,boolean by_forgot_request);
	
	
	
	boolean isSameClass(User user1, User user2);
	boolean isSameClass(Integer id, List<Integer> list);
	
	boolean isSameSChool(User user1, User user2);
	boolean isSameSChool(Integer id, List<Integer> list);
	
	boolean isHeadTeacherOf(User user, Integer teacher_id);
	boolean isBelongToClass(Integer user_id, Integer class_id);
	ArrayList<User> filterByStatus(ArrayList<User> list, String filter_state );
	ArrayList<User> filterByRoles(ArrayList<User> list, String filter_roles );
	ArrayList<User> filterByClasses(ArrayList<User> list, String filter_classes );
	
	SchoolYear getLatestSchoolYear(User student);
	ArrayList<SchoolYear> getSchoolYears(User student);
	

	int countAvailableUser(Integer school_id, String filter_user_role);
	ArrayList<User> findAvailableUser(Integer school_id,int from_num, int max_result,String filter_user_role);
	void updateClassTerm(User user);
	String saveUploadUsers(User me,MultipartFile[] file,Integer class_id, String file_type);
	
	
	void saveUploadPhoto(User me,Integer user_id, MultipartFile[] file);
	void removeUser2Class(User me,Integer user_id, Integer class_id,  String notice);
	User2Class assignUser2Class(User me, Integer user_id, Integer class_id, String notice);
	void deleteUser(User me,Integer user_id);
	
	public User createUser(User me, User user,E_ROLE role,boolean imp_file);
	public User createAdmin(String sso_id,String pass,Integer school_id);
	void logout(User me, User user);
	File createTmpDownloadExcel(User me);
	
}