package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysStdMsg;


public interface SysStdMsgDao {

	int countAll();
	List<SysStdMsg> findAll() ;
	
}

