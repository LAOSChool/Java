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
@Table(name="m_exam")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class MExam extends MasterBase{
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

	public MTemplate convertToTemp(){
		MTemplate temp = super.convertToTemplate();
		temp.setId(this.getId());
		return temp;
	}
	public void saveFromTemp(MTemplate temp){
		
		super.saveFromTemplate(temp);
		this.setId(temp.getId());
	}
}
