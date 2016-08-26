package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.dao.TimetableDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.MTemplate;
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.SysTemplate;
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
	@Autowired
	private SchoolTermService schoolTermService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MasterTblService masterTblService;
	@Autowired
	private SysTblService sysTblService;
	
	
	@Autowired
	private ClassService classService;
	
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
		validateTimetable(user,timetable,true);
		timetableDao.saveTimeTable(timetable);
		return timetable;
	}

	@Override
	public Timetable updateTimetable(User me, Timetable timetable) {
		
		if (timetable.getId() == null ){
			throw new ESchoolException("timetable.id is null", HttpStatus.BAD_REQUEST);
		}
		Timetable tlb_db= timetableDao.findById(timetable.getId());
		if (tlb_db == null ){
			throw new ESchoolException("timetable.id is not exising: "+timetable.getId().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		if (tlb_db.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("timetable.SchooId is not same with user.school_id", HttpStatus.BAD_REQUEST);
		}
		
		
		
		  try {
			  timetableDao.setFlushMode(FlushMode.MANUAL);
			  tlb_db = Timetable.updateChanges(tlb_db, timetable);
			  	
			  	validateTimetable(me, timetable,false);
			  	
	        } catch (Exception e){
	        	timetableDao.clearChange();
	        	throw e;
	        }
		   finally {
			   timetableDao.setFlushMode(FlushMode.AUTO);
	        }
		  
		  timetableDao.updateTimetable(tlb_db);
		return tlb_db;
	}

	@Override
	public ArrayList<Timetable> findByDate(Integer class_id, String dt) {
		Integer dayofWeeID = 0;
		Integer dayofWee = Utils.parseWeekDay(dt);
		if (dayofWee == null || 
				dayofWee.intValue() < Calendar.SUNDAY || // 1
					dayofWee.intValue() > Calendar.SATURDAY // 7
				){
			throw new ESchoolException("Cannot parse string to date to get day of wee", HttpStatus.BAD_REQUEST);
		}
		dayofWeeID = Utils.convertDayOfWeekToSyWeekDayID(dayofWee);
		

		return (ArrayList<Timetable>) timetableDao.findByWeekDay(class_id, dayofWeeID);
		
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
	private void validateTimetable(User me, Timetable timetable, boolean is_new) {
		
				
		if (is_new){
			if (timetable.getId() != null ){
				throw new ESchoolException("timetable.id != null, cannot create new", HttpStatus.BAD_REQUEST);
			}
		}

		// Update school_id
		timetable.setSchool_id(me.getSchool_id());
		// Update school_id
				
		// check class ID
		if (timetable.getClass_id() == null || timetable.getClass_id().intValue() ==0){
			throw new ESchoolException("class_id is required", HttpStatus.BAD_REQUEST);
		}
		EClass eclass = classService.findById(timetable.getClass_id() );
		if (eclass == null){
			throw new ESchoolException("class_id is not existing", HttpStatus.BAD_REQUEST);
		}
		if (eclass.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("class_id is not belong to same school with current user", HttpStatus.BAD_REQUEST);
		}
		
		
		
		// Check teacher
		if (timetable.getTeacher_id() == null || timetable.getTeacher_id().intValue() <= 0){
			throw new ESchoolException("Teacher_id is required", HttpStatus.BAD_REQUEST);
		}
		User teacher = userService.findById(timetable.getTeacher_id());
		if (teacher == null){
			throw new ESchoolException("teacher_id is not existing", HttpStatus.BAD_REQUEST);
		}
		if (teacher.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("teacher_id not belong to same school with current user", HttpStatus.BAD_REQUEST);
		}
		// Check session
		if (timetable.getSession_id() == null || timetable.getSession_id().intValue() <= 0){
			throw new ESchoolException("Session_id is required", HttpStatus.BAD_REQUEST);
		}
		MTemplate mtemp = masterTblService.findById("m_session", timetable.getSession_id()); 
		if (mtemp == null){
			throw new ESchoolException("session_id is not existing", HttpStatus.BAD_REQUEST);
		}
		if (mtemp.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("session_id not belong to same school with current user", HttpStatus.BAD_REQUEST);
		}
		// Check Subject
		
		
		if (timetable.getSubject_id() == null || timetable.getSubject_id().intValue() <= 0){
			throw new ESchoolException("Subject_id is required", HttpStatus.BAD_REQUEST);
		}
		MTemplate msubj = masterTblService.findById("m_subject", timetable.getSubject_id()); 
		if (msubj == null){
			throw new ESchoolException("subject_id is not existing", HttpStatus.BAD_REQUEST);
		}
		if (msubj.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("subject_id not belong to same school with current user", HttpStatus.BAD_REQUEST);
		}
		
		// Check week day
		if (timetable.getWeekday_id() == null || timetable.getWeekday_id().intValue() <= 0){
			throw new ESchoolException("Weekday_id is required", HttpStatus.BAD_REQUEST);
		}
	
		
		SysTemplate sysweek = sysTblService.findByID("sys_weekday", timetable.getWeekday_id());
		if (sysweek == null){
			throw new ESchoolException("weekday_id is not existing", HttpStatus.BAD_REQUEST);
		}
		// chec school year
		
		if (timetable.getYear_id() == null || timetable.getYear_id().intValue() <= 0){
			SchoolYear schoolYear = schoolYearService.findLatestYearBySchool(me.getSchool_id());
			if (schoolYear == null ){
				throw new ESchoolException("findLatestYearBySchool is NULL", HttpStatus.BAD_REQUEST);
			}
			timetable.setYear_id(schoolYear.getId());
		}else if (!schoolYearService.valid_year_id(me.getSchool_id(), timetable.getYear_id())){
			throw new ESchoolException("Year_id is not valid or not belong to school_id", HttpStatus.BAD_REQUEST);
		}
		
		// Check term val
		
		if (timetable.getTerm_val() == null || timetable.getTerm_val().intValue() <= 0){
			// AUTO set to latest term val
			//SchoolTerm term = schoolTermService.findMaxActiveTermBySchool(timetable.getSchool_id());
			ArrayList<SchoolTerm> terms = schoolTermService.findAllTermByYear(timetable.getSchool_id(), timetable.getYear_id());
			if (terms != null && terms.size() >0 ){
				timetable.setTerm_val(terms.get(terms.size()-1).getTerm_val());	
			}else{
				throw new ESchoolException("timetable.year_id does not have any terms, year_id="+timetable.getYear_id().intValue(), HttpStatus.BAD_REQUEST);
			}
		}else if (!schoolTermService.valid_term_val(me.getSchool_id(), timetable.getYear_id(),timetable.getTerm_val())){
			throw new ESchoolException("TermVal is not belong to school_id/year_id"+me.getSchool_id().intValue()+"/"+timetable.getYear_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		
			ArrayList<Timetable> existingtbls = (ArrayList<Timetable>) timetableDao.findTimetableExt(
					timetable.getSchool_id(), 
					timetable.getClass_id(), 
					timetable.getWeekday_id(), 
					timetable.getSession_id(), 
					timetable.getYear_id(), 
					timetable.getTerm_val());
			if (existingtbls != null &&  existingtbls.size() > 0){
				boolean is_error = false;
				String err = String.format("Timetable already existing for school_id:%d///class_id:%d///weekday_id:%d///session_id:%d///year_id:%d///term_val:%d", 
						timetable.getSchool_id(), 
						timetable.getClass_id(), 
						timetable.getWeekday_id(), 
						timetable.getSession_id(), 
						timetable.getYear_id(), 
						timetable.getTerm_val());
			
				if (is_new){
					is_error = true;
				}else {
					for (Timetable tbl : existingtbls){
						if (tbl.getId().intValue() != timetable.getId().intValue()){
							is_error = true;
							break;
						}
					}
				}
				if (is_error){
					throw new ESchoolException(err, HttpStatus.BAD_REQUEST);
				}
			}
		
		
		
	}

	@Override
	public Timetable reloadTimetable(Timetable timetable) {
		Timetable tbl = null;
		if (timetable != null && timetable.getId() != null && timetable.getId().intValue() > 0){
			tbl = timetableDao.findById(timetable.getId());
			return tbl;
		}else{
			return timetable;
		}
		
	}

	@Override
	public int countTimetableExt(Integer school_id, Integer class_id,Integer weekday_id, Integer session_id,Integer year_id, Integer term_val) {
		return timetableDao.countTimetableExt(school_id, class_id, weekday_id,session_id,year_id,term_val);
	}

	@Override
	public ArrayList<Timetable> findTimetableExt(Integer school_id, Integer class_id,Integer weekday_id, Integer session_id,Integer year_id, Integer term_val){
		
		return (ArrayList<Timetable>) timetableDao.findTimetableExt(school_id, class_id, weekday_id,session_id,year_id,term_val);
	}
	
	


}
