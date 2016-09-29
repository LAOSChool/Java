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

import com.itpro.restws.helper.Constant;

@Entity
@Table(name="exam_rank")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class ExamRank extends AbstractModel{
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
	
	public String getByExKey(String ex_key){
		
		if (ex_key.equalsIgnoreCase("m1")){
			return getM1();
		}
		if (ex_key.equalsIgnoreCase("m2")){
			return getM2();
		}
		if (ex_key.equalsIgnoreCase("m3")){
			return getM3();
		}
		if (ex_key.equalsIgnoreCase("m4")){
			return getM4();
		}
		if (ex_key.equalsIgnoreCase("m5")){
			return getM5();
		}
		if (ex_key.equalsIgnoreCase("m6")){
			return getM6();
		}
		if (ex_key.equalsIgnoreCase("m7")){
			return getM7();
		}
		if (ex_key.equalsIgnoreCase("m8")){
			return getM8();
		}
		if (ex_key.equalsIgnoreCase("m9")){
			return getM9();
		}
		if (ex_key.equalsIgnoreCase("m10")){
			return getM10();
		}
		if (ex_key.equalsIgnoreCase("m11")){
			return getM11();
		}
		if (ex_key.equalsIgnoreCase("m12")){
			return getM12();
		}
		if (ex_key.equalsIgnoreCase("m13")){
			return getM13();
		}
		if (ex_key.equalsIgnoreCase("m14")){
			return getM14();
		}
		if (ex_key.equalsIgnoreCase("m15")){
			return getM15();
		}
		if (ex_key.equalsIgnoreCase("m16")){
			return getM16();
		}
		if (ex_key.equalsIgnoreCase("m17")){
			return getM17();
		}
		if (ex_key.equalsIgnoreCase("m18")){
			return getM18();
		}
		if (ex_key.equalsIgnoreCase("m19")){
			return getM19();
		}
		if (ex_key.equalsIgnoreCase("m20")){
			return getM20();
		}
		return null;
	}
	
}
