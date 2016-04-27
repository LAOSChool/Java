package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="notify_img")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 

public class NotifyImg extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

//	@NotFound(action=NotFoundAction.IGNORE)
//	@ManyToOne (optional=true)
//	@ManyToOne(optional=true)
//	@JoinColumn(name = "task_id", referencedColumnName = "task_id")
//    private Notify notify;

	@Column(name="notify_id")
	private Integer notify_id;

	@Column(name="user_id")
	private Integer user_id;
	
	
	@Column(name="upload_dt")
	private String upload_dt;
	
	@Column(name="file_name")
	private String file_name;
	
	@Column(name="file_path")
	private String file_path;
	
	@Column(name="file_url")
	private String file_url;
	
	@Column(name="caption")
	private String caption;
	
	
	@Column(name="task_id")
	private Integer task_id;// ID of group notify/message (sending to class or school)
	
	

	@Column(name="idx")
	private Integer idx;
	
	
	public Integer getId() {
		return id;
	}

	public Integer getNotify_id() {
		return notify_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public String getUpload_dt() {
		return upload_dt;
	}

	public String getFile_name() {
		return file_name;
	}

	public String getFile_path() {
		return file_path;
	}

	public String getFile_url() {
		return file_url;
	}

	public String getCaption() {
		return caption;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNotify_id(Integer notify_id) {
		this.notify_id = notify_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public void setUpload_dt(String upload_dt) {
		this.upload_dt = upload_dt;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer order) {
		this.idx = order;
	}
	
	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}
	
}
