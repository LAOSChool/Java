package com.itpro.restws.controller;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.User;
import com.itpro.restws.securityimpl.UserContext;
import com.itpro.restws.service.ActionLogService;
import com.itpro.restws.service.UserService;
/**
 * Controller with REST API. Access to login is generally permitted, stuff in
 * /secure/ sub-context is protected by configuration. Some security annotations are
 * thrown in just to make a point.
 * Notice:
 * Consume should be the type of data that the web service expects to receive
 * Produces should be the type of data that the web service will return
 */
//Here @RestController is shorthand of = @Controller + @ResponseBody
// Where every method returns a domain object instead of a view
@RestController 
public class MainRestController {
	private static final Logger logger = Logger.getLogger(MainRestController.class);
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	ActionLogService actionLogService;
//	@Autowired
//	private ResourceBundleMessageSource messageSource;// Store messages
	@Autowired
	private UserService userService;	
	
//	@Autowired
//	private MasterTblService masterTblService;
	

	
//	@Autowired
//	private AuthenticationService authenticationService;

//	@Autowired
//	private TokenManager tokenManager;
//	@Autowired
//	private PermitService permitService;

	
	@PostConstruct
	public void init() {
		logger.info(" *** MainRestController.init with: " + applicationContext);
	}
	


	@RequestMapping("/test")
	public String test() {
		System.out.println(" *** MainRestController.test");
		// Spring Security dependency is unwanted in controller, typically some @Component (UserContext) hides it.
		// Not that we don't use Spring Security annotations anyway...
		return "SecurityContext: " + SecurityContextHolder.getContext();
	}


		
	// standard JSR 250 annotation
	@RolesAllowed("ADMIN")
	@RequestMapping("/admin")
	public String admin() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		return "Cool, you're admin!";
	}


//	@RequestMapping("/secure/mytokens")
//	public Collection<TokenInfo> myTokens() {
//		System.out.println(" *** MainRestController.myTokens");
//		logger.info(" *** MainRestController.myTokens");
//		UserDetails currentUser = authenticationService.currentUser();
//		return tokenManager.getUserTokens(currentUser);
//	}

	// Spring annotation virtually equivalent with @RolesAllowed - except for...
	// WARNING: @Secured by default works only with roles starting with ROLE_
	// prefix, see this for more:
	// http://bluefoot.info/howtos/spring-security-adding-a-custom-role-prefix/
	// I don't want to mess with RoleVoters - that's why ADMIN does NOT have
	// access to this page
	@Secured({ "ROLE_SPECIAL", "ROLE_ADMIN" })
	@RequestMapping("/secure/special")
	public String special() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		return "ROLE_SPECIAL users should have access.";
	}

	// Spring annotation that speaks SpEL!
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping("/secure/allusers")
//	public Map<String, UserDetails> allUsers() {
//		// System.out.println(" *** MainRestController.allUsers");
//		logger.info(" *** MainRestController.allUsers");
//		return tokenManager.getValidUsers();
//	}
	
	
	/**
	 * @return true if the user has one of the specified roles.
	 */
	protected boolean hasRole(String[] roles) {
		logger.info(" *** MainRestController.hasRole");
	    boolean result = false;
	    for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
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
	


	
	@RequestMapping("/Access_Denied")
	public RespInfo Access_Denied(
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		//System.out.println(" *** MainRestController.Access_Denied");
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		// Spring Security dependency is unwanted in controller, typically some
		// @Component (UserContext) hides it.
		// Not that we don't use Spring Security annotations anyway...
		//return "Access_Denied://SecurityContext: " + SecurityContextHolder.getContext();
		RespInfo rsp = new RespInfo(HttpStatus.FORBIDDEN.value(),"Error", request.getRequestURL().toString(), "Access is denied");
		// return "Access_Denied";
		return rsp;
	}

	
	

	
	
	
	
	
	
//	@RequestMapping(value="/api/masters/{tbl_name}",method = RequestMethod.GET)
//	@ResponseStatus(value=HttpStatus.OK)	
//	public ListEnt getMaster(
//			 @PathVariable String tbl_name			
//			) {
//		logger.info(" *** MainRestController.getMaster");
//		
//		int total_row = 0;
//		int from_row = 0;
//		int max_result = Constant.MAX_RESP_ROW;;
//		
//		ListEnt listResp = new ListEnt();
//		User user = getCurrentUser();
//    	// Count user
//    	total_row = masterTblService.countBySchool(tbl_name,  user.getSchool_id());
//    	if (total_row > Constant.MAX_RESP_ROW){
//    		max_result = Constant.MAX_RESP_ROW;
//    	}else{
//    		max_result = total_row;
//    	}
//		logger.info("Master:"+ tbl_name+ " count: total_row : "+total_row);
//		// Query class by school id
//		ArrayList<MTemplate> masters = (ArrayList<MTemplate>) masterTblService.findBySchool(tbl_name, user.getSchool_id(), from_row, max_result);
//		
//		listResp.setList(masters);
//		listResp.setFrom_row(from_row);
//		listResp.setTo_row(from_row + max_result);
//		listResp.setTotal_count(total_row);
//	    return listResp;
//	}
//	
//	@RequestMapping(value = "/api/masters/create/{tbl_name}", method = RequestMethod.POST)
//	@ResponseStatus(value=HttpStatus.OK)	
//	 public MasterBase createMaster(
//			 @PathVariable String  tbl_name,
//			 @RequestBody MTemplate mtemplate
//			 ) {
//		logger.info(" *** MainRestController.createMaster/{tbl_name}:"+tbl_name);
//
//		return masterTblService.insertMTemplate(tbl_name, mtemplate);
//	 }
//	
//	@RequestMapping(value = "/api/masters/update/{tbl_name}", method = RequestMethod.POST)
//	@ResponseStatus(value=HttpStatus.OK)	
//	 public MasterBase updateMaster(
//			 @PathVariable String  tbl_name,
//			 @RequestBody MTemplate mtemplate
//			 ) {
//		logger.info(" *** MainRestController.createMaster/{tbl_name}:"+tbl_name);
//
//		return masterTblService.updateMTemplate(tbl_name, mtemplate);
//	 }
//
//	@RequestMapping(value = "/api/masters/delete/{tbl_name}/{id}", method = RequestMethod.POST)
//	@ResponseStatus(value=HttpStatus.OK)	
//	 public String delMaster(
//			 @PathVariable String  tbl_name,
//			 @PathVariable int  id
//			 ) {
//		logger.info(" *** MainRestController.delMaster/{table}/{id}:"+tbl_name+"/"+id);
//
//	    return "Request was successfully, delMaster:"+ tbl_name + " of id: "+id;
//	 }
//	
//	@RequestMapping(value = "/api/masters/{tbl_name}/{id}", method = RequestMethod.GET)
//	@ResponseStatus(value=HttpStatus.OK)	
//	 public MasterBase getMaster(
//			 @PathVariable String  tbl_name,
//			 @PathVariable int  id
//			 ) {
//		logger.info(" *** MainRestController.getMaster/{table}/{id}:"+tbl_name+"/"+id);
//
//		
//		MTemplate template = masterTblService.findById(tbl_name, id);
//	    return template;
//	 }
	
	public String currentMethod (	
			@Context final HttpServletResponse response,
			@Context final HttpServletRequest request){
		return request.getMethod().toUpperCase();
	}
	
	protected User loadCurrentUser(){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		logger.info(" *** BaseController.getUser");
		String userName = null;
		User user = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		if (userName!= null){
			user =  userService.findBySso(userName);
		}
		return user;
	}
	protected User getCurrentUser(){
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		User user  = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		

		if (principal instanceof UserContext) {
			return ((UserContext)principal).getUser();
		} else {
			String sso = principal.toString();
			user = userService.findBySso(sso);
		}
		return user;
	}

	@Secured({"ROLE_ADMIN" })
	@RequestMapping(value = "/api/sms/log", method = RequestMethod.POST)
	public String log(String content,
			@Context final HttpServletResponse response,
			@Context final HttpServletRequest request
			) {
		
		if (content != null && content.length() > 0){
		logger.info("\n====== API LOG ==== START");
		logger.info(content);
		logger.info("====== API LOG ==== END\n");
		}
		return "DONE";
	}	
			
}
