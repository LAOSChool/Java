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
	private int id;

	
	public int getId() {
		return id;
	}

	public int getSchool_id() {
		return school_id;
	}

	public int getClass_id() {
		return class_id;
	}

	public int getFrom_usr_id() {
		return from_usr_id;
	}

	public int getTo_usr_id() {
		return to_usr_id;
	}

	public String getContent() {
		return content;
	}

	public int getChannel() {
		return channel;
	}

	public int getIs_sent() {
		return is_sent;
	}

	public String getSent_dt() {
		return sent_dt;
	}

	public int getIs_read() {
		return is_read;
	}

	public String getRead_dt() {
		return read_dt;
	}

	public int getImp_flg() {
		return imp_flg;
	}

	public String getOther() {
		return other;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSchool_id(int school_id) {
		this.school_id = school_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public void setFrom_usr_id(int from_usr_id) {
		this.from_usr_id = from_usr_id;
	}

	public void setTo_usr_id(int to_usr_id) {
		this.to_usr_id = to_usr_id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public void setIs_sent(int is_sent) {
		this.is_sent = is_sent;
	}

	public void setSent_dt(String sent_dt) {
		this.sent_dt = sent_dt;
	}

	public void setIs_read(int is_read) {
		this.is_read = is_read;
	}

	public void setRead_dt(String read_dt) {
		this.read_dt = read_dt;
	}

	public void setImp_flg(int imp_flg) {
		this.imp_flg = imp_flg;
	}

	public void setOther(String other) {
		this.other = other;
	}

	@Column(name="school_id")
	private int school_id;

	@Column(name="class_id")
	private int class_id;
	
	
	@Column(name="from_user_id")
	private int from_usr_id;
	
	@Column(name="from_user_name")
	private String from_user_name;
	
	@Column(name="to_user_id")
	private int to_usr_id;
	
	@Column(name="to_user_name")
	private String to_user_name;
	
	@Column(name="content")
	private String content;
	
	
	@Column(name="msg_type_id")
	private int msg_type_id;
	
	@Column(name="channel")
	private int channel;
	
	@Column(name="is_sent")
	private int is_sent;
	
	@Column(name="sent_dt")
	private String sent_dt;
	
	@Column(name="is_read")
	private int is_read;
	
	@Column(name="read_dt")
	private String read_dt;
	
	@Column(name="imp_flg")
	private int imp_flg;
	
	@Column(name="other")
	private String other;
	
	@Column(name="title")
	private String title;
	
	
	
	
	
	
	
	
	@Formula("(SELECT t.title FROM school t WHERE t.id = school_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String SchoolName;	
	public String getSchoolName() {
		return SchoolName;
	}

	public String getTitle() {
		return title;
	}

	public int getMsg_type_id() {
		return msg_type_id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMsg_type_id(int msg_type_id) {
		this.msg_type_id = msg_type_id;
	}
	
	@Formula("(SELECT t.sval FROM m_msg_type t WHERE t.id = msg_type_id)") //@Formula("(SELECT ot1.LABEL FROM OtherTable1 ot1 WHERE ot1.CODE = CODE_FK_1)")
	private String message_type;	
	public String getMessageType() {
		return message_type;
	}

	public String getFrom_user_name() {
		return from_user_name;
	}

	public String getTo_user_name() {
		return to_user_name;
	}

	public void setFrom_user_name(String from_user_name) {
		this.from_user_name = from_user_name;
	}

	public void setTo_user_name(String to_user_name) {
		this.to_user_name = to_user_name;
	}

}
