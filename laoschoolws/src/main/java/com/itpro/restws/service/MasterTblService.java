package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.MTemplate;
import com.itpro.restws.model.User;

public interface MasterTblService {
	
	MTemplate findById(String tbl_name,Integer id);
	int countBySchool(String tbl_name,Integer school_id);
	
	//ArrayList<? extends MasterBase> findBySchool(String tbl_name,int school_id,int from_num, int max_result);
	// int createSaveMasterData(String tbl_name,int school_id,int from_num, int max_result);
	
	
	ArrayList<MTemplate> findBySchool(String tbl_name,Integer school_id,int from_num, int max_result);
	void validMTemplate(User user, String tbl_name,MTemplate template);
	
	MTemplate insertMTemplate(User user, String tbl_name,MTemplate template);
	MTemplate updateMTemplate(User user, String tbl_name,MTemplate template);
	void deleteMTemplate(User user, String tbl_name, Integer id);
	 
}
