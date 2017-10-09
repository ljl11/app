package com.example.newenergyvehicle.salesPersonnel;

import java.io.Serializable;

public class FaultHandingEmergencyInfo implements Serializable{
	private String plateNumber;
	private String releasePeople;
	private String taskTime;
	private int state; 
	private String year;
	private String hour;
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getReleasePeople() {
		return releasePeople;
	}
	public void setReleasePeople(String releasePeople) {
		this.releasePeople = releasePeople;
	}
	public String getTaskTime() {
		return taskTime;
	}
	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}
	
}
