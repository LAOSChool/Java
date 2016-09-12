package com.itpro.laoschool.firebase.ent;

public class FireBase_ent {
	Integer id;
	public Integer getId() {
		return id;
	}
	public String getOrg_tbl() {
		return org_tbl;
	}
	public Integer getSchool_id() {
		return school_id;
	}
	public Integer getOrg_id() {
		return org_id;
	}
	public Integer getFrom_user_id() {
		return from_user_id;
	}
	public String getFrom_user_name() {
		return from_user_name;
	}
	public Integer getTo_user_id() {
		return to_user_id;
	}
	public String getTo_user_name() {
		return to_user_name;
	}
	public String getTo_sso_id() {
		return to_sso_id;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public Integer getIs_sent() {
		return is_sent;
	}
	public String getSent_dt() {
		return sent_dt;
	}
	public Integer getSuccess() {
		return success;
	}
	public String getError() {
		return error;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setOrg_tbl(String org_tbl) {
		this.org_tbl = org_tbl;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	public void setOrg_id(Integer org_id) {
		this.org_id = org_id;
	}
	public void setFrom_user_id(Integer from_user_id) {
		this.from_user_id = from_user_id;
	}
	public void setFrom_user_name(String from_user_name) {
		this.from_user_name = from_user_name;
	}
	public void setTo_user_id(Integer to_user_id) {
		this.to_user_id = to_user_id;
	}
	public void setTo_user_name(String to_user_name) {
		this.to_user_name = to_user_name;
	}
	public void setTo_sso_id(String to_sso_id) {
		this.to_sso_id = to_sso_id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setIs_sent(Integer is_sent) {
		this.is_sent = is_sent;
	}
	public void setSent_dt(String sent_dt) {
		this.sent_dt = sent_dt;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public void setError(String error) {
		this.error = error;
	}
	String org_tbl;
	Integer school_id;
	Integer org_id;
	Integer from_user_id;
	String from_user_name;
	Integer to_user_id;
	String to_user_name;
	String to_sso_id;
	String title;
	String content;
	Integer is_sent;
	String sent_dt;
	Integer success;
	String error;
	public String toString(){
		String str= "school_id:"+school_id+"\n";
		str += "org_tbl:"+org_tbl+"\n";
		str += "org_id:"+org_id+"\n";
		str += "from_user_id:"+from_user_id+"\n";
		str += "from_user_name:"+from_user_name+"\n";
		str += "to_user_id:"+to_user_id+"\n";
		str += "to_user_name:"+to_user_name+"\n";
		str += "to_sso_id:"+to_sso_id+"\n";
		str += "title:"+title+"\n";
		str += "content:"+content+"\n";
		
		return str;
	}
}
