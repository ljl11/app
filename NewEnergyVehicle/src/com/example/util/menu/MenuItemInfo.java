package com.example.util.menu;

public class MenuItemInfo {
	private String id;
	private int image;
	private String name;
	private boolean more;
	private String moreString;
	private Class cla;
	
	public MenuItemInfo(String id, int image, String name, Class cla, boolean more, String moreString) {
		this.id = id;
		this.image = image;
		this.name = name;
		this.cla = cla;
		this.more = more;
		this.moreString = moreString;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
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
	public boolean isMore() {
		return more;
	}
	public void setMore(boolean more) {
		this.more = more;
	}
	public String getMoreString() {
		return moreString;
	}
	public void setMoreString(String moreString) {
		this.moreString = moreString;
	}
}
