package com.example.newenergyvehicle.map;

import java.io.Serializable;
public class VehicleMapList implements Serializable{
	private String carNum_one;
	private String carNum_two;
	private String location;
	public VehicleMapList(){
	}

	public VehicleMapList(String carNum_one, String carNum_two, String location) {
		super();
		this.carNum_one = carNum_one;
		this.carNum_two = carNum_two;
		this.location = location;
	}
	public String getCarNum_one() {
		return carNum_one;
	}
	public void setCarNum_one(String carNum_one) {
		this.carNum_one = carNum_one;
	}
	public String getCarNum_two() {
		return carNum_two;
	}
	public void setCarNum_two(String carNum_two) {
		this.carNum_two = carNum_two;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}
