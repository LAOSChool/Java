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
	private int id;

	
	public int getId() {
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

	public void setId(int id) {
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

	@Column(name="api_key")
	private String api_key;

	
	@Column(name="last_request_dt")
	private String last_request_dt;
	
	@Column(name="first_request_dt")
	private String first_request_dt;

}
