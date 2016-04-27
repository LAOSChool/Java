package com.itpro.restws.helper;

import java.util.List;

public class MessageFilter {
	
	public List<Integer> getClasses() {
		return classes;
	}
	public List<Integer> getUsers() {
		return users;
	}
	public void setClasses(List<Integer> classes) {
		this.classes = classes;
	}
	public void setUsers(List<Integer> users) {
		this.users = users;
	}
	List<Integer> classes;
	List<Integer> users;
	
}
