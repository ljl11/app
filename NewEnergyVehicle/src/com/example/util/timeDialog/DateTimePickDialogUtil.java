package com.example.util.timeDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.login.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimePickDialogUtil implements OnDateChangedListener {
	private static DateTimePickDialogUtil dateDialog;
	private static DatePicker datePicker;
	private AlertDialog ad;
	private String dateTime;
	private String initDateTime;
	private Activity activity;
	static Calendar d = Calendar.getInstance(Locale.CHINA);
	
	public DateTimePickDialogUtil(Activity activity, String initDateTime) {
		this.activity = activity;
		this.initDateTime = initDateTime;
	}

	public void init(DatePicker datePicker) {
			initDateTime = d.get(d.YEAR) + "年"
					+ d.get(d.MONTH) + "月"
					+ d.get(d.DAY_OF_MONTH) + "日";

		datePicker.init(d.get(d.YEAR),
				d.get(d.MONTH),
				d.get(d.DAY_OF_MONTH), this);
	}

	public AlertDialog dateTimePicKDialog(final TextView inputDate) {
		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater().inflate(R.layout.common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		init(datePicker);
		
		String timeStamp = inputDate.getText().toString();
		ad = new AlertDialog.Builder(activity, DatePickerDialog.THEME_HOLO_LIGHT)
				.setTitle(initDateTime)
				.setView(dateTimeLayout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText(dateTime);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();
		onDateChanged(null, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
		return ad;
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) { 
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		dateTime = sdf.format(calendar.getTime());
		ad.setTitle(dateTime);
	}


	public static String spliteString(String srcStr, String pattern,
			String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern);
		} else {
			loc = srcStr.lastIndexOf(pattern);
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc);
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length());
		}
		return result;
	}
	
	public static DateTimePickDialogUtil getInstance(Activity activity){
		if(dateDialog == null){
			dateDialog = new DateTimePickDialogUtil(activity, null);
		}
		return dateDialog;
	}
}
