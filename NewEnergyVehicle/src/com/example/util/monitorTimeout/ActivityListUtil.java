package com.example.util.monitorTimeout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.example.newenergyvehicle.login.Login;


import android.app.Activity;

public class ActivityListUtil {
    private static ActivityListUtil instence;
    public Map<Activity,Integer> activityList;
    public  Activity homePage;
    public Login loginActivity;
    
    public boolean isEmpty() {
    	if(activityList==null)
    		return true;
    	return activityList.isEmpty();
    }
    
    
    
    public Login getLoginActivity() {
		return loginActivity;
	}



	public void setLoginActivity(Login loginActivity) {
		this.loginActivity = loginActivity;
	}



	public  Activity getHomePage() {
		return homePage;
	}
	public  void setHomePage(Activity homePage1) {
		homePage = homePage1;
	}
	public ActivityListUtil() {
        activityList = new HashMap<Activity,Integer>();
    }
    public static ActivityListUtil getInstence() 
    {
        if (instence == null) {
            instence = new ActivityListUtil();
        }
        return instence;
    }
    public void addActivityToList(Activity activity) {
        if(activity!=null)
        {
            activityList.put(activity,1);
        }
    }
    public void removeActivityFromList(Activity activity)
    {
        if(activityList!=null && activityList.size()>0) {
            activityList.remove(activity);
        }
    }
    public void cleanActivityList() {
        if (activityList!=null && activityList.size() > 0) {
        	Iterator<Entry<Activity,Integer>> iterator = activityList.entrySet().iterator();
        	while(iterator.hasNext()) {
        		Activity activity = iterator.next().getKey();
                if(activity!=null && !activity.isFinishing()) {
                	activity.finish();
                }
        	}
        }
         
    }
}