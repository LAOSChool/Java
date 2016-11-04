package com.itpro.restws.service;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.itpro.restws.model.SysTemplate;
import com.itpro.restws.security.TokenAuthenticationFilter;

public class CommandExecutor {
	
	
	@Autowired
	protected AsyncRunner asyncRunner;
	
	@Autowired
	protected SysTblService sysTblService;
	private static final Logger logger = Logger.getLogger(CommandExecutor.class);
	/**
	 * fixedRate specifies the number of milliseconds between each method start , regardless of how long method takes to complete. 
	 * fixedDelay specifies the number of milliseconds between completion of previous run, and start of next run.
	 * For fixed-delay and fixed-rate tasks, an initial delay may be specified indicating the number of milliseconds to wait before the first execution of the method.
	 * @Scheduled(initialDelay=1000, fixedRate=5000)
	 * If simple periodic scheduling is not expressive enough, then a cron expression may be provided. For example, the following will only execute on weekdays.
	 * */
	 //* @Scheduled(cron="*/5 * * * * MON-FRI")
	 
	// @Scheduled(fixedDelay=20000)
	@Scheduled(fixedRate=20000) // => Multiple thread enable
     public void work() {
		// Check any change in sys_settings
		SysTemplate symTemplate = sysTblService.findBySvalOne("sys_settings", "LOG_METHODS");
		if (symTemplate != null  && symTemplate.getLval() != null && symTemplate.getLval().trim().length() > 0){
			synchronized (TokenAuthenticationFilter.LOG_METHODS) {
				String logmethod = symTemplate.getLval();
				if (logmethod != null && 
						logmethod.trim().length() > 0 && 
						(!logmethod.equalsIgnoreCase(TokenAuthenticationFilter.LOG_METHODS))){
					TokenAuthenticationFilter.LOG_METHODS = logmethod;
					logger.info("Found changed from SysSettings: LOG METHOD:"+TokenAuthenticationFilter.LOG_METHODS +"\n");
				}
				
			}
			
		}
		// Execute command in separate threads ( may be overlap)
		asyncRunner.execCommands();

     }
	
	 @Scheduled(cron="0 */1 * * * * ") // Run every minute
     public void work_report() {
		// Check any change in sys_settings
			ArrayList<SysTemplate> list = sysTblService.findBySvalAll("sys_settings", "DAILY_REPORT");
			// String upload_dir = environment.getRequiredProperty("avatar_upload_base");
			
			for (SysTemplate symTemplate: list){
				if (symTemplate != null  && 
						symTemplate.getFval1() != null && 
						symTemplate.getFval2() != null 
						){
//						logger.info("sys_settings[DAILY_REPORT]["+symTemplate.getId().intValue()+"], fval1:"+symTemplate.getFval1().intValue() +"\n");
//						logger.info("sys_settings[DAILY_REPORT]["+symTemplate.getId().intValue()+"], fval2:"+symTemplate.getFval1().intValue() +"\n");
						
						// Execute command in separate threads ( may be overlap)
						int sys_setting_hour = symTemplate.getFval1().intValue();
						int sys_setting_minute = symTemplate.getFval2().intValue();
						Calendar now = Calendar.getInstance();
						int cal_hour= now.get(Calendar.HOUR_OF_DAY);
						int cal_minute = now.get(Calendar.MINUTE);
						
//						logger.info("[DAILY_REPORT]sys_setting_hour:"+sys_setting_hour +"\n");
//						logger.info("[DAILY_REPORT]sys_setting_minute:"+sys_setting_minute +"\n");
//						logger.info("[DAILY_REPORT]cal_hour:"+cal_hour +"\n");
//						logger.info("[DAILY_REPORT]cal_minute:"+cal_minute +"\n");
						
						if (	(sys_setting_hour == cal_hour) && 
								(sys_setting_minute == cal_minute)){
							
							logger.info("[DAILY_REPORT]sys_setting_hour:"+sys_setting_hour +"\n");
							logger.info("[DAILY_REPORT]sys_setting_minute:"+sys_setting_minute +"\n");
							logger.info("[DAILY_REPORT]cal_hour:"+cal_hour +"\n");
							logger.info("[DAILY_REPORT]cal_minute:"+cal_minute +"\n");
							// Execute command in separate threads ( may be overlap)
							asyncRunner.execDailyReport(Integer.valueOf(12),null);// From school_id 12
						}
					}
					
				}

     }
}
