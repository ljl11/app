package com.example.newenergyvehicle.emergencyVehicle;

import java.io.Serializable;

import android.widget.TextView;

public class CarChooseInfo implements Serializable{
	private String vehicleId;
	private String plateNumber;
	private String holdernName; // 仓库名称
	private String assignLocation; //所在位置
	private String assignObject; //使用对象id
	private String assignObjectName; //对象名称
	private String type;
	private String typeName; //车型名称
	
	
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
	public String getHoldernName() {
		return holdernName;
	}
	public void setHoldernName(String holdernName) {
		this.holdernName = holdernName;
	}
	public String getAssignLocation() {
		return assignLocation;
	}
	public void setAssignLocation(String assignLocation) {
		this.assignLocation = assignLocation;
	}
	public String getAssignObject() {
		return assignObject;
	}
	public void setAssignObject(String assignObject) {
		this.assignObject = assignObject;
	}
	public String getAssignObjectName() {
		return assignObjectName;
	}
	public void setAssignObjectName(String assignObjectName) {
		this.assignObjectName = assignObjectName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
