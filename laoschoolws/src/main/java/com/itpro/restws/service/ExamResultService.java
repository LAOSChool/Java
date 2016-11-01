package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.ExamRank;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.User;

public interface ExamResultService {
	

	ExamResult findById(User me,Integer id);
	int countBySchoolID(Integer school_id);
	int countByClassID(Integer class_id);
	int countByStudentID(Integer user_id);
	// Get list result
	ArrayList<ExamResult> findBySchoolID(Integer school_id,int from_num, int max_result);
	ArrayList<ExamResult> findByClassID(Integer class_id,int from_num, int max_result);
	ArrayList<ExamResult> findByStudentID(Integer user_id,int from_num, int max_result);
	// Input/delete
	void deleteExamResult(User me,ExamResult exam);
	void validInputExam(User teacher, ExamResult exam);
	//ExamResult updateExamResult_detach(User teacher,ExamResult examResult); 
	ExamResult inputExam(User teacher,ExamResult examResult);
	// User Profile
	int countExamResultExt (Integer school_id, Integer class_id, Integer student_id,Integer subject_id,Integer year_id);
	ArrayList<ExamResult> findExamResultExt(User me, Integer school_id, Integer class_id, Integer student_id, Integer subject_id,Integer year_id);
	ArrayList<ExamResult>  getUserProfile(User me, User student,Integer subject_id, Integer year_id);
	ArrayList<ExamResult>  getClassProfile(User me, Integer school_id,Integer filter_class_id,Integer filter_student_id, Integer filter_subject_id, Integer year_id);
	//void proc_average( User student,ArrayList<ExamResult> examResults);
	
	// Ranking
	ArrayList<ExamRank> getUserRank(User me, User student,Integer class_id,Integer year_id);
	ArrayList<ExamRank> getClassRank(User me, Integer class_id,Integer year_id);
	ExamRank execUserMonthAve(User me, Integer user_id,Integer filter_year_id, Integer filter_class_id, String filter_ex_key);
	
	public ArrayList<ExamRank> execClassMonthAve(User me, Integer filter_class_id, String filter_ex_key);
	ArrayList<ExamRank> procAllocation(User me,ArrayList<ExamRank> ranks );
	
	void orderExamResultByID(ArrayList<ExamResult> list, int order);	
	void orderRankByID(ArrayList<ExamRank> list, int order);
	void orderRankByAllocation(User me, ArrayList<ExamRank> list, String ex_key);
	String valid_rank_process(User me,  String class_ids, String ex_key);
	boolean is_inputted(ExamResult examResult, String ex_key);	
	boolean is_completed(Integer school_id, Integer class_id, Integer subject_id,String ex_key);
	 
}