package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.ActionLog;


public interface ActionLogDao {

	ActionLog findById(Integer id);
	int countBySchool(Integer school_id) ;
	int countByUser(Integer user_id) ;
	List<ActionLog> findBySchool(Integer school_id,int from_row,int max_result) ;
	List<ActionLog> findByUser(Integer user_id,int from_row,int max_result) ;
	void saveAction(ActionLog actionLog);
	void updateAction(ActionLog actionLog);
	int countActionLogExt(Integer school_id, Integer user_id, Integer from_row_id,String from_dt, String to_dt);
	List<ActionLog> findActionLogExt(Integer school_id, Integer user_id, Integer from_row_id,
			int from_num, int max_result,String from_dt, String to_dt);
}

