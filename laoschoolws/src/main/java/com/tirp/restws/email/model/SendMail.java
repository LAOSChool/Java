package com.tirp.restws.email.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="sendmail")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class SendMail {
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	
	@Column(name="sub")
	private String sub;
	
	@Column(name="body")
	private String body;
	

	
	@Column(name="receivers")
	private String receivers;
	
	@Column(name="date_time")
	private String date_time;
	
	@Column(name="imp_sp")
	private String imp_sp;
	
	@Column(name="imp_id")
	private Integer imp_id;
	
	@Column(name="imp_datetime")
	private String imp_datetime;
	
	@Column(name="processed")
	private Integer processed;
	
	@Column(name="success")
	private Integer success;
	
	@Column(name="description")
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getReceivers() {
		return receivers;
	}

	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

	public String getDate_time() {
		return date_time;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public String getImp_sp() {
		return imp_sp;
	}

	public void setImp_sp(String imp_sp) {
		this.imp_sp = imp_sp;
	}

	public Integer getImp_id() {
		return imp_id;
	}

	public void setImp_id(Integer imp_id) {
		this.imp_id = imp_id;
	}

	public String getImp_datetime() {
		return imp_datetime;
	}

	public void setImp_datetime(String imp_datetime) {
		this.imp_datetime = imp_datetime;
	}

	public Integer getProcessed() {
		return processed;
	}

	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
