package com.example.util.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class AppUpdateUtil {
	
	static String  ip = "baidu.com";
	
	public final static  String ERROR ="error";
	
	public static Version ver = new Version();

	/**
     * 通过ping判断是否可用
     * @return
     */
    public static boolean ping() {
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + ip);
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content;
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            //有问题
            int status = p.waitFor();
            if (status == 0) {
                return true;
            }
        }
        catch (IOException e) {}
        catch (InterruptedException e) {}
        return false;
    }
    
    /**
     * 获取最新版本信息
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static void getServerVersion() throws IOException {
    	Thread thread = new Thread() {
			@Override
			public void run() { 
		try {
			 URL url = new URL("http://"+ip+":80/necop-app/version.txt");
			 HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		        httpURLConnection.setReadTimeout(3000);
		        httpURLConnection.setConnectTimeout(3000);
		        try {
					httpURLConnection.connect();
				} catch (IOException e) {
				}
		        try {
						 InputStream inputStream = httpURLConnection.getInputStream();
						 parseResponse(inputStream);
				} catch (IOException e) {
					String errpr =e.toString();
				}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
       
        };
    	};
    	thread.start();
    	
    	while(thread.isAlive());
    	
    }
    
    public static void  parseResponse(InputStream inputStream){
    	 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
         String string;
         String info ="" ;
         try {
			while((string=bufferedReader.readLine())!= null)
			 	info += string;
		} catch (IOException e1) {
			
		}
         //对json数据进行解析
 		try {
 			  JSONObject jsonObject = new JSONObject(info);
 			 
 			  try {
 					ver.setVersionCode(Integer.valueOf(jsonObject.getString("versionCode")));
 				} catch (JSONException e) {
 					ver.setVersionCode(-1);
 				}
 			  try {
 				   ver.setVersionName(jsonObject.getString("versionName"));
 				} catch (JSONException e) {
 					ver.setVersionName("ERROR");
 				}
 			  
 			  try {
 				   ver.setUrl(jsonObject.getString("update"));
 				} catch (JSONException e) {
 					ver.setUrl("ERROR");
 				}
 		}
 		catch(Exception e){
 			
 		}	
    }
    
  //判断网络连接是否可用  
	public static boolean isNetworkAvailable(Context context) {  
		    ConnectivityManager connectivityManager = (ConnectivityManager) context  
		            .getSystemService(Context.CONNECTIVITY_SERVICE);  
		    if (connectivityManager == null) {  
		    } else {  
		        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();  
		        if (networkInfo != null&&networkInfo.length>0) {  
		            for (int i = 0; i < networkInfo.length; i++) {  
		                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {  
		                    return true;  
		                }  
		            }  
		        }  
		    }  
		    return false;  
		}  
}
