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

import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_ROLE;
import com.itpro.restws.helper.ErrInfo;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.ExamRank;
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
	

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
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
		logger.info(" *** inputExamResult Start");
		
		User teacher = getCurrentUser();
		ExamResult ret = examResultService.inputExam(teacher,examResult);
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(ret);
	    return rsp;
		 
	}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER"})
	@RequestMapping(value="/api/exam_results",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getExamResults(
			@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			@RequestParam(value="filter_year_id", required =false) Integer filter_year_id,			
			@RequestParam(value="filter_student_id",required =false) Integer filter_student_id,			
			@RequestParam(value="filter_subject_id", required =false) Integer filter_subject_id,
			//@RequestParam(value="filter_from_id", required =false) String filter_from_id,
			//@RequestParam(value="filter_from_dt", required =false) String filter_from_dt,
			//@RequestParam(value="filter_to_dt", required =false) String filter_to_dt,
			//@RequestParam(value="filter_exam_month", required =false) String filter_exam_month,
			//@RequestParam(value="filter_exam_dt", required =false) String filter_exam_dt,
			//@RequestParam(value="filter_exam_type", required =false) String filter_exam_type,
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getExamResultsExt");
		User teacher = getCurrentUser();

		// check teacher
		if (teacher.hasRole(E_ROLE.ADMIN.getRole_short())){
			
    	}else if (teacher.hasRole(E_ROLE.TEACHER.getRole_short())  ){
    		if (!userService.isBelongToClass(teacher.getId(), filter_class_id)){
    			throw new ESchoolException("User ID="+teacher.getId()+" is not belong to the class id = "+filter_class_id.intValue(),HttpStatus.BAD_REQUEST);
    		}    		
    		
    	}else{
    		throw new ESchoolException("Invalid user role:"+teacher.getRoles(),HttpStatus.BAD_REQUEST);
    	}
    	
		
		List<ExamResult> exam_results  = examResultService.getClassProfile(teacher.getSchool_id(),filter_class_id,filter_student_id, filter_subject_id, filter_year_id);
		
	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(exam_results);
	    return rsp;
	}
	
	@Secured({ "ROLE_STUDENT"})
	@RequestMapping(value = "/api/exam_results/myprofile", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public RespInfo getExamResultProfile(
			 //@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			 @RequestParam(value="filter_subject_id",required =false) Integer filter_subject_id,
			 @RequestParam(value="filter_year_id",required =false) Integer filter_year_id,
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			 ) {
		logger.info(" *** MainRestController.getExamResultProfile Start");
		// Valid class ID
//		Integer class_id = Utils.parseInteger(filter_class_id);
//		if (class_id == null){
//			throw new ESchoolException(" filter_class_id  is required !", HttpStatus.BAD_REQUEST);
//			
//		}
		
		User student = getCurrentUser();
//		if (!student.is_belong2class(class_id)){
//			throw new ESchoolException(" user:"+student.getId()+" is not belong to class_id: "+class_id.intValue(), HttpStatus.BAD_REQUEST);
//		}
		
		
		ListEnt rspEnt = new ListEnt();
		// Initial data if necessary
		// examResultService.initStudentExamResult(student, class_id);
    	// ArrayList<ExamResult> list = examResultService.findUserProfile(student, class_id);
		
		
		ArrayList<ExamResult> list  = examResultService.getUserProfile(student,filter_subject_id, filter_year_id);
	    rspEnt.setFrom_row(0);
	    rspEnt.setTo_row(list.size());
		rspEnt.setTotal_count(list.size());
		rspEnt.setList(list);
	    
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(list);
	    return rsp;
		
	 }
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(value="/api/exam_results/input/batch",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo inputExamResults(
			@RequestBody ArrayList<ExamResult> examResults,
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** createExamResult Start");
		
		User teacher = getCurrentUser();
		ArrayList<ErrInfo> errors = new ArrayList<ErrInfo>();
		int cnt =0;
		if (examResults != null && examResults.size() > 0){
			//Validate
			for (ExamResult examResult: examResults){
				examResultService.validInputExam(teacher,examResult);
			}
			// Input
			
			try{
				for (ExamResult examResult: examResults){
					examResultService.validInputExam(teacher,examResult);
					examResultService.inputExam(teacher,examResult);
				}
				cnt +=1;
			}catch (Exception ex){
				ErrInfo errinfo = new ErrInfo();
				errinfo.setError(ex.getMessage());
				errinfo.setData(examResults);
				errors.add(errinfo);
			}
		}
		
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		if (errors.size() > 0){
			rsp.setMessage("Finished with error, successed:"+cnt+"/"+examResults.size());
			rsp.setMessageObject(errors);
		}
		
	    return rsp;
		 
	}
	
	
	
//	// Get 
//	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_CLS_PRESIDENT" })
//	@RequestMapping(value="/api/exam_results/marks",method = RequestMethod.GET)
//	@ResponseStatus(value=HttpStatus.OK)	
//	public RespInfo getExamResultMarks(
//			@RequestParam(value="filter_class_id",required =true) String filter_class_id,
//			@RequestParam(value="filter_subject_id", required =true) String filter_subject_id,
//
//			@Context final HttpServletRequest request,
//			@Context final HttpServletResponse response
//			) {
//		logger.info(" *** MainRestController.getExamResultsExt");
//		
//		User user = getCurrentUser();
//		Integer school_id = user.getSchool_id();
//		Integer class_id =  Utils.parseInteger(filter_class_id);
//		Integer subject_id = Utils.parseInteger(filter_subject_id);
//		
//		if (user.hasRole(E_ROLE.ADMIN.getRole_short())){
//			
//    	}else if (user.hasRole(E_ROLE.TEACHER.getRole_short()) || user.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short()) ){
//    		if (class_id == null || class_id == 0 ){
//    			throw new ESchoolException("User is not Admin, require filter_class_id to get Exam Info ",HttpStatus.BAD_REQUEST);
//    		}
//    		if (!userService.isBelongToClass(user.getId(), class_id)){
//    			throw new ESchoolException("User ID="+user.getId()+" is not belong to the class id = "+class_id,HttpStatus.BAD_REQUEST);
//    		}
//    		
//    		EClass eclass = classService.findById(class_id);
//    		if (eclass.getSchool_id().intValue() != school_id.intValue()){
//    			if (!userService.isBelongToClass(user.getId(), class_id)){
//        			throw new ESchoolException("User ID="+user.getId()+" and Class are not in same school,class_id= "+class_id,HttpStatus.BAD_REQUEST);
//        		}
//    		}
//    		
//    	}else{
//    		throw new ESchoolException("Invalid user role:"+user.getRoles(),HttpStatus.BAD_REQUEST);
//    	}
//		ListEnt rspEnt = new ListEnt();
//		
//		ArrayList<ExamResult> list  = examResultService.getClassResult_Mark(school_id,class_id, subject_id,false);
//		
//	    rspEnt.setFrom_row(0);
//	    rspEnt.setTo_row(list.size());
//		rspEnt.setTotal_count(list.size());
//		rspEnt.setList(list);
//		// Query class by school id
//		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
//		rsp.setMessageObject(list);
//	    return rsp;
//
//	}
	
	
	
// Get 
//	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
//	@RequestMapping(value="/api/exam_results/exe_ave",method = RequestMethod.GET)
//	@ResponseStatus(value=HttpStatus.OK)	
//	public RespInfo getExamResultMarks(
//			@RequestParam(value="filter_class_id",required =true) Integer filter_class_id,
//			@Context final HttpServletRequest request,
//			@Context final HttpServletResponse response
//			) {
//		logger.info(" *** MainRestController.getExamResultsExt");
//		
//		User user = getCurrentUser();
//		Integer school_id = user.getSchool_id();
//		
//		
//		
//		if (user.hasRole(E_ROLE.ADMIN.getRole_short())){
//			
//    	}else if (user.hasRole(E_ROLE.TEACHER.getRole_short()) || user.hasRole(E_ROLE.CLS_PRESIDENT.getRole_short()) ){
//    		if (!userService.isBelongToClass(user.getId(), filter_class_id)){
//    			throw new ESchoolException("User ID="+user.getId()+" is not belong to the class id = "+filter_class_id.intValue(),HttpStatus.BAD_REQUEST);
//    		}
//    		
//    	}else{
//    		throw new ESchoolException("Invalid user role:"+user.getRoles(),HttpStatus.BAD_REQUEST);
//    	}
//		EClass eclass = classService.findById(filter_class_id);
//		if (eclass.getSchool_id().intValue() != school_id.intValue()){
//			if (!userService.isBelongToClass(user.getId(), filter_class_id)){
//    			throw new ESchoolException("User ID="+user.getId()+" and Class are not in same school,class_id= "+filter_class_id,HttpStatus.BAD_REQUEST);
//    		}
//		}
//
//		examResultService.calAverage(eclass,filter_term_val.intValue());
//		// Query class by school id
//		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
//	    return rsp;
//
//	}
	
	//@Secured({ "ROLE_ADMIN", "ROLE_TEACHER"})
	@RequestMapping(value="/api/exam_results/ranks",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getExamRanks(
			@RequestParam(value="filter_student_id",required =false) Integer filter_student_id,
			@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			@RequestParam(value="filter_year_id", required =false) Integer filter_year_id,			
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getExamRanks");

		User current_user = getCurrentUser();
				
		ArrayList<ExamRank> exam_ranks  = null;
		
		if (filter_class_id == null || filter_class_id.intValue() == 0)  {
			throw new ESchoolException("filter_class_id required", HttpStatus.BAD_REQUEST);
		}

//		
//		if (filter_year_id == null || filter_year_id.intValue() == 0)  {
//				throw new ESchoolException("filter_year_id required", HttpStatus.BAD_REQUEST);
//		}
		
		if (filter_student_id != null && filter_student_id.intValue() > 0){
			User student = userService.findById(filter_student_id);
			if (student != null && student.getSchool_id().intValue() == current_user.getSchool_id().intValue()){ 
				exam_ranks = examResultService.getUserRank(student, filter_class_id, filter_year_id);
			}else{
				throw new ESchoolException("filter_student_id is not existing:"+filter_student_id.intValue(), HttpStatus.BAD_REQUEST);
			}
		}else {
			exam_ranks = examResultService.getClassRank(filter_class_id, filter_year_id);
		}
		
	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(exam_ranks);
	    return rsp;
	}
	
	@RequestMapping(value="/api/exam_results/exec_rank",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo execRank(
			@RequestParam(value="filter_student_id",required =false) Integer filter_student_id,
			@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			@RequestParam(value="filter_year_id", required =false) Integer filter_year_id,			
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.execRank");

		User current_user = getCurrentUser();
				
		ArrayList<ExamRank> exam_ranks  = null;
		
		if (filter_class_id == null || filter_class_id.intValue() == 0)  {
			throw new ESchoolException("filter_class_id required", HttpStatus.BAD_REQUEST);
		}

//		
//		if (filter_year_id == null || filter_year_id.intValue() == 0)  {
//				throw new ESchoolException("filter_year_id required", HttpStatus.BAD_REQUEST);
//		}
		
//		if (filter_student_id != null && filter_student_id.intValue() > 0){
//			User student = userService.findById(filter_student_id);
//			if (student != null && student.getSchool_id().intValue() == current_user.getSchool_id().intValue()){ 
//				exam_ranks = examResultService.calUserAve(student, filter_year_id);
//			}else{
//				throw new ESchoolException("filter_student_id is not existing:"+filter_student_id.intValue(), HttpStatus.BAD_REQUEST);
//			}
//		}else {
//			exam_ranks = examResultService.execClassRank(filter_class_id, filter_year_id);
//		}
		
	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(exam_ranks);
	    return rsp;
	}
	
}
