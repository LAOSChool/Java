package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.SchoolDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.model.School;
import com.itpro.restws.model.Timetable;

@Service("schoolService")
@Transactional
public class SchoolServiceImpl implements SchoolService{

	@Autowired
	private SchoolDao schoolDao;

	

	@Override
	public School findById(Integer id) {
		
		return schoolDao.findById(id);
	}

	@Override
	public ArrayList<School> findActive() {
		//throw new ESchoolException(" aaaa abc",HttpStatus.BAD_REQUEST);
		//throw new ExceptionMethodNotAllowed("testabc");
		return (ArrayList<School>) schoolDao.findAll();
	}

	@Override
	public School insertSchool(School school) {
		schoolDao.saveSchool(school);
		return school;
	}

	@Override
	public School updateSchool(School school) {
		School schoolDB = schoolDao.findById(school.getId());
		schoolDB = School.updateChanges(schoolDB, school);
		schoolDao.updateSchool(schoolDB);
		return schoolDB;
	}




}
