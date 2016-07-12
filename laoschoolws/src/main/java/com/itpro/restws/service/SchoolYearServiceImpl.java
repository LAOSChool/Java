package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.SchoolYearDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.SchoolTerm;
import com.itpro.restws.helper.Utils;
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
		
		int current_year = Utils.getCurrentYear();
		if (list != null && list.size() > 0){
			 max = list.get(0);
			for (SchoolYear schoolYear: list){
//				if (max.getId().intValue() < schoolYear.getId().intValue()){
//					max = schoolYear;
//				}
				if ((schoolYear.getFrom_year().intValue() <= current_year) &&
						schoolYear.getTo_year().intValue() >= current_year){
					max = schoolYear;
					break;
				}
			}
		}
		return max;
	}



	@Override
	public SchoolYear findLatestYearByStudent(Integer user_id) {
		ArrayList<SchoolYear> list = findByStudent(user_id);
		SchoolYear max = null;
		int current_year = Utils.getCurrentYear();
		
		if (list != null && list.size() > 0){
			 max = list.get(0);
			for (SchoolYear schoolYear: list){
//				if (max.getId().intValue() < schoolYear.getId().intValue()){
//					max = schoolYear;
//				}
				if ((schoolYear.getFrom_year().intValue() <= current_year) &&
						schoolYear.getTo_year().intValue() >= current_year){
					max = schoolYear;
					break;
				}
			}
		}
		return max;
	}



	@Override
	public ArrayList<SchoolTerm> findTermByYear(Integer school_id,Integer year_id) {
		ArrayList<SchoolTerm> terms = null; 
		SchoolYear schoolYear = schoolYearDao.findById(year_id);
		if (schoolYear != null ){
			
			
			Integer frm_year = schoolYear.getFrom_year();
			Integer to_year = schoolYear.getTo_year();
			Integer term_num = schoolYear.getTerm_num();
			Integer term_duration = schoolYear.getTerm_duration();
			String  start_dt = schoolYear.getStart_dt();
			
			if (
					frm_year == null ||  
					frm_year.intValue() ==0 ||
					to_year == null ||  
					to_year.intValue() ==0 ||
					term_num == null ||  // number of month
					term_num.intValue() ==0 ||
					term_duration == null ||  
					term_duration.intValue() ==0 ||
					start_dt == null
					){
				throw new ESchoolException("Must one of school year field is NULL, canont generate TERM info", HttpStatus.BAD_REQUEST);
			}
			
			if (to_year.intValue() < frm_year){
				throw new ESchoolException("Invalid to_year:"+to_year.intValue()+" < frm_year:"+frm_year.intValue(), HttpStatus.BAD_REQUEST);
			}
					
			// get total month of year
			terms = new ArrayList<SchoolTerm>();
			String term_start_date=start_dt;
			String term_end_date=Utils.addMonthToDate(term_start_date, term_duration.intValue());
			
			if (Utils.parseYear(term_end_date) > to_year){
				throw new ESchoolException("Invalid term_duration & term_num, term_end_date after > to_year "+to_year.intValue(), HttpStatus.BAD_REQUEST);
			}
			
			for (int i=0;i<  term_num.intValue();i++){
				// new term
				SchoolTerm term = new SchoolTerm();
				term.setSchool_id(school_id);
				term.setYear_id(year_id);
				term.setTerm_val(i+1);

				// Term info
				term.setStart_date(term_start_date);
				term.setEnd_date(term_end_date);
				term.setStart_year(Utils.parseYear(term_start_date));
				term.setStart_month( Utils.parseMonth(term_start_date));
				term.setEnd_year(Utils.parseYear(term_end_date));
				term.setEnd_month( Utils.parseMonth(term_end_date));
				
				
				terms.add(term);
				// Next term
				term_start_date = Utils.addDayToDate(term_end_date, 1);
				term_end_date= Utils.addMonthToDate(term_start_date, term_duration.intValue());				
				
			}
		}
		return terms;
	}

	


	@Override
	public ArrayList<SchoolTerm> findTermBySchool(Integer school_id) {
		ArrayList<SchoolYear> list_year = (ArrayList<SchoolYear>) schoolYearDao.findBySchoolId(school_id);
		ArrayList<SchoolTerm> list_term = new ArrayList<SchoolTerm>();
		
		if (list_year != null && list_year.size() > 0){
			for (SchoolYear schoolYear:list_year){
				ArrayList<SchoolTerm> terms = findTermByYear(school_id,schoolYear.getId());
				if (terms != null && terms.size() > 0){
					list_term.addAll(terms);
				}
			}
		}
		return list_term;
	}



	@Override
	public SchoolTerm findLatestTermBySchool(Integer school_id) {
		
		ArrayList<SchoolTerm> terms = findTermBySchool(school_id);
		SchoolTerm max = null;
		if (terms != null && terms.size() > 0){
			max = terms.get(0);
			for (SchoolTerm term: terms){
				if ((max.getEnd_year().intValue() <  term.getEnd_year().intValue() ) ||
				   ((max.getEnd_year().intValue() ==  term.getEnd_year().intValue()) && (max.getEnd_month().intValue() <  term.getEnd_month().intValue()) ) ||
				   (Utils.compareTo(max.getEnd_date(), term.getEnd_date()) < 0 ) ){
					max = term;
				}
			}
		}
		return max;
	}



	/***
	 * validate when create new SchoolYear
	 * @param schoolYear
	 * @return
	 */
	public boolean validate_schoolYear(SchoolYear schoolYear ) {
		if (schoolYear == null){
			throw new ESchoolException("schoolYear NULL", HttpStatus.BAD_REQUEST);
		}
		Integer school_id = schoolYear.getSchool_id();
		Integer year_id = schoolYear.getId();
		if (school_id == null){
			throw new ESchoolException("school_id NULL", HttpStatus.BAD_REQUEST);
		}
		
		if (year_id == null){
			throw new ESchoolException("year_id NULL", HttpStatus.BAD_REQUEST);
		}
		
		ArrayList<SchoolTerm> terms = null;
		
		Integer frm_year = schoolYear.getFrom_year();
		Integer to_year = schoolYear.getTo_year();
		Integer term_num = schoolYear.getTerm_num();
		Integer term_duration = schoolYear.getTerm_duration();
		String  start_dt = schoolYear.getStart_dt();
		
		if (
				frm_year == null ||  
				frm_year.intValue() ==0 ||
				to_year == null ||  
				to_year.intValue() ==0 ||
				term_num == null ||  // number of month
				term_num.intValue() ==0 ||
				term_duration == null ||  
				term_duration.intValue() ==0 ||
				start_dt == null
				){
			throw new ESchoolException("Must one of school year field is NULL, canont generate TERM info", HttpStatus.BAD_REQUEST);
		}
		
		if (to_year.intValue() < frm_year){
			throw new ESchoolException("Invalid to_year:"+to_year.intValue()+" < frm_year:"+frm_year.intValue(), HttpStatus.BAD_REQUEST);
		}
				
		// Check term start
		terms = new ArrayList<SchoolTerm>();
		String term_start_date=start_dt;
		if (Utils.parseYear(term_start_date) < frm_year){
			throw new ESchoolException("Invalid term_duration & term_num, term_start_date after < frm_year "+frm_year.intValue(), HttpStatus.BAD_REQUEST);
		}
		// Check term end
		String term_end_date=Utils.addMonthToDate(term_start_date, term_duration.intValue());
		if (Utils.parseYear(term_end_date) > to_year){
			throw new ESchoolException("Invalid term_duration & term_num, term_end_date after > to_year "+to_year.intValue(), HttpStatus.BAD_REQUEST);
		}
		
		for (int i=0;i<  term_num.intValue();i++){
			// new term
			SchoolTerm term = new SchoolTerm();
			term.setSchool_id(school_id);
			term.setYear_id(year_id);
			term.setTerm_val(i+1);

			// Term info
			term.setStart_date(term_start_date);
			term.setEnd_date(term_end_date);
			term.setStart_year(Utils.parseYear(term_start_date));
			term.setStart_month( Utils.parseMonth(term_start_date));
			term.setEnd_year(Utils.parseYear(term_end_date));
			term.setEnd_month( Utils.parseMonth(term_end_date));
			
			
			terms.add(term);
			// Next term
			term_start_date = Utils.addDayToDate(term_end_date, 1);
			term_end_date= Utils.addMonthToDate(term_start_date, term_duration.intValue());
			
			if (Utils.parseYear(term_end_date) > to_year){
				throw new ESchoolException("Invalid term_duration & term_num, term_end_date after > to_year "+to_year.intValue(), HttpStatus.BAD_REQUEST);
			}
		}
		
		if (terms != null && terms.size() > 0){
			return true;
		}
		return false;
	}


}
