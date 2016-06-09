package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ActionLogDao;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.ActionLog;
import com.itpro.restws.model.User;

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
	public ActionLog insertAction(ActionLog act) {
		
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
		
		act.setRequest_data(act.getRequest_data() + "\n--- REST Response ---\n"+resp_data);
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
	public ActionLog start_trace(HttpServletRequest request,User user) {
		ActionLog act = new ActionLog();
		
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
	                   .append("]\n[LOCAL ADDRESS:")
	                   .append(request.getLocalAddr())
	                   .append("\n");
	
		act.setRequest_data(logMessage.toString());
		act.setRequest_dt(Utils.now());
		act.setRequest_method(request.getMethod());
		act.setRequest_url(request.getServletPath());
		// User info
		act.setSchool_id(user.getSchool_id());
		act.setSso_id(user.getSso_id());
		act.setUser_role(user.getRoles());
		actionLogDao.saveAction(act);
		return act;
	}
	

}
