package com.itpro.restws.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.dao.SchoolDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.E_STATE;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.School;
import com.itpro.restws.model.User;

@Service("schoolService")
@Transactional
public class SchoolServiceImpl implements SchoolService{

	@Autowired
	private SchoolDao schoolDao;
	@Autowired
    private Environment environment;
	
//	@Autowired
//    private SchoolYearService schoolYearService;
	

	@Override
	public School findById(Integer id) {
		
		return schoolDao.findById(id);
	}

	@Override
	public ArrayList<School> findActive() {
		//throw new ESchoolException(" aaaa abc",HttpStatus.BAD_REQUEST);
		//throw new ExceptionMethodNotAllowed("testabc");
		return (ArrayList<School>) schoolDao.findAll();
	}

	@Override
	public School insertSchool(User me, School school) {
		schoolDao.saveSchool(me,school);
		return school;
	}

	@Override
	public School updateTransSchool(User me,School school) {
		
		valid_update_school(me,school);// [UT]20160907
		
		School schoolDB = schoolDao.findById(school.getId());
		
		if (schoolDB == null ){
			throw new ESchoolException("school_id is not existing", HttpStatus.BAD_REQUEST);
		}
		
		
		schoolDB = School.updateChanges(schoolDB, school);
		schoolDao.updateSchool(me,schoolDB);
		return schoolDB;
	}

	@Override
	public void saveUploadPhoto(User me, MultipartFile []files) {
		String err_msg = "";
		
		// Validation Data
		if (files == null  || files.length<=0 ){
			err_msg = "file is mandatory";
		}
		if (files.length >1 ){
			err_msg = "Cannot upload multiples files";
		}
		
		if (err_msg.length() > 0){
			throw new ESchoolException(err_msg,HttpStatus.BAD_REQUEST);
		}
	
		
		String upload_dir = environment.getRequiredProperty("avatar_upload_base");
		Utils.ensureFolder(upload_dir);
		
		String urlbase = environment.getRequiredProperty("avatar_file_url_base");
		String fileName="";
		String filePath ="";
		
		School school = schoolDao.findById(me.getSchool_id());
		if (school == null ){
			throw new ESchoolException("SchoolId not existing", HttpStatus.BAD_REQUEST);
		}
		
		try {
			MultipartFile file = files[0];
			byte[] bytes = file.getBytes();
			String str_dir = Utils.makeFolderByTime(upload_dir);
			//fileName = file.getOriginalFilename();
			fileName = Utils.getFileName("SCHOOL"+school.getId().intValue(),file.getOriginalFilename());            	
            filePath = str_dir+ "/" + fileName;
            
			// Create the file on server
			File serverFile = new File(filePath);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();
			
			Utils.resizeImage(filePath, Constant.BIG_SIZE, Constant.BIG_SIZE);
			Utils.resizeImage(filePath, Constant.SMALL_SIZE, Constant.SMALL_SIZE);
			
			// Save NotifyImg to DB
			school.setPhoto(filePath.replaceFirst(upload_dir, urlbase));
			schoolDao.updateSchool(me,school);
			
		} catch (Exception e) {
			throw new ESchoolException("You failed to upload " + fileName + " => " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	
		
	}

//	@Override
//	public SchoolTerm createTerm(User me, SchoolTerm schoolTerm) {
//		
//		schoolYearService.insertSchoolTerm(me, schoolTerm);
//		return schoolTerm;
//	}
//
//	@Override
//	public SchoolTerm updateTerm(User me, SchoolTerm schoolTerm) {
//		schoolYearService.updateSchoolTerm(me, schoolTerm);
//		return schoolTerm;
//	}


	void valid_update_school(User me, School school){
		if (!me.hasRole(E_ROLE.ADMIN.getRole_short())){
			throw new ESchoolException("Only Admin can update school info", HttpStatus.BAD_REQUEST);
		}
		if (school.getId() == null || school.getId().intValue() == 0){
			throw new ESchoolException("school_id is required", HttpStatus.BAD_REQUEST);
		}
		if (me.getSchool_id().intValue() != school.getId().intValue()){
			throw new ESchoolException("school_id is not belong to current user", HttpStatus.BAD_REQUEST);
		}
		// Check state
		if (school.getState() != null ){
			Integer sts = Utils.parseInteger(school.getState());
			if (sts == null ){
				throw new ESchoolException("invalid state, must be 0,1,2,3", HttpStatus.BAD_REQUEST);
			}
			if ((sts.intValue() < E_STATE.PENDING.value())|| 
			   (sts.intValue() > E_STATE.CLOSED.value())){
				throw new ESchoolException("invalid state, must be 0,1,2,3", HttpStatus.BAD_REQUEST);
			}
		}
//		// check title
//		if (school.getTitle() == null || 
//				school.getTitle().trim().length() == 0){
//			throw new ESchoolException("title is required", HttpStatus.BAD_REQUEST);
//		}
	}
}
