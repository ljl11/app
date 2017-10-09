package com.example.util.monitorTimeout;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

public class ScreenObserver {
	 
    private static String TAG = "ScreenObserver";
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;
    private static Method mReflectScreenState;
 
    public ScreenObserver(Context context) {
        mContext = context;
        
        try {
            mReflectScreenState = PowerManager.class.getMethod("isScreenOn",
                    new Class[] {});
        } catch (NoSuchMethodException nsme) {
            Log.d(TAG, "API < 7," + nsme);
        }
    }
 
    /**
     * ����screen״̬����
     * 
     * @param listener
     */
    public void requestScreenStateUpdate(ScreenStateListener listener) {
        mScreenStateListener = listener;
        mScreenReceiver = new ScreenBroadcastReceiver(mScreenStateListener);
        startScreenBroadcastReceiver();
 
        firstGetScreenState();
    }
 
    /**
     * ��һ������screen״̬
     */
    private void firstGetScreenState() {
        PowerManager manager = (PowerManager) mContext
                .getSystemService(Activity.POWER_SERVICE);
        if (isScreenOn(manager)) {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOn();
            }
        } else {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOff();
            }
        }
    }
 
    /**
     * ֹͣscreen״̬����
     */
    public void stopScreenStateUpdate() {
        mContext.unregisterReceiver(mScreenReceiver);
    }
 
    /**
     * ����screen״̬�㲥������
     */
    private void startScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //���û�������Ļʱ
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(mScreenReceiver, filter);
    }
 
    /**
     * screen�Ƿ��״̬
     * 
     * @param pm
     * @return
     */
    private static boolean isScreenOn(PowerManager pm) {
        boolean screenState;
        try {
            screenState = (Boolean) mReflectScreenState.invoke(pm);
        } catch (Exception e) {
            screenState = false;
        }
        return screenState;
    }
 
    public interface ScreenStateListener {
        public void onScreenOn();
 
        public void onScreenOff();
    }
    /**
     * �ж���Ļ�Ƿ��ѱ���
     * @param c
     * @return
     */
    public final static boolean isScreenLocked(Context c) 
    {  
        android.app.KeyguardManager mKeyguardManager = (KeyguardManager) c  
                .getSystemService(Context.KEYGUARD_SERVICE);  
        return mKeyguardManager.inKeyguardRestrictedInputMode();  
    }  
     
    /**
     * �жϵ�ǰӦ���Ƿ��Ǳ�Ӧ��
     * @param context
     * @return
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}