package com.itpro.restws.helper;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RankInfo {
	//Ten bai thi: m1,m2...m20
	@JsonIgnore
	String ex_key;
	@JsonIgnore
	Integer user_id;
	// Classify
	@JsonIgnore
	ArrayList<Float> marks;// Danh sach diem tat ca ca mon
	//// Diem trung binh
	Float ave;
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

	public Float getAve() {
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
	
	public void setAve(Float ave) {
		this.ave = ave;
	}

}
