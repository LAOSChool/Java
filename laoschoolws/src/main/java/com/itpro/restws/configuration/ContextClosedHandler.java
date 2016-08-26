package com.itpro.restws.configuration;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
	protected static final Logger logger = Logger.getLogger(ContextClosedHandler.class);
	
	@Autowired ApplicationContext context;
	
//    @Autowired ThreadPoolTaskExecutor executor;
//    @Autowired ThreadPoolTaskScheduler scheduler;

    public void onApplicationEvent(ContextClosedEvent event) {
    	System.out.println("LAOSCHOOL-ContextClosedHandler : onApplicationEvent START");
    	logger.info("LAOSCHOOL-ContextClosedHandler : onApplicationEvent START");

    	Map<String, ThreadPoolTaskScheduler> schedulers = context.getBeansOfType(ThreadPoolTaskScheduler.class);

    	
    	 for (ThreadPoolTaskScheduler scheduler : schedulers.values()) {         
    	        scheduler.getScheduledExecutor().shutdown();
    	        try {
    	            scheduler.getScheduledExecutor().awaitTermination(20000, TimeUnit.MILLISECONDS);
    	            if(scheduler.getScheduledExecutor().isTerminated() || scheduler.getScheduledExecutor().isShutdown())
    	                logger.info("LAOSCHOOL-ContextClosedHandler -Scheduler "+scheduler.getThreadNamePrefix() + " has stoped");
    	            else{
    	                logger.info("LAOSCHOOL-ContextClosedHandler -Scheduler "+scheduler.getThreadNamePrefix() + " has not stoped normally and will be shut down immediately");
    	                scheduler.getScheduledExecutor().shutdownNow();
    	                logger.info("LAOSCHOOL-ContextClosedHandler -Scheduler "+scheduler.getThreadNamePrefix() + " has shut down immediately");
    	            }
    	        } catch (IllegalStateException e) {
    	            e.printStackTrace();
    	        } catch (InterruptedException e) {
    	            e.printStackTrace();
    	        }
    	    }

    	    Map<String, ThreadPoolTaskExecutor> executers = context.getBeansOfType(ThreadPoolTaskExecutor.class);

    	    for (ThreadPoolTaskExecutor executor: executers.values()) {
    	        int retryCount = 0;
    	        while(executor.getActiveCount()>0 && ++retryCount<51){
    	            try {
    	                logger.info("LAOSCHOOL-ContextClosedHandler -Executer "+executor.getThreadNamePrefix()+" is still working with active " + executor.getActiveCount()+" work. Retry count is "+retryCount);
    	                Thread.sleep(1000);
    	            } catch (InterruptedException e) {
    	                e.printStackTrace();
    	            }
    	        }
    	        if(!(retryCount<51))
    	            logger.info("LAOSCHOOL-ContextClosedHandler -Executer "+executor.getThreadNamePrefix()+" is still working.Since Retry count exceeded max value "+retryCount+", will be killed immediately");
    	        executor.shutdown();
    	        logger.info("LAOSCHOOL-ContextClosedHandler -Executer "+executor.getThreadNamePrefix()+" with active " + executor.getActiveCount()+" work has killed");
    	    }
        System.out.println("LAOSCHOOL-ContextClosedHandler : onApplicationEvent END");
        logger.info("LAOSCHOOL-ContextClosedHandler : onApplicationEvent END");
    } 
    
//    @Override
//    public void setApplicationContext(ApplicationContext context)
//            throws BeansException {
//        this.context = context;
//
//    }
//
//
//    @Override
//    public Object postProcessAfterInitialization(Object object, String arg1)
//            throws BeansException {
//        return object;
//    }
//
//
//    @Override
//    public Object postProcessBeforeInitialization(Object object, String arg1)
//            throws BeansException {
//        if(object instanceof ThreadPoolTaskScheduler)
//            ((ThreadPoolTaskScheduler)object).setWaitForTasksToCompleteOnShutdown(true);
//        if(object instanceof ThreadPoolTaskExecutor)
//            ((ThreadPoolTaskExecutor)object).setWaitForTasksToCompleteOnShutdown(true);
//        return object;
//    }
//    
}