package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Permit;


public interface PermitDao {

	List<Permit> findPermit(String role,Integer school_id);
	List<Permit> findPermit(String role,Integer school_id,String entity);
	List<Permit> findPermit(String role,Integer school_id,String entity,int scope);
	List<Permit> findPermit(String role,Integer school_id,String entity,int scope,String right);
	
	void savePermission(Permit permission);
	void updatePermission(Permit permission);
	
}

