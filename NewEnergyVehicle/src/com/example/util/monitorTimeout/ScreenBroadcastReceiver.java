package com.example.util.monitorTimeout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.util.monitorTimeout.ScreenObserver.ScreenStateListener;

public class ScreenBroadcastReceiver extends BroadcastReceiver {
    private String action = null;
    private ScreenStateListener mScreenStateListener;
    public ScreenBroadcastReceiver(){
    	
    }
    public ScreenBroadcastReceiver(ScreenStateListener mScreenStateListener) {
    	this.mScreenStateListener = mScreenStateListener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            mScreenStateListener.onScreenOn();
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            mScreenStateListener.onScreenOff();
        }else if(Intent.ACTION_USER_PRESENT.equals(action)){
        }
    }
}