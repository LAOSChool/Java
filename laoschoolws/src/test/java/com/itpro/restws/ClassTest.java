package com.itpro.restws;
import static com.jayway.restassured.RestAssured.given;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import com.itpro.restws.helper.Utils;

import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

//@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClassTest extends FunctionalTest {
	protected static final Logger logger = Logger.getLogger(ClassTest.class);
	
	@Test 
    public void getClasses_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes";
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
		
    } 
	@Test 
    public void getClasses_filter_paging_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes";
		//logger.info("       path:"+path);
		int from_row = 1;
		int max_result =2;
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			param("from_row",from_row).param("max_result",max_result).
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(200).body("from_row", equalTo(from_row)).body("to_row", equalTo(from_row+max_result));
    } 
	@Test 
    public void getClass_id_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes/1";
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
		
    } 
	@Test 
    public void getClass_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes/7"; // class of school 2
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY). // Admin of school 1
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
		
    } 
	@Test 
    public void getClass_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes/999999"; // class of school 2
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY). // Admin of school 1
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
		
    } 
	@Ignore
	@Test 
    public void createClass_no_head_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes/create";
		//logger.info("       path:"+path);
		
		Map<String,String> eclass = new HashMap<>();
		// eclass.put("id", "1");
		eclass.put("title", "Test Create Class");
		eclass.put("location", "Tang 1");
		eclass.put("level", "1");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(eclass).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
    } 
	@Ignore
	@Test 
    public void createClass_with_head_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes/create";
		//logger.info("       path:"+path);
		
		Map<String,String> eclass = new HashMap<>();
		// eclass.put("id", "1");
		eclass.put("title", "Test Create Class");
		eclass.put("location", "Tang 1");
		eclass.put("level", "1");
		eclass.put("head_teacher_id", "50");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(eclass).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
    } 
	@Ignore
	@Test 
    public void createClass_id_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes/create";
		//logger.info("       path:"+path);
		
		Map<String,String> eclass = new HashMap<>();
		eclass.put("id", "1");
		eclass.put("title", "Test Create Class");
		eclass.put("location", "Tang 1");
		eclass.put("level", "1");
		eclass.put("head_teacher_id", "50");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(eclass).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    } 
	
	@Test 
    public void updateClass_normal_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes/update";
		//logger.info("       path:"+path);
		
		Map<String,String> eclass = new HashMap<>();
		eclass.put("id", "1");
		eclass.put("title", "Class 1 test update : "+Utils.now());
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(eclass).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
    } 
	
	
	@Test 
    public void updateClass_teacher_role_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/classes/update";
		//logger.info("       path:"+path);
		
		Map<String,String> eclass = new HashMap<>();
		eclass.put("id", "1");
		eclass.put("title", "Test Create Class");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY). // teacher cannot create
			contentType("application/json;charset=UTF-8").
		body(eclass).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));
    } 
	
	@Test 
    public void updateClass_id_null_false() {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/classes/update";
		//logger.info("       path:"+path);
		
		Map<String,String> eclass = new HashMap<>();
		// eclass.put("id", "1");
		eclass.put("title", "Test Create Class");
		eclass.put("location", "Tang 1");
		eclass.put("level", "1");
		eclass.put("head_teacher_id", "50");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(eclass).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    } 
	
	@Test 
    public void updateClass_other_school_false() {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/classes/update";
		//logger.info("       path:"+path);
		
		Map<String,String> eclass = new HashMap<>();
		eclass.put("id", "15"); // other school of ADMIN1
		eclass.put("title", "Test Create Class");
		eclass.put("location", "Tang 1");
		eclass.put("level", "1");
		eclass.put("head_teacher_id", "50");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(eclass).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    } 
	@Ignore
	@Test 
    public void delClass_normal_true() {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/classes/delete/1";
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
    }
	@Ignore
	@Test 
    public void delClass_normal_and_user2class_true() {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/classes/delete/1";
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
    } 
	@Test 
    public void getUsers_admin_normal() {
		
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/classes/users";
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		param("class_id",1).
		param("filter_role","STUDENT").
		when().get(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
    }
  }
