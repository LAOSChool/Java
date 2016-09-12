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
@Table(name="firebase_msg")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class FirebaseMsg extends AbstractModel {
	/**
	 * 
	 */


	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="org_tbl")
	private String org_tbl;

	@Column(name="school_id")
	private Integer school_id;
	
	



	@Column(name="org_id")
	private Integer org_id;
	
	
	@Column(name="from_user_id")
	private Integer from_user_id;
	
	@Column(name="from_user_name")
	private String from_user_name;
	
	@Column(name="to_user_id")
	private Integer to_user_id;
	
	@Column(name="to_user_name")
	private String to_user_name;
	
	@Column(name="to_sso_id")
	private String to_sso_id;
	

	@Column(name="title")
	private String title;
	
	@Column(name="content")
	private String content;
	
		
	@Column(name="is_sent")
	private Integer is_sent;
	

	
	@Column(name="sent_dt")
	private String sent_dt;



	
	public Integer getId() {
		return id;
	}



	public String getOrg_tbl() {
		return org_tbl;
	}



	public Integer getOrg_id() {
		return org_id;
	}



	public Integer getFrom_user_id() {
		return from_user_id;
	}



	public String getFrom_user_name() {
		return from_user_name;
	}



	public Integer getTo_user_id() {
		return to_user_id;
	}



	public String getTo_user_name() {
		return to_user_name;
	}



	public String getTo_sso_id() {
		return to_sso_id;
	}



	public String getTitle() {
		return title;
	}



	public String getContent() {
		return content;
	}



	public Integer getIs_sent() {
		return is_sent;
	}



	public String getSent_dt() {
		return sent_dt;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public void setOrg_tbl(String org_tbl) {
		this.org_tbl = org_tbl;
	}



	public void setOrg_id(Integer org_id) {
		this.org_id = org_id;
	}



	public void setFrom_user_id(Integer from_user_id) {
		this.from_user_id = from_user_id;
	}



	public void setFrom_user_name(String from_user_name) {
		this.from_user_name = from_user_name;
	}



	public void setTo_user_id(Integer to_user_id) {
		this.to_user_id = to_user_id;
	}



	public void setTo_user_name(String to_user_name) {
		this.to_user_name = to_user_name;
	}



	public void setTo_sso_id(String to_sso_id) {
		this.to_sso_id = to_sso_id;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public void setIs_sent(Integer is_sent) {
		this.is_sent = is_sent;
	}



	public void setSent_dt(String sent_dt) {
		this.sent_dt = sent_dt;
	}
		

	
	public Integer getSchool_id() {
		return school_id;
	}



	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}	
}
