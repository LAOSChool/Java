package com.itpro.restws.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ApiKeyDao;
import com.itpro.restws.dao.UserDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.User;

@Service("apiKeyService")
@Transactional
public class ApiKeyServiceImpl implements ApiKeyService{
	private static final Logger logger = Logger.getLogger(ApiKeyServiceImpl.class);
	@Autowired
	private ApiKeyDao apiKeyDao;
	
	@Autowired
	private UserDao userDao;
	
//	@Autowired
//	private AuthenKeyDao authenKeyDao;
	
	@Override
	public ApiKey findById(Integer id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("id"+(id==null?"null":id.intValue()));
		
		return apiKeyDao.findById(id);
	}
	@Override
	public ArrayList<ApiKey> findBySsoID(String sso_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("sso_id"+(sso_id==null?"null":sso_id));
		
		return (ArrayList<ApiKey>) apiKeyDao.findBySsoId(sso_id);
	}
	@Override
	public ArrayList<ApiKey> findByApiKey(String api_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("api_key"+(api_key==null?"null":api_key));
		
		return (ArrayList<ApiKey>) apiKeyDao.findByApiKey(api_key);
	}
	@Override
	public ArrayList<ApiKey> findByCloundToken(String token) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("token"+(token==null?"null":token));
		
		
		return (ArrayList<ApiKey>) apiKeyDao.findByCloundToken(token);
	}
//	@Override
//	public ArrayList<ApiKey> findByExt(String sso_id, String api_key, String token) {
//		return (ArrayList<ApiKey>) apiKeyDao.findExt(sso_id, api_key, token);
//	}
	@Override
	public ApiKey loginApiKeySuccess(String sso_id, String api_key,String auth_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("sso_id"+(sso_id==null?"null":sso_id));
		logger.info("api_key"+(api_key==null?"null":api_key));
		logger.info("auth_key"+(auth_key==null?"null":auth_key));
		
		ApiKey apiKey = null;
		if (isIgnoredKey(api_key)){
			return null;
		}
		
		ArrayList<ApiKey> list= (ArrayList<ApiKey>) apiKeyDao.findByApiKey(api_key);
		
		
		// Create new if not existing
		if (list == null || list.size() == 0){
			apiKey = new ApiKey();
			apiKey.setFirst_request_dt(Utils.now());
		}else {
			// If already saved, get first
			apiKey = list.get(0);
			// Delete all others device of same API_KEY
			if (list.size() == 1){
				for (int i =1;i< list.size(); i++){
					apiKeyDao.deleteApiKey(list.get(i));
				}	
			}
		}
			
		
		if (apiKey != null ){
			
			apiKey.setApi_key(api_key);
			apiKey.setActive(1);
			apiKey.setAuth_key(auth_key);
			apiKey.setSso_id(sso_id);
			apiKeyDao.saveApiKey(apiKey);	
		}
		return apiKey;
		
	}
	
	@Override
	public void saveFireBaseToken(String sso_id, String api_key, String auth_key,String cld_token) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("sso_id"+(sso_id==null?"null":sso_id));
		logger.info("api_key"+(api_key==null?"null":api_key));
		logger.info("auth_key"+(auth_key==null?"null":auth_key));
		logger.info("cld_token"+(cld_token==null?"null":cld_token));
		
		ApiKey apiKey = null;
		if (isIgnoredKey(api_key)){
			return ;
		}
		
		// validation mandatory fields		
		if (sso_id == null ){
			throw new ESchoolException("sso_id = NULL", HttpStatus.BAD_REQUEST);
		}
		if (api_key == null ){
			throw new ESchoolException("api_key = NULL", HttpStatus.BAD_REQUEST);
		}
		if (auth_key == null ){
			throw new ESchoolException("auth_key = NULL", HttpStatus.BAD_REQUEST);
		}
		if (cld_token == null ){
			throw new ESchoolException("cld_token = NULL", HttpStatus.BAD_REQUEST);
		}
		// Save clound token 
		ArrayList<ApiKey> list= (ArrayList<ApiKey>) apiKeyDao.findByApiKey(api_key);
		if (list == null || list.size() == 0){
			// New
		}else{
			for (ApiKey tmp: list){
				apiKey = tmp;
				break;
//				if ((tmp.getAuth_key().equalsIgnoreCase(auth_key)) &&
//				   (tmp.getSso_id().equalsIgnoreCase(sso_id))){
//					apiKey = tmp;
//				}else{
//					// Delete all invalid devices
//					apiKeyDao.deleteApiKey(tmp);
//				}
			}
		
		}
		if (apiKey == null){
			apiKey = new ApiKey();
			
			apiKey.setApi_key(api_key);
			apiKey.setSso_id(sso_id);
			apiKey.setAuth_key(auth_key);
			apiKey.setActive(1);
			// Save new CLD token
			apiKey.setCld_token(cld_token);
			// Commit to DB
			apiKeyDao.saveApiKey(apiKey);
		}else{
			apiKey.setApi_key(api_key);
			apiKey.setSso_id(sso_id);
			apiKey.setAuth_key(auth_key);
			apiKey.setActive(1);
			// Save new CLD token
			apiKey.setCld_token(cld_token);
			// Commit to DB			
			apiKeyDao.updateApiKey(apiKey);
		}
	}
	
	

	@Override
	public void updateApiKey(ApiKey apikey) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		
		if (apikey.getId() != null  && apikey.getId().intValue() > 0){
			apiKeyDao.updateApiKey(apikey);
		}
		
	}
	@Override
	public void logoutApiKey(String api_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("api_key"+(api_key==null?"null":api_key));
		
		
		ArrayList<ApiKey> list = (ArrayList<ApiKey>) apiKeyDao.findByApiKey(api_key);
		if (list != null && list.size() > 0){
			for (ApiKey ob_api_key : list){
				ob_api_key.setSso_id("");
				ob_api_key.setAuth_key("");
				ob_api_key.setActive(0);
				apiKeyDao.updateApiKey(ob_api_key);
			}
		}
		
	}
	@Override
	public void logoutBySSoID(String username) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("username"+(username==null?"null":username));
		
		
		
		ArrayList<ApiKey> list = (ArrayList<ApiKey>) apiKeyDao.findBySsoId(username);
		if (list != null && list.size() > 0){
			for (ApiKey ob_api_key : list){
				ob_api_key.setSso_id("");
				ob_api_key.setAuth_key("");
				ob_api_key.setActive(0);
				apiKeyDao.updateApiKey(ob_api_key);
			}
		}
		
	}
	@Override
	public void logoutByAuthKey(String auth_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("auth_key"+(auth_key==null?"null":auth_key));
		
		ArrayList<ApiKey> list = (ArrayList<ApiKey>) apiKeyDao.findByAuthKey(auth_key);
		if (list != null && list.size() > 0){
			for (ApiKey ob_api_key : list){
				ob_api_key.setSso_id("");
				ob_api_key.setAuth_key("");
				ob_api_key.setActive(0);
				apiKeyDao.updateApiKey(ob_api_key);
			}
		}
		
	}
	@Override
	public void validGetApiKey(User me, String sso_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("sso_id"+(sso_id==null?"null":sso_id));
		
		User user = userDao.findBySSO(sso_id);
		if (user == null  || user.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("sso_id is not existing or not belong to use school", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@Override
	public boolean isIgnoredKey(String api_key){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("api_key"+(api_key==null?"null":api_key));
		
		String[] ignores_devices = Constant.NON_DEVICE_API_KEY;
		for (int i = 0;i< ignores_devices.length;i++){
			if (api_key.equalsIgnoreCase(ignores_devices[i])){
				return true;
			}
		}
		return false;
	}
	@Override
	public ArrayList<ApiKey> findActivedApiKey(String api_key, String auth_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("api_key"+(api_key==null?"null":api_key));
		logger.info("auth_key"+(auth_key==null?"null":auth_key));
		
		return (ArrayList<ApiKey>) apiKeyDao.findActivedApiKey(api_key, auth_key);
	}


}
