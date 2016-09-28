package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.SchoolExamDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.User;

@Service("schoolExamService")
@Transactional
public class SchoolExamServiceImpl implements SchoolExamService{

	@Autowired
	private SchoolExamDao schoolExamDao;

	@Override
	public SchoolExam findById(Integer id) {
		return schoolExamDao.findById(id);
	
	}

	@Override
	public ArrayList<SchoolExam> findBySchool(Integer school_id) {
		return (ArrayList<SchoolExam>) schoolExamDao.findBySchool(school_id, 0, 999999);
	}

	@Override
	public SchoolExam insertSchoolExam(User me, SchoolExam schoolExam) {
		if (schoolExam.getId() != null ){
			throw new ESchoolException("schoolExam.id must be NULL to create", HttpStatus.BAD_REQUEST);
		}
		validateSchoolExam(me,schoolExam);
		schoolExamDao.saveSchoolExam(me,schoolExam);
		return schoolExam;
		
	}

	@Override
	public SchoolExam updateSchoolExam(User me, SchoolExam schoolExam) {
		if (schoolExam.getId() == null || schoolExam.getId().intValue() == 0  ){
			throw new ESchoolException("schoolExam.id not exist", HttpStatus.BAD_REQUEST);
		}
		
		validateSchoolExam(me,schoolExam);
		
		
		if (schoolExam.getId() != null && schoolExam.getId().intValue() >0 ){
			//update
			SchoolExam ex_db= schoolExamDao.findById(schoolExam.getId());
			if (ex_db == null ){
				throw new ESchoolException("SchoolExam.id is not exising: "+schoolExam.getId().intValue(), HttpStatus.BAD_REQUEST);
			}
			
			if (ex_db.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("ex_db.SchooId is not same with user.school_id", HttpStatus.BAD_REQUEST);
			}
			SchoolExam.updateChanges(ex_db, schoolExam);
			schoolExamDao.updateSchoolExam(me,ex_db);
			return ex_db;
		}else{
			throw new ESchoolException("SchoolExam.id is NULL", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public void delById(User me, Integer id) {
		SchoolExam ex = schoolExamDao.findById(id);
		if (ex == null ){
			throw new ESchoolException("Delete SchoolExam.ID is not existing", HttpStatus.BAD_REQUEST);
		}
		if (ex.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("Delete SchoolExam.school_id is not same with user.school_id", HttpStatus.BAD_REQUEST);
		}
		ex.setActflg("D");
		schoolExamDao.updateSchoolExam(me,ex);
		
	}

	
	private void validateSchoolExam(User user, SchoolExam schoolExam){
		
		schoolExam.setSchool_id(user.getSchool_id());
		if (schoolExam.getId() != null && schoolExam.getId().intValue() >0 ){
//			//update
//			SchoolExam ex_db= schoolExamDao.findById(schoolExam.getId());
//			if (ex_db == null ){
//				throw new ESchoolException("SchoolExam.id is not exising: "+schoolExam.getId().intValue(), HttpStatus.BAD_REQUEST);
//			}
//			
//			if (ex_db.getSchool_id().intValue() != user.getSchool_id().intValue()){
//				throw new ESchoolException("ex_db.SchooId is not same with user.school_id", HttpStatus.BAD_REQUEST);
//			}
//			schoolExam = SchoolExam.updateChanges(ex_db, schoolExam);
		}else{
			// new
			if (schoolExam.getEx_key() == null || schoolExam.getEx_key().trim().length() == 0){
				throw new ESchoolException("ExKey is required (m1,m2....m20)", HttpStatus.BAD_REQUEST);
			}
			SchoolExam ex_db= schoolExamDao.findByExKey(schoolExam.getSchool_id(), schoolExam.getEx_key());
			if (ex_db != null ){
				throw new ESchoolException("Dublicated school_id/ex_key:"+user.getSchool_id().intValue()+"/"+schoolExam.getEx_key(), HttpStatus.BAD_REQUEST);
			}
		}
		
		
		if (schoolExam.getTerm_val() == null || schoolExam.getTerm_val().intValue() == 0){
			throw new ESchoolException("Term val is required", HttpStatus.BAD_REQUEST);
		}
		if (schoolExam.getEx_type() == null || schoolExam.getEx_type().intValue() == 0){
			throw new ESchoolException("ExType is required(1: Month Exam, 2: Term Contest, 3: Ave 4 months , 4:  Ave Term, 5: Ave year, 6: retest, 7: final level contest)", HttpStatus.BAD_REQUEST);
		}

		if (schoolExam.getEx_name() == null || schoolExam.getEx_name().trim().length() == 0){
			throw new ESchoolException("ExName is required", HttpStatus.BAD_REQUEST);
		}
		if (schoolExam.getEx_key() == null || schoolExam.getEx_key().trim().length() == 0){
			throw new ESchoolException("ExKey is required (m1,m2....m20)", HttpStatus.BAD_REQUEST);
		}
		boolean valid_ex_key = false;
		for (String key: Constant.exam_keys){
			if (schoolExam.getEx_key().equals(key)){
				valid_ex_key = true;
				break;
			}
		}
		if (!valid_ex_key){
			throw new ESchoolException("ExKey is not correct, only accept value from: m1,m2....m20 ", HttpStatus.BAD_REQUEST);
		}
		boolean valid_ex_type = false;
		for (int ex_tp: Constant.exam_types){
			if (schoolExam.getEx_type().intValue() == ex_tp){
				valid_ex_type = true;
				break;
			}
		}
		if (!valid_ex_type){
			throw new ESchoolException("ExType is not correct, only accept: (1: Month Exam, 2: Term Contest, 3: Ave 4 months , 4:  Ave Term, 5: Ave year, 6: retest, 7: final level contest)", HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public boolean valid_ex_key(User me, String ex_key) {
		Integer school_id = me.getSchool_id();
		SchoolExam exam = schoolExamDao.findByExKey(school_id, ex_key);
		if (exam == null ){
			return false;
		}
		return true;
	}

}
