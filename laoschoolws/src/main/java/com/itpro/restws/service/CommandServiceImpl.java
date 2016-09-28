package com.itpro.restws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itpro.restws.dao.CommandDao;
import com.itpro.restws.helper.Constant;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Command;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.Notify;
import com.itpro.restws.model.User;

@Service("commandService")
@Transactional
public class CommandServiceImpl implements CommandService{
	@Autowired
	protected CommandDao commandDao;

	@Override
	public Command create_class_message_cmd(User me, Message msg_task, String filter_roles) {
		Command cmd = new Command();
		cmd = new Command();
		cmd.setCommand(Constant.CMD_MESSAGE);
		
		cmd.setSchool_id(me.getSchool_id());
		
		cmd.setParams("me_id="+me.getId().intValue()+"&task_id="+msg_task.getTask_id().intValue()+"&filter_roles="+(filter_roles==null?"":filter_roles));
		cmd.setCmd_dt(Utils.now());
		cmd.setProcessed(0);
		cmd.setMessage("Waiting");
		commandDao.saveCommand(cmd);
		return cmd;
	}

	@Override
	public Command create_notify_cmd(User me, Notify notify_task, String filter_roles) {
		Command cmd = new Command();
		cmd = new Command();
		
		cmd.setSchool_id(me.getSchool_id());
		cmd.setCommand(Constant.CMD_NOTIFY);
		cmd.setParams("me_id="+me.getId().intValue()+"&task_id="+notify_task.getTask_id().intValue()+"&filter_roles="+(filter_roles==null?"":filter_roles));
		cmd.setCmd_dt(Utils.now());
		cmd.setProcessed(0);
		cmd.setMessage("Waiting");
		commandDao.saveCommand(cmd);
		return cmd;
	}

	@Override
	public Command create_user_forgot_pass_cmd(User me, String sso_id, String phone) {
		Command cmd = new Command();
		cmd.setCommand(Constant.CMD_FOROT_PASS);
		
		cmd.setSchool_id(me.getSchool_id());
		cmd.setParams("sso_id="+sso_id+"&phone="+phone);
		cmd.setCmd_dt(Utils.now());
		cmd.setProcessed(0);
		cmd.setMessage("Waiting");
		commandDao.saveCommand(cmd);
		return cmd;
	}

	@Override
	public Command create_rank_process(User me, String class_ids, String ex_key) {
		if (class_ids == null || class_ids.trim().length() == 0){
			return null;
		}
		
		Command cmd = new Command();
		cmd = new Command();
		cmd.setCommand(Constant.CMD_RANK_PROCESS);
		
		cmd.setSchool_id(me.getSchool_id());
		
		
		cmd.setParams("me_id="+me.getId().intValue()+"&class_ids="+class_ids+"&ex_key="+ex_key);
		cmd.setCmd_dt(Utils.now());
		cmd.setProcessed(0);
		cmd.setMessage("Waiting");
		commandDao.saveCommand(cmd);
		return cmd;
	}
	
	
	
}
