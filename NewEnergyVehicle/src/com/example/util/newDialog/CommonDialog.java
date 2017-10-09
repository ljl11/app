package com.example.util.newDialog;

import com.example.util.Common;

import android.app.Activity;

public class CommonDialog {
	public static void exceptionDialog(Activity activity){
		Common.dialogMark(activity, null, "网络异常");
	}
}
