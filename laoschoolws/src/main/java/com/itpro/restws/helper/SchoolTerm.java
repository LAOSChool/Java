package com.itpro.restws.helper;

public class SchoolTerm {
	
	private Integer school_id;
	private Integer term_val;
	
	private String start_date;
	private Integer start_month;
	private Integer start_year;
	private String end_date;
	private Integer end_month;
	private Integer end_year;
	private Integer year_id;
	public Integer getSchool_id() {
		return school_id;
	}
	public Integer getTerm_val() {
		return term_val;
	}
	
	public Integer getStart_month() {
		return start_month;
	}
	public Integer getStart_year() {
		return start_year;
	}
	
	public Integer getEnd_month() {
		return end_month;
	}
	public Integer getEnd_year() {
		return end_year;
	}
	public Integer getYear_id() {
		return year_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	public void setTerm_val(Integer term_val) {
		this.term_val = term_val;
	}
	
	public void setStart_month(Integer start_month) {
		this.start_month = start_month;
	}
	public void setStart_year(Integer start_year) {
		this.start_year = start_year;
	}
	
	public void setEnd_month(Integer end_month) {
		this.end_month = end_month;
	}
	public void setEnd_year(Integer end_year) {
		this.end_year = end_year;
	}
	public void setYear_id(Integer year_id) {
		this.year_id = year_id;
	}
	public String getStart_date() {
		return start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
}

