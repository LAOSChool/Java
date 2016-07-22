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
@Table(name="timetable")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

  
public class Timetable extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	
	public Integer getId() {
		return id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public Integer getTeacher_id() {
		return teacher_id;
	}

	public Integer getSubject_id() {
		return subject_id;
	}

	public Integer getSession_id() {
		return session_id;
	}

	public Integer getWeekday_id() {
		return weekday_id;
	}

	public String getDescription() {
		return description;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public void setTeacher_id(Integer teacher_id) {
		this.teacher_id = teacher_id;
	}

	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}

	public void setSession_id(Integer session_id) {
		this.session_id = session_id;
	}

	public void setWeekday_id(Integer weekday_id) {
		this.weekday_id = weekday_id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	@Column(name="school_id")
	private Integer school_id;

	@Column(name="class_id")
	private Integer class_id;
	
	@Column(name="teacher_id")
	private Integer teacher_id;
	
	@Column(name="subject_id")
	private Integer subject_id;
	
	@Column(name="session_id")
	private Integer session_id;
	
	@Column(name="weekday_id")
	private Integer weekday_id;
	
	

	@Column(name="description")
	private String description;
	
	public Integer getTerm_val() {
		return term_val;
	}

	public Integer getYear_id() {
		return year_id;
	}

	public void setTerm_val(Integer term_val) {
		this.term_val = term_val;
	}

	public void setYear_id(Integer year_id) {
		this.year_id = year_id;
	}


	@Column(name="term_val")
	private Integer term_val;
	
	@Column(name="year_id")
	private Integer year_id;
	 


	@Formula("(SELECT t.sval FROM m_subject t WHERE t.id = subject_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String subject;
	
	@Formula("(SELECT t.lval FROM m_subject t WHERE t.id = subject_id)") // Ten tieng Lao
	private String subject_lao;
	
	
	public String getSubject_lao() {
		return subject_lao;
	}

	public void setSubject_lao(String subject_lao) {
		this.subject_lao = subject_lao;
	}



	@Formula("(SELECT CONCAT(t.sval,'@',t.notice,'@',t.fval1,'@',t.fval2) FROM m_session t WHERE t.id = session_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String session;
	

	@Formula("(SELECT t.sval FROM sys_weekday t WHERE t.id = weekday_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String weekday;
	
	@Formula("(SELECT t.lval FROM sys_weekday t WHERE t.id = weekday_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String weekday_laos;
	
	
	// Teacher Name
	@Formula("(SELECT t.fullname FROM user t WHERE t.id = teacher_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String teacher_name;
	
	
	public String getTeacher_name() {
		return teacher_name;
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

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getWeekday_laos() {
		return weekday_laos;
	}

	public void setWeekday_laos(String weekday_laos) {
		this.weekday_laos = weekday_laos;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

}
