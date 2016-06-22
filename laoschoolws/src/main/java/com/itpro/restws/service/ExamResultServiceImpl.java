package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.engine.jdbc.spi.ExtractedDatabaseMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.ExamResultDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.dao.SchoolExamDao;
import com.itpro.restws.dao.SchoolYearDao;
import com.itpro.restws.dao.TermDao;
import com.itpro.restws.helper.AveInfo;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_EXAM_TYPE;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

@Service("examResultService")
@Transactional
public class ExamResultServiceImpl implements ExamResultService{
	private static final Logger logger = Logger.getLogger(ExamResultServiceImpl.class);
	@Autowired
	private ExamResultDao examResultDao;
	
	@Autowired
	private TermDao termDao;
	
	@Autowired
	private ClassService classService;
	@Autowired
	private MSubjectDao msubjectDao;

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SchoolYearDao schoolYearDao;
	
	@Autowired
	private SchoolExamDao schoolExamDao;
	
	
	
	@Autowired
	private TimetableService timetableService;
	
	@Autowired
	private ClassDao classesDao;
	
	
	
	@Override
	public ExamResult findById(Integer id) {
		return examResultDao.findById(id);
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		
		return examResultDao.countExamBySchool(school_id);
	}

	@Override
	public int countByClassID(Integer class_id) {
		
		return examResultDao.countExamBySclass(class_id);
	}

	@Override
	public int countByStudentID(Integer user_id) {
		
		return examResultDao.countExamByUser(user_id);
	}

	@Override
	public ArrayList<ExamResult> findBySchoolID(Integer school_id, int from_num, int max_result) {
		
		return (ArrayList<ExamResult>) examResultDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByClassID(Integer class_id, int from_num, int max_result) {
		return (ArrayList<ExamResult>) examResultDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByStudentID(Integer user_id, int from_num, int max_result) {
		return (ArrayList<ExamResult>) examResultDao.findByStudent(user_id, from_num, max_result);
	}


	
	@Override
	public ExamResult updateExamResult(User teacher, ExamResult examResult) {
		ExamResult existing_db = examResultDao.findById(examResult.getId());
		existing_db = ExamResult.updateChanges(existing_db, examResult);
		examResultDao.updateExamResult(existing_db);
		return existing_db;
	}


	@Override
	public ExamResult inputExam(User teacher, ExamResult examResult) {
		// Check Teacher
		if (teacher.getSchool_id().intValue() != examResult.getSchool_id().intValue()){
			throw new ESchoolException("Exam.SchoolID not similar with current user.school_id", HttpStatus.BAD_REQUEST);
		}
		// Update exam
		if (examResult.getId() != null && examResult.getId().intValue() > 0){
			updateExamResult(teacher,examResult);
			return examResult;
		}else{
		// Add new exam
			examResult.setId(null);// New exam
			// Check exam_id
			SchoolExam school_exam = null;
			if (examResult.getExam_id() != null && examResult.getExam_id().intValue() > 0){
				school_exam = schoolExamDao.findById(examResult.getExam_id());
				if (school_exam == null ){
					throw new ESchoolException("ExamResult.exam_id is not existing:"+examResult.getExam_id().intValue(), HttpStatus.BAD_REQUEST);
				}
				if (school_exam.getSchool_id().intValue() != examResult.getSchool_id().intValue() ){
					throw new ESchoolException("ExamResult.Term_ID.School_ID is not similar with Exam.School_ID", HttpStatus.BAD_REQUEST);
				}	
			}
			if (school_exam == null ){
				throw new ESchoolException("ExamResult.exam_id  is NULL", HttpStatus.BAD_REQUEST);
			}
			
			
			
			// Get term_id
			SchoolTerm term = null;
			
			if (examResult.getTerm_id() != null && examResult.getTerm_id().intValue() > 0){
				term = termDao.findById(examResult.getTerm_id());
				if (term == null ){
					throw new ESchoolException("ExamResult.Term_ID is not existing:"+examResult.getTerm_id().intValue(), HttpStatus.BAD_REQUEST);
				}
				if (term.getSchool_id().intValue() != examResult.getSchool_id().intValue() ){
					throw new ESchoolException("ExamResult.Term_ID.School_ID is not similar with Exam.School_ID", HttpStatus.BAD_REQUEST);
				}
				if (term.getSchool_id().intValue() != examResult.getSchool_id().intValue() ){
					throw new ESchoolException("ExamResult.Term_ID.School_ID is not similar with Exam.School_ID", HttpStatus.BAD_REQUEST);
				}
			}else{
				term = termDao.getCurrentTerm(examResult.getSchool_id());
			}
			
			if (term == null ){
				throw new ESchoolException("Cannot get TERM", HttpStatus.BAD_REQUEST);
			}
			
			// Update Exam Information
			examResult.setExam_month(school_exam.getEx_month());
			examResult.setExam_type(school_exam.getEx_type());
			examResult.setExam_name(school_exam.getEx_name());
			// Update term info
			examResult.setTerm_val(term.getTerm_val());
			examResult.setSch_year_id(term.getSchool_year_id());
			examResult.setExam_year(Utils.getCurrentYear());
			
			examResultDao.saveExamResult(examResult);
			return examResult;
		}
		
	}

	
	@Override
	public void validInputExam(User teacher, ExamResult exam) {
		// Fix school info
		exam.setSchool_id(teacher.getSchool_id());
		// Check class
		if (exam.getClass_id() == null ||exam.getClass_id()<= 0 ){
			throw new ESchoolException("exam.class_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		com.itpro.restws.model.EClass eclass = classesDao.findById(exam.getClass_id());
		if (eclass == null ){
			throw new ESchoolException("exam.class_id is not existing", HttpStatus.BAD_REQUEST);
		}
		if (eclass.getSchool_id().intValue() != exam.getSchool_id().intValue() ){
			throw new ESchoolException("exam.school_id is not similar with current user.school_id", HttpStatus.BAD_REQUEST);
		}
		// check student
		
		if (exam.getStudent_id() == null ||exam.getStudent_id()<= 0 ){
			throw new ESchoolException("exam.student_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		
		User student = userService.findById(exam.getStudent_id());
		if (student == null ){
			throw new ESchoolException("exam.student_id is not existing", HttpStatus.BAD_REQUEST);
		}
		
		if (student.getSchool_id().intValue() != exam.getSchool_id().intValue() ){
			throw new ESchoolException("exam.student_id:"+ student.getId()+" is not belong to school_id:"+exam.getSchool_id(), HttpStatus.BAD_REQUEST);
		}
		
		if (!student.hasRole(E_ROLE.STUDENT.getRole_short())){
			throw new ESchoolException("Exam Student is not Student role:"+exam.getStudent_id(), HttpStatus.BAD_REQUEST);
		}
		exam.setStudent_name(student.getSso_id());
		exam.setStd_nickname(student.getNickname());
		exam.setStd_photo(student.getPhoto());
		
		// Check teacher
		if (exam.getTeacher_id() == null || exam.getTeacher_id().intValue() <=0){
			exam.setTeacher_id(teacher.getId());
		}else{
			User tmp_teach = userService.findById(exam.getTeacher_id());
			if (tmp_teach == null ){
				throw new ESchoolException("exam.teachter_id is not existing", HttpStatus.BAD_REQUEST);
			}
			if (!(tmp_teach.hasRole(E_ROLE.TEACHER.getRole_short()))){
				throw new ESchoolException("exam.teachter_id's ROLE is not TEACHER", HttpStatus.BAD_REQUEST);
			}
			if (tmp_teach.getSchool_id().intValue() != exam.getSchool_id().intValue()){
				throw new ESchoolException("teacher.school_id is not similar with exam.school_id", HttpStatus.BAD_REQUEST);
			}
		}
		
		
		
		// Check Exam 
		if (exam.getExam_id() == null || exam.getExam_id().intValue() <=0){
			throw new ESchoolException("exam.exam_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		SchoolExam school_exam = schoolExamDao.findById(exam.getExam_id());
		if (school_exam == null ){
			throw new ESchoolException("ExamResult.exam_id is not existing:"+exam.getExam_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		if (school_exam.getSchool_id().intValue() != exam.getSchool_id().intValue() ){
			throw new ESchoolException("Exam.School_ID is not similar with Term.School_ID", HttpStatus.BAD_REQUEST);
		}
		

		// Check Subject
		if (exam.getSubject_id() == null  || exam.getSubject_id().intValue() <=0){
			throw new ESchoolException("subject_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		MSubject msubject = msubjectDao.findById(exam.getSubject_id());
		if (msubject == null ){
			throw new ESchoolException("ExamResult.subject_id is not existing:"+exam.getSubject_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		if (msubject.getSchool_id().intValue() != exam.getSchool_id().intValue() ){
			throw new ESchoolException("Subject.School_ID is not similar with exam.School_ID", HttpStatus.BAD_REQUEST);
		}
		
		// Check Term
		if (exam.getTerm_id() == null  || exam.getTerm_id().intValue() <=0){
			throw new ESchoolException("term_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		
		
		SchoolTerm term = termDao.getCurrentTerm(exam.getTerm_id());
		if (term == null ){
			throw new ESchoolException("ExamResult.term_id is not existing:"+exam.getTerm_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		if (term.getSchool_id().intValue() != exam.getSchool_id().intValue() ){
			throw new ESchoolException("Term.School_ID is not similar with exam.School_ID", HttpStatus.BAD_REQUEST);
		}
		exam.setTerm(term.getTerm_name());
		exam.setTerm_val(term.getTerm_val());
		// Check YEAR
		if (exam.getSch_year_id() == null   || exam.getSch_year_id().intValue() <=0 ){
			exam.setSch_year_id(term.getSchool_year_id());
		}
		
		SchoolYear school_year = schoolYearDao.findById(exam.getSch_year_id());
		if (school_year == null ){
			throw new ESchoolException("ExamResult.getSch_year_id is not existing:"+exam.getSubject_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		if (school_year.getSchool_id().intValue() != exam.getSchool_id().intValue() ){
			throw new ESchoolException("SchoolYear.School_ID is not similar with exam.School_ID", HttpStatus.BAD_REQUEST);
		}
		if (exam.getExam_year() == null || exam.getExam_year().intValue() <= 0 ){
			exam.setExam_year(Utils.getCurrentYear());
		}else{
			if( (exam.getExam_year().intValue() < school_year.getFrom_year().intValue()) ||
			  (exam.getExam_year().intValue() > school_year.getTo_year().intValue())){
				throw new ESchoolException("Invalid exam.exam_year, must be withint school_year.from - to range"+school_year.getFrom_year().intValue()+"-"+school_year.getTo_year().intValue(), HttpStatus.BAD_REQUEST);
			}
		}
		// Optional
		if (exam.getExam_dt() == null || "".equals(exam.getExam_dt()) ){
			exam.setExam_dt(Utils.now());
		}
		
		// Check existing Exam for merger
		ArrayList<ExamResult> curr_list = (ArrayList<ExamResult>) examResultDao.findSameExam(
				exam.getSchool_id(),
				exam.getStudent_id(), 
				exam.getClass_id(), 
				exam.getSubject_id(), 
				exam.getExam_id(), 
				exam.getTerm_id(), 
				exam.getExam_year(), 
				exam.getSch_year_id());
		if (curr_list != null && curr_list.size() > 0){
			exam.setId(curr_list.get(0).getId());
		}
	}

	@Override
	public  int countExamResultExt(Integer school_id, 
			Integer class_id, 
			Integer user_id, 
			Integer subject_id,
			Integer term_id, 
			Integer exam_year, 
			Integer ex_month,
			String exam_dt, 
			String dateFrom, 
			String dateTo,
			Integer from_row_id,
			Integer exam_type,
			Integer sch_year_id) {
		

		
		return examResultDao.countExamResultExt(school_id, class_id, user_id, subject_id, term_id, exam_year, ex_month, exam_dt, dateFrom, dateTo, from_row_id,exam_type,sch_year_id);
	}

	@Override
	public ArrayList<ExamResult> findExamResultExt(Integer school_id, int from_row, int max_result, Integer class_id,
			Integer user_id, Integer subject_id, Integer term_id, Integer exam_year, Integer exam_month,
			String exam_dt, String dateFrom, String dateTo, Integer from_row_id,Integer exam_type, Integer sch_year_id) {
		ArrayList<ExamResult> list_ret = new ArrayList<ExamResult>();
		// Get current term
		list_ret = examResultDao.findExamResultExt(school_id, from_row, max_result, class_id, user_id, subject_id, term_id, exam_year, exam_month, exam_dt, dateFrom, dateTo, from_row_id,exam_type,sch_year_id);
		return list_ret;
	}

	@Override
	public void deleteExamResult(ExamResult exam) {
		examResultDao.deleteExamResult(exam);
		
	}



	private ExamResult  new_blank_exam(User student, Integer class_id, SchoolTerm term,SchoolExam sch_exam, MSubject subject){
		ExamResult blk_result= new ExamResult();
		
		// Fixed information
		blk_result.setActflg("A");
		blk_result.setCtddtm(Utils.now());
		blk_result.setSchool_id(student.getSchool_id());
		blk_result.setClass_id(class_id);
		blk_result.setStudent_id(student.getId());
		blk_result.setStudent_name(student.getSso_id());
		blk_result.setStd_nickname(student.getNickname());
		blk_result.setStd_photo(student.getPhoto());
		// Month information
		blk_result.setExam_id(sch_exam.getId());
		blk_result.setExam_name(sch_exam.getEx_name());
		blk_result.setExam_type(sch_exam.getEx_type());
		blk_result.setExam_month(sch_exam.getEx_month());
		// Subject information
		blk_result.setSubject_id(subject.getId());
		blk_result.setSubject(subject.getSval());
		// Term & year information
		//if (term != null) {
			blk_result.setTerm_id(term.getId());
			blk_result.setTerm_val(term.getTerm_val());
			blk_result.setTerm(term.getTerm_name());
			// Year information
			blk_result.setSch_year_id(term.getSchool_year_id());

		//}
		// Notice
		blk_result.setNotice("BLANK");
		return blk_result;
		
	}
	

	@Override
	public ArrayList<ExamResult> getUserResult_Mark(User student, Integer class_id, Integer subject_id, boolean all_term) {
		
		// Get current school year
//		SchoolYear school_year = schoolYearDao.findLastestOfSchoolId(student.getSchool_id());
//		if (school_year == null ){
//			// Return
//			throw new ESchoolException("Cannot find School Year for school_id:"+student.getSchool_id(),HttpStatus.BAD_REQUEST);
//
//		}

		// Get current term & year
		SchoolTerm current_term = termDao.getCurrentTerm(student.getSchool_id());
		SchoolYear school_year = schoolYearDao.findById(current_term.getSchool_year_id());
	
		
		// Get Blank ExamResult
		MSubject tar_subject = null;
		if (subject_id != null && subject_id.intValue() > 0){
			tar_subject = msubjectDao.findById(subject_id);
		}
		
		ArrayList<ExamResult> blank_list = iniBlankExamResults(student, class_id,tar_subject,all_term?null:current_term);
		// Get Actual ExamResult
		ArrayList<ExamResult> db_list = (ArrayList<ExamResult>) examResultDao.findExamResultExt(student.getSchool_id(), 0, 99999, class_id,student.getId(),subject_id, all_term?null:current_term.getId(), null, null, null, null, null, null, null,school_year.getId());
		                                                                                       
		// Final list
		ArrayList<ExamResult> profile_list = new ArrayList<ExamResult>();
		// Merge actual to Blank
		for (ExamResult blank: blank_list){
			ArrayList<ExamResult> founded_list = new ArrayList<ExamResult>();
			for (ExamResult act: db_list){
				
				if (	(blank.getExam_id().intValue() == act.getExam_id().intValue()) &&  			// Same Exam
						( blank.getSubject_id().intValue() == act.getSubject_id().intValue()) &&	// Same Subject
						blank.getSch_year_id().intValue() == act.getSch_year_id().intValue() &&		// Same Year_ID
                        blank.getExam_type().intValue() == act.getExam_type().intValue()			// Same Exam Type (Just for sure)
						){ 
					
					// Check Similar Term val (for sure)
					if (  (  (blank.getTerm_val() == null ||  blank.getTerm_val().intValue() == 0) &&   
							 (act.getTerm_val() == null || act.getTerm_val().intValue() == 0   ) ) 
							||
							 (blank.getTerm_val().intValue() ==  act.getTerm_val().intValue())){
						
						founded_list.add(act);
					}
				}
			}
			
			if (founded_list.size() > 0 ){
				for (ExamResult act: founded_list){
					profile_list.add(act);
					db_list.remove(act);
				}
			}else{
				profile_list.add(blank);
			}
		}
		// Remaining DB list ( not in blank)
//		if (db_list.size() > 0){
//			for (ExamResult act: db_list){
//				profile_list.add(act);
//			}
//		}
		
		//calAverage(profile_list,student,school_year);
		return profile_list;
		
	}
	
	
	private ArrayList<ExamResult> iniBlankExamResults(User student,Integer class_id,MSubject tar_subject, SchoolTerm tar_term ){
		Integer school_id = student.getSchool_id();
		 ArrayList<ExamResult> list = new ArrayList<ExamResult>();
		 // Get list of exam type
		 //ArrayList<SchoolExam> sch_exams = (ArrayList<SchoolExam>) schoolExamDao.findBySchool(school_id, 0, 99999);
		 ArrayList<SchoolExam> class_exams = classService.findExamOfClass(student, class_id,tar_term);
		 
		 // List subject
		 ArrayList<MSubject> msubjects = new ArrayList<MSubject>();
		 if (tar_subject != null ){
			 msubjects.add(tar_subject);
			 
		 }else{
			 msubjects = timetableService.findSubjectOfClass(class_id);
			 
		 }
		 
		 // Get list of Terms
		 ArrayList<SchoolTerm> terms = new ArrayList<SchoolTerm>();
		 if (tar_term != null ){
			 terms.add(tar_term);
		 }else{
			 terms = termDao.findBySchool(school_id, 0, 99999);
		 }
		 
		 // Create Blank result for each Exam vs Subject
		 
		for (MSubject subject: msubjects){
			for (SchoolExam sch_exam: class_exams){
				// Mapping correct Term => correct Exam
				SchoolTerm term = null;
				if (sch_exam.getTerm_val() != null && sch_exam.getTerm_val().intValue() > 0){
					// Thi cuoi ky 1, cuoi ky 2
					for (SchoolTerm tmp_tmp : terms){
						if  (tmp_tmp.getTerm_val().intValue() == sch_exam.getTerm_val().intValue()){
							term = tmp_tmp;
							break;
						}
					}
					
				}else{
					term = null;
					// Thi lai, thi tot nghiep, cuoi cap bat buoc phai la term = 2
				}
				
				
				// Create new blank
				if (term != null) {
					ExamResult blank_result = new_blank_exam(student, class_id, term, sch_exam, subject);
					list.add(blank_result);
				}
			}
		}
	 
		 return list;
	}

	@Override
	public ArrayList<ExamResult> getClassResult_Mark(Integer school_id, Integer class_id, 	Integer subject_id,boolean all_term) {
		// Check lass
		EClass eclass = classesDao.findById(class_id);
		if (eclass == null){
			throw new ESchoolException("Cannot find Class for ID:"+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (eclass.getSchool_id().intValue() != school_id.intValue()){
			throw new ESchoolException("Class is not in same with School of current user:"+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		// Get users		
		ArrayList<ExamResult> list = new ArrayList<ExamResult>();
		ArrayList<User> users = userService.findByClass(class_id, 0, 999999);
		// Get exam result
		for (User user : users){
			if (user.hasRole(E_ROLE.STUDENT.getRole_short())){
				list.addAll(getUserResult_Mark(user, class_id, subject_id,all_term));
			}
		}
		return list;
	}
//	@Override
//	public void calAverage(User student, SchoolYear schoolYear, int term_val){
//		
//		ArrayList<ExamResult> exam_results = findExamResultExt(student.getSchool_id(),0,999999, null, student.getId(), null, null, null, null, null, null, null, null, null, schoolYear.getId());
//		
//		// TB - 4 thang hoc ky 1, HK2
//		ArrayList <Integer> sub_ex_types = new ArrayList<Integer>();
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.MONTH.getValue()));
//		
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_4_MONTH.getValue(),1);
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_4_MONTH.getValue(),2);
//		// TB - Hoc ky 1, Hk2
//		sub_ex_types.clear();
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.AVE_4_MONTH.getValue()));
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.TEST_TERM.getValue()));
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_TERM.getValue(),1);
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_TERM.getValue(),2);
//		
//		// TB Year
//		sub_ex_types.clear();
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.AVE_TERM.getValue()));
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_YEAR.getValue(),0);
//	}
	@Override
	public void calAverageTerm(User student, SchoolYear schoolYear, int term_val){
		
		ArrayList<ExamResult> exam_results = findExamResultExt(student.getSchool_id(),0,999999, null, student.getId(), null, null, null, null, null, null, null, null, null, schoolYear.getId());
		
		// TB - 4 thang hoc ky 1, HK2
		ArrayList <Integer> sub_ex_types = new ArrayList<Integer>();
		sub_ex_types.add(new Integer(E_EXAM_TYPE.MONTH.getValue()));
		
		cal_average(exam_results,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_4_MONTH.getValue(),1);
	}

	@Override
	public void calAverageYear(User student, SchoolYear schoolYear){
		calAverageTerm(student,schoolYear,1);
		calAverageTerm(student,schoolYear,2);
		// TB Year
		ArrayList<ExamResult> exam_results = findExamResultExt(student.getSchool_id(),0,999999, null, student.getId(), null, null, null, null, null, null, null, null, null, schoolYear.getId());		
		ArrayList <Integer> sub_ex_types = new ArrayList<Integer>();
		sub_ex_types.add(new Integer(E_EXAM_TYPE.AVE_TERM.getValue()));
		cal_average(exam_results,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_YEAR.getValue(),0);
		
	}
	
	
	private void  cal_average(ArrayList<ExamResult> list, User student, SchoolYear schoolYear,ArrayList<Integer> sum_ex_types,int ave_ex_type,int term_val){

		HashMap<String, AveInfo> hashMap = new HashMap<>();
		
		for (ExamResult examResult : list){
			// Check user
			if (examResult.getStudent_id().intValue() != student.getId().intValue()){
				continue;
			}
			// Check school year
			if (	examResult.getSch_year_id() == null || 
					examResult.getSch_year_id().intValue() == 0 || 
					(examResult.getSch_year_id().intValue() != schoolYear.getId().intValue())){
				continue;
			}
			
			// Create HashMap with key = SubjectID
			if (!hashMap.containsKey(""+examResult.getSubject_id().intValue())){
				AveInfo aveInfo = new AveInfo();
				//Ini data
				
				SchoolTerm sch_term = termDao.findById(examResult.getTerm_id());
				MSubject msubject = msubjectDao.findById(examResult.getSubject_id());
				SchoolExam ave_exam = null;
				ArrayList<SchoolExam> sch_exams = (ArrayList<SchoolExam>) schoolExamDao.findByEx(student.getSchool_id(), ave_ex_type, term_val);
				if (sch_exams != null && sch_exams.size() > 0){
					ave_exam = sch_exams.get(0);
					ExamResult blank_examResult =  new_blank_exam(student, examResult.getClass_id(), sch_term, ave_exam, msubject);
					aveInfo.setAve_exam_result(blank_examResult);
					aveInfo.setCnt(0);
					aveInfo.setTotal(0);
					aveInfo.setIs_new(1);
					hashMap.put(""+examResult.getSubject_id().intValue(), aveInfo);
				}
				
			}
			
			// SET MAIN EXAM TYPE IF EXIST
			if ( (examResult.getExam_type() != null) && 
					(examResult.getExam_type().intValue() == ave_ex_type) 
					){
				
						if (term_val > 0){
							if (    (examResult.getTerm_val() != null) &&
									(examResult.getTerm_val().intValue() != term_val)){
								continue;
							}
						}
						if (hashMap.containsKey(""+examResult.getSubject_id().intValue())){
							AveInfo aveInfo = hashMap.get(""+examResult.getSubject_id().intValue());
							aveInfo.setAve_exam_result(examResult);// Replace the blank
							aveInfo.setIs_new(0);
						}
			}else{
				// Check SUB EXAM TYPE
				boolean next = false;
				for (Integer sub_ex_type : sum_ex_types){
					if (sub_ex_type.intValue() == examResult.getExam_type().intValue()){
						next = true;
						break;
					}
				}
				if (!next){
					continue;
				}
				// Check term val (Optional)
				next = true;
				if (term_val > 0){
					
					if (    (examResult.getTerm_val() != null) &&
							(examResult.getTerm_val().intValue() == term_val)){
						next = true;
					}
				}
				if (!next){
					continue;
				}
				// Group by subject ID
				Float val = Utils.parseFloat(examResult.getSresult());
				if (val != null ){
					if (hashMap.containsKey(""+examResult.getSubject_id().intValue())){
						AveInfo aveInfo = hashMap.get(""+examResult.getSubject_id().intValue());
						aveInfo.setCnt(aveInfo.getCnt()+1);
						aveInfo.setTotal(aveInfo.getTotal()+ val);
					}
				}
			}
		}
		 // Calculate
		Iterator<String> keyIterator = hashMap.keySet().iterator();
	    while (keyIterator.hasNext()) {
	    	 String key = keyIterator.next();
	    	 AveInfo aveInfo =  hashMap.get(key);
	    	 if (aveInfo.getCnt() > 0){
	    		 Float average =  aveInfo.getTotal() / aveInfo.getCnt();
	    		 ExamResult aveExamResult = aveInfo.getAve_exam_result();
	    		 // calculate average 
	    		 if (aveExamResult != null ) {
		    		 aveExamResult.setSresult(String.format("%.2f", average));
		    		 examResultDao.saveExamResult(aveExamResult);
	    		 }
	    	 }
	    	 
	    }
	  
	}
	private ArrayList<ExamResult> diffExamResults(ArrayList<ExamResult> list1, ArrayList<ExamResult> list2 ){
		ArrayList<ExamResult>  diff_list = new ArrayList<ExamResult>();
		for (ExamResult e1: list1){
			boolean is_diff = true;
			for (ExamResult e2: list2){
				if (( e1.getSchool_id().intValue() == e2.getSchool_id().intValue()) &&
				   ( e1.getClass_id().intValue() == e2.getClass_id().intValue()) &&
				   ( e1.getStudent_id().intValue() == e2.getStudent_id().intValue()) &&
				   ( e1.getSubject_id().intValue() == e2.getSubject_id().intValue()) &&
				   ( e1.getExam_id().intValue() == e2.getExam_id().intValue()) &&
				   ( e1.getSch_year_id().intValue() == e2.getSch_year_id().intValue()) &&
				   ( e1.getTerm_val().intValue() == e2.getTerm_val().intValue()) &&
				   ( e1.getExam_type().intValue() == e2.getExam_type().intValue()) ){
					is_diff = false;
					break;
				}
			}
			if (is_diff){
				diff_list.add(e1);
			}
		}
		return diff_list;
	}

	@Override
	public void calAverage(EClass eclass, int term_val) {
		userService.findByClass(eclass.getId(), 0, 999999);
		
	}

		
}
