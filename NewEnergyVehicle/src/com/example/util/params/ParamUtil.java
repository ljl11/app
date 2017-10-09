package com.example.util.params;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParamUtil {
	public static String mapToString(Map params){
		StringBuffer str = new StringBuffer();
		str.append('?');
		
		Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
		Map.Entry<String, Object> entry = null;
		while(iterator.hasNext()){
			entry = iterator.next();
			str.append(entry.getKey());
			str.append('=');
			str.append(entry.getValue());
			str.append('&');
		}
		int length = str.length();
		str.replace(length -1, length, "");
		return str.toString();
	}
}
