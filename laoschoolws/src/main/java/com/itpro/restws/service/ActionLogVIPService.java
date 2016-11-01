package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.AbstractModel;
import com.itpro.restws.model.ActionLogVIP;
import com.itpro.restws.model.User;

public interface ActionLogVIPService {
	
	ActionLogVIP findById(Integer id);
	int countBySchool(Integer school_id);
	int countBySSO(String sso_id);
	ArrayList<ActionLogVIP> findBySchool(Integer school_id,int from_num, int max_result);
	ArrayList<ActionLogVIP> findBySSO(String sso_id,int from_num, int max_result);
	
	
	void logAction_type(User me,String act_type,String content); // Support important logging from Service layer
	void logAction_url(User me,String url,AbstractModel model); // Support important logging from Control layer
	void logAction_type_json(User me,String act_type,String content, String json_str); // Support login with json to convert data
	

	// Support log action of Input Exam ( only login available mark)
	ActionLogVIP insertAction( ActionLogVIP act);
	String get_action_vip_type(String url);
	
	ActionLogVIP createTmpActionLogVIP(User me,String url,AbstractModel model);
	
	int countActionLogExt(Integer school_id,
			String filter_sso_id, String filter_from_dt, String filter_to_dt,String filter_type);
	
	ArrayList<ActionLogVIP> findActionLogExt(Integer school_id, Integer filter_from_row, Integer filter_max_result,
			String filter_sso_id, String filter_from_dt,String filter_to_dt,String filter_type);
	
	
	
}