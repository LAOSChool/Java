package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.EduProfileDao;
import com.itpro.restws.dao.SchoolDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Attendance;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.EduProfile;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.School;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

@Service("eduProfileService")
@Transactional
public class EduProfileServiceImpl implements EduProfileService{
	private static final Logger logger = Logger.getLogger(EduProfileServiceImpl.class);
	
	
	@Autowired
	protected SchoolYearService schoolYearService;
	
	@Autowired
	protected EduProfileDao eduProfileDao;
	@Autowired
	protected ExamResultService examResultService;
	@Autowired
	protected SchoolDao schoolDao;

	@Autowired
	protected ClassService classService;
	
	
	@Autowired
	protected UserService userService;
	@Autowired
	protected AttendanceService attendanceService;

	
//	@Autowired
//	protected SchoolYearDao schoolYearDao;
	
	@Override
	public ArrayList<SchoolYear> findSchoolYearByStudentID(Integer student_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("student_id"+(student_id==null?"null":student_id.intValue()));
		
		logger.info("findSchoolYearByStudentID START");
		ArrayList<SchoolYear> ret = new ArrayList<>();
		ArrayList<EduProfile> profiles = eduProfileDao.findByStudentID(student_id);
		for (EduProfile eduProfile: profiles){
			SchoolYear schoolYear = schoolYearService.findById(eduProfile.getSchool_year_id());
			boolean insert_ok = true;
			for (SchoolYear year : ret){
				if (year.getId().intValue() == schoolYear.getId().intValue()){
					insert_ok = false;
					break;
				}
			}
			if (insert_ok){
				ret.add(schoolYear);
			}
		}
		return ret;
	}

	@Override
	public ArrayList<EduProfile> getUserProfile(User me, Integer filter_year_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("filter_year_id"+(filter_year_id==null?"null":filter_year_id.intValue()));
		
		
		ArrayList<EduProfile> eduProfiles = new ArrayList<EduProfile>();
		SchoolYear curr_year = schoolYearService.findLatestYearBySchool(me.getSchool_id());
		
		
		if (filter_year_id== null || 
				(curr_year.getId().intValue() == filter_year_id.intValue())){
			// Current profile
			ArrayList<EduProfile> current_profiles =  eduProfileDao.findEx(me.getId(), me.getSchool_id(), null, curr_year.getId());
			if (current_profiles == null || current_profiles.size() <= 0){
				EduProfile current_profile = new_blank_edu_profile(me, curr_year.getId());
				if (current_profile != null ){
					eduProfileDao.saveStudentProfile(null,current_profile);
					eduProfiles.add(current_profile);
				}else{
					throw new ESchoolException("new_blank_edu_profile() return null", HttpStatus.BAD_REQUEST);
				}
			}else{
				eduProfiles.addAll(current_profiles);
			}
			
		
		}else{
			// Past profile
			if (filter_year_id.intValue() > curr_year.getId().intValue()){
				throw new ESchoolException("filter_year_id:"+filter_year_id.intValue() +" > current year id: "+curr_year.getId().intValue(), HttpStatus.BAD_REQUEST);
			}
			eduProfiles = eduProfileDao.findEx(me.getId(), null, null, filter_year_id);
		}
		for (EduProfile eduProfile: eduProfiles){
			ArrayList<ExamResult> examResults = examResultService.getUserProfile(me,me, null, filter_year_id);
			eduProfile.setExam_results(examResults);
			// update attendace info
			ArrayList<Attendance> attendances = attendanceService.findAttendanceExt(me.getSchool_id(), null, me.getId(), null, 0, 999999, null, null, null, null, null, eduProfile.getSchool_year_id());
			if (attendances != null ){
				eduProfile.setDay_absents(attendances.size());
			}
			
		}
		return eduProfiles; 
	}

	private EduProfile new_blank_edu_profile(User student, Integer year_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("year_id"+(year_id==null?"null":year_id.intValue()));
		
		School sch = schoolDao.findById(student.getSchool_id());
		SchoolYear schoolYear = schoolYearService.findById(year_id);
		
		if (sch == null ){
			throw new ESchoolException("Cannot get School for user.school_id:"+student.getSchool_id().intValue(),HttpStatus.BAD_REQUEST);
		}
		
		if (schoolYear == null ){
			throw new ESchoolException("Cannot get SchoolYear for year_id:"+year_id.intValue(),HttpStatus.BAD_REQUEST);
		}
		EduProfile eduProfile = new EduProfile();
		eduProfile.setSchool_id(student.getSchool_id());
		eduProfile.setSchool_name(sch.getTitle());
		
		eduProfile.setStudent_id(student.getId());
		eduProfile.setStudent_name(student.getSso_id());
		eduProfile.setSchool_year_id(year_id);
		eduProfile.setSchool_years(schoolYear.getYears());
		eduProfile.setStart_dt(Utils.now());
		
		return eduProfile;
	}

	@Override
	public ArrayList<EduProfile> getClassProfile(User me, Integer class_id, Integer student_id, Integer year_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("class_id"+(class_id==null?"null":class_id.intValue()));
		logger.info("student_id"+(student_id==null?"null":student_id.intValue()));
		logger.info("year_id"+(year_id==null?"null":year_id.intValue()));
		
		if ((class_id == null || class_id.intValue() == 0) &&
		   (student_id == null || student_id.intValue() == 0) ){
			throw new ESchoolException("Must input filter_class_id OR filter_student_id:", HttpStatus.BAD_REQUEST);
		}
		
		Integer school_id = me.getSchool_id();
		// Check student
		ArrayList<User> users = new ArrayList<User>();
		if (student_id != null && student_id.intValue() > 0){
			User filter_student = userService.findById(student_id);
			if (filter_student == null ){
				throw new ESchoolException("filter_student_id is not existing:"+ student_id.intValue(), HttpStatus.BAD_REQUEST);
			}
			if (filter_student.getSchool_id().intValue() != school_id.intValue()){
				throw new ESchoolException("filter_student_id is not in SAME school with teacher", HttpStatus.BAD_REQUEST);
			}
			if (me.hasRole(E_ROLE.TEACHER.getRole_short())){
				if (!userService.isSameClass(me, filter_student)){
					throw new ESchoolException("filter_student_id is not in SAME class with teacher", HttpStatus.BAD_REQUEST);
				}
			}
			users.add(filter_student);
		}
		else if (class_id != null && class_id.intValue() > 0){
			EClass filter_eclass = classService.findById(class_id);
			if (filter_eclass == null ){
				throw new ESchoolException("filter_class_id is not existing:"+ class_id.intValue(), HttpStatus.BAD_REQUEST);
			}
			if (filter_eclass.getSchool_id().intValue() != school_id.intValue()){
				throw new ESchoolException("filter_class_id is not in SAME school with teacher", HttpStatus.BAD_REQUEST);
			}
			if (me.hasRole(E_ROLE.TEACHER.getRole_short())){
				if (!userService.isBelongToClass(me.getId(), class_id)){
					throw new ESchoolException("teacher:"+me.getId().intValue()+" is not belong to filter_class_id:"+class_id.intValue(), HttpStatus.BAD_REQUEST);
				}
			}
			Set<User> setUsers = filter_eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
			if (setUsers == null || setUsers.size() <=0){
				throw new ESchoolException("filter_class_id has no STUDENT:"+class_id.intValue(), HttpStatus.BAD_REQUEST);
			}
			for (User usr : setUsers){
				users.add(usr);
			}
		}
		ArrayList<EduProfile> eduProfiles = new ArrayList<EduProfile>();
		if (users.size() > 0){
			for (User student: users){
				eduProfiles.addAll(getUserProfile(student, year_id));
			}
		}
		return eduProfiles;
	}



}
