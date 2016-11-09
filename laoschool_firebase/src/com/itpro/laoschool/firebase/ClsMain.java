package com.itpro.laoschool.firebase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.firebase.FirebaseException;
import com.itpro.laoschool.firebase.ent.ApiKey_ent;
import com.itpro.laoschool.firebase.ent.AuthKey_ent;
import com.itpro.laoschool.firebase.ent.FireBase_ent;
import com.itpro.laoschool.firebase.ent.NotificationEnt;
import com.itpro.laoschool.firebase.ent.PostDataEnt;

public class ClsMain {
	static final String exitFileName = "./exit.exit";
	static private FireBase_DA main_da;
	static boolean islive = true;
	static int main_idle_time = 0;
	static Vector<FireBase_ent> vtMainList = new Vector<FireBase_ent>();
	
	
	static int CONCUR_LIMIT = 60; 				// Limitation of concurrent API calls( depend on unitel)
	static int MAX_ITEMS_PER_THREAD = 100;		// Maximum items need to be completed before checking for is_alive flag
	static int WAIT_TIME_OUT = 60*60*1000; // 60 minutes
	static boolean test = true;
	static String FIREBASE_FCM_AUTH_KEY = "AIzaSyCisIWVItd72I8Ha9_UGfIR79JwdipzDcA";
	static String FIREBASE_URL="https://fcm.googleapis.com/fcm/send";

	static Properties prop = new Properties();
	public static void main(String[] args) {
			int main_sleep = 1000;
			
			Utils.log("MAIN THREAD - Start !!!, to exit, please make a file:\" "+exitFileName,0);
			
			try {
				
				File currentDirectory = new File(new File(".").getAbsolutePath());
		        String propertiesPath= currentDirectory.getAbsolutePath();
		        System.out.println(" propertiesPath-"+propertiesPath);
		        Utils.log(" propertiesPath-"+propertiesPath, 0);
		        prop.load(new FileInputStream(propertiesPath+"/config.properties"));
		        if (prop.getProperty("FCM_AUTH_KEY") != null ){
		        	FIREBASE_FCM_AUTH_KEY = prop.getProperty("FCM_AUTH_KEY");
		        }
		        Utils.log(" propertiesPath-FIREBASE_FCM_AUTH_KEY:"+FIREBASE_FCM_AUTH_KEY, 0);
		    } catch (IOException e1) {
		        e1.printStackTrace();
		        Utils.log(e1.getMessage(),1);
		        return;
		    }
			
		main_da = new FireBase_DA(); // Connect to DB

		// Check exit flag file 
		File f = new File(exitFileName);
		// Start 10 charging thread
		
		for (int i = 0; i < 3; i ++){
			ThreadSend tcc = new ThreadSend(i);
			tcc.start();
		}
		
		while (islive) {
			main_sleep = 10000;
			try {
				if(f.exists()) {
					Utils.log(String.format("MAIN THREAD - found exit flag file : %s ",exitFileName),0);
					islive = false; // To exit all child threads
					break;
				}
				// Get next 1000 items from to charge
				synchronized(vtMainList){
					if(vtMainList.size()<= 0 ){
						vtMainList = main_da.get_firebase_list();
					}
				}
			} catch (Exception e) {
				main_sleep = 30000;
				Utils.log("MAIN THREAD Exception message = "+e.getMessage(),1);
				for (StackTraceElement st : e.getStackTrace()) {
					Utils.log("MAIN THREAD \t"+st,1);
				}
				try{
					if (main_da.getConn() != null && !main_da.getConn().isValid(2)){
						main_da.create_new_Conn();
					}
				}catch (Exception e2){	}
			}
			finally{
				// Wait seconds for data to be processed by child threads
				try{
					Thread.sleep(main_sleep);
				}catch (Exception e3){}
			}
		}
		main_da.setCloseCnn();
		// main_da.setCloseSmsCnn();
		Utils.log("MAIN THREAD  - Exit, bye !",0);
		
		
	}
	


	
	
	/***
	 * Only nested static classes can be static, by doing so we can use the
	 * nested class without having an instance of the outer class. 
	 */
	static class ThreadSend extends Thread {
		
		FireBase_DA sub_da = new FireBase_DA();// Connect to callercrbt DB of each thread
		int index = 0; // Index to identify each thread
		public ThreadSend(int i){
			index = i;
		}
		int wait_time = 1000;
		@Override
		public void run() {
			FireBase_ent ent = null;
			Vector<FireBase_ent> vtCurList = new Vector<FireBase_ent>();
			int items = 0;
			Utils.log(String.format("ThreadSend[%d] Started",index),0);
			int total_wait = 0;
			while (islive) {
				// Loop to ensure finish all current charging list before exit
				try{
					wait_time = 1000;
					//  Wait if over API call limit
					
					if (vtCurList.size() > 0  ){
						wait_time = 0;
						
						Utils.log(String.format("ThreadSend[%d] vtCurList.size()= %d",index,vtCurList.size()),0);
						ent = vtCurList.remove(vtCurList.size()-1); // Pop the last item
						
						Utils.log(String.format("ThreadSend[%d] firebase_msg.id: %d; START",index,ent.getId()),0);
						ent.setError("");
						ent.setSuccess(0);// default is not success
						StringBuilder err_msg =  new StringBuilder();
						
						while(true){
							// Find all auth_key ( current login sessions) by SSO_ID
							Utils.log(String.format("\n\nThreadSend[%d] Find all auth_key ( current login sessions) by SSO_ID:%s;",index,ent.getTo_sso_id()),0);
							ArrayList<AuthKey_ent> list = sub_da.get_auth_key_by_sso(ent.getTo_sso_id());
							if (list == null || list.size() == 0 ){
								err_msg.append(String.format("Cannot find any auth_key by SSO_ID: \"%s\" from table: authen_key, user is already logout",ent.getTo_sso_id()));
								err_msg.append("\n++++++\n");
								Utils.log(String.format("Cannot find any auth_key by SSO_ID: \"%s\" from table: authen_key, user is already logout",ent.getTo_sso_id()),1);
								ent.setSuccess(0);
								break;
							}
							//
							// For each AUTH_KEY, find API_KEY and CLOUND_TOKEN
							// 
							
							for (AuthKey_ent auth_key_ent : list){
								Utils.log(String.format("\n\nThreadSend[%d]  Find API_KEY and CLOUND_TOKEN using auth_key:%s;",index,auth_key_ent.getAuth_key()),0);
								ApiKey_ent api_key_ent = sub_da.get_apikey_by_authkey(auth_key_ent.getAuth_key());
								if (api_key_ent == null ){
									err_msg.append(String.format("Ignored auth_key: \"%s\" - User not using Mobile Device (ApiKey = NULL)",auth_key_ent.getAuth_key()));
									err_msg.append("\n++++++\n");
									Utils.log(String.format("Ignored auth_key: \"%s\" - User not using Mobile Device (ApiKey = NULL)",auth_key_ent.getAuth_key()),0);
									continue;
								}
								
								
								Utils.log(String.format("ThreadSend[%d] API_KEY:\n%s",index,nvl(api_key_ent.getApi_key())),0);
								Utils.log(String.format("ThreadSend[%d] CLOUND_TOKEN\n%s",index,nvl(api_key_ent.getCld_token())),0);
								if (api_key_ent.getApi_key() == null || api_key_ent.getApi_key().trim().length() == 0){
									Utils.log(String.format("ThreadSend[%d]API_KEY = NULL, ignored",index),1);
									
									err_msg.append(String.format("Ignored auth_key: \"%s\" - (ApiKey = NULL)",auth_key_ent.getAuth_key()));
									err_msg.append("\n++++++\n");									
									continue;
									
								}
								if (api_key_ent.getCld_token() == null || api_key_ent.getCld_token().trim().length() == 0){
									Utils.log(String.format("ThreadSend[%d]CLOUND_TOKEN = NULL, ignored",index),1);
									
									err_msg.append(String.format("Ignored auth_key: \"%s\" - (CLOUND_TOKEN = NULL)",auth_key_ent.getAuth_key()));
									continue;
								}

								// Sending firebase announcement
								
								try{
									//FirebaseResponse firebaseResponse = sendFirebase(ent.getTitle(),ent.getContent(),api_key_ent.getCld_token());
									FirebaseResponse firebaseResponse = sendFirebase("LaoSchool",ent.getContent(),api_key_ent.getCld_token());
									Utils.log(String.format("ThreadSend[%d] firebaseResponse.success:%s;",index,firebaseResponse.getSuccess()),0);
									
									if (firebaseResponse.getSuccess().equals("1")){
										ent.setSuccess(1);
									}else{
										ent.setSuccess(0);
									}
									err_msg.append(firebaseResponse.getResult());
									err_msg.append("\n++++++\n");
									
								}catch (Exception e){
									Utils.log(String.format("ThreadSend[%d] Exception message = ",index,e.getMessage()),1);
									for (StackTraceElement st : e.getStackTrace()) {
										Utils.log(String.format("ThreadSend[%d] \t%s", index,st),1);
									}
									ent.setSuccess(0);
									err_msg.append(e.getLocalizedMessage());
									err_msg.append("\n++++++\n");
								}
								
							}
							
							
							break;// Always break; here
						}
						ent.setError(err_msg.toString());
						ent.setSent_dt(Utils.getDateTime());
						// Update users and charging_his DB
						sub_da.updateWhenFinish(ent);
						Utils.log(String.format("ThreadSend[%d] firebase_msg.id: %d; END",index,ent.getId()),0);
						
				
					}else{
						// Get charge data
						synchronized (vtMainList){
							items = 0;
							while ( vtMainList.size() > 0 ){
								vtCurList.add(vtMainList.remove(vtMainList.size()-1));
								items++;
								if (items >= MAX_ITEMS_PER_THREAD){ // Maximum items in a thread
									break;
								}
							}
						}
						// Wait 1 second for main thread getting next data
						if (vtCurList.size() <= 0){
							wait_time = 10000;
						}
					}
				}catch (Exception e) {
					wait_time = 30000;
					Utils.log(String.format("ThreadCharge[%d] Exception message:%s",index,e.getMessage()),1);
					for (StackTraceElement st : e.getStackTrace()) {
						Utils.log(String.format("ThreadCharge[%d] \t %s",index,st),1);
					}
					try{
						if (sub_da.getConn() != null && !sub_da.getConn().isValid(2)){
							sub_da.create_new_Conn();
						}
					}catch (Exception e2){	}
				}finally{
					if (! islive) {
						break; // Child thread only exit if islive = FALSE and (vtCurList.size() = 0)
					}
					try{
						// Thread.yield();
						if (wait_time > 0 ){
							total_wait +=wait_time;
							Thread.sleep(wait_time); // Wait for main thread get charging list from DB
							
						}
						if (total_wait > 60*1000){
							total_wait = 0;
							Utils.log(String.format("ThreadCharge[%d] Ping MySQL",index),0);
							sub_da.ping_db();
						}
					}catch (Exception e){}

				}
			}// Exit while true
			sub_da.setCloseCnn();
			Utils.log(String.format("ThreadCharge[%d] Exit",index),0);
		}
		
	}
	
	static String nvl(String value){
		if ( value == null ){
			return "NULL";
		}
		return value;
			
	}
	
	/***
	 * Call web service to charge fee for a msisdn
	 * @param ent
	 * @return
	 * @throws FirebaseException 
	 * @throws UnsupportedEncodingException 
	 * @throws JacksonUtilityException 
	 */
	public static FirebaseResponse sendFirebase(String title, String body, String toDeviceToken) {
		
		Utils.log(String.format("sendFirebase START,\n [title]\n%s \n\n [body]\n%s \n\n [toDeviceToken]\n%s \n\n",title,body,toDeviceToken),0);
		
		FirebaseResponse firebaseResponse =  new FirebaseResponse();;
		    ///
		    firebaseResponse.setCanonical_ids("");
		    firebaseResponse.setFailure("1");
		    firebaseResponse.setMulticast_id("");
		    firebaseResponse.setResult("");
		    firebaseResponse.setSuccess("0");
		    
		try{
			NotificationEnt notifyEnt = new NotificationEnt();
			notifyEnt.setTitle("[No subject]".equalsIgnoreCase(title)?null:title);
			notifyEnt.setBody(body);
			notifyEnt.setBadge(1);
			
			
			PostDataEnt postDatEnt = new PostDataEnt();
			postDatEnt.setPriority("high");
			postDatEnt.setTo(toDeviceToken);
			postDatEnt.setNotification(notifyEnt);
			
			ObjectMapper mapper = new ObjectMapper();
			
			String jsonInString = mapper.writeValueAsString(postDatEnt);
			Utils.log(String.format("sendFirebase jsonInString: \n\t %s",jsonInString),0);
			
				URL url = new URL(FIREBASE_URL);
				
				Utils.log(String.format("sendFirebase FIREBASE_URL:%s",FIREBASE_URL),0);
			    
			    Utils.log("\nSending 'POST' request to URL : " + url,0);
			    Utils.log("Post parameters : " + jsonInString,0);
				
			    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			    
			    conn.setDoOutput(true);
			    conn.setRequestMethod("POST");
			    conn.setRequestProperty("Content-Type", "application/json");
			    conn.setRequestProperty("Authorization", "key=" + FIREBASE_FCM_AUTH_KEY);
			    conn.setDoOutput(true);

			    OutputStream os = conn.getOutputStream();
			    os.write(jsonInString.getBytes());
			    os.flush();
			    os.close();
			    
			    int responseCode = conn.getResponseCode();
			    Utils.log("Response Code : " + responseCode,0);

			    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    String inputLine;
			    StringBuffer response = new StringBuffer();

			    while ((inputLine = in.readLine()) != null) {
			        response.append(inputLine);
			    }
			    in.close();

			    // print result
			    String rsp = response.toString();
			    Utils.log(rsp, 0);
			    /***
				 * {"multicast_id":6782339717028231855,"success":0,"failure":1,"canonical_ids":0,"results":[{"error":"InvalidRegistration"}]}
				 * { "multicast_id": 108,"success": 1,"failure": 0,"canonical_ids": 0,"results": [{ "message_id": "1:08" }]}
				 */
			    
			   
			    String multicast_id="";
				String success="";
				String failure="";
				String canonical_ids="";
				String result="";
				 
			    // Parsing response
			    String[] parts=rsp.split(",");
			    if (parts != null && parts.length > 0){
			    	for (int i = 0;i< parts.length;i++){
				    	String str = parts[i];
				    	
				    	if (str.indexOf("\"multicast_id\"") >= 0){
				    		 String[] sub_parts=str.split(":");
				    		 multicast_id = (sub_parts!= null && sub_parts.length>=2)? sub_parts[1]:"";
				    	}else if (str.indexOf("\"success\"") >= 0){
				    		String[] sub_parts=str.split(":");
				    		success = (sub_parts!= null && sub_parts.length>=2)? sub_parts[1]:"";
				    		
				    	}else if (str.indexOf("\"failure\"") >= 0){
				    		String[] sub_parts=str.split(":");
				    		failure = (sub_parts!= null && sub_parts.length>=2)? sub_parts[1]:"";
				    		
				    	}else if (str.indexOf("\"canonical_ids\"") >= 0){
				    		String[] sub_parts=str.split(":");
				    		canonical_ids = (sub_parts!= null && sub_parts.length>=2)? sub_parts[1]:"";
				    		
				    	}else if (str.indexOf("\"results\"") >= 0){
				    		result = str;
				    	}
			    	}
			    }
			    
			    
			    
			    ///
			    firebaseResponse.setCanonical_ids(canonical_ids.trim());
			    firebaseResponse.setFailure(failure.trim());
			    firebaseResponse.setMulticast_id(multicast_id.trim());
			    firebaseResponse.setResult(result.trim());
			    //firebaseResponse.setResult(rsp);
			    firebaseResponse.setSuccess(success.trim());
			    
		}catch (Exception e){
			Utils.log(String.format("sendFirebase Exception: \n\t %s",e.getMessage()),1);
			  firebaseResponse.setCanonical_ids("");
			    firebaseResponse.setFailure("1");
			    firebaseResponse.setMulticast_id("");
			    firebaseResponse.setResult(e.getMessage());
			    firebaseResponse.setSuccess("0");
			    
		}
		 //Utils.log(firebaseResponse.toString(),0);
		return firebaseResponse;

	}
}
