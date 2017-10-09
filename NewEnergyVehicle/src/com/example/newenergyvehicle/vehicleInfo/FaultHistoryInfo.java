package com.example.newenergyvehicle.vehicleInfo;

import java.io.Serializable;

public class FaultHistoryInfo implements Serializable  {
	private String id;
	private String vehicleId;
	private String faultDescription;
	private String location;
	private String faulTime;
	private String faultType;
	private String faultRecorder;
	private String userId;
	private String state;

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

	public String getFaultDescription() {
		return faultDescription;
	}

	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFaulTime() {
		return faulTime;
	}

	public void setFaulTime(String faulTime) {
		this.faulTime = faulTime;
	}

	public String getFaultType() {
		return faultType;
	}

	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}

	public String getFaultRecorder() {
		return faultRecorder;
	}

	public void setFaultRecorder(String faultRecorder) {
		this.faultRecorder = faultRecorder;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
