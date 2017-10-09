package com.example.newenergyvehicle.salesPersonnel;

import java.io.Serializable;

public class ShuttleMissionInfo implements Serializable {
	private String id;
	private String plateNumber;
	private int unread;
	private String time;
	private String location;
	private int taskType;
	private String vehicleId;
	private String userId;
	private String carDeliveryId;
	private String deliveryObjectNmae;
	private String userPhone;
	private String chargePhone;
	private String currentPhone;

	public String getCarDeliveryId() {
		return carDeliveryId;
	}

	public void setCarDeliveryId(String carDeliveryId) {
		this.carDeliveryId = carDeliveryId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDeliveryObjectNmae() {
		return deliveryObjectNmae;
	}

	public void setDeliveryObjectNmae(String deliveryObjectNmae) {
		this.deliveryObjectNmae = deliveryObjectNmae;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getChargePhone() {
		return chargePhone;
	}

	public void setChargePhone(String chargePhone) {
		this.chargePhone = chargePhone;
	}

	public String getCurrentPhone() {
		return currentPhone;
	}

	public void setCurrentPhone(String currentPhone) {
		this.currentPhone = currentPhone;
	}
	
}
