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
@Table(name="final_result")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

//@SecondaryTables({
//    @SecondaryTable(name="m_term", pkJoinColumns={
//        @PrimaryKeyJoinColumn(name="id", referencedColumnName="term_id") })
//})
public class FinalResult extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	
	@Column(name="school_id")
	private Integer school_id;

	@Column(name="class_id")
	private Integer class_id;
	
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
	
	@Column(name="term_id")
	private Integer term_id;

	
	@Column(name="start_dt")
	private String start_dt;
	
	@Column(name="end_dt")
	private String end_dt;
	
	
	@Column(name="average_score")
	private Float average_score;
	


	@Column(name="bonus_score")
	private Float bonus_score;

	
	
	@Column(name="final_result")
	private Float final_result;
	
	
	

	@Column(name="pass")
	private Integer pass;

	@Column(name="rank_id")
	private Integer rank_id;
	
	@Column(name="absent")
	private Float absent;
	

	@Column(name="excused")
	private Float excused;
	

	@Column(name="late")
	private Float late;


	public Integer getId() {
		return id;
	}


	public Integer getSchool_id() {
		return school_id;
	}


	public Integer getClass_id() {
		return class_id;
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


	public Integer getTerm_id() {
		return term_id;
	}


	public String getStart_dt() {
		return start_dt;
	}


	public String getEnd_dt() {
		return end_dt;
	}


	public Float getAverage_score() {
		return average_score;
	}


	public Float getBonus_score() {
		return bonus_score;
	}


	public Float getFinal_result() {
		return final_result;
	}


	public Integer getPass() {
		return pass;
	}


	public Integer getRank_id() {
		return rank_id;
	}


	public Float getAbsent() {
		return absent;
	}


	public Float getExcused() {
		return excused;
	}


	public Float getLate() {
		return late;
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


	public void setTerm_id(Integer term_id) {
		this.term_id = term_id;
	}


	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}


	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}


	public void setAverage_score(Float average_score) {
		this.average_score = average_score;
	}


	public void setBonus_score(Float bonus_score) {
		this.bonus_score = bonus_score;
	}


	public void setFinal_result(Float final_result) {
		this.final_result = final_result;
	}


	public void setPass(Integer pass) {
		this.pass = pass;
	}


	public void setRank_id(Integer rank_id) {
		this.rank_id = rank_id;
	}


	public void setAbsent(Float absent) {
		this.absent = absent;
	}


	public void setExcused(Float excused) {
		this.excused = excused;
	}


	public void setLate(Float late) {
		this.late = late;
	}
	
	
//	@Column(name="term_id")
//	private Integer term_id;
//	
//	@Column(table="m_term")
//	private String term;
}
