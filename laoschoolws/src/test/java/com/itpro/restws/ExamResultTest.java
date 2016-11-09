package com.itpro.restws;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import com.itpro.restws.helper.Constant;

//@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExamResultTest extends FunctionalTest {
	protected static final Logger logger = Logger.getLogger(ExamResultTest.class);
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
		assertThat().statusCode(200);
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", TEA1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
		assertThat().statusCode(200);
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_student_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", STD10_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.FORBIDDEN.value()) // 403
			.body("developerMessage", containsString("Access is denied"));
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_cls_president_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", CLS_PRESIDENT1_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.FORBIDDEN.value()) // 403
			.body("developerMessage", containsString("Access is denied"));
	}
	
	
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_class_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=999999";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()) // 400
			.body("developerMessage", containsString("filter_class_id is not existing"));
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_class_id_not_belong_to_user_false() {
		// ClassID is not belong to teacher
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=2";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", TEA1_AUTH_KEY).// teacher1 not assigned to classid=2
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()) // 400
			.body("developerMessage", containsString("is not belong to the class id"));
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_class_id_other_school_false() {
		// ClassID invalid
		// 
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=15";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).// class_id=15 belong to other school with admin1
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()) // 400
			.body("developerMessage", containsString("me.school_id != filter_eclass.school_id"));
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_year_id_true() {
		// ClassID invalid
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1&filter_year_id=1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).// class_id=15 belong to other school with admin1
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.OK.value()) // 200
			.body("message", containsString("No error"));
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_year_id_not_exist_false() {
		// ClassID invalid
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1&filter_year_id=99";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).// class_id=15 belong to other school with admin1
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()); // 400
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_year_id_other_school_false() {
		// 
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1&filter_year_id=2";// year_id=2 of school 2

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()); // 400
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_year_id_zero() {
		// 
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1&filter_year_id=0";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
			.body("developerMessage", containsString("year_id will not accept 0"));
	}
	
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_student_id_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1&filter_student_id=10";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.OK.value());
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_student_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_student_id=9999999";

		given().
		header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("filter_student_id is not existing"));
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_student_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_student_id=41"; // not belong to school1

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("me.school_id != filter_student.school_id"));
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_subject_id_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1&filter_subject_id=1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.OK.value());
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_subject_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1&filter_subject_id=1000000";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("Invalid filter_subject_id"));
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_subject_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results?filter_class_id=1&filter_subject_id=20";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("filter_subject_id is not in same school"));
	}
	
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results/1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY).
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.OK.value());
	
	}
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results/1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", TEA1_AUTH_KEY). // teacher
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.OK.value()); 
	}
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_student_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results/1";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", STD10_AUTH_KEY). // teacher
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.FORBIDDEN.value()); 
	}
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results/9999999";

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY). // teacher
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("Cannot find ExamID")); 
	}
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/exam_results/3226";// other school

		given().
			header("api_key", WEB_API_KEY).
			header("auth_key", ADM1_AUTH_KEY). // teacher
			when().get(path).then().log().ifValidationFails().
			assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("not in same school with User"));
	}
	/***
	 * /api/exam_results/input
	 */
	@Test
	public void inputExamResult_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "1");
		user.put("student_id", "10");
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY)// admin
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
	}
	@Test
	public void inputExamResult_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "1");
		user.put("student_id", "10");
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY) // teacher 1 assigned to class1
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());

	}
	@Test
	public void inputExamResult_student_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "1");
		user.put("student_id", "10");
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", STD10_AUTH_KEY) // student 1 assigned to class1
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.FORBIDDEN.value());
	}
	@Test
	public void inputExamResult_president_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "1");
		user.put("student_id", "10");
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", CLS_PRESIDENT1_KEY) // loptruong1a
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.FORBIDDEN.value()); 
	}
	@Test
	public void inputExamResult_teacher_class_id_null_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		//user.put("class_id", "1");
		user.put("student_id", "10");
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY) // loptruong1a
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("exam.class_id cannot be NULL")); 
	}
	@Test
	public void inputExamResult_teacher_class_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "999999");
		user.put("student_id", "10");
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY) // loptruong1a
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("exam.class_id is not existing"));
	}
	@Test
	public void inputExamResult_teacher_class_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "7");// class_id=7 is chool_2
		user.put("student_id", "10");
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY) // loptruong1a
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("exam.school_id is not similar with current user.school_id"));
	}
	@Test
	public void inputExamResult_teacher_student_id_null_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "1");
		// user.put("student_id", "10");
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY) // loptruong1a
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("exam.student_id cannot be NUL")); 

	}
	@Test
	public void inputExamResult_teacher_student_id_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "1");
		user.put("student_id", "99999");
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY) // loptruong1a
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("exam.student_id is not existing")); 
	}
	@Test
	public void inputExamResult_teacher_student_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "1");
		user.put("student_id", "41");// student 41 is school 2
		user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY) // loptruong1a
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString(" not belong to school_id"));
	}
	@Test
	public void inputExamResult_teacher_subject_id_null_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/exam_results/input";

		Map<String, String> user = new HashMap<>();
		user.put("school_id", "1");
		user.put("class_id", "1");
		user.put("student_id", "10");
		//user.put("subject_id", "1");
		user.put("sch_year_id", "1");
		user.put("m1", "{\"sresult\":\"8\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");
		user.put("m2", "{\"sresult\":\"9\",\"notice\":\"TEST\",\"exam_dt\":\"2016-11-01 16:34:42\"}");

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY) // loptruong1a
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage",containsString("subject_id cannot be NULL")); 
	}
	@Test
	public void inputExamResult_teacher_subject_id_not_exist_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_subject_id_other_school_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_year_id_nul_true() { // => current year
		// 
	}
	@Test
	public void inputExamResult_teacher_year_id_not_exist_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_year_id_other_school_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_invalid_poit_limit_false() {
		// poit > 10, < 0
		// 
	}
	@Test
	public void inputExamResult_teacher_update_exam_true() {
		// id != null => update
		// 
	}
	@Test
	public void inputExamResult_teacher_update_not_exist_false() {
		// id != null => update
		// 
	}
	@Test
	public void inputExamResult_teacher_update_change_id_false() {
		// keep school,student,subject,year, only change ID
		// 
	}
	@Test
	public void getExamResultProfile_student_true() {
		// filter_year_id == null
		// Using current year
		// Create new if not exist
		// 
	}
	@Test
	public void getExamResultProfile_admin_false() {
		// keep school,student,subject,year, only change ID
		// 
	}
	@Test
	public void getExamResultProfile_filter_subject() {
		// keep school,student,subject,year, only change ID
		// 
	}
	@Test
	public void getExamResultProfile_filter_year() {
		// filter_year_id != null
		// Just query from DB and return
		// Do not create new if not exist
	}
	@Test
	public void inputExamResults_teacher_true() {
		
	}
	@Test
	public void inputExamResults_president_false() {
		
	}
	@Test
	public void inputExamResults_student_false() {
		
	}
	@Test
	public void inputExamResults_update_true() {
		
	}
	@Test
	public void inputExamResults_update_invalid_id_false() {
		
	}
	@Test
	public void inputExamResults_school_null_false() {
		
	}
	@Test
	public void inputExamResults_class_null_false() {
		
	}
	@Test
	public void inputExamResults_class_other_school_false() {
		
	}
	@Test
	public void inputExamResults_student_null_false() {
		
	}
	@Test
	public void inputExamResults_student_other_school_false() {
		
	}
	@Test
	public void inputExamResults_student_invalid_role_false() {
		
	}
	@Test
	public void inputExamResults_subject_null_false() {
		
	}
	@Test
	public void inputExamResults_subject_other_school_false() {
		
	}
	@Test
	public void inputExamResults_year_null_false() {
		
	}
	@Test
	public void inputExamResults_year_other_school_false() {
		
	}
	@Test
	public void inputExamResults_sresult_not_valid_false() {
		
	}
	@Test
	public void inputExamResults_poit_out_of_limit_false() {
		
	}
	@Test
	public void rankProcessAveAndOrder_one_class_true() {
		// Yeu cau da cocham diem day du
		 // Chi thuc hien rannking
		
	}
	@Test
	public void rankProcessAveAndOrder_many_class_true() {
		// Yeu cau da cocham diem day du
		 // Chi thuc hien rannking
		
	}
	@Test
	public void rankProcessAveAndOrder_lack_points_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void rankProcessAveAndOrder_invalid_ex_key_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void rankProcessAveAndOrder_class_other_school_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void rankProcessAveAndOrder_exam_result_not_inputted_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void getExamRanks_valid_class_id_true() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void getExamRanks_class_id_not_exist_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void getExamRanks_class_id_other_school_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void getExamRanks_student_id_not_exist_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void getExamRanks_student_id_other_school_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void getExamRanks_year_not_exist_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	@Test
	public void getExamRanks_year_other_school_false() {
		// Y
		 // Chi thuc hien rannking
		
	}
	
}
