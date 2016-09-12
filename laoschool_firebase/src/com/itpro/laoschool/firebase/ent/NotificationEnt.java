package com.itpro.laoschool.firebase.ent;

public class NotificationEnt {
	public String getTitle() {
		return title;
	}
	public String getBody() {
		return body;
	}
	public int getBadge() {
		return badge;
	}
	public String getSound() {
		return sound;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public void setBadge(int badge) {
		this.badge = badge;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
	String title;
	String body;
	int badge = 1;
	String sound = "default";
	
}
