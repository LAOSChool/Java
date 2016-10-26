package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.ActionLogVIP;

public interface ActionLogVIPDao {

	ActionLogVIP findById(Integer id);

	int countBySchool(Integer school_id);

	int countBySSO(String sso_id);

	List<ActionLogVIP> findBySchool(Integer school_id, int from_row, int max_result);

	List<ActionLogVIP> findBySSO(String sso_id, int from_row, int max_result);

	void saveAction(ActionLogVIP actionLogVIP);

	void updateAction(ActionLogVIP actionLogVIP);

	int countActionLogExt(Integer school_id, String filter_sso_id, String filter_from_dt, String filter_to_dt,
			String filter_type);

	List<ActionLogVIP> findActionLogExt(Integer school_id, Integer filter_from_row, Integer filter_max_result,
			String filter_sso_id, String filter_from_dt, String filter_to_dt, String filter_type);

}
