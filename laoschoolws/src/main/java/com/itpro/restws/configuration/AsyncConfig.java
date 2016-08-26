package com.itpro.restws.configuration;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/***
 * To enable support for @Scheduled and @Async annotations 
 * add @EnableScheduling and @EnableAsync to one of your @Configuration classes:
 * @author Huy
 *
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer  {

	 @Override
	    public Executor getAsyncExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(50);
	        executor.setMaxPoolSize(100);
	        executor.setQueueCapacity(500);
	        executor.initialize();
	        return executor;
	    }

	    @Override
	    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	        return new SimpleAsyncUncaughtExceptionHandler();
	    }
}