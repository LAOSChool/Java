package com.itpro.restws.helper;

public enum E_RIGHT {
	R(1), 	//001
	RW(2); 	//010
	
	private int value;
	public int getValue() {
		return value;
	}

	private E_RIGHT(final int m_val){
		this.value = m_val;
	}
	

	@Override
	public String toString(){
		return this.value +"";
	}

	public String getName(){
		return this.name();
	}

	public static E_RIGHT of(int value) {
	    switch (value) {
	        case 1: return R;
	        case 2: return RW;
	        default: return null;
	    }
	}
}
