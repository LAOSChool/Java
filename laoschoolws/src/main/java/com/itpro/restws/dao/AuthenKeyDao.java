package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.AuthenKey;


public interface AuthenKeyDao {

	List<AuthenKey> getNonExpired();
	List<AuthenKey> findBySsoID(String sso);
	AuthenKey findByToken(String token);
	void saveToken(AuthenKey authenKey);
	void deleteToken(AuthenKey authenKey);
	int deleteBySso(String sso);
	void updateToken(AuthenKey authenKey);
}

