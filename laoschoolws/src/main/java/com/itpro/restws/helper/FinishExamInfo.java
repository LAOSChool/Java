package com.itpro.restws.helper;

public class FinishExamInfo {
	Integer school_id;
	Integer class_id;
	String class_title;
	
	Integer ex_id;
	String ex_key;
	String ex_name;
	
	public String getEx_name() {
		return ex_name;
	}
	public void setEx_name(String ex_name) {
		this.ex_name = ex_name;
	}
	Integer subject_id;
	String subject_name;
	
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	public Integer getClass_id() {
		return class_id;
	}
	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}
	public String getClass_title() {
		return class_title;
	}
	public void setClass_title(String class_title) {
		this.class_title = class_title;
	}
	public Integer getEx_id() {
		return ex_id;
	}
	public void setEx_id(Integer ex_id) {
		this.ex_id = ex_id;
	}
	public String getEx_key() {
		return ex_key;
	}
	public void setEx_key(String ex_key) {
		this.ex_key = ex_key;
	}
	public Integer getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	
}
