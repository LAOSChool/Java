package com.itpro.restws.service;

import java.util.ArrayList;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.model.Notify;

public interface NotifyService {
	
	String productFile( String filename, byte[] bytes);
	String productFile( String filename, MultipartFile multipartFile);
	FileSystemResource getFile( String filename);
	
	Notify findById(int id);
	int countBySchool(int school_id);
	int countByClass (int class_id);
	int countFromUser(int from_user_id);
	int countToUser(int to_user_id);
	
	ArrayList<Notify> findBySchool(int school_id,int from_num, int max_result);
	ArrayList<Notify> findByClass(int class_id,int from_num, int max_result);
	ArrayList<Notify> findFromUser(int from_userid,int from_num, int max_result);
	ArrayList<Notify> findTomUser(int to_userid,int from_num, int max_result);
	
	
	Notify insertNotify(Notify notify);
	Notify updateNotify(Notify notify);
	
}