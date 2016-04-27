package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.E_STATE;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.Password;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.Attendance;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.FinalResult;
import com.itpro.restws.model.MTemplate;
import com.itpro.restws.model.MasterBase;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.School;
import com.itpro.restws.model.SysTemplate;
import com.itpro.restws.model.Timetable;
import com.itpro.restws.model.User;
import com.itpro.restws.security.AuthenticationService;
import com.itpro.restws.security.TokenInfo;
import com.itpro.restws.security.TokenManager;
import com.itpro.restws.securityimpl.UserContext;
import com.itpro.restws.service.AttendanceService;
import com.itpro.restws.service.ClassService;
import com.itpro.restws.service.ExamResultService;
import com.itpro.restws.service.FinalResultService;
import com.itpro.restws.service.MasterTblService;
import com.itpro.restws.service.MessageService;
import com.itpro.restws.service.PermitService;
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
	private AttendanceService attendanceService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ExamResultService examResultService;
	
	@Autowired
	private FinalResultService finalResultService;
	
	@Autowired
	private TimetableService timetableService;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MasterTblService masterTblService;
	
	@Autowired
	private SysTblService sysTblService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private PermitService permitService;

	
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

	
	
	@RequestMapping(value="/api/classes",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getClasses(@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getClasses");
		List<EClass> classes = null;
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		
		ListEnt rspEnt = new ListEnt();
	    try {
	    	// Count user
	    	total_row = classService.countBySchoolID(school_id);
	    	if (total_row > Constant.MAX_RESP_ROW){
	    		max_result = Constant.MAX_RESP_ROW;;
	    	}else{
	    		max_result = total_row;
	    	}
	    		
			logger.info("Class count: total_row : "+total_row);
			// Query class by school id
			classes = classService.findBySchool(school_id, from_row, max_result);
		    rspEnt.setList(classes);
		    rspEnt.setFrom_row(from_row);
		    rspEnt.setTo_row(from_row + max_result);
		    rspEnt.setTotal_count(total_row);
		    
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController.getClasses() ERROR:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    
	    return rspEnt;

	}
	
	
	@RequestMapping(value = "/api/classes/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public EClass getClass(@PathVariable int  id,@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getClass/{id}:"+id);
		EClass eclass = null;
	    try {
	    	User user = getCurrentUser();
	    	eclass = classService.findById(Integer.valueOf(id));
	    	if (eclass != null && user.getSchool_id() != eclass.getSchool_id()){
	    		logger.info("Eclass is not in same school with current user");
	    		eclass = null;
	    	}
			logger.info("eclass: "+eclass.toString());
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController  ERROR:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }
	    finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    return eclass;
	 }
	
	
	@RequestMapping(value="/api/classes/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public EClass createClass(
			@RequestBody EClass eclass,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.create");
		// eclass = classService.findById(1);
		
		 return classService.insertClass(eclass);
		 
	}
	
	@RequestMapping(value="/api/classes/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public EClass updateClass(
			@RequestBody EClass eclass,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.classes.update");
		
		// eclass = classService.findById(1);
		 return classService.updateClass(eclass);
		 
	}
	
	@RequestMapping(value = "/api/classes/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delClass(
			@PathVariable int id,
			@Context final HttpServletResponse response
			 
			 ) {
		logger.info(" *** MainRestController.delUser/{class_id}:"+id);
	    return "Request was successfully, delete class of id: "+id;
	 }
	
		
	
	@RequestMapping(value="/api/attendances",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getAttendances(@Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getAttendances");
		List<Attendance> attendances = null;
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
				
		ListEnt rspEnt = new ListEnt();
	    try {
	    	// Count user
	    	total_row = attendanceService.countBySchoolID(school_id);
	    	if (total_row > Constant.MAX_RESP_ROW){
	    		max_result = Constant.MAX_RESP_ROW;
	    	}else{
	    		max_result = total_row;
	    	}
	    		
			logger.info("Attendance count: total_row : "+total_row);
			// Query class by school id
			attendances = attendanceService.findBySchool(school_id, from_row, max_result);
		    rspEnt.setList(attendances);
		    rspEnt.setFrom_row(from_row);
		    rspEnt.setTo_row(from_row + max_result);
		    rspEnt.setTotal_count(total_row);
		    
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController.getAttendances() ERROR:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    
	    return rspEnt;

	}
	
	
	@RequestMapping(value = "/api/attendances/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public Attendance getAttendance(
			 @PathVariable int  id,
			 @Context final HttpServletResponse response) {
		logger.info(" *** MainRestController.getAttendance/{id}:"+id);
		Attendance attendance = null;
	    try {
	    	attendance = attendanceService.findById(Integer.valueOf(id));
			logger.info("attendance: "+attendance.toString());
	    }catch(Exception e){
	    	for ( StackTraceElement ste: e.getStackTrace()){
	    		logger.error(ste);
	    	}
	    	logger.info(" *** MainRestController  ERROR:"+e.getMessage());
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    }
	    finally{
	    	try{
	    		response.flushBuffer();
	    	}catch(Exception ex){}
	    }
	    return attendance;
	 }
	
	
	@RequestMapping(value="/api/attendances/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public Attendance createAttendance(
			@RequestBody Attendance attendance,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.users.create");
		
//		 attendance = attendanceService.findById(1);
		 return attendanceService.insertAttendance(attendance);
		 
	}
	
	@RequestMapping(value="/api/attendances/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public Attendance updateAttendances(
			@RequestBody Attendance attendance,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.attendances.update");
			 //attendance = attendanceService.findById(1);
			 return attendanceService.updateAttendance(attendance);
		 
	}
	
	@RequestMapping(value = "/api/attendances/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delAttendance(
			 @PathVariable int  id,
			@Context final HttpServletResponse response
			 
			 ) {
		logger.info(" *** MainRestController.delAttendance/{attendance_id}:"+id);

	    return "Request was successfully, delete attendance of id: "+id;
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
	

	
	
	
	
	
	
	@RequestMapping(value="/api/exam_results",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt  getExamResults() {
		logger.info(" *** MainRestController.getExamResults");
		
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		ListEnt listResp = new ListEnt();
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
    	// Count user
    	total_row = examResultService.countBySchoolID(school_id);
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
    		
		logger.info("ExamResult count: total_row : "+total_row);
		// Query class by school id
		ArrayList<ExamResult> examResults = examResultService.findBySchool(school_id, from_row, max_result);
		
		listResp.setList(examResults);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
	    return listResp;

	}
	
	@RequestMapping(value="/api/exam_results/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ExamResult getExamResult(@PathVariable int  id) 
	{
		
		logger.info(" *** MainRestController.getExamResult/{id}:"+id);
		return examResultService.findById(Integer.valueOf(id));
	 }
	
	

	@RequestMapping(value="/api/exam_results/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public ExamResult createExamResult(
			@RequestBody ExamResult examResult,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.exam_results.update");
		//examResult.setId(100);//TODO:Test
		 return examResultService.insertExamResult(examResult);
		 
	}
	
	@RequestMapping(value="/api/exam_results/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public ExamResult updateExamResult(
			@RequestBody ExamResult examResult,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.exam_results.update");
		return examResultService.updateExamResult(examResult);
		 
	}

	
	@RequestMapping(value = "/api/exam_results/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delExamResult(
			 @PathVariable int  id
			 ) {
		logger.info(" *** MainRestController.delExamResult/{id}:"+id);

	    return "Request was successfully, delete exam result of id: "+id;
	 }
	

	
	@RequestMapping(value="/api/final_results",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt  getFinalResult() {
		logger.info(" *** MainRestController.getFinalResult");
		
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;

		ListEnt listResp = new ListEnt();
		User user = getCurrentUser();
		
    	// Count user
    	total_row = finalResultService.countBySchoolID(user.getSchool_id());
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
    		
		logger.info("FinalResult count: total_row : "+total_row);
		// Query class by school id
		ArrayList<FinalResult> finalResults = finalResultService.findBySchool(user.getSchool_id(), from_row, max_result);
		
		listResp.setList(finalResults);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
	    return listResp;

	}
	
	@RequestMapping(value="/api/final_results/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public FinalResult getFinalResult(@PathVariable int  id) 
	{
		logger.info(" *** MainRestController.getFinalResult/{id}:"+id);
		return finalResultService.findById(Integer.valueOf(id));
	 }
	
	

	@RequestMapping(value="/api/final_results/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public FinalResult createFinalResult(
			@RequestBody FinalResult finalResult,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.final_results.create");
		//finalResult.setId(100);//TODO:Test
		 return finalResultService.insertFinalResult(finalResult);
		 
	}
	
	@RequestMapping(value="/api/final_results/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public FinalResult updateFinalResult(
			@RequestBody FinalResult finalResult,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.final_results.update");
		return finalResultService.updateFinalResult(finalResult);
		 
	}

	
	@RequestMapping(value = "/api/final_result/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delFinalResult(
			 @PathVariable int  id
			 ) {
		logger.info(" *** MainRestController.delFinalResult/{id}:"+id);

	    return "Request was successfully, delete final result of id: "+id;
	 }
	
	
	


	@RequestMapping(value="/api/timetables",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt  getTimetables() {
		logger.info(" *** MainRestController.getTimetables");
		
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		int school_id = 1;//TODO: get from token => user info
		ListEnt listResp = new ListEnt();
		
    	// Count user
    	total_row = timetableService.countBySchoolID(school_id);
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
    		
		logger.info("Timetable count: total_row : "+total_row);
		// Query class by school id
		ArrayList<Timetable> examResults = timetableService.findBySchool(school_id, from_row, max_result);
		
		listResp.setList(examResults);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
	    return listResp;

	}

	
	@RequestMapping(value="/api/timetables/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public Timetable getTimetable(@PathVariable int  id) 
	{
		logger.info(" *** MainRestController.getTimetable/{id}:"+id);
		return timetableService.findById(id);
	 }
	
	
	
	@RequestMapping(value="/api/timetables/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public Timetable createTimetable(
			@RequestBody Timetable timetable
			) {
		logger.info(" *** MainRestController.ceateTimetable.create");
		//timetable.setId(100);//TODO:Test
		return timetableService.insertTimetable(timetable);
		 
		 
	}
	
	@RequestMapping(value="/api/timetables/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public Timetable updateTimetable(
			@RequestBody Timetable timetable
			) {
		logger.info(" *** MainRestController.updateTimetable.update");
		 //return timetable;
		return timetableService.updateTimetable(timetable);
		 
	}
	
	@RequestMapping(value = "/api/timetables/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delTimetable(
			 @PathVariable int  id
			 ) {
		logger.info(" *** MainRestController.delTimetable/{id}:"+id);

	    return "Request was successfully, delete delTimetable of id: "+id;
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


}
