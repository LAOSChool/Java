package com.itpro.restws.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.User;

public interface ExamResultDao {

	int countExamBySchool(Integer school_id);

	int countExamBySclass(Integer class_id);

	int countExamByUser(Integer user_id);

	ExamResult findById(Integer id);

	List<ExamResult> findBySchool(Integer school_id, int from_row, int max_result);

	List<ExamResult> findByClass(Integer class_id, int from_row, int max_result);

	List<ExamResult> findByStudent(Integer user_id, int from_row, int max_result);

	

	void saveExamResult(User me,ExamResult examResult);

	void updateExamResult(User me,ExamResult examResult);

	void deleteExamResult(User me,ExamResult examResult);

	

		ArrayList<ExamResult> findExamResultExt(Integer school_id, Integer class_id, Integer student_id, Integer subject_id,Integer sch_year_id);
		int  countExamResultExt(Integer school_id, Integer class_id, Integer student_id, Integer subject_id,Integer sch_year_id);
		void setFlushMode(FlushMode mode);
		void clearChange();
}
