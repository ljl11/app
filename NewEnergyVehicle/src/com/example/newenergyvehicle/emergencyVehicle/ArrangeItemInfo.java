package com.example.newenergyvehicle.emergencyVehicle;

import java.io.Serializable;

public class ArrangeItemInfo implements Serializable {
	private String id;
	private String vehicleId;
	private String plateNumber;
	private String taskTime;
	private String fullDate;
	private String name;
	private String taskPlace;
	private String releasePeople;
	private String leglePeople;
	private String taskType;
	private int state;
	private String faultRcordId;

	public String getFaultRcordId() {
		return faultRcordId;
	}

	public void setFaultRcordId(String faultRcordId) {
		this.faultRcordId = faultRcordId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTaskPlace() {
		return taskPlace;
	}

	public void setTaskPlace(String taskPlace) {
		this.taskPlace = taskPlace;
	}

	public String getReleasePeople() {
		return releasePeople;
	}

	public void setReleasePeople(String releasePeople) {
		this.releasePeople = releasePeople;
	}

	public String getLeglePeople() {
		return leglePeople;
	}

	public void setLeglePeople(String leglePeople) {
		this.leglePeople = leglePeople;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

}
