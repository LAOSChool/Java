package com.itpro.restws;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import com.itpro.restws.helper.Utils;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

// @Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttendanceTest extends FunctionalTest {
	protected static final Logger logger = Logger.getLogger(AttendanceTest.class);
	
	@Test 
    public void getAttendances_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		
		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(200);
    } 

	@Test 
    public void getAttendances_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances?filter_class_id=1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", TEA1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(200);
		
    }
	@Test 
    public void getAttendances_cls_president_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances?filter_class_id=1";
		given().
		header("api_key", WEB_API_KEY).
		header("auth_key", CLS_PRESIDENT1_KEY).
		when().get(path).then().log().ifValidationFails().
		assertThat().statusCode(200);
    }
	@Test 
    public void getAttendances_student_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		
		given().
		header("api_key", WEB_API_KEY).
		header("auth_key", STD10_AUTH_KEY).
		when().get(path).then().log().ifValidationFails().
		
		assertThat().statusCode(HttpStatus.FORBIDDEN.value()) // 403
		.body("developerMessage", containsString("Access is denied"));
    }
	@Test 
    public void getAttendances_admin_filter_class_id_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		given().
		header("api_key", WEB_API_KEY).
		header("auth_key", ADM1_AUTH_KEY).
		param("filter_class_id",1).
		when().get(path).then().log().ifValidationFails().assertThat().statusCode(200);
    }
	
	
	@Test 
    public void getAttendances_admin_filter_user_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		
		String path = "/api/attendances";
		Integer filter_user_id = 10;
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_user_id", filter_user_id.intValue()).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt > 0);
//
		List<Integer> ids = jsonPath.get("list.student_id");
		if (ids != null && ids.size() > 0){
			for (Integer id : ids) {
				assert (filter_user_id.intValue() == id.intValue());
			}
		}
    }
	@Test 
    public void getAttendances_admin_filter_from_id_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_id = 10;
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_from_id", filter_id.intValue()).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt > 0);
//
		List<Integer> ids = jsonPath.get("list.id");
		if (ids != null && ids.size() > 0){
			for (Integer id : ids) {
				// logger.info("id:"+id);
				assert (filter_id.intValue() < id.intValue());
			}
		}
		
    }
	@Test 
    public void getAttendances_admin_filter_from_dt_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		String filter_from_dt_str = "23-08-2016";
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_from_dt", filter_from_dt_str).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt > 0);
//
		Date filter_from_dt= Utils.parsetDateAll(filter_from_dt_str);
		
		List<String> dates = jsonPath.get("list.att_dt");
		if (dates != null && dates.size() > 0){
			for (String dt_str : dates) {
				//logger.info("dt_str:"+dt_str);
				Date result_dt = Utils.parsetDateAll(dt_str);
				assert (filter_from_dt.equals(result_dt) || filter_from_dt.before(result_dt));
			}
		}
    }
	@Test 
    public void getAttendances_admin_filter_to_dt_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		String filter_to_dt_str = "23-08-2016";
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_to_dt",filter_to_dt_str).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt > 0);
//
		Date filter_to_dt= Utils.parsetDateAll("23-08-2016");
		
		List<String> dates = jsonPath.get("list.att_dt");
		if (dates != null && dates.size() > 0){
			for (String dt_str : dates) {
				//logger.info("dt_str:"+dt_str);
				Date result_dt = Utils.parsetDateAll(dt_str);
				assert (filter_to_dt.equals(result_dt) || filter_to_dt.after(result_dt));
			}
		}
    }
	@Test 
    public void getAttendances_admin_filter_year_id_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_year_id = 1;
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_year_id",filter_year_id.intValue()).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt > 0);

		
		logger.info("filter_year_id:"+filter_year_id.intValue());
		List<Integer> years = jsonPath.get("list.year_id");
		if (years != null && years.size() > 0){
			for (Integer year : years) {
				//logger.info("year:"+year.intValue());
				assert (year.intValue() == filter_year_id.intValue());
			}
		}
    }
	@Test 
    public void getAttendances_admin_filter_term_val_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_term_val = 2;
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_term_val", filter_term_val.intValue()).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt > 0);
//
		
		List<Integer> terms = jsonPath.get("list.term_val");
		if (terms != null && terms.size() > 0){
			for (Integer term : terms) {
				assert (term.intValue() == filter_term_val.intValue());
			}
		}
    }
	@Test 
    public void getAttendances_admin_filter_from_time_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_from_time = 1471910400;// 2016-08-23
		String filter_from_dt_str = "23-08-2016";
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_from_time", filter_from_time).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt > 0);
//
		Date filter_from_dt= Utils.parsetDateAll(filter_from_dt_str);
		
		List<String> dates = jsonPath.get("list.att_dt");
		if (dates != null && dates.size() > 0){
			for (String dt_str : dates) {
				//logger.info("dt_str:"+dt_str);
				Date result_dt = Utils.parsetDateAll(dt_str);
				assert (filter_from_dt.equals(result_dt) || filter_from_dt.before(result_dt));
			}
		}
		
		
		
    }
	@Test 
    public void getAttendances_admin_filter_to_time_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_to_time = 1471910400;// 2016-08-23
		String filter_to_dt_str = "23-08-2016";
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_to_time",filter_to_time).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt > 0);
//
		Date filter_to_dt= Utils.parsetDateAll(filter_to_dt_str);
		
		List<String> dates = jsonPath.get("list.att_dt");
		if (dates != null && dates.size() > 0){
			for (String dt_str : dates) {
				//logger.info("dt_str:"+dt_str);
				Date result_dt = Utils.parsetDateAll(dt_str);
				assert (filter_to_dt.equals(result_dt) || filter_to_dt.after(result_dt));
			}
		}
    }
	@Test 
    public void getAttendances_admin_filter_class_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_class_id=7;// school 2
		
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_class_id",filter_class_id).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt == 0);
		
    }
	@Test 
    public void getAttendances_admin_filter_user_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_user_id=42;// school 2
		
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_user_id",filter_user_id).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt == 0);
    }
	@Test 
    public void getAttendances_admin_filter_year_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_year_id=2; // school 2
		
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_year_id",filter_year_id).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt == 0);
    }
	@Test 
    public void getAttendances_taecher_class_not_belong_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_class_id=2;
		
		
		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY). 
				param("filter_class_id",filter_class_id).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().
				assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("developerMessage", containsString("is not belong to the class id"));
		
    }
	@Test 
    public void getAttendances_cls_president_not_belong_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_class_id=2;
		
		
		given().header("api_key", WEB_API_KEY).header("auth_key", CLS_PRESIDENT1_KEY). 
				param("filter_class_id",filter_class_id).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().
				assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("developerMessage", containsString("is not belong to the class id"));
    }
	@Test 
    public void getAttendances_teacher_class_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_class_id=200000;
		
		
		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY). 
				param("filter_class_id",filter_class_id).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().
				assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("developerMessage", containsString("is not belong to the class id"));
    }
	@Test 
    public void getAttendances_teacher_user_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_user_id=200000;
		Integer filter_class_id=1;
	
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_user_id",filter_user_id).
				param("filter_class_id",filter_class_id).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt == 0);
		
    }
	@Test 
    public void getAttendances_teacher_year_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_year_id=200000;
		Integer filter_class_id=1;
	
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_year_id",filter_year_id).
				param("filter_class_id",filter_class_id).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt == 0);
    }
	@Test 
    public void getAttendances_teacher_term_val_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		Integer filter_term_val=200000;
		Integer filter_class_id=1;
	
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_term_val",filter_term_val).
				param("filter_class_id",filter_class_id).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt == 0);
    }
	@Test 
    public void getAttendances_admin_from_dt_from_time_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances";
		String filter_from_dt_str = "23-08-2016";
		String filter_to_dt_str = "24-08-2016";
		
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				param("filter_from_dt", filter_from_dt_str).
				param("filter_to_dt", filter_to_dt_str).
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
//
		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");
		assert (total_cnt > 0);
//
		Date filter_from_dt= Utils.parsetDateAll(filter_from_dt_str);
		Date filter_to_dt= Utils.parsetDateAll(filter_to_dt_str);
		
		List<String> dates = jsonPath.get("list.att_dt");
		if (dates != null && dates.size() > 0){
			for (String dt_str : dates) {
				//logger.info("dt_str:"+dt_str);
				Date result_dt = Utils.parsetDateAll(dt_str);
				assert ((filter_from_dt.equals(result_dt) || filter_from_dt.before(result_dt)) && (filter_to_dt.equals(result_dt) || filter_to_dt.after(result_dt)) );
			}
		}
    }
	@Test 
    public void getMyprofile_student_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/myprofile";
		
		given().
		header("api_key", WEB_API_KEY).
		header("auth_key", STD10_AUTH_KEY).
		when().get(path).then().log().ifValidationFails().
		assertThat().statusCode(200);
    }
	@Test 
    public void getMyprofile_admin_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/myprofile";
		
		given().
		header("api_key", WEB_API_KEY).
		header("auth_key", ADM1_AUTH_KEY).
		when().get(path).then().log().ifValidationFails().
		assertThat().statusCode(HttpStatus.FORBIDDEN.value()) // 403
		.body("developerMessage", containsString("Access is denied"));
		
    }
	@Test 
    public void getMyprofile_teacher_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/myprofile";
		
		given().
		header("api_key", WEB_API_KEY).
		header("auth_key", TEA1_AUTH_KEY).
		when().get(path).then().log().ifValidationFails().
		assertThat().statusCode(HttpStatus.FORBIDDEN.value()) // 403
		.body("developerMessage", containsString("Access is denied"));
    }
	@Test 
    public void getMyprofile_cls_president_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/myprofile";
		
		given().
		header("api_key", WEB_API_KEY).
		header("auth_key", CLS_PRESIDENT1_KEY).
		when().get(path).then().log().ifValidationFails().
		assertThat().statusCode(HttpStatus.FORBIDDEN.value()) // 403
		.body("developerMessage", containsString("Access is denied"));
    }
	@Test 
    public void getAttendance_id_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/1";
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
		JsonPath jsonPath = response.getBody().jsonPath();
		int id = jsonPath.getInt("id");
		assert(id ==1);
    }
	@Test 
    public void getAttendance_id_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/1";
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY). 
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
		JsonPath jsonPath = response.getBody().jsonPath();
		int id = jsonPath.getInt("id");
		assert(id ==1);
		
    }
	@Test 
    public void getAttendance_id_cls_president_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/1";
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", CLS_PRESIDENT1_KEY). 
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
		JsonPath jsonPath = response.getBody().jsonPath();
		int id = jsonPath.getInt("id");
		assert(id ==1);
		
		
		
    }
	@Test 
    public void getAttendance_id_student_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/1"; // ID=1 belong to STUDENT 10
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", STD10_AUTH_KEY). 
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();
		JsonPath jsonPath = response.getBody().jsonPath();
		int id = jsonPath.getInt("id");
		assert(id ==1);
    }
	@Test 
    public void getAttendance_id_admin_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/100000"; // ID=1 belong to STUDENT 10
		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("developerMessage", containsString("Not found"));
    }
	@Test 
    public void getAttendance_id_admin_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/78"; // ID=78 NOT belong to school 1
		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). 
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("developerMessage", containsString("Current user and attendance_id are not in same School"));
		
		
    }
	@Test 
    public void getAttendance_id_teacher_other_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/2"; // ID=2 belong to class 2
		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY). 
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("developerMessage", containsString("not belong to current TEACHER"));
    }
	@Test 
    public void getAttendance_id_cls_president_other_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/2"; // ID=2 belong to class 2
		given().header("api_key", WEB_API_KEY).header("auth_key", CLS_PRESIDENT1_KEY). 
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("developerMessage", containsString("not belong to current CLS_PRESIDENT"));
    }
	@Test 
    public void getAttendance_id_student_other_user_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/2"; // ID=2 belong to class 2
		given().header("api_key", WEB_API_KEY).header("auth_key", STD10_AUTH_KEY). 
				when().get(path).then().
				contentType("application/json;charset=UTF-8").
				log().ifValidationFails().assertThat()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("developerMessage", containsString("not belong to current Student"));
    }
	@Ignore
	@Test 
    public void getAttendance_create_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/attendances/create";
		
		Map<String,String> att = new HashMap<>();
		att.put("school_id", "1");
		att.put("class_id", "1");		
		att.put("att_dt", "2016-12-01");
		att.put("session_id", "1");
		att.put("excused", "1");
		att.put("term_val", "2");
		att.put("year_id", "1");
		att.put("auditor", "3");//teacher 1 id
		att.put("student_id", "10");
        
		Response response = given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(att).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	extract().response();
		
		
		JsonPath jsonPath = response.getBody().jsonPath();
		
		

		String devMsg  = jsonPath.get("developerMessage");
		logger.info("devMsg:"+devMsg);
		logger.info("Success:"+devMsg.indexOf("Success"));
		logger.info("Similar:"+devMsg.indexOf("Similar attendance already existing"));
		assert((response.statusCode() == HttpStatus.OK.value())|(response.statusCode() == HttpStatus.BAD_REQUEST.value()));
		assert(devMsg!= null && (devMsg.indexOf("Similar attendance already existing")>=0) || (devMsg.indexOf("Success") >= 0));
		/////////////////=========================== CLS PRESIDENT
		response = given().
				header("api_key",WEB_API_KEY).
				header("auth_key",CLS_PRESIDENT1_KEY).
				contentType("application/json;charset=UTF-8").
			body(att).			
			
			when().post(path).then().
	        	log().ifValidationFails().
	        	extract().response();
			
			
			jsonPath = response.getBody().jsonPath();
			devMsg  = jsonPath.get("developerMessage");
			logger.info("devMsg:"+devMsg);
			logger.info("Success:"+devMsg.indexOf("Success"));
			logger.info("Similar:"+devMsg.indexOf("Similar attendance already existing"));
			assert((response.statusCode() == HttpStatus.OK.value())|(response.statusCode() == HttpStatus.BAD_REQUEST.value()));
			assert(devMsg!= null && (devMsg.indexOf("Similar attendance already existing")>=0) || (devMsg.indexOf("Success") >= 0));
    }
	@Test 
    public void getAttendance_create_admin_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/attendances/create";
		
		Map<String,String> att = new HashMap<>();
		att.put("school_id", "2");// 22222222222
		att.put("class_id", "1");		
		att.put("att_dt", "2016-12-01");
		att.put("session_id", "1");
		att.put("excused", "1");
		att.put("term_val", "2");
		att.put("year_id", "1");
		att.put("auditor", "3");//teacher 1 id
		att.put("student_id", "10");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(att).			
		
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.BAD_REQUEST.value())
		.body("developerMessage", containsString("Current user and attendance_id are not in same School"));
    }
	@Test 
    public void getAttendance_create_teacher_other_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/attendances/create";
		
		Map<String,String> att = new HashMap<>();
		att.put("school_id", "1");
		att.put("class_id", "2");		
		att.put("att_dt", "2016-12-01");
		att.put("session_id", "1");
		att.put("excused", "1");
		att.put("term_val", "2");
		att.put("year_id", "1");
		att.put("auditor", "3");//teacher 1 id
		att.put("student_id", "10");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(att).			
		
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.BAD_REQUEST.value())
		.body("developerMessage", containsString("Invalid Class_ID"));
		
    }
	@Test 
    public void getAttendance_create_student_not_authorize_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/attendances/create";
		
		Map<String,String> att = new HashMap<>();
		att.put("school_id", "1");
		att.put("class_id", "1");		
		att.put("att_dt", "2016-08-12");
		att.put("session_id", "1");
		att.put("excused", "1");
		att.put("term_val", "2");
		att.put("year_id", "1");
		att.put("auditor", "3");//teacher 1 id
		att.put("student_id", "10");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",STD10_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(att).			
		
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.FORBIDDEN.value())
		.body("developerMessage", containsString("Access is denied"));
    }
	@Test 
    public void getAttendance_update_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/attendances/update";
		
		Map<String,String> att = new HashMap<>();
		att.put("id", "1");
		att.put("school_id", "1");
		att.put("class_id", "1");		
		att.put("att_dt", "2016-08-12");
		att.put("session_id", "1");
		att.put("excused", "1");
		att.put("term_val", "2");
		att.put("year_id", "1");
		att.put("student_id", "10");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(att).			
		
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.OK.value());
	}
	@Test 
    public void getAttendance_update_admin_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/attendances/update";
		
		Map<String,String> att = new HashMap<>();
		att.put("id", "78");// school 2
		att.put("school_id", "1");
		att.put("class_id", "1");		
		att.put("att_dt", "2016-08-12");
		att.put("session_id", "1");
		att.put("excused", "1");
		att.put("term_val", "2");
		att.put("year_id", "1");
		att.put("student_id", "10");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",TEA1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(att).			
		
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("attendace.id is not belong to same school with me"));
		
    }
	@Test 
    public void getAttendance_update_student_false_not_authrize() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/attendances/update";
		
		Map<String,String> att = new HashMap<>();
		att.put("id", "78");// school 2
		att.put("school_id", "1");
		att.put("class_id", "1");		
		att.put("att_dt", "2016-08-12");
		att.put("session_id", "1");
		att.put("excused", "1");
		att.put("term_val", "2");
		att.put("year_id", "1");
		att.put("student_id", "10");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",STD10_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(att).			
		
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.FORBIDDEN.value());
		
    }
	@Ignore
	@Test 
    public void getAttendance_delete_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/delete/1";
		given().
		header("api_key",WEB_API_KEY).
		header("auth_key",ADM1_AUTH_KEY).
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.OK.value());
    }
	@Test 
    public void getAttendance_delete_admin_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/delete/78";// 78 is shchool 2
		given().
		header("api_key",WEB_API_KEY).
		header("auth_key",ADM1_AUTH_KEY).
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.BAD_REQUEST.value())
		.body("developerMessage", containsString("Current user and attendance_id are not in same School"));
    }
	@Test 
    public void getAttendance_delete_teacher_other_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/delete/2";// 2 is class 2
		given().
		header("api_key",WEB_API_KEY).
		header("auth_key",TEA1_AUTH_KEY).
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.BAD_REQUEST.value())
		.body("developerMessage", containsString("Invalid Class_ID"));
    }
	@Test 
    public void getAttendance_delete_president_other_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/delete/2";// 2 is class 2
		given().
		header("api_key",WEB_API_KEY).
		header("auth_key",CLS_PRESIDENT1_KEY).
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.BAD_REQUEST.value())
		.body("developerMessage", containsString("Invalid Class_ID"));
    }
	@Test 
    public void getAttendance_request_admin_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/attendances/request";
		
		given().
		header("api_key",WEB_API_KEY).
		header("auth_key",ADM1_AUTH_KEY).
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.FORBIDDEN.value());
		
		given().
		header("api_key",WEB_API_KEY).
		header("auth_key",TEA1_AUTH_KEY).
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.FORBIDDEN.value());
		
		given().
		header("api_key",WEB_API_KEY).
		header("auth_key",CLS_PRESIDENT1_KEY).
		when().post(path).then().
		log().ifValidationFails().assertThat()
		.statusCode(HttpStatus.FORBIDDEN.value());
    }
//	@Ignore
//	@Test 
//    public void getAttendance_request_student_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//
//		String path = "/api/attendances/request";
//		/***
//		 * {"student_name":"Student 2",
//		 * "notice":"Nghi hai ngay 1,2/12/2016",
//		 * "school_id":10,
//		 * "excused":0,
//		 * "state":1,
//		 * "student_id":130,
//		 * "class_id":28,
//		 * "chk_user_id":0,
//		 * "subject_id":0,
//		 * "absent":0}
//		 */
//		Map<String,String> att = new HashMap<>();
//		att.put("school_id", "1");
//		att.put("class_id", "1");		
//		att.put("att_dt", "2016-12-02");
//        
//		given().
//			header("api_key",WEB_API_KEY).
//			header("auth_key",STD10_AUTH_KEY).
//			contentType("application/json;charset=UTF-8").
//		body(att).			
//		
//		when().post(path).then().
//		log().ifValidationFails().assertThat()
//		.statusCode(HttpStatus.FORBIDDEN.value());
//    }
//	@Test 
//    public void getAttendance_request_student_from_to_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/request";
//		
//		
//    }
//	@Test 
//    public void getAttendance_request_student_already_request_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/request";
//    }
//	@Test 
//    public void getAttendance_request_student_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/request";
//    }
//	@Test 
//    public void getAttendance_request_student_invalid_from_dto_dt_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/request";
//    }
//	@Test 
//    public void getAttendance_rollup_admin_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
//	@Test 
//    public void getAttendance_rollup_teacher_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
//	@Test 
//    public void getAttendance_rollup_president_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
//	@Test 
//    public void getAttendance_rollup_student_false_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
//	@Test 
//    public void getAttendance_rollup_president_class_id_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
//	@Test 
//    public void getAttendance_rollup_president_class_id_not_exit_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
//	@Test 
//    public void getAttendance_rollup_president_date_in_the_past_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
//	@Test 
//    public void getAttendance_rollup_president_date_in_future_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
//	@Test 
//    public void getAttendance_rollup_president_session_id_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
//	@Test 
//    public void getAttendance_rollup_president_session_id_not_exist_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/rollup";
//    }
}
