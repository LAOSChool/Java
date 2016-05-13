package com.itpro.restws.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.itpro.restws.helper.E_ENTITY;
import com.itpro.restws.model.Permit;
import com.itpro.restws.model.User;

public interface PermitService {
	List<Permit>  loadPermit(User user);
	Permit  loadEntityPermit(User user,E_ENTITY entity);
	
//	List<Permit>  loadPermit(User user,E_ENTITY entity,E_SCOPE scope);
//	List<Permit>  loadPermit(User user,E_ENTITY entity,E_SCOPE scope,E_RIGHT right);
	void checkPermit(User user,HttpServletRequest request);
//	E_SCOPE getDataScope(String roles);
}