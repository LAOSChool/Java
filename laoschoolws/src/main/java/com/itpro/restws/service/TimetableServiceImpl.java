package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.dao.TimetableDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.Timetable;
import com.itpro.restws.model.User;

@Service("timetableService")
@Transactional
public class TimetableServiceImpl implements TimetableService{

	@Autowired
	private TimetableDao timetableDao;
	@Autowired
	private MSubjectDao msubjectDao;
	
	@Autowired
	private SchoolYearService schoolYearService;
	
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
	public Timetable insertTimetable(User user, Timetable timetable) {
		validateTimetable(user,timetable);
		timetableDao.saveTimeTable(timetable);
		return timetable;
	}

	@Override
	public Timetable updateTimetable(User user, Timetable timetable) {
		validateTimetable(user,timetable);
		
//		Timetable timetableDB = timetableDao.findById(timetable.getId());
//
//		
//		if (timetableDB == null ){
//			throw new ESchoolException("Id:"+timetable.getId().intValue()+" is not exisiting", HttpStatus.BAD_REQUEST);
//		}
//		
//		if (user.getSchool_id().intValue() != timetable.getSchool_id().intValue()){
//			throw new ESchoolException("User and timetable is not in same school", HttpStatus.BAD_REQUEST);
//		}
//		
//		if (user.getSchool_id().intValue() != timetableDB.getSchool_id().intValue()){
//			throw new ESchoolException("User and timetableDB is not in same school", HttpStatus.BAD_REQUEST);
//		}
//		
//		
//		timetableDB = Timetable.updateChanges(timetableDB, timetable);
//		timetableDao.updateTimetable(timetableDB);
//		return timetableDB;
		timetableDao.updateTimetable(timetable);
		return timetable;
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

	@Override
	public ArrayList<MSubject> findSubjectOfClass(Integer class_id) {
		ArrayList<MSubject> list_sub = new ArrayList<MSubject>();
		ArrayList<Timetable> list = (ArrayList<Timetable>) timetableDao.findByClass(class_id,0, 99999);
		HashSet<String> set_sub = new HashSet<String>(); 
		for (Timetable tbl : list){
			Integer subject_id = tbl.getSubject_id();
			String key = ""+subject_id.intValue();
			if (!set_sub.contains(key)){
				set_sub.add(key);
				MSubject subject = msubjectDao.findById(subject_id);
				if (subject != null){
					list_sub.add(subject);	
				}
					
			}
			
		}
		return list_sub;
	}

	@Override
	public void delTimetableById(User user, Integer id) {
		Timetable timetable = findById(id);
		
		if (timetable == null ){
			throw new ESchoolException("Id:"+id.intValue()+" is not exisiting", HttpStatus.BAD_REQUEST);
		}
		if (user.getSchool_id().intValue() != timetable.getSchool_id().intValue()){
			throw new ESchoolException("User and timetable is not in same school", HttpStatus.BAD_REQUEST);
		}
		timetable.setActflg("D");
		timetableDao.updateTimetable(timetable);
		
	}
	private void validateTimetable(User user, Timetable timetable) {
		
		if (timetable.getId() != null && timetable.getId().intValue() > 0){
			Timetable tblDb = timetableDao.findById(timetable.getId());
			if (tblDb == null ){
				throw new ESchoolException("timetable.id is not existing:"+timetable.getId().intValue(), HttpStatus.BAD_REQUEST);
			}
			if (tblDb.getSchool_id().intValue() != user.getSchool_id().intValue() ){
				throw new ESchoolException("timetable.school_id is not the same with User", HttpStatus.BAD_REQUEST);
			}
			
			timetable = Timetable.updateChanges(tblDb, timetable);
		}
		

		// Update school_id
		//if (timetable.getSchool_id() == null ){
			timetable.setSchool_id(user.getSchool_id());
		//}
		if (timetable.getSession_id() == null || timetable.getSession_id().intValue() <= 0){
			throw new ESchoolException("Session_id is required", HttpStatus.BAD_REQUEST);
		}
		if (timetable.getSubject_id() == null || timetable.getSubject_id().intValue() <= 0){
			throw new ESchoolException("Subject_id is required", HttpStatus.BAD_REQUEST);
		}
		if (timetable.getTeacher_id() == null || timetable.getTeacher_id().intValue() <= 0){
			throw new ESchoolException("Teacher_id is required", HttpStatus.BAD_REQUEST);
		}
		if (timetable.getTerm_val() == null || timetable.getTerm_val().intValue() <= 0){
			throw new ESchoolException("TermVal is required", HttpStatus.BAD_REQUEST);
		}
		if (timetable.getWeekday_id() == null || timetable.getWeekday_id().intValue() <= 0){
			throw new ESchoolException("Weekday_id is required", HttpStatus.BAD_REQUEST);
		}
		
		if (timetable.getYear_id() == null || timetable.getYear_id().intValue() <= 0){
			throw new ESchoolException("Year_id is required", HttpStatus.BAD_REQUEST);
		}
		
		if (!schoolYearService.valid_year_id(user.getSchool_id(), timetable.getYear_id())){
			throw new ESchoolException("Year_id is not belong to school_id", HttpStatus.BAD_REQUEST);
		}
		
		if (!schoolYearService.valid_term_val(user.getSchool_id(), timetable.getYear_id(),timetable.getTerm_val())){
			throw new ESchoolException("TermVal is not belong to school_id/year_id"+user.getSchool_id().intValue()+"/"+timetable.getYear_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		
	}


}
