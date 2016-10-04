package com.itpro.restws;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;

import com.jayway.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;

/***
 */
public class FunctionalTest {
	protected static final Logger logger = Logger.getLogger(FunctionalTest.class);
	protected static String WEB_API_KEY = "WEB";
	
	protected static String  ADM1_AUTH_KEY = "vmDYeNgMEfWJDON67eoTdQZ+cubs7NZFGVkr8mdyqGM=";
	protected static String  TEA1_AUTH_KEY = "CiT7+T81LHsdmcMPkESdjJHhQKQe7ftBA3Wi/cGdRlY=";
	protected static String  STD10_AUTH_KEY = "ZQEB7Jt2uTFxv8ZX0jSfFoUR7iVwuYtq+tqqwD7GlNs=";
	protected static String  CLS_PRESIDENT1_KEY = "LaN03FSHarUY8ElfiCdDeTddXEYPiEiIJUU1m18vS68="; // lop truong 1a
	
	protected static String  INVALID_AUTH_KEY = "abc";
	
	 @BeforeClass
	    public static void setup() throws IOException {
		 
		 	logger.info("setup() START");
		 	RestAssured.baseURI = "https://localhost";
		 	RestAssured.basePath = "/laoschoolws/";
		 	RestAssured.port = 8443;
	        RestAssured.useRelaxedHTTPSValidation();// Avoid SSLPeerUnverifiedException
	        
	        JsonPath.config = new JsonPathConfig("UTF-8");
	        
//	        logger.info("baseURI:"+RestAssured.baseURI);
//	        logger.info("port:"+RestAssured.port);
//	        logger.info("basePath:"+RestAssured.basePath);
//	        logger.info("setup() END");
	        
		 

	    }
}
