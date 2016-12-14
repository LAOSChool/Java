package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ApiKeyDao;
import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.UserDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.User;

@Service("apiKeyService")
@Transactional
public class ApiKeyServiceImpl implements ApiKeyService{
	private static final Logger logger = Logger.getLogger(ApiKeyServiceImpl.class);
	@Autowired
	private ApiKeyDao apiKeyDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	protected ClassDao classesDao;
	
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
		User me = userDao.findBySSO(sso_id);
		if (me == null ){
			throw new RuntimeException("ApiKeyService.loginApiKeySuccess(): me = null");
		}
		ArrayList<ApiKey> list= (ArrayList<ApiKey>) apiKeyDao.findByApiKey(api_key);
		
		
		// Create new if not existing
		if (list == null || list.size() == 0){
			apiKey = new ApiKey();
			apiKey.setFirst_request_dt(Utils.now());
		}else {
			// If already saved, get first
			apiKey = list.get(0);
		}
		
		if (apiKey != null ){
			apiKey.setApi_key(api_key);
			apiKey.setLast_request_dt(Utils.now());
			apiKey.setActive(1);
			apiKey.setAuth_key(auth_key);
			apiKey.setSso_id(sso_id);
			apiKey.setSchool_id(me.getSchool_id());
			apiKey.setRole(me.getRoles());
			// Check class_id if not admin role
			if (!me.hasRole(E_ROLE.ADMIN.getRole_short())){
				Set<EClass> classes = me.getClasses();
				if (classes != null && classes.size() > 0){
					apiKey.setClass_id(classes.iterator().next().getId());
				}
			}
			if (apiKey.getId() != null ){
				apiKeyDao.updateApiKey(apiKey);
			}else{
				apiKeyDao.saveApiKey(apiKey);
			}
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
//		User me = userDao.findBySSO(sso_id);
//		if (me == null ){
//			throw new ESchoolException("me = null", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
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
		//ArrayList<ApiKey> list= (ArrayList<ApiKey>) apiKeyDao.findByApiKey(api_key);
		//ArrayList<ApiKey> list= (ArrayList<ApiKey>) apiKeyDao.findExt(sso_id, api_key, auth_key, null, null, null, null, 1, null, null, 0, 100);
		ArrayList<ApiKey> list= (ArrayList<ApiKey>) apiKeyDao.findActivedApiKey(api_key, auth_key);
		if (list == null || list.size() == 0){
			throw new ESchoolException("Cannot find: api_key AND auth_key in DB", HttpStatus.BAD_REQUEST);
		}else{
			apiKey = list.get(0);
		}
		if (apiKey == null){
			throw new ESchoolException("apiKey is NULL", HttpStatus.BAD_REQUEST);
		}else{
			// Save new CLD token
			apiKey.setCld_token(cld_token);
			apiKey.setLast_request_dt(Utils.now());
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
	public void clearByApiKey(String api_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("api_key"+(api_key==null?"null":api_key));
		
		
		ArrayList<ApiKey> list = (ArrayList<ApiKey>) apiKeyDao.findByApiKey(api_key);
		if (list != null && list.size() > 0){
			for (ApiKey ob_api_key : list){
				ob_api_key.clearInfo();
				apiKeyDao.updateApiKey(ob_api_key);
				// apiKeyDao.deleteApiKey(ob_api_key);
			}
		}
		
	}
	@Override
	public void clearBySSoID(String username) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("username"+(username==null?"null":username));
		
		ArrayList<ApiKey> list = (ArrayList<ApiKey>) apiKeyDao.findBySsoId(username);
		if (list != null && list.size() > 0){
			for (ApiKey ob_api_key : list){
				ob_api_key.clearInfo();
				apiKeyDao.updateApiKey(ob_api_key);
				//apiKeyDao.deleteApiKey(ob_api_key);
			}
		}
		
	}
	@Override
	public void clearByByAuthKey(String auth_key) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("auth_key"+(auth_key==null?"null":auth_key));
		
		ArrayList<ApiKey> list = (ArrayList<ApiKey>) apiKeyDao.findByAuthKey(auth_key);
		if (list != null && list.size() > 0){
			for (ApiKey ob_api_key : list){
				ob_api_key.clearInfo();
				apiKeyDao.updateApiKey(ob_api_key);
				//apiKeyDao.deleteApiKey(ob_api_key);
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
	@Override
	public ArrayList<ApiKey> findByExt(
			User me,
			Integer filter_class_id, 
			String filter_sso_id, 
			String filter_role, 
			Integer filter_active,
			String api_key, 
			String token,
			Integer from_num, 
			Integer max_result) {
		// Admin only
		
		if (me == null ){
			throw new ESchoolException("me == null", HttpStatus.BAD_REQUEST);
		}
		if (! me.hasRole(E_ROLE.ADMIN.getRole_short())){
			throw new ESchoolException("me is not Admin, cannot excecute", HttpStatus.FORBIDDEN);
		}
		// validation class
		if (filter_class_id != null && filter_class_id.intValue() > 0) {
			EClass eclass = classesDao.findById(filter_class_id);
			if (eclass== null ){
				throw new ESchoolException("class_id not existing", HttpStatus.BAD_REQUEST);
			}
			if (eclass.getSchool_id().intValue() != me.getSchool_id().intValue() ){
				throw new ESchoolException("class_id not belong to same school with me", HttpStatus.BAD_REQUEST);
			}
			
		}else{
			filter_class_id = null;
		}
		// Validate sso
		if (filter_sso_id != null && filter_sso_id.trim().length() > 0) {
			User user= userDao.findBySSO(filter_sso_id);
			if (user== null ){
				throw new ESchoolException("filter_sso_id not existing", HttpStatus.BAD_REQUEST);
			}
			if (user.getSchool_id().intValue() != me.getSchool_id().intValue() ){
				throw new ESchoolException("filter_sso_id not belong to same school with me", HttpStatus.BAD_REQUEST);
			}
			
		}else{
			filter_sso_id = null;
		}
		// Validate role
		if (filter_role != null && filter_role.trim().length() > 0) {
			if (!E_ROLE.contain(filter_role)){
				throw new ESchoolException("filter_role not correct value", HttpStatus.BAD_REQUEST);
			}
			
		}else{
			filter_role = null;
		}
		// Validate filter_active
		if (filter_active != null ) {
			if (filter_active.intValue() < 0 || filter_active.intValue() > 1){
				throw new ESchoolException("filter_active only accept 0 or 1 value", HttpStatus.BAD_REQUEST);
			}
			
		}else{
			filter_active = null;
		}
				
		return (ArrayList<ApiKey>) apiKeyDao.findExt(filter_sso_id, api_key, null, null, me.getSchool_id(), filter_class_id, filter_role, filter_active, null, null, from_num, max_result);
		
	}
	@Override
	public Integer countByExt(User me, 
			Integer filter_class_id, 
			String filter_sso_id, 
			String filter_role,
			Integer filter_active, 
			String api_key, 
			String token) {
		return  apiKeyDao.countExt(filter_sso_id, api_key, null, null, me.getSchool_id(), filter_class_id, filter_role, filter_active, null,null, null, null);
		
	}
}
