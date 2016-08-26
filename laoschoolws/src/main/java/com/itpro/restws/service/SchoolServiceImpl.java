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
	public School insertSchool(School school) {
		schoolDao.saveSchool(school);
		return school;
	}

	@Override
	public School updateSchool(School school) {
		School schoolDB = schoolDao.findById(school.getId());
		schoolDB = School.updateChanges(schoolDB, school);
		schoolDao.updateSchool(schoolDB);
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
			schoolDao.updateSchool(school);
			
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



}
