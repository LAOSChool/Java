package com.itpro.restws.service;

import java.util.List;

import com.itpro.restws.model.Permit;
import com.itpro.restws.model.User;

public interface PermitService {
	List<Permit>  loadPermit(User user);
}