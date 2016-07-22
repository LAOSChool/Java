package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysSts;


public interface SysStsDao {

	int countAll();
	List<SysSts> findAll() ;
	List<SysSts>   findByFval(Integer sts);
}

