package com.itpro.restws.helper;

import java.util.List;

public class ListEnt {
	int total_count = 0;
	public int getTotal_count() {
		return total_count;
	}
	public int getFrom_row() {
		return from_row;
	}
	public int getTo_row() {
		return to_row;
	}
	@SuppressWarnings("rawtypes")
	public List getList() {
		return list;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public void setFrom_row(int from_row) {
		this.from_row = from_row;
	}
	public void setTo_row(int to_row) {
		this.to_row = to_row;
	}
	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}
	int from_row = 0;
	int to_row = 1000;
	@SuppressWarnings("rawtypes")
	private List list;
	
	
	
}
