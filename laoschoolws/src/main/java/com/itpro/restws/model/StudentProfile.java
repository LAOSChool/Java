package com.itpro.restws.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="student_profile")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

//@SecondaryTables({
//    @SecondaryTable(name="m_term", pkJoinColumns={
//        @PrimaryKeyJoinColumn(name="id", referencedColumnName="term_id") })
//})
public class StudentProfile extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	
	@Column(name="school_id")
	private Integer school_id;

	@Column(name="class_id")
	private Integer class_id;
	
	
	@Column(name="teacher_id")
	private Integer teacher_id;
	
	@Column(name="student_id")
	private Integer student_id;
	
	
	@Column(name="school_year")
	private Integer school_year;
	
	@Column(name="day_absents")
	private Integer day_absents;

	
	@Column(name="notice")
	private String notice;
	

	@Column(name="eval_dt")
	private String eval_dt;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stock")
//	public Set<StockDailyRecord> getStockDailyRecords() {
//		return this.stockDailyRecords;
//	}
//
//	public void setStockDailyRecords(Set<StockDailyRecord> stockDailyRecords) {
//		this.stockDailyRecords = stockDailyRecords;
//	}
//	
	
	@NotFound(action=NotFoundAction.IGNORE)
	@OneToMany (fetch = FetchType.EAGER)
	@JoinColumn(name="student_profile_id") 
	private Set<ExamProfile> examProfiles = new HashSet<ExamProfile>(0);
	public Set<ExamProfile> getExamProfiles() {
		return this.examProfiles;
	}

	public void setExamProfiles(Set<ExamProfile> exams) {
		this.examProfiles = exams;
	}

	
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


	public Integer getStudent_id() {
		return student_id;
	}


	public Integer getSchool_year() {
		return school_year;
	}


	public Integer getDay_absents() {
		return day_absents;
	}


	public String getNotice() {
		return notice;
	}


	public String getEval_dt() {
		return eval_dt;
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


	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}


	public void setSchool_year(Integer school_year) {
		this.school_year = school_year;
	}


	public void setDay_absents(Integer day_absents) {
		this.day_absents = day_absents;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}


	public void setEval_dt(String eval_dt) {
		this.eval_dt = eval_dt;
	}

	
}
