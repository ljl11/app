package com.example.util.monitorTimeout;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TimeoutService extends Service{
	 
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
 
    boolean isrun = true;
 
    @Override
    public void onCreate() {
        super.onCreate();
    }
     
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        forceApplicationExit();
        return super.onStartCommand(intent, flags, startId);
         
    }
     
    private void forceApplicationExit()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
            	
                ActivityListUtil.getInstence().cleanActivityList();
                if(ActivityListUtil.getInstence().getHomePage()!=null) {
                	ActivityListUtil.getInstence().getHomePage().finish();
                }
                stopSelf();
                    }
            }).start();
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
        isrun = false;
    }
 
}
