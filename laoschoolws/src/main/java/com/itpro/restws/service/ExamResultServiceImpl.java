package com.itpro.restws.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itpro.restws.dao.ExamRankDao;
import com.itpro.restws.dao.ExamResultDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.ExamDetail;
import com.itpro.restws.helper.ExamRankDetail;
import com.itpro.restws.helper.RankInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.ExamRank;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
@Service("examResultService")
@Transactional
public class ExamResultServiceImpl implements ExamResultService{
	private static final Logger logger = Logger.getLogger(ExamResultServiceImpl.class);
	@Autowired
	private ExamResultDao examResultDao;
	@Autowired
	private ExamRankDao examRankDao;
	
		
	@Autowired
	private ClassService classService;
	
	@Autowired
	private MSubjectDao msubjectDao;

	
	@Autowired
	private UserService userService;
	

	@Autowired
	protected SchoolYearService schoolYearService;
//	@Autowired
//	private SchoolExamDao schoolExamDao;
	
	@Autowired
	private SchoolExamService schoolExamService;
	
	@Autowired
	private ActionLogVIPService actionLogVIPService;
	
		
	@Override
	public ExamResult findById(User me, Integer id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("id:"+(id==null?"null":id.intValue()));
		
		ExamResult rs = examResultDao.findById(id);
		if (rs == null ){
			throw new ESchoolException("Cannot find ExamID:"+id.intValue(),HttpStatus.BAD_REQUEST);
		}
		if (rs.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("Exam_ID: not in same school with User",HttpStatus.BAD_REQUEST);
		}
		return rs;
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("school_id:"+(school_id==null?"null":school_id.intValue()));
		
		return examResultDao.countExamBySchool(school_id);
	}

	@Override
	public int countByClassID(Integer class_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("class_id:"+(class_id==null?"null":class_id.intValue()));
		
		return examResultDao.countExamBySclass(class_id);
	}

	@Override
	public int countByStudentID(Integer user_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("user_id:"+(user_id==null?"null":user_id.intValue()));
		
		return examResultDao.countExamByUser(user_id);
	}

	@Override
	public ArrayList<ExamResult> findBySchoolID(Integer school_id, int from_num, int max_result) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("school_id:"+(school_id==null?"null":school_id.intValue()));
		
		return (ArrayList<ExamResult>) examResultDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByClassID(Integer class_id, int from_num, int max_result) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("class_id:"+(class_id==null?"null":class_id.intValue()));
		
		return (ArrayList<ExamResult>) examResultDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByStudentID(Integer user_id, int from_num, int max_result) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("user_id:"+(user_id==null?"null":user_id.intValue()));
		
		return (ArrayList<ExamResult>) examResultDao.findByStudent(user_id, from_num, max_result);
	}


	
//	public ExamResult updateExamResult_detach(User teacher, ExamResult examResult) {
//		ExamResult existing_db = examResultDao.findById(examResult.getId());
//		
//		existing_db = ExamResult.updateChanges(existing_db, examResult);
//		examResultDao.updateExamResult(teacher,existing_db);
//		return existing_db;
//	}


	@Override
	public ExamResult inputExam(User me, ExamResult examResult) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		String json_str = examResult.toJsonString();
		// Validation Input ( both New or Update)
		validInputExam(me,examResult);
		
		// Update exam
		if (examResult.getId() != null && examResult.getId().intValue() > 0){
			ExamResult existing_db = examResultDao.findById(examResult.getId());
			// Already checked update data in validIputExam
			// Here just double check just for sure
			if (	existing_db != null && 
					existing_db.getSchool_id().intValue() == me.getSchool_id().intValue()){
				existing_db = ExamResult.updateChanges(existing_db, examResult);
				examResultDao.updateExamResult(me,existing_db);
				// Input action Log
				actionLogVIPService.logAction_type_json(me,Constant.ACTION_TYPE_MARK,existing_db.printActLog(),json_str);
				return existing_db;
			}else{
				throw new ESchoolException("Invalid examResult.id (null or not belong to same school)", HttpStatus.BAD_REQUEST);
			}
		}else{
		// Add new exam
			examResult.setId(null);// New exam
			examResultDao.saveExamResult(me,examResult);
			// Input action Log			
			actionLogVIPService.logAction_type_json(me,Constant.ACTION_TYPE_MARK,examResult.printActLog(),json_str);
			return examResult;
		}
		
		
	}

	
	@Override
	public void validInputExam(User me, ExamResult examResult) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		// To calculate average
		//ArrayList<Float> point_years = new ArrayList<Float>();
		logger.info("validInputExam START");
		
		Integer school_id = me.getSchool_id();
		// Fix school info
		examResult.setSchool_id(school_id);
		// Check class
		if (examResult.getClass_id() == null ||examResult.getClass_id()<= 0 ){
			throw new ESchoolException("exam.class_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		com.itpro.restws.model.EClass eclass = classService.findById(examResult.getClass_id());
		if (eclass == null ){
			throw new ESchoolException("exam.class_id is not existing", HttpStatus.BAD_REQUEST);
		}
		if (eclass.getSchool_id().intValue() != school_id.intValue() ){
			throw new ESchoolException("exam.school_id is not similar with current user.school_id", HttpStatus.BAD_REQUEST);
		}
		// check student
		
		if (examResult.getStudent_id() == null ||examResult.getStudent_id()<= 0 ){
			throw new ESchoolException("exam.student_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		
		User student = userService.findById(examResult.getStudent_id());
		if (student == null ){
			throw new ESchoolException("exam.student_id is not existing", HttpStatus.BAD_REQUEST);
		}
		
		if (student.getSchool_id().intValue() != school_id.intValue() ){
			throw new ESchoolException("exam.student_id:"+ student.getId().intValue()+" is not belong to school_id:"+examResult.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		if (!student.hasRole(E_ROLE.STUDENT.getRole_short())){
			throw new ESchoolException("Exam Student is not Student role:"+examResult.getStudent_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		// Teacher only can input for Student belong to same class
		if (me.hasRole(E_ROLE.TEACHER.getRole_short())){
			if (!userService.isSameClass(me, student)){
				throw new ESchoolException("TeacherID:"+me.getId().intValue()+" and StudentID:"+examResult.getStudent_id().intValue()+" is not in same Class", HttpStatus.BAD_REQUEST);
			}
			if (!me.is_belong2class(examResult.getClass_id())){
				throw new ESchoolException("teacher_id:"+ me.getId().intValue()+" is not belong to class_id:"+examResult.getClass_id().intValue(), HttpStatus.BAD_REQUEST);
			}
			
		}
		
		if (!student.is_belong2class(examResult.getClass_id())){
			throw new ESchoolException("exam.student_id:"+ student.getId().intValue()+" is not belong to class_id:"+examResult.getClass_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamService.findBySchool(school_id);
		if (schoolExams == null || schoolExams.size() == 0){
			throw new ESchoolException("There isn't any SchoolExam defined for school_id:"+school_id.intValue(), HttpStatus.BAD_REQUEST);
		}

		
		examResult.setStudent_name(student.getSso_id());
		examResult.setStd_nickname(student.getNickname());
		examResult.setStd_photo(student.getPhoto());
		
		

		// Check Subject
		if (examResult.getSubject_id() == null  || examResult.getSubject_id().intValue() <=0){
			throw new ESchoolException("subject_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		MSubject msubject = msubjectDao.findById(examResult.getSubject_id());
		if (msubject == null ){
			throw new ESchoolException("ExamResult.subject_id is not existing:"+examResult.getSubject_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		if (msubject.getSchool_id().intValue() != school_id.intValue() ){
			throw new ESchoolException("Subject.School_ID is not similar with exam.School_ID", HttpStatus.BAD_REQUEST);
		}
		examResult.setSubject_name(msubject.getSval());
		
		// Check YEAR
		SchoolYear schooYear =null;
		if (examResult.getSch_year_id() == null   || examResult.getSch_year_id().intValue() <=0 ){
			schooYear = schoolYearService.findLatestYearBySchool(school_id);
			examResult.setSch_year_id(schooYear.getId());
		}else{
			schooYear = schoolYearService.findById(examResult.getSch_year_id());
			if (schooYear == null ){
				throw new ESchoolException("ExamResult.getSch_year_id is not existing:"+examResult.getSubject_id().intValue(), HttpStatus.BAD_REQUEST);
			}			
			if (schooYear.getSchool_id().intValue() != school_id.intValue()){
				throw new ESchoolException("exam.sch_year_id is not belong to this school: "+examResult.getSchool_id().intValue(),HttpStatus.BAD_REQUEST);
			}
			
		}
		
		// Check Exam Result ( ajust the point to x.xx format)
		java.lang.reflect.Field[] fields = examResult.getClass().getDeclaredFields();
		//SchoolExam school_exam = null;
		
        for (Field field : fields) {
            field.setAccessible(true);
            String fname =field.getName();
            
        	// Init hash
    		for (SchoolExam school_exam: schoolExams){
    			String ex_key = school_exam.getEx_key();
            //for (String key: Constant.exam_keys){
            	//if (fname.equalsIgnoreCase(key)){ // m1 ~ m20
    			if (fname.equalsIgnoreCase(ex_key)){ // m1 ~ m20
            		String sresult = null;
            		// Parse float value
            		try{
            			sresult = (String)field.get(examResult); // "8.4@2016-06-10";
            		}catch (Exception e){
            			throw new ESchoolException(fname + ": cannot get sresult, exception message: "+ e.getMessage(), HttpStatus.BAD_REQUEST);
            		}
            		if (sresult != null  && sresult.trim().length() > 0){
            			// Parsing JSON to Exam Detail
            			ExamDetail examDetail = ExamDetail.strJson2ExamDetail(sresult);
            			sresult = examDetail.getSresult();
            			
            			
                		// Parsing float 
                		String sval = sresult; // delete point
                		if (sval == null || sval.trim().length() == 0 ){
                			break;
                		}
                			              		
                		
//                		String exam_dt="";
//                		String[] arr = sresult.split("@"); // "8.4@2016-06-10";
//                		if (arr != null && arr.length >=2){
//                			sval = arr[0];
//                			exam_dt = arr[1];
//                			if (!Utils.checkDateFormat(exam_dt)){
//                				throw new ESchoolException(fname + ": invalid date time format, should be YYYY-MM-DD value: "+ exam_dt, HttpStatus.BAD_REQUEST);
//                			}
//                		}
                		String exam_dt=examDetail.getExam_dt();
                		Date dt = null;
                		if (exam_dt != null && exam_dt.trim().length() > 0){
                			dt = Utils.parsetDateAll(exam_dt);
                			if (dt == null ){
                				throw new ESchoolException(fname + ": invalid date time format, should be YYYY-MM-DD value: "+ exam_dt, HttpStatus.BAD_REQUEST);
                			}
                		}
                		
                		Float fval = Utils.parseFloat(sval);
                	
            			if ( fval.floatValue() < school_exam.getMin().floatValue() ){
            				throw new ESchoolException(fname + ": invalid Float value: "+ sval, HttpStatus.BAD_REQUEST);
            			}
            			if ( fval.floatValue() < school_exam.getMin().floatValue() ){
            				throw new ESchoolException(fname +":"+sval+ " < MIN value = "+school_exam.getMin().floatValue(), HttpStatus.BAD_REQUEST);
            			}
            			if (fval.floatValue() > school_exam.getMax().floatValue() ){
            				throw new ESchoolException(fname + ":"+sval+" > MAX value = "+school_exam.getMax().floatValue(), HttpStatus.BAD_REQUEST);
            			}
            			
            			sval = String.format("%.1f", fval);
            			//valid_exam = true;
            			try {
            				examDetail.setSresult(sval);
            				ObjectMapper mapper = new ObjectMapper();
							field.set(examResult,  mapper.writeValueAsString(examDetail));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							throw new ESchoolException("Cannot access field of examResult object:"+e.getMessage(), HttpStatus.BAD_REQUEST);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							throw new ESchoolException("Cannot access field of examResult object:"+e.getMessage(), HttpStatus.BAD_REQUEST);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
							throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
						}
            			// /////////////////////////////////////////////////////////////////////
            		}
        			break;
            	}
            }
            
        }
		// Check existing Exam for merger
		ArrayList<ExamResult> curr_list = (ArrayList<ExamResult>) examResultDao.findExamResultExt(
				examResult.getSchool_id(),
				null,
				examResult.getStudent_id(),
				examResult.getSubject_id(), 
				examResult.getSch_year_id());
		if (curr_list != null && curr_list.size() > 0){
			ExamResult existing_db = curr_list.get(0);
			  // Check update if ID existing
	        if (	examResult.getId() != null && 
	        		examResult.getId().intValue() > 0){
	        	if (existing_db.getId().intValue() != examResult.getId().intValue()){
	        		throw new ESchoolException("validInputExam(): examResult.id("+examResult.getId().intValue()+") != existing_db.id("+existing_db.getId().intValue()+") based on: school_id,student_id,subject_id,year_id", HttpStatus.BAD_REQUEST);
	        	}
	        }			
			examResult.setId(curr_list.get(0).getId());
		}else{
			examResult.setId(null);
		}
	}

	

	@Override
	public void deleteExamResult(User me, ExamResult exam) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		examResultDao.deleteExamResult(me,exam);
		
	}



	@Override
	public int countExamResultExt(Integer school_id, Integer class_id, Integer student_id, Integer subject_id,
			Integer year_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("school_id:"+(school_id==null?"null":school_id.intValue()));
		logger.info("class_id:"+(class_id==null?"null":class_id.intValue()));
		logger.info("student_id:"+(student_id==null?"null":student_id.intValue()));
		logger.info("subject_id:"+(subject_id==null?"null":subject_id.intValue()));
		logger.info("year_id:"+(year_id==null?"null":year_id.intValue()));
		
		return examResultDao.countExamResultExt(school_id, class_id, student_id, subject_id, year_id);
		
	}

	@Override
	public ArrayList<ExamResult> findExamResultExt(User me, Integer school_id, Integer class_id, Integer student_id,
			Integer subject_id, Integer year_id) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("school_id:"+(school_id==null?"null":school_id.intValue()));
		logger.info("class_id:"+(class_id==null?"null":class_id.intValue()));
		logger.info("student_id:"+(student_id==null?"null":student_id.intValue()));
		logger.info("subject_id:"+(subject_id==null?"null":subject_id.intValue()));
		logger.info("year_id:"+(year_id==null?"null":year_id.intValue()));
		
		if (me.getSchool_id().intValue() != school_id.intValue() ){
			throw new ESchoolException("findExamResultExt() ERROR: me.school_id != school_id", HttpStatus.BAD_REQUEST);
		}
		
		return examResultDao.findExamResultExt(school_id, class_id, student_id, subject_id, year_id);
	}

	@Override
	public ArrayList<ExamResult>  getUserProfile(User me, User student, Integer filter_subject_id, Integer filter_year_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("filter_subject_id:"+(filter_subject_id==null?"null":filter_subject_id.intValue()));
		logger.info("filter_year_id:"+(filter_year_id==null?"null":filter_year_id.intValue()));
		
		// lay danh sach mon hoc cua truong
		// count key: school, user, subject, year
		// Neu chua ton tai, tao new exam result
		ArrayList<ExamResult> examResults = new ArrayList<ExamResult>();
		ArrayList<MSubject> subjects = new ArrayList<MSubject>();
		
		if (me.getSchool_id().intValue() != student.getSchool_id().intValue() ){
			throw new ESchoolException("findExamResultExt() ERROR: student.school_id != school_id", HttpStatus.BAD_REQUEST);
		}
		
		SchoolYear curr_year = schoolYearService.findLatestYearBySchool(student.getSchool_id());
		// If filter_yearid = null
		// Using current year
		// Create new ExamResult if not exist
		if (filter_year_id== null || filter_year_id.intValue()<=0){
			if (curr_year == null ){
				throw new ESchoolException("Cannot get Current SchoolYear of shcool_id: "+ student.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
			}
			filter_year_id = curr_year.getId();
			// Current profile
			if (filter_subject_id != null ){
				MSubject msubject= msubjectDao.findById(filter_subject_id);
				if (msubject == null){
					throw new ESchoolException("Invalid filter_subject_id: "+ filter_subject_id.intValue(), HttpStatus.BAD_REQUEST);
				}
				if (msubject.getSchool_id().intValue() != student.getSchool_id().intValue()){
					throw new ESchoolException("filter_subject_id is not in same school with STUDENT", HttpStatus.BAD_REQUEST);
				}
				subjects.add(msubject);
			}else{
				subjects = (ArrayList<MSubject>) msubjectDao.findBySchool(student.getSchool_id(), 0, 999999);
				if (subjects == null ||subjects.size() == 0){
					throw new ESchoolException("There is not MSubjects exam setup for school_id:"+ student.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
				}
			}
			
			
			for (MSubject subject : subjects){
				ArrayList<ExamResult> list = examResultDao.findExamResultExt(student.getSchool_id(), null, student.getId(), subject.getId(), curr_year.getId());
				if (list == null || list.size() == 0){
					ExamResult ret = new_blank_exam_result(student,subject,curr_year.getId());
					examResultDao.saveExamResult(null,ret);
					examResults.add(ret);
				}else{
					examResults.addAll(list);
				}
			}
		}else{
			// If filter_yearid != null
			// Just query from DB and return			
			if (filter_year_id.intValue() > curr_year.getId().intValue()){
				throw new ESchoolException("filter_year_id:"+filter_year_id.intValue() +" > current year id: "+curr_year.getId().intValue(), HttpStatus.BAD_REQUEST);
			}
			examResults = examResultDao.findExamResultExt(student.getSchool_id(), null, student.getId(), null, filter_year_id);
		}
		// Process average before return
		proc_average(student,examResults);
		
		return examResults; 
		
	}
	private ExamResult new_blank_exam_result(User student, MSubject subject, Integer year_id){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("year_id:"+(year_id==null?"null":year_id.intValue()));
		
		
		ExamResult exam_result = new ExamResult();
		
		exam_result.setSchool_id(student.getSchool_id());
		exam_result.setStudent_id(student.getId());
		exam_result.setStudent_name(student.getSso_id());
		exam_result.setSubject_id(subject.getId());
		exam_result.setSubject_name(subject.getSval());
		exam_result.setSch_year_id(year_id);
		return exam_result;
		
		
	}

	@Override
	public ArrayList<ExamResult> getClassProfile(User me,
			Integer school_id, 
			Integer filter_class_id, 
			Integer filter_student_id, 
			Integer subject_id, 
			Integer year_id) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("school_id:"+(school_id==null?"null":school_id.intValue()));
		logger.info("filter_class_id:"+(filter_class_id==null?"null":filter_class_id.intValue()));
		logger.info("filter_student_id:"+(filter_student_id==null?"null":filter_student_id.intValue()));
		logger.info("subject_id:"+(subject_id==null?"null":subject_id.intValue()));
		logger.info("year_id:"+(year_id==null?"null":year_id.intValue()));
		
		
		
		EClass filter_eclass = null;
		User filter_student = null;
		
		ArrayList<User> filter_users = new ArrayList<User>();
		// Check filter user
		if (filter_student_id != null  && filter_student_id.intValue() > 0){
			filter_student = userService.findById(filter_student_id);
			if (filter_student == null){
				throw new ESchoolException("filter_student_id is not existing", HttpStatus.BAD_REQUEST);
			}
			if (!filter_student.hasRole(E_ROLE.STUDENT.getRole_short())){
				throw new ESchoolException("filter_student_id is not STUDENT ROLE", HttpStatus.BAD_REQUEST);
			}
			if (me.getSchool_id().intValue() != filter_student.getSchool_id().intValue() ){
				throw new ESchoolException("getClassProfile() ERROR: me.school_id != filter_student.school_id", HttpStatus.BAD_REQUEST);
			}
			
			filter_users.add(filter_student);
		}
		// check class
		else if (filter_class_id != null && filter_class_id.intValue() > 0){
			filter_eclass = classService.findById(filter_class_id);
			if (filter_eclass == null){
				throw new ESchoolException(" filter_class_id is not existing: "+filter_class_id.intValue(),HttpStatus.BAD_REQUEST);
			}
			if (me.getSchool_id().intValue() != filter_eclass.getSchool_id().intValue() ){
				throw new ESchoolException("getClassProfile() ERROR: me.school_id != filter_eclass.school_id", HttpStatus.BAD_REQUEST);
			}
			
			Set<User> tmpUsers = filter_eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
			if (tmpUsers == null || tmpUsers.size() <= 0){
				throw new ESchoolException(" filter_class_id has not STUDENT: "+filter_class_id.intValue(),HttpStatus.BAD_REQUEST);
			}
			for (User e: tmpUsers){
				filter_users.add(e);
			}
		}else{
			throw new ESchoolException("Must input filter_class_id OR filter_student_id ",HttpStatus.BAD_REQUEST);
		}
		ArrayList<ExamResult> examResults = new ArrayList<ExamResult>();
		if (filter_users != null && filter_users.size() > 0){
			for (User student: filter_users){
				ArrayList<ExamResult> list = getUserProfile(me,student, subject_id, year_id);
				if (list != null && list.size() > 0){
					examResults.addAll(list);
				}
			}
		}
		return examResults;
	}

	
	private void proc_average( User student, ArrayList<ExamResult> examResults){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		
		if (examResults == null ){
			return;
		}
		ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamService.findBySchool(student.getSchool_id());
		if (schoolExams == null || schoolExams.size() == 0){
			throw new ESchoolException("There isn't any SchoolExam defined for school_id:"+student.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
		}

		
		for (ExamResult examResult : examResults){
			// To calculate average
			ArrayList<Float> point_months1 = new ArrayList<Float>();
			ArrayList<Float> point_months2 = new ArrayList<Float>();
			ArrayList<Float> point_terms1 = new ArrayList<Float>();
			ArrayList<Float> point_terms2 = new ArrayList<Float>();
			ArrayList<Float> point_years = new ArrayList<Float>();

			// Find m1,m2,m3...m20 available
			java.lang.reflect.Field[] fields = examResult.getClass().getDeclaredFields();
			//SchoolExam school_exam = null;
			
	        for (Field field : fields) {
	            field.setAccessible(true);
	            String fname =field.getName();
	            
	            //for (String key: Constant.exam_keys){
	        	for (SchoolExam school_exam: schoolExams){
	    			String ex_key = school_exam.getEx_key();	            
	            	//if (fname.equalsIgnoreCase(key)){ // m1 ~ m20
	    			if (fname.equalsIgnoreCase(ex_key)){ // m1 ~ m20
	            		String sresult = null;
	            		// Parse score value
	            		try{
	            			sresult = (String)field.get(examResult); // "8.4@2016-06-10";
	            		}catch (Exception e){
	            			throw new ESchoolException(fname + ": cannot get sresult, exception message: "+ e.getMessage(), HttpStatus.BAD_REQUEST);
	            		}
	            		if (sresult != null && sresult.trim().length() > 0){
	            			// Parsing JSON to Exam Detail
	            			ExamDetail examDetail = ExamDetail.strJson2ExamDetail(sresult);
	            			sresult = examDetail.getSresult();
	            			
	                		// Get Exam master information
//	                		school_exam = schoolExamDao.findByExKey(examResult.getSchool_id(), fname);// school_id vs m1,m2,m3...m20
//	                		if (school_exam == null ){
//	                        	throw new ESchoolException("there is no master data declared for ex_key:"+fname, HttpStatus.BAD_REQUEST);
//	                		}
	                		// Parsing float 
	                		String sval = sresult;
	                		String exam_dt="";
	                		String[] arr = sresult.split("@"); // "8.4@2016-06-10";
	                		if (arr != null && arr.length >=2){
	                			sval = arr[0];
	                			exam_dt = arr[1];
	                			if (!Utils.checkDateFormat(exam_dt)){
	                				throw new ESchoolException(fname + ": invalid date time format, should be YYYY-MM-DD value: "+ exam_dt, HttpStatus.BAD_REQUEST);
	                			}
	                		}
	                		if (sval == null || sval.trim().length() == 0){
	                			break;
	                		}
	                		Float fval = Utils.parseFloat(sval);
	                		if (fval == null){
	                			throw new ESchoolException(fname + ": invalid Float value: "+ sval, HttpStatus.BAD_REQUEST);
	                		}
	                		if ( fval.floatValue() < school_exam.getMin().floatValue() ){
	            				throw new ESchoolException(fname +":"+sval+ " < MIN value = "+school_exam.getMin().floatValue(), HttpStatus.BAD_REQUEST);
	            			}
	            			if (fval.floatValue() > school_exam.getMax().floatValue() ){
	            				throw new ESchoolException(fname + ":"+sval+" > MAX value = "+school_exam.getMax().floatValue(), HttpStatus.BAD_REQUEST);
	            			}
	            			
	            			// /////////////////////////////////////////////////////////////////////
	            			// Save to calculate average
	            			if ((school_exam.getEx_type().intValue() == 1) &&// month point
	            					(school_exam.getTerm_val().intValue() == 1)) // Hoc ky 1
	            			{
	            				point_months1.add(fval);
	            			}
	            			else if ((school_exam.getEx_type().intValue() == 1) &&// month point
	            					(school_exam.getTerm_val().intValue() == 2)) // Hoc ky 2
	            			{
	            				point_months2.add(fval);
	            			}
	            			else if ((school_exam.getEx_type().intValue() == 2) &&// Thi Hoc Ky
	            					(school_exam.getTerm_val().intValue() == 1)) // Hoc ky 1
	            			{
	            				point_terms1.add(fval);
	            			}else if ((school_exam.getEx_type().intValue() == 2) &&// Thi Hoc Ky
	            					(school_exam.getTerm_val().intValue() == 2)) // Hoc ky 2
	            			{
	            				point_terms2.add(fval);
	            			}
	            			// /////////////////////////////////////////////////////////////////////

	            		}
	            		break;// STOP to next field
	            	}
	            }
	        }
	        
	        /// Start calculate average
			////////Calculate M5 ( Trung binh 4 thang  HK 1)
			float total =0;
			int cnt =0;
			ExamDetail ex_detail = null;
			ObjectMapper mapper = new ObjectMapper();;
			if (point_months1.size() > 0){
				total =0;
				cnt =0;
				for (Float fval: point_months1){
					total += fval.floatValue();
					cnt++;
				}
				float m5 = total/cnt;
				// examResult.setM5(String.format("%.1f", m5));
				ex_detail = new ExamDetail();
				ex_detail.setExam_dt(Utils.now());
				ex_detail.setNotice("AUTO");
				ex_detail.setSresult(String.format(Constant.POINT_FORMAT, m5));
				//mapper = new ObjectMapper();
				try {
					examResult.setM5(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
				///
				// Calculate M7 (Trung Binh Hoc Ky 1)
				total =0;
				cnt =0;		
				if (point_terms1.size() > 0 ){  // Neu co diem thi HK1
					point_terms1.add(m5);
					for (Float fval: point_terms1){
						total += fval.floatValue();
						cnt++;
					}
					float m7 = total/cnt;
					// examResult.setM7(String.format("%.1f", m7));
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult(String.format(Constant.POINT_FORMAT, m7));
					//mapper = new ObjectMapper();
					try {
						examResult.setM7(mapper.writeValueAsString(ex_detail));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					}
					// Add to calculate Ave of year
					point_years.add(m7);
				}
			}else{
				// Reset MONTH AVE
				try {
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult("");
					examResult.setM5(mapper.writeValueAsString(ex_detail));
					examResult.setM7(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
			}
			// Calculate M12 ( Trung binh 4 thang HK2 )
			total =0;
			cnt =0;		
			if (point_months2.size() > 0){
				for (Float fval: point_months2){
					total += fval.floatValue();
					cnt++;
				}
				float m12 = total/cnt;
				// examResult.setM12(String.format("%.1f", m12));
				ex_detail = new ExamDetail();
				ex_detail.setExam_dt(Utils.now());
				ex_detail.setNotice("AUTO");
				ex_detail.setSresult(String.format(Constant.POINT_FORMAT, m12));
				mapper = new ObjectMapper();
				try {
					examResult.setM12(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
				
				// Calculate M14 (Trung Binh HK 2)
				total =0;
				cnt =0;		
				if (point_terms2.size() > 0 ){ // Neu co diem thi HK2
					point_terms2.add(m12);
					for (Float fval: point_terms2){
						total += fval.floatValue();
						cnt++;
					}
					float m14 = total/cnt;
					// examResult.setM14(String.format("%.1f", m14));
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult(String.format(Constant.POINT_FORMAT, m14));
					mapper = new ObjectMapper();
					try {
						examResult.setM14(mapper.writeValueAsString(ex_detail));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					}
					// Add to calculate Ave of year								
					point_years.add(m14);
				}
			}else{
				// Reset month HK 2
				try {
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult("");
					examResult.setM12(mapper.writeValueAsString(ex_detail));
					examResult.setM14(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
			}
			
			// Calculate M15 ( Trung binh ca nam )
			total =0;
			cnt =0;		
			if (point_years.size() > 0){
				for (Float fval: point_years){
					total += fval.floatValue();
					cnt++;
				}
				float m15 = total/cnt;
				// examResult.setM15(String.format("%.1f", m15));
				ex_detail = new ExamDetail();
				ex_detail.setExam_dt(Utils.now());
				ex_detail.setNotice("AUTO");
				ex_detail.setSresult(String.format(Constant.POINT_FORMAT, m15));
				mapper = new ObjectMapper();
				try {
					examResult.setM15(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}			
			}else{
				// Reset month HK 2
				try {
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult("");
					examResult.setM15(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
			}
		}
	}

	

	private String getRade(ArrayList<Float> marks){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		float A_GRADE = 7;
		float B_BRADE = 6;
		int a_cnt =0;
		int b_cnt =0;
		if (marks != null && marks.size() > 0){
			for (Float mark: marks){
		   		   if (mark != null && mark.floatValue() >= A_GRADE){
		   			   a_cnt +=1;
		   		   }else if (mark != null && mark.floatValue() >= B_BRADE){
		   			   b_cnt +=1;
		   		   }
		   	   }
			if (a_cnt >0 && a_cnt == marks.size() ){
				return "A";
			}else if (b_cnt >0 && ((a_cnt +b_cnt) == marks.size()) ){
				return "B";
			}
		}
		return "";
		
		 
	}
		
		
	private String getAveMarks(ArrayList<Float> marks) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		
		float total = 0;
		String sval=null;
		int cnt = 0;
		if (marks != null && marks.size() > 0) {
			for (Float mark : marks) {
				if (mark != null && mark.floatValue() >= 0) {
					cnt += 1;
					total += mark.floatValue();
				}
			}
			if (cnt > 0) {
				sval = String.format(Constant.POINT_FORMAT, total / cnt);
				
			}
		}
		return sval;
	}

	/***
	 * getUserRank thi quan trong la year_id
	 * class_id: Optional
	 */
	@Override
	public ArrayList<ExamRank> getUserRank(User me, User student, Integer class_id, Integer year_id) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("class_id:"+(class_id==null?"null":class_id.intValue()));
		logger.info("year_id:"+(year_id==null?"null":year_id.intValue()));
		
		
		if (me.hasRole(E_ROLE.STUDENT.getRole_short())){
			if (me.getId().intValue() != student.getId().intValue()){
				throw new ESchoolException("STUDENT cannot see rank of other STUDENT ", HttpStatus.BAD_REQUEST);
			}
			
		}else if (me.hasRole(E_ROLE.TEACHER.getRole_short())){
			if (!userService.isSameClass(me, student)){
				throw new ESchoolException("TEACHER_ID:"+me.getId().intValue()+" and STUDENT_ID:"+student.getId().intValue()+" is not in same Class ", HttpStatus.BAD_REQUEST);
			}
		}
		
		
		if (year_id == null ){
			SchoolYear schoolYear = schoolYearService.findLatestYearBySchool(student.getSchool_id());
			if (schoolYear == null ){
				throw new ESchoolException("Cannot get schoolYear of school_id:"+student.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
			}
			year_id = schoolYear.getId();
		}
		if (class_id == null ||class_id.intValue() == 0 ){
			class_id = null;
		}
		
		if (class_id!= null && !student.is_belong2class(class_id)){
			throw new ESchoolException("StudentID:"+student.getId().intValue()+" is not belong to class_id:"+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		return examRankDao.findExamRankExt(student.getSchool_id(), class_id, student.getId(), year_id);
	}

	/***
	 * getClassRank:
	 * class_id: required
	 * year_id:optional
	 */
	@Override
	public ArrayList<ExamRank> getClassRank(User me, Integer class_id, Integer year_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("class_id:"+(class_id==null?"null":class_id.intValue()));
		logger.info("year_id:"+(year_id==null?"null":year_id.intValue()));
		
		
		if (class_id == null || class_id.intValue() ==0) {
			throw new ESchoolException("class_id is NULL", HttpStatus.BAD_REQUEST);
		}
		
		 ArrayList<ExamRank> ret = new ArrayList<ExamRank>();
		EClass eclass = classService.findById(class_id);
		if (eclass == null) {
			throw new ESchoolException("class_id is not existing: "+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (eclass.getSchool_id().intValue() != me.getSchool_id().intValue()) {
			throw new ESchoolException("class.school_id != me.school_id", HttpStatus.BAD_REQUEST);
		}
		Set<User> users = eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
		if (users== null || users.size() <= 0){
			throw new ESchoolException("class_id is dont have any users: "+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		
		for (User user: users){
			ArrayList<ExamRank> ranks = getUserRank(me, user, class_id, year_id);
			if (ranks != null && ranks.size() > 0){
				ret.addAll(ranks);
			}
		}
		return ret;
	}


	@Override
	public ArrayList<ExamRank> execClassMonthAve(User me,  Integer filter_class_id, String filter_ex_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("filter_class_id:"+(filter_class_id==null?"null":filter_class_id.intValue()));
		logger.info("filter_ex_key"+(filter_ex_key==null?"null":filter_ex_key));
		
		ArrayList<ExamRank> ret = new ArrayList<ExamRank>();
		if (filter_class_id == null){
			throw new ESchoolException("filter_class_id is NULL", HttpStatus.BAD_REQUEST);
		} 
		 
		EClass eclass = classService.findById(filter_class_id);
		if (eclass == null){
			throw new ESchoolException("filter_class_id is not existing"+filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (eclass.getYear_id() == null){
			throw new ESchoolException("filter_class_id:"+filter_class_id.intValue()+"  has year_id = NULL", HttpStatus.BAD_REQUEST);
		}
		
		if (me.getSchool_id().intValue() != eclass.getSchool_id().intValue()){
			throw new ESchoolException("Class.school_id != current_user.school_id", HttpStatus.BAD_REQUEST);
		}
		
		
		Set<User> users = eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
		if (users == null || users.size() <= 0){
			throw new ESchoolException("filter_class_id has not any user"+filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		// Check available exam result
		String error = "";
		int err_cnt = 0;
		for (User user: users){
			int cnt = countExamResultExt(user.getSchool_id(), null, user.getId(), null, eclass.getYear_id());
			if (cnt <= 0){
				err_cnt++;
				error +=user.getId().intValue()+",";
			}
		}
		if (err_cnt > 0){
			throw new ESchoolException("Cannot execute ranking, there are ["+err_cnt+"] users in class has not any exam result, user_id list:"+error, HttpStatus.BAD_REQUEST);
		}
		// Execute average
		for (User user: users){
			ExamRank exam_rank = execUserMonthAve(me,user.getId(),eclass.getYear_id(),filter_class_id,filter_ex_key);
			if (exam_rank != null && exam_rank.getId() != null && exam_rank.getId().intValue() > 0){
				ret.add(exam_rank);
			}
		}
		return ret;
	}
@Override
	public ExamRank execUserMonthAve(User me, Integer user_id, Integer filter_year_id, Integer filter_class_id,String filter_ex_key) {	
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("user_id:"+(user_id==null?"null":user_id.intValue()));
		logger.info("filter_year_id"+(filter_year_id==null?"null":filter_year_id.intValue()));
		logger.info("filter_class_id"+(filter_class_id==null?"null":filter_class_id.intValue()));
		logger.info("filter_ex_key"+(filter_ex_key==null?"null":filter_ex_key));
	
	
		Integer school_id = me.getSchool_id();
		
		Hashtable<String,RankInfo> hashtable = new Hashtable<String,RankInfo>();
		// Valid student
		if (user_id == null || user_id.intValue()==0 ){
			throw new ESchoolException("user_id is required", HttpStatus.BAD_REQUEST);
		}
		
		User student = userService.findById(user_id);
		if (student == null ){
			throw new ESchoolException("user_id is not existing" + user_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		
		if (student.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("user_id is not belong to same school with current user" + user_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (!student.hasRole(E_ROLE.STUDENT.getRole_short()) ){
			throw new ESchoolException("user_id is not STUDENT" + user_id.intValue(), HttpStatus.BAD_REQUEST);		
			
			
		}
		// Valid class
		
		if (filter_class_id == null || filter_class_id.intValue()==0 ){
			throw new ESchoolException("filter_class_id required", HttpStatus.BAD_REQUEST);
		}
		EClass eclass = classService.findById(filter_class_id);
		if (eclass == null ){
			throw new ESchoolException("filter_class_id is not existing" + filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (me.getSchool_id().intValue() != eclass.getSchool_id().intValue()){
			throw new ESchoolException("eclass.school_id != me.school_id", HttpStatus.BAD_REQUEST);
		}
		if (!student.is_belong2class(filter_class_id)){
			throw new ESchoolException("user_id is not belong to class_id" + filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		
		// Vali year
		if (filter_year_id == null || filter_year_id.intValue()==0 ){
			filter_year_id = eclass.getYear_id();
		}else if (eclass.getYear_id().intValue() != filter_year_id.intValue() ){
			throw new ESchoolException("filter_class_id.year_id:"+eclass.getYear_id().intValue()+" is not same with requested year_id:" + filter_year_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		
		
		// Get existing exam result
		ArrayList<ExamResult> examResults = findExamResultExt(me,student.getSchool_id(), null, user_id, null, filter_year_id);
		if (examResults == null ){
			throw new ESchoolException("There isn't any exam result for user_id:"+user_id.intValue()+" and year_id:"+filter_year_id.intValue(), HttpStatus.BAD_REQUEST);
		}

		// Initial ranking and average values  by MONTH
		ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamService.findBySchool(school_id);
		if (schoolExams == null || schoolExams.size() == 0){
			return null;
		}
		for (SchoolExam schoolExam: schoolExams){
			String ex_key = schoolExam.getEx_key(); // M1, M2... M20
			if (filter_ex_key != null && filter_ex_key.length() > 0){
				if  (!filter_ex_key.equalsIgnoreCase(schoolExam.getEx_key())){
					ex_key = null;
				}
			}
			
			if (ex_key != null ){
				RankInfo rankInfo = new RankInfo();
	
				/////////
				rankInfo.setAllocation(null);
				rankInfo.setAve(null);
				rankInfo.setGrade(null);
				rankInfo.setExam_rank_id(null);
				rankInfo.setMarks(new ArrayList<Float>());			
				rankInfo.setUser_id(user_id);
				rankInfo.setYear_id(filter_year_id);
				rankInfo.setEx_key(ex_key);			
				// put to hash
				hashtable.put(ex_key, rankInfo);
			}
		}
		
		 // Get or initial exam_rank record for student by year 
		ArrayList<ExamRank> exam_ranks  = examRankDao.findExamRankExt(school_id, null, student.getId(), filter_year_id);
        ExamRank exam_rank = null;
        if (exam_ranks == null ||exam_ranks.size() <=0){
        	exam_rank = new ExamRank();
        	
        	exam_rank.setSchool_id(student.getSchool_id());
        	exam_rank.setClass_id(filter_class_id);
        	exam_rank.setStudent_id(user_id);
        	exam_rank.setSch_year_id(filter_year_id);
        	
        }else{
        	exam_rank = exam_ranks.get(0);
        }
        // Scan all exam results by ex_key
        Enumeration<String> keys = hashtable.keys();
        
        while(keys.hasMoreElements()) {
           String ex_key = (String) keys.nextElement();
           RankInfo rankInfo = hashtable.get(ex_key);
           ArrayList<Float> marks = getMarkByExKeys(student,ex_key,examResults);
           
           rankInfo.setMarks(marks);
           rankInfo.setAve(getAveMarks(marks));
           rankInfo.setGrade(getRade(marks));

           
           // Save rankInfo to field having name of ex_key
           ObjectMapper mapper = new ObjectMapper();
           java.lang.reflect.Field[] fields = exam_rank.getClass().getDeclaredFields();
	   		for (Field field : fields) {
	   			field.setAccessible(true);
	            String fname =field.getName();
	            if (fname.equalsIgnoreCase(ex_key)){
	            	try {
						field.set(exam_rank,  mapper.writeValueAsString(rankInfo));
					 } catch (IllegalArgumentException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot access field of rankInfo object:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot access field of rankInfo object:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot cast rankInfo object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					}
	            	break;
	            }
	            
	   		}
           
           
        }
      // save result to db
		if (exam_rank != null ){
			examRankDao.saveExamRank(me,exam_rank);
		}
		return exam_rank;
	}
	

	
	private ArrayList<Float> getMarkByExKeys(User user, String ex_key, ArrayList<ExamResult> examResults){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("ex_key"+(ex_key==null?"null":ex_key));
		
		ArrayList<Float> marks = new ArrayList<>();
		
		
		for (ExamResult examResult: examResults){
			if (examResult.getStudent_id().intValue() != user.getId().intValue()){
				continue;
			}
			java.lang.reflect.Field[] fields = examResult.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
	            String fname =field.getName();
	            if (fname.equalsIgnoreCase(ex_key)){
	            	String sresult = null;
            		// Parse float value
            		try{
            			sresult = (String)field.get(examResult);// {"sresult":"4.5","notice":"test","exam_dt":"2016-07-05 15:36:07"}
            		}catch (Exception e){
            			throw new ESchoolException(fname + ": cannot get sresult, exception message: "+ e.getMessage(), HttpStatus.BAD_REQUEST);
            		}
            		if (sresult != null && sresult.trim().length() > 0){
            			// Parsing JSON to Exam Detail
            			ExamDetail examDetail = ExamDetail.strJson2ExamDetail(sresult);
                		Float fval = Utils.parseFloat(examDetail.getSresult());
                		if (fval != null && fval >=0){
                			marks.add(fval);
                		}
            		
            		}
	            	break;
	            }
			}
		}
		
		
		
		return marks;
	}
	/***
	 * Input: danh sach ExamRank ( da co diem trung binh theo tung thang)
	 * Output: danh sach ExamRank update thong tin allocation 
	 * Xu ly: 
	 * 1. Loop ex_key m1...m20 
	 * 1.2.    Parse ExamRank(String) RankInfo Object {"ave":"4,5","grade":"A","allocation":1}
	 * 1.3     Push RankInfo vao ArrayList list = hashtables.get(ex_key)
	 * 2. Loop ex_key m1...m20
	 * 2.1     Get ArrayList list =  hashtables.get(ex_key)
	 * 2.2     Sort list theo ave
	 * 2.3     Update allocation theo order cua list
	 * 2.4     Save ExamResult to DB 
	 */
	@Override
	public ArrayList<ExamRank> procAllocation(User me,ArrayList<ExamRank> exam_ranks){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		if (exam_ranks == null || exam_ranks.size() == 0){
			return null;
		}
		Integer school_id = me.getSchool_id();
		// Initial hash table
		Hashtable<String, ArrayList<RankInfo>> hashtables= new Hashtable<String, ArrayList<RankInfo>>();
		
		 // List exam : m1 .. m20
		 ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamService.findBySchool(school_id);
		 for (SchoolExam schoolExam :schoolExams){
			 String ex_key = schoolExam.getEx_key();
			 if (ex_key == null || ex_key.equals("")){
				 continue;
			 }
			 ex_key = ex_key.toLowerCase();
			 // Put to hash table
			 hashtables.put(ex_key,  new ArrayList<RankInfo>());
		 }
				
				
		// Put DB to hashtable
		for (ExamRank examRank : exam_ranks){
			
			if (examRank.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("examResult.school_id != me.school_id", HttpStatus.BAD_REQUEST);
			}
			
			java.lang.reflect.Field[] fields = examRank.getClass().getDeclaredFields();
	        for (Field field : fields) {
	            field.setAccessible(true);
	            String fname =field.getName();
	            String sresult= "";
	            ArrayList<RankInfo> arr_rankinfo = hashtables.get(fname.toLowerCase());// m1...m20
	            if (arr_rankinfo != null ){
					try {
						sresult = (String) field.get(examRank);//{"ave":"4,5","grade":"A","allocation":1}
					} catch (IllegalArgumentException e) {
						
						e.printStackTrace();
						throw new ESchoolException(fname + ": cannot GET to field.get(examRank) :"+ e.getMessage(), HttpStatus.BAD_REQUEST);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						throw new ESchoolException(fname + ": cannot GET to field.get(examRank) :"+ e.getMessage(), HttpStatus.BAD_REQUEST);
					}
	            	if (sresult != null && !sresult.equals("")){
	            		ExamRankDetail rank_detail = ExamRankDetail.strJson2ExamDetail(sresult);//{"ave":"4,5","grade":"A","allocation":1}
	            		if (rank_detail != null && rank_detail.getAve() != null){
	            			RankInfo rankinfo = new RankInfo();
	            			
	            			// Detail info from JSON String
	            			rankinfo.setAllocation(rank_detail.getAllocation());
	            			rankinfo.setAve(rank_detail.getAve());
	            			rankinfo.setGrade(rank_detail.getGrade());
	            			/// other info - may be used
	            			rankinfo.setExam_rank_id(examRank.getId());
	            			rankinfo.setEx_key(fname);
	            			rankinfo.setUser_id(examRank.getStudent_id());
	            			rankinfo.setYear_id(examRank.getSch_year_id());
	            			// add to array to sort
	            			arr_rankinfo.add(rankinfo);
	            		}
	            	}
	            	
	            }
	        }
		}
				
		// Scan hashtable => arraylist => sort
        Enumeration<String> names = hashtables.keys();
        while(names.hasMoreElements()) {
           String ex_key = (String) names.nextElement();
           ArrayList<RankInfo> arr_infos = hashtables.get(ex_key);
           if (arr_infos != null && arr_infos.size() > 0){
        	   if (arr_infos.size() > 1){
        		   for (int i =0;i< arr_infos.size() -1;i++){
            		   float ave_i = arr_infos.get(i).getAve() != null?Float.valueOf(arr_infos.get(i).getAve()):0;
            			  // scan for max
            		   for (int j=i+1;j < arr_infos.size();j++){
            			   float ave_j = arr_infos.get(j).getAve() != null?Float.valueOf(arr_infos.get(j).getAve()):0;   
                		   // found max
                		   if (ave_i < ave_j ){
                			   // Doi cho
                			   RankInfo min = arr_infos.get(i);
                			   RankInfo max = arr_infos.get(j);
                			   arr_infos.set(i, max);
                			   arr_infos.set(j, min);
                		   }
            		   }
            		  
            	    }
        	   	  }
        	   }
           }
		           
		 
        ArrayList<ExamRank> ret_list = new ArrayList<ExamRank>();
		           
        // Update allocation
        names = hashtables.keys(); // m1,m2..
           while(names.hasMoreElements()) {
        	   String ex_key = (String) names.nextElement();
        	   ArrayList<RankInfo> arr_infos = hashtables.get(ex_key);
              if (arr_infos != null && arr_infos.size() > 0){
           	   for (int i =0;i< arr_infos.size();i++){
           		   
           		   RankInfo rank_info = arr_infos.get(i);  // Subject: {"ave":"4,5","grade":"A","allocation":1 - and oher infor}
           		   
           		   RankInfo prev_info = null;
           		   if (i> 0){
           			prev_info = arr_infos.get(i-1);
           		   }

           		   // Reload Exam Rank
           		   ExamRank examRank = examRankDao.findById(rank_info.getExam_rank_id());
           		   
           		   if (examRank != null ){
           			   // New allocation 
           			   rank_info.setAllocation(i+1);
	           			if (prev_info != null ){
	        				   if (		rank_info.getAve() != null && 
	        						   	rank_info.getAve().trim().length() > 0 &&
	        						   	// Compare with previous
	        						   	prev_info != null  && 
	        						   	prev_info.getAve() != null && 
	        						   	prev_info.getAve().trim().length() > 0)
	        				   {
	        					   // Same allocation
	        					   if (Float.valueOf(rank_info.getAve()).floatValue() == Float.valueOf(prev_info.getAve()).floatValue()){
	        						   rank_info.setAllocation(prev_info.getAllocation());   
	        					   }
	        				   }
	        			   }
           			
           			   // Create ExamRankDetail
               		   ExamRankDetail rank_detail = new ExamRankDetail();
               		   
               		   rank_detail.setAve(rank_info.getAve());
               		   rank_detail.setGrade(rank_info.getGrade());
               		   rank_detail.setAllocation(rank_info.getAllocation());
               		   
               		   ObjectMapper mapper = new ObjectMapper();
               		   String jsonstr= null; 
               		   try {
               			   jsonstr =  mapper.writeValueAsString(rank_detail); //JSON STRING:  {"ave":"4,5","grade":"A","allocation":1}
               		   } catch (JsonProcessingException e) {
               			   e.printStackTrace();
               			   throw new ESchoolException("Cannot convert Object (rank_detail) to JSON STRING:"+e.getMessage(), HttpStatus.BAD_REQUEST);
               		   }
               		   
               		   // Save JSON to String and store into ExamRank           		   
               		   if (jsonstr != null && jsonstr.length() > 0){
               			java.lang.reflect.Field[] fields = examRank.getClass().getDeclaredFields();
	                    for (Field field : fields) {
	                        field.setAccessible(true);
	                        String fname =field.getName();
	                        	if (fname.equalsIgnoreCase(ex_key)){ // m1 ~ m20
	                        		try {
										field.set(examRank, jsonstr);
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
										throw new ESchoolException("Cannot update filed(examRank,jsonstr):"+e.getMessage(), HttpStatus.BAD_REQUEST);
									} catch (IllegalAccessException e) {
										e.printStackTrace();
										throw new ESchoolException("Cannot update filed(examRank,jsonstr):"+e.getMessage(), HttpStatus.BAD_REQUEST);
									}
	                        		break;
	                        	}
	                        }
               		   	}
               		   // Update to DB
               		   examRankDao.updateExamRank(me,examRank);
               		   ret_list.add(examRank);
           		   }
           	   }
           }
        }
        return ret_list;
	}

	@Override
	public void orderExamResultByID(ArrayList<ExamResult> list, int order) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		if (list == null || list.size() <= 1){
			return;
		}
		if (order == 0){// ASC
			for (int i = 0; i< list.size()-1;i++){
				for (int j = i+1;j< list.size(); j++){
					if (list.get(i).getId().intValue() > list.get(j).getId().intValue()){
						ExamResult tmp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, tmp);
					}
				}
			}
		}else if  (order == 1){// DESC
			for (int i = 0; i< list.size()-1;i++){
				for (int j = i+1;j< list.size(); j++){
					if (list.get(i).getId().intValue() < list.get(j).getId().intValue()){
						ExamResult tmp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, tmp);
					}
				}
			}
		}
		
	}

	@Override
	public void orderRankByID(ArrayList<ExamRank> list, int order) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		if (list == null || list.size() <= 1){
			return;
		}
		if (order == 0){// ASC
			for (int i = 0; i< list.size()-1;i++){
				for (int j = i+1;j< list.size(); j++){
					if (list.get(i).getId().intValue() > list.get(j).getId().intValue()){
						ExamRank tmp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, tmp);
					}
				}
			}
		}else if  (order == 1){// DESC
			for (int i = 0; i< list.size()-1;i++){
				for (int j = i+1;j< list.size(); j++){
					if (list.get(i).getId().intValue() < list.get(j).getId().intValue()){
						ExamRank tmp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, tmp);
					}
				}
			}
		}
		
	}
	
	@Override
	public void orderRankByAllocation(User me, ArrayList<ExamRank> list, String ex_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		if (list == null || list.size() <= 1){
			return;
		}
		if (ex_key == null || ex_key.trim().length()==0){
			// throw new ESchoolException("ex_key is NULL", HttpStatus.BAD_REQUEST);
			return;
		}
		if (!schoolExamService.valid_ex_key(me, ex_key)){
			// throw new ESchoolException("ex_key is not existing:"+ex_key, HttpStatus.BAD_REQUEST);
			return;
		}
		for (int i = 0; i< list.size()-1;i++){
			String sresult1 = null;
			ExamRankDetail rank_detail_1 = null;
			ExamRank rank1 = list.get(i);
			if (rank1 != null ){
				sresult1 = rank1.getByExKey(ex_key);
				if (sresult1 != null && sresult1.trim().length() > 0 ){
					rank_detail_1 = ExamRankDetail.strJson2ExamDetail(sresult1);
				}
			}
			 
					
			for (int j = i+1;j< list.size(); j++){
				String sresult2 = null;
				ExamRankDetail rank_detail_2 = null;
				
				ExamRank rank2 = list.get(j);
				if (rank2 != null ){
					sresult2 = rank2.getByExKey(ex_key);
				}
				
				if ( sresult2 != null &&  sresult2.trim().length() > 0){
					rank_detail_2 = ExamRankDetail.strJson2ExamDetail(sresult2);
				}
				
				if (rank_detail_1 != null && rank_detail_2 != null ){
					Integer allo1 = rank_detail_1.getAllocation();
					Integer allo2 = rank_detail_2.getAllocation();
					if (allo1 != null && allo1.intValue() > 0 && allo2 != null && allo2.intValue() > 0){
						if (allo1.intValue() > allo2.intValue()){
							ExamRank tmp = list.get(i);
							list.set(i, list.get(j));
							list.set(j, tmp);			
						}else if (allo1.intValue() == allo2.intValue()){
							if (list.get(i).getId().intValue() > list.get(j).getId().intValue()){
								ExamRank tmp = list.get(i);
								list.set(i, list.get(j));
								list.set(j, tmp);
							}
						}
					}
				}
			}
		}
		
		
	}

	@Override
	public String valid_rank_process(User me,  String class_ids, String ex_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		String err_msg = "";
		if (class_ids == null || class_ids.trim().length()==0){
			throw new ESchoolException("class_ids is NULL", HttpStatus.BAD_REQUEST);
		}
		

		if (ex_key == null || ex_key.trim().length()==0){
			throw new ESchoolException("ex_key is NULL", HttpStatus.BAD_REQUEST);
		}
		if (!schoolExamService.valid_ex_key(me, ex_key)){
			throw new ESchoolException("ex_key is not existing:"+ex_key, HttpStatus.BAD_REQUEST);
		}
		
		String[] classes = class_ids.split(",");
		if (classes== null || classes.length <= 0){
			throw new ESchoolException("classes = class_ids.split(,) is BLANK", HttpStatus.BAD_REQUEST);
		}
		
		
		// Check each class
		
		for (String str_id : classes){
			Integer id = Utils.parseInteger(str_id);
			if (id == null ){
				throw new ESchoolException("Cannot parse class_id, plz input list of class_id separated by comma!"+class_ids, HttpStatus.BAD_REQUEST);
			}
			EClass eclass = classService.findById(id);
			if (eclass == null ){
				throw new ESchoolException("eclass is NULL,class_id not exist:"+id.intValue(), HttpStatus.BAD_REQUEST);
			}
			if(eclass.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("eclass.school_id = "+eclass.getSchool_id().intValue()+" is not same with me.school_id:"+me.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
			}
			// Check exam result of each class
			Set<User> users = eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
			if (users == null || users.size() <= 0){
				throw new ESchoolException("class_id: "+eclass.getId().intValue()+", class title: "+(eclass.getTitle()==null?"":eclass.getTitle())+ "  is empty (no users), please remove from class_ids first !", HttpStatus.BAD_REQUEST);
			}
			
			
			// Check each user in class
			for (User user: users){
				boolean is_error = false;
				// Count exam of a user
				ArrayList <ExamResult> lst = findExamResultExt(me,user.getSchool_id(), null, user.getId(), null, eclass.getYear_id());
				if (lst == null || lst.size() <=0){
						is_error = true;
				}else{
					for (ExamResult ex: lst){
						if (!is_inputted(ex, ex_key)){
							is_error = true;
							break;
						}
					}
					
				}
				if (is_error){
					// Only one class
					if (classes.length <= 1){
						err_msg += user.getId().intValue()+","; // list of error user.id in the class
					}else{
						// Many classes
						err_msg += eclass.getId().intValue()+","; // list of error class_id
						break;
					}
				}
				
			}
		}
		
		if (err_msg.length() > 0){
			if (classes.length > 1){
				//err_msg = "Please complete exam result for classes:"+err_msg;
				err_msg = "There are some classes that were not filled score field:"+err_msg;
				
				
			}else{
				//err_msg = "Please complete exam result for users:"+err_msg;
				err_msg = "There are some students that were not filled score field. Please refill and continue:"+err_msg;
				
			}
		}
		return err_msg;
		
	}
	@Override
	public boolean is_inputted(ExamResult examResult, String ex_key){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("ex_key"+(ex_key==null?"null":ex_key));
		
		// Find m1,m2,m3...m20 available
		java.lang.reflect.Field[] fields = examResult.getClass().getDeclaredFields();
		//SchoolExam school_exam = null;
		
        for (Field field : fields) {
            field.setAccessible(true);
            String fname =field.getName();// m1,m2..m15
            
			if (fname.equalsIgnoreCase(ex_key)){ // m1 ~ m20
        		String sresult = null;
        		// Parse score value
        		try{
        			sresult = (String)field.get(examResult); // {"sresult":"9.0","notice":"","exam_dt":"2016-09-07"}
        		}catch (Exception e){
        			throw new ESchoolException(fname + ": cannot get sresult, exception message: "+ e.getMessage(), HttpStatus.BAD_REQUEST);
        		}
        		if (sresult != null && sresult.trim().length() > 0){
        			// Parsing JSON to Exam Detail
        			ExamDetail examDetail = ExamDetail.strJson2ExamDetail(sresult);//{"sresult":"9.0","notice":"","exam_dt":"2016-09-07"}
            		// Parsing float 
            		String sval = examDetail.getSresult();
            		
            		if (sval == null || sval.trim().length() == 0){
            			return false;
            		}
            		Float fval = Utils.parseFloat(sval);
            		if (fval != null && fval.floatValue() >= 0){
            			return true;
            		}

        		}
        		break;
        	}
			
        }
        return false;
   	}
	/***
	 * Kiem tra da input diem cho ca lop cho toan bo mon hoc trong thang chua
	 * Lay danh sach hoc sinh cua lop
	 * Loop student
	 *    Voi moi student
	 *        Lay examResult theo subject
	 *        Kiem tra ret = is_inputted(examResult,ex_key)
	 *              Neu  ret == false, return false
	 *  Return true;
	 *        
	 */
	@Override
	public boolean is_completed(Integer school_id, Integer class_id, Integer subject_id, String ex_key) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("school_id"+(school_id==null?"null":school_id));
		logger.info("class_id"+(class_id==null?"null":class_id));
		logger.info("subject_id"+(subject_id==null?"null":subject_id));
		logger.info("ex_key"+(ex_key==null?"null":ex_key));
		
		if (school_id == null || school_id.intValue() == 0){
			logger.error("school_id is null");
			return false;
		}
		if (class_id == null || class_id.intValue() == 0){
			logger.error("class_id is null");
			return false;
		}
		if (subject_id == null || subject_id.intValue() == 0){
			logger.error("school_id is null");
			return false;
		}
		if (ex_key == null || ex_key.trim().length() == 0){
			logger.error("ex_key is required");
			return false;
		}
		
		// Lay class
		EClass eclass = classService.findById(class_id);
		if (eclass == null ){
			logger.error("class_id"+(class_id==null?"null":class_id)+" is not existing");
			return false;
		}
		if (eclass.getSchool_id().intValue() != school_id.intValue()){
			logger.error("class_id"+(class_id==null?"null":class_id)+" is not belong to school_id="+school_id.intValue());
			return false;
		}
		// Lay subject
		MSubject subject = msubjectDao.findById(subject_id);
		if (subject == null ){
			logger.error("subject_id"+(subject_id==null?"null":subject_id)+" is not existing");
			return false;
		}
		if (subject.getSchool_id().intValue() != school_id.intValue()){
			logger.error("subject_id"+(subject_id==null?"null":subject_id)+" is not belong to school_id="+school_id.intValue());
			return false;
		}
		
		// Lay Exam
		SchoolExam  schoolExam = schoolExamService.findBySchoolAndKey(school_id, ex_key);
		if (schoolExam == null ){
			logger.error("ex_key"+(ex_key==null?"null":ex_key)+" is not existing");
			return false;
		}
		if (subject.getSchool_id().intValue() != school_id.intValue()){
			logger.error("ex_key"+(ex_key==null?"null":ex_key)+" is not belong to school_id="+school_id.intValue());
			return false;
		}
		
		
		// Lay danh sach lop
		Set<User> users = eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
		if (users == null || users.size() <= 0){
			logger.error("class_id"+(class_id==null?"null":class_id)+" has no USER");
			return false;
		}
		
		SchoolYear schoolYear  = schoolYearService.findLatestYearBySchool(school_id);
		if (schoolYear == null){
			logger.error(" Cannot get current active school_year of school_id:"+school_id.intValue());
			return false;
		}
		
		for (User user: users){
			// Lay danh sach diem cua SUBJECT
			ArrayList<ExamResult> examResults = examResultDao.findExamResultExt(school_id, class_id, user.getId(), subject_id, schoolYear.getId());
			if (examResults == null || examResults.size() <= 0){
				logger.info("Tim danh sach diem (examResults) theo school, class,user,subject,year = BLANK");
				return false;
			}
			for (ExamResult examResult: examResults){
				if ( ! is_inputted(examResult, ex_key)){
					return false;
				}
			}
		}
		
		return true;
	}

	
}
