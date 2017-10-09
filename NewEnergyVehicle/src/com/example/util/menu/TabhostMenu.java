package com.example.util.menu;

import java.io.Serializable;

public class TabhostMenu implements Serializable{
	private String id;
	private String name;
	private Class cla;
	private String headerName;
	
	public TabhostMenu(){
		
	}
	
	public TabhostMenu(String id, String name, Class cla, String headerName){
		this.id = id;
		this.name = name;
		this.cla = cla;
		this.headerName = headerName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getCla() {
		return cla;
	}
	public void setCla(Class cla) {
		this.cla = cla;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
}
