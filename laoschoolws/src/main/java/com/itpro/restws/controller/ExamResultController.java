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
import com.itpro.restws.helper.RespInfo;
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
	

	@RequestMapping(value="/api/exam_results/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ExamResult getExamResult(@PathVariable int  id) 
	{
		
		logger.info(" *** MainRestController.getExamResult/{id}:"+id);
		return examResultService.findById(Integer.valueOf(id));
	 }
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(value="/api/exam_results/input",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo inputExamResult(
			@RequestBody ExamResult examResult,
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** createExamResult Start");
		
		User teacher = getCurrentUser();
		
		examResultService.validInputExam(teacher,examResult);
		
		ExamResult ret = examResultService.inputExam(teacher,examResult);
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(ret);
	    return rsp;
		 
	}
//	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
//	@RequestMapping(value="/api/exam_results/update",method = RequestMethod.POST)
//	@ResponseStatus(value=HttpStatus.OK)	
//	public RespInfo updateExamResult(
//			@RequestBody ExamResult examResult,
//			@Context final HttpServletRequest request,
//			@Context final HttpServletResponse response
//			) {
//		logger.info(" *** MainRestController.exam_results.update");
//		
//		User teacher = getCurrentUser();
//		if (examResult.getId() == null){
//			throw new ESchoolException("examResult.id is null", HttpStatus.BAD_REQUEST);
//		}
//		examResultService.validInputExam(teacher,examResult);
//		ExamResult ret = examResultService.updateExamResult(teacher, examResult);
//		
//		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
//		rsp.setMessageObject(ret);
//	    return rsp;
//		 
//	}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value = "/api/exam_results/delete/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo delExamResult(
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response,
			 @PathVariable int  id
			 ) {
		logger.info(" *** MainRestController.delExamResult/{id}:"+id);
		User teacher = getCurrentUser();
		
		ExamResult examresult = examResultService.findById(id);
		if (examresult == null ){
			throw new ESchoolException("Exam not found", HttpStatus.NOT_FOUND);
		}
		
		if (teacher.getSchool_id() != examresult.getSchool_id() ){
			throw new ESchoolException("Cannot access other school exam result", HttpStatus.BAD_REQUEST);
		}
		
		if (!teacher.hasRole(E_ROLE.ADMIN.getRole_short())){
			
		}else{
			if (examresult.getTeacher_id() == null ){
				throw new ESchoolException("Exam.teacherID cannot be NULL", HttpStatus.BAD_REQUEST);
			}
			
			if	(teacher.getId().intValue() != examresult.getTeacher_id().intValue()){
				throw new ESchoolException("Exam.teacherID="+examresult.getTeacher_id()+"  cannot be different from current teacher_id="+teacher.getId(), HttpStatus.BAD_REQUEST);
			}
			
		
		}
		examresult.setActflg("D");
		examResultService.deleteExamResult(examresult);
	    
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject("Request was successfully, delete exam result of id: "+id);
	    return rsp;
	 }
	

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/exam_results",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getExamResults(
			@RequestParam(value="filter_class_id",required =false) String filter_class_id,
			@RequestParam(value="filter_user_id",required =false) String filter_student_id,			
			@RequestParam(value="filter_from_id", required =false) String filter_from_id,
			@RequestParam(value="filter_from_dt", required =false) String filter_from_dt,
			@RequestParam(value="filter_to_dt", required =false) String filter_to_dt,
			@RequestParam(value="filter_subject_id", required =false) String filter_subject_id,
			@RequestParam(value="filter_term_id", required =false) String filter_term_id,
			@RequestParam(value="filter_exam_year", required =false) String filter_exam_year,
			@RequestParam(value="filter_exam_month", required =false) String filter_exam_month,
			@RequestParam(value="filter_exam_dt", required =false) String filter_exam_dt,
			@RequestParam(value="filter_exam_type", required =false) String filter_exam_type,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getExamResultsExt");
		
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		Integer class_id =  Utils.parseInteger(filter_class_id);
		Integer student_id = Utils.parseInteger(filter_student_id);
		
		
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
    		
    	}else{
    		throw new ESchoolException("Invalid user role:"+user.getRoles(),HttpStatus.BAD_REQUEST);
    	}
    	
		
		List<ExamResult> exam_results = null;
		int total_row = 0;
		int from_row = 0;
		int max_result = Constant.MAX_RESP_ROW;;
		
				
		ListEnt rspEnt = new ListEnt();
	    
    	// Count user
    	total_row = examResultService.countExamResultExt(school_id, class_id, student_id,Utils.parseInteger(filter_subject_id), Utils.parseInteger(filter_term_id), Utils.parseInteger(filter_exam_year), Utils.parseInteger(filter_exam_month), filter_exam_dt, filter_from_dt, filter_to_dt,  Utils.parseInteger(filter_from_id),null,null);
    	if (total_row > Constant.MAX_RESP_ROW){
    		max_result = Constant.MAX_RESP_ROW;
    	}else{
    		max_result = total_row;
    	}
    		
		logger.info("Attendance count: total_row : "+total_row);
		// Query class by school id
		exam_results = examResultService.findExamResultExt(school_id,from_row,max_result, class_id, student_id,Utils.parseInteger(filter_subject_id), Utils.parseInteger(filter_term_id), Utils.parseInteger(filter_exam_year), Utils.parseInteger(filter_exam_month), filter_exam_dt, filter_from_dt, filter_to_dt,  Utils.parseInteger(filter_from_id),null,null);
	    rspEnt.setList(exam_results);
	    rspEnt.setFrom_row(from_row);
	    rspEnt.setTo_row(from_row + max_result);
	    rspEnt.setTotal_count(total_row);
	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(rspEnt);
	    return rsp;
	}
	
	@Secured({ "ROLE_STUDENT"})
	@RequestMapping(value = "/api/exam_results/myprofile", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo getExamResultProfile(
			 @RequestParam(value="filter_class_id",required =false) String filter_class_id,
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 ) {
		logger.info(" *** MainRestController.getExamResultProfile Start");
		// Valid class ID
		Integer class_id = Utils.parseInteger(filter_class_id);
		if (class_id == null){
			throw new ESchoolException(" filter_class_id  is required !", HttpStatus.BAD_REQUEST);
			
		}
		
		User student = getCurrentUser();
		if (!student.is_belong2class(class_id)){
			throw new ESchoolException(" user:"+student.getId()+" is not belong to class_id: "+class_id.intValue(), HttpStatus.BAD_REQUEST);
		}
		ListEnt rspEnt = new ListEnt();
		// Initial data if necessary
		// examResultService.initStudentExamResult(student, class_id);
    	// ArrayList<ExamResult> list = examResultService.findUserProfile(student, class_id);
		
		ArrayList<ExamResult> list  = examResultService.getUserProfile_Mark(student,class_id,null,true);
	    rspEnt.setFrom_row(0);
	    rspEnt.setTo_row(list.size());
		rspEnt.setTotal_count(list.size());
		rspEnt.setList(list);
	    
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(list);
	    return rsp;
		
	 }
	
	// Get 
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
	@RequestMapping(value="/api/exam_results/marks",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getExamResultMarks(
			@RequestParam(value="filter_class_id",required =true) String filter_class_id,
			@RequestParam(value="filter_subject_id", required =true) String filter_subject_id,

			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getExamResultsExt");
		
		User user = getCurrentUser();
		Integer school_id = user.getSchool_id();
		Integer class_id =  Utils.parseInteger(filter_class_id);
		Integer subject_id = Utils.parseInteger(filter_subject_id);
		
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
    		
    	}else{
    		throw new ESchoolException("Invalid user role:"+user.getRoles(),HttpStatus.BAD_REQUEST);
    	}
		ListEnt rspEnt = new ListEnt();
		
		ArrayList<ExamResult> list  = examResultService.getClassProfile_Mark(school_id,class_id, subject_id,false);
	    rspEnt.setFrom_row(0);
	    rspEnt.setTo_row(list.size());
		rspEnt.setTotal_count(list.size());
		rspEnt.setList(list);
		// Query class by school id
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(list);
	    return rsp;

	}
}
