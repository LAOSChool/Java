package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="school")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class School extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;

	
	@Column(name="title")
	private String title;

	@Column(name="addr1")
	private String addr1;
	
	@Column(name="addr2")
	private String addr2;
	
	@Column(name="prov_city")
	private int prov_city;
	
	@Column(name="county")
	private int county;
	
	@Column(name="dist")
	private int dist;
	
	@Column(name="url")
	private String url;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="ext")
	private String ext;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="degree")
	private int degree;
	
	
	@Column(name="description")
	private String description;
	
	
	@Column(name="principal")
	private String principal;
	
	@Column(name="found_dt")
	private String found_dt;
	
	
	@Column(name="photo")
	private String photo;
	
	@Column(name="state")
	private String state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public int getProv_city() {
		return prov_city;
	}

	public void setProv_city(int prov_city) {
		this.prov_city = prov_city;
	}

	public int getCounty() {
		return county;
	}

	public void setCounty(int county) {
		this.county = county;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getFound_dt() {
		return found_dt;
	}

	public void setFound_dt(String found_dt) {
		this.found_dt = found_dt;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Formula("(SELECT t.sval FROM sys_province t WHERE t.id = prov_city)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String province_name;

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

}
