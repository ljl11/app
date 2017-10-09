package com.cqut.report;

import android.content.Context;

import com.example.util.SystemUtil;



public class RaParam {
	public static String getServerAddr() {
			return "http://"+SystemUtil.getSystemParameter("ip")+":"+SystemUtil.getSystemParameter("port")+"/"+SystemUtil.getSystemParameter("project")+"/";
			
	}
}
