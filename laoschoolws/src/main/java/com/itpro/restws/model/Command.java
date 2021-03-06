package com.itpro.restws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.SelectBeforeUpdate;


@NamedNativeQuery(
	    name="getCommandSQL", 
	    query="call get_unproc_command()", 
	    callable=true, 
	    readOnly=true, 
	    resultClass=Command.class
)





@Entity
@Table(name="command")
@DynamicInsert(value=true)
@DynamicUpdate(value=true)
@SelectBeforeUpdate(value=true) 



public class Command extends AbstractModel{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="school_id")
	private Integer school_id;
	
	@Column(name="cmd_dt")
	private String cmd_dt;

	@Column(name="command")
	private String command;

	@Column(name="cmd_type")
	private Integer cmd_type;

	@Column(name="params")
	private String params;

	@Column(name="processed")
	private Integer processed;

	@Column(name="processed_dt")
	private String processed_dt;
	
	@Column(name="success")
	private int success;
	
	@Column(name="message")
	private String message;

	public Integer getId() {
		return id;
	}

	public String getCmd_dt() {
		return cmd_dt;
	}

	public String getCommand() {
		return command;
	}

	public Integer getCmd_type() {
		return cmd_type;
	}

	public String getParams() {
		return params;
	}

	public Integer getProcessed() {
		return processed;
	}

	public String getProcessed_dt() {
		return processed_dt;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCmd_dt(String cmd_dt) {
		this.cmd_dt = cmd_dt;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setCmd_type(Integer cmd_type) {
		this.cmd_type = cmd_type;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	public void setProcessed_dt(String processed_dt) {
		this.processed_dt = processed_dt;
	}
	@Override
	public
	String toString(){
		return "command="+this.command+" " + "param"+this.params==null?"":this.params;
	}

	public int getSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
