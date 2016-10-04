package com.itpro.restws;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;



// @Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest extends FunctionalTest {
	protected static final Logger logger = Logger.getLogger(UserTest.class);
	//@Ignore
	@Test 
    public void getUsers_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
		
    } 
	
	//@Ignore
	@Test 
    public void getUsers_teacher_wrong_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY). 		// Teacher
			param("filter_class_id","100"). 			// Class not assigned to teacher 1
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    } 
	//@Ignore
	@Test 
    public void getUsers_student_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",STD10_AUTH_KEY). 		// STUDENT
			 			
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(500).body("developerMessage", containsString("Access is denied"));
    } 
	//@Ignore
	@Test 
	public void getUsers_teacher_no_filter_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		// https://localhost:8443/laoschoolws//api/users?filter_class_id=2&filter_user_role=STUDENT&filter_sts=1&filter_from_id=14&from_row=0&max_result=1
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY). // teacher1 is assigned to class2 already
			param("filter_class_id","2").
			param("filter_user_role","TEACHER").
			param("filter_sts","2").
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    } 
	@Ignore
	@Test 
	public void getUsers_teacher_filter_class_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		// https://localhost:8443/laoschoolws//api/users?filter_class_id=2&filter_user_role=STUDENT&filter_sts=1&filter_from_id=14&from_row=0&max_result=1
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY). // teacher1 is assigned to class2 already
			param("filter_class_id","2").
			param("filter_user_role","TEACHER").
			param("filter_sts","2").
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
    } 
	//@Ignore
	@Test 
	public void getUsers_from_id_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		int filter_from_id = 10;
		
		// https://localhost:8443/laoschoolws//api/users?filter_class_id=2&filter_user_role=STUDENT&filter_sts=1&filter_from_id=14&from_row=0&max_result=1
		Response response =  given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY). // teacher1 is assigned to class2 already
			param("filter_from_id",filter_from_id).
			
		when().
        	get(path).
        then().
        	contentType("application/json;charset=UTF-8").
        	log().ifValidationFails().
        	assertThat().statusCode(200)
        .extract().
            response(); 

		
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		
		assert(total_cnt > 0);
		
		List<Integer> ids = jsonPath.get("list.id");
		for (Integer id:ids){
			// logger.info("id:"+id);
			assert(id.intValue() > filter_from_id);
			// assertThat("id > 1",id.intValue() > 1);
		}
    } 	
	//@Ignore
	@Test 
	public void getUsers_from_row_max_result_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		int from_row = 1;
		int max_result =2;
		// https://localhost:8443/laoschoolws//api/users?filter_class_id=2&filter_user_role=STUDENT&filter_sts=1&filter_from_id=14&from_row=0&max_result=1
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY). // teacher1 is assigned to class2 already
			param("from_row",from_row).param("max_result",max_result).
			
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(200).body("from_row", equalTo(from_row)).body("to_row", equalTo(from_row+max_result));
    } 
	//@Ignore
	@Test 
    public void getUser_id_exit_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id =10;
		String path = "/api/users";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			pathParam("id", id.intValue()). // STUDENT 10
		
		when().get(path+"/{id}").then().
        	log().ifValidationFails().
        	assertThat().statusCode(200).body("id", equalTo(id.intValue()));
    } 
	//@Ignore
	@Test 
    public void getUser_id_not_exit_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id =9999999;// Not found ID
		String path = "/api/users";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			pathParam("id", id.intValue()). // STUDENT 10
		
		when().get(path+"/{id}").then().
        	log().ifValidationFails().
        	assertThat().statusCode(404);
    } 
	//@Ignore
	@Test 
    public void getUser_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id =42;// ID belong to school 2
		String path = "/api/users";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			pathParam("id", id.intValue()). // STUDENT 10
		
		when().get(path+"/{id}").then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    }
	@Ignore
	@Test 
    public void getUser_id_not_same_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id =10;
		String path = "/api/users";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY). // teacher1 &  student10 not same class
			pathParam("id", id.intValue()). // STUDENT 10
		
		when().get(path+"/{id}").then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    } 
	//@Ignore
	@Test 
    public void getUser_id_student_cannot_access_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id =10;
		String path = "/api/users";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",STD10_AUTH_KEY). // student10 cannot  access
			pathParam("id", id.intValue()). // STUDENT 10
		
		when().get(path+"/{id}").then().
        	log().ifValidationFails().
        	assertThat().statusCode(500).body("developerMessage", containsString("Access is denied"));
    } 
	//@Ignore
	@Test 
    public void myprofile_student_normal() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		
		
		
		String path = "/api/users/myprofile";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",STD10_AUTH_KEY). // student10 cannot  access
			
		
		when().get(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200).body("id", equalTo(10));
    } 
	//@Ignore
	@Test 
    public void myprofile_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/myprofile";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY). // student10 cannot  access
			
		
		when().get(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);	
    } 
	//@Ignore
	@Test 
    public void myprofile_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/myprofile";
		
		given().
		header("api_key",WEB_API_KEY).
		header("auth_key",TEA1_AUTH_KEY). // student10 cannot  access
		
	
		when().get(path).then().
    	log().ifValidationFails().
    	assertThat().statusCode(200);	
    }
	/////////////////////////////////////
///////////////////////////////////
	//@Ignore
	@Test 
    public void create_user_teacher_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/create";
		
		
		Map<String,String> user = new HashMap<>();
		user.put("id", "1");
		user.put("role", "STUDENT");
		
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(user).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(500).body("developerMessage", containsString("Access is denied"));
    } 
	//@Ignore
	@Test 
    public void create_user_student_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/create";
		
		
		Map<String,String> user = new HashMap<>();
		user.put("id", "1");
		user.put("role", "STUDENT");
		
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",STD10_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(user).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(500).body("developerMessage", containsString("Access is denied"));
    }
	//@Ignore
	@Test 
    public void create_user_admin_id_existing_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/create";
		
		
		Map<String,String> user = new HashMap<>();
		user.put("id", "1");
		user.put("role", "STUDENT");
		
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(user).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
		
    }
	//@Ignore
	@Test 
    public void update_user_admin_normal_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";
		
		
		Map<String,String> user = new HashMap<>();
		user.put("id", "10");
		user.put("fullname", "Student xyz");// udate name
		
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(user).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
    }
	//@Ignore
	@Test 
    public void update_user_admin_id_null_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";
		
		
		Map<String,String> user = new HashMap<>();
		//user.put("id", "10");
		user.put("fullname", "Student xyz");// udate name
		
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(user).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    }
	//@Ignore
	@Test 
    public void update_user_admin_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";
		
		
		Map<String,String> user = new HashMap<>();
		user.put("id", "39"); // belong to other school
		user.put("fullname", "admin 39");
		
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(user).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    } 
	//@Ignore
	@Test 
    public void update_user_cannot_change_admin_state_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";
		
		
		Map<String,String> user = new HashMap<>();
		user.put("id", "2"); // ADMIN1
		user.put("state", "0");
		
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(user).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    }
	//@Ignore
	@Test 
    public void update_user_admin_invalid_state_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";
		
		
		Map<String,String> user = new HashMap<>();
		user.put("id", "10"); // STUDENT
		user.put("state", "100");// Invalid state
		
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(user).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(400);
    } 	
	@Test
	 public void update_user_teacher_false() {
		 String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(method_name + " START");
			String path = "/api/users/update";
			
			
			Map<String,String> user = new HashMap<>();
			user.put("id", "10"); // STUDENT
			user.put("fullname", "student 10");// Invalid state
			
	        
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).// Teacher cannot update student
				contentType("application/json;charset=UTF-8").
			body(user).			
			
			when().post(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(500).body("developerMessage", containsString("Access is denied"));
	    } 	
	@Test
	 public void update_user_student_false() {
			
		 String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
			logger.info(method_name + " START");
			String path = "/api/users/update";
			
			
			Map<String,String> user = new HashMap<>();
			user.put("id", "10"); // STUDENT
			user.put("fullname", "student 10");// Invalid state
			
	        
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",STD10_AUTH_KEY).// Student cannot update student
				contentType("application/json;charset=UTF-8").
			body(user).			
			
			when().post(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(500).body("developerMessage", containsString("Access is denied"));
	    } 
	@Test
	 public void reset_pass_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "00000010";
		String path = "/api/users/reset_pass";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			pathParam("sso",sso). // {sso_id}
		
		when().post(path+"/{sso}").then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);	
		
	    } 
	@Test
	 public void reset_pass_sso_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "admin11123345778";
		String path = "/api/users/reset_pass";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			pathParam("sso",sso). // {sso_id}
		
		when().post(path+"/{sso}").then().
       	log().ifValidationFails().
       	assertThat().statusCode(400).body("developerMessage", containsString("sso_id is not found"));	
		
	    } 
	@Test
	 public void reset_pass_sso_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "s2_admin1";// admin school2
		String path = "/api/users/reset_pass";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			pathParam("sso",sso). // {sso_id}
		
		when().post(path+"/{sso}").then().
      	log().ifValidationFails().
      	assertThat().statusCode(400).body("developerMessage", containsString("is not in same school"));	
		
	    }
	
	@Test
	 public void reset_pass_teacher_cannot_reset_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "s2_admin1";// admin school2
		String path = "/api/users/reset_pass";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY).// teacher cannot reset pass
			pathParam("sso",sso). // {sso_id}
		
		when().post(path+"/{sso}").then().
      	log().ifValidationFails().
     	assertThat().statusCode(500).body("developerMessage", containsString("Access is denied"));
		
	    }
	@Test
	 public void reset_pass_student_cannot_reset_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "s2_admin1";// admin school2
		String path = "/api/users/reset_pass";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",STD10_AUTH_KEY).// student cannot reset pass
			pathParam("sso",sso). // {sso_id}
		
		when().post(path+"/{sso}").then().
      	log().ifValidationFails().
     	assertThat().statusCode(500).body("developerMessage", containsString("Access is denied"));
			
		    }
	@Test
	 public void reset_pass_cls_president_cannot_reset_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "s2_admin1";// admin school2
		String path = "/api/users/reset_pass";
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",CLS_PRESIDENT1_KEY).// CLS_PRESIDENT
			pathParam("sso",sso). // {sso_id}
		
		when().post(path+"/{sso}").then().
      	log().ifValidationFails().
     	assertThat().statusCode(500).body("developerMessage", containsString("Access is denied"));
			
		    }
  }
