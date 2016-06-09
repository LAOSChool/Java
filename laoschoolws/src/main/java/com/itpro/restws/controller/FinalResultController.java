package com.itpro.restws.controller;

import java.util.ArrayList;

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
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.FinalResult;
import com.itpro.restws.model.StudentProfile;
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
public class FinalResultController extends BaseController {
	
	@RequestMapping(value="/api/final_results",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public ListEnt  getFinalResult() {
		logger.info(" *** getFinalResult START ");
		
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
	
	
	@Secured({ "ROLE_STUDENT"})
	@RequestMapping(value = "/api/final_results/myprofile", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public ListEnt getFinalResultProfile(
			 @RequestParam(value="filter_school_year",required =false) String filter_school_year,
			 @Context final HttpServletRequest request,
			 @Context final HttpServletResponse response
			 ) {
		logger.info(" *** getFinalResultProfile Start");
		// Valid class ID
		Integer school_year = Utils.parseInteger(filter_school_year);
		if (school_year == null){
			throw new ESchoolException(" filter_school_year is required !", HttpStatus.BAD_REQUEST);
			
		}
		User student = getCurrentUser();
	
		ListEnt rspEnt = new ListEnt();
		// Return data
    	ArrayList<StudentProfile> list = finalResultService.findUserProfile(student, school_year);
	    rspEnt.setFrom_row(0);
	    rspEnt.setTo_row(list.size());
		rspEnt.setTotal_count(list.size());
		rspEnt.setList(list);
	    
	    return rspEnt;
		
	 }

}
