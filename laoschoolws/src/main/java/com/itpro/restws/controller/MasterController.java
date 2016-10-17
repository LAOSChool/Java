package com.itpro.restws.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
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

import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.ListEnt;
import com.itpro.restws.helper.RespInfo;
import com.itpro.restws.model.MTemplate;
import com.itpro.restws.model.MasterBase;
import com.itpro.restws.model.User;
import com.itpro.restws.service.MasterTblService;
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
public class MasterController  extends BaseController { 
	private static final Logger logger = Logger.getLogger(MasterController.class);	
	@Autowired
	private MasterTblService masterTblService;
		
	
	@RequestMapping(value="/api/masters/{tbl_name}",method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	public RespInfo getMaster(
			 @PathVariable String tbl_name,
			 @RequestParam(value="from_row",required =false) String filter_from_row,
			 @RequestParam(value="max_result",required =false) String filter_max_result,
				
			 @Context final HttpServletRequest request,
				@Context final HttpServletResponse response
			) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		
		 RespInfo rsp = new RespInfo(HttpStatus.OK.value(),"No error", request.getRequestURL().toString(), "Successful");
		int from_row = filter_from_row == null?0:Integer.valueOf(filter_from_row);
		int max_result = filter_max_result == null?Constant.MAX_RESP_ROW:Integer.valueOf(filter_max_result);
		if (max_result <= 0){
			max_result = Constant.MAX_RESP_ROW;
		}	
		int total_row = 0;
		
		ListEnt listResp = new ListEnt();
		User user = getCurrentUser();
    	// Count user
    	total_row = masterTblService.countBySchool(tbl_name,  user.getSchool_id());
    
    	if( (total_row <=  0) || (from_row > total_row) ||  max_result<=0) {
    		listResp.setList(null);
    		listResp.setFrom_row(0);
    		listResp.setTo_row(0);
    		listResp.setTotal_count(0);
    		rsp.setMessageObject(listResp);
    		return rsp;
    	}
    	
    	if ((from_row + max_result > total_row)){
    		max_result = total_row-from_row;
    	}	
    	
		logger.info("Master:"+ tbl_name+ " count: total_row : "+total_row);
		// Query class by school id
		ArrayList<MTemplate> masters = (ArrayList<MTemplate>) masterTblService.findBySchool(tbl_name, user.getSchool_id(), from_row, max_result);
		
		listResp.setList(masters);
		listResp.setFrom_row(from_row);
		listResp.setTo_row(from_row + max_result);
		listResp.setTotal_count(total_row);
	    //return listResp;
		
		rsp.setMessageObject(listResp);
		return rsp;
	}
	
	@Secured({ "ROLE_ADMIN","ROLE_SYS_ADMIN" })
	@RequestMapping(value = "/api/masters/create/{tbl_name}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public MasterBase createMaster(
			 @PathVariable String  tbl_name,
			 @RequestBody MTemplate mtemplate
			 ) {
		logger.info(" *** createMaster/{tbl_name}:"+tbl_name);
		User user = getCurrentUser();
				
		return masterTblService.insertMTemplate(user,tbl_name, mtemplate);
	 }

	@Secured({ "ROLE_ADMIN","ROLE_SYS_ADMIN" })
	@RequestMapping(value = "/api/masters/update/{tbl_name}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public MasterBase updateMaster(
			 @PathVariable String  tbl_name,
			 @RequestBody MTemplate mtemplate
			 ) {
		logger.info(" *** updateMaster/{tbl_name}:"+tbl_name);
		User user = getCurrentUser();
		if (mtemplate.getId() == null || mtemplate.getId().intValue() <= 0){
			throw new ESchoolException("id is required", HttpStatus.BAD_REQUEST);
		}
		return masterTblService.updateTranMTemplate(user,tbl_name, mtemplate);
	 }

	@Secured({ "ROLE_ADMIN","ROLE_SYS_ADMIN" })
	@RequestMapping(value = "/api/masters/delete/{tbl_name}/{id}", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)	
	 public String delMaster(
			 @PathVariable String  tbl_name,
			 @PathVariable int  id
			 ) {
		
		logger.info(" *** MainRestController.delMaster/{table}/{id}:"+tbl_name+"/"+id);
		User user = getCurrentUser();
		masterTblService.deleteMTemplate(user, tbl_name, id);
		
	    return "Request was successfully, delMaster:"+ tbl_name + " of id: "+id;
	 }
	
	@RequestMapping(value = "/api/masters/{tbl_name}/{id}", method = RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)	
	 public MasterBase getMaster(
			 @PathVariable String  tbl_name,
			 @PathVariable int  id
			 ) {
		logger.info(" *** getMaster/{table}/{id}:"+tbl_name+"/"+id);

		
		MTemplate template = masterTblService.findById(tbl_name, id);
	    return template;
	 }
	
				
}
