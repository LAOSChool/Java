package com.itpro.restws.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.itpro.restws.helper.CronExecutor;

@Configuration
@EnableScheduling
public class CronConfig {

	@Bean
    public CronExecutor task() {
        return new CronExecutor();
    }
}