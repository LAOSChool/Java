package com.itpro.restws.service;

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
	 
	// @Scheduled(fixedDelay=30000)
	@Scheduled(fixedRate=30000) // => Multiple thread enable
     public void work() {
		// Check any change in sys_settings
		SysTemplate symTemplate = sysTblService.findBySval("sys_settings", "LOG_METHODS");
		if (symTemplate != null  && symTemplate.getLval() != null && symTemplate.getLval().trim().length() > 0){
			synchronized (TokenAuthenticationFilter.LOG_METHODS) {
				TokenAuthenticationFilter.LOG_METHODS = symTemplate.getLval();
				logger.info("\nLOG_METHODS:"+TokenAuthenticationFilter.LOG_METHODS +"\n");
			}
			
		}
		// Execute command in separate threads ( may be overlap)
		asyncRunner.doIt();

     }
}
