package com.example.service.pushService;


import java.lang.reflect.Method;

import com.example.newenergyvehicle.MainActivity;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.util.HttpUtil;
import com.example.util.exceptionCatch.MyActivityManager;
import com.example.util.menu.MenuList;
import com.igexin.sdk.PushManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;


public class NotificationBroadcastReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("ACTION_BUTTON")) {
        	int id = intent.getIntExtra("id", 0);
        	startApp(context,id);
        }
       else if(action.equals("android.net.conn.CONNECTIVITY_CHANGE")){
        	ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    		NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    		if(gprs.isConnected() ||  wifi.isConnected()){
    			PushManager.getInstance().bindAlias(context, Login.userid);
    	        bindClient(PushManager.getInstance().getClientid(context));
    	}
       }
       
    }
    
    public void startApp(Context context,int id){
    	NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE );
    	manager.cancel(id);//nofify中第一个参数使用的id
    	collapseStatusBar(context);
    	Intent[] intents = new Intent[1];
    	Intent intent = new Intent(context, MainActivity.class);
    	intents[0] = intent;
        intent.addCategory(Intent.CATEGORY_LAUNCHER);  
        intent.setAction(Intent.ACTION_MAIN);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if(Login.userid !=null){
          context.startActivities(intents);
          try {
			goToPage(DemoIntentService.map.get(id));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
          DemoIntentService.map.remove(id); //移除选中的通知栏
        }
    }
    
    public boolean checkApkExist(Context context, String packageName) {
    	if (packageName == null || "".equals(packageName))
    	return false;
    	try {
    	ApplicationInfo info = context.getPackageManager()
    	.getApplicationInfo(packageName,
    	PackageManager.GET_UNINSTALLED_PACKAGES);
    	return true;
    	} catch (NameNotFoundException e) {
    	return false;
    	}
    	
    }
    
    public static void collapseStatusBar(Context context) { 
    	try { Object statusBarManager = context.getSystemService("statusbar"); 
    	Method collapse; 
    	if (Build.VERSION.SDK_INT <= 16) { collapse = statusBarManager.getClass().getMethod("collapse"); } else { collapse = statusBarManager.getClass().getMethod("collapsePanels"); } 
    	collapse.invoke(statusBarManager); } catch (Exception localException) { localException.printStackTrace(); } }
    
    private void goToPage(Notice n) throws InstantiationException, IllegalAccessException{
		DrawerLayoutMenu c = (DrawerLayoutMenu) MyActivityManager.getInstance().getCurrentActivity();
		if (c.getMenuDrawerLayout().isDrawerOpen(Gravity.START)) {
			c.getMenuDrawerLayout().closeDrawers();
		}
		Fragment f = (Fragment) n.getCla().newInstance();
		if(n.getSize() == 1){
			Bundle b = new Bundle();
			b.putString("id", n.getTaskId());
			f.setArguments(b);
		}

		c.changeViewByNotification(f, MenuList.getMenuList().getIdByClass(n.getCla()));
		if( f instanceof MyWork){
			MyWork fr = (MyWork)f;
			fr.setTabTag(n.getType());
		}
    }
    
    public void bindClient(final String cid){
    	Handler handler  = new Handler();
    	handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				RequestParams param = new RequestParams();
				param.put("cid", cid);
				
					HttpUtil.getClient()
							.post(HttpUtil
									.getURL("api/bindForLogin"),
									param, new AsyncHttpResponseHandler() {
										@Override
										public void onSuccess(String response) {
											try {
												if (response != null) {
													
												}
											} catch (Exception e) {
												 Log.e("error", "onReceiveClientId -> " + "clientid = " + e.getMessage());
											}
										}

										@Override
										public void onFailure(Throwable error) {
											super.onFailure(error);
										}								});
				} 
		}, 0);
    }
  
}