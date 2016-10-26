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
import org.springframework.http.HttpStatus;

import com.itpro.restws.helper.ESchoolException;

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
	
	@Column(name="dest_type")
	private Integer dest_type;//0:Person;1: Class;2:School
	
	@Column(name="task_id")
	private Integer task_id;// ID of group notify/message (sending to class or school)

	@Column(name="to_sso_id")
	private String to_sso_id;
	
	@Column(name="from_sso_id")
	private String from_sso_id;
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

	

	public String getContent() {
		return content;
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



	public void setContent(String content) {
		this.content = content;
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

	public Integer getDest_type() {
		return dest_type;
	}

	public void setDest_type(Integer dest_type) {
		this.dest_type = dest_type;
	}
	
	
	public Notify copy(){
		Notify copy = new Notify();
		copy.setSchool_id(this.school_id);
		copy.setClass_id(this.class_id);
		copy.setFrom_usr_id(this.from_user_id);
		copy.setFrom_user_name(this.from_user_name);
		copy.setFrom_sso_id(this.from_sso_id);
		
		
		copy.setTo_user_id(this.to_user_id);
		copy.setTo_user_name(this.to_user_name);
		copy.setTo_sso_id(this.to_sso_id);
		
		
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

	public Integer getFrom_user_id() {
		return from_user_id;
	}

	public Integer getTo_user_id() {
		return to_user_id;
	}

	public String getTo_sso_id() {
		return to_sso_id;
	}

	public void setFrom_user_id(Integer from_user_id) {
		this.from_user_id = from_user_id;
	}

	public void setTo_user_id(Integer to_user_id) {
		this.to_user_id = to_user_id;
	}

	public void setTo_sso_id(String to_sso_id) {
		this.to_sso_id = to_sso_id;
	}
	public String printActLog(){
		//String className = this.getClass().getSimpleName();
		StringBuffer ret = new StringBuffer();
		try{
			ret.append("from_user_id:");
			ret.append(this.from_user_id== null?"null":this.from_user_id.intValue());
			ret.append("\n");
			ret.append("from_user_sso:");
			ret.append(this.from_sso_id== null?"null":this.from_sso_id);
			ret.append("\n");
			
			ret.append("to_class_id:");
			ret.append(this.class_id== null?"null":this.class_id.intValue());
			ret.append("\n");
			
			if (this.title != null){
				ret.append("title:");
				ret.append(this.title== null?"null":this.title);
				ret.append("\n");
			}
			if (this.content != null){
				ret.append("content:");
				ret.append(this.content== null?"null":this.content);
				ret.append("\n");
			}
			
			if (this.notifyImages != null && this.notifyImages.size() > 0){
				for (NotifyImg img: this.notifyImages){
					ret.append("image:"+img.getFile_url()==null?"null":img.getFile_url());
					ret.append("\n");
					ret.append("caption:"+img.getCaption()==null?"null":img.getCaption());
					ret.append("\n");
				}
			}
			
		}catch (Exception e){
			throw new ESchoolException("Exception when Notify.printActLog(), exception message: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
		return ret.toString();
	}

	public String getFrom_sso_id() {
		return from_sso_id;
	}

	public void setFrom_sso_id(String from_sso_id) {
		this.from_sso_id = from_sso_id;
	}
	
}
