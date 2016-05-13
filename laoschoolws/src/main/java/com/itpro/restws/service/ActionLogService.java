package com.itpro.restws.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.itpro.restws.model.ActionLog;
import com.itpro.restws.model.User;

public interface ActionLogService {
	
	ActionLog findById(Integer id);
	int countBySchool(Integer school_id);
	int countByUser(Integer user_id);
	
	ArrayList<ActionLog> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<ActionLog> findByUser(Integer user_id,int from_num, int max_result);
	
	ActionLog insertAction(ActionLog act);
	ActionLog updateAction(ActionLog act);
	
	//ActionLog start_trace(HttpServletRequest request);
	ActionLog start_trace(HttpServletRequest request,User user);
	//void end_trace(Integer id, HttpServletResponse response,long duration);
	 void end_trace(Integer id, String resp_data, int resp_sts,long duration); 
	//ActionLog logUserContext(Integer act_id, User user);
}