package com.itpro.restws.model;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
//MappedSuperClass must be used to inherit properties, associations, and methods.
@MappedSuperclass
public class AbstractModel {
//	@Id
//	  @GeneratedValue(strategy = GenerationType.AUTO)
//	  @Column(name = "ID", updatable = false, nullable = false)
//	  private Integer id;
//	
	// DEFAULT FIELDS Start
	// Active Flag ('A': Active///'D':Deleted
	@JsonIgnore
	@Column(name="actflg")
	protected String actflg;
	// Create User
	@JsonIgnore
	@Column(name="ctdusr")
	protected String ctdusr;
	// Create Workstation
	@JsonIgnore
	@Column(name="ctdwks")
	protected String ctdwks;
	// Create Program
	@JsonIgnore
	@Column(name="ctdpgm")
	protected String ctdpgm;
	// Create Date Time	
	@JsonIgnore
	@Column(name="ctddtm")
	protected String ctddtm;
	// Modify User
	@JsonIgnore
	@Column(name="mdfusr")
	protected String mdfusr;
	// Modify Workstation
	@JsonIgnore
	@Column(name="mdfwks")
	protected String mdfwks;
	// Modify Program	
	@JsonIgnore
	@Column(name="mdfpgm")
	protected String mdfpgm;
	// Last Modify Date time	
	@JsonIgnore
	@Column(name="lstmdf")
	protected String lstmdf;
	
	@JsonIgnore
	public String getActflg() {
		return actflg;
	}
	@JsonIgnore
	public String getCtdusr() {
		return ctdusr;
	}
	@JsonIgnore
	public String getCtdwks() {
		return ctdwks;
	}
	@JsonIgnore
	public String getCtdpgm() {
		return ctdpgm;
	}
	@JsonIgnore
	public String getCtddtm() {
		return ctddtm;
	}
	@JsonIgnore
	public String getMdfusr() {
		return mdfusr;
	}
	@JsonIgnore
	public String getMdfwks() {
		return mdfwks;
	}
	@JsonIgnore
	public String getMdfpgm() {
		return mdfpgm;
	}
	@JsonIgnore
	public String getLstmdf() {
		return lstmdf;
	}
	@JsonIgnore
	public void setActflg(String actflg) {
		this.actflg = actflg;
	}
	@JsonIgnore
	public void setCtdusr(String ctdusr) {
		this.ctdusr = ctdusr;
	}
	@JsonIgnore
	public void setCtdwks(String ctdwks) {
		this.ctdwks = ctdwks;
	}
	@JsonIgnore
	public void setCtdpgm(String ctdpgm) {
		this.ctdpgm = ctdpgm;
	}
	@JsonIgnore
	public void setCtddtm(String ctddtm) {
		this.ctddtm = ctddtm;
	}
	@JsonIgnore
	public void setMdfusr(String mdfusr) {
		this.mdfusr = mdfusr;
	}
	@JsonIgnore
	public void setMdfwks(String mdfwks) {
		this.mdfwks = mdfwks;
	}
	@JsonIgnore
	public void setMdfpgm(String mdfpgm) {
		this.mdfpgm = mdfpgm;
	}
	@JsonIgnore
	public void setLstmdf(String lstmdf) {
		this.lstmdf = lstmdf;
	}

//	oUp = Updated row with null fields & oDb = Row fetched from database at update time
	/***
	 * 
	 * @param oDb: Row fetched from database at update time
	 * @param oUp: Updated row with null fields
	 * Merger cac truong khac NULL cua oUp =>vao => oDB sau do return oDB voi ID co san.
	 * @return: changed oDB
	 */
	public static <T> T updateChanges(T oDb, T oUp) {
	    try {
	    	// Update Super class
	    	java.lang.reflect.Field[] fields = oDb.getClass().getSuperclass().getDeclaredFields();
	        for (Field field : fields) {
	            field.setAccessible(true);
	            if (field.get(oUp) != null) {
	            	// field.set(oDb, field.get(oUp));
	            	if(field.get(oUp) instanceof Collection) {
	                    //Do your thing
	            		@SuppressWarnings("rawtypes")
						Collection list = (Collection) field.get(oUp);
	            		if (list.size() > 0){
	            			field.set(oDb, field.get(oUp));
	            		}
	                }
	            	else{
	            		field.set(oDb, field.get(oUp));
	            	}
	            	
	            }
	        }
	        // Update this class
	        fields = oDb.getClass().getDeclaredFields();
	        for (Field field : fields) {
	            field.setAccessible(true);
	            if (field.get(oUp) != null) {
	                //field.set(oDb, field.get(oUp));
	            	if(field.get(oUp) instanceof Collection) {
	                    //Do your thing
	            		@SuppressWarnings("rawtypes")
						Collection list = (Collection) field.get(oUp);
	            		if (list.size() > 0){
	            			field.set(oDb, field.get(oUp));
	            		}
	                }
	            	else{
	            		field.set(oDb, field.get(oUp));
	            	}

	            }
	        }
	    } catch (IllegalAccessException e) {
	        e.printStackTrace();
	    }
	    return oDb;
	}
	
	
// DEFAULT FIELDS End	
}
