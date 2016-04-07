package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ExamResultDao;
import com.itpro.restws.model.ExamResult;

@Service("examResultService")
@Transactional
public class ExamResultServiceImpl implements ExamResultService{

	@Autowired
	private ExamResultDao examResultDao;

	@Override
	public ExamResult findById(int id) {
		
		return examResultDao.findById(id);
	}

	@Override
	public int countBySchoolID(int school_id) {
		
		return examResultDao.countExamBySchool(school_id);
	}

	@Override
	public int countByClassID(int class_id) {
		
		return examResultDao.countExamBySclass(class_id);
	}

	@Override
	public int countByStudentID(int user_id) {
		
		return examResultDao.countExamByUser(user_id);
	}

	@Override
	public ArrayList<ExamResult> findBySchool(int school_id, int from_num, int max_result) {
		
		return (ArrayList<ExamResult>) examResultDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByClass(int class_id, int from_num, int max_result) {
		return (ArrayList<ExamResult>) examResultDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByStudent(int user_id, int from_num, int max_result) {
		return (ArrayList<ExamResult>) examResultDao.findByStudent(user_id, from_num, max_result);
	}

	@Override
	public ExamResult insertExamResult(ExamResult examResult) {
		examResultDao.saveExamResult(examResult);
		return examResult;
	}

	@Override
	public ExamResult updateExamResult(ExamResult examResult) {
		ExamResult examResultDB = examResultDao.findById(examResult.getId());
		examResultDB = ExamResult.updateChanges(examResultDB, examResult);
		examResultDao.updateExamResult(examResultDB);
		return examResultDB;
	
	}
	

	
	


}
