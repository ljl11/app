package com.example.util.String;

public class StringUtil {
	
	public static String firstCharToUp(String str){
		
		return Character.toUpperCase(str.charAt(0))+str.substring(1);
	}
	
	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.equals(""))
			return true;
		return false;
	}
	
}
