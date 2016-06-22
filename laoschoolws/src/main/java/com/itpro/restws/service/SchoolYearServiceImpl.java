package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.SchoolYearDao;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.SchoolYear;

@Service("schoolYearService")
@Transactional
public class SchoolYearServiceImpl implements SchoolYearService{

	@Autowired
	private SchoolYearDao schoolYearDao;

	@Autowired
	private EduProfileService eduProfileService;
	
	@Autowired
	private ClassDao classDao;
	@Override
	public SchoolYear findById(Integer id) {
		
		return schoolYearDao.findById(id);
	}



	@Override
	public SchoolYear insertSchoolYear(SchoolYear schoolYear) {
		schoolYearDao.saveSchoolYear(schoolYear);
		return schoolYear;
	}

	@Override
	public SchoolYear updateSchoolYear(SchoolYear schoolYear) {
		SchoolYear db = findById(schoolYear.getId());
		db = SchoolYear.updateChanges(db, schoolYear);
		schoolYearDao.updateSchoolYear(db);
		return db;
	}



	@Override
	public ArrayList<SchoolYear> findBySchool(Integer school_id) {
		
		return (ArrayList<SchoolYear>) schoolYearDao.findBySchoolId(school_id);
	}



	@Override
	public ArrayList<SchoolYear> findByStudent(Integer user_id) {
		return eduProfileService.findSchoolYearByStudentID(user_id);
		
	}



	@Override
	public SchoolYear findByClass(Integer class_id) {
		EClass eclass = classDao.findById(class_id);
		return findById(eclass.getYear_id());
	}



	@Override
	public SchoolYear findLatestYearBySchool(Integer school_id) {
		ArrayList<SchoolYear> list = findBySchool(school_id);
		SchoolYear max = null;
		if (list != null && list.size() > 0){
			 max = list.get(0);
			for (SchoolYear schoolYear: list){
				if (max.getId().intValue() < schoolYear.getId().intValue()){
					max = schoolYear;
				}
			}
		}
		return max;
	}



	@Override
	public SchoolYear findLatestYearByStudent(Integer user_id) {
		ArrayList<SchoolYear> list = findByStudent(user_id);
		SchoolYear max = null;
		if (list != null && list.size() > 0){
			 max = list.get(0);
			for (SchoolYear schoolYear: list){
				if (max.getId().intValue() < schoolYear.getId().intValue()){
					max = schoolYear;
				}
			}
		}
		return max;
	}





}
