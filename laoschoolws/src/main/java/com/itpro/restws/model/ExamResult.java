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


//
//@NamedNativeQuery(
//	name = "call_sp_get_exam_result",
//	query = "CALL sp_get_exam_result(:p_school_id,:p_class_id,:p_student_id,:p_year_id)",
//	callable=true, 
//	readOnly=true, 
//	resultClass = ExamResult.class
//)


@Entity
@Table(name="exam_result")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class ExamResult extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	
	@Column(name="school_id")
	private Integer school_id;

	@Column(name="class_id")
	private Integer class_id;
	
	@Column(name="exam_id")
	private Integer exam_id;
	
	@Column(name="exam_name")
	private String exam_name;
	
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
	
	@Column(name="sresult")
	private String sresult;
	
	@Column(name="term_id")
	private Integer term_id;

	@Column(name="exam_month")
	private Integer exam_month;
	
	
	@Column(name="exam_year")
	private Integer exam_year;

	
	@Column(name="term_val")
	private Integer term_val;
	
	
	@Column(name="sch_year_id")
	private Integer sch_year_id;
	
	
	
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

	public String getSresult() {
		return sresult;
	}

	public Integer getTerm_id() {
		return term_id;
	}

	public Integer getExam_id() {
		return exam_id;
	}

	public String getExam_name() {
		return exam_name;
	}

	public Integer getTerm_val() {
		return term_val;
	}

	public Integer getSch_year_id() {
		return sch_year_id;
	}

	public void setExam_id(Integer exam_id) {
		this.exam_id = exam_id;
	}

	public void setExam_name(String exam_name) {
		this.exam_name = exam_name;
	}

	public void setTerm_val(Integer term_val) {
		this.term_val = term_val;
	}

	public void setSch_year_id(Integer sch_year_id) {
		this.sch_year_id = sch_year_id;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
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
	
	@Formula("(SELECT t.photo FROM user t WHERE t.id = student_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String std_photo;
	
	@Formula("(SELECT t.nickname FROM user t WHERE t.id = student_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String std_nickname;
	
	
	@Formula("(SELECT t.term_name FROM school_term t WHERE t.id = term_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
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

	public String getStd_photo() {
		return std_photo;
	}

	public String getStd_nickname() {
		return std_nickname;
	}

	

	public void setStd_photo(String std_photo) {
		this.std_photo = std_photo;
	}

	public void setStd_nickname(String std_nickname) {
		this.std_nickname = std_nickname;
	}

	
}
