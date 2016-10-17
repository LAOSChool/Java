package com.itpro.restws.securityimpl;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ApiKey;
import com.itpro.restws.model.User;
import com.itpro.restws.security.AuthenticationService;
import com.itpro.restws.security.TokenInfo;
import com.itpro.restws.security.TokenManager;
import com.itpro.restws.service.ApiKeyService;
import com.itpro.restws.service.UserService;

/**
 * Service responsible for all around authentication, token checks, etc.
 * This class does not care about HTTP protocol at all.
 */
public class AuthenticationServiceDefault implements AuthenticationService {
	private static final Logger logger = Logger.getLogger(AuthenticationServiceDefault.class);
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private UserService userService;
	@Autowired
	private ApiKeyService apiKeyService;
//	@Autowired
//	private AuthenKeyDao authenKeyDao;
	
	private final AuthenticationManager authenticationManager;
	private final TokenManager tokenManager;
	//private final ApiKeyManager apiKeyManager;

	public AuthenticationServiceDefault(AuthenticationManager authenticationManager, TokenManager tokenManager) {
		this.authenticationManager = authenticationManager;
		this.tokenManager = tokenManager;
	}

	@PostConstruct
	public void init() {
		logger.info(" *** AuthenticationServiceImpl.init with: " + applicationContext);
	}

	@Override
	public TokenInfo authenticate(String login, String password) {
		logger.info(" *** AuthenticationServiceImpl.authenticate");
		// Here principal=username, credentials=password
		Authentication authentication = new UsernamePasswordAuthenticationToken(login, password);
		try {
			authentication = authenticationManager.authenticate(authentication);
			// Here principal=UserDetails (UserContext in our case), credentials=null (security reasons)
			SecurityContextHolder.getContext().setAuthentication(authentication);

			if (authentication.getPrincipal() != null) {
				UserDetails userContext = (UserDetails) authentication.getPrincipal();
				TokenInfo newToken = tokenManager.createNewToken(userContext);
				if (newToken == null) {
					return null;
				}
				return newToken;
			}
		} catch (AuthenticationException e) {
			logger.info(" *** AuthenticationServiceImpl.authenticate - FAILED: " + e.toString());
		}
		return null;
	}

	@Override
	public boolean checkToken(String token) {
		logger.info(" *** AuthenticationServiceImpl.checkToken");

		UserDetails userDetails = tokenManager.getUserDetails(token);
		if (userDetails == null) {
			return false;
		}

		Authentication securityToken = new PreAuthenticatedAuthenticationToken(
			userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(securityToken);

		return true;
	}

	@Override
	public void logout(String token) {
		logger.info(" *** AuthenticationServiceImpl.logout");
		
		UserDetails logoutUser = tokenManager.removeToken(token);
		logger.info(" *** AuthenticationServiceImpl.logout: " + logoutUser);
		SecurityContextHolder.clearContext();
		
//		try {
//	        HttpSession session = request.getSession(false);
//	        if (session != null) {
//	            session.invalidate();
//	        }
//
//	        SecurityContextHolder.clearContext();
//
//	    } catch (Exception e) {
//	        logger.log(LogLevel.INFO, "Problem logging out.");
//	    }
	    
	}

	@Override
	public UserDetails currentUser() {
		logger.info(" *** AuthenticationServiceImpl.currentUser");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		
		User user = userService.findBySso(authentication.getPrincipal().toString());
		if (user != null ){
			return new UserContext(user);
		}else {
			return null;
		}
	}

	@Override
	public void loginApiKeySuccess(String sso_id, String api_key,String auth_key) {
		logger.info(" *** AuthenticationServiceImpl.loginApiKeySuccess");
		
		apiKeyService.loginApiKeySuccess(sso_id, api_key,auth_key);
		
	}


	@Override
	public void logoutAuthKeySuccess( String api_key, String auth_key){
		logger.info(" *** AuthenticationServiceImpl.logoutAuthKeySuccess");
		apiKeyService.logoutApiKey(api_key);
		
	}

	
	@Override
	public boolean checkActivedApiKey(String api_key, String auth_key) {
		logger.info(" *** AuthenticationServiceImpl.checkActivedApiKey");
		if (auth_key == null || auth_key.trim().length() == 0){
			return false;
		}
		
		if (api_key == null || api_key.trim().length() == 0){
			return false;
		}
		if (apiKeyService.isIgnoredKey(api_key)){
			return true;
		}
		
		
		
		//if (Constant.API_KEY.equalsIgnoreCase(api_key)){
		//ArrayList<ApiKey> list = apiKeyService.findByApiKey(api_key); // Phai check theo ca auth_key
		ArrayList<ApiKey> list = apiKeyService.findActivedApiKey(api_key, auth_key);
		if (list != null && list.size() > 0){
			// Update last request DT
			for (ApiKey tmp : list){
				tmp.setLast_request_dt(Utils.now());
				apiKeyService.updateApiKey(tmp);
				break;
			}
			// update user context
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserContext) {
				User user =  ((UserContext)principal).getUser();
				if (user != null){
					user.setApi_key(list.get(0));
				}
			}

			
			return true;
		}else {
			return false;
		}

	}
	
	
}
