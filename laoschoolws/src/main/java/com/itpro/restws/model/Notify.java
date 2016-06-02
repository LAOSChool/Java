package com.itpro.restws.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="notify")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class Notify extends AbstractModel{
	
	
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
	
	@Column(name="title")
	private String title;
	
	@Column(name="content")
	private String content;
	
	
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
	
	@Column(name="dest_type")
	private int dest_type;//0:Person;1: Class;2:School
	
	@Column(name="task_id")
	private Integer task_id;// ID of group notify/message (sending to class or school)

//
//	@JsonIgnore
//	@Column(name="images")
//	private String images="";
	


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

	public Integer getTo_usr_id() {
		return to_user_id;
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

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public void setFrom_usr_id(Integer from_user_id) {
		this.from_user_id = from_user_id;
	}

	public void setTo_usr_id(Integer to_user_id) {
		this.to_user_id = to_user_id;
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
	

	
//   @OneToMany (fetch = FetchType.EAGER, cascade=CascadeType.ALL)
//   @JoinColumn(name="notify_id") 
//	private Set<NotifyImg> notifyImages = new HashSet<NotifyImg>(0);
//	public Set<NotifyImg> getNotifyImages() {
//		return this.notifyImages;
//	}
//
//	public void setNotifyImages(Set<NotifyImg> files) {
//		this.notifyImages = files;
//	}

	@Transient
	private Set<NotifyImg> notifyImages = new HashSet<NotifyImg>(0);
	public Set<NotifyImg> getNotifyImages() {
		return this.notifyImages;
	}

	public void setNotifyImages(Set<NotifyImg> files) {
		this.notifyImages = files;
	}
	
	 public void addImageToNotify(NotifyImg img) {
		 
		 notifyImages.add(img);
	 }
	 public String getTitle() {
			return title;
		} 
	public void setTitle(String title) {
		this.title = title;
	}

	public int getDest_type() {
		return dest_type;
	}

	public void setDest_type(int dest_type) {
		this.dest_type = dest_type;
	}
	
//	public void addImageID(int id){
//		this.images = this.images + ","+id;
//		if (this.images.startsWith(",")){
//			this.images = this.images.substring(1);
//		}
//	}
//	public String getImages() {
//		return images;
//	}
//
//	public void setImages(String images) {
//		this.images = images;
//	}
	
	public Notify copy(){
		Notify copy = new Notify();
		copy.setSchool_id(this.school_id);
		copy.setClass_id(this.class_id);
		copy.setFrom_usr_id(this.from_user_id);
		copy.setFrom_user_name(this.from_user_name);
		copy.setTo_usr_id(this.to_user_id);
		copy.setTo_user_name(this.to_user_name);
		copy.setTitle(this.title);
		copy.setChannel(this.channel);
		copy.setContent(this.getContent());

		// Status
		copy.setIs_sent(this.is_sent);
		copy.setIs_read(this.is_read);
		copy.setImp_flg(this.imp_flg);
		
		copy.setOther(this.other);
		
		
		copy.setDest_type(this.getDest_type());
		//copy.setImages(this.getImages());
		copy.setTask_id(this.getTask_id());
		
		return copy;
	}
	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}
}
