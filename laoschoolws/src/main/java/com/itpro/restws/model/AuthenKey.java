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
@Table(name="authen_key")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class AuthenKey extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;

	
	@Column(name="auth_key")
	private String auth_key;

	@Column(name="sso_id")
	private String sso_id;
	
	@JsonIgnore
	@Column(name="expired_dt")
	private String expired_dt;

	public int getId() {
		return id;
	}

	public String getAuth_key() {
		return auth_key;
	}

	public String getSso_id() {
		return sso_id;
	}

	public String getExpired_dt() {
		return expired_dt;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAuth_key(String auth_key) {
		this.auth_key = auth_key;
	}

	public void setSso_id(String sso_id) {
		this.sso_id = sso_id;
	}

	public void setExpired_dt(String expired_dt) {
		this.expired_dt = expired_dt;
	}
	

}
