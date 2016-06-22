package com.itpro.restws.helper;

import com.itpro.restws.model.ExamResult;

public class AveInfo {
	
	private int is_new=0;
	
	public int getIs_new() {
		return is_new;
	}
	public void setIs_new(int is_new) {
		this.is_new = is_new;
	}
	public float getTotal() {
		return total;
	}
	public int getCnt() {
		return cnt;
	}
	
	public void setTotal(float total) {
		this.total = total;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	private ExamResult ave_exam_result;
	public ExamResult getAve_exam_result() {
		return ave_exam_result;
	}
	public void setAve_exam_result(ExamResult ave_exam_result) {
		this.ave_exam_result = ave_exam_result;
	}
	private float total;
	private int cnt;
	

}
