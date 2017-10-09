package com.example.newenergyvehicle.faultHand;

import java.io.Serializable;

public class FaultHandHistoryItemInfo implements Serializable {
	private String history_content;
	private String history_place;
	private String history_type;
	private String time;
	private String state;
	private String userName;
	private String userPhone;
	private String plate_num;
	private String id;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlate_num() {
		return plate_num;
	}

	public void setPlate_num(String plate_num) {
		this.plate_num = plate_num;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getHistory_place() {
		return history_place;
	}

	public void setHistory_place(String history_place) {
		this.history_place = history_place;
	}

	public String getHistory_type() {
		return history_type;
	}

	public void setHistory_type(String history_type) {
		this.history_type = history_type;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHistory_content() {
		return history_content;
	}

	public void setHistory_content(String history_content) {
		this.history_content = history_content;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
