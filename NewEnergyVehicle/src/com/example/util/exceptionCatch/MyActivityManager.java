package com.example.util.exceptionCatch;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;

public class MyActivityManager {
    private static MyActivityManager sInstance = new MyActivityManager();
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private Stack<Activity> list =new Stack<Activity>();


    private MyActivityManager() {

    }

    public static MyActivityManager getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }
    
    public void addActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
        list.push(activity);
    }
    
    //退出栈中所有Activity
    public void popAllActivity() {
    	while(!list.isEmpty()){
    		Activity a =list.pop();
    		a.finish();
    	}
    }
    
    
    public void popActivity(Activity a) {
    	if(list.peek() == a ){
    		list.peek();
    	}
    }
 
}