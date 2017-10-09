package com.example.newenergyvehicle.vehicleInfo;

import java.io.Serializable;

public class MaintenanceReminderInfo implements Serializable{
	private String itemName;// 保养项
	private String lastMaintainDate;//最后一次保养时间
	private String lastMaintainMileage;//最后一次保养里程
	private int maintenanceState;//0代表需要保养1代表即将需要保养2代表不需要保养
	private String outTime; // 超出时间(距上一次保养时间的天数)
	private String mileageInterval;// 保养里程间隔
	private String curMileage; // 初始里程
	private String timeInterval; // 保养时间间隔
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getLastMaintainDate() {
		return lastMaintainDate;
	}
	public void setLastMaintainDate(String lastMaintainDate) {
		this.lastMaintainDate = lastMaintainDate;
	}
	public String getLastMaintainMileage() {
		return lastMaintainMileage;
	}
	public void setLastMaintainMileage(String lastMaintainMileage) {
		this.lastMaintainMileage = lastMaintainMileage;
	}
	public int getMaintenanceState() {
		return maintenanceState;
	}
	public void setMaintenanceState(int maintenanceState) {
		this.maintenanceState = maintenanceState;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public String getMileageInterval() {
		return mileageInterval;
	}
	public void setMileageInterval(String mileageInterval) {
		this.mileageInterval = mileageInterval;
	}
	public String getCurMileage() {
		return curMileage;
	}
	public void setCurMileage(String curMileage) {
		this.curMileage = curMileage;
	}
	public String getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}
	
	
}
