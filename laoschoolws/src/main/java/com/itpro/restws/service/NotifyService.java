package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import com.itpro.restws.helper.MessageFilter;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.Notify;
import com.itpro.restws.model.NotifyImg;
import com.itpro.restws.model.User;

public interface NotifyService {
	
	String productFile( String filename, byte[] bytes);
	String productFile( String filename, MultipartFile multipartFile);
	FileSystemResource getFile( String filename);
	
	Notify findById(Integer id);
	int countBySchool(Integer school_id);
	int countByClass (Integer class_id);
	int countFromUser(Integer from_user_id);
	int countToUser(Integer to_user_id);
	
	ArrayList<Notify> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<Notify> findByClass(Integer class_id,int from_num, int max_result);
	ArrayList<Notify> findFromUser(Integer from_userid,int from_num, int max_result);
	ArrayList<Notify> findTomUser(Integer to_userid,int from_num, int max_result);
	
	
//	Notify insertNotify(Notify notify);
	Notify updateNotify(Notify notify);
	//Notify saveUploadData(MultipartFile file,String str_notify);
	//Notify saveUploadData(User user,MultipartFile[] files,String[] captions, String content, String title,String json_str_notify);
	Notify saveUploadData(User user,MultipartFile[] files,String[] captions, String[] orders,String json_str_notify);
	ArrayList<Notify>  broadcastNotify(User user,Notify notify,String filter_roles);
	public HashSet<NotifyImg> findImgByTaskID(Integer task_id);
	
	int countNotifyExt(Integer school_id, 
			//int from_row, int max_result, 
			// Secure filter
			  MessageFilter filter,
					// User filter
					Integer class_id, 
					String dateFrom, 
					String dateTo,
					Integer fromUserID, 
					Integer toUserID, 
					Integer channel, 
					Integer is_read,
					Integer from_row_id); 
	
	ArrayList<Notify>  findNotifyExt(Integer school_id, int from_row, int max_result,
			// Secure filter
			MessageFilter filter,
			// User filter
			Integer class_id, String dateFrom, String dateTo, Integer fromUserID, Integer toUserID, Integer channel,
			Integer is_read, Integer from_row_id);
	
	MessageFilter secureGetMessages(User user);
}