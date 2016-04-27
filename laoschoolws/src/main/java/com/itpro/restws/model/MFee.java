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
@Table(name="m_fee")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class MFee extends MasterBase{
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
//		MTemplate temp = new MTemplate(); 
//		temp.actflg = this.actflg;
//		temp.ctddtm = this.ctddtm;
//		temp.ctdpgm = this.ctdpgm;
//		temp.ctdusr = this.ctdusr;
//		temp.ctdwks = this.ctdwks;
//		temp.lstmdf = this.lstmdf;
//		temp.mdfpgm = this.mdfpgm;
//		temp.mdfusr = this.mdfusr;
//		temp.mdfwks = this.mdfwks;
//		
//		temp.fval1 = this.fval1;
//		temp.fval2 = this.fval2;
//		temp.sval = this.sval;
//		temp.school_id = this.school_id;
//		temp.notice = this.notice;
//		temp.setId(this.getId());
//		return temp;
		
		MTemplate temp = super.convertToTemplate();
		temp.setId(this.getId());
		return temp;
	}
public void saveFromTemplate(MTemplate temp){
		
//	this.actflg = temp.actflg==null?this.actflg:temp.actflg;
//	this.ctddtm = temp.ctddtm==null?this.ctddtm:temp.ctddtm;
//	this.ctdpgm =temp.ctdpgm==null?this.ctdpgm:temp.ctdpgm;
//	this.ctdusr = temp.ctdusr==null?this.ctdusr:temp.ctdusr;
//	this.ctdwks = temp.ctdwks==null?this.ctdwks:temp.ctdwks;
//	this.lstmdf = temp.lstmdf==null?this.lstmdf:temp.lstmdf;
//	this.mdfpgm = temp.mdfpgm==null?this.mdfpgm:temp.mdfpgm;
//	this.mdfusr = temp.mdfusr==null?this.mdfusr:temp.mdfusr;
//	this.mdfwks = temp.mdfwks==null?this.mdfwks:temp.mdfwks;
//	
//	this.fval1 = temp.fval1;
//	this.fval2 = temp.fval2;
//	this.sval = temp.sval==null?this.sval:temp.sval;
//	this.school_id = temp.school_id;
//	this.notice = temp.notice==null?this.notice:temp.notice;
//	
//	this.setId(temp.getId());
		super.saveFromTemplate(temp);
		this.setId(temp.getId());
	}
}
