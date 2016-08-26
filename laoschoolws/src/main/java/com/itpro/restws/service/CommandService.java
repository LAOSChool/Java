package com.itpro.restws.service;

import com.itpro.restws.model.Command;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.Notify;
import com.itpro.restws.model.User;

public interface CommandService {
	Command create_class_message_cmd(User me, Message msg_task, String filter_roles);
	Command create_notify_cmd(User me, Notify notify_task, String filter_roles);
	Command create_user_forgot_pass_cmd(User me, String sso_id, String phone);
	
}