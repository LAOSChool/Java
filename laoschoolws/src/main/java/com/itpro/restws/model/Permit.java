package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="permission")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class Permit extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="entity")
	private String entity;
	
	
	@Column(name="rights")
	private int rights;

	@JsonIgnore
	@Column(name="roles")
	private String roles;

	// Personal: 001, Class: 010, School: 100
	@Column(name="scope")
	private int scope;
	
	@Column(name="school_id")
	private Integer school_id;

	public Integer getId() {
		return id;
	}

	public String getEntity() {
		return entity;
	}

	public int getRights() {
		return rights;
	}

	public String getRoles() {
		return roles;
	}

	public int getScope() {
		return scope;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public void setRights(int rights) {
		this.rights = rights;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	
	public String toString(){
		return "Permit: {entity="+entity+";roles="+roles+";school="+school_id+";rights="+rights+";scope="+scope+"}";
	}

	public boolean hasRole(String role){
		String roles = this.roles;
		if (roles == null || "".equalsIgnoreCase(roles)){
			return false;
		}
		for (String txt : roles.split(",")){
			if (role.equalsIgnoreCase(txt)){
				return true;
			}	
		}
		return false;
	}
}
