package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class MasterBase extends AbstractModel{
	@Column(name="school_id")
	protected int school_id;

	@Column(name="sval")
	protected String sval;
	
	
	@Column(name="fval1")
	protected float fval1;
	
	@Column(name="fval2")
	protected float fval2;
	
	@Column(name="notice")
	protected String notice;
	
	

	public int getSchool_id() {
		return school_id;
	}

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

	public void setSchool_id(int school_id) {
		this.school_id = school_id;
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
	
	
	public MTemplate convertToTemplate(){
		MTemplate temp = new MTemplate(); 
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
		temp.school_id = this.school_id;
		temp.notice = this.notice;
		return temp;
	}
public void saveFromTemplate(MTemplate temp){
		
	this.actflg = temp.actflg==null?this.actflg:temp.actflg;
	this.ctddtm = temp.ctddtm==null?this.ctddtm:temp.ctddtm;
	this.ctdpgm =temp.ctdpgm==null?this.ctdpgm:temp.ctdpgm;
	this.ctdusr = temp.ctdusr==null?this.ctdusr:temp.ctdusr;
	this.ctdwks = temp.ctdwks==null?this.ctdwks:temp.ctdwks;
	this.lstmdf = temp.lstmdf==null?this.lstmdf:temp.lstmdf;
	this.mdfpgm = temp.mdfpgm==null?this.mdfpgm:temp.mdfpgm;
	this.mdfusr = temp.mdfusr==null?this.mdfusr:temp.mdfusr;
	this.mdfwks = temp.mdfwks==null?this.mdfwks:temp.mdfwks;
	
	this.fval1 = temp.fval1;
	this.fval2 = temp.fval2;
	this.sval = temp.sval==null?this.sval:temp.sval;
	this.school_id = temp.school_id;
	this.notice = temp.notice==null?this.notice:temp.notice;
	
	}

}
