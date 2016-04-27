package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MSubject;
public interface MSubjectDao {
	int countBySchool(Integer school_id);
	MSubject findById(Integer id);
	List<MSubject> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveSubject(MSubject msubject);
	void updateSubject(MSubject msubject);
}
