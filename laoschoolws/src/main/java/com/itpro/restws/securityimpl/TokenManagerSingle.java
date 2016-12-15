package com.itpro.restws.securityimpl;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;

import com.itpro.restws.dao.AuthenKeyDao;
import com.itpro.restws.dao.UserDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.AuthenKey;
import com.itpro.restws.model.User;
import com.itpro.restws.security.TokenInfo;
import com.itpro.restws.security.TokenManager;
import com.itpro.restws.service.ApiKeyService;

/**
 * Implements simple token manager, that keeps a single token for each user. If user logs in again,
 * older token is invalidated.
 */


public class TokenManagerSingle implements TokenManager {
	private static final Logger logger = Logger.getLogger(TokenManagerSingle.class);
	//private Map<String, UserDetails> validUsers = new HashMap<>();
	
	@Autowired
	private AuthenKeyDao authenKeyDao;
	
	@Autowired
	protected ApiKeyService apiKeyService;
	
	@Autowired
	private UserDao userDao;;

	/**
	 * This maps system users to tokens because equals/hashCode is delegated to User entity.
	 * This can store either one token or list of them for each user, depending on what you want to do.
	 * Here we store single token, which means, that any older tokens are invalidated.
	 */
	//private Map<UserDetails, TokenInfo> tokens = new HashMap<>();

	@Override
	public TokenInfo createNewToken(UserDetails userDetails,String api_key) {
		logger.info("TokenManagerSingle.createNewToken() Start");
		
		//Delete all previous auth_key
		if (hasRole(new String[]{"ROLE_ADMIN","ROLE_TEACHER","ROLE_CLS_PRESIDENT"},userDetails)){
			removeUserDetails(userDetails);
		}else{
			// Check previous existing api_key & auth_key
			ArrayList<ApiKey> prev_api_keys = null;
			if (api_key != null && api_key.trim().length()> 0 && (!isIgnoredKey(api_key))){
				prev_api_keys = apiKeyService.findByApiKey(api_key);
			}
			// Clear all auth_key of existing api_key
			if (prev_api_keys != null && prev_api_keys.size() > 0){
				for (ApiKey prev_api:prev_api_keys){
					String str_api_key = prev_api.getApi_key();
					String str_auth_key = prev_api.getAuth_key();
					// Delete token
					AuthenKey auth = authenKeyDao.findByToken(str_auth_key);
					if (auth != null ){
						authenKeyDao.deleteToken(auth);
					}
					// Clear api_key
					apiKeyService.clearByApiKey(str_api_key);
				}
			}
			
			// Delete if over max session
			List<AuthenKey> list  =  authenKeyDao.findBySsoID(userDetails.getUsername());// order ASC by ID
			if (list != null && list.size() >= Constant.MAX_STUDENT_SESSION){
				for (int i = 0; i< list.size() - Constant.MAX_STUDENT_SESSION+1;i++){
					AuthenKey authen =list.get(i);
					authenKeyDao.deleteToken(authen);
					// del api_key
					apiKeyService.clearByByAuthKey(authen.getAuth_key());
				}
			}
			
		}
		// Generate new auth_key
		List<AuthenKey> list  =  authenKeyDao.findBySsoID(userDetails.getUsername());
		String token;
		boolean is_same;
		do{
			is_same = false;
			token = generateToken();
			if (list == null || (list.size()<= 0 ) ){	break;}
			for (int i=0;i<list.size();i++){
				if (token.equals(list.get(i).getAuth_key())){
					is_same = true;
				}
			}
		}while(is_same);
		// Save to DB
		AuthenKey authenKey  = new AuthenKey();
		authenKey.setAuth_key(token);
		authenKey.setSso_id(userDetails.getUsername());
		authenKeyDao.saveToken(authenKey);
		
		TokenInfo tokenInfo = new TokenInfo(token, userDetails);
		return tokenInfo;
	}

	private String generateToken() {
		logger.info("TokenManagerSingle.generateToken() Start");
		
		byte[] tokenBytes = new byte[32];
		new SecureRandom().nextBytes(tokenBytes);
		return new String(Base64.encode(tokenBytes), StandardCharsets.UTF_8);
	}

	@Override
	public void removeUserDetails(UserDetails userDetails) {
		logger.info("TokenManagerSingle.removeUserDetails() Start");
		
		int ret = authenKeyDao.deleteBySso(userDetails.getUsername());
		// del api_key
		apiKeyService.clearBySSoID(userDetails.getUsername());
		
		logger.info("delete: "+ret+" tokens for user:"+userDetails.getUsername());
	}

	@Override
	public UserDetails removeToken(String token) {
		logger.info("TokenManagerSingle.removeToken() Start");
		
		AuthenKey authkey = authenKeyDao.findByToken(token);
		User user = userDao.findBySSO(authkey.getSso_id());
		authenKeyDao.deleteToken(authkey);
		// del api_key
		apiKeyService.clearByByAuthKey(token);
		
		return new UserContext(user);
	}

	@Override
	public UserDetails getUserDetails(String token) {
		logger.info("TokenManagerSingle.getUserDetails() Start");
		//return validUsers.get(token);
		AuthenKey authkey = authenKeyDao.findByToken(token);
		if (authkey == null )
			return null;
		User user = userDao.findBySSO(authkey.getSso_id());
		if (user != null ){
			return new UserContext(user);
		}
		return null;
	}

	@Override
	public Collection<TokenInfo> getUserTokens(UserDetails userDetails) {
		logger.info("TokenManagerSingle.getUserTokens() Start");
		
		//return Arrays.asList(tokens.get(userDetails));
		ArrayList<TokenInfo> arr = new ArrayList<TokenInfo>();
		List<AuthenKey> list  =  authenKeyDao.findBySsoID(userDetails.getUsername());
		for (int i=0;i<list.size();i++){
			TokenInfo info = new TokenInfo(list.get(i).getAuth_key(), userDetails);
			arr.add(info);
		}
		return arr;
		
	}

	@Override
	public Map<String, UserDetails> getValidUsers() {
		logger.info("TokenManagerSingle.getValidUsers() Start");
		
		Map<String, UserDetails> map = new HashMap<String, UserDetails>();
		List<AuthenKey> list  =  authenKeyDao.getNonExpired();
		for (int i=0;i<list.size();i++){
			User user = userDao.findBySSO(list.get(i).getSso_id());
			if (user != null ){
				UserDetails userDetails = new UserContext(user);
				map.put(list.get(i).getAuth_key(), userDetails);
			}
		}
		return map;
	}
	
	private boolean hasRole(String[] roles,UserDetails userDetails){
		logger.info("TokenManagerSingle.hasRole() Start");
		
		boolean result = false;
		  for (GrantedAuthority authority : userDetails.getAuthorities()) {
		        String userRole = authority.getAuthority();
		        for (String role : roles) {
		            if (role.equals(userRole)) {
		                result = true;
		                break;
		            }
		        }

		        if (result) {
		            break;
		        }
		    }
		  return result;
	}
	private boolean isIgnoredKey(String api_key){
		
		String[] ignores_devices = Constant.NON_DEVICE_API_KEY;
		for (int i = 0;i< ignores_devices.length;i++){
			if (api_key.equalsIgnoreCase(ignores_devices[i])){
				return true;
			}
		}
		return false;
	}
}
