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
@Table(name="m_grade")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class MGrade extends MasterBase{
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

public void saveFromTemplate(MTemplate temp){
		
	super.saveFromTemplate(temp);
	this.setId(temp.getId());
	}
}
