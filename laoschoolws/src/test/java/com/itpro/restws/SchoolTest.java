package com.itpro.restws;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import com.itpro.restws.helper.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Ignore
public class SchoolTest extends FunctionalTest {
	protected static final Logger logger = Logger.getLogger(SchoolTest.class);
	
    public static String jsonAsString;
    
    /***
	 * https://localhost:8443/laoschoolws/api/schools
	 */		
    
	@Test 
    public void getSchools_admin_case_true() {
		logger.info("getSchools_admin_case_true() START");

		String path = "/api/schools";
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(200).
        	body(containsString("No error"));

		
    } 
	/***
	 * auth_key: abc
	 * https://localhost:8443/laoschoolws/api/schools
	 */		
	@Test 
    public void getSchools_invalid_auth_key_false() {
		logger.info("getSchools_invalid_auth_key_false() START");

		String path = "/api/schools";
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",INVALID_AUTH_KEY).
		when().
        	get(path).
        then().
        	log().ifValidationFails().
        	assertThat().statusCode(409);

		
    } 
	//@Ignore
	/***
	 * https://localhost:8443/laoschoolws/non-secure/schools/1
	 */		
	@Test 
    public void non_secure_school_id_exist_true() {
		logger.info("non_secure_school_id_exist_true() START");
		String path = "/non-secure/schools/1";
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
		when().
			get(path).
		then().
			log().ifError().
			assertThat().statusCode(200);
        
        
    } 
	/***
	 * https://localhost:8443/laoschoolws/non-secure/schools/999
	 */	
	//@Ignore
	@Test 
    public void non_secure_school_id_not_exist_false() {
		logger.info("non_secure_school_id_not_exist_false() START");
		String path = "/non-secure/schools/9999";
		//logger.info("       path:"+path);
		
		given().
			header("api_key",WEB_API_KEY).
		when().
			get(path).
		then().
			log().ifError().
			assertThat().statusCode(200);
    } 
	/***
	 * role: ADMIN
	 * school: 1
	 * {"id":1,"title":"School 1","description": "School 1 - description"}
	 * https://localhost:8443/laoschoolws/api/schools/update
	 */
  
	@Test 
    public void updateSchools_admin_true() {
		logger.info("updateSchools_admin_true() START");

		String path = "/api/schools/update";
		//logger.info("       path:"+path);
		
		Map<String,String> school = new HashMap<>();
		
		school.put("id", "1");
		school.put("title", "School 1");
		school.put("description", "School 1 - description");
        
		given().
			header("api_key",WEB_API_KEY).
			header("auth_key",ADM1_AUTH_KEY).
			contentType("application/json;charset=UTF-8").
		body(school).			
		
		when().post(path).then().
        	log().ifValidationFails().
        	assertThat().statusCode(200);
    } 
	/***
	 * role: ADMIN  school: 1
	 * {"id":2,"title":"School 2","description": "School 2 - description"}
	 * https://localhost:8443/laoschoolws/api/schools/update
	 */
	 // @Ignore
		@Test 
	    public void updateSchools_wrong_school_id_false() {
			logger.info("updateSchools_wrong_school_id_false() START");

			String path = "/api/schools/update";
			//logger.info("       path:"+path);
			
			Map<String,String> school = new HashMap<>();
			
			school.put("id", "2");
			school.put("title", "School 2");
			school.put("description", "School 2 - description");
	        
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",ADM1_AUTH_KEY).
				contentType("application/json;charset=UTF-8").
			body(school).			
			
			when().post(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(400).body("developerMessage", containsString("school.id is not belong to current user"));
	    } 
		
		/***
		 * role: TEACHER
		 * {"id":1,"title":"School 1","description": "School 1 - description"}
		 * https://localhost:8443/laoschoolws/api/schools/update
		 */	
		 // @Ignore
		@Test 
	    public void updateSchools_invalid_user_role_false() {
			logger.info("updateSchools_invalid_user_role_false() START");

			String path = "/api/schools/update";
			//logger.info("       path:"+path);
			
			Map<String,String> school = new HashMap<>();
			
			school.put("id", "1");
			school.put("title", "School 1");
			school.put("description", "School 1 - description");
	        
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				contentType("application/json;charset=UTF-8").
			body(school).			
			
			when().post(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));
	    } 
		/***
		 * https://localhost:8443/laoschoolws/api/schools/exams
		 */		
		@Test 
	    public void getSchoolExams_teacher_true() {
			logger.info("getSchoolExams_teacher_true() START");

			String path = "/api/schools/exams";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				
			
			when().get(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(200).body("message", containsString("No error"));
	    } 
		/***
		 * https://localhost:8443/laoschoolws/api/schools/exams/1
		 */				
		@Test 
	    public void getSchoolExams_valid_id_true() {
			logger.info("getSchoolExams_valid_id_true() START");

			String path = "/api/schools/exams";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				pathParam("id", 1).
			
			when().get(path+"/{id}").then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(200).body("message", containsString("No error"));
	    } 
		/***
		 * https://localhost:8443/laoschoolws/api/schools/exams/99999999
		 */		
		@Test 
	    public void getSchoolExams_id_not_exsit_false() {
			logger.info("getSchoolExams_id_not_exsit_false() START");

			String path = "/api/schools/exams";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				pathParam("id", 99999999).
			
			when().get(path+"/{id}").then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(400).body("message", containsString("Bad Request"));
	    } 
		/***
		 * https://localhost:8443/laoschoolws/api/schools/exams/20
		 */
		@Test 
	    public void getSchoolExams_other_school_false() {
			logger.info("getSchoolExams_other_school_false() START");

			String path = "/api/schools/exams";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				pathParam("id", 20). // exam ID of other school that user not belong to
			
			when().get(path+"/{id}").then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(400).body("message", containsString("Bad Request"));
	    } 
		
		/***
		 * https://localhost:8443/laoschoolws/api/schools/years/1
		 */
		@Test 
	    public void getSchoolYear_teacher_true() {
			logger.info("getSchoolYear_teacher_true() START");

			String path = "/api/schools/years";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				pathParam("id", 1).
			
			when().get(path+"/{id}").then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(200).body("message", containsString("No error"));
	    } 
		/***
		 * https://localhost:8443/laoschoolws/api/schools/years/9999
		 */
		@Test 
	    public void getSchoolYear_id_not_existing_false() {
			logger.info("getSchoolYear_id_not_existing_false() START");

			String path = "/api/schools/years";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				pathParam("id", 9999).
			
			when().get(path+"/{id}").then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(400).body("message", containsString("Bad Request"));
	    } 
		/***
		 * school:1
		 * https://localhost:8443/laoschoolws/api/schools/years/3
		 */
		@Test 
	    public void getSchoolYear_other_school_false() {
			logger.info("getSchoolYear_other_school_false() START");

			String path = "/api/schools/years";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				pathParam("id", 3).
			
			when().get(path+"/{id}").then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(400).body("message", containsString("Bad Request"));
	    } 
		/***
		 * school:1
		 * https://localhost:8443/laoschoolws/api/schools/years/3
		 */
		@Test 
	    public void getSchoolYear_all_years_true() {
			logger.info("getSchoolYear_all_years_true() START");

			String path = "/api/schools/years";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				
			
			when().get(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(200).body("message", containsString("No error"));
	    } 
		/***
		 * school:1
		 * https://localhost:8443/laoschoolws/api/schools/terms
		 */
		@Test 
	    public void getCurrentTerm_normal_true() {
			logger.info("getCurrentTerm_normal_true() START");

			String path = "/api/schools/terms";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				
			
			when().get(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(200).body("message", containsString("No error"));
	    } 
		/***
		 * school:1
		 * https://localhost:8443/laoschoolws/api/schools/terms?filter_year_id=1
		 */
		@Test 
	    public void getCurrentTerm_filter_year_true() {
			logger.info("getCurrentTerm_filter_year_true() START");

			String path = "/api/schools/terms";
		//	logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				param("filter_year_id", "1").
			
			when().get(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(200).body("message", containsString("No error"));
	    } 
		/***
		 * school:1
		 * https://localhost:8443/laoschoolws/api/schools/terms?filter_year_id=9999
		 */
		@Test 
	    public void getCurrentTerm_invalid_filter_year_false() {
			logger.info("getCurrentTerm_invalid_filter_year_false() START");

			String path = "/api/schools/terms";
		//	logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				param("filter_year_id", "9999").
			
			when().get(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(400).body("message", containsString("Bad Request"));
	    } 
		/***
		 * school:1
		 * https://localhost:8443/laoschoolws/api/schools/terms?filter_year_id=3
		 */
		@Test 
	    public void getCurrentTerm_filter_year_other_school_false() {
			logger.info("getCurrentTerm_filter_year_other_school_false() START");

			String path = "/api/schools/terms";
		//	logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				param("filter_year_id", "3").
			
			when().get(path).then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(400).body("message", containsString("Bad Request"));
	    } 
		@Ignore
		@Test 
		 public void createSchoolExam_admin_true() {
			 logger.info("createSchoolExam_admin_true() START");

				String path = "/api/schools/exams/create";
			//	logger.info("       path:"+path);
				
				Map<String,String> exam = new HashMap<>();
				
				//exam.put("id", "1");
				exam.put("school_id", "1");
				exam.put("term_val", "1");
				exam.put("ex_month", "9");
				exam.put("ex_type", "1");// normal exam
				exam.put("ex_name", "Sep");
				exam.put("cls_levels", "--ALL--");
				exam.put("ex_displayname", "Sep");
				exam.put("ex_key", "m1");
				exam.put("min", "1");
				exam.put("max", "10");

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(exam).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(200);
				
		} 
		
		@Test 
		 public void createSchoolExam_teacher_false() {
			 logger.info("createSchoolExam_teacher_false() START");

				String path = "/api/schools/exams/create";
			//	logger.info("       path:"+path);
				
				Map<String,String> exam = new HashMap<>();
				
				//exam.put("id", "1");
				exam.put("school_id", "1");
				exam.put("term_val", "1");
				exam.put("ex_month", "9");
				exam.put("ex_type", "1");// normal exam
				exam.put("ex_name", "Sep");
				exam.put("cls_levels", "--ALL--");
				exam.put("ex_displayname", "Sep");
				exam.put("ex_key", "m1");
				exam.put("min", "1");
				exam.put("max", "10");

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",TEA1_AUTH_KEY). // teacher cannot create
					contentType("application/json;charset=UTF-8").
				body(exam).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));
				
		} 
		@Test 
		 public void updateSchoolExam_normal_true() {
			 logger.info("updateSchoolExam_normal_true() START");

				String path = "/api/schools/exams/update";
				//logger.info("       path:"+path);
				
				Map<String,String> exam = new HashMap<>();
				
				exam.put("id", "1");
				exam.put("school_id", "1");
				exam.put("term_val", "1");
				exam.put("ex_month", "9");
				exam.put("ex_type", "1");// normal exam
				exam.put("ex_name", "Sep");
				exam.put("cls_levels", "--ALL--");
				exam.put("ex_displayname", "Sep");
				exam.put("ex_key", "m1");
				exam.put("min", "1");
				exam.put("max", "10");

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(exam).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(200);
				
		} 
		@Test 
		 public void updateSchoolExam_teacher_role_false() {
			 logger.info("updateSchoolExam_teacher_role_false() START");

				String path = "/api/schools/exams/update";
				//logger.info("       path:"+path);
				
				Map<String,String> exam = new HashMap<>();
				
				exam.put("id", "1");
				exam.put("school_id", "1");
				exam.put("term_val", "1");
				exam.put("ex_month", "9");
				exam.put("ex_type", "1");// normal exam
				exam.put("ex_name", "Sep");
				exam.put("cls_levels", "--ALL--");
				exam.put("ex_displayname", "Sep");
				exam.put("ex_key", "m1");
				exam.put("min", "1");
				exam.put("max", "10");

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",TEA1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(exam).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));
				
		} 
		@Test 
		 public void updateSchoolExam_other_school_false() {
			 logger.info("updateSchoolExam_other_school_false() START");

				String path = "/api/schools/exams/update";
			//	logger.info("       path:"+path);
				
				Map<String,String> exam = new HashMap<>();
				
				exam.put("id", "21");
				exam.put("school_id", "1");
				exam.put("term_val", "1");
				exam.put("ex_month", "9");
				exam.put("ex_type", "1");// normal exam
				exam.put("ex_name", "Sep");
				exam.put("cls_levels", "--ALL--");
				exam.put("ex_displayname", "Sep");
				exam.put("ex_key", "m1");
				exam.put("min", "1");
				exam.put("max", "10");

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(exam).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void updateSchoolExam_invalid_ex_key_false() {
			 logger.info("updateSchoolExam_invalid_ex_key_false() START");

				String path = "/api/schools/exams/update";
			//	logger.info("       path:"+path);
				
				Map<String,String> exam = new HashMap<>();
				
				exam.put("id", "1");
				exam.put("school_id", "1");
				exam.put("term_val", "1");
				exam.put("ex_month", "9");
				exam.put("ex_type", "1");// normal exam
				exam.put("ex_name", "Sep");
				exam.put("cls_levels", "--ALL--");
				exam.put("ex_displayname", "Sep");
				exam.put("ex_key", "m111");
				exam.put("min", "1");
				exam.put("max", "10");

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(exam).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void updateSchoolExam_invalid_ex_type_false() {
			 logger.info("updateSchoolExam_invalid_ex_type_false() START");

				String path = "/api/schools/exams/update";
			//	logger.info("       path:"+path);
				
				Map<String,String> exam = new HashMap<>();
				
				exam.put("id", "1");
				exam.put("school_id", "1");
				exam.put("term_val", "1");
				exam.put("ex_month", "9");
				exam.put("ex_type", "11111");// normal exam
				exam.put("ex_name", "Sep");
				exam.put("cls_levels", "--ALL--");
				exam.put("ex_displayname", "Sep");
				exam.put("ex_key", "m1");
				exam.put("min", "1");
				exam.put("max", "10");

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(exam).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Ignore
		@Test 
		 public void createSchoolYear_normal_true() {
			 logger.info("createSchoolYear_normal_true() START");

				String path = "/api/schools/years/create";
				//logger.info("       path:"+path);
				
				Map<String,String> year = new HashMap<>();
				
				//year.put("id", "1");
				year.put("school_id", "1");
				year.put("years", "2017-2018");
				year.put("from_year", "2017");// normal exam
				year.put("to_year", "2018");
				year.put("start_dt", "2017-09-05");
				year.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(year).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(200);
				
		} 
		@Test 
		 public void createSchoolYear_id_exist_false() {
			 logger.info("createSchoolYear_id_exist_false() START");

				String path = "/api/schools/years/create";
			//	logger.info("       path:"+path);
				
				Map<String,String> year = new HashMap<>();
				
				year.put("id", "1");// ERROR => Cannot create if id != null
				year.put("school_id", "1");
				year.put("years", "2017-2018");
				year.put("from_year", "2017");// normal exam
				year.put("to_year", "2018");
				year.put("start_dt", "2017-09-05");
				year.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(year).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void createSchoolYear_frm_year_to_year_exist_false() {
			 logger.info("createSchoolYear_frm_year_to_year_exist_false() START");

				String path = "/api/schools/years/create";
			//	logger.info("       path:"+path);
				
				Map<String,String> year = new HashMap<>();
				
				
				year.put("school_id", "1");
				year.put("years", "2016-2017");
				year.put("from_year", "2016");// already exist in other id
				year.put("to_year", "2017");
				year.put("start_dt", "2016-09-05");
				year.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(year).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void createSchoolYear_teacher_role_false() {
			 logger.info("createSchoolYear_teacher_role_false() START");

				String path = "/api/schools/years/create";
			//	logger.info("       path:"+path);
				
				Map<String,String> year = new HashMap<>();
				
				//year.put("id", "1");
				year.put("school_id", "1");
				year.put("years", "2017-2018");
				year.put("from_year", "2017");// normal exam
				year.put("to_year", "2018");
				year.put("start_dt", "2017-09-05");
				year.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",TEA1_AUTH_KEY). // Teacher cannot create year
					contentType("application/json;charset=UTF-8").
				body(year).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(HttpStatus.FORBIDDEN.value()).body(containsString("Access is denied"));
				
		}
		
		@Test 
		 public void updateSchoolYear_normal_true() {
			 logger.info("updateSchoolYear_normal_true() START");

				String path = "/api/schools/years/update";
			//	logger.info("       path:"+path);
				
				Map<String,String> year = new HashMap<>();
				
				year.put("id", "1");
				year.put("school_id", "1");
				year.put("years", "2016-2017");
				year.put("from_year", "2016");// normal exam
				year.put("to_year", "2017");
				year.put("start_dt", "2016-09-05");
				year.put("notice", "Junit Test on "+Utils.now());
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(year).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(200);
				
		} 
		@Test 
		 public void updateSchoolYear_id_null_false() {
			 logger.info("updateSchoolYear_id_null_false() START");

				String path = "/api/schools/years/update";
			//	logger.info("       path:"+path);
				
				Map<String,String> year = new HashMap<>();
				
				// year.put("id", "1");
				year.put("school_id", "1");
				year.put("years", "2016-2017");
				year.put("from_year", "2016");// normal exam
				year.put("to_year", "2017");
				year.put("start_dt", "2016-09-05");
				year.put("notice", "Junit Test on "+Utils.now());
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(year).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		}
		@Test 
		 public void updateSchoolYear_conflict_year_false() {
			 logger.info("updateSchoolYear_conflict_year_false() START");

				String path = "/api/schools/years/update";
			//	logger.info("       path:"+path);
				
				Map<String,String> year = new HashMap<>();
				
				year.put("id", "1");
				year.put("school_id", "1");
				year.put("years", "2017-2018");
				year.put("from_year", "2017");// normal exam
				year.put("to_year", "2018");
				year.put("start_dt", "2017-09-05");
				year.put("notice", "Junit Test on "+Utils.now());
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(year).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void updateSchoolYear_teacher_role_false() {
			 logger.info("updateSchoolYear_teacher_role_false() START");

				String path = "/api/schools/years/update";
			//	logger.info("       path:"+path);
				
				Map<String,String> year = new HashMap<>();
				
				year.put("id", "1");
				year.put("school_id", "1");
				year.put("years", "2017-2018");
				year.put("from_year", "2017");// normal exam
				year.put("to_year", "2018");
				year.put("start_dt", "2017-09-05");
				year.put("notice", "Junit Test on "+Utils.now());
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",TEA1_AUTH_KEY). // TEACHER cannot update year
					contentType("application/json;charset=UTF-8").
				body(year).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));
				
		} 
		@Ignore
		@Test 
		 public void upload_school_photo_true() {
			 logger.info("upload_school_photo_true() START");

				String path = "/api/schools/upload_photo";
				String file_path ="D:\\HuyNQ\\Images\\FunnyAvatar\\monkey.jpg";
			//	logger.info("       path:"+path);
				
				
			  given().
			  	header("api_key",WEB_API_KEY).
				header("auth_key",ADM1_AUTH_KEY).
			  
				multiPart("file", new File(file_path)).
			  
			  when().post(path).then().
			  	log().ifValidationFails().
			  	assertThat().statusCode(200);
		} 
		@Test 
		 public void upload_school_photo_invalid_role_false() {
			 logger.info("upload_school_photo_invalid_role_false() START");

				String path = "/api/schools/upload_photo";
				String file_path ="D:\\HuyNQ\\Images\\FunnyAvatar\\monkey.jpg";
			//	logger.info("       path:"+path);
				
				
			  given().
			  	header("api_key",WEB_API_KEY).
				header("auth_key",STD10_AUTH_KEY). // invalid role
			  multiPart("file", new File(file_path)).
			  
			  when().post(path).then().
			  	log().ifValidationFails().
			  	assertThat().statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));
		} 

		@Ignore
		@Test 
		 public void createTerm_normal_true() {
			 logger.info("createTerm_normal_true() START");

				String path = "/api/schools/terms/create";
				
				 
				Map<String,String> term = new HashMap<>();
				
				//year.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","1");
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(200);
				
		} 
		
		
		@Test 
		 public void createTerm_teacher_role_false() {
			 logger.info("createTerm_teacher_role_false() START");

				String path = "/api/schools/terms/create";
				
				 
				Map<String,String> term = new HashMap<>();
				
				//year.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","1");
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",TEA1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));
				
		} 
		@Test 
		 public void createTerm_id_existing_false() {
			 logger.info("createTerm_id_existing_false() START");

				String path = "/api/schools/terms/create";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","1");
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void createTerm_year_id_not_existing_false() {
			 logger.info("createTerm_id_existing_false() START");

				String path = "/api/schools/terms/create";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","100000"); // invalid data
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void createTerm_year_id_other_school_false() {
			 logger.info("createTerm_year_id_other_school_false() START");

				String path = "/api/schools/terms/create";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","3"); // invalid data => belong to other schooln
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void createTerm_invalid_start_dt_after_from_dt_false() {
			 logger.info("createTerm_invalid_start_dt_after_from_dt_false() START");

				String path = "/api/schools/terms/create";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","1"); 
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2016-05-31 00:00:00"); // end_dt before start_dt => error
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void createTerm_term_val_existing_false() {
			 logger.info("createTerm_term_val_existing_false() START");

				String path = "/api/schools/terms/create";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","1"); 
				term.put("start_dt", "2016-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "1");  // already existing
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void updateTerm_normal_true() {
			 logger.info("updateTerm_normal_true() START");

				String path = "/api/schools/terms/update";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("notice", "Junit: "+Utils.now());
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY).
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(200);
				
		} 
		
		
		@Test 
		 public void updateTerm_teacher_role_false() {
			 logger.info("createTerm_teacher_role_false() START");

				String path = "/api/schools/terms/update";
				
				 
				Map<String,String> term = new HashMap<>();
				
				//year.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","1");
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",TEA1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(HttpStatus.FORBIDDEN.value()).body("developerMessage", containsString("Access is denied"));
				
		} 
		@Test 
		 public void updateTerm_id_null_false() {
			 logger.info("updateTerm_id_null_false() START");

				String path = "/api/schools/terms/update";
				
				 
				Map<String,String> term = new HashMap<>();
				
				// term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","1");
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void updateTerm_year_id_not_existing_false() {
			 logger.info("updateTerm_year_id_not_existing_false() START");

				String path = "/api/schools/terms/update";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","100000"); // invalid data
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void updateTerm_year_id_other_school_false() {
			 logger.info("updateTerm_year_id_other_school_false() START");

				String path = "/api/schools/terms/update";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","3"); // invalid data => belong to other schooln
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void updateTerm_invalid_start_dt_after_from_dt_false() {
			 logger.info("updateTerm_invalid_start_dt_after_from_dt_false() START");

				String path = "/api/schools/terms/update";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","1"); 
				term.put("start_dt", "2017-02-01 00:00:00");
				term.put("end_dt", "2016-05-31 00:00:00"); // end_dt before start_dt => error
				term.put("term_val", "3");
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		} 
		@Test 
		 public void updateTerm_term_val_existing_false() {
			 logger.info("updateTerm_term_val_existing_false() START");

				String path = "/api/schools/terms/create";
				
				 
				Map<String,String> term = new HashMap<>();
				
				term.put("id", "1");
				term.put("school_id", "1");
				term.put("year_id","1"); 
				term.put("start_dt", "2016-02-01 00:00:00");
				term.put("end_dt", "2017-05-31 00:00:00");
				term.put("term_val", "2");  // already existing in term.id = 2 already
				term.put("actived", "2");
				term.put("notice", "N/A");
				

				given().
					header("api_key",WEB_API_KEY).
					header("auth_key",ADM1_AUTH_KEY). // Teacher => false
					contentType("application/json;charset=UTF-8").
				body(term).			
				
				when().post(path).then().
		        	log().ifValidationFails().
		        	assertThat().statusCode(400);
				
		}
		
		@Test 
	    public void getTerm_normal_true() {
			logger.info("getTerm_normal_true() START");

			String path = "/api/schools/terms";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				pathParam("id", 1). // id = 4 not belong to school_id =1
			
			when().get(path+"/{id}").then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(200);
	    } 
		
		@Test 
	    public void getTerm_id_other_school_false() {
			logger.info("getTerm_id_other_school_false() START");

			String path = "/api/schools/terms";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				pathParam("id", 4). // id = 4 not belong to school_id =1
			
			when().get(path+"/{id}").then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(400).body("message", containsString("Bad Request"));
	    } 
		
		@Test 
	    public void getTerm_id_not_existing_false() {
			logger.info("getTerm_id_not_existing_false() START");

			String path = "/api/schools/terms";
			//logger.info("       path:"+path);
			
			given().
				header("api_key",WEB_API_KEY).
				header("auth_key",TEA1_AUTH_KEY).
				pathParam("id", 9999).
			
			when().get(path+"/{id}").then().
	        	log().ifValidationFails().
	        	assertThat().statusCode(400).body("message", containsString("Bad Request"));
	    } 
}
