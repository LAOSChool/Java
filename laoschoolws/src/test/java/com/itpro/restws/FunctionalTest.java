package com.itpro.restws;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;

import com.jayway.restassured.RestAssured;

/***
 */
public class FunctionalTest {
	protected static final Logger logger = Logger.getLogger(FunctionalTest.class);
	protected static String WEB_API_KEY = "WEB";
	
	protected static String  ADM1_AUTH_KEY = "aH1OTc8OfgA6WXrpRp2XPe0tJGBNzWd3Umvi2zzl3zI=";
	protected static String  TEA1_AUTH_KEY = "CiT7+T81LHsdmcMPkESdjJHhQKQe7ftBA3Wi/cGdRlY=";
	protected static String  STD10_AUTH_KEY = "m7DWYTq5T+K78VLTnbkhXHXuOBLsU/3whDwArrd0Fwo=";
	
	protected static String  INVALID_AUTH_KEY = "abc";
	
	 @BeforeClass
	    public static void setup() throws IOException {
		 
		 	logger.info("setup() START");
		 	RestAssured.baseURI = "https://localhost";
		 	RestAssured.basePath = "/laoschoolws/";
		 	RestAssured.port = 8443;
	        RestAssured.useRelaxedHTTPSValidation();// Avoid SSLPeerUnverifiedException
	        
//	        logger.info("baseURI:"+RestAssured.baseURI);
//	        logger.info("port:"+RestAssured.port);
//	        logger.info("basePath:"+RestAssured.basePath);
//	        logger.info("setup() END");
	        
		 

	    }
}
