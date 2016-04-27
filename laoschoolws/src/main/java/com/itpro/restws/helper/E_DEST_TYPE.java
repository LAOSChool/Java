package com.itpro.restws.helper;

public enum E_DEST_TYPE {
	PERSON(0),
	CLASS(1),
	SCHOOL(2);
	
	private int value;
	
	private E_DEST_TYPE(final int val){
		this.value = val;
	}
	
	public int getValue(){
		return this.value;
	}

	@Override
	public String toString(){
		return this.value+"";
	}

	public String getName(){
		return this.name();
	}


}
