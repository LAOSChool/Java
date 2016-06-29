package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.ExamResult;
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
//	ArrayList<ExamResult>  getUserResult_Mark(User student, Integer class_id, Integer subject_id,boolean all_term);
//	ArrayList<ExamResult>  getClassResult_Mark(Integer school_id, Integer class_id, Integer subject_id,boolean all_term);
	int countExamResultExt (Integer school_id, Integer class_id, Integer student_id,Integer subject_id,Integer year_id);
	ArrayList<ExamResult> findExamResultExt(Integer school_id, Integer class_id, Integer student_id, Integer subject_id,Integer year_id);
//	 void calAverageTerm(User student, SchoolYear schoolYear,int term_val);
//	 void calAverageYear(User student, SchoolYear schoolYear);
//	 void calAverage(EClass eclass,int term_val);
	ArrayList<ExamResult>  getUserProfile(User student,Integer subject_id, Integer year_id);
	ArrayList<ExamResult>  getClassProfile(User teacher,Integer filter_class_id,Integer filter_student_id, Integer filter_subject_id, Integer year_id);
	void proc_average( ArrayList<ExamResult> examResults);
	 
}