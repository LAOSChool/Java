package com.itpro.restws.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.itpro.restws.helper.AuthenticationRequestWrapper;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.MultipartFromWrapper;
import com.itpro.restws.model.ActionLog;
import com.itpro.restws.securityimpl.UserContext;
import com.itpro.restws.service.ActionLogService;

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

	@Autowired
	ActionLogService actionLogService;
	
	private static final Logger logger = Logger.getLogger(TokenAuthenticationFilter.class);
	public static String LOG_METHODS="GET,POST,PUT, DELETE";

//	private static final String HEADER_TOKEN = "X-Auth-Token";
//	private static final String HEADER_USERNAME = "X-Username";
//	private static final String HEADER_PASSWORD = "X-Password";

	

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
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Check API_KEY Start
		checkBlankApiKey(httpRequest, httpResponse);

		// Check token
		boolean authenticated = false;
		if (canRequestProcessingContinue(httpRequest)) {
			authenticated = checkToken(httpRequest, httpResponse);
		}

		// Check Log out/Login
		if (canRequestProcessingContinue(httpRequest)) { //		if (canRequestProcessingContinue(httpRequest) && httpRequest.getMethod().equals("POST")) {
			// If we're not authenticated, we don't bother with logout at all.
			// Logout does not work in the same request with login - this does not make sense,
			// because logout works with token and login returns it.
			if (authenticated ) {
				checkLogout(httpRequest,httpResponse); // Logout and del auth_key
				// Check API_KEY is active
				if (canRequestProcessingContinue(httpRequest)) {
					checkActivedApiKey(httpRequest,httpResponse);
				}
				// Disable Admin from to Mobile (API_KEY)
				if (canRequestProcessingContinue(httpRequest)) {
					disableAdminToMobile(httpRequest,httpResponse);
				}
				
			}else{
				checkLogin(httpRequest, httpResponse);// Login and save api_key + sso_id to 2 tables ( auth_key and api_key)
			}
		}
		// Process chain 
		if (canRequestProcessingContinue(httpRequest)) {
			// Support access non-security API
			if (SecurityContextHolder.getContext().getAuthentication()  == null ){
				chain.doFilter (httpRequest, httpResponse);
			}else{
				UserContext usercontext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				ActionLog act = null;
				long startTime = System.currentTimeMillis();
				// Request wrapper Start
				// Neu muon log byte[] thi thay bang AuthenticationRequestWrapper
				
				// End
				HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(httpResponse);
				
				//boolean wrap_request_ok = true;
				boolean is_upload_file = false;
				if (	httpRequest.getRequestURL().toString().contains("upload") ||
						httpRequest.getRequestURL().toString().contains("notifies/create")
						){
					is_upload_file = true;
					
				}
				if (!is_upload_file){
					if (isMultipartForm(httpRequest)) {
						
						// Dump start
						act = null;
						MultipartFromWrapper myRequestWrapper = new MultipartFromWrapper(httpRequest);
						if (LOG_METHODS.toUpperCase().indexOf(myRequestWrapper.getMethod().toUpperCase()) >= 0){
							act = actionLogService.start_tracewrapper3(myRequestWrapper,usercontext.getUser());
							myRequestWrapper.setAttribute("x-actlog_id", act.getId());
						}
						try {
							
							chain.doFilter(myRequestWrapper, responseCopier); //chain.doFilter (httpRequest, httpResponse);
							responseCopier.flushBuffer();
				        } finally {
				        	if (act != null ){
								byte[] copy = responseCopier.getCopy();
								long endTime = System.currentTimeMillis();
								long executeTime = endTime - startTime;
								if (act != null ){
										actionLogService.end_trace(act.getId(), new String(copy, response.getCharacterEncoding()),
												responseCopier.getStatus(), executeTime);
									}							
				        	}
				        }
					}else{
						// Dump start
						// MyRequestWrapper myRequestWrapper = new MyRequestWrapper( httpRequest);
						AuthenticationRequestWrapper myRequestWrapper = new AuthenticationRequestWrapper(httpRequest);
						act = null;
						if (LOG_METHODS.toUpperCase().indexOf(myRequestWrapper.getMethod().toUpperCase()) >= 0){
							//act = actionLogService.start_tracewrapper(myRequestWrapper,usercontext.getUser());
							act = actionLogService.start_tracewrapper2(myRequestWrapper,usercontext.getUser());
							myRequestWrapper.setAttribute("x-actlog_id", act.getId());
						}
						
						
						try {
							chain.doFilter(myRequestWrapper, responseCopier); //chain.doFilter (httpRequest, httpResponse);
				            responseCopier.flushBuffer();
				        } finally {
							byte[] copy = responseCopier.getCopy();
							long endTime = System.currentTimeMillis();
							long executeTime = endTime - startTime;
							
							if (act !=null ){
								
								actionLogService.end_trace(act.getId(), new String(copy, response.getCharacterEncoding()),
										responseCopier.getStatus(), executeTime);
								
							}
				        	
				        }
					}
					
					// Dump end
				
				}else{
					// Dump start
					act = null;
					if (LOG_METHODS.toUpperCase().indexOf(httpRequest.getMethod().toUpperCase()) >= 0){
						act = actionLogService.start_trace(httpRequest,usercontext.getUser());
						httpRequest.setAttribute("x-actlog_id", act.getId());
					}
					try {
						chain.doFilter(request, responseCopier); //chain.doFilter (httpRequest, httpResponse);
			            responseCopier.flushBuffer();
			        } finally {
			        	if (act != null ){
							byte[] copy = responseCopier.getCopy();
							long endTime = System.currentTimeMillis();
							long executeTime = endTime - startTime;
							if (act != null ){
									actionLogService.end_trace(act.getId(), new String(copy, response.getCharacterEncoding()),
											responseCopier.getStatus(), executeTime);
								
							}
			        	}
			        }
				}
			
			}
			
		}
		
		logger.info(" === AUTHENTICATION: " + SecurityContextHolder.getContext().getAuthentication());

	}

	private void checkLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		
		String username = httpRequest.getHeader(Constant.HEADER_USERNAME);
		String password = httpRequest.getHeader(Constant.HEADER_PASSWORD);
		
		
		if (username != null && password != null) {
			doNotContinueWithRequestProcessing(httpRequest);
			checkUsernameAndPassword(username, password, httpRequest,httpResponse);
			
		}
		else{
			// Do nothing to support non-secure access non-secure/test
			// /non-secure/schools/{id}
//			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//			doNotContinueWithRequestProcessing(httpRequest);
		}
		
	}

	private void checkUsernameAndPassword(String username, String password,HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		TokenInfo tokenInfo = authenticationService.authenticate(username, password);
		if (tokenInfo != null) {
			httpResponse.setHeader(Constant.HEADER_AUTH_KEY, tokenInfo.getToken());
			httpResponse.getOutputStream().println("OK");
			httpResponse.getOutputStream().flush();
			
			// Sau khi Login thanh cong, save API_KEY/SSO_ID
			String api_key = httpRequest.getHeader(Constant.HEADER_API_KEY);
			authenticationService.loginApiKeySuccess(username, api_key,tokenInfo.getToken());
			//authenticationService.addAuthKey_to_ApiKey(api_key, tokenInfo.getToken());
		} else {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	

	/** Returns true, if request contains valid authentication token. */
	private boolean checkToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String token = httpRequest.getHeader(Constant.HEADER_AUTH_KEY);
		if (token == null) {
			// OK, continue to check_login
			return false;
			
//			if (currentLink(httpRequest).equals(Constant.LOGIN_LINK) || 
//					currentLink(httpRequest).equals(Constant.FORGOT_PASS)){
//				//OK, continue to check_login
//				return false;
//			}
			
			
			
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
			logger.info(" *** " + Constant.HEADER_AUTH_KEY + " valid for: " +
				SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			return true;
		} else {
			logger.info(" *** Invalid " + Constant.HEADER_AUTH_KEY + ' ' + token);
			//httpResponse.sendError(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
			httpResponse.sendError(HttpServletResponse.SC_CONFLICT);
			
			doNotContinueWithRequestProcessing(httpRequest);
		}
		return false;
	}

	private void checkLogout(HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws IOException {
		if (currentLink(httpRequest).equals(logoutLink)) {
			String token = httpRequest.getHeader(Constant.HEADER_AUTH_KEY);
			// we go here only authenticated, token must not be null
			authenticationService.logout(token);
			
			// Clear auth_key from api_key
			String api_key = httpRequest.getHeader(Constant.HEADER_API_KEY);
			
			if (api_key != null && !api_key.trim().equals("")) {
				authenticationService.logoutAuthKeySuccess(api_key, token);
			}
			
			
			
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
	
	
//	/** Returns true, if request contains valid apk_key. */
	private void checkActivedApiKey(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String api_key = httpRequest.getHeader(Constant.HEADER_API_KEY);
		String auth_key = httpRequest.getHeader(Constant.HEADER_AUTH_KEY);
		
		if ( authenticationService.checkActivedApiKey(api_key,auth_key)) {
			logger.info(" *** api_key: " + api_key + " : is OK ");
		} else {
			logger.error(" *** ERROR api_key:" + Constant.HEADER_API_KEY );
			httpResponse.sendError(HttpServletResponse. SC_NON_AUTHORITATIVE_INFORMATION);
			doNotContinueWithRequestProcessing(httpRequest);
		}
	}
	
	/** Returns true, if request contains valid apk_key. */
	private void checkBlankApiKey(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String api_key = httpRequest.getHeader(Constant.HEADER_API_KEY);
		
		if (api_key == null || api_key.trim().equals("")) {
			logger.info(" *** api_key is BLANK" + Constant.HEADER_API_KEY );
			httpResponse.sendError(HttpServletResponse. SC_NON_AUTHORITATIVE_INFORMATION);
			doNotContinueWithRequestProcessing(httpRequest);
		}
	}

	/////////////// Request/Response Dump
	
    public static class HttpServletResponseCopier extends HttpServletResponseWrapper {

        private ServletOutputStream outputStream;
        private PrintWriter writer;
        private ServletOutputStreamCopier copier;

        public HttpServletResponseCopier(HttpServletResponse response) throws IOException {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (writer != null) {
                throw new IllegalStateException("getWriter() has already been called on this response.");
            }

            if (outputStream == null) {
                outputStream = getResponse().getOutputStream();
                copier = new ServletOutputStreamCopier(outputStream);
            }

            return copier;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (outputStream != null) {
                throw new IllegalStateException("getOutputStream() has already been called on this response.");
            }

            if (writer == null) {
                copier = new ServletOutputStreamCopier(getResponse().getOutputStream());
                writer = new PrintWriter(new OutputStreamWriter(copier, getResponse().getCharacterEncoding()), true);
            }

            return writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            } else if (outputStream != null) {
                copier.flush();
            }
        }

        public byte[] getCopy() {
            if (copier != null) {
                return copier.getCopy();
            } else {
                return new byte[0];
            }
        }

    }
    public static class ServletOutputStreamCopier extends ServletOutputStream {
    	private ServletOutputStream origin;
        private ByteArrayOutputStream copy;

        public ServletOutputStreamCopier(ServletOutputStream org) throws IOException {
        	origin = org;
            this.copy = new ByteArrayOutputStream(1024);
        }

        @Override
        public void write(int b) throws IOException {
        	origin.write(b);
            copy.write(b);
        }

        public byte[] getCopy() {
            return copy.toByteArray();
        }

		@Override
		public boolean isReady() {
			return origin.isReady();
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			origin.setWriteListener(writeListener);
			
		}

    }
	
//	/** Returns true, if request contains valid apk_key. */
	private void disableAdminToMobile(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String api_key = httpRequest.getHeader(Constant.HEADER_API_KEY);
		

		boolean is_admin = hasRole(E_ROLE.ADMIN.getRole());
		if (canRequestProcessingContinue(httpRequest)){
			if (is_admin){
				String[] ignores_devices = Constant.NON_DEVICE_API_KEY;
				for (int i = 0;i< ignores_devices.length;i++){
					if (api_key.equalsIgnoreCase(ignores_devices[i])){
						return;
					}
				}
				doNotContinueWithRequestProcessing(httpRequest);
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
			
		}
		
		
	}
	protected boolean hasRole(String role) {
        // get security context from thread local
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null)
            return false;

        Authentication authentication = context.getAuthentication();
        if (authentication == null)
            return false;

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (role.equals(auth.getAuthority()))
                return true;
        }

        return false;
    }
	 private boolean isMultipartForm(HttpServletRequest aRequest){
		    return     
		      aRequest.getMethod().equalsIgnoreCase("POST") && 
		      aRequest.getContentType().startsWith("multipart/form-data")
		    ;
		  }
}
