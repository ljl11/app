package com.example.util.dropDownOrUp;

import com.example.util.LRSlashAnim;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;
import android.widget.ListView;

public class CommonDropInterfaceForScroll {
	private ScrollView list;
	private ListView listView;
	private Droping dr;
	public final static int SCROLL_TOP=0;
	public final static int SCROLL_BOTTOM=1;
	private int status=SCROLL_TOP;
	private Moving mo;
	private Scrolling sc;
	private Activity activity;
	private ViewGroup vg;
	private float lastX = 0;
	private float lastY = 0;
	private boolean isNeedConnect;
	public CommonDropInterfaceForScroll(Activity activity,ViewGroup vg,ListView listView,ScrollView list,Droping dr,Moving mo,Scrolling sc,boolean isNeedConnect) {
		this.list = list;
		this.listView = listView;
		this.dr = dr;
		this.mo = mo;
		this.sc =sc;
		this.vg = vg;
		this.activity = activity;
		this.isNeedConnect = isNeedConnect;
		this.init();
	}
	
	
	public void init() {
		OnTouchListener ot = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(status== SCROLL_BOTTOM || status == SCROLL_TOP) {
					float x = arg1.getX();
					float y = arg1.getY();
					switch(arg1.getAction()) {
					case MotionEvent.ACTION_DOWN:
						lastX = x;
						lastY = y;
						if(mo!=null){
							mo.down(arg1);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if(mo!=null){
							mo.move(arg1);
						}
						break;
					case MotionEvent.ACTION_UP:
						if(mo!=null){
							mo.up(arg1);
						}
						if(dr!=null) {
							float deX = x-lastX;
							float NdeY = Math.abs(y-lastY);
							if(NdeY < deX && deX > 200) {
								LRSlashAnim.start(activity,
										vg, vg, false);
								if(isNeedConnect) {
									dr.right();
								}
								else {
									new Thread(new Runnable() {
										@Override
										public void run() {
											activity.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													dr.right();
												}
											});
										}
									}).start();
								}
								return true;
								
							}
							else if(NdeY < -deX && -deX > 200) {
								LRSlashAnim.start(activity,
										vg, vg, true);
								if(isNeedConnect) {
									dr.left();
								}
								else {
									new Thread(new Runnable() {
										@Override
										public void run() {
											activity.runOnUiThread(new Runnable() {
												
												@Override
												public void run() {
													dr.left();
												}
											});
										}
									}).start();
								}
								return true;
							}
						}
						break;
					}
				}
				
				return arg0.onTouchEvent(arg1);
			}
		};
		this.list.setOnTouchListener(ot);
		this.listView.setOnTouchListener(ot);
	}
}
