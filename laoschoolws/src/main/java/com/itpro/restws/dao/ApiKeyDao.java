package com.itpro.restws.dao;

import java.util.List;

import com.itpro.restws.model.ApiKey;


public interface ApiKeyDao {
	ApiKey findById(Integer id);
	
	List<ApiKey> findBySsoId(String sso_id);
	List<ApiKey> findByApiKey(String api_key);
	List<ApiKey> findActivedApiKey(String api_key,String auth_key);
	
	List<ApiKey> findByCloundToken(String token);
	List<ApiKey> findByAuthKey(String auth_key);
	
	List<ApiKey>  findExt(String sso_id, String api_key, String auth_key, String clound_token);
	
	
	void saveApiKey(ApiKey apikey);
	void updateApiKey(ApiKey apikey);
	void deleteApiKey(ApiKey  apikey);
	
}

