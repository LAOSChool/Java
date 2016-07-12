package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysAttMsg;


public interface SysAttMsgDao {

	int countAll();
	List<SysAttMsg> findAll() ;
}

