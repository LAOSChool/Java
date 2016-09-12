package com.itpro.restws.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.ExamRank;
import com.itpro.restws.model.User;

public interface ExamRankDao {

	int countExamBySchool(Integer school_id);

	int countExamBySclass(Integer class_id);

	int countExamByUser(Integer user_id);

	ExamRank findById(Integer id);

	List<ExamRank> findBySchool(Integer school_id, int from_row, int max_result);

	List<ExamRank> findByClass(Integer class_id, int from_row, int max_result);

	List<ExamRank> findByStudent(Integer user_id, int from_row, int max_result);

	
	void saveExamRank(User me,ExamRank examRank);

	void updateExamRank(User me,ExamRank examRank);

	void deleteExamRank(User me,ExamRank examRank);

	
		ArrayList<ExamRank> findExamRankExt(Integer school_id, Integer class_id, Integer student_id, Integer sch_year_id);
		int  countExamRankExt(Integer school_id, Integer class_id, Integer student_id,Integer sch_year_id);
		void setFlushMode(FlushMode mode);
		void clearChange();
}
