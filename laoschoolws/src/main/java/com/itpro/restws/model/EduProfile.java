package com.itpro.restws.model;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="edu_profile")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class EduProfile extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	
	@Column(name="school_id")
	private Integer school_id;
	
	
	@Column(name="school_name")
	private String school_name;
	
	@Column(name="cls_id")
	private Integer cls_id;
	
	@Column(name="cls_name")
	private String cls_name;
	
	@Column(name="cls_level")
	private Integer cls_level;
	
	@Column(name="cls_location")
	private String cls_location;
	
	@Column(name="teacher_id")
	private Integer teacher_id;
	
	@Column(name="teacher_name")
	private String teacher_name;
	
	@Column(name="student_id")
	private Integer student_id;
	
	@Column(name="student_name")
	private String student_name;
	
	@Column(name="school_year_id")
	private Integer school_year_id;
	
	@Column(name="school_years")
	private String school_years;
	
	@Column(name="day_absents")
	private Integer day_absents;
	
	
	
	@Column(name="passed")
	private Integer passed;
	
	@Column(name="eval_dt")
	private String eval_dt;
	
	

	@Column(name="start_dt")
	private String start_dt;
	
	
	@Column(name="closed_dt")
	private String closed_dt;
	
	@Column(name="closed")
	private Integer closed;
	
	
	@Column(name="notice")
	private String notice;
	
		
//	@NotFound(action=NotFoundAction.IGNORE)
//	@OneToMany (fetch = FetchType.EAGER)
//	@JoinColumn(name="profile_id") 
	@Transient
	private ArrayList<ExamResult> exam_results;// = new ArrayList<ExamResult>(0);
	public ArrayList<ExamResult> getExam_results() {
		return this.exam_results;
	}

	public void setExam_results(ArrayList<ExamResult> results) {
		this.exam_results= results;
	}

	public Integer getId() {
		return id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public String getSchool_name() {
		return school_name;
	}

	public Integer getCls_id() {
		return cls_id;
	}

	public String getCls_name() {
		return cls_name;
	}

	public Integer getCls_level() {
		return cls_level;
	}

	public String getCls_location() {
		return cls_location;
	}

	public Integer getTeacher_id() {
		return teacher_id;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public Integer getSchool_year_id() {
		return school_year_id;
	}

	public String getSchool_years() {
		return school_years;
	}

	public Integer getDay_absents() {
		return day_absents;
	}

	public Integer getPassed() {
		return passed;
	}

	public String getEval_dt() {
		return eval_dt;
	}

	public String getStart_dt() {
		return start_dt;
	}

	public String getClosed_dt() {
		return closed_dt;
	}

	public Integer getClosed() {
		return closed;
	}

	public String getNotice() {
		return notice;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public void setCls_id(Integer cls_id) {
		this.cls_id = cls_id;
	}

	public void setCls_name(String cls_name) {
		this.cls_name = cls_name;
	}

	public void setCls_level(Integer cls_level) {
		this.cls_level = cls_level;
	}

	public void setCls_location(String cls_location) {
		this.cls_location = cls_location;
	}

	public void setTeacher_id(Integer teacher_id) {
		this.teacher_id = teacher_id;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public void setSchool_year_id(Integer school_year_id) {
		this.school_year_id = school_year_id;
	}

	public void setSchool_years(String school_years) {
		this.school_years = school_years;
	}

	public void setDay_absents(Integer day_absents) {
		this.day_absents = day_absents;
	}

	public void setPassed(Integer passed) {
		this.passed = passed;
	}

	public void setEval_dt(String eval_dt) {
		this.eval_dt = eval_dt;
	}

	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}

	public void setClosed_dt(String closed_dt) {
		this.closed_dt = closed_dt;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	@Formula("(SELECT t.fullname FROM user t WHERE t.id = student_id)")
	private String std_fullname;
	public String getStd_fullname() {
		return std_fullname;
	}
	public void setStd_fullname(String std_fullname) {
		this.std_fullname = std_fullname;
	}
	
	}
