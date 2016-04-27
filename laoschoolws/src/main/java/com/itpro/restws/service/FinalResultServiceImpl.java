package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.FinalResultDao;
import com.itpro.restws.model.FinalResult;

@Service("finalResultService")
@Transactional
public class FinalResultServiceImpl implements FinalResultService{

	@Autowired
	private FinalResultDao finalResultDao;

	@Override
	public FinalResult findById(Integer id) {
		
		return finalResultDao.findById(id);
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		
		return finalResultDao.countBySchool(school_id);
	}

	@Override
	public int countByClassID(Integer class_id) {
		
		return finalResultDao.countByClass(class_id);
	}

	@Override
	public int countByStudentID(Integer user_id) {
		
		return finalResultDao.countByUser(user_id);
	}

	@Override
	public ArrayList<FinalResult> findBySchool(Integer school_id, int from_num, int max_result) {
		
		return (ArrayList<FinalResult>) finalResultDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<FinalResult> findByClass(Integer class_id, int from_num, int max_result) {
		return (ArrayList<FinalResult>) finalResultDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public ArrayList<FinalResult> findByStudent(Integer user_id, int from_num, int max_result) {
		return (ArrayList<FinalResult>) finalResultDao.findByStudent(user_id, from_num, max_result);
	}

	@Override
	public FinalResult insertFinalResult(FinalResult finalResult) {
		finalResultDao.saveFinalResult(finalResult);
		return finalResult;
	}

	@Override
	public FinalResult updateFinalResult(FinalResult finalResult) {
		FinalResult finalResultDB = finalResultDao.findById(finalResult.getId());
		finalResultDB = FinalResult.updateChanges(finalResultDB, finalResult);
		finalResultDao.updateFinalResult(finalResultDB);
		return finalResultDB;
		
	}

	
	


}
