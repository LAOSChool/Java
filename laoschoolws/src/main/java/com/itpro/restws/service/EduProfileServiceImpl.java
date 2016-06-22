package com.itpro.restws.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.EduProfileDao;
import com.itpro.restws.dao.SchoolYearDao;
import com.itpro.restws.model.EduProfile;
import com.itpro.restws.model.SchoolYear;

@Service("eduProfileService")
@Transactional
public class EduProfileServiceImpl implements EduProfileService{
	private static final Logger logger = Logger.getLogger(EduProfileServiceImpl.class);

	@Autowired
	protected EduProfileDao eduProfileDao;
	
	@Autowired
	protected SchoolYearDao schoolYearDao;
	
	@Override
	public ArrayList<SchoolYear> findSchoolYearByStudentID(Integer student_id) {
		ArrayList<SchoolYear> ret = new ArrayList<>();
		ArrayList<EduProfile> profiles = eduProfileDao.findByStudentID(student_id);
		for (EduProfile eduProfile: profiles){
			SchoolYear schoolYear = schoolYearDao.findById(eduProfile.getSchool_year_id());
			boolean insert_ok = true;
			for (SchoolYear year : ret){
				if (year.getId().intValue() == schoolYear.getId().intValue()){
					insert_ok = false;
					break;
				}
			}
			if (insert_ok){
				ret.add(schoolYear);
			}
		}
		return ret;
	}

	

}
