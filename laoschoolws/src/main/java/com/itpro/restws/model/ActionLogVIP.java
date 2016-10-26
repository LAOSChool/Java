package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="action_log_imp")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class ActionLogVIP extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="school_id")
	private Integer school_id;
	
	@Column(name="sso_id")
	private String sso_id;
	
	@Column(name="user_role")
	private String user_role;
	
	@Column(name="full_name")
	private String full_name;
	
	@Column(name="act_type")
	private String act_type;
	
	@Column(name="content")
	private String content;
	
	@Column(name="request_dt")
	private String request_dt;
	
	@JsonIgnore
	@Column(name="str_json")
	private String str_json;
	
	@JsonIgnore
	@Column(name="notice")
	private String notice;
	

	public Integer getId() {
		return id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public String getSso_id() {
		return sso_id;
	}

	public String getUser_role() {
		return user_role;
	}

	public String getFull_name() {
		return full_name;
	}

	public String getAct_type() {
		return act_type;
	}

	public String getContent() {
		return content;
	}

	public String getRequest_dt() {
		return request_dt;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setSso_id(String sso_id) {
		this.sso_id = sso_id;
	}

	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public void setAct_type(String act_type) {
		this.act_type = act_type;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setRequest_dt(String date_time) {
		this.request_dt = date_time;
	}

	public String getStr_json() {
		return str_json;
	}

	public void setStr_json(String str_json) {
		this.str_json = str_json;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
}
