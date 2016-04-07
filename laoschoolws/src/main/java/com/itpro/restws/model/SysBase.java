package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@MappedSuperclass
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 
public class SysBase extends AbstractModel{
	@Column(name="sval")
	protected String sval;
	
	
	@Column(name="fval1")
	protected float fval1;
	
	@Column(name="fval2")
	protected float fval2;
	
	@Column(name="notice")
	protected String notice;

	public String getSval() {
		return sval;
	}

	public float getFval1() {
		return fval1;
	}

	public float getFval2() {
		return fval2;
	}

	public String getNotice() {
		return notice;
	}

	public void setSval(String sval) {
		this.sval = sval;
	}

	public void setFval1(float fval1) {
		this.fval1 = fval1;
	}

	public void setFval2(float fval2) {
		this.fval2 = fval2;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	
}
