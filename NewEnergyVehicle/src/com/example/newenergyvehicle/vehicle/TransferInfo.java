package com.example.newenergyvehicle.vehicle;

import java.io.Serializable;

public class TransferInfo implements Serializable {
	private String id;
	private String vehicleId;
	private String type;
	private String plateNumber;
	private String vehicleProviderType;
	private String vehicleProviderId;
	private String vehicleReceiverType;
	private String vehicleReceiverId;
	private String handOverTime;
	private String providerName;
	private String providerUnitName;
	private String receiverName;
	private String receiverUnitName;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVehicleProviderType() {
		return vehicleProviderType;
	}

	public void setVehicleProviderType(String vehicleProviderType) {
		this.vehicleProviderType = vehicleProviderType;
	}

	public String getVehicleProviderId() {
		return vehicleProviderId;
	}

	public void setVehicleProviderId(String vehicleProviderId) {
		this.vehicleProviderId = vehicleProviderId;
	}

	public String getVehicleReceiverType() {
		return vehicleReceiverType;
	}

	public void setVehicleReceiverType(String vehicleReceiverType) {
		this.vehicleReceiverType = vehicleReceiverType;
	}

	public String getVehicleReceiverId() {
		return vehicleReceiverId;
	}

	public void setVehicleReceiverId(String vehicleReceiverId) {
		this.vehicleReceiverId = vehicleReceiverId;
	}

	public String getHandOverTime() {
		return handOverTime;
	}

	public void setHandOverTime(String handOverTime) {
		this.handOverTime = handOverTime;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderUnitName() {
		return providerUnitName;
	}

	public void setProviderUnitName(String providerUnitName) {
		this.providerUnitName = providerUnitName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverUnitName() {
		return receiverUnitName;
	}

	public void setReceiverUnitName(String receiverUnitName) {
		this.receiverUnitName = receiverUnitName;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

}
