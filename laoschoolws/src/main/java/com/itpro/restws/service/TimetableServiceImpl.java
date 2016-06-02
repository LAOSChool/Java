package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.TimetableDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Timetable;

@Service("timetableService")
@Transactional
public class TimetableServiceImpl implements TimetableService{

	@Autowired
	private TimetableDao timetableDao;

	@Override
	public Timetable findById(Integer id) {
		
		return timetableDao.findById(id);
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		
		return timetableDao.countBySchool(school_id);
	}

	
	@Override
	public ArrayList<Timetable> findBySchool(Integer school_id, int from_num, int max_result) {
		
		return (ArrayList<Timetable>) timetableDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<Timetable> findByClass(Integer class_id, int from_num, int max_result) {
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

	@Override
	public ArrayList<Timetable> findByDate(Integer class_id, String dt) {
		
		Integer dayofWee = Utils.parseWeekDay(dt);
		if (dayofWee == null || 
				dayofWee.intValue() < Calendar.SUNDAY || // 1
					dayofWee.intValue() > Calendar.SATURDAY // 7
				){
			throw new ESchoolException("Cannot parse string to date to get day of wee", HttpStatus.BAD_REQUEST);
		}
		return (ArrayList<Timetable>) timetableDao.findByWeekDay(class_id, dayofWee);
	}


}
