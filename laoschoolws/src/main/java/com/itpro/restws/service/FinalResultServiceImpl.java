package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ExamMonthDao;
import com.itpro.restws.dao.ExamProfileDao;
import com.itpro.restws.dao.ExamResultDao;
import com.itpro.restws.dao.FinalResultDao;
import com.itpro.restws.dao.MSubjectDao;
import com.itpro.restws.dao.StudentProfileDao;
import com.itpro.restws.dao.TermDao;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.ExamProfile;
import com.itpro.restws.model.FinalResult;
import com.itpro.restws.model.MSubject;
import com.itpro.restws.model.StudentProfile;
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.User;

@Service("finalResultService")
@Transactional
public class FinalResultServiceImpl implements FinalResultService{

	@Autowired
	private FinalResultDao finalResultDao;
	@Autowired
	private StudentProfileDao studentProfileDao;
	
	@Autowired
	private ExamResultDao examResultDao;
	
	@Autowired
	private TermDao termDao;
	
	@Autowired
	private ExamMonthDao examMonthDao;
	@Autowired
	private MSubjectDao msubjectDao;
	
	@Autowired
	private ExamProfileDao examProfileDao;
	
	@Override
	public FinalResult findById(Integer id) {
		
		return finalResultDao.findById(id);
	}

	@Override
	public int countBySchoolID(Integer school_id) {
		
		return finalResultDao.countBySchool(school_id);
	}

	@Override
	public int countByClassID(Integer class_id) {
		
		return finalResultDao.countByClass(class_id);
	}

	@Override
	public int countByStudentID(Integer user_id) {
		
		return finalResultDao.countByUser(user_id);
	}

	@Override
	public ArrayList<FinalResult> findBySchool(Integer school_id, int from_num, int max_result) {
		
		return (ArrayList<FinalResult>) finalResultDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<FinalResult> findByClass(Integer class_id, int from_num, int max_result) {
		return (ArrayList<FinalResult>) finalResultDao.findByClass(class_id, from_num, max_result);
	}

	@Override
	public ArrayList<FinalResult> findByStudent(Integer user_id, int from_num, int max_result) {
		return (ArrayList<FinalResult>) finalResultDao.findByStudent(user_id, from_num, max_result);
	}

	@Override
	public FinalResult insertFinalResult(FinalResult finalResult) {
		finalResultDao.saveFinalResult(finalResult);
		return finalResult;
	}

	@Override
	public FinalResult updateFinalResult(FinalResult finalResult) {
		FinalResult finalResultDB = finalResultDao.findById(finalResult.getId());
		finalResultDB = FinalResult.updateChanges(finalResultDB, finalResult);
		finalResultDao.updateFinalResult(finalResultDB);
		return finalResultDB;
		
	}

	@Override
	public ArrayList<StudentProfile> findUserProfile(User student, Integer school_year_id) {
		ArrayList<StudentProfile> list = studentProfileDao.findEx(student.getId(), null, null, school_year_id);
		
		return list;
	}
	@Override
	public void initStudentProfile(User student,Integer school_year) {
		for (EClass eclass:student.getClasses()){
			initStudentProfile(student,eclass,school_year);
		}
	}

	private void initStudentProfile(User student, EClass eclass, Integer school_year) {
		Integer school_id = student.getSchool_id();
		Integer class_id = eclass.getId();
		ArrayList<SchoolTerm> terms = termDao.getLatestTerm(school_id);
		// Get current year
		if (school_year == null){
			school_year = terms.get(0).getSchool_year_id();
		}
		
		ArrayList<StudentProfile> list_std_profiles = studentProfileDao.findEx(student.getId(), school_id, class_id, school_year);
		StudentProfile stdProfile = null;
		if ((list_std_profiles == null ) ||
			(list_std_profiles != null && list_std_profiles.size() <= 0))
		{
			// new profile
			stdProfile = new StudentProfile();
			stdProfile.setActflg("A");
			stdProfile.setSchool_id(school_id);
			stdProfile.setClass_id(class_id);
			stdProfile.setStudent_id(student.getId());
			stdProfile.setSchool_year(school_year);
			
			studentProfileDao.saveStudentProfile(stdProfile);
		}else{
			stdProfile = list_std_profiles.get(0);
		}
		
		// Loop all Subjects available
		ArrayList<MSubject> m_subjects = (ArrayList<MSubject>) msubjectDao.findBySchool(student.getSchool_id(), 0, 1000);
		for (MSubject msubject: m_subjects){
			
			//ArrayList<ExamResult> examResult= examResultDao.findExamResultExt(school_id, 0, 999999, class_id, student.getId(), msubject.getId(), null, null, null, null, null, null, null, null);
			
			ArrayList<ExamProfile>  exam_profiles  = examProfileDao.findExt(school_id, class_id, student.getId(), school_year, msubject.getId());
			if ((exam_profiles == null) ||
				(exam_profiles != null && exam_profiles.size() <= 0))
			{
				ExamProfile exam_profile = new ExamProfile();
				// School info
				exam_profile.setSchool_id(school_id);
				exam_profile.setClass_id(class_id);
				exam_profile.setStudent_id(student.getId());
				
				exam_profile.setSchool_year(school_year);
				// Subject info
				exam_profile.setSubject_id(msubject.getId());
				exam_profile.setSubject_name(msubject.getSval());
				// Exam result info
				Float m9 = (float) 0;
				Float m10 = (float) 0;
				Float m11 = (float) 0;
				Float m12 = (float) 0;
				Float ave_m1 = (float) 0;
				Float test_term1 = (float) 0;
				Float ave_term1 = (float) 0;
				Float m2 = (float) 0;
				Float m3 = (float) 0;
				Float m4 = (float) 0;
				Float m5 = (float) 0;
				Float ave_m2= (float) 0;
				Float test_term2 = (float) 0;
				Float ave_term2 = (float) 0;
				
				Float ave_year= (float) 0;
				Float re_test= (float) 0;
				
				exam_profile.setM9(m9);
				exam_profile.setM10(m10);
				exam_profile.setM11(m11);
				exam_profile.setM12(m12);
				
				exam_profile.setAve_m1(ave_m1);
				exam_profile.setTest_term1(test_term1);
				exam_profile.setAve_term1(ave_term1);
				
				exam_profile.setM2(m2);
				exam_profile.setM3(m3);
				exam_profile.setM4(m4);
				exam_profile.setM5(m5);
				
				
				exam_profile.setAve_m2(ave_m2);
				exam_profile.setTest_term2(test_term2);
				exam_profile.setAve_term2(ave_term2);
				
				
				exam_profile.setAve_year(ave_year);
				exam_profile.setRetest(re_test);
				
				// Save to DB
				exam_profile.setStudent_profile_id(stdProfile.getId());
				examProfileDao.saveExamEval(exam_profile);
			}
			
		}
		
	}

}
