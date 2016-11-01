package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.EClass;
import com.itpro.restws.model.User;


public interface ClassDao {

	int countClassBySchool(Integer school_id);
	EClass findById(Integer id);
	List<EClass> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<EClass> findByUser(Integer user_id,int from_row,int max_result) ;
	
	void saveClass(User me, EClass eClass);
	void updateClass(User me, EClass eClass);
	void setFlushMode(FlushMode mode);
	void clearChange();
	List<EClass> findActiveBySchool(Integer school_id) ;
}

