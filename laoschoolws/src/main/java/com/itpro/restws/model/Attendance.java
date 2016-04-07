package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="attendance")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class Attendance extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;

	
	@Column(name="school_id")
	private int school_id;

	@Column(name="class_id")
	private int class_id;
	
	@Column(name="att_dt")
	private String att_dt;
	
	@Column(name="subject_id")
	private int subject_id;
	
	@Column(name="session_id")
	private int session_id;

	@Column(name="user_id")
	private int user_id;
	
	@Column(name="user_name")
	private String user_name;
	
	@Column(name="absent")
	private int absent;
	
	@Column(name="excused")
	private int excused;
	
	@Column(name="late")
	private int late;
	
	@Column(name="notice")
	private String notice;
	

	@Column(name="chk_user_id")
	private int chk_user_id;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getSchool_id() {
		return school_id;
	}


	public void setSchool_id(int school_id) {
		this.school_id = school_id;
	}


	public int getClass_id() {
		return class_id;
	}


	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}


	public String getAtt_dt() {
		return att_dt;
	}


	public void setAtt_dt(String att_dt) {
		this.att_dt = att_dt;
	}


	public int getSubject_id() {
		return subject_id;
	}


	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}


	

	public int getAbsent() {
		return absent;
	}


	public void setAbsent(int absent) {
		this.absent = absent;
	}


	public int getExcused() {
		return excused;
	}


	public void setExcused(int excused) {
		this.excused = excused;
	}


	public int getLate() {
		return late;
	}


	public void setLate(int late) {
		this.late = late;
	}


	public String getNotice() {
		return notice;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}


	public int getChk_user_id() {
		return chk_user_id;
	}


	public void setChk_user_id(int chk_user_id) {
		this.chk_user_id = chk_user_id;
	}


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public String getUser_name() {
		return user_name;
	}


	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	
	@Formula("(SELECT t.sval FROM m_subject t WHERE t.id = subject_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String subject;

	@Formula("(SELECT t.sval FROM m_session t WHERE t.id = session_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String session;
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}

}
