package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysAttReason;


public interface SysAttReasonDao {

	int countAll();
	List<SysAttReason> findAll() ;
}

