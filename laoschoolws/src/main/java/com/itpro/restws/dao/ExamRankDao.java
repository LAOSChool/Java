package com.itpro.restws.dao;

import java.util.ArrayList;
import java.util.List;

import com.itpro.restws.model.ExamRank;

public interface ExamRankDao {

	int countExamBySchool(Integer school_id);

	int countExamBySclass(Integer class_id);

	int countExamByUser(Integer user_id);

	ExamRank findById(Integer id);

	List<ExamRank> findBySchool(Integer school_id, int from_row, int max_result);

	List<ExamRank> findByClass(Integer class_id, int from_row, int max_result);

	List<ExamRank> findByStudent(Integer user_id, int from_row, int max_result);

	
	void saveExamRank(ExamRank examRank);

	void updateExamRank(ExamRank examRank);

	void deleteExamRank(ExamRank examRank);

	
		ArrayList<ExamRank> findExamRankExt(Integer school_id, Integer class_id, Integer student_id, Integer sch_year_id);
		int  countExamRankExt(Integer school_id, Integer class_id, Integer student_id,Integer sch_year_id);

}
