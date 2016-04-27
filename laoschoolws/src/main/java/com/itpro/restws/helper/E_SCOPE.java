package com.itpro.restws.helper;

public enum E_SCOPE {
	SYSTEM(5), 	//101
	SCHOOL(4), 	//100
	CLASS(2),  	//010
	PERSON(1);	//001
	
	private int value;
	public int getValue() {
		return value;
	}

	private E_SCOPE(final int m_val){
		this.value = m_val;
	}
	

	@Override
	public String toString(){
		return this.value +"";
	}

	public String getName(){
		return this.name();
	}

	public static E_SCOPE of(int value) {

	    switch (value) {
	        case 1: return PERSON;
	        case 2: return CLASS;
	        case 4: return SCHOOL;
	        case 5: return SYSTEM;
	        default: return null;

	    }
	}

}
