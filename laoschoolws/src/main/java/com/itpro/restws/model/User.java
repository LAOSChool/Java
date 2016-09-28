package com.itpro.restws.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.WhereJoinTable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itpro.restws.helper.Utils;

@Entity
@Table(name="user")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 
public class User extends AbstractModel {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	Integer  id;

	
	@Column(name="sso_id", unique = true)
	private String sso_id;
	
	@JsonIgnore
	@Column(name="password")
	private String password;
		
	@Column(name="fullname")
	private String fullname;

	@Column(name="nickname")
	private String nickname;

	//0:Pending;1:Active;2:Suspense;3:Closed
	@Column(name="state")
	private Integer state;

	@Column(name="school_id")
	private Integer school_id;
	

	@Column(name="roles")
	private String roles;
	
	@Column(name="addr1")
	private String addr1;
	
	@Column(name="addr2")
	private String addr2;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="ext")
	private String ext;
	
	@Column(name="photo")
	private String photo;
	
	@Column(name="birthday")
	private String birthday;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="email", nullable=true)
	private String email;
	
	@Column(name="std_contact_name", nullable=true)
	private String std_contact_name;
	
	@Column(name="std_contact_phone", nullable=true)
	private String std_contact_phone;
	
	@Column(name="std_contact_email", nullable=true)
	private String std_contact_email;
	
	@Column(name="std_payment_dt", nullable=true)
	private String std_payment_dt;
	
	@Column(name="std_valid_through_dt", nullable=true)
	private String std_valid_through_dt;
	
	@Column(name="std_graduation_dt", nullable=true)
	private String std_graduation_dt;
	
	@Column(name="std_parent_name", nullable=true)
	private String std_parent_name;
	
	@Column(name="cls_level")
	private Integer cls_level;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToMany(fetch = FetchType.EAGER) //EAGER=fetch immediately;LAZY = fetch when needed
	@JoinTable(name = "user2class", 
             joinColumns        = { @JoinColumn(name = "user_id") }, 
             inverseJoinColumns = { @JoinColumn(name = "class_id") })
	// @Where (applied on the target entity)
	//@WhereJoinTable (applied on the association table)
	@WhereJoinTable(clause="actflg='A' AND closed = 0")
	@OrderBy(clause="id")
	private Set<EClass> classes = new HashSet<EClass>();
	public Set<EClass> getClasses() {
		return classes;
	}
	
	@JsonIgnore
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToMany(fetch = FetchType.EAGER) //EAGER=fetch immediately;LAZY = fetch when needed
	@JoinTable(name = "user2class", 
             joinColumns        = { @JoinColumn(name = "user_id") }, 
             inverseJoinColumns = { @JoinColumn(name = "class_id") })
	@WhereJoinTable(clause="actflg='A' AND closed = 1")
	@OrderBy(clause="id")
	private Set<EClass> classes_old = new HashSet<EClass>();
	public Set<EClass> getClasses_old() {
		return classes_old;
	}
	
	
	@Formula("(SELECT t.title FROM school t WHERE t.id = school_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String SchoolName;	
	
	@Transient
	private ArrayList<Permit> permisions;
	

	
	@Transient
	@JsonIgnore
	private ApiKey api_key;
	
	@Transient
	private String default_pass;
	
// 	Khong cho hien thi password trong JSON
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	// Cho phep truyen password qua JSON de store vao DB
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getSso_id() {
		return sso_id;
	}


	public void setSso_id(String sso_id) {
		this.sso_id = sso_id;
	}

	

	public String getFullname() {
		return fullname;
	}


	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public Integer getState() {
		return state;
	}



	public Integer getSchool_id() {
		return school_id;
	}


	public void setSchool_id(Integer schoo_id) {
		this.school_id = schoo_id;
	}




	public String getRoles() {
		return roles;
	}


	public void setRoles(String roles) {
		this.roles = roles;
	}


	public String getAddr1() {
		return addr1;
	}


	public String getAddr2() {
		return addr2;
	}


	public String getPhone() {
		return phone;
	}


	public String getExt() {
		return ext;
	}


	public String getPhoto() {
		return photo;
	}


	public String getBirthday() {
		return birthday;
	}


	public String getGender() {
		return gender;
	}


	public String getEmail() {
		return email;
	}


	public String getStd_contact_name() {
		return std_contact_name;
	}


	public String getStd_contact_phone() {
		return std_contact_phone;
	}


	public String getStd_contact_email() {
		return std_contact_email;
	}


	public String getStd_payment_dt() {
		return std_payment_dt;
	}


	public String getStd_valid_through_dt() {
		return std_valid_through_dt;
	}


	public String getStd_graduation_dt() {
		return std_graduation_dt;
	}


	public String getStd_parent_name() {
		return std_parent_name;
	}


	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}


	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public void setExt(String ext) {
		this.ext = ext;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setStd_contact_name(String std_contact_name) {
		this.std_contact_name = std_contact_name;
	}


	public void setStd_contact_phone(String std_contact_phone) {
		this.std_contact_phone = std_contact_phone;
	}


	public void setStd_contact_email(String std_contact_email) {
		this.std_contact_email = std_contact_email;
	}


	public void setStd_payment_dt(String std_payment_dt) {
		this.std_payment_dt = std_payment_dt;
	}


	public void setStd_valid_through_dt(String std_valid_through_dt) {
		this.std_valid_through_dt = std_valid_through_dt;
	}


	public void setStd_graduation_dt(String std_graduation_dt) {
		this.std_graduation_dt = std_graduation_dt;
	}


	public void setStd_parent_name(String std_parent_name) {
		this.std_parent_name = std_parent_name;
	}


	
	public String getSchoolName() {
		return SchoolName;
	}
	
	
	public void setDefault_pass(String default_pass) {
		this.default_pass = default_pass;
	}
 	
	
	
	
	public String eClassesToString() {
		if (classes == null ){
			return "";
		}
		String ret = "";
		for (EClass e: classes){
			ret = ret + e.getId() +",";
		}
		ret = Utils.removeTxtLastComma(ret);
		return ret;
	}
	public List<Integer> eClassesListID() {
		if (this.classes == null ){
			return null;
		}
		ArrayList<Integer>  list = new ArrayList<>();
    	for (EClass e : classes){
    		list.add(e.getId());
    	}
    	return list;
	}
	public boolean hasRole(String filter_roles){
		if (this.roles==null || roles.equals("")){
			return false;
		}
		if (filter_roles==null || filter_roles.equals("")){
			return false;
		}
		for (String txt : roles.split(",")){
			
			for (String filter : filter_roles.split(",")){
				if (filter.equalsIgnoreCase(txt)){
					return true;
				}	
			}
				
		}
		return false;
	}
	

	public boolean is_belong2class(Integer class_id){
		if (classes == null){
			return false;
		}
		for (EClass eclass: classes){
			if (eclass.getId().intValue() == class_id.intValue()){
				return true;
			}
		}
		return false;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setClasses(Set<EClass> classes) {
		this.classes = classes;
	}

	public ArrayList<Permit> getPermisions() {
		return permisions;
	}
	public void setPermisions(ArrayList<Permit> permisions) {
		this.permisions = permisions;
	}
	public String getDefault_pass() {
		return default_pass;
	}
	public void setSchoolName(String schoolName) {
		SchoolName = schoolName;
	}

	public Integer getCls_level() {
		return cls_level;
	}

	public void setCls_level(Integer cls_level) {
		this.cls_level = cls_level;
	}

	

	public ApiKey getApi_key() {
		return api_key;
	}



	public void setApi_key(ApiKey api_key) {
		this.api_key = api_key;
	}
}
