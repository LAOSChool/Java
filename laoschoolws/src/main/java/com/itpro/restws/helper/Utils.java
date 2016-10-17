package com.itpro.restws.helper;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

public class Utils {
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	protected static final Logger logger = Logger.getLogger(Utils.class);
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
		//
		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		       formater.parse(inputString);
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
		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd");
		    try{
		       formater.parse(inputString);
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
	
	public static String dateToStringDateOnly(Date date){
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		String s = formatter.format(date);
		return s;
	}
	public static String dateToStringDateOnly_with_format(Date date,String str_format){
		Format formatter = new SimpleDateFormat(str_format);
		String s = formatter.format(date);
		return s;
	}
	
	
	public static String numberToDateTime(Long value){
		String myDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(value.longValue() * 1000L));
		
		
		return myDate;
	}
	
	public static Date fullTimeToDate(Date date){
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		String s = formatter.format(date);
		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd");
		    try{
		       return formater.parse(s);
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
	
//	public static Date parsetDate(String inputString){
//		if ((inputString == null ) || inputString.equals("")){
//			return null;
//		}
////		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd");
////		try{
////		       return  formater.parse(inputString);
////		    }
////		    catch(Exception e)
////		    {
////		        return null;
////		    }
//
//		SimpleDateFormat formater = null;
//		for (int i=0;i< com.itpro.restws.helper.Constant.datetime_formats.length;i++){
//			formater = new java.text.SimpleDateFormat(Constant.datetime_formats[i]);
//			try{
//				return  formater.parse(inputString);
//				
//			} catch(Exception e)
//		    {
//		        
//		    }
//		}
//		return null;
//		    
//	}
	public static Date parsetDateAll(String inputString){
		if ((inputString == null ) || inputString.equals("")){
			return null;
		}
//		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd");
//		try{
//		       return  formater.parse(inputString);
//		    }
//		    catch(Exception e)
//		    {
//		        return null;
//		    }
		Date date  = null;
		SimpleDateFormat formater = null;
		for (int i=0;i< com.itpro.restws.helper.Constant.datetime_formats.length;i++){
			formater = new java.text.SimpleDateFormat(Constant.datetime_formats[i]);
			try{
				date  = formater.parse(inputString);
				if (!inputString.equals(formater.format(date))) {
			        date = null;
			        logger.info("parsetDateAll(): NG="+Constant.datetime_formats[i]);
			    }else{
			    	logger.info("parsetDateAll(): OK="+Constant.datetime_formats[i]);
					return date;	
			    }
				
				
				
			} catch(Exception e)
		    {
				logger.info("parsetDateAll(): Exception="+Constant.datetime_formats[i]);   
		    }
		}
		return null;
		    
	}
	public static Integer parseWeekDay(String inputString){
		if ((inputString == null ) || inputString.equals("")){
			return null;
		}
		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd");
		    try{
		       Calendar now = Calendar.getInstance();   // Gets the current date and time
		       now.setTime(formater.parse(inputString));
		       return now.get(Calendar.DAY_OF_WEEK);
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
		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd");
		    try{
		       Calendar now = Calendar.getInstance();   // Gets the current date and time
		       now.setTime(formater.parse(inputString));
		       return now.get(Calendar.YEAR);
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
		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		       Calendar now = Calendar.getInstance();   // Gets the current date and time
		       now.setTime(formater.parse(inputString));
		       return (now.get(Calendar.MONTH)+1);
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
		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		    	Calendar cal = Calendar.getInstance();   // Gets the current date and time
		    	cal.setTime(formater.parse(inputString));
		    	cal.add(Calendar.MONTH, months);
			    String s = formater.format(cal.getTime());
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
		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		    	Calendar cal = Calendar.getInstance();   // Gets the current date and time
		    	cal.setTime(formater.parse(inputString));
		    	cal.add(Calendar.DATE, days);
			    String s = formater.format(cal.getTime());
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
		SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    try{
		    	Calendar cal1 = Calendar.getInstance();   // Gets the current date and time
		    	Calendar cal2 = Calendar.getInstance();   // Gets the current date and time
		    	cal1.setTime(formater.parse(date1));
		    	cal2.setTime(formater.parse(date2));
		    	
		    	return cal1.compareTo(cal2);
		    }
		    catch(Exception e)
		    {
		        return -1;
		    }
	}
	public static int convertDayOfWeekToSyWeekDayID(Integer dayofWee){
		
			if (dayofWee.intValue() ==  Calendar.MONDAY){ //2
				return 1;
			}
			else if (dayofWee.intValue() ==  Calendar.TUESDAY){ //3
				return 2;
			}
			else if (dayofWee.intValue() ==  Calendar.WEDNESDAY){ //4
				return 3;
			}
			else if (dayofWee.intValue() ==  Calendar.THURSDAY){ //5
				return 4;
			}
			else if (dayofWee.intValue() ==  Calendar.FRIDAY){ //6
				return 5;
					
			}
			else if (dayofWee.intValue() ==  Calendar.SATURDAY){ //7
				return 6;
				
			}
			else if (dayofWee.intValue() ==  Calendar.SUNDAY){ //1
				return 7;
				
			}
			return 0;
	
	}
	public static void ensureFolder(String fld_path){
		File directory = new File(fld_path);
	    if (! directory.exists()){
	        directory.mkdir();
	    }	
	
	}

	public static String resizeImage(String inputPath, int new_width, int new_height) throws ESchoolException{
		try{
			if (inputPath == null || inputPath.trim().length() == 0){
				throw new ESchoolException("resizeImage(): inputPath = NULL ", HttpStatus.BAD_REQUEST);
			}
			File inputFile = new File(inputPath);
			
			BufferedImage originalImage = ImageIO.read(inputFile);
			if (originalImage == null ){
				throw new ESchoolException("Cannot read input fie:"+inputPath, HttpStatus.BAD_REQUEST);
			}
			
			String fName = inputFile.getName();// /d/home/huynq/test.jpg => test.jpg
			String fExt = getFileExtension(fName); // test.jpg =>  jpg
			String fPath = inputFile.getParent(); // /d/home/huynq/test.jpg => /d/home/huynq/
			
			String outPath = fPath +"/" + getFilenameWithoutExt(fName) + "_" + new_width + "." +    fExt; // /d/home/huynq/test_300.jpg
			File outFile = new File(outPath);
			if (outFile.exists() ){
				outFile.delete();
			}
			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

			BufferedImage resizeImageJpg = resizeImage(originalImage, type,new_width,new_height);//jpg,png
			ImageIO.write(resizeImageJpg, fExt, new File(outPath));

			return outPath;
		}catch (Exception e){
			
			for (StackTraceElement st: e.getStackTrace()){
				logger.error(st.toString());
			}
			throw new ESchoolException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		

	}
	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT){
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	    }

	private static String getFileExtension(String fileName){
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i >= 0) {
		    extension = fileName.substring(i+1);
		}
		/***
		 * "file.doc" => "doc"
		"file.doc.gz" => "gz"
		".doc" => "doc"
		 */
		return extension;
	}
	private static String getFilenameWithoutExt(String str){
		
        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
	}
	public static ArrayList<String> readFileIntoArray(String fileName){
		BufferedReader bufferReader = null;
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			String strLine;
			bufferReader = new BufferedReader(new FileReader(fileName));
			
			// How to read file in java line by line?
			while ((strLine = bufferReader.readLine()) != null) {
				list.add(strLine);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			//throw new RuntimeException("readFileIntoArray() error:"+e.getMessage());
			list = null;
		} finally {
			try {
				if (bufferReader != null) bufferReader.close();
			} catch (IOException crunchifyException) {
				crunchifyException.printStackTrace();
			}
		}
		return list;
	}
	
		public static String validMobilePhoneNo(String phoneNo){
			logger.info("validPhone START():"+phoneNo);
			phoneNo = phoneNo.trim();
			while (phoneNo.trim().length() > 0 && phoneNo.startsWith("0") ){
				phoneNo = phoneNo.substring(1);
			}
			if (phoneNo.startsWith("856")){
				phoneNo = phoneNo.substring(3);
			}
			if (phoneNo.startsWith("+856")){
				phoneNo = phoneNo.substring(4);
			}
			while (phoneNo.trim().length() > 0 && phoneNo.startsWith("0") ){
				phoneNo = phoneNo.substring(1);
			}
			phoneNo ="0"+phoneNo;// 0302000010 or 02029999250
			logger.info("fixed phoneNo:"+phoneNo);
			if (phoneNo.matches("020\\d{8,10}")) {
				return phoneNo;
			}else  if (phoneNo.matches("030\\d{7,10}")) {
				return phoneNo;
			}
			return null;
		}
		
	
}

