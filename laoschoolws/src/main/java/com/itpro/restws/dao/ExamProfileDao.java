package com.itpro.restws.dao;

import java.util.ArrayList;

import com.itpro.restws.model.ExamProfile;


public interface ExamProfileDao {
	
	ExamProfile findByID(Integer id);
	ArrayList<ExamProfile> findBySchoolID(Integer school_id,int from_row, int max_result); 
	ArrayList<ExamProfile> findByStudentID(Integer id);
	ArrayList<ExamProfile> findByClassID(Integer id);
	void saveExamEval(ExamProfile exam_eval);
	void updateExamEval(ExamProfile exam_eval);
	
	ArrayList<ExamProfile> findExt(Integer school_id,Integer class_id, Integer student_id,Integer school_year, Integer subject_id);
}

