package com.example.util.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageUtil  {
	
	public static int getLocalVersion(Context context){  
		  
        int versionCode = -1;  
  
        //通过上下文获取Packagemanager  
        PackageManager manager = context.getPackageManager();  
  
        try {  
            //通过manager获取package信息  
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);  
  
            //获取了app版本号  
            versionCode = packageInfo.versionCode;  
  
  
        } catch (PackageManager.NameNotFoundException e) {  
            e.printStackTrace();  
        }  
        return versionCode;  
  
    } 
    
    
}
