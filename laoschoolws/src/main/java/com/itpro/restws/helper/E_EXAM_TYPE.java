package com.itpro.restws.helper;

public enum E_EXAM_TYPE {
	MONTH(1),	
	TEST_TERM(2),
	AVE_4_MONTH(3),
	AVE_TERM(4),
	AVE_YEAR(5),
	RETEST(6),
	GRADUATED(7);
	
	private int value;
	public int getValue() {
		return value;
	}

	private E_EXAM_TYPE(final int m_val){
		this.value = m_val;
	}

	@Override
	public String toString(){
		return this.value +"";
	}

	public String getName(){
		return this.name();
	}
}
