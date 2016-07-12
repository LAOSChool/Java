package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.User2ClassDao;
import com.itpro.restws.dao.UserDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
import com.itpro.restws.model.User2Class;

@Service("user2ClassService")
@Transactional
public class User2ClassServiceImpl implements User2ClassService{
	private static final Logger logger = Logger.getLogger(User2ClassServiceImpl.class);
	@Autowired
	private User2ClassDao user2ClassDao;

	
	@Autowired
	private UserDao userDao;
	
//	@Autowired
//	private SchoolDao schoolDao;
	
	@Autowired
	private ClassDao classesDao;
	
//	@Autowired
//	private SchoolYearDao schoolYearDao;
	

//	@Autowired
//	private EduProfileDao eduProfileDao;
	
	@Autowired
	protected SchoolYearService schoolYearService;
	
	@Override
	public User2Class findById(Integer id) {
		
		return user2ClassDao.findById(id);
	}
	@Override
	public ArrayList<User2Class> findByUserId(Integer user_id,boolean is_running) {
		return (ArrayList<User2Class>) user2ClassDao.findByUserId(user_id,is_running);
	}
	@Override
	public int countBySchoolID(Integer school_id) {
		
		return user2ClassDao.countBySchoolId(school_id);
	}
	@Override
	public int countByClassID(Integer class_id) {
		return user2ClassDao.countByClassId(class_id);
	}
	@Override
	public ArrayList<User2Class> findBySchool(Integer school_id, int from_num, int max_result) {
		return (ArrayList<User2Class>) user2ClassDao.findBySchoolId(school_id, from_num, max_result);
	}
	@Override
	public ArrayList<User2Class> findByClass(Integer class_id, int from_num, int max_result) {
		return (ArrayList<User2Class>) user2ClassDao.findByClassId(class_id, from_num, max_result);
	}
	@Override
	public User2Class assignUserToClass(User admin, Integer user_id, Integer class_id, String notice) {
		logger.info("assignUserToClass START");
		User user = userDao.findById(user_id);
		EClass eclass = classesDao.findById(class_id);
		
		
		if (user == null ){
			throw new ESchoolException("user_id is not existing:"+ user_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (eclass == null ){
			throw new ESchoolException("class_id is not existing:"+ class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (user.getSchool_id().intValue() != eclass.getSchool_id().intValue() ){
			throw new ESchoolException("user assigned to class are not in same School", HttpStatus.BAD_REQUEST);
		}
		
		if (user.getSchool_id().intValue() != admin.getSchool_id().intValue() ){
			throw new ESchoolException("assigned user & current user are not in same School", HttpStatus.BAD_REQUEST);
		}
		// Get school_year
		SchoolYear schoolYear = schoolYearService.findLatestYearBySchool(user.getSchool_id());
		if (schoolYear == null){
			throw new ESchoolException("schoolYear of school_id is null, school_id =  "+user.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		// find existing user 2 class
		List<User2Class> list = user2ClassDao.findByUserAndClass(class_id, user_id,false);		
		if (list != null && list.size() > 0){
			//start_user_profile(user,eclass,schoolYear,notice);
			throw new ESchoolException("User already assigned to class", HttpStatus.BAD_REQUEST);
		}
				
				
		// Disable one user student assigned to many classes
		if (user.hasRole(E_ROLE.STUDENT.getRole_short())){
			ArrayList<User2Class> existing_relations = (ArrayList<User2Class>) user2ClassDao.findByUserId(user.getId(),true);
			if (existing_relations != null && existing_relations.size() > 0){
				throw new ESchoolException("Cannot assigne one studen to many classes: student_id="+user.getId().intValue()+"///class_id="+class_id.intValue(), HttpStatus.BAD_REQUEST);
			}
		}
		
		
	
		
		User2Class user2Class = new User2Class();
		user2Class.setSchool_id(admin.getSchool_id());
		user2Class.setClass_id(class_id);
		user2Class.setUser_id(user_id);
		user2Class.setSchool_year_id(schoolYear.getId());
		
		user2Class.setAssigned_dt(Utils.now());
		user2Class.setNotice(notice);
		
		user2ClassDao.saveUser2Class(user2Class);
		
//		// Start new Student Profile
//		start_user_profile(user,eclass,schoolYear,notice);
//		if (user.hasRole(E_ROLE.STUDENT.getRole_short())){
//			// Get school info
//			School school = schoolDao.findById(user.getSchool_id());
//				
//			EduProfile eduProfile = new EduProfile();
//			
//			eduProfile.setSchool_id(user.getSchool_id());
//			eduProfile.setSchool_name(school.getTitle());
//			eduProfile.setCls_id(class_id);
//			eduProfile.setCls_name(eclass.getTitle());
//			eduProfile.setCls_level(eclass.getLevel());
//			eduProfile.setCls_location(eclass.getLocation());
//			eduProfile.setTeacher_id(eclass.getHead_teacher_id());
//			eduProfile.setTeacher_name(eclass.getHeadTeacherName());
//			eduProfile.setStudent_id(user.getId());
//			eduProfile.setStudent_name(user.getFullname());
//						
//			eduProfile.setSchool_year_id(schoolYear.getId());
//			
//			eduProfile.setSchool_years(schoolYear.getYears());
//			eduProfile.setStart_dt(Utils.now());
//			eduProfile.setNotice(notice);
//
//			// Check existing profile
//			boolean is_existing = false;
//			ArrayList<EduProfile> std_prof_list = eduProfileDao.findByStudentID(user.getId());
//			if (std_prof_list != null ){
//				for (EduProfile db_profile: std_prof_list){
//					if ((db_profile.getSchool_id().intValue() == user.getSchool_id().intValue()) &&
//					   (db_profile.getCls_id().intValue() == class_id.intValue()) &&
//					   (db_profile.getStudent_id().intValue() == user.getId().intValue()) &&
//					   (db_profile.getSchool_year_id().intValue() == schoolYear.getId().intValue()) && 
//					   (db_profile.getClosed().intValue() == 0)){
//						// Do nothing
//						logger.info("already exising Student profile, do nothing.");
//						is_existing = true;
//						break;
//					}
//				}
//			}
//			if (!is_existing){
//				eduProfileDao.saveStudentProfile(eduProfile);
//			}
//			
//		}
		return user2Class;
		
	}
	
//	private void start_user_profile(User student, EClass eclass, SchoolYear schoolYear, String notice){
//		// Start new Student Profile
//		if (student.hasRole(E_ROLE.STUDENT.getRole_short())){
//			Integer school_id = student.getSchool_id();
//			Integer class_id = eclass.getId();
//			Integer school_year_id = schoolYear.getId();
//			
//			// Get school info
//			School school = schoolDao.findById(student.getSchool_id());
//				
//			EduProfile eduProfile = new EduProfile();
//			
//			eduProfile.setSchool_id(school_id);
//			eduProfile.setSchool_name(school.getTitle());
//			eduProfile.setCls_id(class_id);
//			eduProfile.setCls_name(eclass.getTitle());
//			eduProfile.setCls_level(eclass.getLevel());
//			eduProfile.setCls_location(eclass.getLocation());
//			eduProfile.setTeacher_id(eclass.getHead_teacher_id());
//			eduProfile.setTeacher_name(eclass.getHeadTeacherName());
//			eduProfile.setStudent_id(student.getId());
//			eduProfile.setStudent_name(student.getFullname());
//						
//			eduProfile.setSchool_year_id(school_year_id);
//			
//			eduProfile.setSchool_years(schoolYear.getYears());
//			eduProfile.setStart_dt(Utils.now());
//			eduProfile.setNotice(notice);
//
//			// Check existing profile
//			boolean is_existing = false;
//			ArrayList<EduProfile> std_prof_list = eduProfileDao.findByStudentID(student.getId());
//			if (std_prof_list != null ){
//				for (EduProfile db_profile: std_prof_list){
//					if ((db_profile.getSchool_id().intValue() == student.getSchool_id().intValue()) &&
////					   (db_profile.getCls_id().intValue() == class_id.intValue()) &&
//					   (db_profile.getStudent_id().intValue() == student.getId().intValue()) &&
//					   (db_profile.getSchool_year_id().intValue() == schoolYear.getId().intValue()))  
////					   (db_profile.getClosed().intValue() == 0))
//					{
//						// Do nothing
//						logger.info("already exising Student profile, do nothing.");
//						is_existing = true;
//						break;
//					}
//				}
//			}
//			if (!is_existing){
//				eduProfileDao.saveStudentProfile(eduProfile);
//			}
//		}
//					
//	}
//	private void close_user_profile(User admin, Integer user_id, Integer class_id, String notice){
//		//assignUserToClass.in
//	}
	

	
	
}
