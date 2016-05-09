package com.itpro.restws.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.EClass;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.User;
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
public class ExamResultController extends BaseController {
	
//	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
//	@RequestMapping(value="/api/exam_results",method = RequestMethod.GET)
//	@ResponseStatus(value=HttpStatus.OK)	
//	public ListEnt  getExamResults() {
//		logger.info(" *** MainRestController.getExamResults");
//		
//		int total_row = 0;
//		int from_row = 0;
//		int max_result = Constant.MAX_RESP_ROW;;
//		ListEnt listResp = new ListEnt();
//		User user = getCurrentUser();
//		Integer school_id = user.getSchool_id();
//    	// Count user
//    	total_row = examResultService.countBySchoolID(school_id);
//    	if (total_row > Constant.MAX_RESP_ROW){
//    		max_result = Constant.MAX_RESP_ROW;
//    	}else{
//    		max_result = total_row;
//    	}
//    		
//		logger.info("ExamResult count: total_row : "+total_row);
//		// Query class by school id
//		ArrayList<ExamResult> examResults = examResultService.findBySchool(school_id, from_row, max_result);
//		
//		listResp.setList(examResults);
//		listResp.setFrom_row(from_row);
//		listResp.setTo_row(from_row + max_result);
//		listResp.setTotal_count(total_row);
//	    return listResp;
//
//	}
	
	@RequestMapping(value="/api/exam_results/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ExamResult getExamResult(@PathVariable int  id) 
	{
		
		logger.info(" *** MainRestController.getExamResult/{id}:"+id);
		return examResultService.findById(Integer.valueOf(id));
	 }
	
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/exam_results/create",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public ExamResult createExamResult(
			@RequestBody ExamResult examResult,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.exam_results.update");
		
		User teacher = getCurrentUser();
		examResult.setTeacher_id(teacher.getId());
		examResult.setSchool_id(teacher.getSchool_id());
		examResult.setExam_dt(Utils.now());
		
		User student = userService.findById(examResult.getStudent_id());
		if (!student.hasRole(E_ROLE.STUDENT.getRole_short())){
			throw new ESchoolException("Exam Student is not Student role:"+examResult.getStudent_id(), HttpStatus.BAD_REQUEST);
		}
		examResult.setStudent_name(student.getFullname());
		
		return examResultService.insertExamResult(examResult);
		 
	}
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/exam_results/update",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public ExamResult updateExamResult(
			@RequestBody ExamResult examResult,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.exam_results.update");
		
		User teacher = getCurrentUser();
		examResult.setTeacher_id(teacher.getId());
		examResult.setSchool_id(teacher.getSchool_id());
		examResult.setExam_dt(Utils.now());
		
		User student = userService.findById(examResult.getStudent_id());
		if (!student.hasRole(E_ROLE.STUDENT.getRole_short())){
			throw new ESchoolException("Exam Student is not Student role:"+examResult.getStudent_id(), HttpStatus.BAD_REQUEST);
		}
		examResult.setStudent_name(student.getFullname());
		
		ExamResult curr =examResultService.findById(examResult.getId());
		if (curr == null ){
			throw new ESchoolException("Current Exam Result is not exisint:"+examResult.getId(), HttpStatus.BAD_REQUEST);
		}
		examResult.setActflg(curr.getActflg());
		examResult.setCtddtm(curr.getCtddtm());
		examResult.setCtdusr(curr.getCtdusr());
		
		return examResultService.updateExamResult(examResult);
		 
	}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value = "/api/exam_results/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delExamResult(
			 @PathVariable int  id
			 ) {
		logger.info(" *** MainRestController.delExamResult/{id}:"+id);

	    return "Request was successfully, delete exam result of id: "+id;
	 }
	

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/exam_results",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt getExamResultsExt(
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			@RequestParam(value="filter_user_id",required =false) String filter_user_id,			
			@RequestParam(value="filter_from_id", required =false) String filter_from_id,
			@RequestParam(value="filter_from_dt", required =false) String filter_from_dt,
			@RequestParam(value="filter_to_dt", required =false) String filter_to_dt,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getExamResultsExt");
		
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		Integer class_id =  Utils.parseInteger(filter_class_id);
		Integer user_id = Utils.parseInteger(filter_user_id);
		
		
		if (user.hasRole(E_ROLE.ADMIN.getRole_short())){
			
    	}else if (user.hasRole(E_ROLE.TEACHER.getRole_short()) || user.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short()) ){
    		if (class_id == null || class_id == 0 ){
    			throw new ESchoolException("User is not Admin, require filter_class_id to get Exam Info ",HttpStatus.BAD_REQUEST);
    		}
    		if (!userService.isBelongToClass(user.getId(), class_id)){
    			throw new ESchoolException("User ID="+user.getId()+" is not belong to the class id = "+class_id,HttpStatus.BAD_REQUEST);
    		}
    		
    		EClass eclass = classService.findById(class_id);
    		if (eclass.getSchool_id().intValue() != school_id.intValue()){
    			if (!userService.isBelongToClass(user.getId(), class_id)){
        			throw new ESchoolException("User ID="+user.getId()+" and Class are not in same school,class_id= "+class_id,HttpStatus.BAD_REQUEST);
        		}
    		}
    		throw new ESchoolException("Invalid user role:"+user.getRoles(),HttpStatus.BAD_REQUEST);
    	}
    	
		
		List<ExamResult> exam_results = null;
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
				
		ListEnt rspEnt = new ListEnt();
	    
    	// Count user
    	total_row = examResultService.countExamResultExt(school_id, class_id, user_id, Utils.parseInteger(filter_from_id),filter_from_dt,filter_to_dt);
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
    		
		logger.info("Attendance count: total_row : "+total_row);
		// Query class by school id
		exam_results = examResultService.findExamResultExt(school_id, class_id, user_id, Utils.parseInteger(filter_from_id), from_row, max_result,filter_from_dt,filter_to_dt);
	    rspEnt.setList(exam_results);
	    rspEnt.setFrom_row(from_row);
	    rspEnt.setTo_row(from_row + max_result);
	    rspEnt.setTotal_count(total_row);
		    
	
	    
	    return rspEnt;

	}
	
	@Secured({ "ROLE_STUDENT"})
	@RequestMapping(value = "/api/exam_results/myprofile", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public ListEnt getExamResultProfile(
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 ) {
		logger.info(" *** MainRestController.getExamResultProfile Start");
		ListEnt rspEnt = new ListEnt();
		
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
		User student = getCurrentUser();

		// Count user
    	int total_row = attendanceService.countByStudent(student.getId());
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
    	
		ArrayList<ExamResult> list = examResultService.findByStudent(student.getId(), from_row, max_result);
        
		rspEnt.setList(list);
	    rspEnt.setFrom_row(from_row);
	    rspEnt.setTo_row(from_row + max_result);
	    rspEnt.setTotal_count(total_row);
	    
	    return rspEnt;
	 }
}
