package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.EClass;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

public interface ExamResultService {
	

	ExamResult findById(Integer id);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	int countByStudentID(Integer user_id);
	// Get list result
	ArrayList<ExamResult> findBySchoolID(Integer school_id,int from_num, int max_result);
	ArrayList<ExamResult> findByClassID(Integer class_id,int from_num, int max_result);
	ArrayList<ExamResult> findByStudentID(Integer user_id,int from_num, int max_result);

	// Input/delete
	void deleteExamResult(ExamResult exam);
	void validInputExam(User teacher, ExamResult exam);
	ExamResult updateExamResult(User teacher,ExamResult examResult); 
	ExamResult inputExam(User teacher,ExamResult examResult);
	// User Profile
//	ArrayList<ExamResult> findUserProfile(User user,Integer class_id);
	//public ArrayList<ExamResult> findUserProfile_Up(User user, Integer class_id);
	
	
	ArrayList<ExamResult>  getUserResult_Mark(User student, Integer class_id, Integer subject_id,boolean all_term);
	ArrayList<ExamResult>  getClassResult_Mark(Integer school_id, Integer class_id, Integer subject_id,boolean all_term);
	
	//	ArrayList<ExamResult> findByUser(User user, int from_num, int max_result);
	int countExamResultExt(Integer school_id,
			Integer class_id, 
			Integer student_id, 
			Integer subject_id,
			Integer term_id,
			Integer exam_year,
			Integer exam_month,
			String exam_dt,
			String dateFrom, 
			String dateTo,
			Integer from_row_id,
			Integer exam_type,
			Integer sch_year_id
			);
	ArrayList<ExamResult>  findExamResultExt(Integer school_id, int from_row, int max_result,
			Integer class_id, 
			Integer student_id, 
			Integer subject_id,
			Integer term_id,
			Integer exam_year,
			Integer exam_month,
			String exam_dt,
			String dateFrom, 
			String dateTo,
			Integer from_row_id,
			Integer exam_type,
			Integer sch_year_id);
	
	//void initStudentExamResult(User user,Integer class_id);
	 void calAverageTerm(User student, SchoolYear schoolYear,int term_val);
	 void calAverageYear(User student, SchoolYear schoolYear);
	 void calAverage(EClass eclass,int term_val);
	
	 
}