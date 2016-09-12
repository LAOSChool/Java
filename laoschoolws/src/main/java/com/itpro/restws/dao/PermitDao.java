package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.FlushMode;

import com.itpro.restws.model.Permit;
import com.itpro.restws.model.User;


public interface PermitDao {

	List<Permit> findPermit(String role,Integer school_id);
	List<Permit> findPermit(String role,Integer school_id,String entity);
	List<Permit> findPermit(String role,Integer school_id,String entity,int scope);
	List<Permit> findPermit(String role,Integer school_id,String entity,int scope,String right);
	
	void savePermission(User me,Permit permission);
	void updatePermission(User me,Permit permission);
	void setFlushMode(FlushMode mode);
	void clearChange();
	
}

