package com.itpro.restws.helper;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.itpro.restws.dao.CommandDao;
import com.itpro.restws.model.Command;
import com.itpro.restws.service.UserService;

@Component
public class CronExecutor implements  Runnable{
	protected static final Logger logger = Logger.getLogger(CronExecutor.class);
	@Autowired
	protected ApplicationContext applicationContext;
	@Autowired
	protected CommandDao commandDao;
	@Autowired
	protected UserService userService;
	
	
	String name;
	static int cron_id=0;
	static int ide_time=0;
	public CronExecutor(){
		cron_id ++;
		this.name = "ESCHOOL_CrontabEsecutor";
	}
	@Override
	public void run() {
		logger.info("LAOESCHOOL_CrontabName="+name + " is running, ID="+cron_id);
		while(true){
//			if (ide_time > 60000){
//				ide_time = 0;
//				logger.info("LAOESCHOOL_CrontabName="+name + " is running, ID="+cron_id);
//			}
			try {
				logger.info("FindUnProcess Start,ID="+cron_id);
				try{
					//ArrayList<Command> list = (ArrayList<Command>) commandDao.findUnProcessed();
				}catch (Exception e){
					e.printStackTrace();
					logger.error(e.getMessage());
				}
				logger.info("FindUnProcess End,ID="+cron_id);
//				for (Command command:list){
//					try{
//						logger.info("command:"+command.toString());
//						if ("FOROT_PASS".equals(command.getCommand())){
//							if  (command.getParams() == null ){
//								throw new ESchoolException("FORGOT Password, no params: sso_id=xxx&phone=xxx",HttpStatus.BAD_REQUEST);
//							}
//							
//							if (command.getParams().matches("sso_id=[-a-zA-Z0-9]+&phone=\\d{3,20}")){
//								String[] params = command.getParams().split("&");//sso_id=xxxx&phone=xxxx
//								String sso_id = params[0].split("=")[1];
//								String phone = params[1].split("=")[1];
//								logger.info("sso_id:"+sso_id);
//								logger.info("phone:"+phone);
//								
//								User user = userService.findBySso(sso_id);
//								if (user == null || (user.getState() != E_STATE.ACTIVE.value())){
//									throw new ESchoolException("sso_id:("+sso_id+") is not exising",HttpStatus.BAD_REQUEST);
//								}
//								if (user.getPhone() == null ||  (!user.getPhone().equals(phone))){
//									throw new ESchoolException("phone:("+phone+") is not mapped with user's phone",HttpStatus.BAD_REQUEST);
//								}
//								String ret = userService.forgotPassword(sso_id, phone);
//								logger.info("result:"+ret);
//							}else{
//								throw new ESchoolException("FORGOT Password, invalid params: correct format: sso_id=xxx&phone=xxx",HttpStatus.BAD_REQUEST);
//							}
//								
//						}
//					}catch (Exception e){
//						command.setSuccess(0);
//						command.setMessage(e.getMessage());
//						commandDao.updateCommand(command);
//					}
//					
//				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			finally{
				try {
//					ide_time = ide_time+5000;
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.error(e.getMessage());

				}
			}
		}
		
	}


}
