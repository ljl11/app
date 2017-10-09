package com.cqut.service;

import java.util.List;
import java.util.Map;

import com.loopj.android.http.RequestParams;

public interface IDaoService {

	void testContent(RequestParams rp,  String url,
			AbsResponseHandler responseHandler);
	
	void getItems(RequestParams rp,String url,AbsResponseHandler responseHandler);
}
