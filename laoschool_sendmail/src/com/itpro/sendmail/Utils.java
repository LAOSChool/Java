package com.itpro.sendmail;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Utils {

	/***
	 * Log message to standard output and files.
	 * INFO log will be logged to ./log/YYYYMMDD_sync_song.log
	 * synchronized method is for future, if multiple thread calls  
	 * @param message
	 * @param type
	 */
	public static synchronized  void log(String message,int type){
		String msg_type = "INFO";
		String info_folder = "/var/log/laoschool/sendmail/";
		// String info_folder = "D:\\jar\\logs";
		//String err_folder = "./error/";
		
		String info_file = info_folder+getDate()+"_sendmail.log";
		//String erro_file = err_folder+getDate()+"_sync_song_error.log";
		String log_file = info_file;
		try{
			if (type > 0 ){
				msg_type = "ERROR";
				//log_file = erro_file;
			}

			String log= String.format("[%s][%s] %s \n",getDateTime(),msg_type,message );
			// Log to standard screen
			System.out.print(log);

			// Log all message to to info file
			File f = new File(log_file);
			if (! f.exists() ){
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f,true); // Appending mode
			fw.write(log);
			fw.close();
           
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public	static String getDateTime(){
		String datetime="0000-00-00 00:00:00";
			try {
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 datetime = format.format(date);
			} catch (Exception e) {
			e.printStackTrace();
			}
			return datetime;
	}
	
	public	static String getDate(){
		String datetime="UNKNOWDATE";
			try {
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				datetime = format.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return datetime;
	}	
	/***
	 * Parsing WebService result from XML to Hash collection
	 * @param str
	 * @return
	 */
	public static HashMap<String, String> loadProperties(String str) throws Exception{
		HashMap<String, String>  hm=new HashMap<String, String>();

		if(str.endsWith(","))
		str=str.substring(0,str.length()-1);
		String[] strarr=str.split(",");
		
		for (String string : strarr) {
			if(string.contains("=")){
			String[] strarr2=string.split("=");
			hm.put(strarr2[0], strarr2[1]);
			}
		}
					
		return hm;
	}	
	public static boolean isNum(String strNum) {
	    boolean ret = true;
	    try {

	        Double.parseDouble(strNum);

	    }catch (NumberFormatException e) {
	        ret = false;
	    }
	    return ret;
	}	
	
}
