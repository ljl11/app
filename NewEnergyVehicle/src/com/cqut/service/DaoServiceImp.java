package com.cqut.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cqut.http.HttpHelperImp;
import com.cqut.http.IHttpHelper;
import com.loopj.android.http.RequestParams;

public class DaoServiceImp implements IDaoService {
	private IHttpHelper httpHelper = null;
	
	public static DaoServiceImp getInstance(boolean hasTimeOut) {
		return new DaoServiceImp(hasTimeOut);
	}
	private DaoServiceImp(boolean hasTimeOut){ 
		 httpHelper = new HttpHelperImp(hasTimeOut);
	}
	
	@Override
	public void testContent(RequestParams rp,  String url,
			AbsResponseHandler responseHandler) {
		try {
			httpHelper.excute(url, "POST", responseHandler,rp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void getItems(RequestParams rp, String url,
			AbsResponseHandler responseHandler) {
		try {
			httpHelper.excute(url, "GET", responseHandler,rp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getPut(RequestParams rp, String url,
			AbsResponseHandler responseHandler){
		try{
			httpHelper.excute(url, "PUT", responseHandler, rp);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
