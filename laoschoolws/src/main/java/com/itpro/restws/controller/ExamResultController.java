package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Command;
import com.itpro.restws.model.ExamRank;
import com.itpro.restws.model.ExamResult;
import com.itpro.restws.model.User;
import com.itpro.restws.service.CommandService;
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
	@Autowired
	protected CommandService commandService;

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(value="/api/exam_results/{id}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ExamResult getExamResult(@PathVariable int  id) 
	{
		
		logger.info(" *** MainRestController.getExamResult/{id}:"+id);
		User me = getCurrentUser();
		ExamResult ret = examResultService.findById(me,Integer.valueOf(id));
		
		if (ret == null){
    		throw new ESchoolException("Not found", HttpStatus.NOT_FOUND);
    	}
		if (ret.getSchool_id().intValue() != me.getSchool_id().intValue()){
			throw new ESchoolException("school_id of user not same with exam result", HttpStatus.BAD_REQUEST);
		}
		return ret;
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
		
		User me = getCurrentUser();
		ExamResult ret = examResultService.inputExam(me,examResult);
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(ret);
	    return rsp;
		 
	}

	/***
	 * Do phai tu dong generate ra Profile neu chua ton tai
	 * Neu ko co filter from_row/max_result
	 * 
	 * @param filter_class_id
	 * @param filter_year_id
	 * @param filter_student_id
	 * @param filter_subject_id
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER"})
	@RequestMapping(value="/api/exam_results",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getExamResults(
			@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			@RequestParam(value="filter_year_id", required =false) Integer filter_year_id,			
			@RequestParam(value="filter_student_id",required =false) Integer filter_student_id,			
			@RequestParam(value="filter_subject_id", required =false) Integer filter_subject_id,
			
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getExamResults() START");
		User me = getCurrentUser();

		// check teacher
		if (me.hasRole(E_ROLE.ADMIN.getRole_short())){
			
    	}else if (me.hasRole(E_ROLE.TEACHER.getRole_short())  ){
    		if (!userService.isBelongToClass(me.getId(), filter_class_id)){
    			throw new ESchoolException("User ID="+me.getId()+" is not belong to the class id = "+filter_class_id.intValue(),HttpStatus.BAD_REQUEST);
    		}    		
    		
    	}else{
    		throw new ESchoolException("Invalid user role:"+me.getRoles(),HttpStatus.BAD_REQUEST);
    	}
    	
		
		ArrayList<ExamResult> exam_results  = examResultService.getClassProfile(me.getSchool_id(),filter_class_id,filter_student_id, filter_subject_id, filter_year_id);
		
		examResultService.orderExamResultByID(exam_results, 0);
		
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
		
		User student = getCurrentUser();
		
		ListEnt rspEnt = new ListEnt();
		// Initial data if necessary
	
		
		ArrayList<ExamResult> list  = examResultService.getUserProfile(student,filter_subject_id, filter_year_id);
		
		examResultService.orderExamResultByID(list, 0);
		
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
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value="/api/exam_results/ranks/process",method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo rankProcessAveAndOrder(
			@RequestParam(value="class_ids", required =false) String class_ids,
			@RequestParam(value="ex_key", required =false) String ex_key,
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.execRank");
		User me = getCurrentUser();
	
		if (class_ids == null||class_ids.trim().length() == 0){
			throw new ESchoolException("class_ids is required",HttpStatus.BAD_REQUEST);
		}
		if (ex_key == null||ex_key.trim().length() == 0){
			throw new ESchoolException("ex_key is required",HttpStatus.BAD_REQUEST);
		}
		
		// Calculate Average value by Month (m1,m2..m20)
//		ArrayList<ExamRank> examRanks = examResultService.execClassMonthAve(me, filter_class_id);
//		// Ranking average value by Month (m1,m2..m20)		
//		examResultService.procAllocation(me,examRanks);
//	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
//		rsp.setMessageObject("DONE, to get result, plz call : /api/exam_results/ranks");
		
		String err_msg = examResultService.valid_rank_process(me,class_ids, ex_key);
		if (err_msg != null && err_msg.length() > 0){
			err_msg = Utils.removeTxtLastComma(err_msg);
			RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"ERROR", request.getRequestURL().toString(),err_msg);
			rsp.setMessageObject("Cannot execute ranking due to error");
			return rsp;
		}
		
		
		Command cmd = commandService.create_rank_process(me, class_ids,ex_key);
	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
	    rsp.setMessageObject("Plz wait ~ 30s, background taks_id:"+cmd.getId().intValue() + " is in processing");
	    return rsp;
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER","ROLE_STUDENT"})
	@RequestMapping(value="/api/exam_results/ranks",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo rankGetExamRanks(
			@RequestParam(value="filter_student_id",required =false) Integer filter_student_id,
			@RequestParam(value="filter_class_id",required =false) Integer filter_class_id,
			@RequestParam(value="filter_year_id", required =false) Integer filter_year_id,			
			
			@Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		logger.info(" *** MainRestController.getExamRanks");

		User me = getCurrentUser();
				
		ArrayList<ExamRank> exam_ranks  = null;
		
		if (filter_class_id == null || filter_class_id.intValue() == 0)  {
			throw new ESchoolException("filter_class_id required", HttpStatus.BAD_REQUEST);
		}
		// Filter by Student
		
		if (filter_student_id != null && filter_student_id.intValue() > 0){
			User student = null;
			if (me.hasRole(E_ROLE.STUDENT.getRole_short())){
				filter_student_id = me.getId();
				student = me;
			}else{
				student = userService.findById(filter_student_id);
			}
			if (student != null && student.getSchool_id().intValue() == me.getSchool_id().intValue()){ 
				exam_ranks = examResultService.getUserRank(me,student, filter_class_id, filter_year_id);
			}else{
				throw new ESchoolException("filter_student_id is not existing:"+filter_student_id.intValue(), HttpStatus.BAD_REQUEST);
			}
		}else {
			exam_ranks = examResultService.getClassRank(me,filter_class_id, filter_year_id);
		}
		
		examResultService.orderRankByID(exam_ranks, 0);
	    RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		rsp.setMessageObject(exam_ranks);
	    return rsp;
	}


	
	
	
}
