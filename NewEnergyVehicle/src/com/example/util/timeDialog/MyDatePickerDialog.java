package com.example.util.timeDialog;

import android.app.DatePickerDialog;
import android.content.Context;

public class MyDatePickerDialog extends DatePickerDialog{

	public MyDatePickerDialog(Context context, int theme,
			OnDateSetListener listener, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, listener, year, monthOfYear, dayOfMonth);
	}
	
	@Override
	protected void onStop() {
	}
}
