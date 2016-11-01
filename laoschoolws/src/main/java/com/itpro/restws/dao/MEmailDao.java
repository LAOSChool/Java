package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.MEmail;
import com.itpro.restws.model.User;


public interface MEmailDao {

	int countBySchool(Integer school_id);
	MEmail findById(Integer id);
	List<MEmail> findBySchool(Integer school_id,int from_row,int max_result) ;
	
	void saveEmail(User me,MEmail memail);
	void updateEmail(User me,MEmail memail);
	void delEmail(User me,MEmail memail);
}

