package com.itpro.restws.helper;

import org.springframework.http.HttpStatus;

public class ESchoolException extends RuntimeException{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String error_msg;
	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public HttpStatus getHttpSts() {
		return httpSts;
	}

	public void setHttpSts(HttpStatus httpSts) {
		this.httpSts = httpSts;
	}

	private HttpStatus httpSts;
     
    public ESchoolException(String txt, HttpStatus pHttpSts) {
    	error_msg = txt;
    	httpSts= pHttpSts;
    }
     
  
}
