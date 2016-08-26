package com.itpro.restws.service;

public enum SysTblName {
	TBLNAME_SYS_DEGREE("sys_degree","SysDegree"),// tbl_name,model_name
	TBLNAME_SYS_PROVINCE("sys_province","SysProvince"),
	TBLNAME_SYS_DIST("sys_dist","SysDist"),
	
	TBLNAME_SYS_ROLE("sys_role","SysRole"),
	
	TBLNAME_SYS_ATT_REASON("sys_att_reason","SysAttReason"),
	TBLNAME_SYS_LATE_REASON("sys_late_reason","SysLateReason"),
	TBLNAME_SYS_ATT_MSG("sys_att_msg","SysAttMsg"),
	
	TBLNAME_SYS_STS("sys_sts","SysSts"),
	TBLNAME_SYS_STD_MSG("sys_std_msg","SysStdMsg"),
	
	TBLNAME_SYS_MSG_SAMP("sys_msg_samp","SysMsgSamp"),
	TBLNAME_SYS_SETTINGS("sys_settings","SysSettings"),
	
	
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
