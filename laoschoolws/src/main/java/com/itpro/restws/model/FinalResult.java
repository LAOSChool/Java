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
	private int id;

	
	@Column(name="school_id")
	private int school_id;

	@Column(name="class_id")
	private int class_id;
	
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
	
	@Column(name="term_id")
	private int term_id;

	
	@Column(name="start_dt")
	private String start_dt;
	
	@Column(name="end_dt")
	private String end_dt;
	
	
	@Column(name="average_score")
	private float average_score;
	


	@Column(name="bonus_score")
	private float bonus_score;

	
	
	@Column(name="final_result")
	private float final_result;
	
	
	

	@Column(name="pass")
	private int pass;

	@Column(name="rank_id")
	private int rank_id;
	
	@Column(name="absent")
	private float absent;
	

	@Column(name="excused")
	private float excused;
	

	@Column(name="late")
	private float late;


	public int getId() {
		return id;
	}


	public int getSchool_id() {
		return school_id;
	}


	public int getClass_id() {
		return class_id;
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


	public int getTerm_id() {
		return term_id;
	}


	public String getStart_dt() {
		return start_dt;
	}


	public String getEnd_dt() {
		return end_dt;
	}


	public float getAverage_score() {
		return average_score;
	}


	public float getBonus_score() {
		return bonus_score;
	}


	public float getFinal_result() {
		return final_result;
	}


	public int getPass() {
		return pass;
	}


	public int getRank_id() {
		return rank_id;
	}


	public float getAbsent() {
		return absent;
	}


	public float getExcused() {
		return excused;
	}


	public float getLate() {
		return late;
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


	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}


	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}


	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}


	public void setAverage_score(float average_score) {
		this.average_score = average_score;
	}


	public void setBonus_score(float bonus_score) {
		this.bonus_score = bonus_score;
	}


	public void setFinal_result(float final_result) {
		this.final_result = final_result;
	}


	public void setPass(int pass) {
		this.pass = pass;
	}


	public void setRank_id(int rank_id) {
		this.rank_id = rank_id;
	}


	public void setAbsent(float absent) {
		this.absent = absent;
	}


	public void setExcused(float excused) {
		this.excused = excused;
	}


	public void setLate(float late) {
		this.late = late;
	}
	
	
//	@Column(name="term_id")
//	private int term_id;
//	
//	@Column(table="m_term")
//	private String term;
}
