package com.itpro.restws.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ActionLogDao;
import com.itpro.restws.helper.AuthenticationRequestWrapper;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.MultipartFromWrapper;
import com.itpro.restws.helper.MyRequestWrapper;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ActionLog;
import com.itpro.restws.model.User;

import sun.misc.IOUtils;

@Service("actionLogService")
@Transactional
public class ActionLogServiceImpl implements ActionLogService{

	@Autowired
	private ActionLogDao actionLogDao;

	@Override
	public ActionLog findById(Integer id) {
		
		return actionLogDao.findById(id);
	}

	@Override
	public int countBySchool(Integer school_id) {
		
		return actionLogDao.countBySchool(school_id);
	}

	@Override
	public int countByUser(Integer user_id) {
		return actionLogDao.countBySchool(user_id);
	}

	@Override
	public ArrayList<ActionLog> findBySchool(Integer school_id, int from_num, int max_result) {
		
		return (ArrayList<ActionLog>) actionLogDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<ActionLog> findByUser(Integer user_id, int from_num, int max_result) {
		
		return (ArrayList<ActionLog>) actionLogDao.findByUser(user_id, from_num, max_result);
	}

	@Override
	public ActionLog insertAction( ActionLog act) {
		
		actionLogDao.saveAction(act);;
		return act;
	}

	@Override
	public ActionLog updateAction(ActionLog act) {
		actionLogDao.updateAction(act);
		return act;
	}

	
//	@Override
//	public ActionLog logUserContext(Integer act_id, User user) {
//		ActionLog act = findById(act_id);
//		act.setUser_id(user.getId());
//		act.setUser_name(user.getFullname());
//		act.setUser_role(user.getRoles());
//		return act;
//	}

//	@Override
//	public ActionLog start_trace(HttpServletRequest request) {
//		ActionLog act = new ActionLog();
//		
//		 Map<String, String> requestMap = this.getTypesafeRequestMap(request);
//		 Map<String, String> headerMap = this.getTypesafeRequestHeaderMap(request);
//	        final StringBuilder logMessage = new StringBuilder("REST Request - ")
//	                   .append("[HTTP METHOD:")
//                        .append(request.getMethod())                                        
//	                   .append("]\n[PATH INFO:")
//                        .append(request.getPathInfo())
//                        .append("]\n[REQUEST HEADERS:")
// 	                   .append(headerMap)
//	                   .append("]\n[REQUEST PARAMETERS:")
//	                   .append(requestMap)
//	                   .append("]\n[REMOTE ADDRESS:")
//	                   .append(request.getRemoteAddr())
//	                   .append("]\n[LOCAL ADDRESS:")
//	                   .append(request.getLocalAddr())
//	                   .append("]");
//	
//		act.setRequest_data(logMessage.toString());
//		act.setRequest_dt(Utils.now());
//		act.setRequest_method(request.getMethod());
//		act.setRequest_url(request.getServletPath());
//		actionLogDao.saveAction(act);
//		return act;
//	}
//
//	@Override
//	public void end_trace(Integer id, HttpServletResponse response,long duration) {
//		ActionLog act = actionLogDao.findById(id);
//		act.setResp_dt(Utils.now());
//		act.setExec_duration(duration);
//		act.setResp_status(response.getStatus());
//		
//		actionLogDao.updateAction(act);
//		
//	}

	@Override
	public void end_trace(Integer id, String resp_data, int resp_sts,long duration) {
		ActionLog act = actionLogDao.findById(id);
		
		act.setResp_dt(Utils.now());
		act.setExec_duration(duration);
		String req_data =act.getRequest_data() + "\n--- REST Response ---\n"+resp_data;
		act.setRequest_data(req_data.length() > Constant.ActionLogMaxLength?req_data.substring(0, Constant.ActionLogMaxLength-1):req_data);
		act.setResp_status(resp_sts);
		
		actionLogDao.updateAction(act);
		
	}

	private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
		Map<String, String> typesafeRequestMap = new HashMap<String, String>();
		Enumeration<?> requestParamNames = request.getParameterNames();
		while (requestParamNames.hasMoreElements()) {
			String requestParamName = (String)requestParamNames.nextElement();
			String requestParamValue = request.getParameter(requestParamName);
			typesafeRequestMap.put(requestParamName, requestParamValue);
		}
		return typesafeRequestMap;
	}
	
	private Map<String, String> getTypesafeRequestHeaderMap(HttpServletRequest request) {
		Map<String, String> typesafeHeaderMap = new HashMap<String, String>();
		Enumeration<?> requestHeaderNames = request.getHeaderNames();
		while (requestHeaderNames.hasMoreElements()) {
			String requestHeaderName = (String)requestHeaderNames.nextElement();
			String requestHeaderValue = request.getHeader(requestHeaderName);
			typesafeHeaderMap.put(requestHeaderName, requestHeaderValue);
		}
		return typesafeHeaderMap;
	}

	@Override
	public ActionLog start_trace(HttpServletRequest request,User me) {
		boolean is_loggin = false;
		String log_content = "";
		ActionLog act = new ActionLog();
		/// Log JSON
		//  Only if /api/sms/log
		if (request.getRequestURL().toString().contains("/api/sms/log")){
			is_loggin = true;
			try {
				
				byte[] jsonBody = IOUtils.readFully(request.getInputStream(),-1,true);
				log_content = new String(jsonBody);
			} catch (IOException e) {
				e.printStackTrace();
			}
	
		}
		//		
		
     
     
		 Map<String, String> requestMap = this.getTypesafeRequestMap(request);
		 Map<String, String> headerMap = this.getTypesafeRequestHeaderMap(request);
	        final StringBuilder logMessage = new StringBuilder("--- REST Request ---\n")
	                   .append("[HTTP METHOD:")
                       .append(request.getMethod())                                        
	                   .append("]\n[PATH INFO:")
                       .append(request.getServletPath())
                       .append("]\n[REQUEST HEADERS:")
	                   .append(headerMap)
	                   .append("]\n[REQUEST PARAMETERS:")
	                   .append(requestMap)
	                   .append("]\n[REMOTE ADDRESS:")
	                   .append(request.getRemoteAddr())
	                   .append("]\n[REMOTE HOST:")
	                   .append(request.getRemoteHost())
	                   .append("]\n[LOCAL ADDRESS:")
	                   .append(request.getLocalAddr())
	                   .append("\n");
        
		act.setRequest_data(logMessage.length() > Constant.ActionLogMaxLength?logMessage.toString().substring(0, Constant.ActionLogMaxLength-1):logMessage.toString());
		if (is_loggin){
			act.setRequest_data("--- REST Request ---\n"+log_content);	
		}
		act.setRequest_dt(Utils.now());
		act.setRequest_method(request.getMethod());
		act.setRequest_url(request.getServletPath());
		// User info
		act.setSchool_id(me.getSchool_id());
		act.setSso_id(me.getSso_id());
		act.setUser_role(me.getRoles());
		actionLogDao.saveAction(act);
		return act;
	}
/*
	private void printRequest(HttpServletRequest httpRequest) {
        System.out.println("receive " + httpRequest.getMethod() +" notification for "+ httpRequest.getRequestURI());


        System.out.println(" \n\n Headers");

        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            System.out.println(headerName + " = " + httpRequest.getHeader(headerName));
        }

        System.out.println("\n\nParameters");

        Enumeration<String> params = httpRequest.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = (String)params.nextElement();
            System.out.println(paramName + " = " + httpRequest.getParameter(paramName));
        }

        System.out.println("\n\n Row data");
        System.out.println(extractPostRequestBody(httpRequest));
    }

    static String extractPostRequestBody(HttpServletRequest request) {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            Scanner s = null;
            try {
                s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s.hasNext() ? s.next() : "";
        }
        return "";
    }
*/
	@Override
	public ActionLog start_tracewrapper(MyRequestWrapper myRequestWrapper, User me) {
		ActionLog act = new ActionLog();
		String body = myRequestWrapper.getBody();
//		byte ptext[] = myRequestWrapper.getBody().getBytes(Constant.ISO_8859_1); 
//		String body = new String(ptext, Constant.UTF_8); 

    
			 Map<String, String> requestMap = this.getTypesafeRequestMap(myRequestWrapper);
			 Map<String, String> headerMap = this.getTypesafeRequestHeaderMap(myRequestWrapper);
		        final StringBuilder logMessage = new StringBuilder("--- REST Request ---\n")
		                   .append("[HTTP METHOD:")
	                       .append(myRequestWrapper.getMethod())                                        
		                   .append("]\n[PATH INFO:")
	                       .append(myRequestWrapper.getServletPath())
	                       .append("]\n[REQUEST HEADERS:")
		                   .append(headerMap)
		                   .append("]\n[REQUEST BODY:")
		                   .append(body)		                   
		                   .append("]\n[REQUEST PARAMETERS:")
		                   .append(requestMap)
		                   .append("]\n[REMOTE ADDRESS:")
		                   .append(myRequestWrapper.getRemoteAddr())
		                   .append("]\n[REMOTE HOST:")
		                   .append(myRequestWrapper.getRemoteHost())
		                   .append("]\n[LOCAL ADDRESS:")
		                   .append(myRequestWrapper.getLocalAddr())
		                   .append("\n");
	        
			act.setRequest_data(logMessage.length() > Constant.ActionLogMaxLength?logMessage.toString().substring(0, Constant.ActionLogMaxLength-1):logMessage.toString());
		
			act.setRequest_dt(Utils.now());
			act.setRequest_method(myRequestWrapper.getMethod());
			act.setRequest_url(myRequestWrapper.getServletPath());
			// User info
			act.setSchool_id(me.getSchool_id());
			act.setSso_id(me.getSso_id());
			act.setUser_role(me.getRoles());
			
			actionLogDao.saveAction(act);
			return act;

	}

	@Override
	public ActionLog start_tracewrapper2(AuthenticationRequestWrapper myRequestWrapper, User me)   {
		try{
		ActionLog act = new ActionLog();
		byte[] bytes = myRequestWrapper.getRequestBody();
		String log_content = new String(bytes);
		
    
			 Map<String, String> requestMap = this.getTypesafeRequestMap(myRequestWrapper);
			 Map<String, String> headerMap = this.getTypesafeRequestHeaderMap(myRequestWrapper);
		        final StringBuilder logMessage = new StringBuilder("--- REST Request ---\n")
		                   .append("[HTTP METHOD:")
	                       .append(myRequestWrapper.getMethod())                                        
		                   .append("]\n[PATH INFO:")
	                       .append(myRequestWrapper.getServletPath())
	                       .append("]\n[REQUEST HEADERS:")
		                   .append(headerMap)
		                   .append("]\n[REQUEST BODY:")
		                   .append(log_content)		                   
		                   .append("]\n[REQUEST PARAMETERS:")
		                   .append(requestMap)
		                   .append("]\n[REMOTE ADDRESS:")
		                   .append(myRequestWrapper.getRemoteAddr())
		                   .append("]\n[REMOTE HOST:")
		                   .append(myRequestWrapper.getRemoteHost())
		                   .append("]\n[LOCAL ADDRESS:")
		                   .append(myRequestWrapper.getLocalAddr())
		                   .append("\n");
	        
			act.setRequest_data(logMessage.length() > Constant.ActionLogMaxLength?logMessage.toString().substring(0, Constant.ActionLogMaxLength-1):logMessage.toString());
		
			act.setRequest_dt(Utils.now());
			act.setRequest_method(myRequestWrapper.getMethod());
			act.setRequest_url(myRequestWrapper.getServletPath());
			// User info
			act.setSchool_id(me.getSchool_id());
			act.setSso_id(me.getSso_id());
			act.setUser_role(me.getRoles());
			
			actionLogDao.saveAction(act);
			return act;
		}catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public ActionLog start_tracewrapper3(MultipartFromWrapper myRequestWrapper, User me) {
		try{
			ActionLog act = new ActionLog();
			
			String log_content =  myRequestWrapper.getRequestBody();
			
	    
				 Map<String, String> requestMap = this.getTypesafeRequestMap(myRequestWrapper);
				 Map<String, String> headerMap = this.getTypesafeRequestHeaderMap(myRequestWrapper);
			        final StringBuilder logMessage = new StringBuilder("--- REST Request ---\n")
			                   .append("[HTTP METHOD:")
		                       .append(myRequestWrapper.getMethod())                                        
			                   .append("]\n[PATH INFO:")
		                       .append(myRequestWrapper.getServletPath())
		                       .append("]\n[REQUEST HEADERS:")
			                   .append(headerMap)
			                   .append("]\n[REQUEST BODY:")
			                   .append(log_content)		                   
			                   .append("]\n[REQUEST PARAMETERS:")
			                   .append(requestMap)
			                   .append("]\n[REMOTE ADDRESS:")
			                   .append(myRequestWrapper.getRemoteAddr())
			                   .append("]\n[REMOTE HOST:")
			                   .append(myRequestWrapper.getRemoteHost())
			                   .append("]\n[LOCAL ADDRESS:")
			                   .append(myRequestWrapper.getLocalAddr())
			                   .append("\n");
		        
				act.setRequest_data(logMessage.length() > Constant.ActionLogMaxLength?logMessage.toString().substring(0, Constant.ActionLogMaxLength-1):logMessage.toString());
			
				act.setRequest_dt(Utils.now());
				act.setRequest_method(myRequestWrapper.getMethod());
				act.setRequest_url(myRequestWrapper.getServletPath());
				// User info
				act.setSchool_id(me.getSchool_id());
				act.setSso_id(me.getSso_id());
				act.setUser_role(me.getRoles());
				
				actionLogDao.saveAction(act);
				return act;
			}catch (Exception e){
				throw new RuntimeException(e.getMessage());
			}
	}

}
