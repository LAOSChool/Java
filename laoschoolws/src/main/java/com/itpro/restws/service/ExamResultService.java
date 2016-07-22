package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.helper.RankInfo;
import com.itpro.restws.model.ExamRank;
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
	int countExamResultExt (Integer school_id, Integer class_id, Integer student_id,Integer subject_id,Integer year_id);
	ArrayList<ExamResult> findExamResultExt(Integer school_id, Integer class_id, Integer student_id, Integer subject_id,Integer year_id);
	ArrayList<ExamResult>  getUserProfile(User student,Integer subject_id, Integer year_id);
	ArrayList<ExamResult>  getClassProfile(Integer school_id,Integer filter_class_id,Integer filter_student_id, Integer filter_subject_id, Integer year_id);
	//void proc_average( User student,ArrayList<ExamResult> examResults);
	
	// Diem trung binh thang
	//ArrayList<RankInfo> exec_ranking(User curr_user,ArrayList<ExamResult> exam_results);
	ArrayList<ExamRank> getUserRank(User student,Integer class_id,Integer year_id);
	ArrayList<ExamRank> getClassRank(Integer class_id,Integer year_id);
	// Ranking
	// Ranking
	ExamRank execUserMonthAve(Integer user_id,Integer filter_year_id,Integer filter_class_id);
	public ArrayList<ExamRank> execClassMonthAve(User curr_user, Integer filter_year_id, Integer filter_class_id);
	//ArrayList<ExamRank> execMonthAllocation(User user,Integer school_id, Integer filter_class_id, Integer filter_year_id);
	ArrayList<ExamRank> procAllocation(User user,ArrayList<ExamRank> ranks );
	
	
	
	
	 
}