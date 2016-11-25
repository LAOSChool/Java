package com.itpro.restws;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

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
//	@Test 
//    public void getAttendances_admin_filter_class_id_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_user_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_from_id_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_from_dt_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_to_dt_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_year_id_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_term_val_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_from_time_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_to_time_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_class_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_user_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_filter_year_id_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_taecher_class_not_belong_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_cls_president_not_belong_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	
//	@Test 
//    public void getAttendances_teacher_class_id_not_exist_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_teacher_user_id_not_exist_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_teacher_year_id_not_exist_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_teacher_term_val_not_exist_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_from_dt_from_time_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getAttendances_admin_to_dt_to_time_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances";
//    }
//	@Test 
//    public void getMyprofile_student_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/myprofile";
//    }
//	@Test 
//    public void getMyprofile_admin_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/myprofile";
//    }
//	@Test 
//    public void getMyprofile_teacher_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/myprofile";
//    }
//	@Test 
//    public void getMyprofile_cls_president_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/myprofile";
//    }
//	@Test 
//    public void getAttendance_id_admin_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/1";
//    }
//	@Test 
//    public void getAttendance_id_teacher_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/1";
//    }
//	@Test 
//    public void getAttendance_id_cls_president_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/1";
//    }
//	@Test 
//    public void getAttendance_id_student_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/1";
//    }
//	
//	@Test 
//    public void getAttendance_id_admin_id_not_exist_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/1";
//    }
//	@Test 
//    public void getAttendance_id_admin_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/1";
//    }
//	@Test 
//    public void getAttendance_id_teacher_other_class_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/1";
//    }
//	@Test 
//    public void getAttendance_id_cls_president_other_class_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/1";
//    }
//	@Test 
//    public void getAttendance_id_student_other_user_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/1";
//    }
//	
//	@Test 
//    public void getAttendance_create_admin_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/create";
//    }
//	@Test 
//    public void getAttendance_create_admin_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/create";
//    }
//	@Test 
//    public void getAttendance_create_teacher_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/create";
//    }
//	@Test 
//    public void getAttendance_create_teacher_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/create";
//    }
//	@Test 
//    public void getAttendance_create_teacher_other_class_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/create";
//    }
//	@Test 
//    public void getAttendance_create_cls_president_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/create";
//    }
//	@Test 
//    public void getAttendance_create_cls_president_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/create";
//    }
//	@Test 
//    public void getAttendance_create_cls_president_other_class_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/create";
//    }
//	@Test 
//    public void getAttendance_create_student_not_authorize_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/create";
//    }
//	
//	@Test 
//    public void getAttendance_update_admin_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/update";
//    }
//	@Test 
//    public void getAttendance_update_admin_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/update";
//    }
//	@Test 
//    public void getAttendance_update_teacher_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/update";
//    }
//	
//	@Test 
//    public void getAttendance_update_teacher_other_class_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/update";
//    }
//	@Test 
//    public void getAttendance_update_cls_president_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/update";
//    }
//	@Test 
//    public void getAttendance_update_cls_president_other_class_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/update";
//    }
//	@Test 
//    public void getAttendance_update_student_false_not_authrize() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/update";
//    }
//	@Test 
//    public void getAttendance_delete_admin_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/delete/1";
//    }
//	@Test 
//    public void getAttendance_delete_admin_other_school_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/delete/1";
//    }
//	@Test 
//    public void getAttendance_delete_teacher_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/delete/1";
//    }
//	@Test 
//    public void getAttendance_delete_teacher_other_class_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/delete/1";
//    }
//	@Test 
//    public void getAttendance_delete_president_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/delete/1";
//    }
//	@Test 
//    public void getAttendance_delete_president_other_class_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/delete/1";
//    }
//	@Test 
//    public void getAttendance_request_admin_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/request";
//    }
//	@Test 
//    public void getAttendance_request_teacher_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/request";
//    }
//	@Test 
//    public void getAttendance_request_president_false() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/request";
//    }
//	@Test 
//    public void getAttendance_request_student_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/request";
//    }
//	@Test 
//    public void getAttendance_request_student_from_to_true() {
//		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
//		logger.info(method_name + " START");
//		String path = "/api/attendances/request";
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
