package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;


@Repository("apiKeyDao")
@Transactional
public class ApiKeyDaoImpl extends AbstractDao<Integer, ApiKey> implements ApiKeyDao {

	@Override
	public ApiKey findById(Integer id) {
		
		return getByKey(id);
	}

	@Override
	public List<ApiKey> findBySsoId(String sso_id) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("sso_id", sso_id));
	     @SuppressWarnings("unchecked")
		List<ApiKey> list = crit_list.list();
	     
		return  list;
	}

	@Override
	public List<ApiKey> findByCloundToken(String token) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("cld_token", token));
	    
		 @SuppressWarnings("unchecked")
			List<ApiKey> list = crit_list.list();
	     
		return  list;
	}

	@Override
	public void saveApiKey(ApiKey apikey) {

		apikey.setActflg("A");
		apikey.setCtdusr(Constant.USER_SYS);
		apikey.setCtddtm(Utils.now());
		apikey.setCtdpgm(Constant.PGM_REST);
		
		save(apikey);
	}
	@Override
	public void updateApiKey(ApiKey apiKey) {

		
		apiKey.setMdfusr(Constant.USER_SYS);
		apiKey.setLstmdf(Utils.now());
		apiKey.setMdfpgm(Constant.PGM_REST);
		
		
		update(apiKey);

		
	}

	
	@Override
	public void deleteApiKey(ApiKey  apiKey) {
		delete(apiKey);
	}

	
	


	@Override
	public List<ApiKey> findByApiKey(String api_key) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("api_key", api_key));
		
	    
		 @SuppressWarnings("unchecked")
			List<ApiKey> list = crit_list.list();
	     
		return  list;
	}

	@Override
	public List<ApiKey> findByAuthKey(String auth_key) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("auth_key", auth_key));
	    
		 @SuppressWarnings("unchecked")
			List<ApiKey> list = crit_list.list();
	     
		return  list;
	}

//	@Override
//	public List<ApiKey> findExt(String sso_id, String api_key, String auth_key, String clound_token) {
//		boolean is_emty = true;
//		Criteria crit_list = createEntityCriteria();
//		if (sso_id != null && !("".equals(sso_id))){
//			is_emty = false;
//			crit_list.add(Restrictions.eq("sso_id", sso_id));
//		}
//		if (api_key != null && !("".equals(api_key))){
//			is_emty = false;
//			crit_list.add(Restrictions.eq("api_key", api_key));	
//		}
//		
//		if (clound_token != null && !("".equals(clound_token))){
//			is_emty = false;
//			crit_list.add(Restrictions.eq("cld_token", clound_token));	
//		}
//		if (auth_key != null && !("".equals(auth_key))){
//			is_emty = false;
//			crit_list.add(Restrictions.eq("auth_key", auth_key));	
//		}
//		
//	    if (is_emty){
//	    	return null;
//	    }
//		 @SuppressWarnings("unchecked")
//			List<ApiKey> list = crit_list.list();
//	     
//		return  list;
//	}

	@Override
	public List<ApiKey> findActivedApiKey(String api_key, String auth_key) {
		Criteria crit_list = createEntityCriteria();
		crit_list.add(Restrictions.eq("auth_key", auth_key));
		crit_list.add(Restrictions.eq("api_key", api_key));
		crit_list.add(Restrictions.eq("active", 1));
	    
		 @SuppressWarnings("unchecked")
			List<ApiKey> list = crit_list.list();
	     
		return  list;
	}

	@Override
	public List<ApiKey> findExt(String sso_id, 
			String api_key, 
			String auth_key, 
			String cld_token, 
			Integer school_id,
			Integer class_id, 
			String role, 
			Integer active,
			String from_dt, 
			String to_dt, 
			Integer from_num,
			Integer max_result) {
		
		String str = 	"from ApiKey att where att.actflg = 'A' ";
		// SSO_ID
		if (sso_id != null && sso_id.trim().length() > 0){
			str = str +" and att.sso_id = '"+sso_id+"'"; 
		}
		// APK
		if (api_key != null && api_key.trim().length() > 0){
			str = str +" and att.api_key = '"+api_key+"'"; 
		}
		// AUTH
		if (auth_key != null && auth_key.trim().length() > 0){
			str = str +" and att.auth_key = '"+auth_key+"'"; 
		}
		// CLD TOKEN
		if (cld_token != null && cld_token.trim().length() > 0){
			str = str +" and att.cld_token = '"+cld_token+"'"; 
		}
		// SCHOOL
		if (school_id != null && school_id > 0){
			str = str +" and att.school_id = '"+school_id.intValue()+"'"; 
		}
		// CHLASS
		if (class_id != null && class_id > 0){
			str = str +" and att.class_id = '"+class_id.intValue()+"'"; 
		}
		// ROLE
		if (role != null && role.trim().length() > 0){
			str = str +" and att.role = '"+role+"'"; 
		}
		// ACTIVE
		if (active != null && active > 0){
			str = str +" and att.active = '"+active.intValue()+"'"; 
		}
		// FROM_DT
		if (from_dt != null ){
			str = str +" and att.last_request_dt >= '"+from_dt+"'";
		}
		// TO_DT
		if (to_dt != null ){
			str = str +" and att.last_request_dt <= '"+to_dt+"'";
		}
	
		
		
		// str = str +" order by att.id desc ";
		str = str +" order by att.last_request_dt desc ";
		Query query =  getSession().createQuery(str);
		query.setMaxResults(max_result);
		query.setFirstResult(from_num);
		
		
		
		@SuppressWarnings("unchecked")
		List<ApiKey>  ret= query.list();
		return ret;
		
		
	}

	@Override
	public Integer countExt(
			String sso_id, 
			String api_key, 
			String auth_key, 
			String cld_token, 
			Integer school_id,
			Integer class_id, 
			String role,
			Integer active, 
			String from_dt,
			String to_dt, 
			Integer from_row,
			Integer max_result) {
		String query = 	"select count(*)  from ApiKey att where att.actflg = 'A' ";
		// SSO_ID
		if (sso_id != null && sso_id.trim().length() > 0){
			query = query +" and att.sso_id = '"+sso_id+"'"; 
		}
		// APK
		if (api_key != null && api_key.trim().length() > 0){
			query = query +" and att.api_key = '"+api_key+"'"; 
		}
		// AUTH
		if (auth_key != null && auth_key.trim().length() > 0){
			query = query +" and att.auth_key = '"+auth_key+"'"; 
		}
		// CLD TOKEN
		if (cld_token != null && cld_token.trim().length() > 0){
			query = query +" and att.cld_token = '"+cld_token+"'"; 
		}
		// SCHOOL
		if (school_id != null && school_id > 0){
			query = query +" and att.school_id = '"+school_id.intValue()+"'"; 
		}
		// CHLASS
		if (class_id != null && class_id > 0){
			query = query +" and att.class_id = '"+class_id.intValue()+"'"; 
		}
		// ROLE
		if (role != null && role.trim().length() > 0){
			query = query +" and att.role = '"+role+"'"; 
		}
		// ACTIVE
		if (active != null && active > 0){
			query = query +" and att.active = '"+active.intValue()+"'"; 
		}
		// FROM_DT
		if (from_dt != null ){
			query = query +" and att.last_request_dt >= '"+from_dt+"'";
		}
		// TO_DT
		if (to_dt != null ){
			query = query +" and att.last_request_dt <= '"+to_dt+"'";
		}
	
		
		int count = ((Long)getSession().createQuery(query).uniqueResult()).intValue();
		return count;
	}


	
}
