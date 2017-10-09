package com.example.util.menu;

import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;

public class MenuContainer {
	public Map<String, Fragment> menuContainer = new HashMap<String, Fragment>();
	private static final MenuContainer menu = new MenuContainer();
	
	private MenuContainer(){
		
	}
	
	public static MenuContainer getMenuContainer(){
		return menu;
	}
	
	public void setMenuContainer(String id){
		menuContainer.put(id, MenuList.getMenuList().getFragment(id));
	}
	
	public Fragment getFragment(String id){
		Fragment fragment = null;
		fragment = menuContainer.get(id);
		if(fragment != null)
			return fragment;
		
		setMenuContainer(id);
		
		return menuContainer.get(id);
	}
	
	public void clrean(){
		menuContainer = new HashMap<String, Fragment>();
	}
}
