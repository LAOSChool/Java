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
	private int id;

	
	public int getId() {
		return id;
	}

	public int getSchool_id() {
		return school_id;
	}

	public int getClass_id() {
		return class_id;
	}

	public int getTeacher_id() {
		return teacher_id;
	}

	public int getSubject_id() {
		return subject_id;
	}

	public int getSession_id() {
		return session_id;
	}

	public int getWeekday_id() {
		return weekday_id;
	}

	public String getDescription() {
		return description;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setSchool_id(int school_id) {
		this.school_id = school_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public void setTeacher_id(int teacher_id) {
		this.teacher_id = teacher_id;
	}

	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}

	public void setSession_id(int session_id) {
		this.session_id = session_id;
	}

	public void setWeekday_id(int weekday_id) {
		this.weekday_id = weekday_id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	@Column(name="school_id")
	private int school_id;

	@Column(name="class_id")
	private int class_id;
	
	@Column(name="teacher_id")
	private int teacher_id;
	
	@Column(name="subject_id")
	private int subject_id;
	
	@Column(name="session_id")
	private int session_id;
	
	@Column(name="weekday_id")
	private int weekday_id;
	
	

	@Column(name="description")
	private String description;
	
	public int getTerm_val() {
		return term_val;
	}

	public int getYear_id() {
		return year_id;
	}

	public void setTerm_val(int term_val) {
		this.term_val = term_val;
	}

	public void setYear_id(int year_id) {
		this.year_id = year_id;
	}


	@Column(name="term_val")
	private int term_val;
	
	@Column(name="year_id")
	private int year_id;
	 


	@Formula("(SELECT t.sval FROM m_subject t WHERE t.id = subject_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String subject;
	
	@Formula("(SELECT CONCAT(t.sval,'@',t.notice,'@',t.fval1,'@',t.fval2) FROM m_session t WHERE t.id = session_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String session;
	

	@Formula("(SELECT t.sval FROM sys_weekday t WHERE t.id = weekday_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String weekday;
	
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

}
