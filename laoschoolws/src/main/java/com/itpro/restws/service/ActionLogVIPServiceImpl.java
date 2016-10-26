package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.ActionLogVIPDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.AbstractModel;
import com.itpro.restws.model.ActionLogVIP;
import com.itpro.restws.model.User;

@Service("actionLogVIPService")
@Transactional
public class ActionLogVIPServiceImpl implements ActionLogVIPService{
	private static final Logger logger = Logger.getLogger(ActionLogVIPServiceImpl.class);
	@Autowired
	private ActionLogVIPDao actionLogVIPDao;
	@Autowired
	private UserService userService;
	
	@Override
	public ActionLogVIP findById(Integer id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("id"+(id==null?"null":id.intValue()));
		
		return actionLogVIPDao.findById(id);
	}

	@Override
	public int countBySchool(Integer school_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("school_id"+(school_id==null?"null":school_id.intValue()));
		
		return actionLogVIPDao.countBySchool(school_id);
	}

	@Override
	public int countBySSO(String sso_id) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("user_id"+(sso_id==null?"null":sso_id));
		
		return actionLogVIPDao.countBySSO(sso_id);
	}

	@Override
	public ArrayList<ActionLogVIP> findBySchool(Integer school_id, int from_num, int max_result) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("school_id"+(school_id==null?"null":school_id.intValue()));
		
		return (ArrayList<ActionLogVIP>) actionLogVIPDao.findBySchool(school_id, from_num, max_result);
	}

	@Override
	public ArrayList<ActionLogVIP> findBySSO(String sso_id, int from_num, int max_result) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("sso_id"+(sso_id==null?"null":sso_id));
		
		return (ArrayList<ActionLogVIP>) actionLogVIPDao.findBySSO(sso_id, from_num, max_result);
	}

	@Override
	public ActionLogVIP insertAction( ActionLogVIP act) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");

		actionLogVIPDao.saveAction(act);;
		return act;
	}

	@Override
	public String get_action_vip_type(String url) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("url"+(url==null?"null":url));
		String key ="";
		String val =null;
		for (Map.Entry<String, String> entry : Constant.MAP_ACTIONS.entrySet())
		{
			key = entry.getKey();
			val = entry.getValue();
			if (url.equalsIgnoreCase(key)){
				break;
			}
		}
		logger.info("val"+(val==null?"null":val));
		return val;
	}

	@Override
	public void logAction_type(User me, String act_type, String content) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("act_type"+(act_type==null?"null":act_type));
		// logger.info("content"+(content==null?"null":content));
		
		ActionLogVIP actionLogVIP = new ActionLogVIP();
		actionLogVIP.setAct_type(act_type);
		actionLogVIP.setSchool_id(me.getSchool_id());
		actionLogVIP.setSso_id(me.getSso_id());
		actionLogVIP.setUser_role(me.getRoles());
		actionLogVIP.setFull_name(me.getFullname());
		actionLogVIP.setContent(content);
		actionLogVIP.setRequest_dt(Utils.currenDate());
		// Insert to DB
		actionLogVIPDao.saveAction(actionLogVIP);
	}
	@Override
	public void logAction_url(User me, String url, AbstractModel model) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("url"+(url==null?"null":url));
		// logger.info("content"+(content==null?"null":content));
		
		String type = get_action_vip_type(url);
		if (type != null ){
			logAction_type(me,type,model.printActLog());
		}
	}

	@Override
	public ActionLogVIP createTmpActionLogVIP(User me, String url, AbstractModel model) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("url"+(url==null?"null":url));
		String type = get_action_vip_type(url);
		if (type != null ){
			ActionLogVIP actionLogVIP = new ActionLogVIP();
			actionLogVIP.setAct_type(type);
			actionLogVIP.setSchool_id(me.getSchool_id());
			actionLogVIP.setSso_id(me.getSso_id());
			actionLogVIP.setUser_role(me.getRoles());
			actionLogVIP.setFull_name(me.getFullname());
			actionLogVIP.setContent(model.printActLog());
			actionLogVIP.setRequest_dt(Utils.now());
			return actionLogVIP;
		}
		return null;
		
	}



	

	@Override
	public int countActionLogExt(User me,
			String filter_sso_id, String filter_from_dt, String filter_to_dt,String filter_type) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("filter_sso_id:"+(filter_sso_id==null?"null":filter_sso_id));
		logger.info("filter_from_dt:"+(filter_from_dt==null?"null":filter_from_dt));
		logger.info("filter_to_dt:"+(filter_to_dt==null?"null":filter_to_dt));
		logger.info("filter_type:"+(filter_type==null?"null":filter_type));
		
		
		return actionLogVIPDao.countActionLogExt(me.getSchool_id(), filter_sso_id, filter_from_dt, filter_to_dt,filter_type);
	}

	@Override
	public ArrayList<ActionLogVIP> findActionLogExt(User me, Integer filter_from_row, Integer filter_max_result,
			String filter_sso_id, String filter_from_dt,String filter_to_dt, String filter_type) {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("filter_from_row:"+(filter_from_row==null?"null":filter_from_row.intValue()));
		logger.info("filter_max_result:"+(filter_max_result==null?"null":filter_max_result.intValue()));
		logger.info("filter_sso_id:"+(filter_sso_id==null?"null":filter_sso_id));
		logger.info("filter_from_dt:"+(filter_from_dt==null?"null":filter_from_dt));
		logger.info("filter_to_dt:"+(filter_to_dt==null?"null":filter_to_dt));
		logger.info("filter_type:"+(filter_type==null?"null":filter_type));
		
		
		
		if (filter_sso_id != null && filter_sso_id.trim().length() >0){
			User user = userService.findBySso(filter_sso_id);
			if (user == null ){
				throw new ESchoolException("filter_user_id is not existing", HttpStatus.BAD_REQUEST);
			}
			if (user.getSchool_id().intValue() != me.getSchool_id().intValue() ){
				throw new ESchoolException("user.school_id != me.school_id", HttpStatus.BAD_REQUEST);
			}
		}
		return (ArrayList<ActionLogVIP>) actionLogVIPDao.findActionLogExt(me.getSchool_id(), filter_from_row, filter_max_result, filter_sso_id, filter_from_dt, filter_to_dt,filter_type);
	}

	@Override
	public void logAction_type_json(User me, String act_type, String content, String json_str) {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(" *** " + method_name + "() START");
		logger.info("act_type"+(act_type==null?"null":act_type));
		// logger.info("content"+(content==null?"null":content));
		
		ActionLogVIP actionLogVIP = new ActionLogVIP();
		actionLogVIP.setAct_type(act_type);
		actionLogVIP.setSchool_id(me.getSchool_id());
		actionLogVIP.setSso_id(me.getSso_id());
		actionLogVIP.setUser_role(me.getRoles());
		actionLogVIP.setFull_name(me.getFullname());
		actionLogVIP.setContent(content);
		actionLogVIP.setRequest_dt(Utils.currenDate());
		actionLogVIP.setStr_json(json_str);
		// Insert to DB
		actionLogVIPDao.saveAction(actionLogVIP);
		
	}


}
