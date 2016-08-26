package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysProvince;


public interface SysProvinceDao {

	List<SysProvince> findAll() ;
	int countAll();
	SysProvince findById(Integer id);
}

