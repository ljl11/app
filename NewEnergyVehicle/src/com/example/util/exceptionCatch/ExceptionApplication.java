package com.example.util.exceptionCatch;


import com.baidu.mapapi.SDKInitializer;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class ExceptionApplication  extends Application{
	
	@Override  
    public void onCreate(){  
		super.onCreate();
		ExceptionCatch handler = ExceptionCatch.getInstance(); 
		handler.init(this);
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            	 MyActivityManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
            	
            }


            @Override
            public void onActivityDestroyed(Activity activity) {
            	/*if(activity instanceof DrawerLayoutMenu){
            		MyActivityManager.getInstance().popAllActivity();
            	} */
            }


			@Override
			public void onActivitySaveInstanceState(Activity activity,
					Bundle outState) {
		
			}
			
        });
    }
}
