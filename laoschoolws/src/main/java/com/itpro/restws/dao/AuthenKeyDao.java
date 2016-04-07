package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.AuthenKey;


public interface AuthenKeyDao {

	List<AuthenKey> findBySsoID(String string);
	AuthenKey findByToken(String token);
	void saveToken(AuthenKey authenKey);
	void updateToken(AuthenKey authenKey);
}

