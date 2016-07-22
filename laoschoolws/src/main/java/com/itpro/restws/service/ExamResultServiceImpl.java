package com.itpro.restws.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.ExamRankDao;
import com.itpro.restws.dao.ExamResultDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.dao.SchoolExamDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.ExamDetail;
import com.itpro.restws.helper.ExamRankDetail;
import com.itpro.restws.helper.RankInfo;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.ExamRank;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.SchoolExam;
import com.itpro.restws.model.SchoolYear;
import com.itpro.restws.model.User;
@Service("examResultService")
@Transactional
public class ExamResultServiceImpl implements ExamResultService{
	private static final Logger logger = Logger.getLogger(ExamResultServiceImpl.class);
	@Autowired
	private ExamResultDao examResultDao;
	@Autowired
	private ExamRankDao examRankDao;
	
		
	@Autowired
	private ClassService classService;
	
	@Autowired
	private MSubjectDao msubjectDao;

	
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private SchoolYearDao schoolYearDao;
	
	@Autowired
	protected SchoolYearService schoolYearService;
	@Autowired
	private SchoolExamDao schoolExamDao;
	
	
	
	
	@Autowired
	private ClassDao classesDao;
	
	
	
	@Override
	public ExamResult findById(Integer id) {
		return examResultDao.findById(id);
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		
		return examResultDao.countExamBySchool(school_id);
	}

	@Override
	public int countByClassID(Integer class_id) {
		
		return examResultDao.countExamBySclass(class_id);
	}

	@Override
	public int countByStudentID(Integer user_id) {
		
		return examResultDao.countExamByUser(user_id);
	}

	@Override
	public ArrayList<ExamResult> findBySchoolID(Integer school_id, int from_num, int max_result) {
		
		return (ArrayList<ExamResult>) examResultDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByClassID(Integer class_id, int from_num, int max_result) {
		return (ArrayList<ExamResult>) examResultDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public ArrayList<ExamResult> findByStudentID(Integer user_id, int from_num, int max_result) {
		return (ArrayList<ExamResult>) examResultDao.findByStudent(user_id, from_num, max_result);
	}


	
	@Override
	public ExamResult updateExamResult(User teacher, ExamResult examResult) {
		ExamResult existing_db = examResultDao.findById(examResult.getId());
		existing_db = ExamResult.updateChanges(existing_db, examResult);
		examResultDao.updateExamResult(existing_db);
		return existing_db;
	}


	@Override
	public ExamResult inputExam(User teacher, ExamResult examResult) {
		// Validation Input
		validInputExam(teacher,examResult);
		
		// Update exam
		if (examResult.getId() != null && examResult.getId().intValue() > 0){
			updateExamResult(teacher,examResult);
			return examResult;
		}else{
		// Add new exam
			examResult.setId(null);// New exam
			examResultDao.saveExamResult(examResult);
			return examResult;
		}
		
	}

	
	@Override
	public void validInputExam(User teacher, ExamResult examResult) {
		// To calculate average
//		ArrayList<Float> point_months1 = new ArrayList<Float>();
//		ArrayList<Float> point_months2 = new ArrayList<Float>();
//		ArrayList<Float> point_terms1 = new ArrayList<Float>();
//		ArrayList<Float> point_terms2 = new ArrayList<Float>();
		//ArrayList<Float> point_years = new ArrayList<Float>();
		logger.info("validInputExam START");
		
		Integer school_id = teacher.getSchool_id();
		// Fix school info
		examResult.setSchool_id(school_id);
		// Check class
		if (examResult.getClass_id() == null ||examResult.getClass_id()<= 0 ){
			throw new ESchoolException("exam.class_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		com.itpro.restws.model.EClass eclass = classesDao.findById(examResult.getClass_id());
		if (eclass == null ){
			throw new ESchoolException("exam.class_id is not existing", HttpStatus.BAD_REQUEST);
		}
		if (eclass.getSchool_id().intValue() != school_id.intValue() ){
			throw new ESchoolException("exam.school_id is not similar with current user.school_id", HttpStatus.BAD_REQUEST);
		}
		// check student
		
		if (examResult.getStudent_id() == null ||examResult.getStudent_id()<= 0 ){
			throw new ESchoolException("exam.student_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		
		User student = userService.findById(examResult.getStudent_id());
		if (student == null ){
			throw new ESchoolException("exam.student_id is not existing", HttpStatus.BAD_REQUEST);
		}
		
		if (student.getSchool_id().intValue() != school_id.intValue() ){
			throw new ESchoolException("exam.student_id:"+ student.getId().intValue()+" is not belong to school_id:"+examResult.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		
		if (!student.hasRole(E_ROLE.STUDENT.getRole_short())){
			throw new ESchoolException("Exam Student is not Student role:"+examResult.getStudent_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamDao.findBySchool(school_id, 0, 999999);
		if (schoolExams == null || schoolExams.size() == 0){
			throw new ESchoolException("There isn't any SchoolExam defined for school_id:"+school_id.intValue(), HttpStatus.BAD_REQUEST);
		}

		
		examResult.setStudent_name(student.getSso_id());
		examResult.setStd_nickname(student.getNickname());
		examResult.setStd_photo(student.getPhoto());
		
		

		// Check Subject
		if (examResult.getSubject_id() == null  || examResult.getSubject_id().intValue() <=0){
			throw new ESchoolException("subject_id cannot be NULL", HttpStatus.BAD_REQUEST);
		}
		MSubject msubject = msubjectDao.findById(examResult.getSubject_id());
		if (msubject == null ){
			throw new ESchoolException("ExamResult.subject_id is not existing:"+examResult.getSubject_id().intValue(), HttpStatus.BAD_REQUEST);
		}
		if (msubject.getSchool_id().intValue() != school_id.intValue() ){
			throw new ESchoolException("Subject.School_ID is not similar with exam.School_ID", HttpStatus.BAD_REQUEST);
		}
		examResult.setSubject_name(msubject.getSval());
		
		// Check YEAR
		SchoolYear schooYear =null;
		if (examResult.getSch_year_id() == null   || examResult.getSch_year_id().intValue() <=0 ){
			schooYear = schoolYearService.findLatestYearBySchool(school_id);
			examResult.setSch_year_id(schooYear.getId());
		}else{
			schooYear = schoolYearService.findById(examResult.getSch_year_id());
			if (schooYear == null ){
				throw new ESchoolException("ExamResult.getSch_year_id is not existing:"+examResult.getSubject_id().intValue(), HttpStatus.BAD_REQUEST);
			}			
			if (schooYear.getSchool_id().intValue() != school_id.intValue()){
				throw new ESchoolException("exam.sch_year_id is not belong to this school: "+examResult.getSchool_id().intValue(),HttpStatus.BAD_REQUEST);
			}
			
		}
		
	
				
		// Check Exam 
		//boolean valid_exam = false;
//		if (examResult.getM1() == null &&
//			examResult.getM2() == null &&
//			examResult.getM3() == null &&
//			examResult.getM4() == null &&
//			examResult.getM5() == null &&
//			examResult.getM6() == null &&
//			examResult.getM7() == null &&
//			examResult.getM8() == null &&
//			examResult.getM9() == null &&
//			examResult.getM10() == null &&
//			examResult.getM11() == null &&
//			examResult.getM12() == null &&
//			examResult.getM13() == null &&
//			examResult.getM14() == null &&
//			examResult.getM15() == null &&
//			examResult.getM16() == null &&
//			examResult.getM17() == null &&
//			examResult.getM18() == null &&
//			examResult.getM19() == null &&
//			examResult.getM20() == null 
//				
//				){
//			throw new ESchoolException("Must input atleat one exam value: M1 ~ M20", HttpStatus.BAD_REQUEST);
//		}
//		
		
		java.lang.reflect.Field[] fields = examResult.getClass().getDeclaredFields();
		//SchoolExam school_exam = null;
		
        for (Field field : fields) {
            field.setAccessible(true);
            String fname =field.getName();
            
        	// Init hash
    		for (SchoolExam school_exam: schoolExams){
    			String ex_key = school_exam.getEx_key();
            //for (String key: Constant.exam_keys){
            	//if (fname.equalsIgnoreCase(key)){ // m1 ~ m20
    			if (fname.equalsIgnoreCase(ex_key)){ // m1 ~ m20
            		String sresult = null;
            		// Parse float value
            		try{
            			sresult = (String)field.get(examResult); // "8.4@2016-06-10";
            		}catch (Exception e){
            			throw new ESchoolException(fname + ": cannot get sresult, exception message: "+ e.getMessage(), HttpStatus.BAD_REQUEST);
            		}
            		if (sresult != null  && sresult.trim().length() > 0){
            			// Parsing JSON to Exam Detail
            			ExamDetail examDetail = ExamDetail.strJson2ExamDetail(sresult);
            			sresult = examDetail.getSresult();
            			
            			
                		// Get Exam master information
//                		school_exam = schoolExamDao.findByExKey(school_id, fname);// school_id vs m1,m2,m3...m20
//                		if (school_exam == null ){
//                        	throw new ESchoolException("there is no master data declared for ex_key:"+fname, HttpStatus.BAD_REQUEST);
//                        }
                		
                		// Parsing float 
                		String sval = sresult; // delete point
                		if (sval == null || sval.trim().length() == 0 ){
                			break;
                		}
                			              		
                		
                		String exam_dt="";
                		String[] arr = sresult.split("@"); // "8.4@2016-06-10";
                		if (arr != null && arr.length >=2){
                			sval = arr[0];
                			exam_dt = arr[1];
                			if (!Utils.checkDateFormat(exam_dt)){
                				throw new ESchoolException(fname + ": invalid date time format, should be YYYY-MM-DD value: "+ exam_dt, HttpStatus.BAD_REQUEST);
                			}
                		}
                		
                		Float fval = Utils.parseFloat(sval);
                	
            			if ( fval.floatValue() < school_exam.getMin().floatValue() ){
            				throw new ESchoolException(fname + ": invalid Float value: "+ sval, HttpStatus.BAD_REQUEST);
            			}
            			if ( fval.floatValue() < school_exam.getMin().floatValue() ){
            				throw new ESchoolException(fname +":"+sval+ " < MIN value = "+school_exam.getMin().floatValue(), HttpStatus.BAD_REQUEST);
            			}
            			if (fval.floatValue() > school_exam.getMax().floatValue() ){
            				throw new ESchoolException(fname + ":"+sval+" > MAX value = "+school_exam.getMax().floatValue(), HttpStatus.BAD_REQUEST);
            			}
            			
            			sval = String.format("%.1f", fval);
            			//valid_exam = true;
            			try {
            				examDetail.setSresult(sval);
            				ObjectMapper mapper = new ObjectMapper();
							field.set(examResult,  mapper.writeValueAsString(examDetail));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							throw new ESchoolException("Cannot access field of examResult object:"+e.getMessage(), HttpStatus.BAD_REQUEST);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							throw new ESchoolException("Cannot access field of examResult object:"+e.getMessage(), HttpStatus.BAD_REQUEST);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
							throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
						}
            			// /////////////////////////////////////////////////////////////////////
            			// Save to calculate average
//            			if ((school_exam.getEx_type().intValue() == 1) &&// month point
//            					(school_exam.getTerm_val().intValue() == 1)) // Hoc ky 1
//            			{
//            				point_months1.add(fval);
//            			}
//            			else if ((school_exam.getEx_type().intValue() == 1) &&// month point
//            					(school_exam.getTerm_val().intValue() == 2)) // Hoc ky 2
//            			{
//            				point_months2.add(fval);
//            			}
//            			else if ((school_exam.getEx_type().intValue() == 2) &&// Thi Hoc Ky
//            					(school_exam.getTerm_val().intValue() == 1)) // Hoc ky 1
//            			{
//            				point_terms1.add(fval);
//            			}else if ((school_exam.getEx_type().intValue() == 2) &&// Thi Hoc Ky
//            					(school_exam.getTerm_val().intValue() == 2)) // Hoc ky 2
//            			{
//            				point_terms2.add(fval);
//            			}
                		
                		
            			// /////////////////////////////////////////////////////////////////////
            		}
        			break;
            	}
            }
            
        }
//        if (!valid_exam){
//        	throw new ESchoolException("Must input atleat one exam value: M1 ~ M20", HttpStatus.BAD_REQUEST);
//        }
        
		// Check existing Exam for merger
		ArrayList<ExamResult> curr_list = (ArrayList<ExamResult>) examResultDao.findExamResultExt(
				examResult.getSchool_id(),
				null,
				examResult.getStudent_id(),
				examResult.getSubject_id(), 
				examResult.getSch_year_id());
		if (curr_list != null && curr_list.size() > 0){
			examResult.setId(curr_list.get(0).getId());
		}else{
			examResult.setId(null);
		}
		////// Calculate Average
		////////Calculate M5 ( Trung binh 4 thang  HK 1)
//		float total =0;
//		int cnt =0;
//		ExamDetail ex_detail = null;
//		ObjectMapper mapper = null;
//		if (point_months1.size() > 0){
//			total =0;
//			cnt =0;
//			for (Float fval: point_months1){
//				total += fval.floatValue();
//				cnt++;
//			}
//			float m5 = total/cnt;
//			// examResult.setM5(String.format("%.1f", m5));
//			ex_detail = new ExamDetail();
//			ex_detail.setExam_dt(Utils.now());
//			ex_detail.setNotice("AUTO");
//			ex_detail.setSresult(String.format("%.1f", m5));
//			mapper = new ObjectMapper();
//			try {
//				examResult.setM5(mapper.writeValueAsString(ex_detail));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//				throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
//			}
//			///
//			// Calculate M7 (Trung Binh Hoc Ky 1)
//			total =0;
//			cnt =0;		
//			if (point_terms1.size() > 0 ){  // Neu co diem thi HK1
//				point_terms1.add(m5);
//				for (Float fval: point_terms1){
//					total += fval.floatValue();
//					cnt++;
//				}
//				float m7 = total/cnt;
//				// examResult.setM7(String.format("%.1f", m7));
//				ex_detail = new ExamDetail();
//				ex_detail.setExam_dt(Utils.now());
//				ex_detail.setNotice("AUTO");
//				ex_detail.setSresult(String.format("%.1f", m7));
//				mapper = new ObjectMapper();
//				try {
//					examResult.setM7(mapper.writeValueAsString(ex_detail));
//				} catch (JsonProcessingException e) {
//					e.printStackTrace();
//					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
//				}
//				// Add to calculate Ave of year
//				point_years.add(m7);
//			}
//		}
//		// Calculate M12 ( Trung binh 4 thang HK2 )
//		total =0;
//		cnt =0;		
//		if (point_months2.size() > 0){
//			for (Float fval: point_months2){
//				total += fval.floatValue();
//				cnt++;
//			}
//			float m12 = total/cnt;
//			// examResult.setM12(String.format("%.1f", m12));
//			ex_detail = new ExamDetail();
//			ex_detail.setExam_dt(Utils.now());
//			ex_detail.setNotice("AUTO");
//			ex_detail.setSresult(String.format("%.1f", m12));
//			mapper = new ObjectMapper();
//			try {
//				examResult.setM12(mapper.writeValueAsString(ex_detail));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//				throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
//			}
//			
//			// Calculate M14 (Trung Binh HK 2)
//			total =0;
//			cnt =0;		
//			if (point_terms2.size() > 0 ){ // Neu co diem thi HK2
//				point_terms2.add(m12);
//				for (Float fval: point_terms2){
//					total += fval.floatValue();
//					cnt++;
//				}
//				float m14 = total/cnt;
//				// examResult.setM14(String.format("%.1f", m14));
//				ex_detail = new ExamDetail();
//				ex_detail.setExam_dt(Utils.now());
//				ex_detail.setNotice("AUTO");
//				ex_detail.setSresult(String.format("%.1f", m14));
//				mapper = new ObjectMapper();
//				try {
//					examResult.setM14(mapper.writeValueAsString(ex_detail));
//				} catch (JsonProcessingException e) {
//					e.printStackTrace();
//					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
//				}
//				// Add to calculate Ave of year								
//				point_years.add(m14);
//			}
//		}
//		
//		// Calculate M15 ( Trung binh ca nam )
//		total =0;
//		cnt =0;		
//		if (point_years.size() > 0){
//			for (Float fval: point_years){
//				total += fval.floatValue();
//				cnt++;
//			}
//			float m15 = total/cnt;
//			// examResult.setM15(String.format("%.1f", m15));
//			ex_detail = new ExamDetail();
//			ex_detail.setExam_dt(Utils.now());
//			ex_detail.setNotice("AUTO");
//			ex_detail.setSresult(String.format("%.1f", m15));
//			mapper = new ObjectMapper();
//			try {
//				examResult.setM15(mapper.writeValueAsString(ex_detail));
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//				throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
//			}			
//		}
	}

	

	@Override
	public void deleteExamResult(ExamResult exam) {
		examResultDao.deleteExamResult(exam);
		
	}



//	private ExamResult  new_blank_exam(User student, Integer class_id, SchoolTerm term,SchoolExam sch_exam, MSubject subject){
//		ExamResult blk_result= new ExamResult();
//		
//		// Fixed information
//		blk_result.setActflg("A");
//		blk_result.setCtddtm(Utils.now());
//		blk_result.setSchool_id(student.getSchool_id());
//		blk_result.setClass_id(class_id);
//		blk_result.setStudent_id(student.getId());
//		blk_result.setStudent_name(student.getSso_id());
//		blk_result.setStd_nickname(student.getNickname());
//		blk_result.setStd_photo(student.getPhoto());
//		// Month information
//		blk_result.setExam_id(sch_exam.getId());
//		blk_result.setExam_name(sch_exam.getEx_name());
//		blk_result.setExam_type(sch_exam.getEx_type());
//		blk_result.setExam_month(sch_exam.getEx_month());
//		// Subject information
//		blk_result.setSubject_id(subject.getId());
//		blk_result.setSubject(subject.getSval());
//		// Term & year information
//		//if (term != null) {
//			blk_result.setTerm_id(term.getId());
//			blk_result.setTerm_val(term.getTerm_val());
//			blk_result.setTerm(term.getTerm_name());
//			// Year information
//			blk_result.setSch_year_id(term.getSchool_year_id());
//
//		//}
//		// Notice
//		blk_result.setNotice("BLANK");
//		return blk_result;
//		
//	}
//	
//
//	@Override
//	public ArrayList<ExamResult> getUserResult_Mark(User student, Integer class_id, Integer subject_id, boolean all_term) {
//		
//		// Get current school year
////		SchoolYear school_year = schoolYearDao.findLastestOfSchoolId(student.getSchool_id());
////		if (school_year == null ){
////			// Return
////			throw new ESchoolException("Cannot find School Year for school_id:"+student.getSchool_id(),HttpStatus.BAD_REQUEST);
////
////		}
//
//		// Get current term & year
//		SchoolTerm current_term = termDao.getCurrentTerm(student.getSchool_id());
//		SchoolYear school_year = schoolYearDao.findById(current_term.getSchool_year_id());
//	
//		
//		// Get Blank ExamResult
//		MSubject tar_subject = null;
//		if (subject_id != null && subject_id.intValue() > 0){
//			tar_subject = msubjectDao.findById(subject_id);
//		}
//		
//		ArrayList<ExamResult> blank_list = iniBlankExamResults(student, class_id,tar_subject,all_term?null:current_term);
//		// Get Actual ExamResult
//		ArrayList<ExamResult> db_list = (ArrayList<ExamResult>) examResultDao.findExamResultExt(student.getSchool_id(), 0, 99999, class_id,student.getId(),subject_id, all_term?null:current_term.getId(), null, null, null, null, null, null, null,school_year.getId());
//		                                                                                       
//		// Final list
//		ArrayList<ExamResult> profile_list = new ArrayList<ExamResult>();
//		// Merge actual to Blank
//		for (ExamResult blank: blank_list){
//			ArrayList<ExamResult> founded_list = new ArrayList<ExamResult>();
//			for (ExamResult act: db_list){
//				
//				if (	(blank.getExam_id().intValue() == act.getExam_id().intValue()) &&  			// Same Exam
//						( blank.getSubject_id().intValue() == act.getSubject_id().intValue()) &&	// Same Subject
//						blank.getSch_year_id().intValue() == act.getSch_year_id().intValue() &&		// Same Year_ID
//                        blank.getExam_type().intValue() == act.getExam_type().intValue()			// Same Exam Type (Just for sure)
//						){ 
//					
//					// Check Similar Term val (for sure)
//					if (  (  (blank.getTerm_val() == null ||  blank.getTerm_val().intValue() == 0) &&   
//							 (act.getTerm_val() == null || act.getTerm_val().intValue() == 0   ) ) 
//							||
//							 (blank.getTerm_val().intValue() ==  act.getTerm_val().intValue())){
//						
//						founded_list.add(act);
//					}
//				}
//			}
//			
//			if (founded_list.size() > 0 ){
//				for (ExamResult act: founded_list){
//					profile_list.add(act);
//					db_list.remove(act);
//				}
//			}else{
//				profile_list.add(blank);
//			}
//		}
//		// Remaining DB list ( not in blank)
////		if (db_list.size() > 0){
////			for (ExamResult act: db_list){
////				profile_list.add(act);
////			}
////		}
//		
//		//calAverage(profile_list,student,school_year);
//		return profile_list;
//		
//	}
//	
	
//	private ArrayList<ExamResult> iniBlankExamResults(User student,Integer class_id,MSubject tar_subject, SchoolTerm tar_term ){
//		Integer school_id = student.getSchool_id();
//		 ArrayList<ExamResult> list = new ArrayList<ExamResult>();
//		 // Get list of exam type
//		 //ArrayList<SchoolExam> sch_exams = (ArrayList<SchoolExam>) schoolExamDao.findBySchool(school_id, 0, 99999);
//		 ArrayList<SchoolExam> class_exams = classService.findExamOfClass(student, class_id,tar_term);
//		 
//		 // List subject
//		 ArrayList<MSubject> msubjects = new ArrayList<MSubject>();
//		 if (tar_subject != null ){
//			 msubjects.add(tar_subject);
//			 
//		 }else{
//			 msubjects = timetableService.findSubjectOfClass(class_id);
//			 
//		 }
//		 
//		 // Get list of Terms
//		 ArrayList<SchoolTerm> terms = new ArrayList<SchoolTerm>();
//		 if (tar_term != null ){
//			 terms.add(tar_term);
//		 }else{
//			 terms = termDao.findBySchool(school_id, 0, 99999);
//		 }
//		 
//		 // Create Blank result for each Exam vs Subject
//		 
//		for (MSubject subject: msubjects){
//			for (SchoolExam sch_exam: class_exams){
//				// Mapping correct Term => correct Exam
//				SchoolTerm term = null;
//				if (sch_exam.getTerm_val() != null && sch_exam.getTerm_val().intValue() > 0){
//					// Thi cuoi ky 1, cuoi ky 2
//					for (SchoolTerm tmp_tmp : terms){
//						if  (tmp_tmp.getTerm_val().intValue() == sch_exam.getTerm_val().intValue()){
//							term = tmp_tmp;
//							break;
//						}
//					}
//					
//				}else{
//					term = null;
//					// Thi lai, thi tot nghiep, cuoi cap bat buoc phai la term = 2
//				}
//				
//				
//				// Create new blank
//				if (term != null) {
//					ExamResult blank_result = new_blank_exam(student, class_id, term, sch_exam, subject);
//					list.add(blank_result);
//				}
//			}
//		}
//	 
//		 return list;
//	}
//
//	@Override
//	public ArrayList<ExamResult> getClassResult_Mark(Integer school_id, Integer class_id, 	Integer subject_id,boolean all_term) {
//		// Check lass
//		EClass eclass = classesDao.findById(class_id);
//		if (eclass == null){
//			throw new ESchoolException("Cannot find Class for ID:"+class_id.intValue(), HttpStatus.BAD_REQUEST);
//		}
//		if (eclass.getSchool_id().intValue() != school_id.intValue()){
//			throw new ESchoolException("Class is not in same with School of current user:"+class_id.intValue(), HttpStatus.BAD_REQUEST);
//		}
//		// Get users		
//		ArrayList<ExamResult> list = new ArrayList<ExamResult>();
//		ArrayList<User> users = userService.findByClass(class_id, 0, 999999);
//		// Get exam result
//		for (User user : users){
//			if (user.hasRole(E_ROLE.STUDENT.getRole_short())){
//				list.addAll(getUserResult_Mark(user, class_id, subject_id,all_term));
//			}
//		}
//		return list;
//	}
//	@Override
//	public void calAverage(User student, SchoolYear schoolYear, int term_val){
//		
//		ArrayList<ExamResult> exam_results = findExamResultExt(student.getSchool_id(),0,999999, null, student.getId(), null, null, null, null, null, null, null, null, null, schoolYear.getId());
//		
//		// TB - 4 thang hoc ky 1, HK2
//		ArrayList <Integer> sub_ex_types = new ArrayList<Integer>();
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.MONTH.getValue()));
//		
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_4_MONTH.getValue(),1);
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_4_MONTH.getValue(),2);
//		// TB - Hoc ky 1, Hk2
//		sub_ex_types.clear();
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.AVE_4_MONTH.getValue()));
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.TEST_TERM.getValue()));
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_TERM.getValue(),1);
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_TERM.getValue(),2);
//		
//		// TB Year
//		sub_ex_types.clear();
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.AVE_TERM.getValue()));
//		cal_average(list,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_YEAR.getValue(),0);
//	}
//	@Override
//	public void calAverageTerm(User student, SchoolYear schoolYear, int term_val){
//		
//		ArrayList<ExamResult> exam_results = findExamResultExt(student.getSchool_id(),0,999999, null, student.getId(), null, null, null, null, null, null, null, null, null, schoolYear.getId());
//		
//		// TB - 4 thang hoc ky 1, HK2
//		ArrayList <Integer> sub_ex_types = new ArrayList<Integer>();
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.MONTH.getValue()));
//		
//		cal_average(exam_results,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_4_MONTH.getValue(),1);
//	}
//
//	@Override
//	public void calAverageYear(User student, SchoolYear schoolYear){
//		calAverageTerm(student,schoolYear,1);
//		calAverageTerm(student,schoolYear,2);
//		// TB Year
//		ArrayList<ExamResult> exam_results = findExamResultExt(student.getSchool_id(),0,999999, null, student.getId(), null, null, null, null, null, null, null, null, null, schoolYear.getId());		
//		ArrayList <Integer> sub_ex_types = new ArrayList<Integer>();
//		sub_ex_types.add(new Integer(E_EXAM_TYPE.AVE_TERM.getValue()));
//		cal_average(exam_results,student,schoolYear,sub_ex_types,E_EXAM_TYPE.AVE_YEAR.getValue(),0);
//		
//	}
//	
//	
//	private void  cal_average(ArrayList<ExamResult> list, User student, SchoolYear schoolYear,ArrayList<Integer> sum_ex_types,int ave_ex_type,int term_val){
//
//		HashMap<String, AveInfo> hashMap = new HashMap<>();
//		
//		for (ExamResult examResult : list){
//			// Check user
//			if (examResult.getStudent_id().intValue() != student.getId().intValue()){
//				continue;
//			}
//			// Check school year
//			if (	examResult.getSch_year_id() == null || 
//					examResult.getSch_year_id().intValue() == 0 || 
//					(examResult.getSch_year_id().intValue() != schoolYear.getId().intValue())){
//				continue;
//			}
//			
//			// Create HashMap with key = SubjectID
//			if (!hashMap.containsKey(""+examResult.getSubject_id().intValue())){
//				AveInfo aveInfo = new AveInfo();
//				//Ini data
//				
//				SchoolTerm sch_term = termDao.findById(examResult.getTerm_id());
//				MSubject msubject = msubjectDao.findById(examResult.getSubject_id());
//				SchoolExam ave_exam = null;
//				ArrayList<SchoolExam> sch_exams = (ArrayList<SchoolExam>) schoolExamDao.findByEx(student.getSchool_id(), ave_ex_type, term_val);
//				if (sch_exams != null && sch_exams.size() > 0){
//					ave_exam = sch_exams.get(0);
//					ExamResult blank_examResult =  new_blank_exam(student, examResult.getClass_id(), sch_term, ave_exam, msubject);
//					aveInfo.setAve_exam_result(blank_examResult);
//					aveInfo.setCnt(0);
//					aveInfo.setTotal(0);
//					aveInfo.setIs_new(1);
//					hashMap.put(""+examResult.getSubject_id().intValue(), aveInfo);
//				}
//				
//			}
//			
//			// SET MAIN EXAM TYPE IF EXIST
//			if ( (examResult.getExam_type() != null) && 
//					(examResult.getExam_type().intValue() == ave_ex_type) 
//					){
//				
//						if (term_val > 0){
//							if (    (examResult.getTerm_val() != null) &&
//									(examResult.getTerm_val().intValue() != term_val)){
//								continue;
//							}
//						}
//						if (hashMap.containsKey(""+examResult.getSubject_id().intValue())){
//							AveInfo aveInfo = hashMap.get(""+examResult.getSubject_id().intValue());
//							aveInfo.setAve_exam_result(examResult);// Replace the blank
//							aveInfo.setIs_new(0);
//						}
//			}else{
//				// Check SUB EXAM TYPE
//				boolean next = false;
//				for (Integer sub_ex_type : sum_ex_types){
//					if (sub_ex_type.intValue() == examResult.getExam_type().intValue()){
//						next = true;
//						break;
//					}
//				}
//				if (!next){
//					continue;
//				}
//				// Check term val (Optional)
//				next = true;
//				if (term_val > 0){
//					
//					if (    (examResult.getTerm_val() != null) &&
//							(examResult.getTerm_val().intValue() == term_val)){
//						next = true;
//					}
//				}
//				if (!next){
//					continue;
//				}
//				// Group by subject ID
//				Float val = Utils.parseFloat(examResult.getSresult());
//				if (val != null ){
//					if (hashMap.containsKey(""+examResult.getSubject_id().intValue())){
//						AveInfo aveInfo = hashMap.get(""+examResult.getSubject_id().intValue());
//						aveInfo.setCnt(aveInfo.getCnt()+1);
//						aveInfo.setTotal(aveInfo.getTotal()+ val);
//					}
//				}
//			}
//		}
//		 // Calculate
//		Iterator<String> keyIterator = hashMap.keySet().iterator();
//	    while (keyIterator.hasNext()) {
//	    	 String key = keyIterator.next();
//	    	 AveInfo aveInfo =  hashMap.get(key);
//	    	 if (aveInfo.getCnt() > 0){
//	    		 Float average =  aveInfo.getTotal() / aveInfo.getCnt();
//	    		 ExamResult aveExamResult = aveInfo.getAve_exam_result();
//	    		 // calculate average 
//	    		 if (aveExamResult != null ) {
//		    		 aveExamResult.setSresult(String.format("%.2f", average));
//		    		 examResultDao.saveExamResult(aveExamResult);
//	    		 }
//	    	 }
//	    	 
//	    }
//	  
//	}
	
//	private ArrayList<ExamResult> diffExamResults(ArrayList<ExamResult> list1, ArrayList<ExamResult> list2 ){
//		ArrayList<ExamResult>  diff_list = new ArrayList<ExamResult>();
//		for (ExamResult e1: list1){
//			boolean is_diff = true;
//			for (ExamResult e2: list2){
//				if (( e1.getSchool_id().intValue() == e2.getSchool_id().intValue()) &&
//				   ( e1.getClass_id().intValue() == e2.getClass_id().intValue()) &&
//				   ( e1.getStudent_id().intValue() == e2.getStudent_id().intValue()) &&
//				   ( e1.getSubject_id().intValue() == e2.getSubject_id().intValue()) &&
//				   ( e1.getExam_id().intValue() == e2.getExam_id().intValue()) &&
//				   ( e1.getSch_year_id().intValue() == e2.getSch_year_id().intValue()) &&
//				   ( e1.getTerm_val().intValue() == e2.getTerm_val().intValue()) &&
//				   ( e1.getExam_type().intValue() == e2.getExam_type().intValue()) ){
//					is_diff = false;
//					break;
//				}
//			}
//			if (is_diff){
//				diff_list.add(e1);
//			}
//		}
//		return diff_list;
//	}

//	@Override
//	public void calAverage(EClass eclass, int term_val) {
//		userService.findByClass(eclass.getId(), 0, 999999);
//		
//	}

	@Override
	public int countExamResultExt(Integer school_id, Integer class_id, Integer student_id, Integer subject_id,
			Integer year_id) {
		return examResultDao.countExamResultExt(school_id, class_id, student_id, subject_id, year_id);
		
	}

	@Override
	public ArrayList<ExamResult> findExamResultExt(Integer school_id, Integer class_id, Integer student_id,
			Integer subject_id, Integer year_id) {
		return examResultDao.findExamResultExt(school_id, class_id, student_id, subject_id, year_id);
	}

	@Override
	public ArrayList<ExamResult>  getUserProfile(User student, Integer filter_subject_id, Integer filter_year_id) {
		// lay danh sach mon hoc cua truong
		// count key: school, user, subject, year
		// Neu chua ton tai, tao new exam result
		ArrayList<ExamResult> examResults = new ArrayList<ExamResult>();
		ArrayList<MSubject> subjects = new ArrayList<MSubject>();
		
		SchoolYear curr_year = schoolYearService.findLatestYearBySchool(student.getSchool_id());
		if (filter_year_id== null || 
				(curr_year.getId().intValue() == filter_year_id.intValue())){
			filter_year_id = curr_year.getId();
			// Current profile
			if (filter_subject_id != null ){
				MSubject msubject= msubjectDao.findById(filter_subject_id);
				if (msubject == null){
					throw new ESchoolException("Invalid filter_subject_id: "+ filter_subject_id.intValue(), HttpStatus.BAD_REQUEST);
				}
				if (msubject.getSchool_id().intValue() != student.getSchool_id().intValue()){
					throw new ESchoolException("filter_subject_id is not in same school with STUDENT", HttpStatus.BAD_REQUEST);
				}
				subjects.add(msubject);
			}else{
				subjects = (ArrayList<MSubject>) msubjectDao.findBySchool(student.getSchool_id(), 0, 999999);
				if (subjects == null ||subjects.size() == 0){
					throw new ESchoolException("There is not MSubjects exam setup for school_id:"+ student.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
				}
			}
			
			
			for (MSubject subject : subjects){
				ArrayList<ExamResult> list = examResultDao.findExamResultExt(student.getSchool_id(), null, student.getId(), subject.getId(), curr_year.getId());
				if (list == null || list.size() == 0){
					ExamResult ret = new_blank_exam_result(student,subject,curr_year.getId());
					examResultDao.saveExamResult(ret);
					examResults.add(ret);
				}else{
					examResults.addAll(list);
				}
			}
		}else{
			// Past profile
			if (filter_year_id.intValue() > curr_year.getId().intValue()){
				throw new ESchoolException("filter_year_id:"+filter_year_id.intValue() +" > current year id: "+curr_year.getId().intValue(), HttpStatus.BAD_REQUEST);
			}
			examResults = examResultDao.findExamResultExt(student.getSchool_id(), null, student.getId(), null, filter_year_id);
		}
		proc_average(student,examResults);
		return examResults; 
		
	}
	private ExamResult new_blank_exam_result(User student, MSubject subject, Integer year_id){
		ExamResult exam_result = new ExamResult();
		
		exam_result.setSchool_id(student.getSchool_id());
		exam_result.setStudent_id(student.getId());
		exam_result.setStudent_name(student.getSso_id());
		exam_result.setSubject_id(subject.getId());
		exam_result.setSubject_name(subject.getSval());
		exam_result.setSch_year_id(year_id);
		return exam_result;
		
		
	}

	@Override
	public ArrayList<ExamResult> getClassProfile(Integer school_id, Integer filter_class_id, Integer filter_student_id, Integer subject_id, Integer year_id) {
		EClass filter_eclass = null;
		User filter_student = null;
		
		ArrayList<User> filter_users = new ArrayList<User>();
		// Check filter user
		if (filter_student_id != null  && filter_student_id.intValue() > 0){
			filter_student = userService.findById(filter_student_id);
			if (filter_student == null){
				throw new ESchoolException("filter_student_id is not existing", HttpStatus.BAD_REQUEST);
			}
			if (!filter_student.hasRole(E_ROLE.STUDENT.getRole_short())){
				throw new ESchoolException("filter_student_id is not STUDENT ROLE", HttpStatus.BAD_REQUEST);
			}
//			if (filter_student.getSchool_id().intValue() != teacher.getSchool_id().intValue()){
//				throw new ESchoolException("filter_student_id is not in same school with teacher", HttpStatus.BAD_REQUEST);
//			}
			filter_users.add(filter_student);
		}
		// check class
		else if (filter_class_id != null && filter_class_id.intValue() > 0){
			filter_eclass = classService.findById(filter_class_id);
			if (filter_eclass == null){
				throw new ESchoolException(" filter_class_id is not existing: "+filter_class_id.intValue(),HttpStatus.BAD_REQUEST);
			}
			
//			if (filter_eclass.getSchool_id().intValue() != school_id.intValue()){
//				if (!userService.isBelongToClass(teacher.getId(), filter_class_id)){
//	    			throw new ESchoolException("teacher ID="+teacher.getId()+" and Class are not in same school,class_id= "+filter_class_id.intValue(),HttpStatus.BAD_REQUEST);
//	    		}
//			}
			
			Set<User> tmpUsers = filter_eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
			if (tmpUsers == null || tmpUsers.size() <= 0){
				throw new ESchoolException(" filter_class_id has not STUDENT: "+filter_class_id.intValue(),HttpStatus.BAD_REQUEST);
			}
			for (User e: tmpUsers){
				filter_users.add(e);
			}
		}else{
			throw new ESchoolException("Must input filter_class_id OR filter_student_id ",HttpStatus.BAD_REQUEST);
		}
		ArrayList<ExamResult> examResults = new ArrayList<ExamResult>();
		if (filter_users != null && filter_users.size() > 0){
			for (User user: filter_users){
				ArrayList<ExamResult> list = getUserProfile(user, subject_id, year_id);
				if (list != null && list.size() > 0){
					examResults.addAll(list);
				}
			}
		}
		return examResults;
	}

	//@Override
	private void proc_average( User student, ArrayList<ExamResult> examResults){
		if (examResults == null ){
			return;
		}
		ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamDao.findBySchool(student.getSchool_id(), 0, 999999);
		if (schoolExams == null || schoolExams.size() == 0){
			throw new ESchoolException("There isn't any SchoolExam defined for school_id:"+student.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
		}

		
		for (ExamResult examResult : examResults){
			// To calculate average
			ArrayList<Float> point_months1 = new ArrayList<Float>();
			ArrayList<Float> point_months2 = new ArrayList<Float>();
			ArrayList<Float> point_terms1 = new ArrayList<Float>();
			ArrayList<Float> point_terms2 = new ArrayList<Float>();
			ArrayList<Float> point_years = new ArrayList<Float>();

			// Scall to find m1,m2,m3...m20
			java.lang.reflect.Field[] fields = examResult.getClass().getDeclaredFields();
			//SchoolExam school_exam = null;
			
	        for (Field field : fields) {
	            field.setAccessible(true);
	            String fname =field.getName();
	            
	            //for (String key: Constant.exam_keys){
	        	for (SchoolExam school_exam: schoolExams){
	    			String ex_key = school_exam.getEx_key();	            
	            	//if (fname.equalsIgnoreCase(key)){ // m1 ~ m20
	    			if (fname.equalsIgnoreCase(ex_key)){ // m1 ~ m20
	            		String sresult = null;
	            		// Parse score value
	            		try{
	            			sresult = (String)field.get(examResult); // "8.4@2016-06-10";
	            		}catch (Exception e){
	            			throw new ESchoolException(fname + ": cannot get sresult, exception message: "+ e.getMessage(), HttpStatus.BAD_REQUEST);
	            		}
	            		if (sresult != null && sresult.trim().length() > 0){
	            			// Parsing JSON to Exam Detail
	            			ExamDetail examDetail = ExamDetail.strJson2ExamDetail(sresult);
	            			sresult = examDetail.getSresult();
	            			
	                		// Get Exam master information
//	                		school_exam = schoolExamDao.findByExKey(examResult.getSchool_id(), fname);// school_id vs m1,m2,m3...m20
//	                		if (school_exam == null ){
//	                        	throw new ESchoolException("there is no master data declared for ex_key:"+fname, HttpStatus.BAD_REQUEST);
//	                		}
	                		// Parsing float 
	                		String sval = sresult;
	                		String exam_dt="";
	                		String[] arr = sresult.split("@"); // "8.4@2016-06-10";
	                		if (arr != null && arr.length >=2){
	                			sval = arr[0];
	                			exam_dt = arr[1];
	                			if (!Utils.checkDateFormat(exam_dt)){
	                				throw new ESchoolException(fname + ": invalid date time format, should be YYYY-MM-DD value: "+ exam_dt, HttpStatus.BAD_REQUEST);
	                			}
	                		}
	                		if (sval == null || sval.trim().length() == 0){
	                			break;
	                		}
	                		Float fval = Utils.parseFloat(sval);
	                		if (fval == null){
	                			throw new ESchoolException(fname + ": invalid Float value: "+ sval, HttpStatus.BAD_REQUEST);
	                		}
	                		if ( fval.floatValue() < school_exam.getMin().floatValue() ){
	            				throw new ESchoolException(fname +":"+sval+ " < MIN value = "+school_exam.getMin().floatValue(), HttpStatus.BAD_REQUEST);
	            			}
	            			if (fval.floatValue() > school_exam.getMax().floatValue() ){
	            				throw new ESchoolException(fname + ":"+sval+" > MAX value = "+school_exam.getMax().floatValue(), HttpStatus.BAD_REQUEST);
	            			}
	            			
	            			// /////////////////////////////////////////////////////////////////////
	            			// Save to calculate average
	            			if ((school_exam.getEx_type().intValue() == 1) &&// month point
	            					(school_exam.getTerm_val().intValue() == 1)) // Hoc ky 1
	            			{
	            				point_months1.add(fval);
	            			}
	            			else if ((school_exam.getEx_type().intValue() == 1) &&// month point
	            					(school_exam.getTerm_val().intValue() == 2)) // Hoc ky 2
	            			{
	            				point_months2.add(fval);
	            			}
	            			else if ((school_exam.getEx_type().intValue() == 2) &&// Thi Hoc Ky
	            					(school_exam.getTerm_val().intValue() == 1)) // Hoc ky 1
	            			{
	            				point_terms1.add(fval);
	            			}else if ((school_exam.getEx_type().intValue() == 2) &&// Thi Hoc Ky
	            					(school_exam.getTerm_val().intValue() == 2)) // Hoc ky 2
	            			{
	            				point_terms2.add(fval);
	            			}
	            			// /////////////////////////////////////////////////////////////////////

	            		}
	            		break;// STOP to next field
	            	}
	            }
	        }
	        
	        /// Start calculate average
			////////Calculate M5 ( Trung binh 4 thang  HK 1)
			float total =0;
			int cnt =0;
			ExamDetail ex_detail = null;
			ObjectMapper mapper = new ObjectMapper();;
			if (point_months1.size() > 0){
				total =0;
				cnt =0;
				for (Float fval: point_months1){
					total += fval.floatValue();
					cnt++;
				}
				float m5 = total/cnt;
				// examResult.setM5(String.format("%.1f", m5));
				ex_detail = new ExamDetail();
				ex_detail.setExam_dt(Utils.now());
				ex_detail.setNotice("AUTO");
				ex_detail.setSresult(String.format("%.1f", m5));
				//mapper = new ObjectMapper();
				try {
					examResult.setM5(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
				///
				// Calculate M7 (Trung Binh Hoc Ky 1)
				total =0;
				cnt =0;		
				if (point_terms1.size() > 0 ){  // Neu co diem thi HK1
					point_terms1.add(m5);
					for (Float fval: point_terms1){
						total += fval.floatValue();
						cnt++;
					}
					float m7 = total/cnt;
					// examResult.setM7(String.format("%.1f", m7));
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult(String.format("%.1f", m7));
					//mapper = new ObjectMapper();
					try {
						examResult.setM7(mapper.writeValueAsString(ex_detail));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					}
					// Add to calculate Ave of year
					point_years.add(m7);
				}
			}else{
				// Reset MONTH AVE
				try {
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult("");
					examResult.setM5(mapper.writeValueAsString(ex_detail));
					examResult.setM7(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
			}
			// Calculate M12 ( Trung binh 4 thang HK2 )
			total =0;
			cnt =0;		
			if (point_months2.size() > 0){
				for (Float fval: point_months2){
					total += fval.floatValue();
					cnt++;
				}
				float m12 = total/cnt;
				// examResult.setM12(String.format("%.1f", m12));
				ex_detail = new ExamDetail();
				ex_detail.setExam_dt(Utils.now());
				ex_detail.setNotice("AUTO");
				ex_detail.setSresult(String.format("%.1f", m12));
				mapper = new ObjectMapper();
				try {
					examResult.setM12(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
				
				// Calculate M14 (Trung Binh HK 2)
				total =0;
				cnt =0;		
				if (point_terms2.size() > 0 ){ // Neu co diem thi HK2
					point_terms2.add(m12);
					for (Float fval: point_terms2){
						total += fval.floatValue();
						cnt++;
					}
					float m14 = total/cnt;
					// examResult.setM14(String.format("%.1f", m14));
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult(String.format("%.1f", m14));
					mapper = new ObjectMapper();
					try {
						examResult.setM14(mapper.writeValueAsString(ex_detail));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					}
					// Add to calculate Ave of year								
					point_years.add(m14);
				}
			}else{
				// Reset month HK 2
				try {
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult("");
					examResult.setM12(mapper.writeValueAsString(ex_detail));
					examResult.setM14(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
			}
			
			// Calculate M15 ( Trung binh ca nam )
			total =0;
			cnt =0;		
			if (point_years.size() > 0){
				for (Float fval: point_years){
					total += fval.floatValue();
					cnt++;
				}
				float m15 = total/cnt;
				// examResult.setM15(String.format("%.1f", m15));
				ex_detail = new ExamDetail();
				ex_detail.setExam_dt(Utils.now());
				ex_detail.setNotice("AUTO");
				ex_detail.setSresult(String.format("%.1f", m15));
				mapper = new ObjectMapper();
				try {
					examResult.setM15(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}			
			}else{
				// Reset month HK 2
				try {
					ex_detail = new ExamDetail();
					ex_detail.setExam_dt(Utils.now());
					ex_detail.setNotice("AUTO");
					ex_detail.setSresult("");
					examResult.setM15(mapper.writeValueAsString(ex_detail));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					throw new ESchoolException("Cannot cast ExamDeail object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
				}
			}
		}
	}

	
//	private ArrayList<RankInfo> rank_user_ave(User curr_user, ArrayList<ExamResult> exam_results) {
//		
//		Hashtable<String, RankInfo> hashtables= new Hashtable<String, RankInfo>(); 
//		
//		for (ExamResult examResult: exam_results){
//			String hash_key = "";
//			RankInfo rankInfo = null;
//			Integer student_id =examResult.getStudent_id();
//			hash_key = ""+student_id.intValue()+"_";
//			
//			// Scan to find m1,m2,m3...m20
//			java.lang.reflect.Field[] fields = examResult.getClass().getDeclaredFields();
//	        for (Field field : fields) {
//	            field.setAccessible(true);
//	            String fname =field.getName();
//	            
//	            for (String ex_key: Constant.exam_keys){
//	            	if (fname.equalsIgnoreCase(ex_key)){
//	            		// Found m1 .. m 20 , create HashTable entry if need
//	            		hash_key = ""+student_id.intValue()+"_"+ex_key.toLowerCase();//user_id_m1, user_id_m2
//	            		if (!hashtables.containsKey(hash_key)){
//	            			
//	            			rankInfo = new RankInfo();
//	            			rankInfo.setEx_key(ex_key);
//	            			rankInfo.setMarks(new ArrayList<Float>());
//	            			rankInfo.setUser_id(student_id);
//	            			
//	            			hashtables.put(hash_key,rankInfo);
//	            		}else{
//	            			// Putting to array
//		            		rankInfo = hashtables.get(hash_key);	
//	            		}
//	            		// Parsing m1 ... m20 value
//	            		Float fval = null;
//	            		try {
//							fval = parseFval((String)field.get(examResult));
//						}catch (Exception e){
//	            			throw new ESchoolException(fname + ": cannot get sresult, exception message: "+ e.getMessage(), HttpStatus.BAD_REQUEST);
//	            		}
//	            		
//	            		if (fval != null ){
//	            			rankInfo.getMarks().add(fval);
//	            		}
//	            		// STOP to next field
//	            		break;
//	            	
//	            	}
//	            }
//	        }
//		}
//		 // 
//        ArrayList<RankInfo> arr_ranks = new ArrayList<RankInfo>();
//        
//        // Sorting by average
//        Enumeration<String> names = hashtables.keys();
//        
//        while(names.hasMoreElements()) {
//           String str = (String) names.nextElement();
//           RankInfo rank = hashtables.get(str);
//           ArrayList<Float> marks = rank.getMarks();
//           
//           rank.setAve(averageMarks(marks));
//           rank.setGrade(averageArade(marks));
//           // rank.Allocation only available with Class info
//           arr_ranks.add(rank);
//        }
//        
//		return arr_ranks;
//		
//	}
	private String getRade(ArrayList<Float> marks){
		float A_GRADE = 7;
		float B_BRADE = 6;
		int a_cnt =0;
		int b_cnt =0;
		if (marks != null && marks.size() > 0){
			for (Float mark: marks){
		   		   if (mark != null && mark.floatValue() >= A_GRADE){
		   			   a_cnt +=1;
		   		   }else if (mark != null && mark.floatValue() >= B_BRADE){
		   			   b_cnt +=1;
		   		   }
		   	   }
			if (a_cnt >0 && a_cnt == marks.size() ){
				return "A";
			}else if (b_cnt >0 && ((a_cnt +b_cnt) == marks.size()) ){
				return "B";
			}
		}
		return "";
		
		 
	}
		
		
	private String getAveMarks(ArrayList<Float> marks) {
		float total = 0;
		String sval=null;
		int cnt = 0;
		if (marks != null && marks.size() > 0) {
			for (Float mark : marks) {
				if (mark != null && mark.floatValue() >= 0) {
					cnt += 1;
					total += mark.floatValue();
				}
			}
			if (cnt > 0) {
				sval = String.format("%.1f", total / cnt);
				
			}
		}
		return sval;
	}
//	private Float parseFval(String sresult) {
//		String sval = "";
//		if (sresult != null ){
//			// Parsing JSON to Exam Detail
//			ExamDetail examDetail = ExamDetail.strJson2ExamDetail(sresult);
//			sval = examDetail.getSresult();
//			
//    		if (sval == null || sval.trim().length() == 0){
//    			return null;
//    		}
//    		Float fval = Utils.parseFloat(sval);
//    		if (fval == null){
//    			throw new ESchoolException("Cannot parsing Float: "+ sval, HttpStatus.BAD_REQUEST);
//    		}
//    		return fval;
//
//		}
//		return null;
//	}


	
//	private void sorting_average(ArrayList<RankInfo> ranks) {
//		if (ranks == null || ranks.size() <=0){
//			return;
//		}
//		Hashtable<String, ArrayList<RankInfo>> hashtables= new Hashtable<String, ArrayList<RankInfo>>();
//		
//		for (RankInfo rank: ranks){
//			String key = rank.getEx_key();
//			ArrayList<RankInfo> m_ranks = hashtables.get(key);
//			if (m_ranks == null){
//				m_ranks = new ArrayList<RankInfo>();
//				hashtables.put(key, m_ranks);
//			}
//			m_ranks.add(rank);
//		}
//		
//		// update allocation by month
//		// scan month
//        Enumeration<String> names = hashtables.keys();
//        while(names.hasMoreElements()) {
//           String str = (String) names.nextElement();
//           // Bet by mont
//           ArrayList<RankInfo> sub_ranks = hashtables.get(str);
//           sortRankDesc(sub_ranks);
//           
//        }
		
//	}

//	private void sortRankDesc(ArrayList<RankInfo> list) {
//		if (list == null || list.size() <=0){
//			return;
//		}
//		RankInfo tmp = null;
//		
//		for (int i = 0;i< list.size()-1 ; i++){
//			for (int j=i+1;j<list.size(); j++){
//				if (list.get(i).getAve() != null && list.get(j).getAve() != null && list.get(i).getAve().floatValue() < list.get(j).getAve().floatValue() ){
//					tmp = list.get(i);
//					list.set(i, list.get(j));
//					list.set(j, tmp);
//				}
//			}
//		}
//		for (int i = 0;i< list.size()-1 ; i++){
//			list.get(i).setAllocation(i+1);
//		}
//	}

	@Override
	public ArrayList<ExamRank> getUserRank(User student, Integer class_id, Integer year_id) {
		if (class_id == null ){
			throw new ESchoolException("class_id is null ", HttpStatus.BAD_REQUEST);
		}
		
		if (year_id == null ){
			SchoolYear schoolYear = schoolYearService.findLatestYearBySchool(student.getSchool_id());
			if (schoolYear == null ){
				throw new ESchoolException("Cannot get schoolYear of school_id:"+student.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
			}
			year_id = schoolYear.getId();
		}
		if (!student.is_belong2class(class_id)){
			throw new ESchoolException("StudentID:"+student.getId().intValue()+" is not belong to class_id:"+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		return examRankDao.findExamRankExt(student.getSchool_id(), class_id, student.getId(), year_id);
	}

	@Override
	public ArrayList<ExamRank> getClassRank(Integer class_id, Integer year_id) {
		 ArrayList<ExamRank> ret = new ArrayList<ExamRank>();
		EClass eclass = classService.findById(class_id);
		if (eclass == null) {
			throw new ESchoolException("class_id is not existing: "+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (year_id == null ){
			SchoolYear schoolYear = schoolYearService.findLatestYearBySchool(eclass.getSchool_id());
			if (schoolYear == null ){
				throw new ESchoolException("Cannot get schoolYear of school_id:"+eclass.getSchool_id().intValue(), HttpStatus.BAD_REQUEST);
			}
			year_id = schoolYear.getId();
		}
		
		Set<User> users = eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
		if (users== null || users.size() <= 0){
			throw new ESchoolException("class_id is dont have any users: "+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		
		for (User user: users){
			ArrayList<ExamRank> ranks = getUserRank(user, class_id, year_id);
			if (ranks != null && ranks.size() > 0){
				ret.addAll(ranks);
			}
		}
		return ret;
	}


//	@Override
//	public ArrayList<ExamRank> execMonthAllocation(User user, Integer filter_class_id, Integer filter_year_id) {
//		 
//
//		Integer school_id = user.getSchool_id();
//		
//		EClass eclass = classService.findById(filter_class_id);
//		if (filter_year_id == null ){
//			throw new ESchoolException("filter_year_id is required", HttpStatus.BAD_REQUEST);
//		}
//		
//		if (eclass == null ){
//			throw new ESchoolException("filter_class_id not existing:"+filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
//		}
//		if (eclass.getSchool_id().intValue() != school_id.intValue() ){
//			throw new ESchoolException("class.school_id not the same with current school_id", HttpStatus.BAD_REQUEST);
//		}
//		
//		Set<User> students = eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
//		if (students == null ){
//			throw new ESchoolException("class_id have no students:"+filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
//		}
//		// Get ave data from DB
//		ArrayList<ExamRank> exam_ranks = new ArrayList<ExamRank>();
//		
//		for (User student: students){
//			ArrayList<ExamRank> user_rank = examRankDao.findExamRankExt(school_id, filter_class_id, user.getId(), filter_year_id);
//			if (user_rank != null && user_rank.size() >0 ){
//				exam_ranks.addAll(user_rank);
//			}
//		}
//		if (exam_ranks == null || exam_ranks.size() <=0){
//			throw new ESchoolException("class_id have no exam_ranks info:"+filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
//		}
//		
//        return procAllocation(user, exam_ranks);
//	}
//	
	

	@Override
	public ArrayList<ExamRank> execClassMonthAve(User curr_user, Integer filter_year_id, Integer filter_class_id) {
		 ArrayList<ExamRank> ret = new ArrayList<ExamRank>();
		EClass eclass = classService.findById(filter_class_id);
		if (eclass == null){
			throw new ESchoolException("filter_class_id is not existing"+filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (curr_user.getSchool_id().intValue() != eclass.getSchool_id().intValue()){
			throw new ESchoolException("Class.school_id != current_user.school_id", HttpStatus.BAD_REQUEST);
		}
		
		
		Set<User> users = eclass.getUserByRoles(E_ROLE.STUDENT.getRole_short());
		if (users == null || users.size() <= 0){
			throw new ESchoolException("filter_class_id has not any user"+filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		
		for (User user: users){
			ExamRank exam_rank = execUserMonthAve(user.getId(),filter_year_id,filter_class_id);
			if (exam_rank != null && exam_rank.getId() != null && exam_rank.getId().intValue() > 0){
				ret.add(exam_rank);
			}
		}
		return ret;
	}
	@Override
	public ExamRank execUserMonthAve(Integer user_id, Integer filter_year_id, Integer filter_class_id) {
		Integer school_id = null;
		
		Hashtable<String,RankInfo> hashtable = new Hashtable<String,RankInfo>();
		if (user_id == null || user_id.intValue()==0 ){
			throw new ESchoolException("user_id is required", HttpStatus.BAD_REQUEST);
		}
		if (filter_year_id == null || filter_year_id.intValue() == 0) {
			SchoolYear schoolYear =  schoolYearService.findLatestYearBySchool(school_id);
			if (schoolYear == null ){
				throw new ESchoolException("SchoolYear of school_id is null", HttpStatus.BAD_REQUEST);
			}	
			filter_year_id = schoolYear.getId();
		}
		
		
		
		User user = userService.findById(user_id);
		if (user == null ){
			throw new ESchoolException("user_id is not existing" + user_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		if (!user.hasRole(E_ROLE.STUDENT.getRole_short()) ){
			throw new ESchoolException("user_id is not STUDENT" + user_id.intValue(), HttpStatus.BAD_REQUEST);		
			
		}
		if (!user.is_belong2class(filter_class_id)){
			throw new ESchoolException("user_id is not belong to class_id" + filter_class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		
		ArrayList<ExamResult> examResults = findExamResultExt(user.getSchool_id(), null, user_id, null, filter_year_id);
		if (examResults == null ){
			throw new ESchoolException("There isn't any exam result for user_id:"+user_id.intValue()+" and year_id:"+filter_year_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		school_id = user.getSchool_id();
		ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamDao.findBySchool(school_id, 0, 999999);
		if (schoolExams == null || schoolExams.size() == 0){
			return null;
		}
		// Init hash
		for (SchoolExam schoolExam: schoolExams){
			String ex_key = schoolExam.getEx_key();
			RankInfo rank_info = new RankInfo();

			/////////
			rank_info.setAllocation(null);
			rank_info.setAve(null);
			rank_info.setGrade(null);
			rank_info.setExam_rank_id(null);
			rank_info.setMarks(new ArrayList<Float>());			
			rank_info.setUser_id(user_id);
			rank_info.setYear_id(filter_year_id);
			rank_info.setEx_key(ex_key);			
			// put to hash
			hashtable.put(ex_key, rank_info);
		}
		
		 // calculate average
		ArrayList<ExamRank> exam_ranks  = examRankDao.findExamRankExt(school_id, null, user.getId(), filter_year_id);
        ExamRank exam_rank = null;
        if (exam_ranks == null ||exam_ranks.size() <=0){
        	exam_rank = new ExamRank();
        	
        	exam_rank.setSchool_id(user.getSchool_id());
        	exam_rank.setClass_id(filter_class_id);
        	exam_rank.setStudent_id(user_id);
        	exam_rank.setSch_year_id(filter_year_id);
        	
        }else{
        	exam_rank = exam_ranks.get(0);
        }
        
        Enumeration<String> keys = hashtable.keys();
        
        while(keys.hasMoreElements()) {
           String ex_key = (String) keys.nextElement();
           RankInfo rankInfo = hashtable.get(ex_key);
           ArrayList<Float> marks = getMarkByExKeys(user,ex_key,examResults);
           
           rankInfo.setMarks(marks);
           rankInfo.setAve(getAveMarks(marks));
           rankInfo.setGrade(getRade(marks));

           
           // Merge rankInfo to Rank
           ObjectMapper mapper = new ObjectMapper();
           java.lang.reflect.Field[] fields = exam_rank.getClass().getDeclaredFields();
	   		for (Field field : fields) {
	   			field.setAccessible(true);
	            String fname =field.getName();
	            if (fname.equalsIgnoreCase(ex_key)){
	            	try {
						field.set(exam_rank,  mapper.writeValueAsString(rankInfo));
					 } catch (IllegalArgumentException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot access field of rankInfo object:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot access field of rankInfo object:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						throw new ESchoolException("Cannot cast rankInfo object ot JSON String:"+e.getMessage(), HttpStatus.BAD_REQUEST);
					}
	            }
	            
	   		}
           
           
        }
      // save db
		if (exam_rank != null ){
			examRankDao.saveExamRank(exam_rank);
		}
		return exam_rank;
	}
	
	private ArrayList<Float> getMarkByExKeys(User user, String ex_key, ArrayList<ExamResult> examResults){
		ArrayList<Float> marks = new ArrayList<>();
		
		
		for (ExamResult examResult: examResults){
			if (examResult.getStudent_id().intValue() != user.getId().intValue()){
				continue;
			}
			java.lang.reflect.Field[] fields = examResult.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
	            String fname =field.getName();
	            if (fname.equalsIgnoreCase(ex_key)){
	            	String sresult = null;
            		// Parse float value
            		try{
            			sresult = (String)field.get(examResult);// {"sresult":"4.5","notice":"test","exam_dt":"2016-07-05 15:36:07"}
            		}catch (Exception e){
            			throw new ESchoolException(fname + ": cannot get sresult, exception message: "+ e.getMessage(), HttpStatus.BAD_REQUEST);
            		}
            		if (sresult != null && sresult.trim().length() > 0){
            			// Parsing JSON to Exam Detail
            			ExamDetail examDetail = ExamDetail.strJson2ExamDetail(sresult);
                		Float fval = Utils.parseFloat(examDetail.getSresult());
                		if (fval != null && fval >=0){
                			marks.add(fval);
                		}
            		
            		}
	            	break;
	            }
			}
		}
		
		
		
		return marks;
	}
	@Override
	public ArrayList<ExamRank> procAllocation(User user,ArrayList<ExamRank> exam_ranks){
		Integer school_id = user.getSchool_id();
		// Initial hashtable
		Hashtable<String, ArrayList<RankInfo>> hashtables= new Hashtable<String, ArrayList<RankInfo>>();
		
		 // List exam : m1 .. m20
		 ArrayList<SchoolExam> schoolExams = (ArrayList<SchoolExam>) schoolExamDao.findBySchool(school_id, 0, 999999);
		 for (SchoolExam schoolExam :schoolExams){
			 String ex_key = schoolExam.getEx_key();
			 if (ex_key == null || ex_key.equals("")){
				 continue;
			 }
			 ex_key = ex_key.toLowerCase();
			 // Put to hash table
			 hashtables.put(ex_key,  new ArrayList<RankInfo>());
		 }
				
				
		// Put DB to hashtable
		for (ExamRank examRank : exam_ranks){
			java.lang.reflect.Field[] fields = examRank.getClass().getDeclaredFields();
	        for (Field field : fields) {
	            field.setAccessible(true);
	            String fname =field.getName();
	            String sresult= "";
	            ArrayList<RankInfo> arr_rankinfo = hashtables.get(fname.toLowerCase());// m1...m20
	            if (arr_rankinfo != null ){
					try {
						sresult = (String) field.get(examRank);//{"ave":"4,5","grade":"A","allocation":1}
					} catch (IllegalArgumentException e) {
						
						e.printStackTrace();
						throw new ESchoolException(fname + ": cannot GET to field.get(examRank) :"+ e.getMessage(), HttpStatus.BAD_REQUEST);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						throw new ESchoolException(fname + ": cannot GET to field.get(examRank) :"+ e.getMessage(), HttpStatus.BAD_REQUEST);
					}
	            	if (sresult != null && !sresult.equals("")){
	            		ExamRankDetail rank_detail = ExamRankDetail.strJson2ExamDetail(sresult);//{"ave":"4,5","grade":"A","allocation":1}
	            		if (rank_detail != null && rank_detail.getAve() != null){
	            			RankInfo rankinfo = new RankInfo();
	            			
	            			// Detail info from JSON String
	            			rankinfo.setAllocation(rank_detail.getAllocation());
	            			rankinfo.setAve(rank_detail.getAve());
	            			rankinfo.setGrade(rank_detail.getGrade());
	            			/// other info - may be used
	            			rankinfo.setExam_rank_id(examRank.getId());
	            			rankinfo.setEx_key(fname);
	            			rankinfo.setUser_id(examRank.getStudent_id());
	            			rankinfo.setYear_id(examRank.getSch_year_id());
	            			// add to array to sort
	            			arr_rankinfo.add(rankinfo);
	            		}
	            	}
	            	
	            }
	        }
		}
				
		// Scan hashtable => arraylist => sort
        Enumeration<String> names = hashtables.keys();
        while(names.hasMoreElements()) {
           String ex_key = (String) names.nextElement();
           ArrayList<RankInfo> arr_infos = hashtables.get(ex_key);
           if (arr_infos != null && arr_infos.size() > 0){
        	   if (arr_infos.size() > 1){
        		   for (int i =0;i< arr_infos.size() -1;i++){
            		   float ave_i = arr_infos.get(i).getAve() != null?Float.valueOf(arr_infos.get(i).getAve()):0;
            			  // scan for max
            		   for (int j=i+1;j < arr_infos.size();j++){
            			   float ave_j = arr_infos.get(j).getAve() != null?Float.valueOf(arr_infos.get(j).getAve()):0;   
                		   // found max
                		   if (ave_i < ave_j ){
                			   // Doi cho
                			   RankInfo min = arr_infos.get(i);
                			   RankInfo max = arr_infos.get(j);
                			   arr_infos.set(i, max);
                			   arr_infos.set(j, min);
                		   }
            		   }
            		  
            	    }
        	   	  }
        	   }
           }
		           
		 
        ArrayList<ExamRank> ret_list = new ArrayList<ExamRank>();
		           
        // Update allocation
        names = hashtables.keys(); // m1,m2..
           while(names.hasMoreElements()) {
        	   String ex_key = (String) names.nextElement();
        	   ArrayList<RankInfo> arr_infos = hashtables.get(ex_key);
              if (arr_infos != null && arr_infos.size() > 0){
           	   for (int i =0;i< arr_infos.size();i++){
           		   
           		   RankInfo rank_info = arr_infos.get(i);  // Subject: {"ave":"4,5","grade":"A","allocation":1 - and oher infor}
           		   
           		   RankInfo prev_info = null;
           		   if (i> 0){
           			prev_info = arr_infos.get(i-1);
           		   }

           		   // Reload Exam Rank
           		   ExamRank examRank = examRankDao.findById(rank_info.getExam_rank_id());
           		   
           		   if (examRank != null ){
           			   // New allocation 
           			   rank_info.setAllocation(i+1);
	           			if (prev_info != null ){
	        				   if (		rank_info.getAve() != null && 
	        						   	rank_info.getAve().trim().length() > 0 &&
	        						   	// Compare with previous
	        						   	prev_info != null  && 
	        						   	prev_info.getAve() != null && 
	        						   	prev_info.getAve().trim().length() > 0)
	        				   {
	        					   // Samve allocation
	        					   if (Float.valueOf(rank_info.getAve()).floatValue() == Float.valueOf(prev_info.getAve()).floatValue()){
	        						   rank_info.setAllocation(prev_info.getAllocation());   
	        					   }
	        				   }
	        			   }
           			
           			   // Create ExamRankDetail
               		   ExamRankDetail rank_detail = new ExamRankDetail();
               		   
               		   rank_detail.setAve(rank_info.getAve());
               		   rank_detail.setGrade(rank_info.getGrade());
               		   rank_detail.setAllocation(rank_info.getAllocation());
               		   
               		   ObjectMapper mapper = new ObjectMapper();
               		   String jsonstr= null; 
               		   try {
               			   jsonstr =  mapper.writeValueAsString(rank_detail); //JSON STRING:  {"ave":"4,5","grade":"A","allocation":1}
               		   } catch (JsonProcessingException e) {
               			   e.printStackTrace();
               			   throw new ESchoolException("Cannot convert Object (rank_detail) to JSON STRING:"+e.getMessage(), HttpStatus.BAD_REQUEST);
               		   }
               		   
               		   // Save JSON to String and store into ExamRank           		   
               		   if (jsonstr != null && jsonstr.length() > 0){
               			java.lang.reflect.Field[] fields = examRank.getClass().getDeclaredFields();
	                    for (Field field : fields) {
	                        field.setAccessible(true);
	                        String fname =field.getName();
	                        	if (fname.equalsIgnoreCase(ex_key)){ // m1 ~ m20
	                        		try {
										field.set(examRank, jsonstr);
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
										throw new ESchoolException("Cannot update filed(examRank,jsonstr):"+e.getMessage(), HttpStatus.BAD_REQUEST);
									} catch (IllegalAccessException e) {
										e.printStackTrace();
										throw new ESchoolException("Cannot update filed(examRank,jsonstr):"+e.getMessage(), HttpStatus.BAD_REQUEST);
									}
	                        		break;
	                        	}
	                        }
               		   	}
               		   // Update to DB
               		   examRankDao.updateExamRank(examRank);
               		   ret_list.add(examRank);
           		   }
           	   }
           }
        }
        return ret_list;
	}
}
