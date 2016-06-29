package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.SchoolExamDao;
import com.itpro.restws.dao.SchoolYearDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.SchoolTerm;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

@Service("classService")
@Transactional
public class ClassServiceImpl implements ClassService{

	@Autowired
	private ClassDao classDao;
	
	
	@Autowired
	private SchoolExamDao schoolExamDao;
	
	@Autowired
	protected SchoolYearDao schoolYearDao;

	
	@Override
	public EClass findById(Integer id) {
		return classDao.findById(id);
		
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		return classDao.countClassBySchool(school_id);
		
	}


	@Override
	public ArrayList<EClass> findByUser(Integer user_id, int from_num, int max_result) {
		return (ArrayList<EClass>) classDao.findByUser(user_id, from_num, max_result);
		
	}

	@Override
	public ArrayList<EClass> findBySchool(Integer school_id, int from_num, int max_result) {
		return (ArrayList<EClass>) classDao.findBySchool(school_id, from_num, max_result);
	}
	@Override
	public EClass insertClass(EClass eClass) {
		classDao.saveClass(eClass);
		return eClass;

	}

	@Override
	public EClass updateClass(EClass eClass) {
		EClass eClassDB = classDao.findById(eClass.getId());
		eClassDB = EClass.updateChanges(eClassDB, eClass);
		classDao.updateClass(eClassDB);
		return eClassDB;

		
	}

	@Override
	public ArrayList<SchoolExam> findExamOfClass(User user, Integer class_id,SchoolTerm term) {
		
	    EClass eclass = findById(class_id);
	  
	    if (eclass == null){
	    	throw new ESchoolException("class_id is not existing", HttpStatus.BAD_REQUEST);
	    }
	    if (eclass.getSchool_id().intValue() != user.getSchool_id().intValue()){
	    	throw new ESchoolException("Class.School_ID not same with current user SchoolID", HttpStatus.BAD_REQUEST);
	    }
	    
	    ArrayList<SchoolExam> all_list = (ArrayList<SchoolExam>) schoolExamDao.findBySchool(user.getSchool_id(), 0, 999999); 
	    ArrayList<SchoolExam> cls_list = new  ArrayList<SchoolExam>();
	    
		for (SchoolExam schoolExam : all_list){
			// Filter by term if required
			if (term != null){
				if (term.getTerm_val().intValue() != schoolExam.getTerm_val().intValue()){
					continue;
				}
			}
			
			String levles = schoolExam.getCls_levels()==null?"":schoolExam.getCls_levels();
			if ("--ALL--".equalsIgnoreCase(levles)){
				cls_list.add(schoolExam);
			}else{
				if (levles.indexOf("."+eclass.getLevel().intValue()+".") >= 0){
					cls_list.add(schoolExam);
				}
			}
		}
	    
		
	    return cls_list;
	}

	@Override
	public SchoolYear getSchoolYear(EClass eclass) {
		Integer year_id = eclass.getYear_id();
		
		return schoolYearDao.findById(year_id); 
	}





}
