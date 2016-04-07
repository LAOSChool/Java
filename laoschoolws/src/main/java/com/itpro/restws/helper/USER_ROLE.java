package com.itpro.restws.helper;

public enum USER_ROLE {

	ADMIN("ROLE_ADMIN"),
	TEACHER("ROLE_TEACHER"),
	CLS_PRESIDENT("ROLE_CLS_PRESIDENT"),
	STUDENT("ROLE_STUDENT");
	
	private String role;
	
	private USER_ROLE(final String role){
		this.role = role;
	}
	
	public String getRole(){
		return this.role;
	}

	@Override
	public String toString(){
		return this.role;
	}

	public String getName(){
		return this.name();
	}


}
