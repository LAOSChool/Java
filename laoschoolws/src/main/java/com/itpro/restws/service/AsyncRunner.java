package com.itpro.restws.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.itpro.restws.dao.CommandDao;
import com.itpro.restws.helper.ESchoolException;
import com.itpro.restws.helper.E_DEST_TYPE;
import com.itpro.restws.helper.E_STATE;
import com.itpro.restws.helper.Utils;
import com.itpro.restws.model.Command;
import com.itpro.restws.model.Message;
import com.itpro.restws.model.Notify;
import com.itpro.restws.model.User;

@Service("asyncRunner")
public class AsyncRunner {
	protected static final Logger logger = Logger.getLogger(AsyncRunner.class);
	@Autowired
	protected ApplicationContext applicationContext;
	@Autowired
	protected CommandDao commandDao;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected MessageService messageService;
	
	@Autowired
	protected NotifyService notifyService;
	
	static int cron_id=0;
	 @Async
	 public void doIt(){
		 // task execution logic
			try {
				cron_id++;
				logger.info("++++++ AsyncRunner START,ID="+cron_id);
				ArrayList<Command> list = (ArrayList<Command>) commandDao.findUnProcessed();
				if (list.size() > 0){
					logger.info("Found :"+list.size()+" tasks");
				}
				for (Command command:list){
					try{
						command.setProcessed(1);
						command.setProcessed_dt(Utils.now());
						logger.info("command[cron_id]="+cron_id+"///"+command.toString());
						// Forgot Password
						if ("FOROT_PASS".equals(command.getCommand())){
							proc_gorgot_pass(command);
								
						}
						// Send Message To Classes
						else if ("MESSAGE".equals(command.getCommand())){
							proc_message(command);
								
						}else if ("NOTIFY".equals(command.getCommand())){
							proc_notify(command);
								
						}
	
					}catch (ESchoolException e){
						command.setSuccess(0);
						command.setMessage(e.getError_msg());
					}catch (Exception e){
						command.setSuccess(0);
						command.setMessage(e.getMessage()+"///cause:"+(e.getCause()==null?"---":e.getCause().toString()));
					}finally{
						commandDao.updateCommand(command);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}finally{
				logger.info("++++++ AsyncRunner END,ID="+cron_id);
				cron_id--;
			}
			
	     }
		
		
		
		private void proc_gorgot_pass(Command command){
			logger.info("proc_gorgot_pass() START, command:"+command.toString());
			if  (command.getParams() == null ){
				throw new ESchoolException("FORGOT Password, no params: sso_id=xxx&phone=xxx",HttpStatus.BAD_REQUEST);
			}
			
			if (command.getParams().matches("sso_id=[-a-zA-Z0-9]+&phone=\\d{3,20}")){
				String[] params = command.getParams().split("&");//sso_id=xxxx&phone=xxxx
				String sso_id = params[0].split("=")[1];
				String phone = params[1].split("=")[1];
				logger.info("sso_id:"+sso_id);
				logger.info("phone:"+phone);
				
				User user = userService.findBySso(sso_id);
				if (user == null || (user.getState() != E_STATE.ACTIVE.value())){
					throw new ESchoolException("sso_id:("+sso_id+") is not exising",HttpStatus.BAD_REQUEST);
				}
				if (user.getPhone() == null ||  (!user.getPhone().equals(phone))){
					throw new ESchoolException("phone:("+phone+") is not mapped with user's phone",HttpStatus.BAD_REQUEST);
				}
				String ret = userService.forgotPassword(user,sso_id, phone);
				logger.info("result:"+ret);
				command.setSuccess(1);
				command.setMessage(ret);
			}else{
				throw new ESchoolException("FORGOT Password, invalid params: correct format: sso_id=xxx&phone=xxx",HttpStatus.BAD_REQUEST);
			}
			logger.info("proc_gorgot_pass() END, command:"+command.toString());
			
		}
		private void proc_notify(Command command) {
			logger.info("proc_notify() START, command:"+command.toString());
			if  (command.getParams() == null ){
				throw new ESchoolException("NOTIFY, no params: please check param format: me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*",HttpStatus.BAD_REQUEST);
			}
			
			if (command.getParams().matches("me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*")){
				String[] params = command.getParams().split("&");//me_id=yyy&task_id=xxxx&filter_rolese=xxxx
				
				Integer me_id = Utils.parseInteger(params[0].split("=")[1]);
				Integer task_id = Utils.parseInteger(params[1].split("=")[1]);
				
				String filter_roles = null;
				String[] roles = params[2].split("=");
				
				if (roles != null && roles.length > 1){
					filter_roles = params[2].split("=")[1];
				}
				
				logger.info("proc_notify():task_id:"+task_id.intValue());
				logger.info("proc_notify():filter_roles:"+filter_roles);
				if (filter_roles !=null && filter_roles.trim().length() == 0){
					filter_roles = null;
				}
				if (filter_roles !=null && filter_roles.equalsIgnoreCase("null")){
					filter_roles = null;
				}
				if (me_id ==  null || me_id.intValue() <= 0){
					throw new ESchoolException("NOTIFY,me_id=NULL",HttpStatus.BAD_REQUEST);
				}
				if (task_id ==  null || task_id.intValue() <= 0){
					throw new ESchoolException("NOTIFY,task_id=NULL",HttpStatus.BAD_REQUEST);
				}
				// Send message
				User me = userService.findById(me_id);
				if (me == null ){
					throw new ESchoolException("NOTIFY, me_id not existing: task_id="+task_id.intValue()+"///me_id:"+me_id.intValue(),HttpStatus.BAD_REQUEST);
				}
				Notify notifyTask = notifyService.findById(task_id);
				if (notifyTask == null ){
					throw new ESchoolException("NOTIFY, task not existing: task_id="+task_id.intValue(),HttpStatus.BAD_REQUEST);
				}
				
				ArrayList<Notify> list  = notifyService.broadcastNotify(me, notifyTask, filter_roles); //20160823
				String info ="result: list.size()="+(list == null ?"0":list.size()+"");
				logger.info("proc_notify(): result: list.size()="+list == null ?0:list.size());
				command.setSuccess(1);
				command.setMessage("Finished at:"+Utils.now()+"///"+info);
				
				
			}else{
				throw new ESchoolException("NOTIFY, invalid params: correct format:me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*",HttpStatus.BAD_REQUEST);
			}
			logger.info("proc_notify() START, command:"+command.toString());
			
		}
		private void proc_message(Command command) {
			logger.info("proc_message() START, command:"+command.toString());
			if  (command.getParams() == null ){
				throw new ESchoolException("MESSAGE, no params please check params format: me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*",HttpStatus.BAD_REQUEST);
			}
			
			if (command.getParams().matches("me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*")){
				String[] params = command.getParams().split("&");//me_id=yyy&task_id=xxxx&filter_rolese=xxxx
				
				Integer me_id = Utils.parseInteger(params[0].split("=")[1]);
				Integer task_id = Utils.parseInteger(params[1].split("=")[1]);
				
				String filter_roles = null;
				String[] roles = params[2].split("=");
				
				if (roles != null && roles.length > 1){
					filter_roles = params[2].split("=")[1];
				}
				
				logger.info("proc_message():task_id:"+task_id.intValue());
				logger.info("proc_message():filter_roles:"+filter_roles);
				if (filter_roles !=null && filter_roles.trim().length() == 0){
					filter_roles = null;
				}
				if (filter_roles !=null && filter_roles.equalsIgnoreCase("null")){
					filter_roles = null;
				}
				if (me_id ==  null || me_id.intValue() <= 0){
					throw new ESchoolException("MESSAGE,me_id=NULL",HttpStatus.BAD_REQUEST);
				}
				if (task_id ==  null || task_id.intValue() <= 0){
					throw new ESchoolException("MESSAGE,task_id=NULL",HttpStatus.BAD_REQUEST);
				}
				// Send message
				User me = userService.findById(me_id);
				if (me == null ){
					throw new ESchoolException("MESSAGE, me_id not existing: task_id="+task_id.intValue()+"///me_id:"+me_id.intValue(),HttpStatus.BAD_REQUEST);
				}
				Message messageTask = messageService.findById(task_id);
				if (messageTask == null ){
					throw new ESchoolException("MESSAGE, task not existing: task_id="+task_id.intValue(),HttpStatus.BAD_REQUEST);
				}
				
				
				if (messageTask.getDest_type() == E_DEST_TYPE.CLASS.getValue()){
					
					ArrayList<Message> list  = messageService.sendClassMessage(me, messageTask, filter_roles);
					
					String info ="result: list.size()="+(list == null ?"0":list.size()+"");
					logger.info("proc_message(): result: list.size()="+list == null ?0:list.size());
					command.setSuccess(1);
					command.setMessage("Finished at:"+Utils.now()+"///"+info);
				}else{
					throw new ESchoolException("MESSAGE, message.dest_type not supported, dest_type =="+messageTask.getDest_type().intValue(),HttpStatus.BAD_REQUEST);
				}
				
				
			}else{
				throw new ESchoolException("MESSAGE, invalid params: correct format:me_id=[0-9]+&task_id=[0-9]+&filter_roles=[^=]*",HttpStatus.BAD_REQUEST);
			}
			logger.info("proc_message() END, command:"+command.toString());
			
		}
		
}
