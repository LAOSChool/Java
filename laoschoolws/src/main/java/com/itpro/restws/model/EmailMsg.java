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
@Table(name="email_msg")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class EmailMsg extends AbstractModel {
	/**
	 * 
	 */


	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	

	@Column(name="school_id")
	private Integer school_id;
	
	

	
	@Column(name="receivers")
	private String receivers;
	
	@Column(name="content")
	private String content;
	
	@Column(name="processed")
	private Integer processed;
	
	@Column(name="processed_dt")
	private String processed_dt;
	

	@Column(name="notice")
	private String notice;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getSchool_id() {
		return school_id;
	}


	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}


	public String getReceivers() {
		return receivers;
	}


	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Integer getProcessed() {
		return processed;
	}


	public void setProcessed(Integer processed) {
		this.processed = processed;
	}


	public String getProcessed_dt() {
		return processed_dt;
	}


	public void setProcessed_dt(String processed_dt) {
		this.processed_dt = processed_dt;
	}


	public String getNotice() {
		return notice;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}
	

}
