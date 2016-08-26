package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysLateReason;


public interface SysLateReasonDao {

	int countAll();
	List<SysLateReason> findAll() ;
	SysLateReason findById(Integer id);
}

