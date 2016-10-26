package com.itpro.restws;

import static com.jayway.restassured.RestAssured.given;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_student_false() {
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_class_id_true() {
		// ClassID valid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_class_id_not_exist_false() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_class_id_not_belong_to_user_false() {
		// ClassID is not belong to teacher
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_class_id_other_school_false() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_year_id_true() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_year_id_not_exist_false() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_year_id_other_school_false() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_student_id_true() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_student_id_not_exist_false() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_student_id_other_school_false() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_subject_id_true() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_subject_id_not_exist_false() {
		// ClassID invalid
	}
	/***
	 * /api/exam_results
	 */
	@Test
	public void getExamResults_filter_subject_id_other_school_false() {
		// ClassID invalid
	}
	
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_admin_true() {
		// 
	}
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_teacher_true() {
		// 
	}
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_student_false() {
		// 
	}
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_id_not_exist_false() {
		// 
	}
	/***
	 * "/api/exam_results/{id}"
	 */
	@Test
	public void getOneExamResult_id_other_school_false() {
		// 
	}
	/***
	 * /api/exam_results/input
	 */
	@Test
	public void inputExamResult_admin_true() {
		// 
	}
	@Test
	public void inputExamResult_teacher_true() {
		// 
	}
	@Test
	public void inputExamResult_student_false() {
		// 
	}
	@Test
	public void inputExamResult_president_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_class_id_null_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_class_id_not_exist_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_class_id_other_school_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_student_id_null_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_student_id_not_exist_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_student_id_other_school_false() {
		// 
	}
	@Test
	public void inputExamResult_teacher_subject_id_null_false() {
		// 
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
