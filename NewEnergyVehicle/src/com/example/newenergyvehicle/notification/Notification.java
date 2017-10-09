package com.example.newenergyvehicle.notification;

import java.io.Serializable;

public class Notification implements Serializable{
	private String id;
	private String time;
	private String title;
	private Integer is_read;
	private String type;
	private String content;
	private Integer isDetele;
	public Notification(){
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Notification(String time, String title, Integer is_read, String type,
			String content, Integer isDetele) {
		this.time = time;
		this.title = title;
		this.is_read = is_read;
		this.type = type;
		this.content = content;
		this.isDetele = isDetele;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getIs_read() {
		return is_read;
	}
	public void setIs_read(Integer is_read) {
		this.is_read = is_read;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getIsDetele() {
		return isDetele;
	}
	public void setIsDetele(Integer isDetele) {
		this.isDetele = isDetele;
	}
}
