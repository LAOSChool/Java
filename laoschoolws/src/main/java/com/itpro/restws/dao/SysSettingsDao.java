package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysSettings;


public interface SysSettingsDao {

	List<SysSettings> findAll() ;
	int countAll();
	SysSettings findById(Integer id);
}

