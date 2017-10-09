package com.example.newenergyvehicle.vehicleInfo;

import java.io.Serializable;

public class Vehicle_carlist implements Serializable{
	private String id;
	private String carType;
	private String carNum;
	private String vin;
	private String maker;//负责人
	private String type;
	private String mileage;
	private String electricity;
	private String engine_no;
	private String possessor;
	private String vehicleId;
	private String xcoordinate;
	private String ycoordinate;
	private String position;
	private String maintenanceStatus;
	private String paymentStatus;
	private String repairStatus;
	private String violationStatus;
	private String lockState;
	private String speed;
	private String vonline;
	private String time;
	
	public Vehicle_carlist(){
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Vehicle_carlist(String id, String carType, String carNum,
			String vin, String maker, String type, String mileage,
			String electricity,String engine_no) {
		super();
		this.id = id;
		this.carType = carType;
		this.carNum = carNum;
		this.vin = vin;
		this.maker = maker;
		this.type = type;
		this.mileage = mileage;
		this.electricity = electricity;
		this.engine_no = engine_no;
	}


	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getElectricity() {
		return electricity;
	}

	public void setElectricity(String electricity) {
		this.electricity = electricity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEngine_no() {
		return engine_no;
	}

	public void setEngine_no(String engine_no) {
		this.engine_no = engine_no;
	}

	public String getPossessor() {
		return possessor;
	}

	public void setPossessor(String possessor) {
		this.possessor = possessor;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getXcoordinate() {
		return xcoordinate;
	}

	public void setXcoordinate(String xcoordinate) {
		this.xcoordinate = xcoordinate;
	}

	public String getYcoordinate() {
		return ycoordinate;
	}

	public void setYcoordinate(String ycoordinate) {
		this.ycoordinate = ycoordinate;
	}

	public String getMaintenanceStatus() {
		return maintenanceStatus;
	}

	public void setMaintenanceStatus(String maintenanceStatus) {
		this.maintenanceStatus = maintenanceStatus;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getRepairStatus() {
		return repairStatus;
	}

	public void setRepairStatus(String repairStatus) {
		this.repairStatus = repairStatus;
	}

	public String getViolationStatus() {
		return violationStatus;
	}

	public void setViolationStatus(String violationStatus) {
		this.violationStatus = violationStatus;
	}

	public String getLockState() {
		return lockState;
	}

	public void setLockState(String lockState) {
		this.lockState = lockState;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getVonline() {
		return vonline;
	}

	public void setVonline(String vonline) {
		this.vonline = vonline;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
