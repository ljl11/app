package com.example.newenergyvehicle.check;

import java.io.Serializable;

public class CheckInfo implements Serializable {
	private String id; // id
	private String state; // 申请状态
	private String apply_user; // 申请人
	private String apply_reason; // 申请原因
	private String emergency_vehicle_allocation_id; // 应急车id
	private String apply_address; // 派送地点
	private String apply_time; // 派送时间
	private String charge;
	private int unread;
	private String uId;
	private String result;
	private String faultRecordId; //故障记录id

	public String getFaultRecordId() {
		return faultRecordId;
	}

	public void setFaultRecordId(String faultRecordId) {
		this.faultRecordId = faultRecordId;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getApply_user() {
		return apply_user;
	}

	public void setApply_user(String apply_user) {
		this.apply_user = apply_user;
	}

	public String getApply_reason() {
		return apply_reason;
	}

	public void setApply_reason(String apply_reason) {
		this.apply_reason = apply_reason;
	}

	public String getEmergency_vehicle_allocation_id() {
		return emergency_vehicle_allocation_id;
	}

	public void setEmergency_vehicle_allocation_id(
			String emergency_vehicle_allocation_id) {
		this.emergency_vehicle_allocation_id = emergency_vehicle_allocation_id;
	}

	public String getApply_address() {
		return apply_address;
	}

	public void setApply_address(String apply_address) {
		this.apply_address = apply_address;
	}

	public String getApply_time() {
		return apply_time;
	}

	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public int getUnread() {
		return unread;
	}

	public void setUnread(int unread) {
		this.unread = unread;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
