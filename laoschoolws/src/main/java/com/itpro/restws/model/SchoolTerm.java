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
	
	@Column(name="school_id")
	private Integer school_id;

	@Column(name="year_id")
	private Integer year_id;
	
	@Column(name="start_dt")
	private String start_dt;
	
	@Column(name="end_dt")
	private String end_dt;
	
	@Column(name="term_val")
	private Integer term_val;
	
	@Column(name="actived")
	private Integer actived;
	
	
	
	@Column(name="notice")
	private String notice;



	public Integer getId() {
		return id;
	}



	public Integer getSchool_id() {
		return school_id;
	}



	public Integer getYear_id() {
		return year_id;
	}



	public String getStart_dt() {
		return start_dt;
	}



	public String getEnd_dt() {
		return end_dt;
	}



	public Integer getTerm_val() {
		return term_val;
	}



	public Integer getActived() {
		return actived;
	}



	public String getNotice() {
		return notice;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}



	public void setYear_id(Integer year_id) {
		this.year_id = year_id;
	}



	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}



	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}



	public void setTerm_val(Integer term_val) {
		this.term_val = term_val;
	}



	public void setActived(Integer actived) {
		this.actived = actived;
	}



	public void setNotice(String notice) {
		this.notice = notice;
	}

	

}
