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
@Table(name="sys_province")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 
public class SysProvince extends SysBase{
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
	public SysTemplate convertToTemplate(){
		SysTemplate temp = super.convert2ToTemplate();
		temp.setId(this.getId());
		return temp;
	}
	public void saveFromTemplate(SysTemplate temp){
		
	super.saveFromTemplate(temp);
	this.setId(temp.getId());
	}
}
