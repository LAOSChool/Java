package com.itpro.restws.dao;

import java.util.ArrayList;

import com.itpro.restws.model.NotifyImg;


public interface NotifyImgDao {

	void saveImg(NotifyImg notifyImg);
	void updateImg(NotifyImg notifyImg);
	NotifyImg findById(Integer id);
	ArrayList<NotifyImg> findByTaskId(Integer task_id);
	
}

