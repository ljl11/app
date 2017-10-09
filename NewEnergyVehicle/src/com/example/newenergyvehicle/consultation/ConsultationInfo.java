package com.example.newenergyvehicle.consultation;

import java.io.Serializable;

public class ConsultationInfo implements Serializable{
	private String badNum;
	private String goodNum;
	private String replyCount;
	private String time;
	private String title;
	private String content;
	private String personName;
	private String id;
	private String replyId;
	private String post;
	private String replyTime;
	private String state;
	
	public ConsultationInfo(){
		
	}

	public ConsultationInfo(String id,String badNum, String goodNum, String replyCount,
			String time, String title, String content,
			String personName ,String state,String replyId,String post ,String replyTime) {
		super();
		this.id = id;
		this.badNum = badNum;
		this.goodNum = goodNum;
		this.replyCount = replyCount;
		this.time = time;
		this.title = title;
		this.content = content;
		this.personName = personName;
		this.state =state;
		this.replyId = replyId;
		this.post = post;
		this.replyTime = replyTime;
	}

	public String getBadNum() {
		return badNum;
	}

	public void setBadNum(String badNum) {
		this.badNum = badNum;
	}

	public String getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}

	public String getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(String replyCount) {
		this.replyCount = replyCount;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}


}
