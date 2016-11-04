package com.itpro.ent;

public class MailEnt {

	long id; 
	Integer school_id;
	public Integer getSchool_id() {
		return school_id;
	}
	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	String sub; 
	String body; 
	String receivers; 
	String date_time; 
	String imp_sp; 
	 
	String imp_datetime; 
	int processed;
	int success;
	
	String description;
	
	long imp_id;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getReceivers() {
		return receivers;
	}
	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}
	public String getDate_time() {
		return date_time;
	}
	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}
	public String getImp_sp() {
		return imp_sp;
	}
	public void setImp_sp(String imp_sp) {
		this.imp_sp = imp_sp;
	}
	
	public String getImp_datetime() {
		return imp_datetime;
	}
	public void setImp_datetime(String imp_datetime) {
		this.imp_datetime = imp_datetime;
	}
	public int getProcessed() {
		return processed;
	}
	public void setProcessed(int processed) {
		this.processed = processed;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getImp_id() {
		return imp_id;
	}
	public void setImp_id(long imp_id) {
		this.imp_id = imp_id;
	}
	@Override
	public String toString() {
		String ret = "school_id:"+school_id==null?"null":school_id.intValue()+"\n";
		ret += "body:"+body==null?"null":body+"\n";
		ret += "receivers"+receivers==null?"null":receivers+"\n";
		return ret;
	}
}
