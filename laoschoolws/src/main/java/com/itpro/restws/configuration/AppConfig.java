package com.itpro.restws.configuration;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.itpro.restws")
public class AppConfig extends WebMvcConfigurerAdapter {
	
	  @Bean(name = "multipartResolver")
	    public StandardServletMultipartResolver resolver() {
	        return new StandardServletMultipartResolver();
	    }
	
	@Bean(name = "messageSource")
	  public ResourceBundleMessageSource messageSource() {
	      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	      messageSource.setBasename("messages_en_US");
	      messageSource.setDefaultEncoding("UTF-8");
	      messageSource.setUseCodeAsDefaultMessage(true);
	      return messageSource;
	  }
	
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
	    CharacterEncodingFilter filter = new CharacterEncodingFilter();
	    filter.setEncoding("UTF-8");

	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    registrationBean.setFilter(filter);
	    registrationBean.addUrlPatterns("/*");
	    return registrationBean;
	}
	
	/*
	 * Configure ResourceHandlers to serve static resources like CSS/ Javascript
	 * etc...
	 *
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}
	
	  @Override
	    public void configureViewResolvers(ViewResolverRegistry registry) {
	        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
	        viewResolver.setViewClass(JstlView.class);
	        viewResolver.setPrefix("/WEB-INF/views/");
	        viewResolver.setSuffix(".jsp");
	        registry.viewResolver(viewResolver);
	    }
	  
}