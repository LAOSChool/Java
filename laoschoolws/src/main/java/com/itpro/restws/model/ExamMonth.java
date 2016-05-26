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
@Table(name="exam_month")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class ExamMonth extends AbstractModel{
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
	

	@Column(name="term_id")
	private Integer term_id;
	
	@Column(name="term_name")
	private String term_name;
	
	@Column(name="ex_month")
	private Integer ex_month;
	
	@Column(name="ex_year")
	private Integer ex_year;
	
	@Column(name="ex_type")
	private Integer ex_type;
	
	@Column(name="notice")
	private String notice;
	public Integer getSchool_id() {
		return school_id;
	}

	public Integer getTerm_id() {
		return term_id;
	}

	public String getTerm_name() {
		return term_name;
	}

	public Integer getEx_month() {
		return ex_month;
	}

	public Integer getEx_year() {
		return ex_year;
	}

	public Integer getEx_type() {
		return ex_type;
	}

	public String getNotice() {
		return notice;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setTerm_id(Integer term_id) {
		this.term_id = term_id;
	}

	public void setTerm_name(String term_name) {
		this.term_name = term_name;
	}

	public void setEx_month(Integer ex_month) {
		this.ex_month = ex_month;
	}

	public void setEx_year(Integer ex_year) {
		this.ex_year = ex_year;
	}

	public void setEx_type(Integer ex_type) {
		this.ex_type = ex_type;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	
	
}

