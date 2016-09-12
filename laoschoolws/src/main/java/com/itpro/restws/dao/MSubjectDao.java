package com.itpro.restws.dao;

import java.util.ArrayList;

import org.hibernate.FlushMode;

import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.User;
public interface MSubjectDao {
	int countBySchool(Integer school_id);
	MSubject findById(Integer id);
	ArrayList<MSubject> findBySchool(Integer school_id,int from_row,int max_result) ;
	void saveSubject(User me,MSubject msubject);
	void updateSubject(User me,MSubject msubject);
	void delSubject(User me,MSubject msubject);
	void setFlushMode(FlushMode mode);
	void clearChange();
}
