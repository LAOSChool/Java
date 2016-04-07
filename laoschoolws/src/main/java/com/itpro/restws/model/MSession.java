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
@Table(name="m_session")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class MSession extends MasterBase{
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
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
