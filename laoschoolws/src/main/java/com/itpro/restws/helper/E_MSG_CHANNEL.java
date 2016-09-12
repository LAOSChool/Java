package com.itpro.restws.helper;

public enum E_MSG_CHANNEL {
	FIREBASE(0),
	SMS(1),
	BOTH_FIREBASE_SMS(2);
	
	private int value;
	
	private E_MSG_CHANNEL(final int val){
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
