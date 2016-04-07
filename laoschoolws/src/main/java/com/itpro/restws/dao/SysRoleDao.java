package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.SysRole;


public interface SysRoleDao {

	int countAll();
	List<SysRole> findAll() ;
}

