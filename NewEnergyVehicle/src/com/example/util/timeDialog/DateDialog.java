package com.example.util.timeDialog;

import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

public class DateDialog {
	static Calendar d = Calendar.getInstance(Locale.CHINA);
	private static DatePickerDialog dateDialog;
	
	
	public static DatePickerDialog getDateDialog(Context context){
			dateDialog = new MyDatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
					
				}
			}, d.get(d.YEAR), d.get(d.MONTH), d.get(d.DAY_OF_MONTH));
			dateDialog.setTitle("请选择时间");
			dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
				}
			});
		updataTime();
		return dateDialog;
	}
	
	public static String getTime(){
		DatePicker date = dateDialog.getDatePicker();
		StringBuilder time = new StringBuilder();
		time.append(date.getYear());
		time.append("-");
		time.append(date.getMonth() + 1);
		time.append("-");
		time.append(date.getDayOfMonth());
		
		return time.toString();
	}
	
	public static void updataTime(){
		dateDialog.updateDate(d.get(d.YEAR), d.get(d.MONTH), d.get(d.DAY_OF_MONTH));
	}
	
	public static void init(Context context){
		dateDialog = new MyDatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				
			}
		}, d.get(d.YEAR), d.get(d.MONTH), d.get(d.DAY_OF_MONTH));
		dateDialog.setTitle("请选择时间");
		dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
	}
}
