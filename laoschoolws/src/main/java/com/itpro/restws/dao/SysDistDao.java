package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysDist;


public interface SysDistDao {

	List<SysDist> findAll() ;
	int countAll();
	SysDist findById(Integer id);
}

