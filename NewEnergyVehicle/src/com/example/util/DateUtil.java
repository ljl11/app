package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public final static String YEAR_MONTH_DAY_H_M_S ="yyyy年MM月dd日   HH:mm:ss";
	
	public final static String YEAR_MONTH_DAY ="yyyy年MM月dd日";
	
	public final static String HOUR_MINIUTE ="HH:mm";
	public static  String getCurrentTime(String pattern){
		 SimpleDateFormat   df   =   new   SimpleDateFormat();     
		 Date curDate =  new Date(System.currentTimeMillis());
		
		 return  df.format(curDate); 
	}
	
	public static String getCurrentTime(){
		long time=System.currentTimeMillis();
		final Calendar mCalendar=Calendar.getInstance();
		mCalendar.setTimeInMillis(time);
		int mHour=mCalendar.get(Calendar.HOUR);
	    int mMinuts=mCalendar.get(Calendar.MINUTE);
	    
	    return mHour+":"+mMinuts;
	}
}
