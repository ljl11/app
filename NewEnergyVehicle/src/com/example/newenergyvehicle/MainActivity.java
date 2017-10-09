package com.example.newenergyvehicle;

import java.util.Calendar;

import com.example.newenergyvehicle.failureReport.FailureReport;
import com.example.newenergyvehicle.faultHand.FaultHandHistory;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePage;
import com.example.newenergyvehicle.homePage.HorizontalMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.MapActivity;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.notification.NotificationMain;
import com.example.service.pushService.MyService;
import com.example.util.SystemUtil;
import com.example.util.monitorTimeout.ScreenObserver;
import com.example.util.monitorTimeout.TimeoutService;
import com.example.util.monitorTimeout.ScreenObserver.ScreenStateListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.app.Activity;
//import android.app.Activity;
import android.app.AlarmManager;
//import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	public static PendingIntent pi;
	private ScreenObserver mScreenObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_main);
		SystemUtil.init(this);
		Intent manageIntent = new Intent();
		manageIntent.setClass(MainActivity.this, Login.class);
		startActivity(manageIntent);
		
		if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
			   finish();
			   return;
			}
		try {
			mScreenObserver = new ScreenObserver(this);
			mScreenObserver.requestScreenStateUpdate(new ScreenStateListener() {
				@Override
				public void onScreenOn() {
					if (!ScreenObserver
							.isApplicationBroughtToBackground(MainActivity.this)) {
						cancelAlarmManager();
					}
				}

				@Override
				public void onScreenOff() {
					if (!ScreenObserver
							.isApplicationBroughtToBackground(MainActivity.this)) {
						cancelAlarmManager();
						setAlarmManager();
					}
				}
			});
		} catch (Exception e) {

		}

	}

	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		super.getResources();
		Configuration config = new Configuration();
		config.fontScale = 0.85f;
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}

	/**
	 * 设置定时器管理器
	 */
	private void setAlarmManager() {
		int numTimeout = 0;
		try {
			numTimeout = Integer.parseInt(SystemUtil
					.getSystemParameter("sleepTimeOut"));
		} catch (Exception e) {
			numTimeout = 300000;// 5分钟;
		}
		Intent alarmIntent = new Intent(MainActivity.this, TimeoutService.class);
		alarmIntent.putExtra("action", "timeout"); // 自定义参数
		pi = PendingIntent.getService(MainActivity.this, 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.MILLISECOND, numTimeout);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); // 设定的一次性闹钟，这里决定是否使用绝对时间
	}

	/**
	 * 取消定时管理器
	 */
	private void cancelAlarmManager() {
		if (pi != null) {
			AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			am.cancel(pi);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		cancelAlarmManager();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (ScreenObserver.isApplicationBroughtToBackground(this)) {
			cancelAlarmManager();
			setAlarmManager();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void openOptionsMenu() {
		super.openOptionsMenu();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
