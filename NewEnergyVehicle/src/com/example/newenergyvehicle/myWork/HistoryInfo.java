package com.example.newenergyvehicle.myWork;

import java.io.Serializable;

import com.example.util.SerachView;

public class HistoryInfo implements Serializable{
	private String id;
	private int statue;
	private String title;
	private String time;
	private String content;
	private String recipientName;
	public HistoryInfo(){
	}
	
	public HistoryInfo(String id, int statue, String title, String time, String content){
		this.id = id;
		this.statue = statue;
		this.time = time;
		this.title = title;
		this.content = content;
	}
	
	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getStatue() {
		return statue;
	}
	public void setStatue(int statue) {
		this.statue = statue;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
