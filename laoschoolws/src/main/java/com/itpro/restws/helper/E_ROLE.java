package com.itpro.restws.helper;

public enum E_ROLE {
	SYS_ADMIN("ROLE_SYS_ADMIN","SYS_ADMIN"),
	ADMIN("ROLE_ADMIN","ADMIN"),
	TEACHER("ROLE_TEACHER","TEACHER"),
	CLS_PRESIDENT("ROLE_CLS_PRESIDENT","CLS_PRESIDENT"),
	STUDENT("ROLE_STUDENT","STUDENT");
	
	private String role;
	private String role_short;
	
	private E_ROLE(final String role,final String db_short){
		this.role = role;
		this.role_short = db_short;
	}
	
	public String getRole(){
		return this.role;
	}

	public String getRole_short(){
		return this.role_short;
	}
	
	@Override
	public String toString(){
		return this.role;
	}

	public String getName(){
		return this.name();
	}

	public static boolean contain(String txt) {

	   return ("ADMIN".equals(txt) || "TEACHER".equals(txt) || "CLS_PRESIDENT".equals(txt) || "STUDENT".equals(txt));
	}
	public static boolean upload_Accept(String txt) {

		   return ("TEACHER".equals(txt) || "CLS_PRESIDENT".equals(txt) || "STUDENT".equals(txt));
		}

}
