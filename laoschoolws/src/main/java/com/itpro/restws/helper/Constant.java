package com.itpro.restws.helper;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
public class Constant {
	public static final int MAX_RESP_ROW=100;
	public static final int MAX_STUDENT_SESSION=3;
	public static final String API_KEY="TEST_API_KEY";
	public static final String LOGIN_LINK="/login";
	public static final String FORGOT_PASS="/forgot_pass";
	public static final String LOGOUT_LINK="/api/logout";
	
	public static final String CMD_FOROT_PASS = "FOROT_PASS";
	public static final String CMD_MESSAGE = "MESSAGE";
	public static final String CMD_NOTIFY = "NOTIFY";
	public static final String CMD_RANK_PROCESS = "RANK_PROCESS";
	
	public static final String actlog_id ="x-actlog_id";
	
	public static final String HEADER_API_KEY = "api_key";
	public static final String HEADER_AUTH_KEY = "auth_key";
	
	public static final int SMALL_SIZE = 90 ;
	public static final int BIG_SIZE = 300;
	
	
	public static final String HEADER_USERNAME = "sso_id";
	public static final String HEADER_PASSWORD = "password";
	public static final int ActionLogMaxLength = 65535;//64 Kilobytes
	
	public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final String PGM_REST = "REST";
    public static final String USER_SYS = "SYS";
    
    public static final String POINT_FORMAT = "%.2f";
    
    public static final String UPLOAD_TYPE_CSV = "CSV";
    public static final String UPLOAD_TYPE_EXCEL = "EXCEL";
    
	public static final String[] exam_keys ={
			  "m1", 
			  "m2", 
			  "m3", 
			  "m4",
			  "m5",
			  "m6",
			  "m7",
			  "m8",
			  "m9",
			  "m10",
			  "m11",
			  "m12",
			  "m13",
			  "m14",
			  "m15",
			  "m16",
			  "m17",
			  "m18",
			  "m19",
			  "m20"
			  
			};
	/***
	 * 1: Month Exam, 2: Term Contest, 3: Ave 4 months , 4:  Ave Term, 5: Ave year, 6: retest, 7: final level contest
	 */
	public static final int[] exam_types ={
			  1,//Month Exam
			  2,//Term Contest
			  3,// Ave 4 months
			  4,//Ave Term
			  5,//Ave year
			  6,// Re-test
			  7//final level contest
			  
			};
	public static final String[] NON_DEVICE_API_KEY ={
			  "WEB",
			  "SMS",
			  "TEST", 
			  "TEST_API_KEY"
			};
	
	public static final String[] datetime_formats ={
			  "yyyy-MM-dd", 
			  "yyyy-MM-dd HH:mm:ss", 
			  "yyyy-MM-dd HH:mm:ss.S",
			  "yyyy-MM-dd HH:mm:ss.SSS",
			  "yyyy.MM.dd G 'at' HH:mm:ss z",	//  2001.07.04 AD at 12:08:56 PDT
			  "dd-MM-yyyy",
			  "dd-MM-yyyy HH:mm:ss",
			  "dd-MM-yyyy HH:mm:ss.SSS",
			  "dd-MM-yyyy HH:mm:ss.S",
			  "dd-MM-yyyy G 'at' HH:mm:ss z",	// 2001.07.04 AD at 12:08:56 PDT
			  
			  "EEE, MMM d, ''yy",				// Wed, Jul 4, '01
			  "h:mm a",							// 12:08 PM
			  "hh 'o''clock' a, zzzz",			// 12 o'clock PM, Pacific Daylight Time
			  "K:mm a, z",						// 0:08 PM, PDT
			  "yyyyy.MMMMM.dd GGG hh:mm aaa",	//  02001.July.04 AD 12:08 PM
			  "EEE, d MMM yyyy HH:mm:ss Z",		//  Wed, 4 Jul 2001 12:08:56 -0700
			  "yyMMddHHmmssZ",					//  010704120856-0700
			  "yyyy-MM-dd'T'HH:mm:ss.SSSZ",		//  2001-07-04T12:08:56.235-0700
			  "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",	//  2001-07-04T12:08:56.235-07:00
			  "YYYY-'W'ww-u",					//  2001-W27-3,
			  "E MMM dd HH:mm:ss Z yyyy"   		// "Tue Sep 13 16:03:10 GMT+07:00 2016
			  
			};
	public static String[] UserCVSheader = {"fullname","nickname","addr1","addr2","phone","birthday","gender","email","std_parent_name"};
	// Action Log Type
	public static  String ACTION_TYPE_MESSAGE = "MESSAGE";
	public static  String  ACTION_TYPE_NOTIFY = "NOTIFY";
	public static  String  ACTION_TYPE_MARK = "MARK";
	public static  String  ACTION_TYPE_ROLLUP = "ROLLUP";
	public static final Map<String, String> MAP_ACTIONS;
	static {
		MAP_ACTIONS = new HashMap<String, String>();
	    String[][] pairs = {
	        {"/api/messages/create_ext", ACTION_TYPE_MESSAGE},
	        {"/api/messages/create", ACTION_TYPE_MESSAGE},
	        {"/api/notifies/create", ACTION_TYPE_NOTIFY},
	        {"/api/notifies/create_php",ACTION_TYPE_NOTIFY},
	        {"/api/exam_results/input",ACTION_TYPE_MARK},
	        {"/api/exam_results/input/batch", ACTION_TYPE_MARK},
	        {"/api/attendances/create", ACTION_TYPE_ROLLUP}
	    };
	    for (String[] pair : pairs) {
	    	MAP_ACTIONS.put(pair[0], pair[1]);
	    }
	}
}
