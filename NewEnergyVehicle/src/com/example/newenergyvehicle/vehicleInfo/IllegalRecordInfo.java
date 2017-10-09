package com.example.newenergyvehicle.vehicleInfo;

import java.io.Serializable;

public class IllegalRecordInfo implements Serializable{
	private String illegalAction;
	private String illegalAddress;
	private String illegalDate;
	private String illegalMoney;
	private String illegalPoint;
	private String vehicleUserName;
	private String vehicleUserPhone;
	public String getIllegalAction() {
		return illegalAction;
	}
	public void setIllegalAction(String illegalAction) {
		this.illegalAction = illegalAction;
	}
	public String getIllegalAddress() {
		return illegalAddress;
	}
	public void setIllegalAddress(String illegalAddress) {
		this.illegalAddress = illegalAddress;
	}
	public String getIllegalDate() {
		return illegalDate;
	}
	public void setIllegalDate(String illegalDate) {
		this.illegalDate = illegalDate;
	}
	public String getIllegalMoney() {
		return illegalMoney;
	}
	public void setIllegalMoney(String illegalMoney) {
		this.illegalMoney = illegalMoney;
	}
	public String getIllegalPoint() {
		return illegalPoint;
	}
	public void setIllegalPoint(String illegalPoint) {
		this.illegalPoint = illegalPoint;
	}
	public String getVehicleUserName() {
		return vehicleUserName;
	}
	public void setVehicleUserName(String vehicleUserName) {
		this.vehicleUserName = vehicleUserName;
	}
	public String getVehicleUserPhone() {
		return vehicleUserPhone;
	}
	public void setVehicleUserPhone(String vehicleUserPhone) {
		this.vehicleUserPhone = vehicleUserPhone;
	}
	
}
