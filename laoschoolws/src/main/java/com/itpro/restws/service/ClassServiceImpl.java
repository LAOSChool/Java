package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.SchoolExamDao;
import com.itpro.restws.dao.UserDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;

@Service("classService")
@Transactional
public class ClassServiceImpl implements ClassService{

	@Autowired
	private ClassDao classDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SchoolExamDao schoolExamDao;
	
	@Autowired
	protected SchoolYearService schoolYearService;
	@Autowired
	private SchoolTermService schoolTermService;
	
	@Autowired
	protected User2ClassService user2ClassService;
	
	
	
	@Override
	public EClass findById(Integer id) {
		EClass eclass= classDao.findById(id);
		updateTermVal(eclass);
		return eclass;
		
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		return classDao.countClassBySchool(school_id);
		
	}


	@Override
	public ArrayList<EClass> findByUser(Integer user_id, int from_num, int max_result) {
		ArrayList<EClass> list= (ArrayList<EClass>) classDao.findByUser(user_id, from_num, max_result);
		updateTermVals(list);
		return list;
		
	}

	@Override
	public ArrayList<EClass> findBySchool(Integer school_id, int from_num, int max_result) {
		ArrayList<EClass> list= (ArrayList<EClass>) classDao.findBySchool(school_id, from_num, max_result);
		updateTermVals(list);
		return list;
	}
	@Override
	public EClass newClass(User me, EClass eClass) {
		
		valid_new_class(me,eClass);
		classDao.saveClass(me,eClass);
		// assign head teacher to class auto
		assignHeadTeacher(me,eClass);
		
		updateTermVal(eClass);
		return eClass;

	}

	@Override
	public EClass updateClass(User me,EClass eClass) {
		if (eClass.getId() == null || eClass.getId().intValue() <= 0){
			throw new ESchoolException("class.id is required to update", HttpStatus.BAD_REQUEST);
		}
		EClass eClassDB = classDao.findById(eClass.getId());
		
		if (eClassDB== null){
			throw new ESchoolException("class.id is not existing", HttpStatus.BAD_REQUEST);
		}
		
		Integer old_teacher_id = eClassDB.getHead_teacher_id();
		Integer new_teacher_id = eClass.getHead_teacher_id();
		
		eClassDB = EClass.updateChanges(eClassDB, eClass);
		
		valid_new_class(me,eClass);
		
		classDao.updateClass(me,eClassDB);
		
		if (new_teacher_id == null || new_teacher_id.intValue() <=0) {
			if (old_teacher_id != null && old_teacher_id.intValue() >0){
				// Delete user2Class
				// user2ClassService.removeUserToClass(admin, old_teacher_id, eClass.getId(), "AUTO-Update Class");
				// Will not delete
			}
		}else {
			if (old_teacher_id == null || old_teacher_id.intValue() != new_teacher_id.intValue()){
				// update user2class
				assignHeadTeacher(me,eClass);
			}
		}
		updateTermVal(eClass);
		return eClassDB;

		
	}
	
	private void assignHeadTeacher(User admin, EClass eClass){
		Integer teacher_id = eClass.getHead_teacher_id();
		if (teacher_id != null && teacher_id.intValue() > 0){
			User teacher = userDao.findById(teacher_id);
			if (teacher==  null){
				throw new ESchoolException("Head teacher_id is not existing", HttpStatus.BAD_REQUEST);
			}
			if (teacher.getSchool_id().intValue() != eClass.getSchool_id().intValue()){
				throw new ESchoolException("Head teacher_id is not belong to same school_id", HttpStatus.BAD_REQUEST);
			}
			if (!teacher.hasRole((E_ROLE.TEACHER.getRole_short()))){
				throw new ESchoolException("Head teacher_id do not have TEACHER ROLE", HttpStatus.BAD_REQUEST);
			}
			user2ClassService.assignUserToClass(admin, teacher_id, eClass.getId(), "AUTO - Create Class");
			
		}
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
		
		return schoolYearService.findById(year_id); 
	}

	@Override
	public SchoolTerm getCurrentTerm(EClass eClass) {
		if (eClass == null ){
			return null;
		}
		if (eClass.getYear_id() != null && eClass.getYear_id().intValue() > 0){
			SchoolTerm term = schoolTermService.findMaxActiveTermBySchool(eClass.getSchool_id());
			return term;
		}
		return null;
	}
	
	@Override
	public void updateTermVal(EClass eClass) {
		if (eClass == null ){
			return;
		}
		if (eClass.getYear_id() != null && eClass.getYear_id().intValue() > 0){
			SchoolTerm term = schoolTermService.findMaxActiveTermBySchool(eClass.getSchool_id());
			if (term != null ){
				eClass.setTerm(term.getTerm_val());
			}
			
		}
	}

	@Override
	public void updateTermVals(ArrayList<EClass> classes) {
		if (classes == null || classes.size() <= 0 ){
			return;
		}
		EClass eClass = classes.get(0);
		SchoolTerm term = null;
		if (eClass.getYear_id() != null && eClass.getYear_id().intValue() > 0){
			term = schoolTermService.findMaxActiveTermBySchool(eClass.getSchool_id());
		}
		if (term != null ){
			for (EClass eclass : classes){
				eclass.setTerm(term.getTerm_val());
			}
		}
	}

	private void valid_new_class(User me, EClass eclass){
		
		if (eclass.getSchool_id() == null ){
			eclass.setSchool_id(me.getSchool_id());
		}
		SchoolYear schoolYear = null;
		if (eclass.getYear_id() == null ){
			schoolYear = schoolYearService.findLatestYearBySchool(eclass.getSchool_id());
		}else{
			schoolYear = schoolYearService.findById(eclass.getYear_id());
			if (schoolYear == null ){
				throw new ESchoolException("year_id is not existing", HttpStatus.BAD_REQUEST);
			}
			if (schoolYear.getSchool_id().intValue() != me.getSchool_id().intValue()){
				throw new ESchoolException("year_id is not be long to same school with me", HttpStatus.BAD_REQUEST);
			}
		}
		eclass.setYear_id(schoolYear.getId());
		eclass.setYears(schoolYear.getYears());
	}

	@Override
	public EClass delClass(User me, Integer class_id) {
		EClass eClass = classDao.findById(class_id);
		
		if (eClass.getId() == null || eClass.getId().intValue() <= 0){
			throw new ESchoolException("class.id is required to update", HttpStatus.BAD_REQUEST);
		}
		

		if (eClass.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("class.chool_id != me.school_id", HttpStatus.BAD_REQUEST);
		}
		user2ClassService.delClass(me, class_id);
		
		eClass.setActflg("D");
		classDao.updateClass(me,eClass);
		
		
		
		return eClass;
		
	

	}


}
