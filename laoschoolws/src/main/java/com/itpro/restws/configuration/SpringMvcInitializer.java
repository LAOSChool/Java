package com.itpro.restws.configuration;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@PropertySource("classpath:application.properties")
public class SpringMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Value("${temp_location}")
	private static String temp_location;
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}
 
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}
 
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	 @Override
	    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
	        registration.setMultipartConfig(getMultipartConfigElement());
	    }
	 private MultipartConfigElement getMultipartConfigElement() {
	        MultipartConfigElement multipartConfigElement = new MultipartConfigElement( LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
	        return multipartConfigElement;
	    }
	 
//    private static final String LOCATION = "/tmp/"; // Temporary location where files will be stored
	//private static final String LOCATION = "D:/tmp/"; // Temporary location where files will be stored
	 private static final String LOCATION =temp_location;
	 
//	    private static final long MAX_FILE_SIZE = 2097152;// 2M 
	 private static final long MAX_FILE_SIZE = 5242880;//5MB : Max file size.
	                                                        // Beyond that size spring will throw exception.
	    private static final long MAX_REQUEST_SIZE =20971520;// 20MB : Total request size containing Multi part.
	     
	    private static final int FILE_SIZE_THRESHOLD = 0; // Size threshold after which files will be written to disk

	  //To resolve ${} in @Value
	    //To resolve ${} in @Values, you must register a static PropertySourcesPlaceholderConfigurer in either XML or annotation configuration file.
		@Bean
		public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
			return new PropertySourcesPlaceholderConfigurer();
		}
}
