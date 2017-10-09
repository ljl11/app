package com.example.newenergyvehicle.homePage;

import java.io.Serializable;

public class NavigationItem implements Serializable{
	private String id;
	private int img;
	private String module;
	private int num;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getImg() {
		return img;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setImg(int img) {
		this.img = img;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	
 }
