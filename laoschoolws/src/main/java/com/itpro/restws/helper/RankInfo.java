package com.itpro.restws.helper;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RankInfo {
	//Ten bai thi: m1,m2...m20
	
	@JsonIgnore
	String ex_key;
	@JsonIgnore
	Integer user_id;
	@JsonIgnore
	Integer exam_rank_id;
	@JsonIgnore
	Integer year_id;
	@JsonIgnore
	ArrayList<Float> marks;// Danh sach diem tat ca ca mon

	//// /////////// mapper.writeValueAsString(rankInfo) ////
	String ave; //Diem trung binh
	String grade;// Phan loai A,B,C
	Integer allocation;// Rank theo lop
	
	public Integer getAllocation() {
		return allocation;
	}
	public void setAllocation(Integer allocation) {
		this.allocation = allocation;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
	public String getEx_key() {
		return ex_key;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public ArrayList<Float> getMarks() {
		return marks;
	}

	public String getAve() {
		return ave;
	}

	public void setEx_key(String ex_key) {
		this.ex_key = ex_key;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public void setMarks(ArrayList<Float> marks) {
		this.marks = marks;
	}
	
	public void setAve(String ave) {
		this.ave = ave;
	}
	public Integer getExam_rank_id() {
		return exam_rank_id;
	}
	public Integer getYear_id() {
		return year_id;
	}
	public void setExam_rank_id(Integer exam_rank_id) {
		this.exam_rank_id = exam_rank_id;
	}
	public void setYear_id(Integer year_id) {
		this.year_id = year_id;
	}
	
	public static RankInfo strJson2RankInfo(String json_str_rankinfo){
		//Convert JSON from String to Object
		ObjectMapper mapper = new ObjectMapper();
		RankInfo rankInfo=null;
		try {
			rankInfo = mapper.readValue(json_str_rankinfo, RankInfo.class);
			return rankInfo;
			
		} catch (IOException e) {
			throw new ESchoolException("Cannot convert STRING to JSON =>" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
