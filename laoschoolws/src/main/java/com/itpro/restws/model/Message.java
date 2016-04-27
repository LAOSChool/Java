package com.itpro.restws.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.itpro.restws.helper.Utils;

@Entity
@Table(name="message")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

//@SecondaryTables({
//    @SecondaryTable(name="m_term", pkJoinColumns={
//        @PrimaryKeyJoinColumn(name="id", referencedColumnName="term_id") })
//})
public class Message extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="school_id")
	private Integer school_id;

	@Column(name="class_id")
	private Integer class_id;
	
	
	@Column(name="from_user_id")
	private Integer from_user_id;
	
	@Column(name="from_user_name")
	private String from_user_name;
	
	@Column(name="to_user_id")
	private Integer to_user_id;
	
	@Column(name="to_user_name")
	private String to_user_name;
	
	@Column(name="content")
	private String content;
	
	
	@Column(name="msg_type_id")
	private Integer msg_type_id;
	
	@Column(name="channel")
	private Integer channel;
	
	@Column(name="is_sent")
	private Integer is_sent;
	
	@Column(name="sent_dt")
	private String sent_dt;
	
	@Column(name="is_read")
	private Integer is_read;
	
	@Column(name="read_dt")
	private String read_dt;
	
	@Column(name="imp_flg")
	private Integer imp_flg;
	
	@Column(name="other")
	private String other;
	
	@Column(name="title")
	private String title;

	@Column(name="dest_type")
	private Integer dest_type;//0:Person;1: Class;2:School
	
	@Column(name="task_id")
	private Integer task_id;
	
	@Column(name="frm_user_photo")
	private String frm_user_photo;
	

	@Formula("(SELECT t.title FROM school t WHERE t.id = school_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String SchoolName;	
	public String getSchoolName() {
		return SchoolName;
	}
	
	@Formula("(SELECT t.sval FROM m_msg_type t WHERE t.id = msg_type_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String message_type;	
	public String getMessageType() {
		return message_type;
	}

	
	public Message copy(){
		Message copy = new Message();
		copy.setSchool_id(this.school_id);
		copy.setClass_id(this.class_id);
		copy.setFrom_usr_id(this.from_user_id);
		copy.setFrom_user_name(this.from_user_name);
		copy.setTo_usr_id(this.to_user_id);
		copy.setTo_user_name(this.to_user_name);
		copy.setTitle(this.title);
		copy.setMsg_type_id(this.msg_type_id);
		copy.setChannel(this.channel);
		copy.setContent(this.getContent());
		copy.setImp_flg(this.imp_flg);
		copy.setOther(this.other);
		
		// Status
//		copy.setIs_sent(this.is_sent);
//		copy.setIs_read(this.is_read);
		copy.setImp_flg(this.imp_flg);
		
		
		copy.setDest_type(this.getDest_type());
		copy.setTask_id(this.getTask_id());
		
		
		return copy;
	}
	@Transient
	private String cc_list;
	
	public String getCc_list() {
		return cc_list;
	}


	public void setCc_list(String cc_list) {
		this.cc_list = cc_list;
	}


	public Integer getId() {
		return id;
	}


	public Integer getSchool_id() {
		return school_id;
	}


	public Integer getClass_id() {
		return class_id;
	}


	public Integer getFrom_usr_id() {
		return from_user_id;
	}


	public String getFrom_user_name() {
		return from_user_name;
	}


	public Integer getTo_usr_id() {
		return to_user_id;
	}


	public String getTo_user_name() {
		return to_user_name;
	}


	public String getContent() {
		return content;
	}


	public Integer getMsg_type_id() {
		return msg_type_id;
	}


	public Integer getChannel() {
		return channel;
	}


	public Integer getIs_sent() {
		return is_sent;
	}


	public String getSent_dt() {
		return sent_dt;
	}


	public Integer getIs_read() {
		return is_read;
	}


	public String getRead_dt() {
		return read_dt;
	}


	public Integer getImp_flg() {
		return imp_flg;
	}


	public String getOther() {
		return other;
	}


	public String getTitle() {
		return title;
	}


	public String getMessage_type() {
		return message_type;
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


	public void setFrom_usr_id(Integer from_usr_id) {
		this.from_user_id = from_usr_id;
	}


	public void setFrom_user_name(String from_user_name) {
		this.from_user_name = from_user_name;
	}


	public void setTo_usr_id(Integer to_usr_id) {
		this.to_user_id = to_usr_id;
	}


	public void setTo_user_name(String to_user_name) {
		this.to_user_name = to_user_name;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public void setMsg_type_id(Integer msg_type_id) {
		this.msg_type_id = msg_type_id;
	}


	public void setChannel(Integer channel) {
		this.channel = channel;
	}


	public void setIs_sent(Integer is_sent) {
		this.is_sent = is_sent;
	}


	public void setSent_dt(String sent_dt) {
		this.sent_dt = sent_dt;
	}


	public void setIs_read(Integer is_read) {
		this.is_read = is_read;
	}


	public void setRead_dt(String read_dt) {
		this.read_dt = read_dt;
	}


	public void setImp_flg(Integer imp_flg) {
		this.imp_flg = imp_flg;
	}


	public void setOther(String other) {
		this.other = other;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public void setSchoolName(String schoolName) {
		SchoolName = schoolName;
	}


	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	
	public List<Integer> getCCList(){
		ArrayList<Integer> ret = new ArrayList<Integer>(); 
		if (this.cc_list == null || this.cc_list.equals("")){
			return ret;
		}
		for (String cc_txt :cc_list.split(",") ){
			ret.add(Utils.parseLong(cc_txt));
		}
		return ret;
	}
	
	public Integer getDest_type() {
		return dest_type;
	}

	public void setDest_type(Integer dest_type) {
		this.dest_type = dest_type;
	}


	public Integer getTask_id() {
		return task_id;
	}


	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}


	public Integer getFrom_user_id() {
		return from_user_id;
	}


	public Integer getTo_user_id() {
		return to_user_id;
	}


	public String getFrm_user_photo() {
		return frm_user_photo;
	}


	public void setFrom_user_id(Integer from_user_id) {
		this.from_user_id = from_user_id;
	}


	public void setTo_user_id(Integer to_user_id) {
		this.to_user_id = to_user_id;
	}


	public void setFrm_user_photo(String frm_user_photo) {
		this.frm_user_photo = frm_user_photo;
	}
}
