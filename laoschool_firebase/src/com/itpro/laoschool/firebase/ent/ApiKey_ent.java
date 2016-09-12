package com.itpro.laoschool.firebase.ent;

public class ApiKey_ent {
	Integer id;
	public Integer getId() {
		return id;
	}
	public String getApi_key() {
		return api_key;
	}
	public String getLast_request_dt() {
		return last_request_dt;
	}
	public String getFirst_request_dt() {
		return first_request_dt;
	}
	public String getSso_id() {
		return sso_id;
	}
	public String getCld_token() {
		return cld_token;
	}
	public String getNotice() {
		return notice;
	}
	public String getAuth_key() {
		return auth_key;
	}
	public Integer getActive() {
		return active;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}
	public void setLast_request_dt(String last_request_dt) {
		this.last_request_dt = last_request_dt;
	}
	public void setFirst_request_dt(String first_request_dt) {
		this.first_request_dt = first_request_dt;
	}
	public void setSso_id(String sso_id) {
		this.sso_id = sso_id;
	}
	public void setCld_token(String cld_token) {
		this.cld_token = cld_token;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public void setAuth_key(String auth_key) {
		this.auth_key = auth_key;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	String api_key;
	String last_request_dt;
	String first_request_dt;
	String sso_id;
	String cld_token;
	String notice;
	String auth_key;
	Integer active;
	
}
