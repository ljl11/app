package com.cqut.http;

import android.util.Log;

import com.cqut.json.JsonHelper;
import com.cqut.json.JsonHelperImpl;
import com.cqut.report.RaParam;
import com.cqut.service.AbsResponseHandler;
import com.cqut.tool.ObjectHelper;
import com.cqut.tool.Parameter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpHelperImp implements IHttpHelper, Parameter {
	private AsyncHttpClient client = null; 
	private static JsonHelper jsonHelper = JsonHelperImpl.getInstance();
	public HttpHelperImp(boolean hasTimeOut) {
		client = new AsyncHttpClient();
		if(hasTimeOut==false) {
			client.setTimeout(30000);
		}
	}
	
	@Override
	public <E> void post(String url, RequestParams rp,
			AsyncHttpResponseHandler asyncHandler)  throws Exception {
		try {
			client.post(url,rp, asyncHandler);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public <E> void post(String url, RequestParams rp,
			final AbsResponseHandler responseHandler) throws Exception {
		try {
			client.post(url,rp, new AsyncHttpResponseHandler(){
				@Override
				public void onFailure(Throwable error) {
					responseHandler.onFailure(error);
				}
				
			    @Override
			    public void onSuccess(String response) {
			    	Object o = HttpHelperImp.getObject(responseHandler, response);
			    	responseHandler.onSuccess(o);
			    }
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	@Override
	public <E> void get(String url, RequestParams rp,
			AsyncHttpResponseHandler asyncHandler)  throws Exception {
		try {
			client.get(url,rp, asyncHandler);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void excute(String url, String method,AsyncHttpResponseHandler asyncHandler,
			Object... params)  throws Exception{
		RequestParams rp = new RequestParams();
			url =  RaParam.getServerAddr()+url;
		for(int i = 0; i < params.length; i++){
			rp.put(String.valueOf(i), jsonHelper.getJson(params[i]));
		}
		
		if("POST".equals(method.toUpperCase())){
			Log.e("liu",url);
			this.post(url, rp, asyncHandler);
		}
		else if("GET".equals(method.toUpperCase())){
			this.get(url, rp, asyncHandler);
		}
		else{
			throw new Exception();
		}
	}


	@Override
	public <E> void get(String url, RequestParams rp,
			final AbsResponseHandler responseHandler) throws Exception {
		try {
			client.get(url,rp, new AsyncHttpResponseHandler(){
				
				@Override
				public void onFailure(Throwable error) {
					responseHandler.onFailure(error);
				}
				
			    @Override
			    public void onSuccess(String response) {
			    	Object o = HttpHelperImp.getObject(responseHandler, response);
			    	responseHandler.onSuccess(o);
			    }
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excute(String url, String method,
			final AbsResponseHandler responseHandler, RequestParams rp) throws Exception {
		/*RequestParams rp = new RequestParams();
		url =  RaParam.getServerAddr() + url + suffix;  //TODO
		Log.e("liu",url.toString());
		for(int i = 0; i < params.length; i++){
			
			Log.e("liu",jsonHelper.getJson(params[i]).toString());
			rp.put(String.valueOf(i), jsonHelper.getJson(params[i]));
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map = (Map<String,Object>)params[0];
		
		for(Map.Entry<String, Object> entity :map.entrySet()){
			if(entity.getValue().getClass() == String.class){
				rp.add(entity.getKey(), entity.getValue().toString());
			}
			else if(entity.getValue() instanceof List){
				rp.add(entity.getKey(), entity.getValue().toString());
			}else if(entity.getValue().getClass() == Integer.class){
				rp.add(entity.getKey(),entity.getValue().toString() );
			}else{
				rp.add("", entity.getValue().toString());
			}

		}*/
		
		url =  RaParam.getServerAddr()+url;
		Log.e("liu",rp.toString());
		if("POST".equals(method.toUpperCase())){
			this.post(url, rp, responseHandler);
		}
		else if("GET".equals(method.toUpperCase())){
			this.get(url, rp, responseHandler);
		} else if("PUT".equals(method.toUpperCase())){
			this.put(url, rp, responseHandler);
		} else{
			throw new Exception("����Զ�̷���,��������!");
		}
		
	}
	
	@Override
	public void excute(String url, String method,
			final AbsResponseHandler responseHandler) throws Exception {

			url = RaParam.getServerAddr()+url;
		RequestParams rp = new RequestParams();
		
		Log.e("liu",url);
		
		if("POST".equals(method.toUpperCase())){
			this.post(url, rp, responseHandler);
		}
		else if("GET".equals(method.toUpperCase())){
			this.get(url, rp, responseHandler);
		}
		else{
			throw new Exception("����Զ�̷���,��������!");
		}
		
	}
	
	public static Object getObject(AbsResponseHandler responseHandler, String result){
		if(ObjectHelper.isEmpty(result)){
			return null;
		}
		JsonType jsontype = responseHandler.getTypeClass();
		if(jsontype == JsonType.object){
			return jsonHelper.getBean(result, responseHandler.getResultClass());
		}
		else if(jsontype == JsonType.list){
			return jsonHelper.getBeanList(result, responseHandler.getResultClass());
		}
		else if(jsontype == JsonType.map){
			return null;   //TODO
		}
		else if(jsontype == JsonType.string){
			return result;
		}
		else{
			return null;
		}
	}

	@Override
	public void put(String url, RequestParams rp, final AbsResponseHandler responseHandler) throws Exception {
		try{
			client.put(url, rp, new AsyncHttpResponseHandler(){
				@Override
				public void onFailure(Throwable error) {
					responseHandler.onFailure(error);
				}
				
			    @Override
			    public void onSuccess(String response) {
			    	Object o = HttpHelperImp.getObject(responseHandler, response);
			    	responseHandler.onSuccess(o);
			    }
			});
		}catch (Exception e) {
			throw e;
		}
		
	}
}
