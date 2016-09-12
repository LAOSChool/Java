package com.itpro.laoschool.firebase;

public class FirebaseResponse {
	/***
	 * {"multicast_id":6782339717028231855,"success":0,"failure":1,"canonical_ids":0,"results":[{"error":"InvalidRegistration"}]}
	 * { "multicast_id": 108,"success": 1,"failure": 0,"canonical_ids": 0,"results": [{ "message_id": "1:08" }]}
	 */
	String multicast_id;
	String success;
	String failure;
	String canonical_ids;
	String result;
	
	public String getMulticast_id() {
		return multicast_id;
	}
	public String getSuccess() {
		return success;
	}
	public String getFailure() {
		return failure;
	}
	public String getCanonical_ids() {
		return canonical_ids;
	}
	public String getResult() {
		return result;
	}
	public void setMulticast_id(String multicast_id) {
		this.multicast_id = multicast_id;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public void setFailure(String failure) {
		this.failure = failure;
	}
	public void setCanonical_ids(String canonical_ids) {
		this.canonical_ids = canonical_ids;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
