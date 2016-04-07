package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.MTemplate;

public interface MasterTblService {
	
	MTemplate findById(String tbl_name,int id);
	int countBySchool(String tbl_name,int school_id);
	
	//ArrayList<? extends MasterBase> findBySchool(String tbl_name,int school_id,int from_num, int max_result);
	// int createSaveMasterData(String tbl_name,int school_id,int from_num, int max_result);
	
	
	ArrayList<MTemplate> findBySchool(String tbl_name,int school_id,int from_num, int max_result);
	MTemplate insertMTemplate(String tbl_name,MTemplate template);
	MTemplate updateMTemplate(String tbl_name,MTemplate template);
	 
}
