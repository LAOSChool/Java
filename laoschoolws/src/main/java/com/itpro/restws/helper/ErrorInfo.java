package com.itpro.restws.helper;

public class ErrorInfo {
	private int status;
	private String url;
	private String message;
	private String developerMessage;
	
	
	public ErrorInfo(int pSts, String message,String url,String pDeveloperMessage) {
	    this.url = url;
	    this.message = message;
	    this.status = pSts;
	    this.developerMessage = pDeveloperMessage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getHttpStatus() {
		return status;
	}

	public void setHttpStatus(int httpStatus) {
		this.status = httpStatus;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
}
