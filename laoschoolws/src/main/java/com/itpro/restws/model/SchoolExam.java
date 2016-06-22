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
@Table(name="school_exam")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class SchoolExam extends AbstractModel{
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
	


	@Column(name="term_val")
	private Integer term_val;
	
	@Column(name="ex_month")
	private Integer ex_month;
	
	@Column(name="ex_type")
	private Integer ex_type;
	
	@Column(name="ex_name")
	private String ex_name;
	
	
	@Column(name="cls_levels")
	private String cls_levels;
	
	@Column(name="ex_displayname")
	private String ex_displayname;
	
	public Integer getSchool_id() {
		return school_id;
	}

	public Integer getTerm_val() {
		return term_val;
	}

	public Integer getEx_month() {
		return ex_month;
	}

	public Integer getEx_type() {
		return ex_type;
	}

	public String getEx_name() {
		return ex_name;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setTerm_val(Integer term_val) {
		this.term_val = term_val;
	}

	public void setEx_month(Integer ex_month) {
		this.ex_month = ex_month;
	}

	public void setEx_type(Integer ex_type) {
		this.ex_type = ex_type;
	}

	public void setEx_name(String ex_name) {
		this.ex_name = ex_name;
	}

	public String getCls_levels() {
		return cls_levels;
	}

	public void setCls_levels(String cls_levels) {
		this.cls_levels = cls_levels;
	}
	
	
	public String getEx_displayname() {
		return ex_displayname;
	}

	public void setEx_displayname(String ex_displayname) {
		this.ex_displayname = ex_displayname;
	}
	
	
}

