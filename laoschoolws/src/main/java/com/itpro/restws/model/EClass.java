package com.itpro.restws.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
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
	private Integer id;

	
	@Column(name="school_id")
	private Integer school_id;

	@Column(name="title")
	private String title;
	
	@Column(name="location")
	private String location;
	
	@Column(name="term")
	private Integer term;
	
	@Column(name="years")
	private String years;
	
	@Column(name="start_dt")
	private String start_dt;
	
	@Column(name="end_dt")
	private String end_dt;
	
	@Column(name="class_type")
	private Integer class_type;
	
	@Column(name="grade_type")
	private Integer grade_type;
	
	@Column(name="fee")
	private Integer fee;
	
	@Column(name="sts")
	private Integer sts;

	@Column(name="head_teacher_id")
	private Integer head_teacher_id;

	@Column(name="level")
	private Integer level;
	
	@Column(name="year_id")
	private Integer year_id;
	
	public Integer getYear_id() {
		return year_id;
	}
	public void setYear_id(Integer year_id) {
		this.year_id = year_id;
	}

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY,mappedBy = "classes")
	private Set<User> users;
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
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

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
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

	public Integer getClass_type() {
		return class_type;
	}

	public void setClass_type(Integer class_type) {
		this.class_type = class_type;
	}

	public Integer getGrade_type() {
		return grade_type;
	}

	public void setGrade_type(Integer grade_type) {
		this.grade_type = grade_type;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public Integer getSts() {
		return sts;
	}

	public void setSts(Integer sts) {
		this.sts = sts;
	}

	public Integer getHead_teacher_id() {
		return head_teacher_id;
	}

	public void setHead_teacher_id(Integer head_teacher_id) {
		this.head_teacher_id = head_teacher_id;
	}
	
	@Formula("(SELECT t.fullname FROM user t WHERE t.id = head_teacher_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String headTeacherName;	
	public String getHeadTeacherName() {
		return headTeacherName;
	}
	
	public  Set<User>  getUserByRoles(String fileter_roles) {
		HashSet<User> ret = new HashSet<>();
		for (User user: this.users){
			for (String role:fileter_roles.split(",")){
				if (user.hasRole(role)){
					ret.add(user);
					break;
				}
			}
			
		}
		return ret;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public void setHeadTeacherName(String headTeacherName) {
		this.headTeacherName = headTeacherName;
	}


}
