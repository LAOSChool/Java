package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.model.EClass;

@Service("classService")
@Transactional
public class ClassServiceImpl implements ClassService{

	@Autowired
	private ClassDao classDao;

	@Override
	public EClass findById(int id) {
		return classDao.findById(id);
		
	}

	@Override
	public int countBySchoolID(int school_id) {
		return classDao.countClassBySchool(school_id);
		
	}


	@Override
	public ArrayList<EClass> findByUser(int user_id, int from_num, int max_result) {
		return (ArrayList<EClass>) classDao.findByUser(user_id, from_num, max_result);
		
	}

	@Override
	public ArrayList<EClass> findBySchool(int school_id, int from_num, int max_result) {
		return (ArrayList<EClass>) classDao.findBySchool(school_id, from_num, max_result);
	}
	@Override
	public EClass insertClass(EClass eClass) {
		classDao.saveClass(eClass);
		return eClass;

	}

	@Override
	public EClass updateClass(EClass eClass) {
		EClass eClassDB = classDao.findById(eClass.getId());
		eClassDB = EClass.updateChanges(eClassDB, eClass);
		classDao.updateClass(eClassDB);
		return eClassDB;

		
	}





}
