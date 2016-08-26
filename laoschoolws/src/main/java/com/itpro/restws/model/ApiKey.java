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
@Table(name="api_key")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class ApiKey extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="api_key")
	private String api_key;

	
	@Column(name="last_request_dt")
	private String last_request_dt;
	
	@Column(name="first_request_dt")
	private String first_request_dt;
	
	@Column(name="sso_id")
	private String sso_id;
	
	@Column(name="cld_token")
	private String cld_token;
	
	
	@Column(name="notice")
	private String notice;
	
	@Column(name="auth_key")
	private String auth_key;
	
	@Column(name="active")
	private Integer active;
	
	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getAuth_key() {
		return auth_key;
	}

	public void setAuth_key(String auth_key) {
		this.auth_key = auth_key;
	}

	public Integer getId() {
		return id;
	}

	public String getApi_key() {
		return api_key;
	}

	public String getLast_request_dt() {
		return last_request_dt;
	}

	public String getFirst_request_dt() {
		return first_request_dt;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	public void setLast_request_dt(String last_request_dt) {
		this.last_request_dt = last_request_dt;
	}

	public void setFirst_request_dt(String first_request_dt) {
		this.first_request_dt = first_request_dt;
	}

	public String getSso_id() {
		return sso_id;
	}

	public String getCld_token() {
		return cld_token;
	}

	
	public String getNotice() {
		return notice;
	}

	

	public void setSso_id(String sso_id) {
		this.sso_id = sso_id;
	}

	public void setCld_token(String cld_token) {
		this.cld_token = cld_token;
	}



	public void setNotice(String notice) {
		this.notice = notice;
	}

	
	

}
