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
@Table(name="exam_result")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

//@SecondaryTables({
//    @SecondaryTable(name="m_term", pkJoinColumns={
//        @PrimaryKeyJoinColumn(name="id", referencedColumnName="term_id") })
//})
public class ExamResult extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;

	
	@Column(name="school_id")
	private int school_id;

	@Column(name="class_id")
	private int class_id;
	
	@Column(name="exam_type")
	private int exam_type;
	
	@Column(name="exam_dt")
	private String exam_dt;
	

	@Column(name="subject_id")
	private int subject_id;
	
	@Column(name="teacher_id")
	private int teacher_id;
	
	@Column(name="student_id")
	private int student_id;
	
	@Column(name="student_name")
	private String student_name;
	
	@Column(name="notice")
	private String notice;
	
	@Column(name="result_type_id")
	private int result_type_id;
	

	@Column(name="iresult")
	private int iresult;

	@Column(name="fresult")
	private float fresult;
	
	@Column(name="sresult")
	private String sresult;
	
	@Column(name="term_id")
	private int term_id;
//	
//	@Column(table="m_term")
//	private String term;

	public int getId() {
		return id;
	}

	public int getSchool_id() {
		return school_id;
	}

	public int getClass_id() {
		return class_id;
	}

	public int getExam_type() {
		return exam_type;
	}

	public String getExam_dt() {
		return exam_dt;
	}

	public int getSubject_id() {
		return subject_id;
	}

	public int getTeacher_id() {
		return teacher_id;
	}

	public int getStudent_id() {
		return student_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public String getNotice() {
		return notice;
	}

	public int getResult_type_id() {
		return result_type_id;
	}

	public int getIresult() {
		return iresult;
	}

	public float getFresult() {
		return fresult;
	}

	public String getSresult() {
		return sresult;
	}

	public int getTerm_id() {
		return term_id;
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

	public void setExam_type(int exam_type) {
		this.exam_type = exam_type;
	}

	public void setExam_dt(String exam_dt) {
		this.exam_dt = exam_dt;
	}

	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}

	public void setTeacher_id(int teacher_id) {
		this.teacher_id = teacher_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public void setResult_type_id(int result_type_id) {
		this.result_type_id = result_type_id;
	}

	public void setIresult(int iresult) {
		this.iresult = iresult;
	}

	public void setFresult(float fresult) {
		this.fresult = fresult;
	}

	public void setSresult(String sresult) {
		this.sresult = sresult;
	}

	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}
}
