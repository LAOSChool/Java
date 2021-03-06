package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.SysTemplate;

public interface SysTblService {
	
	ArrayList<SysTemplate> findAll(String tbl_name,int from_num, int max_result);
	
	SysTemplate findByID(String tbl_name,Integer id);
	SysTemplate findBySvalOne(String tbl_name,String sval);
	ArrayList<SysTemplate> findBySvalAll(String tbl_name,String sval);
	
	int countAll(String tbl_name);
	
}