package com.itpro.restws.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="class")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class EClass extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;

	
	@Column(name="school_id")
	private int school_id;

	@Column(name="title")
	private String title;
	
	@Column(name="location")
	private String location;
	
	@Column(name="term")
	private int term;
	
	@Column(name="years")
	private String years;
	
	@Column(name="start_dt")
	private String start_dt;
	
	@Column(name="end_dt")
	private String end_dt;
	
	@Column(name="class_type")
	private int class_type;
	
	@Column(name="grade_type")
	private int grade_type;
	
	@Column(name="fee")
	private int fee;
	
	@Column(name="sts")
	private int sts;

	@Column(name="head_teacher_id")
	private long head_teacher_id;
//
//	@JsonIgnore
//	 @OneToMany(mappedBy="eclass",targetEntity=User.class, fetch=FetchType.EAGER)
//	 private Set<User> users;	
	

//	public Set<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

//	public Set<User> getUsers() {
//		return users;
//	}

//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}

	public int getSchool_id() {
		return school_id;
	}

	public void setSchool_id(int school_id) {
		this.school_id = school_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getStart_dt() {
		return start_dt;
	}

	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}

	public String getEnd_dt() {
		return end_dt;
	}

	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}

	public int getClass_type() {
		return class_type;
	}

	public void setClass_type(int class_type) {
		this.class_type = class_type;
	}

	public int getGrade_type() {
		return grade_type;
	}

	public void setGrade_type(int grade_type) {
		this.grade_type = grade_type;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getSts() {
		return sts;
	}

	public void setSts(int sts) {
		this.sts = sts;
	}

	public long getHead_teacher_id() {
		return head_teacher_id;
	}

	public void setHead_teacher_id(long head_teacher_id) {
		this.head_teacher_id = head_teacher_id;
	}

}
