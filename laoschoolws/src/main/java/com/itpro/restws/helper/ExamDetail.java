package com.itpro.restws.helper;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExamDetail {
	
	String sresult;
	String notice;
	String exam_dt;
	//String json;
	
//	public String getJson() {
//		return json;
//	}
//	public void setJson(String json) {
//		this.json = json;
//	}
	public String getSresult() {
		return sresult;
	}
	public String getNotice() {
		return notice;
	}
	public String getExam_dt() {
		return exam_dt;
	}
	public void setSresult(String sresult) {
		this.sresult = sresult;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public void setExam_dt(String exam_dt) {
		this.exam_dt = exam_dt;
	}
	
	public static ExamDetail strJson2ExamDetail(String json_str_examdetail){
		//Convert JSON from String to Object
		ObjectMapper mapper = new ObjectMapper();
		ExamDetail examDetail=null;
		try {
			examDetail = mapper.readValue(json_str_examdetail, ExamDetail.class);
			return examDetail;
			
		} catch (IOException e) {
			throw new ESchoolException("Cannot convert STRING to JSON =>" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
