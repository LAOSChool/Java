package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.http.HttpStatus;

import com.itpro.restws.helper.ESchoolException;


//
//@NamedNativeQuery(
//	name = "call_sp_get_exam_result",
//	query = "CALL sp_get_exam_result(:p_school_id,:p_class_id,:p_student_id,:p_year_id)",
//	callable=true, 
//	readOnly=true, 
//	resultClass = ExamResult.class
//)


@Entity
@Table(name="exam_result")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class ExamResult extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	
	@Column(name="school_id")
	private Integer school_id;

	@Column(name="class_id")
	private Integer class_id;

	@Column(name="student_id")
	private Integer student_id;
	
	@Column(name="student_name")
	private String student_name;
	
	@Column(name="subject_id")
	private Integer subject_id;
	@Column(name="subject_name")	
	private String subject_name;
	
	@Column(name="notice")
	private String notice;
	
	@Column(name="sch_year_id")
	private Integer sch_year_id;
	
	@Column(name="m1")
	private String m1;
	
	@Column(name="m2")
	private String m2;
	
	@Column(name="m3")
	private String m3;
	
	@Column(name="m4")
	private String m4;
	
	@Column(name="m5")
	private String m5;
	
	@Column(name="m6")
	private String m6;
	
	@Column(name="m7")
	private String m7;

	@Column(name="m8")
	private String m8;

	@Column(name="m9")
	private String m9;

	@Column(name="m10")
	private String m10;

	@Column(name="m11")
	private String m11;

	@Column(name="m12")
	private String m12;

	@Column(name="m13")
	private String m13;

	@Column(name="m14")
	private String m14;

	@Column(name="m15")
	private String m15;

	@Column(name="m16")
	private String m16;

	@Column(name="m17")
	private String m17;

	@Column(name="m18")
	private String m18;

	@Column(name="m19")
	private String m19;

	@Column(name="m20")
	private String m20;


	
	@Formula("(SELECT t.photo FROM user t WHERE t.id = student_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String std_photo;
	
	@Formula("(SELECT t.nickname FROM user t WHERE t.id = student_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String std_nickname;
	
	
	
	public Integer getId() {
		return id;
	}


	public Integer getSchool_id() {
		return school_id;
	}


	public Integer getClass_id() {
		return class_id;
	}


	public Integer getStudent_id() {
		return student_id;
	}


	public String getStudent_name() {
		return student_name;
	}


	public Integer getSubject_id() {
		return subject_id;
	}


	public String getNotice() {
		return notice;
	}


	public Integer student() {
		return sch_year_id;
	}


	public String getM1() {
		return m1;
	}


	public String getM2() {
		return m2;
	}


	public String getM3() {
		return m3;
	}


	public String getM4() {
		return m4;
	}


	public String getM5() {
		return m5;
	}


	public String getM6() {
		return m6;
	}


	public String getM7() {
		return m7;
	}


	public String getM8() {
		return m8;
	}


	public String getM9() {
		return m9;
	}


	public String getM10() {
		return m10;
	}


	public String getM11() {
		return m11;
	}


	public String getM12() {
		return m12;
	}


	public String getM13() {
		return m13;
	}


	public String getM14() {
		return m14;
	}


	public String getM15() {
		return m15;
	}


	public String getM16() {
		return m16;
	}


	public String getM17() {
		return m17;
	}


	public String getM18() {
		return m18;
	}


	public String getM19() {
		return m19;
	}


	public String getM20() {
		return m20;
	}




	public String getStd_photo() {
		return std_photo;
	}


	public String getStd_nickname() {
		return std_nickname;
	}


	

	public void setId(Integer id) {
		this.id = id;
	}


	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}


	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}


	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}


	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}


	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}


	public void setSch_year_id(Integer sch_year_id) {
		this.sch_year_id = sch_year_id;
	}


	public void setM1(String m1) {
		this.m1 = m1;
	}


	public void setM2(String m2) {
		this.m2 = m2;
	}


	public void setM3(String m3) {
		this.m3 = m3;
	}


	public void setM4(String m4) {
		this.m4 = m4;
	}


	public void setM5(String m5) {
		this.m5 = m5;
	}


	public void setM6(String m6) {
		this.m6 = m6;
	}


	public void setM7(String m7) {
		this.m7 = m7;
	}


	public void setM8(String m8) {
		this.m8 = m8;
	}


	public void setM9(String m9) {
		this.m9 = m9;
	}


	public void setM10(String m10) {
		this.m10 = m10;
	}


	public void setM11(String m11) {
		this.m11 = m11;
	}


	public void setM12(String m12) {
		this.m12 = m12;
	}


	public void setM13(String m13) {
		this.m13 = m13;
	}


	public void setM14(String m14) {
		this.m14 = m14;
	}


	public void setM15(String m15) {
		this.m15 = m15;
	}


	public void setM16(String m16) {
		this.m16 = m16;
	}


	public void setM17(String m17) {
		this.m17 = m17;
	}


	public void setM18(String m18) {
		this.m18 = m18;
	}


	public void setM19(String m19) {
		this.m19 = m19;
	}


	public void setM20(String m20) {
		this.m20 = m20;
	}


	


	public void setStd_photo(String std_photo) {
		this.std_photo = std_photo;
	}


	public void setStd_nickname(String std_nickname) {
		this.std_nickname = std_nickname;
	}




	public String getSubject_name() {
		return subject_name;
	}


	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}


	public Integer getSch_year_id() {
		return sch_year_id;
	}
	
	@Formula("(SELECT t.fullname FROM user t WHERE t.id = student_id)")
	private String std_fullname;
	public String getStd_fullname() {
		return std_fullname;
	}
	public void setStd_fullname(String std_fullname) {
		this.std_fullname = std_fullname;
	}
	public String printActLog(){
		//String className = this.getClass().getSimpleName();
		StringBuffer ret = new StringBuffer();
		try{
			ret.append("class_id:");
			ret.append(this.class_id== null?"null":this.class_id.intValue());
			ret.append("\n");
			
			ret.append("student_id:");
			ret.append(this.student_id== null?"null":this.student_id.intValue());
			ret.append("\n");
			
			ret.append("student_name:");
			ret.append(this.student_name== null?"null":this.student_name);
			ret.append("\n");
			
			ret.append("subject_id:");
			ret.append(this.subject_id== null?"null":this.subject_id.intValue());
			ret.append("\n");
			
			ret.append("subject_name:");
			ret.append(this.subject_name== null?"null":this.subject_name);
			ret.append("\n");
			
			ret.append("sch_year_id:");
			ret.append(this.sch_year_id== null?"null":this.sch_year_id.intValue());
			ret.append("\n");
			
			if (this.m1 != null ){
				ret.append("m1:");
				ret.append(this.m1== null?"null":this.m1);
				ret.append("\n");
			}

			if (this.m2 != null ){
				ret.append("m2:");
				ret.append(this.m2== null?"null":this.m2);
				ret.append("\n");
			}
			
			if (this.m3 != null ){
				ret.append("m3:");
				ret.append(this.m3== null?"null":this.m3);
				ret.append("\n");
			}
			if (this.m4 != null ){
				ret.append("m4:");
				ret.append(this.m4== null?"null":this.m4);
				ret.append("\n");
			}
//			if (this.m5 != null ){
//				ret.append("m5:");
//				ret.append(this.m5== null?"null":this.m5);
//				ret.append("\n");
//			}
//			
			if (this.m6 != null ){
				ret.append("m6:");
				ret.append(this.m6== null?"null":this.m6);
				ret.append("\n");
			}
//			
//			if (this.m7 != null ){
//				ret.append("m7:");
//				ret.append(this.m7== null?"null":this.m7);
//				ret.append("\n");
//			}
			
			if (this.m8 != null ){
				ret.append("m8:");
				ret.append(this.m8== null?"null":this.m8);
				ret.append("\n");
			}
			
			if (this.m9 != null ){
				ret.append("m9:");
				ret.append(this.m9== null?"null":this.m9);
				ret.append("\n");
			}
			
			if (this.m10 != null ){
				ret.append("m10:");
				ret.append(this.m10== null?"null":this.m10);
				ret.append("\n");
			}
			
			if (this.m11 != null ){
				ret.append("m11:");
				ret.append(this.m11== null?"null":this.m11);
				ret.append("\n");
			}
			
//			if (this.m12 != null ){
//				ret.append("m12:");
//				ret.append(this.m12== null?"null":this.m12);
//				ret.append("\n");
//			}
			if (this.m13 != null ){
				ret.append("m13:");
				ret.append(this.m13== null?"null":this.m13);
				ret.append("\n");
			}
//			if (this.m14 != null ){
//				ret.append("m14:");
//				ret.append(this.m14== null?"null":this.m14);
//				ret.append("\n");
//			}
//			if (this.m15 != null ){
//				ret.append("m15:");
//				ret.append(this.m15== null?"null":this.m15);
//				ret.append("\n");
//			}
			if (this.m16 != null ){
				ret.append("m16:");
				ret.append(this.m16== null?"null":this.m16);
				ret.append("\n");
			}
			if (this.m17 != null ){
				ret.append("m17:");
				ret.append(this.m17== null?"null":this.m17);
				ret.append("\n");
			}
			if (this.m18 != null ){
				ret.append("m18:");
				ret.append(this.m18== null?"null":this.m18);
				ret.append("\n");
			}
			if (this.m19 != null ){
				ret.append("m19:");
				ret.append(this.m19== null?"null":this.m19);
				ret.append("\n");
			}
			if (this.m20 != null ){
				ret.append("m20:");
				ret.append(this.m20== null?"null":this.m20);
				ret.append("\n");
			}
			
			
		}catch (Exception e){
			throw new ESchoolException("Exception when Notify.printActLog(), exception message: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
		return ret.toString();
	}
}
