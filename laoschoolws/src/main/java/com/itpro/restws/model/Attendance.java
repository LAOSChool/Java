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
	private Integer id;

	
	@Column(name="school_id")
	private Integer school_id;

	@Column(name="class_id")
	private Integer class_id;
	
	@Column(name="att_dt")
	private String att_dt;
	
	@Column(name="subject_id")
	private Integer subject_id;
	
	@Column(name="session_id")
	private Integer session_id;

	@Column(name="user_id")
	private Integer user_id;
	
	@Column(name="user_name")
	private String user_name;
	
	@Column(name="absent")
	private Integer absent;
	
	@Column(name="excused")
	private Integer excused;
	
	@Column(name="late")
	private Integer late;
	
	@Column(name="notice")
	private String notice;
	

	@Column(name="chk_user_id")
	private Integer chk_user_id;


	@Column(name="term_id")
	private Integer term_id;
	
	
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


	public Integer getClass_id() {
		return class_id;
	}


	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}


	public String getAtt_dt() {
		return att_dt;
	}


	public void setAtt_dt(String att_dt) {
		this.att_dt = att_dt;
	}


	public Integer getSubject_id() {
		return subject_id;
	}


	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}


	

	public Integer getAbsent() {
		return absent;
	}


	public void setAbsent(Integer absent) {
		this.absent = absent;
	}


	public Integer getExcused() {
		return excused;
	}


	public void setExcused(Integer excused) {
		this.excused = excused;
	}


	public Integer getLate() {
		return late;
	}


	public void setLate(Integer late) {
		this.late = late;
	}


	public String getNotice() {
		return notice;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}


	public Integer getChk_user_id() {
		return chk_user_id;
	}


	public void setChk_user_id(Integer chk_user_id) {
		this.chk_user_id = chk_user_id;
	}


	public Integer getUser_id() {
		return user_id;
	}


	public void setUser_id(Integer user_id) {
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

	@Formula("(SELECT t.sval FROM m_term t WHERE t.id = term_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String term;
	public String getTerm() {
		return term;
	}
	
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


	public Integer getSession_id() {
		return session_id;
	}


	public Integer getTerm_id() {
		return term_id;
	}




	public void setSession_id(Integer session_id) {
		this.session_id = session_id;
	}


	public void setTerm_id(Integer term_id) {
		this.term_id = term_id;
	}



}
