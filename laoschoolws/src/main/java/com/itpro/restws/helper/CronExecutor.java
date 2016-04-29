package com.itpro.restws.helper;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;

import com.itpro.restws.dao.CommandDao;
import com.itpro.restws.model.Command;
import com.itpro.restws.model.User;
import com.itpro.restws.service.UserService;

public class CronExecutor {
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
		this.name = "CrontExecutor[+"+cron_id+"]";
	}
	/**
	 * fixedRate specifies the number of milliseconds between each method start , regardless of how long method takes to complete. 
	 * fixedDelay specifies the number of milliseconds between completion of previous run, and start of next run.
	 */
	@Scheduled(fixedDelay=5000)
     public void work() {
         // task execution logic
			try {
				logger.info("Work Start,ID="+cron_id);
				ArrayList<Command> list = (ArrayList<Command>) commandDao.findUnProcessed();
				if (list.size() > 0){
					logger.info("Found :"+list.size()+" tasks");
				}
				for (Command command:list){
					try{
						command.setProcessed(1);
						command.setProcessed_dt(Utils.now());
						logger.info("command:"+command.toString());
						if ("FOROT_PASS".equals(command.getCommand())){
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
								String ret = userService.forgotPassword(sso_id, phone);
								logger.info("result:"+ret);
								command.setSuccess(1);
								command.setMessage(ret);
							}else{
								throw new ESchoolException("FORGOT Password, invalid params: correct format: sso_id=xxx&phone=xxx",HttpStatus.BAD_REQUEST);
							}
								
						}
					}catch (ESchoolException e){
						command.setSuccess(0);
						command.setMessage(e.getError_msg());
					}catch (Exception e){
						command.setSuccess(0);
						command.setMessage(e.getMessage());
					}finally{
						commandDao.updateCommand(command);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		
     }
}
