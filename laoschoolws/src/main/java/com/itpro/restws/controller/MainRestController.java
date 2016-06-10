package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.dao.TermDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.MTemplate;
import com.itpro.restws.model.MasterBase;
import com.itpro.restws.model.School;
import com.itpro.restws.model.SchoolTerm;
import com.itpro.restws.model.SysTemplate;
import com.itpro.restws.model.User;
import com.itpro.restws.securityimpl.UserContext;
import com.itpro.restws.service.ClassService;
import com.itpro.restws.service.ExamResultService;

import com.itpro.restws.service.MasterTblService;
import com.itpro.restws.service.SchoolService;
import com.itpro.restws.service.SysTblService;
import com.itpro.restws.service.TimetableService;
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

//	@Autowired
//	private ResourceBundleMessageSource messageSource;// Store messages
	
	@Autowired
	private UserService userService;	
	@Autowired
	private ClassService classService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ExamResultService examResultService;
	
	
	
	@Autowired
	private TimetableService timetableService;

	
	@Autowired
	private MasterTblService masterTblService;
	
	@Autowired
	private SysTblService sysTblService;
	
	@Autowired
	private TermDao termDao;

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
		logger.info(" *** MainRestController.admin");
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
		logger.info(" *** MainRestController.secure.special");
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
	public String Access_Denied() {
		//System.out.println(" *** MainRestController.Access_Denied");
		logger.info(" *** MainRestController.Access_Denied");
		// Spring Security dependency is unwanted in controller, typically some
		// @Component (UserContext) hides it.
		// Not that we don't use Spring Security annotations anyway...
		return "Access_Denied://SecurityContext: " + SecurityContextHolder.getContext();
	}

	
	

	
	
	
	@RequestMapping(value="/api/schools",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public List<School> getSchools(
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			@RequestParam(value="filter_user_id",required =false) String filter_user_id,			
			@RequestParam(value="filter_sts", defaultValue="Active",required =false) String filter_sts
			) {
		
		logger.info(" *** MainRestController.getSchools");
	   	return schoolService.findActive();
	}
	
	@RequestMapping(value = "/api/schools/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public School getSchool(
				@PathVariable int  id,
				@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getSchool/{id}:"+id);
		School school = null;

	    response.setStatus(HttpServletResponse.SC_OK);
	    try {
	    	school = schoolService.findById(Integer.valueOf(id));
			logger.info("Schoo : "+school.toString());
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController.ERROR:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }
	    finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    return school;
	 }

	
	@RequestMapping(value="/api/schools/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public School createSchool(
			@RequestBody School school,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.school.create");
		 //school = schoolService.findById(1);
		 return schoolService.insertSchool(school);
		 
	}
	
	@RequestMapping(value="/api/schools/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public School updateSchool(
			@RequestBody School school,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.schools.update");
			 school = schoolService.findById(Integer.valueOf(1));
		 return schoolService.updateSchool(school);
	}
	
	@RequestMapping(value = "/api/schools/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delSchool(
			 @PathVariable int  id,
			@Context final HttpServletResponse response
			 
			 ) {
		logger.info(" *** MainRestController.delSchool/{school_id}:"+id);

	    return "Request was successfully, delete school of id: "+id;
	 }
	

	
	
	
	
	@RequestMapping(value="/api/masters/{tbl_name}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getMaster(
			 @PathVariable String tbl_name			
			) {
		logger.info(" *** MainRestController.getMaster");
		
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
		ListEnt listResp = new ListEnt();
		User user = getCurrentUser();
    	// Count user
    	total_row = masterTblService.countBySchool(tbl_name,  user.getSchool_id());
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
		logger.info("Master:"+ tbl_name+ " count: total_row : "+total_row);
		// Query class by school id
		ArrayList<MTemplate> masters = (ArrayList<MTemplate>) masterTblService.findBySchool(tbl_name, user.getSchool_id(), from_row, max_result);
		
		listResp.setList(masters);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
	    return listResp;
	}
	
	@RequestMapping(value = "/api/masters/create/{tbl_name}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public MasterBase createMaster(
			 @PathVariable String  tbl_name,
			 @RequestBody MTemplate mtemplate
			 ) {
		logger.info(" *** MainRestController.createMaster/{tbl_name}:"+tbl_name);

		return masterTblService.insertMTemplate(tbl_name, mtemplate);
	 }
	
	@RequestMapping(value = "/api/masters/update/{tbl_name}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public MasterBase updateMaster(
			 @PathVariable String  tbl_name,
			 @RequestBody MTemplate mtemplate
			 ) {
		logger.info(" *** MainRestController.createMaster/{tbl_name}:"+tbl_name);

		return masterTblService.updateMTemplate(tbl_name, mtemplate);
	 }

	@RequestMapping(value = "/api/masters/delete/{tbl_name}/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delMaster(
			 @PathVariable String  tbl_name,
			 @PathVariable int  id
			 ) {
		logger.info(" *** MainRestController.delMaster/{table}/{id}:"+tbl_name+"/"+id);

	    return "Request was successfully, delMaster:"+ tbl_name + " of id: "+id;
	 }
	
	@RequestMapping(value="/api/sys/{tbl_name}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getSys(
			 @PathVariable String tbl_name			
			) {
		logger.info(" *** MainRestController.getSys");
		
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;
		ListEnt listResp = new ListEnt();
		
		
    	// Count user
    	total_row = sysTblService.countAll(tbl_name);
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
		logger.info("System Table:"+ tbl_name+ " count: total_row : "+total_row);
		// Query class by school id
		ArrayList<SysTemplate> list = (ArrayList<SysTemplate>) sysTblService.findAll(tbl_name, from_row, max_result);
		
		listResp.setList(list);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
	    return listResp;
	}
	
	
	public String currentMethod (	
			@Context final HttpServletResponse response,
			@Context final HttpServletRequest request){
		return request.getMethod().toUpperCase();
	}
	
	protected User loadCurrentUser(){
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
		logger.info(" *** MainRestController.getCurrentUser");
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

	@RequestMapping(value="/api/schools/current_term",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getCurrentTerm(
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		
		
		logger.info(" *** getCurrentTerm Start");
	    User user = getCurrentUser();
	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
	    SchoolTerm terms = termDao.getCurrentTerm(user.getSchool_id());
	    rsp.setMessageObject(terms);
		
	    return rsp;
	    
	 }
			
}
