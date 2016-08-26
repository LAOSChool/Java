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
	
	@Column(name="lval")
	protected String lval;
	
	
	@Column(name="fval1")
	protected Float fval1;
	
	@Column(name="fval2")
	protected Float fval2;
	
	@Column(name="notice")
	protected String notice;

	public String getSval() {
		return sval;
	}

	public Float getFval1() {
		return fval1;
	}

	public Float getFval2() {
		return fval2;
	}

	public String getNotice() {
		return notice;
	}

	public void setSval(String sval) {
		this.sval = sval;
	}

	public void setFval1(Float fval1) {
		this.fval1 = fval1;
	}

	public void setFval2(Float fval2) {
		this.fval2 = fval2;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getLval() {
		return lval;
	}

	public void setLval(String lval) {
		this.lval = lval;
	}
	
	public SysTemplate convert2ToTemplate(){
		SysTemplate temp = new SysTemplate(); 
		temp.actflg = this.actflg;
		temp.ctddtm = this.ctddtm;
		temp.ctdpgm = this.ctdpgm;
		temp.ctdusr = this.ctdusr;
		temp.ctdwks = this.ctdwks;
		temp.lstmdf = this.lstmdf;
		temp.mdfpgm = this.mdfpgm;
		temp.mdfusr = this.mdfusr;
		temp.mdfwks = this.mdfwks;
		
		temp.fval1 = this.fval1;
		temp.fval2 = this.fval2;
		
		temp.sval = this.sval;
		temp.lval = this.lval;
		
		temp.notice = this.notice;
		return temp;
	}
public void saveFromTemplate(SysTemplate temp){
	    
	
		this.actflg = temp.actflg;
		this.ctddtm = temp.ctddtm;
		this.ctdpgm = temp.ctdpgm;
		this.ctdusr = temp.ctdusr;
		this.ctdwks = temp.ctdwks;
		this.lstmdf = temp.lstmdf;
		this.mdfpgm = temp.mdfpgm;
		this.mdfusr = temp.mdfusr;
		this.mdfwks = temp.mdfwks;
		
		this.fval1 = temp.fval1;
		this.fval2 = temp.fval2;
		
		this.sval = temp.sval;
		this.lval = temp.lval;
		
		this.notice = temp.notice;
		
		//this.setId(temp.getId());
	}	
}
