package com.itpro.restws.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.dao.NotifyDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.model.Notify;

@Service("notifyService")
@Transactional
public class NotifyServiceImpl implements NotifyService{

	@Autowired
	private NotifyDao notifyDao;

	@Override
	public Notify findById(int id) {
		return notifyDao.findById(id);
		
	}

	@Override
	public int countFromUser(int from_user_id) {
		return notifyDao.countByFromUser(from_user_id);
	}

	@Override
	public int countToUser(int to_user_id) {
		return notifyDao.countByToUser(to_user_id);
	}

	@Override
	public ArrayList<Notify> findFromUser(int from_userid, int from_num, int max_result) {
		
		return (ArrayList<Notify>) notifyDao.findByFromUser(from_userid, from_num, max_result);
	}

	@Override
	public ArrayList<Notify> findTomUser(int to_userid, int from_num, int max_result) {
		
		return (ArrayList<Notify>) notifyDao.findByToUser(to_userid, from_num, max_result);
	}

	@Override
	public int countBySchool(int school_id) {
		
		return notifyDao.countBySchool(school_id);
	}

	@Override
	public int countByClass(int class_id) {
		return notifyDao.countByClass(class_id);
	}

	@Override
	public ArrayList<Notify> findBySchool(int school_id, int from_num, int max_result) {
		return (ArrayList<Notify>) notifyDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<Notify> findByClass(int class_id, int from_num, int max_result) {
		return (ArrayList<Notify>) notifyDao.findBySchool(class_id, from_num, max_result);
	}


	
	

	@Override
	public String productFile(String filename, byte[] bytes) {
		
		if (bytes == null ){
            try {
            	String dest_file = "./"+filename;// YYYY/MM/DD/UserID_YYYYMMDDSSMMSS_name;
                // Save to server
                BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(new File(dest_file)));
                stream.write(bytes);
                stream.close();
                return "dest_file:" + dest_file;
            } catch (Exception e) {
            	String err = "You failed to upload " + filename + " => " + e.getMessage();
                throw new ESchoolException(err, HttpStatus.INTERNAL_SERVER_ERROR) ;
            }
        } else {
        	String err = "You failed to upload " + filename + " because the file was empty.";
            throw new ESchoolException(err, HttpStatus.NO_CONTENT) ;
        }
	}

	@Override
	public String productFile(String filename, MultipartFile multipartFile) {
		  if (!multipartFile.isEmpty()) {
                byte[] bytes;
				try {
					bytes = multipartFile.getBytes();
					return productFile(filename,bytes);
				} catch (IOException e) {
					String err = "You failed to upload " + filename + " => " + e.getMessage();
	                throw new ESchoolException(err, HttpStatus.INTERNAL_SERVER_ERROR) ;
				}
	        } else {
	        	String err = "You failed to upload " + filename + " because the file was empty.";
	        	throw new ESchoolException(err, HttpStatus.NO_CONTENT) ;
	        }
    }

	@Override
	public FileSystemResource getFile(String filename) {
		return new FileSystemResource(filename);
	}

	@Override
	public Notify insertNotify(Notify notify) {
		notifyDao.saveNotify(notify);
		return notify;
	}

	@Override
	public Notify updateNotify(Notify notify) {
		Notify notifyDB = notifyDao.findById(notify.getId());
		notifyDB = Notify.updateChanges(notifyDB, notify);
		notifyDao.updateNotify(notifyDB);
		return notifyDB;
		
	}
}
