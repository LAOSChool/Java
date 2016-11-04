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
	
	protected static String  ADM1_AUTH_KEY = "8Cy6pdZ2VcpayQ+0t5DjjNjzmOHtsAnoDh4csX2PyoQ=";
	protected static String  TEA1_AUTH_KEY = "md5tGC3uIe1j2Xy+FRBhCy0QZ0CDBKM5bYQtKN6TMKE=";
	protected static String  STD10_AUTH_KEY = "m7DWYTq5T+K78VLTnbkhXHXuOBLsU/3whDwArrd0Fwo=";
	protected static String  CLS_PRESIDENT1_KEY = "4eCMdMbdKdIlLm3xFpZnNCZ751EXnwwhAKdZ2nj18+8="; // lop truong 1a
	
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
