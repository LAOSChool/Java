package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ExamResultDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.User;

@Service("examResultService")
@Transactional
public class ExamResultServiceImpl implements ExamResultService{

	@Autowired
	private ExamResultDao examResultDao;

	@Autowired
	private UserService userService;
	
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
	public ArrayList<ExamResult> findBySchool(Integer school_id, int from_num, int max_result) {
		
		return (ArrayList<ExamResult>) examResultDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByClass(Integer class_id, int from_num, int max_result) {
		return (ArrayList<ExamResult>) examResultDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByStudent(Integer user_id, int from_num, int max_result) {
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
	public int countExamResultExt(Integer school_id, Integer class_id, Integer user_id, Integer subject_id,
			Integer term_id, Integer exam_year, Integer exam_month, String exam_dt, String dateFrom, String dateTo,
			Integer from_row_id) {
		return examResultDao.countExamResultExt(school_id, class_id, user_id, subject_id, term_id, exam_year, exam_month, exam_dt, dateFrom, dateTo, from_row_id);

	}

	@Override
	public ArrayList<ExamResult> findExamResultExt(Integer school_id, int from_row, int max_result, Integer class_id,
			Integer user_id, Integer subject_id, Integer term_id, Integer exam_year, Integer exam_month,
			String exam_dt, String dateFrom, String dateTo, Integer from_row_id) {
		return examResultDao.findExamResultExt(school_id, from_row, max_result, class_id, user_id, subject_id, term_id, exam_year, exam_month, exam_dt, dateFrom, dateTo, from_row_id);
	}

	@Override
	public void deleteExamResult(ExamResult exam) {
		examResultDao.deleteExamResult(exam);
		
	}

	


}
