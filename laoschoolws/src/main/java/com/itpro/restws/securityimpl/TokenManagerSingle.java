package com.itpro.restws.securityimpl;

import com.itpro.restws.dao.AuthenKeyDao;
import com.itpro.restws.dao.ClassDao;
import com.itpro.restws.dao.UserDao;
import com.itpro.restws.model.AuthenKey;
import com.itpro.restws.model.User;
import com.itpro.restws.security.TokenInfo;
import com.itpro.restws.security.TokenManager;
import com.itpro.restws.service.UserService;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;

/**
 * Implements simple token manager, that keeps a single token for each user. If user logs in again,
 * older token is invalidated.
 */


public class TokenManagerSingle implements TokenManager {
	private static final Logger logger = Logger.getLogger(TokenManagerSingle.class);
	private Map<String, UserDetails> validUsers = new HashMap<>();
	
	@Autowired
	private AuthenKeyDao authenKeyDao;
	@Autowired
	private UserDao userDao;;

	/**
	 * This maps system users to tokens because equals/hashCode is delegated to User entity.
	 * This can store either one token or list of them for each user, depending on what you want to do.
	 * Here we store single token, which means, that any older tokens are invalidated.
	 */
	private Map<UserDetails, TokenInfo> tokens = new HashMap<>();

	@Override
	public TokenInfo createNewToken(UserDetails userDetails) {
//		String token;
//		do {
//			token = generateToken();
//		} while (validUsers.containsKey(token));
//
//		TokenInfo tokenInfo = new TokenInfo(token, userDetails);
//		
//		removeUserDetails(userDetails);
//		UserDetails previous = validUsers.put(token, userDetails);
//		if (previous != null) {
//			logger.info(" *** SERIOUS PROBLEM HERE - we generated the same token (randomly?)!");
//			return null;
//		}
//		tokens.put(userDetails, tokenInfo);
//
//		return tokenInfo;
		
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
		byte[] tokenBytes = new byte[32];
		new SecureRandom().nextBytes(tokenBytes);
		return new String(Base64.encode(tokenBytes), StandardCharsets.UTF_8);
	}

	@Override
	public void removeUserDetails(UserDetails userDetails) {
		TokenInfo token = tokens.remove(userDetails);
		if (token != null) {
			validUsers.remove(token.getToken());
		}
	}

	@Override
	public UserDetails removeToken(String token) {
		UserDetails userDetails = validUsers.remove(token);
		if (userDetails != null) {
			tokens.remove(userDetails);
		}
		return userDetails;
	}

	@Override
	public UserDetails getUserDetails(String token) {
		//return validUsers.get(token);
		AuthenKey autheKey = authenKeyDao.findByToken(token);
		User user = userDao.findBySSO(autheKey.getSso_id());
		if (user != null ){
			return new UserContext(user);
		}
		return null;
	}

	@Override
	public Collection<TokenInfo> getUserTokens(UserDetails userDetails) {
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
		return Collections.unmodifiableMap(validUsers);
	}
}
