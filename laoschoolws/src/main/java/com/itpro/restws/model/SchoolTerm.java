package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="school_term")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class SchoolTerm extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="school_id")
	private Integer school_id;
	

	@Column(name="term_name")
	private String term_name;
	
	@Column(name="start_date")
	private Integer start_date;
	
	@Column(name="start_month")
	private Integer start_month;
	
	@Column(name="start_year")
	private Integer start_year;
	
	@Column(name="end_date")
	private Integer end_date;
	
	@Column(name="end_month")
	private Integer end_month;
	
	@Column(name="end_year")
	private Integer end_year;
	
	@Column(name="notice")
	private String notice;
	
	@Column(name="school_year_id")
	private Integer school_year_id;
	
	
	public Integer getSchool_id() {
		return school_id;
	}

	public String getTerm_name() {
		return term_name;
	}

	public Integer getStart_date() {
		return start_date;
	}

	public Integer getStart_month() {
		return start_month;
	}

	public Integer getStart_year() {
		return start_year;
	}

	public Integer getEnd_date() {
		return end_date;
	}

	public Integer getEnd_month() {
		return end_month;
	}

	public Integer getEnd_year() {
		return end_year;
	}

	public String getNotice() {
		return notice;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setTerm_name(String term_name) {
		this.term_name = term_name;
	}

	public void setStart_date(Integer start_date) {
		this.start_date = start_date;
	}

	public void setStart_month(Integer start_month) {
		this.start_month = start_month;
	}

	public void setStart_year(Integer start_year) {
		this.start_year = start_year;
	}

	public void setEnd_date(Integer end_date) {
		this.end_date = end_date;
	}

	public void setEnd_month(Integer end_month) {
		this.end_month = end_month;
	}

	public void setEnd_year(Integer end_year) {
		this.end_year = end_year;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public Integer getSchool_year_id() {
		return school_year_id;
	}

	public void setSchool_year_id(Integer school_year) {
		this.school_year_id = school_year;
	}
	
	
}

