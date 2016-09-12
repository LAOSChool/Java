package com.itpro.restws.dao;

import java.util.ArrayList;

import com.itpro.restws.model.NotifyImg;
import com.itpro.restws.model.User;


public interface NotifyImgDao {

	void saveImg(User me,NotifyImg notifyImg);
	void updateImg(User me,NotifyImg notifyImg);
	NotifyImg findById(Integer id);
	ArrayList<NotifyImg> findByTaskId(Integer task_id);
	
}

