package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.TimetableDao;
import com.itpro.restws.model.Timetable;

@Service("timetableService")
@Transactional
public class TimetableServiceImpl implements TimetableService{

	@Autowired
	private TimetableDao timetableDao;

	@Override
	public Timetable findById(int id) {
		
		return timetableDao.findById(id);
	}

	@Override
	public int countBySchoolID(int school_id) {
		
		return timetableDao.countBySchool(school_id);
	}

	
	@Override
	public ArrayList<Timetable> findBySchool(int school_id, int from_num, int max_result) {
		
		return (ArrayList<Timetable>) timetableDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<Timetable> findByClass(int class_id, int from_num, int max_result) {
		return (ArrayList<Timetable>) timetableDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public Timetable insertTimetable(Timetable timetable) {
		timetableDao.saveTimeTable(timetable);
		return timetable;
	}

	@Override
	public Timetable updateTimetable(Timetable timetable) {
		Timetable timetableDB = timetableDao.findById(timetable.getId());
		timetableDB = Timetable.updateChanges(timetableDB, timetable);
		timetableDao.updateTimetable(timetableDB);
		return timetableDB;
		
		
	}


}
