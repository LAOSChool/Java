package com.itpro.restws.service;

import java.util.ArrayList;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.SchoolTermDao;
import com.itpro.restws.dao.SchoolYearDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_TERM_STS;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

@Service("schoolTermService")
@Transactional
public class SchoolTermServiceImpl implements SchoolTermService{

	@Autowired
	private SchoolYearDao schoolYearDao;
	
	@Autowired
	private SchoolTermDao schoolTermDao;


	
	@Override
	public SchoolTerm findById(Integer id) {
		
		return schoolTermDao.findById(id);
	}



	@Override
	public SchoolTerm insertSchoolTerm(User me, SchoolTerm schoolTerm) {
		if (schoolTerm.getId() != null ){
			throw new ESchoolException("Cannot create new term when id != null", HttpStatus.BAD_REQUEST);
		}
		validTerm(me,schoolTerm,true);
		schoolTermDao.saveSchoolTerm(me,schoolTerm);
		return schoolTerm;
	}



	@Override
	public SchoolTerm updateTransSchoolTerm(User me, SchoolTerm schoolTerm) {
		
		if (schoolTerm.getId() == null ){
			throw new ESchoolException("schoolTerm.id is null", HttpStatus.BAD_REQUEST);
		}
		SchoolTerm term_db= schoolTermDao.findById(schoolTerm.getId());
		if (term_db == null ){
			throw new ESchoolException("SchoolTerm.id is not exising: "+schoolTerm.getId().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		if (term_db.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("term_db.SchooId is not same with user.school_id", HttpStatus.BAD_REQUEST);
		}
		
		// check INACTIVE cannot active again
		if (term_db.getActived().intValue() == E_TERM_STS.INACTIVE.getValue()){
			if (schoolTerm.getActived() != null  && schoolTerm.getActived().intValue() != term_db.getActived().intValue() ){
				throw new ESchoolException("INACTIVE term cannot be change to ACTIVE or PENDING", HttpStatus.BAD_REQUEST);
			}
		}
		
		  try {
			  	schoolTermDao.setFlushMode(FlushMode.MANUAL);
			  	term_db = SchoolTerm.updateChanges(term_db, schoolTerm);
			  	
			  	validTerm(me, term_db,false);
			  	
	        } catch (Exception e){
	        	schoolTermDao.clearChange();
	        	throw e;
	        }
		   finally {
	        	schoolTermDao.setFlushMode(FlushMode.AUTO);
	        }
		  
		
		schoolTermDao.updateSchoolTerm(me,term_db);
		return term_db;
	}



	@Override
	public ArrayList<SchoolTerm> findBySchool(Integer school_id) {
		
		return (ArrayList<SchoolTerm>) schoolTermDao.findBySchoolId(school_id,null);
	}

	@Override
	public ArrayList<SchoolTerm> findAllTermByYear(Integer school_id,Integer year_id) {
		 
		if (year_id == null ){
			throw new ESchoolException("findAllTermByYear(): year_id is null", HttpStatus.BAD_REQUEST);
		}
		if (school_id == null ){
			throw new ESchoolException("findAllTermByYear(): school_id is null", HttpStatus.BAD_REQUEST);
		}
		SchoolYear schoolYear = schoolYearDao.findById(year_id);
		if (schoolYear == null ){
			throw new ESchoolException("findAllTermByYear(): year_id not existing", HttpStatus.BAD_REQUEST);
		}
		if (schoolYear.getSchool_id().intValue() != school_id.intValue()){
			throw new ESchoolException("findAllTermByYear(): schoolYear.school_id != me.school_id", HttpStatus.BAD_REQUEST);
		}
		return (ArrayList<SchoolTerm>) schoolTermDao.findBySchoolAndYear(school_id, schoolYear.getId(),null);
	}

	
	@Override
	public ArrayList<SchoolTerm> findAllTermBySchool(Integer school_id) {
		ArrayList<SchoolYear> list_year = (ArrayList<SchoolYear>) schoolYearDao.findBySchoolId(school_id);
		ArrayList<SchoolTerm> list_term = new ArrayList<SchoolTerm>();
		
		if (list_year != null && list_year.size() > 0){
			for (SchoolYear schoolYear:list_year){
				ArrayList<SchoolTerm> terms = findAllTermByYear(school_id,schoolYear.getId());
				if (terms != null && terms.size() > 0){
					list_term.addAll(terms);
				}
			}
		}
		return list_term;
	}



	@Override
	public SchoolTerm findMaxActiveTermBySchool(Integer school_id) {

		ArrayList<SchoolTerm> terms = findAllTermBySchool(school_id);
		SchoolTerm max = null;
		if (terms != null && terms.size() > 0){
			for (SchoolTerm term: terms){
				if (term.getActived().intValue() == E_TERM_STS.ACTIVE.getValue()){
					max = term;
					break;
				}
			}
			// Neu ko tim thay Active term thi tim max term
			if (max == null ) {
				max = terms.get(0);
				for (SchoolTerm term: terms){
					if (	

							 max.getTerm_val().intValue() < term.getTerm_val().intValue()
					    )
					{
						max = term;
					}
				}
			}
			
			
		}
		return max;		
	}
	



	void validTerm(User me, SchoolTerm new_term, boolean is_new){
		if (is_new ){
			// Check ID
			if (new_term.getId() != null){
				throw new ESchoolException("Term.id is NOT NULL, cannot create new", HttpStatus.BAD_REQUEST);
			}
			// Check school_id	
			if (new_term.getSchool_id() == null){
				new_term.setSchool_id(me.getSchool_id());
			}else{
				if (me.getSchool_id().intValue() != new_term.getSchool_id().intValue()){
					throw new ESchoolException("Term.school_id != user.school_id", HttpStatus.BAD_REQUEST);
				}
			}
		}
		// Check Year_ID
		SchoolYear school_year = null;
		if (new_term.getYear_id() != null ){
			school_year = schoolYearDao.findById(new_term.getYear_id());	
		}
		
		if (school_year == null){
			throw new ESchoolException("Term.year_id is required or not exisiting", HttpStatus.BAD_REQUEST);
		}
		if (school_year.getSchool_id().intValue() != new_term.getSchool_id().intValue()){
			throw new ESchoolException("Term.school_id != year.school_id", HttpStatus.BAD_REQUEST);
		}
		// Check start_DT/End_DT
		if (new_term.getStart_dt() == null){
			throw new ESchoolException("Term.start_dt is null", HttpStatus.BAD_REQUEST);
		}
		if (new_term.getEnd_dt() == null){
			throw new ESchoolException("Term.end_dt is null", HttpStatus.BAD_REQUEST);
		}
		
		if (Utils.compareTo(new_term.getEnd_dt(),new_term.getStart_dt()) <= 0){
			throw new ESchoolException("Term.start_dt >= term.end_dt", HttpStatus.BAD_REQUEST);
		}
		Integer term_start_year = Utils.parseYear(new_term.getStart_dt());
		Integer term_end_year = Utils.parseYear(new_term.getEnd_dt());
		if (school_year.getFrom_year() != null &&
				(term_start_year.intValue() <  school_year.getFrom_year().intValue())
				)
		{
			throw new ESchoolException("Term.START_YEAR="+term_start_year+" is < SchoolYear.FromYear="+school_year.getFrom_year().intValue(), HttpStatus.BAD_REQUEST);
		}
		if (school_year.getTo_year() != null &&
				(term_end_year.intValue() >  school_year.getTo_year().intValue())
				)
		{
			throw new ESchoolException("Term.END_YEAR="+term_end_year+" is > SchoolYear.ToYear="+school_year.getTo_year().intValue(), HttpStatus.BAD_REQUEST);
		}

		
		ArrayList<SchoolTerm> exit_list = findAllTermByYear( new_term.getSchool_id(), new_term.getYear_id());
		if (exit_list !=  null && exit_list.size() > 0){
			for (SchoolTerm tmp: exit_list){
				// Check term Val already existing
				if (is_new){
					if (	(tmp.getTerm_val().intValue() == new_term.getTerm_val().intValue()) &&
							(tmp.getSchool_id().intValue() == new_term.getSchool_id().intValue()) &&
							(tmp.getYear_id().intValue() == new_term.getYear_id().intValue() )
							)
					{
						throw new ESchoolException("Same term_val="+new_term.getTerm_val().intValue()+ " already existing", HttpStatus.BAD_REQUEST);	
					}
				}
			
				// Check already ACTIVED
				else if (new_term.getActived() != null  && new_term.getActived().intValue() == E_TERM_STS.ACTIVE.getValue()){
					if (tmp.getActived().intValue() ==  E_TERM_STS.ACTIVE.getValue()){
						if (	is_new || 
								( tmp.getId() != null && new_term.getId() != null && 
								  tmp.getId().intValue() != new_term.getId().intValue())
						)
						{
							throw new ESchoolException("Already actived Term is existing. Cannot create two active terms at same time", HttpStatus.BAD_REQUEST);
						}
					}
				}
			}
		}
		// Check Active				
		if (is_new){
			if (new_term.getActived() == null){
				new_term.setActived(E_TERM_STS.PENDING.getValue());
			}else if (new_term.getActived().intValue() ==  E_TERM_STS.INACTIVE.getValue()){
				throw new ESchoolException("New TERM cannot be INACTIVE", HttpStatus.BAD_REQUEST);
			}
		}

		if (new_term.getActived().intValue() == E_TERM_STS.INACTIVE.getValue() ||
				new_term.getActived().intValue() == E_TERM_STS.ACTIVE.getValue() ||
				new_term.getActived().intValue() == E_TERM_STS.PENDING.getValue() ){
			// Do nothing
		}else{
			throw new ESchoolException("term.actived must be one of : INACTIVE(0)/ACTIVE(1)/PENDING(2)", HttpStatus.BAD_REQUEST);
		}
	
		
	}



	@Override
	public SchoolTerm findTermById(User me, Integer term_id) {
		SchoolTerm term = schoolTermDao.findById(term_id);
		if (term == null ){
			throw new ESchoolException("term_id"+term_id.intValue()+" is not existing", HttpStatus.BAD_REQUEST);
		}
		if (term.getSchool_id().intValue() != me.getSchool_id().intValue() ){
			throw new ESchoolException("term_id"+term_id.intValue()+" is not in same school with current user", HttpStatus.BAD_REQUEST);
		}
		return term;
	}



	@Override
	public void delSchoolTerm(User me, Integer id) {
		SchoolTerm term = schoolTermDao.findById(id);
		if (term == null ){
			throw new ESchoolException("term_id"+id.intValue()+" is not existing", HttpStatus.BAD_REQUEST);
		}
		if (term.getSchool_id().intValue() != me.getSchool_id().intValue() ){
			throw new ESchoolException("term_id"+id.intValue()+" is not in same school with current user", HttpStatus.BAD_REQUEST);
		}
		term.setActflg("D");
		schoolTermDao.updateSchoolTerm(me,term);
		
	}



	@Override
	public SchoolTerm activeSchoolTerm(User me, Integer term_id, Integer active) {
		if (term_id == null ){
			throw new ESchoolException("term_id is null", HttpStatus.BAD_REQUEST);
		}
		SchoolTerm termDB = schoolTermDao.findById(term_id);
		if (termDB == null ){
			throw new ESchoolException("term_id"+term_id.intValue()+" is not existing", HttpStatus.BAD_REQUEST);
		}
		if (termDB.getSchool_id().intValue() != me.getSchool_id().intValue() ){
			throw new ESchoolException("term_id"+term_id.intValue()+" is not in same school with current user", HttpStatus.BAD_REQUEST);
		}
		
		if (active == null || active.intValue() < 0 || active.intValue() > 1){
			throw new ESchoolException("must input active = 0 or 1", HttpStatus.BAD_REQUEST);
		}
		// Check duplicated active
		if (active.intValue() == E_TERM_STS.ACTIVE.getValue()){
			ArrayList<SchoolTerm> terms = (ArrayList<SchoolTerm>) schoolTermDao.findBySchoolId(me.getSchool_id(),1);
			if (terms.size() > 0){
				for (SchoolTerm tmp: terms){
					if (tmp.getId().intValue() == term_id.intValue()){
						return tmp;
					}else{
						throw new ESchoolException("Cannot having two actived term at same time, already existing actived term_id ="+tmp.getId().intValue(), HttpStatus.BAD_REQUEST);
					}
				}
			}
		}
		// Disable inactive => active
		if (termDB.getActived().intValue() ==   E_TERM_STS.INACTIVE.getValue()){
			throw new ESchoolException("INACTIVE term cannot be change to ACTIVE or PENDING", HttpStatus.BAD_REQUEST);
		}
		termDB.setActived(active);
		schoolTermDao.updateSchoolTerm(me,termDB);
		return termDB;
	}

	@Override
	public boolean valid_term_val(Integer school_id, Integer year_id, Integer term_val) {
		ArrayList <SchoolTerm> terms = findAllTermByYear(school_id, year_id);
		if (terms != null && terms.size() > 0){
			for (SchoolTerm term : terms){
				if (term.getTerm_val().intValue() == term_val.intValue()){
					return true;
				}
			}	
		}
		return false;
		
	}


	@Override
	public ArrayList<SchoolTerm> findTermExt(User me, Integer filter_year_id, Integer filter_actived) {
		validGetTerm(me,filter_year_id);
		return (ArrayList<SchoolTerm>) schoolTermDao.findBySchoolAndYear(me.getSchool_id(), filter_year_id, filter_actived);
	}
	
	void validGetTerm(User me, Integer year_id){
		if (year_id != null  && year_id.intValue() > 0){
			SchoolYear year = schoolYearDao.findById(year_id);
			if (year == null ){
				throw new ESchoolException("year_id:"+year_id.intValue() + " is not existing", HttpStatus.BAD_REQUEST);
			}
			if (year.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("year_id:"+year_id.intValue() + " is not belong to same school with user", HttpStatus.BAD_REQUEST);
			}
		}
	}

}
