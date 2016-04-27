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
@Table(name="sys_weekday")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 
public class SysWeekday extends SysBase{
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
		temp.notice = this.notice;
		temp.setId(this.getId());
		return temp;
	}
public void saveFromTemplate(MTemplate temp){
		
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
		this.notice = temp.notice;
		
		this.setId(temp.getId());
	}
}