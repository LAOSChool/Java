package com.itpro.restws.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.model.ActionLog;
import com.itpro.restws.model.User;
import com.itpro.restws.security.AuthenticationService;
import com.itpro.restws.security.TokenManager;
import com.itpro.restws.securityimpl.UserContext;
import com.itpro.restws.service.ActionLogService;
import com.itpro.restws.service.AttendanceService;
import com.itpro.restws.service.ClassService;
import com.itpro.restws.service.ExamResultService;
import com.itpro.restws.service.MasterTblService;
import com.itpro.restws.service.MessageService;
import com.itpro.restws.service.NotifyService;
import com.itpro.restws.service.PermitService;
import com.itpro.restws.service.SchoolService;
import com.itpro.restws.service.SchoolTermService;
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
public class BaseController {
	protected static final Logger logger = Logger.getLogger(BaseController.class);
	@Autowired
	protected ApplicationContext applicationContext;
	
	
	
//	@Autowired
//	private ResourceBundleMessageSource messageSource;// Store messages
	
	@Autowired
	protected UserService userService;	
	@Autowired
	protected ClassService classService;
	@Autowired
	protected AttendanceService attendanceService;
	@Autowired
	protected SchoolService schoolService;
	@Autowired
	protected ExamResultService examResultService;

	
	
	@Autowired
	protected TimetableService timetableService;

	@Autowired
	protected MessageService messageService;
	@Autowired
	protected NotifyService notifyService;
	
	@Autowired
	protected MasterTblService masterTblService;
	
	@Autowired
	protected SysTblService sysTblService;

	@Autowired
	protected AuthenticationService authenticationService;

	@Autowired
	protected TokenManager tokenManager;

	@Autowired
	protected PermitService permitService;
	
	@Autowired
	protected ActionLogService actionLogService;
	@Autowired
	protected SchoolTermService schoolTermService;
	
	@PostConstruct
	public void init() {
		logger.info(" *** BaseController.init with: " + applicationContext);
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
//		String sso =null;
//		
//		if (principal instanceof UserContext) {
//			 sso = ((UserContext) principal).getUsername();
//	} else {
//		sso = principal.toString();
//		
//	}
//		user = userService.findBySso(sso);
		return user;
	}

	

	protected String getUserName(){
		logger.info(" *** MainRestController.getUserName");
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}	
	
	@SuppressWarnings("unchecked")
	protected String getRoles(){
		logger.info(" *** MainRestController.getRoles");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<GrantedAuthority> authorities = null;
		String ret = "";
		if (principal instanceof UserDetails) {
			authorities = (List<GrantedAuthority>) ((UserDetails)principal).getAuthorities();
			ret = authorities.get(0).toString();//Get first role only
		}
		logger.info(" *** MainRestController.getRole:ret="+ret);
		return ret;
	}
	@SuppressWarnings("unchecked")
	boolean isRole(String role){
		logger.info(" *** MainRestController.isRole///"+role + "/// Start");
		boolean ret = false;
		
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<GrantedAuthority> authorities = null;
		if (principal instanceof UserDetails) {
			authorities = (List<GrantedAuthority>) ((UserDetails)principal).getAuthorities();
			for (GrantedAuthority arantedAuthority : authorities){
				logger.info(" *** MainRestController.isRole() arantedAuthority:"+arantedAuthority.getAuthority());
				if (role.equalsIgnoreCase(arantedAuthority.getAuthority())){
					ret= true;
					break;
				}
			}
			
		}
		logger.info(" *** MainRestController.isRole() return:"+ret);
		return ret;
	}
	/***
	 * 
	 * @param req_school_id
	 * @param req_class_id
	 * @param req_user_id
	 * @param uri
	 * @param proto
	 */
	void checkPermit(String entity, int  school_id, int class_id, String roles){
		logger.info(" *** MainRestController.checkPermit() start:");
		logger.info(" *** MainRestController.checkPermit()    entity:"+entity);
		logger.info(" *** MainRestController.checkPermit()    school_id:"+school_id);
		logger.info(" *** MainRestController.checkPermit()    class_id:"+class_id);

		User user = this.getCurrentUser();
		boolean permit = false;
		
		boolean schoool_chk = false;
		boolean class_chk = false;
		boolean user_chk = false;
		
		if (school_id == 0 || school_id == user.getSchool_id()){
			schoool_chk =  true; 
		}
		
		
		if (isRole(E_ROLE.ADMIN.getRole())){
			logger.info(" *** MainRestController.checkPermit()    user is ADMIN");
			permit= schoool_chk;
		}else if (isRole(E_ROLE.TEACHER.getRole()) ||isRole(E_ROLE.CLS_PRESIDENT.getRole() )){
			logger.info(" *** MainRestController.checkPermit()    user is TEACHER r -CLS_PRESIDENT");
			permit= schoool_chk && class_chk;
			
		}else if (isRole(E_ROLE.STUDENT.getRole())){
			logger.info(" *** MainRestController.checkPermit()    user is STUDENT");
			permit= schoool_chk && class_chk && user_chk;
		}
		if (!permit ) 
		{
			logger.info(" *** MainRestController.checkPermit()    permit is FALSE, throw Exception");
			throw new ESchoolException("Method not allowed", HttpStatus.FORBIDDEN);
		}
	}
	
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
	protected String getPrincipal(){
		logger.info(" *** MainRestController.getPrincipal");
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
	protected ActionLog traceUserInfo(HttpServletRequest request){
		Integer act_id = (Integer)request.getAttribute(Constant.actlog_id);
		ActionLog act = actionLogService.findById(act_id);
		User user = getCurrentUser();
		if (act != null ){
			act.setSchool_id(user.getSchool_id());
			act.setSso_id(user.getSso_id());
			act.setUser_role(user.getRoles());
			actionLogService.updateAction(act);
		}
		return act;
	}
	
	

}
