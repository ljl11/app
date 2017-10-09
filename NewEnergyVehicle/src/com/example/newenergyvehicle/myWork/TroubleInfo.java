package com.example.newenergyvehicle.myWork;

import java.io.Serializable;

public class TroubleInfo implements Serializable {
	private String vehicleId;
	private String plateNumber;
	private int unread;
	private String time;
	private String trouble_detail;
	public String id;
	private String location;
	private String distributionId;
	private int isHandle;

	public String getDistributionId() {
		return distributionId;
	}

	public void setDistributionId(String distributionId) {
		this.distributionId = distributionId;
	}

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

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getIsHandle() {
		return isHandle;
	}

	public void setIsHandle(int isHandle) {
		this.isHandle = isHandle;
	}

}
