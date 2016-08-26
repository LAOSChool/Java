package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysDegree;


public interface SysDegreeDao {

	List<SysDegree> findAll() ;
	SysDegree findById(Integer id);
	int countAll();
}

