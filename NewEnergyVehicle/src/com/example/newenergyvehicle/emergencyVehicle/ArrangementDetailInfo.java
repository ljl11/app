package com.example.newenergyvehicle.emergencyVehicle;

import java.io.Serializable;

public class ArrangementDetailInfo implements Serializable{
	private String id;
	private String plateNumber;
	private String taskTime;
	private String fullDate;
	private String userName;
	private String taskPlace;
	private String serverName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getTaskTime() {
		return taskTime;
	}
	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}
	public String getFullDate() {
		return fullDate;
	}
	public void setFullDate(String fullDate) {
		this.fullDate = fullDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTaskPlace() {
		return taskPlace;
	}
	public void setTaskPlace(String taskPlace) {
		this.taskPlace = taskPlace;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
   
	
	
}
