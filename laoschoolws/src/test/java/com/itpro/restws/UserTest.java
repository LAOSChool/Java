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
import org.springframework.http.HttpStatus;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

// @Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest extends FunctionalTest {
	protected static final Logger logger = Logger.getLogger(UserTest.class);

	// @Ignore
	@Test
	public void getUsers_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).when().get(path).then().log()
				.ifValidationFails().assertThat().statusCode(200);

	}

	// @Ignore
	@Test
	public void getUsers_teacher_wrong_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY). // Teacher
				param("filter_class_id", "100"). // Class not assigned to
													// teacher 1
		when().get(path).then().log().ifValidationFails().assertThat().statusCode(400);
	}

	// @Ignore
	@Test
	public void getUsers_student_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";

		given().header("api_key", WEB_API_KEY).header("auth_key", STD10_AUTH_KEY). // STUDENT

		when().get(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.FORBIDDEN.value())
				.body("developerMessage", containsString("Access is denied"));
	}

	// @Ignore
	@Test
	public void getUsers_teacher_no_filter_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		// https://localhost:8443/laoschoolws//api/users?filter_class_id=2&filter_user_role=STUDENT&filter_sts=1&filter_from_id=14&from_row=0&max_result=1
		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY). // teacher1
																					// is
																					// assigned
																					// to
																					// class2
																					// already
		param("filter_class_id", "2").param("filter_user_role", "TEACHER").param("filter_sts", "2").when().get(path)
				.then().log().ifValidationFails().assertThat().statusCode(400);
	}

	@Ignore
	@Test
	public void getUsers_teacher_filter_class_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		// https://localhost:8443/laoschoolws//api/users?filter_class_id=2&filter_user_role=STUDENT&filter_sts=1&filter_from_id=14&from_row=0&max_result=1
		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY). // teacher1
																					// is
																					// assigned
																					// to
																					// class2
																					// already
		param("filter_class_id", "2").param("filter_user_role", "TEACHER").param("filter_sts", "2").when().get(path)
				.then().log().ifValidationFails().assertThat().statusCode(200);
	}

	// @Ignore
	@Test
	public void getUsers_from_id_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		int filter_from_id = 10;

		// https://localhost:8443/laoschoolws//api/users?filter_class_id=2&filter_user_role=STUDENT&filter_sts=1&filter_from_id=14&from_row=0&max_result=1
		Response response = given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). // teacher1
																										// is
																										// assigned
																										// to
																										// class2
																										// already
		param("filter_from_id", filter_from_id).

		when().get(path).then().contentType("application/json;charset=UTF-8").log().ifValidationFails().assertThat()
				.statusCode(200).extract().response();

		JsonPath jsonPath = response.getBody().jsonPath();
		int total_cnt = jsonPath.getInt("total_count");

		assert (total_cnt > 0);

		List<Integer> ids = jsonPath.get("list.id");
		for (Integer id : ids) {
			// logger.info("id:"+id);
			assert (id.intValue() > filter_from_id);
			// assertThat("id > 1",id.intValue() > 1);
		}
	}

	// @Ignore
	@Test
	public void getUsers_from_row_max_result_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users";
		int from_row = 1;
		int max_result = 2;
		// https://localhost:8443/laoschoolws//api/users?filter_class_id=2&filter_user_role=STUDENT&filter_sts=1&filter_from_id=14&from_row=0&max_result=1
		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). // teacher1
																					// is
																					// assigned
																					// to
																					// class2
																					// already
		param("from_row", from_row).param("max_result", max_result).

		when().get(path).then().log().ifValidationFails().assertThat().statusCode(200)
				.body("from_row", equalTo(from_row)).body("to_row", equalTo(from_row + max_result));
	}

	// @Ignore
	@Test
	public void getUser_id_exit_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id = 10;
		String path = "/api/users";

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).pathParam("id", id.intValue()). // STUDENT
																													// 10

		when().get(path + "/{id}").then().log().ifValidationFails().assertThat().statusCode(200).body("id",
				equalTo(id.intValue()));
	}

	// @Ignore
	@Test
	public void getUser_id_not_exit_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id = 9999999;// Not found ID
		String path = "/api/users";

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).pathParam("id", id.intValue()). // STUDENT
																													// 10

		when().get(path + "/{id}").then().log().ifValidationFails().assertThat().statusCode(404);
	}

	// @Ignore
	@Test
	public void getUser_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id = 42;// ID belong to school 2
		String path = "/api/users";

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).pathParam("id", id.intValue()). // STUDENT
																													// 10

		when().get(path + "/{id}").then().log().ifValidationFails().assertThat().statusCode(400);
	}

	@Ignore
	@Test
	public void getUser_id_not_same_class_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id = 10;
		String path = "/api/users";

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY). // teacher1
																					// &
																					// student10
																					// not
																					// same
																					// class
		pathParam("id", id.intValue()). // STUDENT 10

		when().get(path + "/{id}").then().log().ifValidationFails().assertThat().statusCode(400);
	}

	// @Ignore
	@Test
	public void getUser_id_student_cannot_access_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		Integer id = 10;
		String path = "/api/users";

		given().header("api_key", WEB_API_KEY).header("auth_key", STD10_AUTH_KEY). // student10
																					// cannot
																					// access
		pathParam("id", id.intValue()). // STUDENT 10

		when().get(path + "/{id}").then().log().ifValidationFails().assertThat()
				.statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));
	}

	// @Ignore
	@Test
	public void myprofile_student_normal() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");

		String path = "/api/users/myprofile";

		given().header("api_key", WEB_API_KEY).header("auth_key", STD10_AUTH_KEY). // student10
																					// cannot
																					// access

		when().get(path).then().log().ifValidationFails().assertThat().statusCode(200).body("id", equalTo(10));
	}

	// @Ignore
	@Test
	public void myprofile_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/myprofile";

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY). // student10
																					// cannot
																					// access

		when().get(path).then().log().ifValidationFails().assertThat().statusCode(200);
	}

	// @Ignore
	@Test
	public void myprofile_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/myprofile";

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY). // student10
																					// cannot
																					// access

		when().get(path).then().log().ifValidationFails().assertThat().statusCode(200);
	}

	/////////////////////////////////////
	///////////////////////////////////
	// @Ignore
	@Test
	public void create_user_teacher_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/create";

		Map<String, String> user = new HashMap<>();
		user.put("id", "1");
		user.put("role", "STUDENT");

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY)
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.FORBIDDEN.value())
				.body("developerMessage", containsString("Access is denied"));
	}

	// @Ignore
	@Test
	public void create_user_student_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/create";

		Map<String, String> user = new HashMap<>();
		user.put("id", "1");
		user.put("role", "STUDENT");

		given().header("api_key", WEB_API_KEY).header("auth_key", STD10_AUTH_KEY)
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.FORBIDDEN.value())
				.body("developerMessage", containsString("Access is denied"));
	}

	// @Ignore
	@Test
	public void create_user_admin_id_existing_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/create";

		Map<String, String> user = new HashMap<>();
		user.put("id", "1");
		user.put("role", "STUDENT");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY)
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(400);

	}

	// @Ignore
	@Test
	public void update_user_admin_normal_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";

		Map<String, String> user = new HashMap<>();
		user.put("id", "10");
		user.put("fullname", "Student xyz");// udate name

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY)
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(200);
	}

	// @Ignore
	@Test
	public void update_user_admin_id_null_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";

		Map<String, String> user = new HashMap<>();
		// user.put("id", "10");
		user.put("fullname", "Student xyz");// udate name

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY)
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(400);
	}

	// @Ignore
	@Test
	public void update_user_admin_id_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";

		Map<String, String> user = new HashMap<>();
		user.put("id", "39"); // belong to other school
		user.put("fullname", "admin 39");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY)
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(400);
	}

	// @Ignore
	@Test
	public void update_user_cannot_change_admin_state_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";

		Map<String, String> user = new HashMap<>();
		user.put("id", "2"); // ADMIN1
		user.put("state", "0");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY)
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(400);
	}

	// @Ignore
	@Test
	public void update_user_admin_invalid_state_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";

		Map<String, String> user = new HashMap<>();
		user.put("id", "10"); // STUDENT
		user.put("state", "100");// Invalid state

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY)
				.contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(400);
	}

	@Test
	public void update_user_teacher_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";

		Map<String, String> user = new HashMap<>();
		user.put("id", "10"); // STUDENT
		user.put("fullname", "student 10");// Invalid state

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY).// Teacher
																					// cannot
																					// update
																					// student
		contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.FORBIDDEN.value())
				.body("developerMessage", containsString("Access is denied"));
	}

	@Test
	public void update_user_student_false() {

		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/update";

		Map<String, String> user = new HashMap<>();
		user.put("id", "10"); // STUDENT
		user.put("fullname", "student 10");// Invalid state

		given().header("api_key", WEB_API_KEY).header("auth_key", STD10_AUTH_KEY).// Student
																					// cannot
																					// update
																					// student
		contentType("application/json;charset=UTF-8").body(user).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.FORBIDDEN.value())
				.body("developerMessage", containsString("Access is denied"));
	}

	@Test
	public void reset_pass_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "00000010";
		String path = "/api/users/reset_pass";

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).pathParam("sso", sso). // {sso_id}

		when().post(path + "/{sso}").then().log().ifValidationFails().assertThat().statusCode(200);

	}

	@Test
	public void reset_pass_sso_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "admin11123345778";
		String path = "/api/users/reset_pass";

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).pathParam("sso", sso). // {sso_id}

		when().post(path + "/{sso}").then().log().ifValidationFails().assertThat().statusCode(400)
				.body("developerMessage", containsString("sso_id is not found"));

	}

	@Test
	public void reset_pass_sso_other_school_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "s2_admin1";// admin school2
		String path = "/api/users/reset_pass";

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).pathParam("sso", sso). // {sso_id}

		when().post(path + "/{sso}").then().log().ifValidationFails().assertThat().statusCode(400)
				.body("developerMessage", containsString("is not in same school"));

	}

	@Test
	public void reset_pass_teacher_cannot_reset_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "s2_admin1";// admin school2
		String path = "/api/users/reset_pass";

		given().header("api_key", WEB_API_KEY).header("auth_key", TEA1_AUTH_KEY).// teacher
																					// cannot
																					// reset
																					// pass
		pathParam("sso", sso). // {sso_id}

		when().post(path + "/{sso}").then().log().ifValidationFails().assertThat()
				.statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));

	}

	@Test
	public void reset_pass_student_cannot_reset_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "s2_admin1";// admin school2
		String path = "/api/users/reset_pass";

		given().header("api_key", WEB_API_KEY).header("auth_key", STD10_AUTH_KEY).// student
																					// cannot
																					// reset
																					// pass
		pathParam("sso", sso). // {sso_id}

		when().post(path + "/{sso}").then().log().ifValidationFails().assertThat()
				.statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));

	}

	@Test
	public void reset_pass_cls_president_cannot_reset_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String sso = "s2_admin1";// admin school2
		String path = "/api/users/reset_pass";

		given().header("api_key", WEB_API_KEY).header("auth_key", CLS_PRESIDENT1_KEY).// CLS_PRESIDENT
				pathParam("sso", sso). // {sso_id}

		when().post(path + "/{sso}").then().log().ifValidationFails().assertThat()
				.statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));

	}
	
	@Ignore
	@Test
	public void change_pass_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/change_pass";

		Map<String, String> change_pass_info = new HashMap<>();
		change_pass_info.put("username", "admin1");
		change_pass_info.put("old_pass", "******");
		change_pass_info.put("new_pass", "******");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).
		contentType("application/json;charset=UTF-8").body(change_pass_info).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value())
				.body("developerMessage", containsString("Successful"));
	}

	

	@Test
	public void change_pass_admin_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/change_pass";

		Map<String, String> change_pass_info = new HashMap<>();
		change_pass_info.put("username", "admin1");
		change_pass_info.put("old_pass", "******");
		change_pass_info.put("new_pass", "******");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).
		contentType("application/json;charset=UTF-8").body(change_pass_info).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("Input current password is not correct"));
	}

	@Test
	public void change_pass_student_no_name_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/change_pass";

		Map<String, String> change_pass_info = new HashMap<>();
		// change_pass_info.put("username", "admin1");
		change_pass_info.put("old_pass", "******");
		change_pass_info.put("new_pass", "******");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).
		contentType("application/json;charset=UTF-8").body(change_pass_info).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("data.username is NULL or Blank"));

	}

	@Test
	public void change_pass_student_no_pass_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/change_pass";

		Map<String, String> change_pass_info = new HashMap<>();
		// change_pass_info.put("username", "admin1");
		change_pass_info.put("old_pass", "******");
		change_pass_info.put("new_pass", "******");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).
		contentType("application/json;charset=UTF-8").body(change_pass_info).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());

	}
	@Test
	public void change_pass_student_invalid_sso_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/change_pass";

		Map<String, String> change_pass_info = new HashMap<>();
		change_pass_info.put("username", "teacher1");
		change_pass_info.put("old_pass", "******");
		change_pass_info.put("new_pass", "******");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).
		contentType("application/json;charset=UTF-8").body(change_pass_info).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("username is not mapped with logined sso_id"));
	}
	@Test
	public void change_pass_student_invalid_old_pass_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/change_pass";

		Map<String, String> change_pass_info = new HashMap<>();
		change_pass_info.put("username", "admin1");
		change_pass_info.put("old_pass", "******");
		change_pass_info.put("new_pass", "******");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).
		contentType("application/json;charset=UTF-8").body(change_pass_info).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("Input current password is not correct"));
	}
	@Ignore
	@Test
	public void change_pass_student_invalid_new_pass_too_long_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/change_pass";

		Map<String, String> change_pass_info = new HashMap<>();
		change_pass_info.put("username", "admin1");
		change_pass_info.put("old_pass", "xxxx");
		change_pass_info.put("new_pass", "123456789012345678901");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).
		contentType("application/json;charset=UTF-8").body(change_pass_info).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("Input Password length is not correct - expected length should be >= 4 AND <= 20"));
	}
	@Ignore
	@Test
	public void change_pass_student_invalid_new_pass_too_short_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/api/users/change_pass";

		Map<String, String> change_pass_info = new HashMap<>();
		change_pass_info.put("username", "admin1");
		change_pass_info.put("old_pass", "xxxx");
		change_pass_info.put("new_pass", "123");

		given().header("api_key", WEB_API_KEY).header("auth_key", ADM1_AUTH_KEY).
		contentType("application/json;charset=UTF-8").body(change_pass_info).

		when().post(path).then().log().ifValidationFails().assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("Input Password length is not correct - expected length should be >= 4 AND <= 20"));
	}
	@Test
	public void forgot_pass_admin_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/forgot_pass?sso_id=admin1&phone=02099108969";

		// POST https://localhost:8443/laoschoolws/forgot_pass?sso_id=admin1&phone=02099108969
		given().header("api_key", WEB_API_KEY).
		// header("auth_key", ADM1_AUTH_KEY).
		when().post(path).
		then().
		log().ifValidationFails().
		assertThat().statusCode(HttpStatus.OK.value()).body("developerMessage", containsString("Plz wait, your request is in processing"));
	}
	@Test
	public void forgot_pass_teacher_true() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/forgot_pass?sso_id=teacher1&phone=02096604661";

		// POST https://localhost:8443/laoschoolws/forgot_pass?sso_id=admin1&phone=02099108969
		given().header("api_key", WEB_API_KEY).
		// header("auth_key", ADM1_AUTH_KEY).
		when().post(path).
		then().
		log().ifValidationFails().
		assertThat().statusCode(HttpStatus.OK.value()).body("developerMessage", containsString("Plz wait, your request is in processing"));
	}
	@Test
	public void forgot_pass_sso_not_exist_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/forgot_pass?sso_id=agcssssafafafaaafa&phone=02096604661";

		// POST https://localhost:8443/laoschoolws/forgot_pass?sso_id=admin1&phone=02099108969
		given().header("api_key", WEB_API_KEY).
		// header("auth_key", ADM1_AUTH_KEY).
		when().post(path).
		then().
		log().ifValidationFails().
		assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("not exising"));
	}
	@Ignore
	@Test
	public void forgot_pass_user_not_active_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/forgot_pass?sso_id=teacher1&phone=02096604661";

		// POST https://localhost:8443/laoschoolws/forgot_pass?sso_id=admin1&phone=02099108969
		given().header("api_key", WEB_API_KEY).
		// header("auth_key", ADM1_AUTH_KEY).
		when().post(path).
		then().
		log().ifValidationFails().
		assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("not exising"));
	}
	@Test
	public void forgot_pass_no_phone_false() {
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		logger.info(method_name + " START");
		String path = "/forgot_pass?sso_id=teacher1&phone=xxxxx";

		// POST https://localhost:8443/laoschoolws/forgot_pass?sso_id=admin1&phone=02099108969
		given().header("api_key", WEB_API_KEY).
		// header("auth_key", ADM1_AUTH_KEY).
		when().post(path).
		then().
		log().ifValidationFails().
		assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("developerMessage", containsString("not mapped with user's phone"));
	}
//	@Test
//	public void del_user_admin__true() {
//	}
//	@Test
//	public void del_user_teacher_false() {
//	}
//
//	@Test
//	public void del_user_cls_president_false() {
//	}
//	@Test
//	public void del_user_student_false() {
//	}
//	@Test
//	public void del_user_not_exist_false() {
//	}
//	@Test
//	public void del_user_not_active_false() {
//	}
//	@Test
//	public void del_user_other_school_false() {
//	}
//	@Test
//	public void assign2class_admin_true() {
//	}
//	@Test
//	public void assign2class_student_false() {
//	}
//	@Test
//	public void assign2class_already_assigned_false() {
//	}
//	@Test
//	public void assign2class_student_not_exist_false() {
//	}
//	@Test
//	public void assign2class_student_other_school_false() {
//	}
//	@Test
//	public void assign2class_class_not_exist_false() {
//	}
//	@Test
//	public void remove_frm_class_admin_true() {
//	}
//	@Test
//	public void remove_frm_class_student_false() {
//	}
//	@Test
//	public void remove_frm_class_not_assigned_yet_false() {
//	}
//	@Test
//	public void remove_frm_class_student_not_exist_false() {
//	}
//	@Test
//	public void remove_frm_class_student_other_school_false() {
//	}
//	@Test
//	public void remove_frm_class_class_not_exist_false() {
//	}
//	@Test
//	public void getSchoolYears_student_true() {
//	}
//	@Test
//	public void getSchoolYears_admin_true() {
//	}
//	@Test
//	public void getSchoolYears_cls_president_true() {
//	}
//	@Test
//	public void getSchoolYears_teacher_true() {
//	}
//	@Test
//	public void getSchoolYears_year_null_true() {
//	}
//	@Test
//	public void getAvailableUsers_admin_true() {
//	}
//	@Test
//	public void getAvailableUsers_student_false() {
//	}
//	@Test
//	public void upload_photo_admin_true() {
//	}
//	@Test
//	public void upload_photo_student_false() {
//	}
//	@Test
//	public void upload_photo_user_not_exist_false() {
//	}
//	@Test
//	public void upload_photo_user_other_school_false() {
//	}
//	@Test
//	public void download_cvs_admin_true() {
//	}
//	@Test
//	public void download_cvs_student_false() {
//	}
}
