package com.itpro.laoschool.firebase.ent;

public class PostDataEnt {
	String priority="high";
	public String getPriority() {
		return priority;
	}
	public String getTo() {
		return to;
	}
	public NotificationEnt getNotification() {
		return notification;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public void setNotification(NotificationEnt notification) {
		this.notification = notification;
	}
	String to;
	NotificationEnt notification;
	
}
