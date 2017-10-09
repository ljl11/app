package com.example.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.example.newenergyvehicle.login.Login;
import com.example.util.String.StringUtil;
import com.loopj.android.http.AsyncHttpClient;


public class HttpUtil {
	private static String ip = SystemUtil.getSystemParameter("ip");
	private static String port = SystemUtil.getSystemParameter("port");
	public static String ipport = ip + ':' + port;
	public static String url = "http://" + ipport + '/' + SystemUtil.getSystemParameter("project") + '/';
	
	public static void  init(){
		ip = SystemUtil.getSystemParameter("ip");
		port = SystemUtil.getSystemParameter("port");
		ipport = ip + ':' + port;
		url = "http://" + ipport + '/' + SystemUtil.getSystemParameter("project") + '/';
	}
	public static boolean isStatus(JSONObject json){
		if(json == null)
			return false;
		try {
			String status = json.getString("status");
			if(status != null)
				return Boolean.valueOf(status);
		} catch (JSONException e) {
			return false;
		}
		
		return true;
	}
	
	public static AsyncHttpClient getClient(){
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Authorization", Login.token);
		client.addHeader("curRoleType", Login.roleType);
		client.addHeader("curUserid", Login.userid);
		client.addHeader("curOrgId", Login.orgId);
		client.setTimeout(30000);
		return client;
	}
	
	public static String getURL(String method){
		if(StringUtil.isNullOrEmpty(SystemUtil.getSystemParameter("ip"))){
			SystemUtil.init(Login.context);
			init();
		}
		StringBuffer url = new StringBuffer(HttpUtil.url);
		url.append(method);
		return url.toString();
	}
}
