package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.Permit;


public interface PermitDao {

	List<Permit> findByRole(String role,int school_id);
	void savePermission(Permit permission);
	void updatePermission(Permit permission);
	
}

