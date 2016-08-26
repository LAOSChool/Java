package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysMsgSamp;


public interface SysMsgSampDao {

	int countAll();
	List<SysMsgSamp> findAll() ;
	List<SysMsgSamp> findByNotice(String notice) ;
	SysMsgSamp findById(Integer id);
}

