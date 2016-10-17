package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("user_id:"+user_id.intValue());
		logger.info("class_id:"+class_id.intValue());
		
		
		User user = userDao.findById(user_id);
		EClass eclass = classesDao.findById(class_id);
		
		
		
		if (user == null  ){
			throw new ESchoolException("user_id is not existing:"+ user_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		
//		if (user.getClasses() != null && user.getClasses().size() > 0){
//			throw new ESchoolException("User already assigned to other class", HttpStatus.BAD_REQUEST);	
//		}
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
		// Check CLASS level if is STUDENT 
		if (user.hasRole(E_ROLE.STUDENT.getRole_short())){
			if (eclass.getLevel() == null ||  
					eclass.getLevel().intValue()== 0 
					){
				throw new ESchoolException("class.level is NULL or Zero, please initial this value before assign", HttpStatus.BAD_REQUEST);
			}
			if (
					user.getCls_level()== null ||
					user.getCls_level().intValue() == 0
					
					){
				throw new ESchoolException("user.cls_level is NULL or Zero, please initial this value before assign", HttpStatus.BAD_REQUEST);
			}
			
			if (eclass.getLevel().intValue() != user.getCls_level().intValue()){
				throw new ESchoolException("Cannot assign STUDENT to class due to: class.level("+eclass.getLevel().intValue()+") != user.cls_levlel("+user.getCls_level().intValue()+")", HttpStatus.BAD_REQUEST);
			}
		}
		// 20160822 Disable oneuser multiple class
		if (user.getClasses() != null && user.getClasses().size() > 0){
			EClass tmp =user.getClasses().iterator().next();
			throw new ESchoolException("User already assigned to class_id:"+tmp.getId().intValue(), HttpStatus.BAD_REQUEST);
		}
		
//		// find existing user 2 class
//		List<User2Class> list = user2ClassDao.findByUserAndClass(class_id, user_id,false);		
//		if (list != null && list.size() > 0){
//			//start_user_profile(user,eclass,schoolYear,notice);
//			throw new ESchoolException("User already assigned to class", HttpStatus.BAD_REQUEST);
//		}
//				
//				
//		// Disable one user student assigned to many classes
//		if (user.hasRole(E_ROLE.STUDENT.getRole_short())){
//			ArrayList<User2Class> existing_relations = (ArrayList<User2Class>) user2ClassDao.findByUserId(user.getId(),true);
//			if (existing_relations != null && existing_relations.size() > 0){
//				throw new ESchoolException("Cannot assigne one studen to many classes: student_id="+user.getId().intValue()+"///class_id="+class_id.intValue(), HttpStatus.BAD_REQUEST);
//			}
//		}
//		
		User2Class user2Class = null;
		// Find active connections
		List<User2Class> list = user2ClassDao.findByUserAndClass(user_id, class_id, 0);
		if (list != null && list.size() > 0){
			user2Class = list.get(0);
			return user2Class;
		}
		
		user2Class = new User2Class();
		
		
		user2Class.setSchool_id(admin.getSchool_id());
		
		user2Class.setClass_id(class_id);
		user2Class.setCls_title(eclass.getTitle());
		user2Class.setUser_id(user.getId());
		user2Class.setSso_id(user.getSso_id());
		user2Class.setSchool_year_id(schoolYear.getId());
		
		user2Class.setAssigned_dt(Utils.now());
		user2Class.setNotice(notice);
		user2Class.setClosed(0);
		
		user2ClassDao.saveUser2Class(admin,user2Class);
		
		return user2Class;
		
	}
	@Override
	public void removeUserToClass(User me, Integer user_id, Integer class_id, String notice) {
		logger.info("revmoveUserToClass START,user_id:"+user_id.intValue()+"///+class_id:"+class_id.intValue());
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
		if (!me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			if (user.getSchool_id().intValue() != me.getSchool_id().intValue() ){
				throw new ESchoolException("assigned user & current user are not in same School", HttpStatus.BAD_REQUEST);
			}
		}
		// Only running user2Class available		
		List<User2Class> list = user2ClassDao.findByUserAndClass(user_id, class_id,0);
		if (list == null || list.size() == 0){
			throw new ESchoolException("user is not assigned to classed yet", HttpStatus.BAD_REQUEST);
		}
		
		// Remove class head techer if need
		if (eclass.getHead_teacher_id() != null && eclass.getHead_teacher_id().intValue() == user.getId().intValue()){
			eclass.setHead_teacher_id(null);
			eclass.setHeadTeacherName(null);
			classesDao.updateClass(me,eclass);
		}
		// Remove relationship
		for (User2Class user2Class: list){
			user2Class.setClosed(1);
			user2Class.setClosed_dt(Utils.now());
			user2Class.setNotice("AUTO when call: removeUserToClass()");
			user2ClassDao.updateUser2Class(me,user2Class);
		}
		
	

		
		
	}
	@Override
	public void delUser(User me, Integer user_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("user_id:"+user_id.intValue());
		
		User user = userDao.findById(user_id);
		
		if (user == null ){
			return;
		}
		
		if (!me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			if (user.getSchool_id().intValue() != me.getSchool_id().intValue() ){
				throw new ESchoolException("assigned user & current user are not in same School", HttpStatus.BAD_REQUEST);
			}
		}
		// Del head teacher
		if (user.hasRole(E_ROLE.TEACHER.getRole_short())){
			Set<EClass> classes = user.getClasses();
			for (EClass  cls: classes){
				if (cls.getHead_teacher_id() != null && cls.getHead_teacher_id().intValue() == user.getId().intValue()){
					cls.setHead_teacher_id(null);
					cls.setHeadTeacherName(null);
					classesDao.updateClass(me,cls);
				}
			}
		}
				
		// Only running user2Class available		
		List<User2Class> list = user2ClassDao.findByUserAndClass(user_id, null,0);
		if (list != null &&  list.size()  > 0){
			for (User2Class user2Class: list){
				user2Class.setActflg("D");
				user2Class.setClosed(1);
				user2Class.setClosed_dt(Utils.now());
				user2Class.setNotice("AUTO when del_user");
				user2ClassDao.updateUser2Class(me,user2Class);
			}	
		}
		
		
		
		
	}
	@Override
	public void delClass(User me, Integer class_id) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("class_id:"+class_id.intValue());
		
		
		EClass eclass = classesDao.findById(class_id);
		
		if (eclass == null ){
			return;
		}
		if (!me.hasRole(E_ROLE.SYS_ADMIN.getRole_short())){
			if (eclass.getSchool_id().intValue() != me.getSchool_id().intValue() ){
				throw new ESchoolException("eclass & current user are not in same School", HttpStatus.BAD_REQUEST);
			}
		}
		// Only running user2Class available		
		List<User2Class> list = user2ClassDao.findByUserAndClass(null, class_id,0);
		if (list != null &&  list.size() > 0){
			for (User2Class user2Class: list){
				user2Class.setActflg("D");
				user2Class.setClosed(1);
				user2Class.setClosed_dt(Utils.now());
				user2Class.setNotice("AUTO - When Del Class");
				user2ClassDao.updateUser2Class(me,user2Class);
			}
		}
		
		
		
	}
	

	
	
}
