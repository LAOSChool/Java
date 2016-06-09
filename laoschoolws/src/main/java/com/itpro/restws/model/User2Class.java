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
@Table(name="user2class")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class User2Class extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	
	@Column(name="user_id")
	private Integer user_id;

	@Column(name="class_id")
	private Integer class_id;

	@Column(name="user2class_tp")
	private Integer user2class_tp;

	@Column(name="school_id")
	private Integer school_id;

	@Column(name="school_year_id")
	private Integer school_year_id;

	@Column(name="assigned_dt")
	private String assigned_dt;

	@Column(name="closed_dt")
	private String closed_dt;

	@Column(name="closed")
	private Integer closed;

	@Column(name="notice")
	private String notice;

	public Integer getId() {
		return id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public Integer getUser2class_tp() {
		return user2class_tp;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public Integer getSchool_year_id() {
		return school_year_id;
	}

	public String getAssigned_dt() {
		return assigned_dt;
	}

	public String getClosed_dt() {
		return closed_dt;
	}

	public Integer getClosed() {
		return closed;
	}

	public String getNotice() {
		return notice;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public void setUser2class_tp(Integer user2class_tp) {
		this.user2class_tp = user2class_tp;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setSchool_year_id(Integer school_year_id) {
		this.school_year_id = school_year_id;
	}

	public void setAssigned_dt(String assigned_dt) {
		this.assigned_dt = assigned_dt;
	}

	public void setClosed_dt(String closed_dt) {
		this.closed_dt = closed_dt;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	
	
}
