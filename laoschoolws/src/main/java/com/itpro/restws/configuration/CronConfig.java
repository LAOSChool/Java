package com.itpro.restws.configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.itpro.restws.helper.CronExecutor;

//@Configuration
//@EnableScheduling
public class CronConfig implements SchedulingConfigurer{

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// TODO Auto-generated method stub
		
	}

//	@Autowired
//	CronExecutor cronExecutor;
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//         taskRegistrar.setScheduler(taskExecutor());    
//    }
//
//
//     @Bean(destroyMethod="shutdown")
//     public Executor taskExecutor() {
//    	 Executor executor = Executors.newScheduledThreadPool(10);
//    	 //executor.execute(new CronExecutor("CrontaTab"));
//    	 executor.execute(cronExecutor);
//    	 return executor;
//     }
//     
     

}