package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.SchoolYearDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

@Service("schoolYearService")
@Transactional
public class SchoolYearServiceImpl implements SchoolYearService{

	@Autowired
	private SchoolYearDao schoolYearDao;
	
//	@Autowired
//	private SchoolTermDao schoolTermDao;

	@Autowired
	private EduProfileService eduProfileService;
	
	@Autowired
	private ClassDao classDao;
	@Override
	public SchoolYear findById(Integer id) {
		
		return schoolYearDao.findById(id);
	}



	@Override
	public SchoolYear insertSchoolYear(User me, SchoolYear schoolYear) {
		validateSchoolYear(me, schoolYear);
		schoolYearDao.saveSchoolYear(me,schoolYear);
		return schoolYear;
	}

	@Override
	public SchoolYear updateSchoolYear(User me, SchoolYear schoolYear) {
		validateSchoolYear(me, schoolYear);
		
		if (schoolYear.getId() == null ){
			throw new ESchoolException("schoolYear.id is null", HttpStatus.BAD_REQUEST);
		}
		
		
		SchoolYear year_db= schoolYearDao.findById(schoolYear.getId());
		if (year_db == null ){
			throw new ESchoolException("SchoolYear.id is not exising: "+schoolYear.getId().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		if (year_db.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("year_db.SchooId is not same with user.school_id", HttpStatus.BAD_REQUEST);
		}
		year_db = SchoolYear.updateChanges(year_db, schoolYear);
		schoolYearDao.updateSchoolYear(me,year_db);
		return year_db;
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
		SchoolYear curr = null;
		
		int this_year = Utils.getCurrentYear();
		if (list != null && list.size() > 0){
			 max = list.get(0);
			 curr = list.get(0);
			for (SchoolYear schoolYear: list){
				if (max.getId().intValue() < schoolYear.getId().intValue()){
					max = schoolYear;
				}
				if ((schoolYear.getFrom_year().intValue() <= this_year) &&
						schoolYear.getTo_year().intValue() >= this_year){
					curr = schoolYear;
					break;
				}
			}
		}
		if (curr != null ){
			return curr;
		}
		return max;
	}



	@Override
	public SchoolYear findLatestYearByStudent(Integer user_id) {
		ArrayList<SchoolYear> list = findByStudent(user_id);
		SchoolYear max = null;
		SchoolYear curr = null;
		
		int this_year = Utils.getCurrentYear();
		
		if (list != null && list.size() > 0){
			 max = list.get(0);
			 curr = list.get(0);
			for (SchoolYear schoolYear: list){
				if (max.getId().intValue() < schoolYear.getId().intValue()){
					max = schoolYear;
				}
				if ((schoolYear.getFrom_year().intValue() <= this_year) &&
						schoolYear.getTo_year().intValue() >= this_year){
					curr = schoolYear;
					break;
				}
			}
		}
		if (curr != null ){
			return curr;
		}
		return max;
	}


	@Override
	public boolean valid_year_id(Integer school_id, Integer year_id) {
		ArrayList<SchoolYear> years = (ArrayList<SchoolYear>) schoolYearDao.findBySchoolAndYear(school_id, year_id);
		if (years != null && years.size() > 0){
			return true;
		}
		return false;
	}



//	@Override
//	public boolean valid_term_val(Integer school_id, Integer year_id, Integer term_val) {
//		ArrayList <SchoolTerm> terms = findAllTermByYear(school_id, year_id);
//		if (terms != null && terms.size() > 0){
//			for (SchoolTerm term : terms){
//				if (term.getTerm_val().intValue() == term_val.intValue()){
//					return true;
//				}
//			}	
//		}
//		return false;
//		
//	}

	private void validateSchoolYear(User user, SchoolYear schoolYear){
		schoolYear.setSchool_id(user.getSchool_id());
		boolean is_new = true;
		if (schoolYear.getId() != null && schoolYear.getId().intValue() >0 ){
			is_new =false;
		}
		if (schoolYear.getFrom_year() == null || schoolYear.getFrom_year().intValue() == 0){
			throw new ESchoolException("FromYear is required", HttpStatus.BAD_REQUEST);
		}

		if (schoolYear.getTo_year() == null || schoolYear.getTo_year().intValue() == 0){
			throw new ESchoolException("ToYear is required", HttpStatus.BAD_REQUEST);
		}
		if (schoolYear.getYears() == null || schoolYear.getYears().trim().length() <=0){
			schoolYear.setYears(""+schoolYear.getFrom_year().intValue()+"-"+schoolYear.getTo_year().intValue()+"");
		}
		
		if (schoolYear.getStart_dt() == null || schoolYear.getStart_dt().trim().length() <=0){
			throw new ESchoolException("StartDate is required", HttpStatus.BAD_REQUEST);
		}
		if (is_new){
			List<SchoolYear> years = schoolYearDao.findFromOrTo(user.getSchool_id(), schoolYear.getFrom_year(), schoolYear.getTo_year());
			if (years != null && years.size() > 0){
				throw new ESchoolException("Already existing FromYear or ToYear:"+schoolYear.getFrom_year().intValue()+" OR "+schoolYear.getTo_year().intValue(), HttpStatus.BAD_REQUEST);
			}
		}
		
		
		//validate_gen_term(schoolYear,true);
	}



	@Override
	public void delSchoolYear(User me, Integer id) {
		SchoolYear schYear = findById(id);
		if (schYear == null ){
			throw new ESchoolException("id is not existing:"+id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (me.getSchool_id().intValue() != schYear.getSchool_id().intValue()){
			throw new ESchoolException("User.school_id is not same with SchoolYear.school_id", HttpStatus.BAD_REQUEST);
		}
		schYear.setActflg("D");
		schoolYearDao.updateSchoolYear(me,schYear);
		
	}


}
