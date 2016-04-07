package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MSubject;
public interface MSubjectDao {
	int countBySchool(int school_id);
	MSubject findById(int id);
	List<MSubject> findBySchool(int school_id,int from_row,int max_result) ;
	void saveSubject(MSubject msubject);
	void updateSubject(MSubject msubject);
}
