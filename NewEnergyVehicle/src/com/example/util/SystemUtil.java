package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class SystemUtil {
	private static Properties pro = new Properties();
	private static InputStream is;
	private static Context context;

	public static void init(Context context1) {
		try {
			if(is==null) {
				context = context1;
				is = context.getAssets().open("systemParameters.properties");
				pro.load(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSystemParameter(String key) {
		return pro.getProperty(key.trim());
	}

	public static void setParam(String key, String value) {
		pro.setProperty(key, value);
	}
}
