package com.itpro.sendmail;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.itpro.ent.MailEnt;

public class SendMail {
	static final String exitFileName = "./sendmail.exit";
	static final String config_file = "./email_config.cfg";
	static private DA da;
	static boolean islive = true;
	static int WAIT_NEXT_LIST = 60000;// every one minute
	static Vector<MailEnt> vtMailList = new Vector<MailEnt>(); // List 1000 items
	static int MAX_ITEMS_PER_THREAD = 100;
	
	static HashMap<String,Object> conf = null;
	static String MAIL_PORT = "587";
	static String  MAIL_SERVER = "smtp.gmail.com";
	static String DISP_NAME="TRANSPORTER";
	static String USR_NAME="laoschoolreport@gmail.com";
	static String USR_PASS="itpro!@#";
	
	static Properties props = new Properties();
	// ============================= SYNC DB 
	
	
	static Vector<MailEnt> vtSyncList = new Vector<MailEnt>(); // List 1000 items
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Utils.log("LAOSCHOOL_EMAIL - MAIN THREAD - Start !!!, to exit, please make a file in same folder:\" "+exitFileName,0);
//		try{
//			da = new DA(); // Connect to DB	
//			moving_email_msg();
//			da.setCloseCnn();
//		}catch (Exception ex){
//			ex.printStackTrace();
//		}
		
		Utils.log("EMAIL config_file:"+config_file,0);
		da = new DA(); // Connect to DB	
		// Parsing configuration info
		conf = get_config_info();
		String tmp_mail_port =  (String) conf.get("MAIL_PORT");
		if (tmp_mail_port != null && tmp_mail_port.length() > 0 && Utils.isNum(tmp_mail_port) ){
			MAIL_PORT = tmp_mail_port;
		}
		String tmp_mail_server =  (String) conf.get("MAIL_SERVER");
		if (tmp_mail_server != null && tmp_mail_server.length() > 0 ){
			MAIL_SERVER = tmp_mail_server;
		}
		String tmp_dsp_name =  (String) conf.get("DISP_NAME");
		if (tmp_dsp_name != null && tmp_dsp_name.length() > 0 ){
			DISP_NAME = tmp_dsp_name;
		}
		Utils.log("Configuration file:"+config_file, 0);
		Utils.log("MAIL_SERVER:"+MAIL_SERVER, 0);
		Utils.log("MAIL_PORT:"+MAIL_PORT, 0);
		Utils.log("DISP_NAME:"+DISP_NAME, 0);
		
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", MAIL_SERVER);
		props.put("mail.smtp.port", MAIL_PORT);
		
		// Runn thread to send mail
		File f_exit = new File(exitFileName);
		ThreadMail threadmail = new ThreadMail();
		threadmail.start();

		
		while (islive) {
			try {
				if(f_exit.exists()) {
					Utils.log(String.format("SENDMAIL - MAIN THREAD - found exit file : %s ",exitFileName),0);
					islive = false; // To exit all child threads
					break;
				}
				// Get next 1000 items from to sync
				synchronized(vtMailList){
					// Check and moving email from laoschool t
					moving_email_msg();
					// Check pending email to send
					if(vtMailList.size()<= 0 ){
						vtMailList = da.get_mail_list();
					}
				}
			} catch (Exception e) {
				// WAIT_NEXT_LIST=20000; // TODO: Test
				Utils.log(" Exception message = "+e.getMessage(),1);
				for (StackTraceElement st : e.getStackTrace()) {
					Utils.log("\t"+st,1);
				}
				try{
					// Send mail
					if (da.get_laoschool_conn() != null && !da.get_laoschool_conn().isValid(2)){
						da.force_make_new_laoschool_Conn();
					}
					
					// Send mail
					if (da.get_sendmail_conn() != null && !da.get_sendmail_conn().isValid(2)){
						da.force_make_new_Conn();
					}
				}catch (Exception e2){	}
			}finally{
				try{
					// Wait 1 minute for child threads processing 1000 phones
					Thread.sleep(WAIT_NEXT_LIST);
				}catch (Exception e){
					Utils.log(" Exception message = "+e.getMessage(),1);
					for (StackTraceElement st : e.getStackTrace()) {
						Utils.log("\t"+st,1);
					}
				}
			}
		}
		da.setCloseCnn();
		Utils.log("SYNC MAIN THREAD  - Exit, bye !",0);
	}
	private static HashMap<String,Object> get_config_info() {
		HashMap<String,Object> hash_conf = new HashMap<String,Object>();
		try {
			Config conf = new Config(new File(config_file));
			hash_conf.put("MAIL_SERVER", conf.lookupSymbol("MAIL_SERVER"));
			hash_conf.put("MAIL_PORT", conf.lookupSymbol("MAIL_PORT"));
			hash_conf.put("DISP_NAME", conf.lookupSymbol("DISP_NAME"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

			
		return hash_conf;
	}
	static private void moving_email_msg() throws SQLException{
		 Vector<MailEnt>  list = da.move_email_msg();
		 if (list != null && list.size() > 0){
			 while (list.size() > 0){
				 MailEnt emailEnt = list.remove(list.size()-1); // Pop the last item
				 da.insert_send_mail(emailEnt);
			 }
		 }
	}
	static class ThreadMail extends Thread {
		static private DA sub_da = new DA();
		public ThreadMail(){
			
		}
		@Override
		public void run() {
			MailEnt ent = null;
			Vector<MailEnt> vtCurList = new Vector<MailEnt>();
			int items = 0;
			int wait_time = 5000;//1000;
			Utils.log(String.format("ThreadMail Started"),0);
			String ret ="DONE";
			String content ="";
			String receivers ="";
			int success =1;
			while (islive) { // Loop to ensure finish all current charging list before exit
				try{
					if (vtCurList.size() > 0  ){
						wait_time = 0;
						Utils.log(String.format("ThreadMail; vtCurList.size()= %d",vtCurList.size()),0);
						ent = vtCurList.remove(vtCurList.size()-1); // Pop the last item
						//Send mail to receiver list
						try{
							
							Utils.log(String.format("ThreadMail; sending mail START"),0);
							Utils.log(String.format("ThreadMail; mail subject:  %s",ent.getSub()),0);
							Utils.log(String.format("ThreadMail; mail body:  %s",ent.getBody()),0);
							ret ="DONE";
							receivers = ent.getReceivers();
							receivers = receivers.trim().replaceAll("\\s+", ",");
							receivers = receivers.replaceAll(";+", ",");
							receivers = receivers.replaceAll(",+", ",");
							Utils.log(String.format("ThreadMail; receivers: %s",receivers),0);
							content = ent.getBody();
							
							Session session = Session.getInstance(props,
									  new javax.mail.Authenticator() {
										protected PasswordAuthentication getPasswordAuthentication() {
											return new PasswordAuthentication(USR_NAME, USR_PASS);
										}
									  });
							
							Message message = new MimeMessage(session);
							message.setFrom(new InternetAddress(USR_NAME,DISP_NAME));
							message.setRecipients(Message.RecipientType.TO,
								InternetAddress.parse(receivers));
							message.setSubject(ent.getSub());
							message.setHeader("Content-Type", "text/plain; charset=UTF-8");
							message.setText(content);
							Transport.send(message);
					
							
//////////////////////////////
				            
							success =1;
						}catch (Exception e){
							// Update to error list to retry later
							ret ="Error: "+e.getMessage();
							success = 0;
							
							Utils.log(String.format("ThreadMail Exception message: %s",e.getMessage()),1);
							
							Utils.log(String.format("ThreadMail ent.getDescription(): %s",ent.getDescription()),1);
							for (StackTraceElement st : e.getStackTrace()) {
								Utils.log(String.format("ThreadMail \t %s",st),1);
							}
						}finally{
							Utils.log(String.format("ThreadMail sending finish."),0);
							try{
								ent.setSuccess(success);
								ent.setDescription(ret);
								ent.setDate_time(Utils.getDateTime());
								sub_da.update_sendmail_result(ent);
							}catch (Exception ex){
								Utils.log(String.format("ThreadMail Exception message 2222: %s",ex.getMessage()),1);
							}
						}
					}else{
						if (! islive) {
							break; // Child thread only exit if islive = FALSE and (vtCurList.size() = 0)
						}
						// Get sync data
						synchronized (vtMailList){
							items = 0;
							while ( vtMailList.size() > 0 ){
								vtCurList.add(vtMailList.remove(vtMailList.size()-1));
								items++;
								if (items >= MAX_ITEMS_PER_THREAD){ // Maximum items in a thread
									break;
								}
							}
						}
						
						if (vtCurList.size() <= 0 ){
							//wait_time = 60000;//wait 1 minute
							wait_time = 10000;//test
						}else{
							wait_time = 0;
						}
					}

				}catch (Exception e) {
					wait_time = 300000;// Wait 5 minutes
					Utils.log(String.format("ThreadMail Exception message: %s",e.getMessage()),1);
					for (StackTraceElement st : e.getStackTrace()) {
						Utils.log(String.format("ThreadMail \t %s",st),1);
					}
				}finally{
					try{
						// Wait 1 minute for child threads processing 1000 phones
						if (wait_time > 0 ){
							Thread.sleep(wait_time);
						}
					}catch (Exception e){}
				}
			}
			Utils.log(String.format("ThreadMail Exit"),0);
		}
	}

	
	

}
