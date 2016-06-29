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
@Table(name="school_year")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class SchoolYear extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="school_id")
	private Integer school_id;

	@Column(name="years")
	private String years;
	
	@Column(name="from_year")
	private Integer from_year;
	
	@Column(name="to_year")
	private Integer to_year;
	
	@Column(name="term_num")
	private Integer term_num;
	
	@Column(name="term_duration")
	private Integer term_duration;
	
	@Column(name="start_dt")
	private String  start_dt;
	
	@Column(name="notice")
	private String notice;

	public Integer getId() {
		return id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public String getYears() {
		return years;
	}

	public Integer getFrom_year() {
		return from_year;
	}

	public Integer getTo_year() {
		return to_year;
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

	public void setYears(String years) {
		this.years = years;
	}

	public void setFrom_year(Integer from_year) {
		this.from_year = from_year;
	}

	public void setTo_year(Integer to_year) {
		this.to_year = to_year;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public Integer getTerm_num() {
		return term_num;
	}

	public Integer getTerm_duration() {
		return term_duration;
	}

	public String getStart_dt() {
		return start_dt;
	}

	public void setTerm_num(Integer term_num) {
		this.term_num = term_num;
	}

	public void setTerm_duration(Integer term_duration) {
		this.term_duration = term_duration;
	}

	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}
	


}
