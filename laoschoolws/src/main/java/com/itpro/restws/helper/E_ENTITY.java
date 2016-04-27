package com.itpro.restws.helper;

public enum E_ENTITY {
	USER("user"),
	CLASS("class"),
	SCHOOL("school"),
	ATTENDANCE("attendance"),
	EXAM_RESULT("exam_result"),
	FINAL_RESULT("final_result"),
	TIMETABLE("timetable"),
	MASTER("master"),
	SYSTEM("system"),
	NOTIFY("notify"),
	MESSAGE("message");
	
	private String value;
	
	private E_ENTITY(final String val){
		this.value = val;
	}
	
	public String getValue(){
		return this.value;
	}

	@Override
	public String toString(){
		return this.value;
	}

	public String getName(){
		return this.name();
	}


}
