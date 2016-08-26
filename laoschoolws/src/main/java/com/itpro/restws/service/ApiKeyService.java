package com.itpro.restws.service;

import java.util.ArrayList;

import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.User;

public interface ApiKeyService {
	
	ApiKey findById(Integer id);
	ArrayList<ApiKey> findBySsoID(String sso_id);
	ArrayList<ApiKey> findByApiKey(String api_key);
	ArrayList<ApiKey> findActivedApiKey(String api_key,String auth_key);
	ArrayList<ApiKey> findByCloundToken(String token);
	
	//ArrayList<ApiKey> findByExt(String sso_id, String api_key, String token);
	
	
	
	ApiKey loginApiKeySuccess(String sso_id, String api_key,String auth_key); // Khi user login
	void logoutApiKey(String api_key); // Khi user logout, clear auth_key va sso_id from api_key, set active=0
	
	void saveFireBaseToken(String sso_id, String api_key, String auth_key,String cld_token);// Dung de Client save FireBase token to server
	void updateApiKey(ApiKey apikey); // De update last modify datetime
	void logoutBySSoID(String username);// Delete all auth_key of sso_id, active =0
	void logoutByAuthKey(String token);// Delete all auth_key of auth_key, active =0
	
	void validGetApiKey(User me, String sso_id);
	boolean isIgnoredKey(String api_key);
	
}