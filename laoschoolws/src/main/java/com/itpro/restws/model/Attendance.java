package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
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

//	@Column(name="user_id")
//	private Integer user_id;
//	
//	@Column(name="user_name")
//	private String user_name;
	
//	@Column(name="absent")
//	private Integer absent;
	
	@Column(name="excused")
	private Integer excused;
	
//	@Column(name="late")
//	private Integer late;
	
	@Column(name="notice")
	private String notice;
	

	
	@Column(name="term_val")
	private Integer term_val;
	
	@Column(name="year_id")
	private Integer year_id;
	

	@Column(name="is_requested")
	private Integer is_requested;

	@Column(name="requested_dt")
	private String requested_dt;
	
	@Column(name="state")
	private Integer state;
	
	@Column(name="auditor")
	private Integer auditor;
	
	
	@Column(name="auditor_name")
	private String auditor_name;	
	
	@Column(name="student_id")
	private Integer student_id;
	
	@Column(name="student_name")
	private String student_name;	
	
	
	
	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}


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


	
//
//	public Integer getAbsent() {
//		return absent;
//	}
//
//
//	public void setAbsent(Integer absent) {
//		this.absent = absent;
//	}


	public Integer getExcused() {
		return excused;
	}


	public void setExcused(Integer excused) {
		this.excused = excused;
	}


//	public Integer getLate() {
//		return late;
//	}
//
//
//	public void setLate(Integer late) {
//		this.late = late;
//	}


	public String getNotice() {
		return notice;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}


	
//	public Integer getUser_id() {
//		return user_id;
//	}
//
//
//	public void setUser_id(Integer user_id) {
//		this.user_id = user_id;
//	}
//
//
//	public String getUser_name() {
//		return user_name;
//	}
//
//
//	public void setUser_name(String user_name) {
//		this.user_name = user_name;
//	}

	
	@Formula("(SELECT t.sval FROM m_subject t WHERE t.id = subject_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String subject;

	@Formula("(SELECT t.sval FROM m_session t WHERE t.id = session_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String session;

	@Formula("(SELECT t.fullname FROM user t WHERE t.id = user_id)")
	private String std_fullname;
	public String getStd_fullname() {
		return std_fullname;
	}
	public void setStd_fullname(String std_fullname) {
		this.std_fullname = std_fullname;
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


	


	public void setSession_id(Integer session_id) {
		this.session_id = session_id;
	}





	public Integer getIs_requested() {
		return is_requested;
	}


	public String getRequested_dt() {
		return requested_dt;
	}


	public void setIs_requested(Integer is_requested) {
		this.is_requested = is_requested;
	}


	public void setRequested_dt(String requested_dt) {
		this.requested_dt = requested_dt;
	}
	public Integer getAuditor() {
		return auditor;
	}


	public String getAuditor_name() {
		return auditor_name;
	}


	public Integer getStudent_id() {
		return student_id;
	}


	public String getStudent_name() {
		return student_name;
	}


	public void setAuditor(Integer auditor) {
		this.auditor = auditor;
	}


	public void setAuditor_name(String auditor_name) {
		this.auditor_name = auditor_name;
	}


	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}


	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}


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


	public Attendance clone(){
		Attendance copy = new Attendance();
		copy.setSchool_id(this.school_id);
		copy.setClass_id(this.class_id);
		copy.setAtt_dt(this.att_dt);
		
		copy.setSubject(this.subject);
		copy.setSubject_id(this.subject_id);
		copy.setSession_id(this.session_id);
		copy.setExcused(this.excused);
		copy.setNotice(this.notice);

		copy.setTerm_val(this.term_val);
		copy.setYear_id(this.year_id);
		copy.setIs_requested(this.is_requested);
		copy.setRequested_dt(this.requested_dt);
		copy.setState(this.state);
		copy.setAuditor(this.auditor);
		
		copy.setAuditor_name(this.auditor_name);
		copy.setStudent_id(this.student_id);
		copy.setStudent_name(this.student_name);
			
		
		return copy;
	}

}
