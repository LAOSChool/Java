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
	private Integer id;

	
	@Column(name="school_id")
	private Integer school_id;

	@Column(name="class_id")
	private Integer class_id;
	
	@Column(name="exam_type")
	private Integer exam_type;
	
	@Column(name="exam_dt")
	private String exam_dt;
	

	@Column(name="subject_id")
	private Integer subject_id;
	
	@Column(name="teacher_id")
	private Integer teacher_id;
	
	@Column(name="student_id")
	private Integer student_id;
	
	@Column(name="student_name")
	private String student_name;
	
	@Column(name="notice")
	private String notice;
	
//	@Column(name="result_type_id")
//	private Integer result_type_id;
	

//	@Column(name="iresult")
//	private Integer iresult;
//
//	@Column(name="fresult")
//	private Float fresult;
//	
	@Column(name="sresult")
	private String sresult;
	
	@Column(name="term_id")
	private Integer term_id;
//	
//	@Column(table="m_term")
//	private String term;

	@Column(name="exam_month")
	private Integer exam_month;
	
	
	@Column(name="exam_year")
	private Integer exam_year;

	
//	@OneToOne (cascade = CascadeType.ALL,optional = true)
//	@JoinColumn(name="exam_time_id")
//	private ExamTime examtime;
//	
//	
//	public ExamTime getExamtime() {
//		return examtime;
//	}
//
//	public void setExamtime(ExamTime examtime) {
//		this.examtime = examtime;
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

	public Integer getExam_type() {
		return exam_type;
	}

	public String getExam_dt() {
		return exam_dt;
	}

	public Integer getSubject_id() {
		return subject_id;
	}

	public Integer getTeacher_id() {
		return teacher_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public String getNotice() {
		return notice;
	}

//	public Integer getResult_type_id() {
//		return result_type_id;
//	}
//
//	public Integer getIresult() {
//		return iresult;
//	}
//
//	public Float getFresult() {
//		return fresult;
//	}

	public String getSresult() {
		return sresult;
	}

	public Integer getTerm_id() {
		return term_id;
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

	public void setExam_type(Integer exam_type) {
		this.exam_type = exam_type;
	}

	public void setExam_dt(String exam_dt) {
		this.exam_dt = exam_dt;
	}

	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}

	public void setTeacher_id(Integer teacher_id) {
		this.teacher_id = teacher_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

//	public void setResult_type_id(Integer result_type_id) {
//		this.result_type_id = result_type_id;
//	}
//
//	public void setIresult(Integer iresult) {
//		this.iresult = iresult;
//	}
//
//	public void setFresult(Float fresult) {
//		this.fresult = fresult;
//	}

	public void setSresult(String sresult) {
		this.sresult = sresult;
	}

	public void setTerm_id(Integer term_id) {
		this.term_id = term_id;
	}
	
	
	
	
	@Formula("(SELECT t.sval FROM m_subject t WHERE t.id = subject_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String subject;

	public void setSubject(String subject) {
		this.subject = subject;
	}




	@Formula("(SELECT t.fullname FROM user t WHERE t.id = teacher_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String teacher;

	@Formula("(SELECT t.sval FROM m_term t WHERE t.id = term_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String term;

	public void setTerm(String term) {
		this.term = term;
	}

	public String getSubject() {
		return subject;
	}

	public String getTeacher() {
		return teacher;
	}

	public String getTerm() {
		return term;
	}


	public Integer getExam_month() {
		return exam_month;
	}

	public Integer getExam_year() {
		return exam_year;
	}

	public void setExam_month(Integer exam_month) {
		this.exam_month = exam_month;
	}

	public void setExam_year(Integer exam_year) {
		this.exam_year = exam_year;
	}
	
}
