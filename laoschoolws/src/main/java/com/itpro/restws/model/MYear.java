package com.itpro.restws.model;

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
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="m_year")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class MYear extends MasterBase{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public MTemplate convertToTemplate(){
		MTemplate temp = super.convertToTemplate();
		temp.setId(this.getId());
		return temp;
	}
	
//	@JsonIgnore
//	@ManyToMany(fetch = FetchType.LAZY,mappedBy = "myears")
//	private Set<User> users;
//	public Set<User> getUsers() {
//		return users;
//	}
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}

	
public void saveFromTemplate(MTemplate temp){
		
	super.saveFromTemplate(temp);
	this.setId(temp.getId());
	}
}

