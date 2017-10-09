package com.example.util.exceptionCatch;

import android.annotation.SuppressLint;
import android.content.Context;
import com.example.util.monitorTimeout.ActivityListUtil;

public class ExceptionCatch implements Thread.UncaughtExceptionHandler {
	private static ExceptionCatch ec;
	private Context mContext;

	private ExceptionCatch() {

	}

	public void init(Context ctx) {
		mContext = ctx;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public static ExceptionCatch getInstance() {
		if (ec == null) {
			ec = new ExceptionCatch();
		}
		return ec;
	}

	@SuppressLint("ShowToast")
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		ActivityListUtil.getInstence().cleanActivityList();
		System.exit(0);
	}

}
