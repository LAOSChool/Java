package com.itpro.restws.helper;

public class ChangePassInfo {
	
	String username;
	public String getUsername() {
		return username;
	}
	public String getOld_pass() {
		return old_pass;
	}
	public String getNew_pass() {
		return new_pass;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setOld_pass(String old_pass) {
		this.old_pass = old_pass;
	}
	public void setNew_pass(String new_pass) {
		this.new_pass = new_pass;
	}
	String old_pass;
	String new_pass;

}
