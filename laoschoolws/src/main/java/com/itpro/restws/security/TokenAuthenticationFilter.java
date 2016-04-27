package com.itpro.restws.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.itpro.restws.helper.Constant;

/**
 * Takes care of HTTP request/response pre-processing for login/logout and token check.
 * Login can be performed on any URL, logout only on specified {@link #logoutLink}.
 * All the interaction with Spring Security should be performed via {@link AuthenticationService}.
 * <p>
 * {@link SecurityContextHolder} is used here only for debug outputs. While this class
 * is configured to be used by Spring Security (configured filter on FORM_LOGIN_FILTER position),
 * but it doesn't really depend on it at all.
 */

public final class TokenAuthenticationFilter extends GenericFilterBean {

	private static final Logger logger = Logger.getLogger(TokenAuthenticationFilter.class);

//	private static final String HEADER_TOKEN = "X-Auth-Token";
//	private static final String HEADER_USERNAME = "X-Username";
//	private static final String HEADER_PASSWORD = "X-Password";

	private static final String HEADER_API_KEY = "api_key";
	private static final String HEADER_AUTH_KEY = "auth_key";
	private static final String HEADER_USERNAME = "sso_id";
	private static final String HEADER_PASSWORD = "password";

	/**
	 * Request attribute that indicates that this filter will not continue with the chain.
	 * Handy after login/logout, etc.
	 */
	private static final String REQUEST_ATTR_DO_NOT_CONTINUE = "MyAuthenticationFilter-doNotContinue";

	private final String logoutLink;
	
	private final AuthenticationService authenticationService;

	public TokenAuthenticationFilter(AuthenticationService authenticationService, String logoutLink) {
		this.authenticationService = authenticationService;
		this.logoutLink = logoutLink;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
//		System.out.println(" *** MyAuthenticationFilter.doFilter");
		logger.info(" *** MyAuthenticationFilter.doFilter");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		Map<String, String[]> parameters = request.getParameterMap();
		String data = parameters.toString();
		// Check API_KEY Start
		// To disable check API_KEY, just comment this code block
		checkApiKey(httpRequest, httpResponse);
		// Check API_KEY End
		boolean authenticated = checkToken(httpRequest, httpResponse);

//		if (canRequestProcessingContinue(httpRequest) && httpRequest.getMethod().equals("POST")) {
		if (canRequestProcessingContinue(httpRequest)) {
			// If we're not authenticated, we don't bother with logout at all.
			// Logout does not work in the same request with login - this does not make sense,
			// because logout works with token and login returns it.
			if (authenticated ) {
				checkLogout(httpRequest,httpResponse);
			}else{
				checkLogin(httpRequest, httpResponse);
			}
		}

		if (canRequestProcessingContinue(httpRequest)) {
			chain.doFilter(request, response);
		}
		logger.info(" === AUTHENTICATION: " + SecurityContextHolder.getContext().getAuthentication());

	}

	private void checkLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		
		String username = httpRequest.getHeader(HEADER_USERNAME);
		String password = httpRequest.getHeader(HEADER_PASSWORD);
		
		
		if (username != null && password != null) {
			doNotContinueWithRequestProcessing(httpRequest);
			checkUsernameAndPassword(username, password, httpResponse);
		}
//		else{
//			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//		}
		
	}

	private void checkUsernameAndPassword(String username, String password, HttpServletResponse httpResponse) throws IOException {
		TokenInfo tokenInfo = authenticationService.authenticate(username, password);
		if (tokenInfo != null) {
			httpResponse.setHeader(HEADER_AUTH_KEY, tokenInfo.getToken());
			httpResponse.getOutputStream().println("OK");
			httpResponse.getOutputStream().flush();
		} else {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	

	/** Returns true, if request contains valid authentication token. */
	private boolean checkToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String token = httpRequest.getHeader(HEADER_AUTH_KEY);
		if (token == null) {
			// OK, continue to check_login
			//return false;
			
			if (currentLink(httpRequest).equals(Constant.LOGIN_LINK) || 
					currentLink(httpRequest).equals(Constant.FORGOT_PASS)){
				//OK, continue to check_login
				return false;
			}
//			else{
//				SecurityContextHolder.clearContext();
//		        HttpSession session = httpRequest.getSession(false);
//		        if (session != null) {
//		            session.invalidate();
//		        }
//				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//			}
		}

		if (authenticationService.checkToken(token)) {
			logger.info(" *** " + HEADER_AUTH_KEY + " valid for: " +
				SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			return true;
		} else {
			logger.info(" *** Invalid " + HEADER_AUTH_KEY + ' ' + token);
			httpResponse.sendError(HttpServletResponse. SC_NON_AUTHORITATIVE_INFORMATION);
			doNotContinueWithRequestProcessing(httpRequest);
		}
		return false;
	}

	private void checkLogout(HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws IOException {
		if (currentLink(httpRequest).equals(logoutLink)) {
			String token = httpRequest.getHeader(HEADER_AUTH_KEY);
			// we go here only authenticated, token must not be null
			authenticationService.logout(token);
			httpResponse.getOutputStream().println("Request was successfully");
			httpResponse.getOutputStream().flush();
			httpResponse.flushBuffer();
			doNotContinueWithRequestProcessing(httpRequest);
		}
	}

	
	
	// or use Springs util instead: new UrlPathHelper().getPathWithinApplication(httpRequest)
	// shame on Servlet API for not providing this without any hassle :-(
	private String currentLink(HttpServletRequest httpRequest) {
		if (httpRequest.getPathInfo() == null) {
			return httpRequest.getServletPath();
		}
		return httpRequest.getServletPath() + httpRequest.getPathInfo();
	}

	/**
	 * This is set in cases when we don't want to continue down the filter chain. This occurs
	 * for any {@link HttpServletResponse#SC_UNAUTHORIZED} and also for login or logout.
	 */
	private void doNotContinueWithRequestProcessing(HttpServletRequest httpRequest) {
		httpRequest.setAttribute(REQUEST_ATTR_DO_NOT_CONTINUE, "");
	}

	private boolean canRequestProcessingContinue(HttpServletRequest httpRequest) {
		return httpRequest.getAttribute(REQUEST_ATTR_DO_NOT_CONTINUE) == null;
	}
	
	
	/** Returns true, if request contains valid apk_key. */
	private void checkApiKey(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String api_key = httpRequest.getHeader(HEADER_API_KEY);
		
		if (api_key != null && authenticationService.checkApiKey(api_key)) {
			logger.info(" *** api_key: " + HEADER_API_KEY + " : is valid ");
		} else {
			logger.info(" *** Invalid api_key:" + HEADER_API_KEY );
			httpResponse.sendError(HttpServletResponse. SC_NON_AUTHORITATIVE_INFORMATION);
			doNotContinueWithRequestProcessing(httpRequest);
		}
	}
}
