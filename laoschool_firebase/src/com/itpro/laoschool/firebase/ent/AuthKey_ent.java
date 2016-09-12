package com.itpro.laoschool.firebase.ent;

public class AuthKey_ent {
	Integer id;
	public Integer getId() {
		return id;
	}
	public String getAuth_key() {
		return auth_key;
	}
	public String getSso_id() {
		return sso_id;
	}
	public String getExpired_dt() {
		return expired_dt;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setAuth_key(String auth_key) {
		this.auth_key = auth_key;
	}
	public void setSso_id(String sso_id) {
		this.sso_id = sso_id;
	}
	public void setExpired_dt(String expired_dt) {
		this.expired_dt = expired_dt;
	}
	String auth_key;
	String sso_id;
	String expired_dt;
	
}
