package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysWeekday;


public interface SysWeekdayDao {
	int countAll();
	List<SysWeekday> findAll() ;
}

