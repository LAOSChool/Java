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
@Table(name="action_log")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class ActionLog extends AbstractModel{
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
	
	@Column(name="request_dt")
	private String request_dt;
	
	@Column(name="request_url")
	private String request_url;
	
	@Column(name="request_method")
	private String request_method;
	
	@Column(name="request_data")
	private String request_data;
	
	@Column(name="resp_status")
	private Integer resp_status;
	
//	@Column(name="resp_data")
//	private String resp_data;
//	
	

	@Column(name="resp_dt")
	private String resp_dt;
	
	@Column(name="exec_duration")
	private long exec_duration;

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

	public String getRequest_dt() {
		return request_dt;
	}

	public String getRequest_url() {
		return request_url;
	}

	public String getRequest_method() {
		return request_method;
	}

	public String getRequest_data() {
		return request_data;
	}

	public Integer getResp_status() {
		return resp_status;
	}

//	public String getResp_data() {
//		return resp_data;
//	}

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

	public void setRequest_dt(String request_dt) {
		this.request_dt = request_dt;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public void setRequest_method(String request_method) {
		this.request_method = request_method;
	}

	public void setRequest_data(String request_data) {
		this.request_data = request_data;
	}

	public void setResp_status(Integer resp_status) {
		this.resp_status = resp_status;
	}

//	public void setResp_data(String resp_data) {
//		this.resp_data = resp_data;
//	}
	public String getResp_dt() {
		return resp_dt;
	}

	public long getExec_duration() {
		return exec_duration;
	}

	public void setResp_dt(String resp_dt) {
		this.resp_dt = resp_dt;
	}

	public void setExec_duration(long exec_duration) {
		this.exec_duration = exec_duration;
	}	
}
