package com.cqut.http;

import java.util.Map;

import com.cqut.service.AbsResponseHandler;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;



public interface IHttpHelper {
	<E> void post(String url, RequestParams rp, AsyncHttpResponseHandler asyncHandler) throws Exception;
	
	<E> void get(String url, RequestParams rp, AsyncHttpResponseHandler asyncHandler) throws Exception;
	
	void excute(String url, String method, AsyncHttpResponseHandler asyncHandler, Object... params) throws Exception;
	
	<E> void post(String url, RequestParams rp, AbsResponseHandler responseHandler) throws Exception;
	
	<E> void get(String url, RequestParams rp, AbsResponseHandler responseHandler) throws Exception;
	
	void excute(String url, String method, AbsResponseHandler responseHandler, RequestParams rp) throws Exception;

	public void excute(String url, String method,final AbsResponseHandler responseHandler) throws Exception;

	void put(String url, RequestParams rp, AbsResponseHandler responseHandler) throws Exception;
}
