package com.itpro.restws.service;

public enum SysTblName {
	TBLNAME_SYS_DEGREE("sys_degree","SysDegree"),
	TBLNAME_SYS_PROVINCE("sys_province","SysProvince"),
	TBLNAME_SYS_ROLE("sys_role","SysRole"),
	TBLNAME_SYS_WEEKDAY("sys_weekday","SysWeekday");
	
	   public String toString() { return tbl_name; }
	   
	   public boolean equalsName(String otherName) {
	        return (otherName == null) ? false : this.tbl_name.equals(otherName);
	    }
	   
	   
	   private final String tbl_name;

		private final String model_name;


		private SysTblName(String tbl,String model) {
			this.tbl_name=tbl;
			this.model_name = model;
		}

		public String getTblName() {
			return this.tbl_name;
		}

		public String getModelName() {
			return this.model_name;
		}
		
	
}
