package com.itpro.restws.helper;

public enum E_TERM_STS {
	INACTIVE(0),
	ACTIVE(1),
	PENDING(2);
	
	private int value;
	
	private E_TERM_STS(final int val){
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
