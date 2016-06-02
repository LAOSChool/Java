package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ExamMonthDao;
import com.itpro.restws.dao.ExamResultDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.dao.SchoolYearDao;
import com.itpro.restws.dao.TermDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ExamMonth;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.MSubject;
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
	private ExamMonthDao examMonthDao;
	@Autowired
	private MSubjectDao msubjectDao;

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SchoolYearDao schoolYearDao;
	
	
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
	public ExamResult updateExamResult(ExamResult examResult) {
		ExamResult existing_db = examResultDao.findById(examResult.getId());
		existing_db = ExamResult.updateChanges(existing_db, examResult);
		examResultDao.updateExamResult(existing_db);
		return existing_db;
	}


	@Override
	public ExamResult inputExam(ExamResult exam) {
		//Find same exam in month 
		 List<ExamResult> list = examResultDao.findSameExam(exam.getSchool_id(),exam.getStudent_id(),exam.getExam_year(),exam.getExam_month(),exam.getSubject_id(),exam.getExam_type());
		 
		 
		if (list != null && list.size() > 0 ){
			ExamResult existing_db = list.get(0); // latest one
			existing_db = ExamResult.updateChanges(existing_db, exam);
			examResultDao.updateExamResult(existing_db);
			return existing_db;
		}else{
			examResultDao.saveExamResult(exam);
			return exam;
		}
	}

	
	@Override
	public void validUpdateExam(User teacher, ExamResult exam,boolean is_update){
		// Fix school info
		exam.setSchool_id(teacher.getSchool_id());
		// Check class
		if (exam.getClass_id() == null ||exam.getClass_id()<= 0 ){
			throw new ESchoolException("exam.class_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}

		// check student
		
		if (exam.getStudent_id() == null ||exam.getStudent_id()<= 0 ){
			throw new ESchoolException("exam.student_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		
		User student = userService.findById(exam.getStudent_id());
		if (student.getSchool_id().intValue() != exam.getSchool_id().intValue() ){
			throw new ESchoolException("exam.student_id:"+ student.getId()+" is not belong to school_id:"+exam.getSchool_id(), HttpStatus.BAD_REQUEST);
		}
		
		if (!student.hasRole(E_ROLE.STUDENT.getRole_short())){
			throw new ESchoolException("Exam Student is not Student role:"+exam.getStudent_id(), HttpStatus.BAD_REQUEST);
		}
		exam.setStudent_name(student.getFullname());

		
		// Check teacher
		if (!teacher.hasRole(E_ROLE.ADMIN.getRole_short())){
			if (exam.getTeacher_id() == null || (exam.getTeacher_id().intValue() <= 0)){
				throw new ESchoolException("exam.teacher_id cannot be NULL", HttpStatus.BAD_REQUEST);
			}
		
//			if (!(userService.isBelongToClass(exam.getTeacher_id(), exam.getClass_id()))){
//				throw new ESchoolException("exam.teacher_id="+exam.getTeacher_id()+"  is not belong to class_id="+ exam.getClass_id(), HttpStatus.BAD_REQUEST);
//			}
		}else{
			if (exam.getTeacher_id() == null ){
				exam.setTeacher_id(teacher.getId());
			}
//			if (teacher.getId().intValue() != exam.getTeacher_id().intValue()){
//				throw new ESchoolException("exam.teacher_id="+exam.getTeacher_id()+"  cannot be different from current teacher_id="+teacher.getId(), HttpStatus.BAD_REQUEST);
//			}
		}
		// Check Student
		if (exam.getStudent_id() == null ){
			throw new ESchoolException("exam.student_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		// Check Exam month - year
		if (exam.getExam_month() == null || "".equals(exam.getExam_month())){
			throw new ESchoolException("exam_month cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		if (exam.getExam_year() == null || "".equals(exam.getExam_year())){
			throw new ESchoolException("exam_year cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		// Check Exam type		
		if (exam.getExam_type() == null ){
			throw new ESchoolException("exam_type cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		if (exam.getSubject_id() == null ){
			throw new ESchoolException("subject_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		if (exam.getTerm_id() == null ){
			throw new ESchoolException("term_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		// Optional
		if (exam.getExam_dt() == null ){
			exam.setExam_dt(Utils.now());
		}
		// Check update
		if (is_update){
			if (exam.getId()== null){
				throw new ESchoolException("Exam ID cannot be NULL", HttpStatus.BAD_REQUEST);
			}
			ExamResult curr = examResultDao.findById(exam.getId());
			if (curr == null ){
				throw new ESchoolException("Current Exam Result is not exising:"+exam.getId(), HttpStatus.BAD_REQUEST);
			}
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
			Integer exam_type) {
		

		
		return examResultDao.countExamResultExt(school_id, class_id, user_id, subject_id, term_id, exam_year, ex_month, exam_dt, dateFrom, dateTo, from_row_id,exam_type);
	}

	@Override
	public ArrayList<ExamResult> findExamResultExt(Integer school_id, int from_row, int max_result, Integer class_id,
			Integer user_id, Integer subject_id, Integer term_id, Integer exam_year, Integer exam_month,
			String exam_dt, String dateFrom, String dateTo, Integer from_row_id,Integer exam_type) {
		ArrayList<ExamResult> list_ret = new ArrayList<ExamResult>();
		// Get current term
		list_ret = examResultDao.findExamResultExt(school_id, from_row, max_result, class_id, user_id, subject_id, term_id, exam_year, exam_month, exam_dt, dateFrom, dateTo, from_row_id,exam_type);
		return list_ret;
	}

	@Override
	public void deleteExamResult(ExamResult exam) {
		examResultDao.deleteExamResult(exam);
		
	}


	private ExamResult  new_blank_exam(User user, Integer class_id, SchoolTerm term,ExamMonth ex_month, MSubject subject){
		ExamResult tmp= new ExamResult();
		
		
		tmp.setActflg("A");
		tmp.setCtddtm(Utils.now());
		tmp.setSchool_id(user.getSchool_id());
		tmp.setClass_id(class_id);
		//tmp.setExam_dt();
		tmp.setExam_month(ex_month.getEx_month());
		tmp.setExam_year(ex_month.getEx_year());
		tmp.setExam_type(ex_month.getEx_type());
		
		tmp.setStudent_id(user.getId());
		tmp.setSubject(subject.getSval());
		tmp.setSubject_id(subject.getId());
		
		tmp.setStudent_name(user.getFullname());
		
		tmp.setNotice(ex_month.getNotice());
		
		tmp.setTerm_id(term.getId());
		tmp.setTerm(term.getTerm_name());
		
		return tmp;
		
	}
	
//	@Override
//	public int countUserProfile(User user,Integer class_id) {
//		
//		ArrayList<MSubject> m_subjects = null;
//		ArrayList<Term> filter_terms = null;
//		ArrayList<ExamMonth> filter_ex_months = null;
//
//		// Filter terms
//		filter_terms = (ArrayList<Term>) termDao.findBySchool(user.getSchool_id(), 0, 99999);
//		// Filter exam_month		
//		filter_ex_months = (ArrayList<ExamMonth>) examMonthDao.findBySchool(user.getSchool_id(), 0, 99999);
//		// Filter subjects
//		m_subjects = (ArrayList<MSubject>) msubjectDao.findBySchool(user.getSchool_id(), 0, 99999);
//		int count=0;
//		// Query from DB
//		for( Term term: filter_terms){
//			for (ExamMonth exam_month: filter_ex_months){
//				for (MSubject msubject: m_subjects){
//					if (exam_month.getTerm_id().intValue() >= term.getId() )
//					{
//						count += examResultDao.countExamResultExt(user.getSchool_id(), class_id, user.getId(), msubject.getId(), term.getId(), exam_month.getEx_year(), exam_month.getEx_month(), null, null, null, null);
//					}
//				}
//			}
//		}
//		
//		return count;
//		
//	}
	@Override
	public ArrayList<ExamResult> findUserProfile(User user,Integer class_id) {
		ArrayList<ExamResult> list_ret = new ArrayList<ExamResult>();
		ArrayList<MSubject> m_subjects = null;
		ArrayList<SchoolTerm> filter_terms = null;
		ArrayList<ExamMonth> filter_ex_months = null;

		// Filter terms
		filter_terms = (ArrayList<SchoolTerm>) termDao.findBySchool(user.getSchool_id(), 0, 99999);
		// Filter exam_month		
		filter_ex_months = (ArrayList<ExamMonth>) examMonthDao.findBySchool(user.getSchool_id(), 0, 99999);
		// Filter subjects
		m_subjects = (ArrayList<MSubject>) msubjectDao.findBySchool(user.getSchool_id(), 0, 99999);
		

		ArrayList<ExamResult> tmp_list = examResultDao.findExamResultExt(user.getSchool_id(), 0,99999, class_id, user.getId(), null,null,null,null,null, null, null, null,null);
		
		// Query from DB
		for( SchoolTerm term: filter_terms){
			for (ExamMonth exam_month: filter_ex_months){
				for (MSubject msubject: m_subjects){
					if (exam_month.getTerm_id().intValue() == term.getId() )
					{
						//ArrayList<ExamResult> tmp_list = examResultDao.findExamResultExt(user.getSchool_id(), 0,99999, class_id, user.getId(), msubject.getId(), term.getId(), exam_month.getEx_year(), exam_month.getEx_month(), null, null, null, null);
//						if (tmp_list !=null && tmp_list.size() >0){
//							list_ret.addAll(tmp_list);
//						}
						for (ExamResult ex_result:tmp_list){
							if  (ex_result.getTerm_id().intValue() == term.getId().intValue() &&
									ex_result.getExam_year().intValue() == exam_month.getEx_year().intValue() &&
										ex_result.getExam_month().intValue() == exam_month.getEx_month().intValue() &&
										   ex_result.getSubject_id().intValue() == msubject.getId().intValue() &&
										   	ex_result.getExam_type().intValue() == exam_month.getEx_type().intValue() 	   
									){
								list_ret.add(ex_result);
							}
						}
					}	
				}
			}
		}
		return list_ret;
	}

	@Override
	public void initStudentExamResult(User user, Integer class_id) {
		ArrayList<MSubject> m_subjects = null;
		ArrayList<SchoolTerm> filter_terms = null;
		ArrayList<ExamMonth> filter_ex_months = null;

		// Filter terms
		filter_terms = (ArrayList<SchoolTerm>) termDao.findBySchool(user.getSchool_id(), 0, 100);
		// Filter exam_month		
		filter_ex_months = (ArrayList<ExamMonth>) examMonthDao.findBySchool(user.getSchool_id(), 0, 100);
		
		// Filter subjects
		m_subjects = (ArrayList<MSubject>) msubjectDao.findBySchool(user.getSchool_id(), 0, 100);
		
		// Query from DB
		for( SchoolTerm term: filter_terms){
			for (ExamMonth exam_month: filter_ex_months){
				if (exam_month.getTerm_id().intValue() == term.getId() )
				{
					for (MSubject msubject: m_subjects){
						int tmp= examResultDao.countExamResultExt(user.getSchool_id(), class_id, user.getId(), msubject.getId(), term.getId(), exam_month.getEx_year(), exam_month.getEx_month(), null, null, null, null,exam_month.getEx_type());
						if (tmp > 0 ){
						}else{
							logger.info("Initial exam for user: "+user.getId()+", class_id:"+class_id+",exam_month:"+exam_month.getEx_month()+",exam_year:"+exam_month.getEx_year()+",subject:"+msubject.getSval()+", exam_type:"+exam_month.getEx_type());
							ExamResult blank = new_blank_exam(user,class_id,term,exam_month,msubject);
							// Save to DB
							examResultDao.saveExamResult(blank);

						}	
					}
					
					
				}
				
			}
		}
	}

	@Override
	public ArrayList<ExamResult> findUserProfile_Now(User user, Integer class_id) {
		
		// Get current school year
		SchoolYear school_year = schoolYearDao.findLastestOfSchoolId(user.getSchool_id());
		if (school_year != null){ 
			ArrayList<ExamResult> list = (ArrayList<ExamResult>) examResultDao.findStudentExam(user.getSchool_id(), class_id, user.getId(), school_year.getId());
			return list;
		}
		logger.error("Cannot find School Year for school_id:"+user.getSchool_id());;
		return null;
		
	}
	
//	public ArrayList<ExamResult> getBlankExamResults(User student){
//		 ArrayList<ExamResult> list = new ArrayList<ExamResult>();
//		 Integer school_id = student.getSchool_id();
//		 Set<EClass> classes = student.getClasses();
//		 ArrayList<MSubject> msubjects = msubjectDao.findBySchool(school_id, 0, 99999);
//		 ArrayList<Term> terms = termDao.getLatestTerm(school_id);
//		// Query from DB
//		for( Term term: terms){
//			
//				if (exam_month.getTerm_id().intValue() == term.getId() )
//				{
//					for (MSubject msubject: m_subjects){
//						int tmp= examResultDao.countExamResultExt(user.getSchool_id(), class_id, user.getId(), msubject.getId(), term.getId(), exam_month.getEx_year(), exam_month.getEx_month(), null, null, null, null,exam_month.getEx_type());
//						if (tmp > 0 ){
//						}else{
//							logger.info("Initial exam for user: "+user.getId()+", class_id:"+class_id+",exam_month:"+exam_month.getEx_month()+",exam_year:"+exam_month.getEx_year()+",subject:"+msubject.getSval()+", exam_type:"+exam_month.getEx_type());
//							ExamResult blank = new_blank_exam(user,class_id,term,exam_month,msubject);
//							// Save to DB
//							examResultDao.saveExamResult(blank);
//
//						}	
//					}
//					
//					
//				}
//				
//			
//		}
//	}
//	
	
	
//	public ArrayList<ExamResult> getBlankExamResults(User student, Integer class_id, MSubject subject, Term term){
//		Integer school_id = student.getSchool_id();
//		 ArrayList<ExamResult> list = new ArrayList<ExamResult>();
//		 // Get list of exam type
//		 ArrayList<ExamMonth> exam_months = (ArrayList<ExamMonth>) examMonthDao.findBySchool(school_id, 0, 99999);
//		 // Create Blank result for each Type
//		 for (ExamMonth exam_month: exam_months){
//			 if ((exam_month.getTerm_id() == null  || exam_month.getTerm_id().intValue() == 0) || // In case ReTest = > no term_id
//			     ((exam_month.getTerm_id() != null && exam_month.getTerm_id().intValue() >0 && exam_month.getTerm_id().intValue() == term.getId())))
//			 {
//				 ExamResult blank = new_blank_exam(student,class_id,term,exam_month,subject);
//				 list.add(blank);
//			 }
//			 
//		 }
//		 return list;
//	}
//	
}
