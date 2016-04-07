package com.itpro.restws.service;

import java.util.HashMap;
import java.util.Map;

public enum MasterTblName {
	TBLNAME_M_EXAM("m_exam","MExam"),
	TBLNAME_M_FEE("m_fee","MFee"),
	TBLNAME_M_GRADE("m_grade","MGrade"),
	TBLNAME_M_SESSION("m_session","MSession"),
	TBLNAME_M_SUBJECT ("m_subject","MSubject"),
	TBLNAME_M_TERM ("m_term","MTerm"),
	TBLNAME_M_USER2CLASS ("m_user2class","MUser2Class");
	
	  
	private final String tbl_name;
	private final String model_name;
	public String toString() { return tbl_name; }
		   
   public boolean equalsName(String otherName) {
        return (otherName == null) ? false : this.tbl_name.equals(otherName);
    }
		   
			
		private MasterTblName(String tbl,String model) {
			this.tbl_name=tbl;
			this.model_name = model;
		}

		public String getTblName() {
			return this.tbl_name;
		}

		public String getModelName() {
			return this.model_name;
		}
		
		private static Map<String, MasterTblName> map = new HashMap<String, MasterTblName>();
		static {
	        for (MasterTblName masterTblName : MasterTblName.values()) {
	            map.put(masterTblName.getTblName(), masterTblName);
	        }
	    }
		public static MasterTblName getENumFromTblName(String tblname) {
	        return map.get(tblname);
		}
	
}
