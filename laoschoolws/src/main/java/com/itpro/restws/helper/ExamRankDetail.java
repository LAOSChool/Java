package com.itpro.restws.helper;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExamRankDetail {
	
	String ave;
	String grade;
	Integer allocation;
	
	public static ExamRankDetail strJson2ExamDetail(String json_str_detail){
		if (json_str_detail == null || json_str_detail.trim().length() == 0){
			return null;
		}
		//Convert JSON from String to Object
		ObjectMapper mapper = new ObjectMapper();
		ExamRankDetail examDetail=null;
		try {
			examDetail = mapper.readValue(json_str_detail, ExamRankDetail.class);
			return examDetail;
			
		} catch (IOException e) {
			throw new ESchoolException("Cannot ExamRankDetail() convert STRING to JSON =>" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public String getAve() {
		return ave;
	}

	public String getGrade() {
		return grade;
	}

	public Integer getAllocation() {
		return allocation;
	}

	public void setAve(String ave) {
		this.ave = ave;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public void setAllocation(Integer allocation) {
		this.allocation = allocation;
	}
	

}
