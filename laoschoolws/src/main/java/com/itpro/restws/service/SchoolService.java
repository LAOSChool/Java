package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.model.School;
import com.itpro.restws.model.User;

public interface SchoolService {
	
	School findById(Integer id);
	ArrayList<School> findActive();
	
	School insertSchool(User me,School school);
	School updateTransSchool(User me,School school);
	void saveUploadPhoto(User me, MultipartFile[] file);
	
//	SchoolTerm createTerm(User me,SchoolTerm schoolTerm);
//	SchoolTerm updateTerm(User me,SchoolTerm schoolTerm);
	
	 
}