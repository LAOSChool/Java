package com.itpro.restws.configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.itpro.restws.service.CommandExecutor;

/***
 * To enable support for @Scheduled and @Async annotations 
 * add @EnableScheduling and @EnableAsync to one of your @Configuration classes:
 * @author Huy
 *
 */
@Configuration
@EnableScheduling
public class CronConfig implements SchedulingConfigurer {

	 @Override
     public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
         taskRegistrar.setScheduler(taskExecutor());
     }

     @Bean(destroyMethod="shutdown")
     public Executor taskExecutor() {
         return Executors.newScheduledThreadPool(100);
     }
     @Bean
	   public CommandExecutor task() {
	       return new CommandExecutor();
	   }
}