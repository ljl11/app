package com.example.newenergyvehicle.sendAndReceive;

import java.io.Serializable;

public class SendAndReceiveInfo implements Serializable{
	private String plateNumber;
	private int unread;
	private String time;
	private String trouble_detail;
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public int getUnread() {
		return unread;
	}
	public void setUnread(int unread) {
		this.unread = unread;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTrouble_detail() {
		return trouble_detail;
	}
	public void setTrouble_detail(String trouble_detail) {
		this.trouble_detail = trouble_detail;
	}
}
