package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.SysTemplate;
import com.itpro.restws.service.SysTblService;
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
public class SysController {
	@Autowired
	private SysTblService sysTblService;
	private static final Logger logger = Logger.getLogger(SysController.class);
	
	@RequestMapping(value="/api/sys/{tbl_name}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getSys(
			 @PathVariable String tbl_name,
			 @RequestParam(value="from_row",required =false) Integer filter_from_row,
			@RequestParam(value="max_result",required =false) Integer filter_max_result,
				
			 @Context final HttpServletRequest request,
			@Context final HttpServletResponse response
			) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		

		ListEnt listResp = new ListEnt();
		RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		
		int total_row = 0;
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
		if (max_result <= 0){
			max_result = Constant.MAX_RESP_ROW;
		}
		
		
    	// Count user
    	total_row = sysTblService.countAll(tbl_name);
    	if (total_row <=  0){
    		listResp.setList(null);
    		listResp.setFrom_row(0);
    		listResp.setTo_row(0);
    		listResp.setTotal_count(0);
    		rsp.setMessageObject(listResp);
    		return rsp;
    	}
    	
    	if (total_row < max_result){
    		max_result = total_row;
    	}	
    	
		logger.info("System Table:"+ tbl_name+ " count: total_row : "+total_row);
		// Query class by school id
		ArrayList<SysTemplate> list = (ArrayList<SysTemplate>) sysTblService.findAll(tbl_name, from_row, max_result);
		
		listResp.setList(list);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
		rsp.setMessageObject(listResp);
		return rsp;
		
	}
	
	
			
}
