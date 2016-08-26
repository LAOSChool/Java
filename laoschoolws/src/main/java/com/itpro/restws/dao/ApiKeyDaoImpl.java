package com.itpro.restws.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
		apikey.setCtdusr("HuyNQ-test");
		apikey.setCtddtm(Utils.now());
		apikey.setCtdpgm("RestWS");
		apikey.setCtddtm(Utils.now());
		save(apikey);
	}
	@Override
	public void updateApiKey(ApiKey apiKey) {
		apiKey.setMdfusr("HuyNQ-test");
		apiKey.setLstmdf(Utils.now());
		apiKey.setMdfpgm("RestWS");
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

	@Override
	public List<ApiKey> findExt(String sso_id, String api_key, String auth_key, String clound_token) {
		boolean is_emty = true;
		Criteria crit_list = createEntityCriteria();
		if (sso_id != null && !("".equals(sso_id))){
			is_emty = false;
			crit_list.add(Restrictions.eq("sso_id", sso_id));
		}
		if (api_key != null && !("".equals(api_key))){
			is_emty = false;
			crit_list.add(Restrictions.eq("api_key", api_key));	
		}
		
		if (clound_token != null && !("".equals(clound_token))){
			is_emty = false;
			crit_list.add(Restrictions.eq("cld_token", clound_token));	
		}
		if (auth_key != null && !("".equals(auth_key))){
			is_emty = false;
			crit_list.add(Restrictions.eq("auth_key", auth_key));	
		}
		
	    if (is_emty){
	    	return null;
	    }
		 @SuppressWarnings("unchecked")
			List<ApiKey> list = crit_list.list();
	     
		return  list;
	}

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

	
	


	
	
}
