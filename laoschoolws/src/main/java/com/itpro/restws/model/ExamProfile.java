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
@Table(name="exam_profile")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class ExamProfile extends AbstractModel{
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
	
	@Column(name="subject_grp_id")
	private Integer subject_grp_id;

	
	@Column(name="subject_grp_name")
	private String subject_grp_name;

	@Column(name="subject_id")
	private Integer subject_id;

	@Column(name="subject_name")
	private String subject_name;

	@Column(name="m9")
	private Float m9;

	@Column(name="m10")
	private Float m10;

	@Column(name="m11")
	private Float m11;

	@Column(name="m12")
	private Float m12;

	@Column(name="ave_m1")
	private Float ave_m1;

	@Column(name="test_term1")
	private Float test_term1;

	@Column(name="ave_term1")
	private Float ave_term1;

	@Column(name="m2")
	private Float m2;

	@Column(name="m3")
	private Float m3;

	@Column(name="m4")
	private Float m4;

	@Column(name="m5")
	private Float m5;

	@Column(name="ave_m2")
	private Float ave_m2;

	@Column(name="test_term2")
	private Float test_term2;

	
	@Column(name="ave_term2")
	private Float ave_term2;

	@Column(name="ave_year")
	private Float ave_year;

	@Column(name="retest")
	private Float retest;

	@Column(name="notice")
	private String notice;

	@Column(name="student_profile_id")
	private Integer student_profile_id;

	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "STOCK_ID", nullable = false)
//	public Stock getStock() {
//		return this.stock;
//	}
//
//	public void setStock(Stock stock) {
//		this.stock = stock;
//	}
	
	
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

	public Integer getSubject_grp_id() {
		return subject_grp_id;
	}

	public String getSubject_grp_name() {
		return subject_grp_name;
	}

	public Integer getSubject_id() {
		return subject_id;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public Float getM9() {
		return m9;
	}

	public Float getM10() {
		return m10;
	}

	public Float getM11() {
		return m11;
	}

	public Float getM12() {
		return m12;
	}

	public Float getAve_m1() {
		return ave_m1;
	}

	public Float getTest_term1() {
		return test_term1;
	}

	public Float getAve_term1() {
		return ave_term1;
	}

	public Float getM2() {
		return m2;
	}

	public Float getM3() {
		return m3;
	}

	public Float getM4() {
		return m4;
	}

	public Float getM5() {
		return m5;
	}

	public Float getAve_m2() {
		return ave_m2;
	}

	public Float getTest_term2() {
		return test_term2;
	}

	public Float getAve_term2() {
		return ave_term2;
	}

	public Float getAve_year() {
		return ave_year;
	}

	public Float getRetest() {
		return retest;
	}

	public String getNotice() {
		return notice;
	}

	public Integer getStudent_profile_id() {
		return student_profile_id;
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

	public void setSubject_grp_id(Integer subject_grp_id) {
		this.subject_grp_id = subject_grp_id;
	}

	public void setSubject_grp_name(String subject_grp_name) {
		this.subject_grp_name = subject_grp_name;
	}

	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public void setM9(Float m9) {
		this.m9 = m9;
	}

	public void setM10(Float m10) {
		this.m10 = m10;
	}

	public void setM11(Float m11) {
		this.m11 = m11;
	}

	public void setM12(Float m12) {
		this.m12 = m12;
	}

	public void setAve_m1(Float ave_m1) {
		this.ave_m1 = ave_m1;
	}

	public void setTest_term1(Float test_term1) {
		this.test_term1 = test_term1;
	}

	public void setAve_term1(Float ave_term1) {
		this.ave_term1 = ave_term1;
	}

	public void setM2(Float m2) {
		this.m2 = m2;
	}

	public void setM3(Float m3) {
		this.m3 = m3;
	}

	public void setM4(Float m4) {
		this.m4 = m4;
	}

	public void setM5(Float m5) {
		this.m5 = m5;
	}

	public void setAve_m2(Float ave_m2) {
		this.ave_m2 = ave_m2;
	}

	public void setTest_term2(Float test_term2) {
		this.test_term2 = test_term2;
	}

	public void setAve_term2(Float ave_term2) {
		this.ave_term2 = ave_term2;
	}

	public void setAve_year(Float ave_year) {
		this.ave_year = ave_year;
	}

	public void setRetest(Float retest) {
		this.retest = retest;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public void setStudent_profile_id(Integer student_profile_id) {
		this.student_profile_id = student_profile_id;
	}

	
}
