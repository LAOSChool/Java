package com.itpro.restws.helper;
import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());

		}
	
	public static String currenDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());

		}
	
	public static String[] duplicateRemove(String [] array){
		for(int i=0;i<array.length-1;i++)
		{
		    for(int j=i + 1;j<array.length;j++)
		    {

		                if(array[i] != null && array[i].equals(array[j]))
		                {
		                  array[j] = null; // Mark for deletion later on
		                }
		    } 
		}
		
		ArrayList <String>list = new ArrayList<String>();
		for(int i=0;i<array.length;i++){
			if (array[i] != null && !array[i].equals("")){
				list.add(array[i]);
			}
		}
		return (String[]) list.toArray(new String[0]);
		
	}
	public static Integer parseInteger(String val){
		if (val == null || "".equals(val)){
			return null;
		}
		try{
			val = val.replaceAll("\r", "");
			val = val.replaceAll("\n", "");
			return Integer.valueOf(val);
		}catch(Exception e){
			return null;
		}
	}
	public static Integer parseLong(String val){
		if (val == null){
			return null;
		}
		try{
			val = val.replaceAll("\r", "");
			val = val.replaceAll("\n", "");
			return Integer.valueOf(val);
		}catch(Exception e){
			return null;
		}
	}
	public static String removeDateTimeLastZero(String dt){
		if (dt == null ){
			return null;
		}
		return dt.substring(0, dt.indexOf('.'));
	}
	public static String removeTxtLastComma(String str){
		if (str != null && str.length() > 0 && str.charAt(str.length()-1)==',') {
		      str = str.substring(0, str.length()-1);
		}
		return str;
	}
	public static String makeFolderByTime(String base_dir){
		Calendar now = Calendar.getInstance();
		
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		//int millis = now.get(Calendar.MILLISECOND);
		
		String str_year = String.format("%d", year);
		String str_month = String.format("%02d", month);
		String str_day = String.format("%02d", day);
		String str_hour = String.format("%02d", hour);
		String str_minute = String.format("%02d", minute);
		String str_second = String.format("%02d", second);
		//String str_millis = String.format("%03d", millis);
		// YYYY folder
		String sub_dir = base_dir + "/" + str_year;
		File directory = new File(String.valueOf(sub_dir));
	    if (! directory.exists()){
	        directory.mkdir();
	    }
		// YYYY/MM folder
	    sub_dir = sub_dir + "/" + str_month;
	    directory = new File(String.valueOf(sub_dir));
	    if (! directory.exists()){
	        directory.mkdir();
	    }
		// YYYY/MM/DD
	    sub_dir = sub_dir + "/" + str_day;
	    directory = new File(String.valueOf(sub_dir));
	    if (! directory.exists()){
	        directory.mkdir();
	    }
		// YYYY/MM/DD/HH
	    sub_dir = sub_dir + "/" + str_hour;
	    directory = new File(String.valueOf(sub_dir));
	    if (! directory.exists()){
	        directory.mkdir();
	    }
	    
	    // YYYY/MM/DD/HH/MM
	    sub_dir = sub_dir + "/" + str_minute;
	    directory = new File(String.valueOf(sub_dir));
	    if (! directory.exists()){
	        directory.mkdir();
	    }
	    
	    // YYYY/MM/DD/HH/MM/SS
	    sub_dir = sub_dir + "/" + str_second;
	    directory = new File(String.valueOf(sub_dir));
	    if (! directory.exists()){
	        directory.mkdir();
	    }
	    return sub_dir;
	}
	public static String getFileName(String username,String org_name){
		
		//String fileName = new SimpleDateFormat("yyyyMMddhhmmss_").format(Calendar.getInstance());
        String fileExt =  org_name.substring(org_name.lastIndexOf('.')+1);;
        String fileName = username + "_"+System.currentTimeMillis()+"."+fileExt;
//        String fileName = System.currentTimeMillis()+"_";        
//		if (org_name != null && (!org_name.equals(""))){
//			int start_idx = 0;
//			int endIdx = org_name.length();
//			if (endIdx > 256){
//				start_idx = endIdx - 256;
//			}
//			fileName = fileName+org_name.substring(start_idx, endIdx);
//		}else{
//			fileName = fileName+"UNKNOW.jpg";
//		}
		return fileName;
	}
	public static boolean checkDateTimeFormat(String inputString){
		if ((inputString == null ) || inputString.equals("")){
			return false;
		}
		//SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		       format.parse(inputString);
		       return true;
		    }
		    catch(Exception e)
		    {
		        return false;
		    }
	}
	public static boolean checkDateFormat(String inputString){
		if ((inputString == null ) || inputString.equals("")){
			return false;
		}
		//SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		    try{
		       format.parse(inputString);
		       return true;
		    }
		    catch(Exception e)
		    {
		        return false;
		    }
	}
	
	public static String dateToString(Date date){
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = formatter.format(date);
		return s;
	}
	
	public static Date fullTimeToDate(Date date){
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		String s = formatter.format(date);
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		    try{
		       return format.parse(s);
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
	}
	public static int getCurrentYear(){
		Calendar now = Calendar.getInstance();   // Gets the current date and time
		int year = now.get(Calendar.YEAR);
		return year;
	}
	
	public static Date parsetDate(String inputString){
		if ((inputString == null ) || inputString.equals("")){
			return null;
		}
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		    try{
		       return  format.parse(inputString);
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
	}
	public static Integer parseWeekDay(String inputString){
		if ((inputString == null ) || inputString.equals("")){
			return null;
		}
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		    try{
		       Calendar now = Calendar.getInstance();   // Gets the current date and time
		       now.setTime(format.parse(inputString));
		       return now.get(Calendar.DAY_OF_WEEK);
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
	}
	public static Integer parseMonth(String inputString){
		if ((inputString == null ) || inputString.equals("")){
			return null;
		}
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		       Calendar now = Calendar.getInstance();   // Gets the current date and time
		       now.setTime(format.parse(inputString));
		       return now.get(Calendar.MONTH);
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
	}
	public static Integer parseYear(String inputString){
		if ((inputString == null ) || inputString.equals("")){
			return null;
		}
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		       Calendar now = Calendar.getInstance();   // Gets the current date and time
		       now.setTime(format.parse(inputString));
		       return now.get(Calendar.YEAR);
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
	}
	public static Float parseFloat(String val){
		if (val == null || "".equals(val)){
			return null;
		}
		try{
			val = val.replaceAll("\r", "");
			val = val.replaceAll("\n", "");
			return Float.valueOf(val);
		}catch(Exception e){
			return null;
		}
	}
	public static String addMonthToDate(String inputString, int months){
		if ((inputString == null ) || inputString.equals("")){
			return null;
		}
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		    	Calendar cal = Calendar.getInstance();   // Gets the current date and time
		    	cal.setTime(format.parse(inputString));
		    	cal.add(Calendar.MONTH, months);
			    String s = format.format(cal);
				return s;
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
	}
	
	public static String addDayToDate(String inputString, int days){
		if ((inputString == null ) || inputString.equals("")){
			return null;
		}
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		    	Calendar cal = Calendar.getInstance();   // Gets the current date and time
		    	cal.setTime(format.parse(inputString));
		    	cal.add(Calendar.DATE, days);
			    String s = format.format(cal);
				return s;
		    }
		    catch(Exception e)
		    {
		        return null;
		    }
	}
	
	public static int compareTo(String date1,String date2){
		if ((date1 == null ) || date2.equals("")){
			return -1;
		}
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		    	Calendar cal1 = Calendar.getInstance();   // Gets the current date and time
		    	Calendar cal2 = Calendar.getInstance();   // Gets the current date and time
		    	cal1.setTime(format.parse(date1));
		    	cal2.setTime(format.parse(date2));
		    	
		    	return cal1.compareTo(cal2);
		    }
		    catch(Exception e)
		    {
		        return -1;
		    }
	}
	
}
