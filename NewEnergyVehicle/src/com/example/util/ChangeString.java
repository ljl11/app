package com.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeString {
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	public static String change(String string){
		if(string==null) {
			return "";
		}
		char[] value =  string.toCharArray();
		int length = value.length;
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < length;i++) {
			if(value[i]=='\'') {
				sb.append("''");
			}
			else if(value[i]=='&') {
				sb.append("chr(38)");
			}
			else {
				sb.append(value[i]);
			}
		}
		return replaceBlank(string);
	}
}
