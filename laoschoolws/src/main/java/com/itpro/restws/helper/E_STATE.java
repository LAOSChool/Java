package com.itpro.restws.helper;

public enum E_STATE {
	PENDING(0,"Pending"),
	ACTIVE(1,"Active"),
	SUSPENSE(2,"Suspense"),
	CLOSED(3,"Closed");
	
	
	private final int value;

	private final String name;


	private E_STATE(int val, String txt) {
		this.value = val;
		this.name = txt;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public int value() {
		return this.value;
	}

	/**
	 * Return the reason phrase of this status code.
	 */
	public String getName() {
		return name;
	}


}
