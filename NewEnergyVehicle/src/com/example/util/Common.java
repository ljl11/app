package com.example.util;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import com.cqut.service.AbsResponseHandler;
import com.cqut.service.DaoServiceImp;
import com.cqut.service.IDaoService;
import com.cqut.tool.Parameter;
import com.cqut.transacteSuccess.interfaces.Enter;
import com.cqut.transacteSuccess.interfaces.MakeSuccess;
import com.cqut.transacteSuccess.interfaces.NoClickInterface;
import com.example.util.newDialog.CustomDialog;
import com.example.util.newDialog.NoneClickDialog;
import com.loopj.android.http.RequestParams;

public class Common {
	public static void dialogMark(final Activity activity,String title,Object message) {
//		long timeDelay = 1000;
//		if(message instanceof String) {
//			int length = ((String) message).length();
//			if(length > 10) {
//				timeDelay =  length*100;
//			}
//		}
//		final  CustomDialog.Builder builder = new CustomDialog.Builder(activity);  
//		builder.setContentView( message); 
//		builder.create(true).show(); 
//		Timer time = new Timer();
//		time.schedule(new TimerTask() {
//			
//			@Override
//			public void run() {
//				activity.runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						if(builder!=null) {
//							try {
//								builder.hide();
//							}
//							catch (Exception e) {
//							}
//						}
//						
//						
//					}
//				});
//				
//				
//			}
//		}, timeDelay);
		Toast.makeText(activity, (CharSequence) message, Toast.LENGTH_SHORT).show();
	}
	public static void submitButton(EditText view,final Enter enter) {
		view.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_DONE || arg1==EditorInfo.IME_ACTION_GO || arg1==EditorInfo.IME_ACTION_NEXT || arg1==EditorInfo.IME_ACTION_SEND) {
					enter.enter();
                }
				return false;
			}
		});
	}
	
	 public static void sendRequestWithTimeOut(final Activity context,Map<String,Object> param,final String action,final MakeSuccess makeObj,boolean hasTimeOut) {
		 try {
			 NoClickInterface noClick = null;
			 if(context!=null) {
				 noClick = setNoneClick(context);
			 }
			 final NoClickInterface noClickFinal = noClick;
				final IDaoService dao = DaoServiceImp.getInstance(hasTimeOut);
				final RequestParams rp = new RequestParams();
				if(param!=null) {
					Iterator<Entry<String,Object>> iterator = param .entrySet().iterator();
					while(iterator.hasNext()) {
						Entry<String,Object> entry = iterator.next();
						String key = entry.getKey();
						Object value =  entry.getValue();
						if(value instanceof String && value.equals("")) {
							rp.put(key, " ");
						}
						else {
							rp.put(key, value);
						}
						
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 public static void sendRequest(Activity context,Map<String,Object> param,String action,MakeSuccess makeObj) {
		 sendRequestWithTimeOut(context,param,action,makeObj,true); 
	 }
	 
	 
	 public static void updateRequest(Map<String,Object> param,String action,final MakeSuccess makeObj) {
		 try {
				final IDaoService dao = DaoServiceImp.getInstance(true);
				final RequestParams rp = new RequestParams();
				if(param!=null) {
					Iterator<Entry<String,Object>> iterator = param .entrySet().iterator();
					while(iterator.hasNext()) {
						Entry<String,Object> entry = iterator.next();
						String key = entry.getKey();
						Object value =  entry.getValue();
						rp.put(key, value);
					}
				}
				//rp.put("dateid", System.currentTimeMillis()+"");
				dao.testContent(rp, action, new AbsResponseHandler(String.class , Parameter.JsonType.string) {
					@Override
					public void onSuccess(Object o) {
						try {
							if(o != null) {
								if(o.toString().equals("error")) {
									if(makeObj!=null)
										makeObj.noData(null);
								}
								else {
									if(makeObj!=null) {
										makeObj.execute(o);
									}
								}
							}
							else {
								if(makeObj!=null)
									makeObj.noData(null);
							}
						}
						catch(Exception e) {
						}
					}

					@Override
					public void onFailure(Throwable error) {
						if(makeObj!=null)
							makeObj.error(null);
					}
				});
			}
			catch(Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 
	 public static CustomDialog.Builder dialog(Activity activity,String title,Object message,String sureButton,OnClickListener sureListener,
				String cancle,OnClickListener cancleListener,String center,OnClickListener centerListener) {
			 CustomDialog.Builder builder = new CustomDialog.Builder(activity);  
				 builder.setContentView(message); 
				 
	         builder.setTitle(title);  
			if(sureListener!=null) {
				  builder.setPositiveButton(sureButton, sureListener);
				  builder.setNegativeButton(cancle,  
						  cancleListener);  
			}
			else {
				if(sureButton!=null) {
					 builder.setPositiveButton(sureButton, sureListener);
				}
				else {
					builder.setNegativeButton(cancle,  
							  cancleListener); 
				}
			}
			if(center!=null) {
				builder.setCenterButton(center, centerListener);
			}
			 builder.create(false).show(); 
			 return builder;
		}
	 
	 
	 public static String formatServerDate (String date) {
		 try {
			 return date.replace("T", " ");
		 }
		 catch(Exception e) {
			 return " ";
		 }
		 
	 }
	 
	 public static String formatData(String longData) {
		 if(longData!=null) {
			 try {
				 long time = Long.parseLong(longData);
				 Calendar ca = Calendar.getInstance();
				 ca.setTimeInMillis(time);
				 StringBuffer timeStr = new StringBuffer();
				 timeStr.append(ca.get(Calendar.YEAR));
				 timeStr.append("-");
				 int month = ca.get(Calendar.MONTH) + 1;
				 if(month>9){
					 timeStr.append(month); 
				 }else{
					 timeStr.append("0" + month); 
				 }
				 timeStr.append("-");
				 int day = ca.get(Calendar.DAY_OF_MONTH);
				 if(day>9){
					 timeStr.append(day); 
				 }else{
					 timeStr.append("0" + day); 
				 }
				 timeStr.append(" ");
				 int hour = ca.get(Calendar.HOUR_OF_DAY);
				 if(hour>9){
					 timeStr.append(hour); 
				 }else{
					 timeStr.append("0" + hour); 
				 }
				 timeStr.append(":");
				 int minute = ca.get(Calendar.MINUTE);
				 if(minute>9){
					 timeStr.append(minute); 
				 }else{
					 timeStr.append("0" + minute); 
				 }
				 timeStr.append(":");
				 int second = ca.get(Calendar.SECOND);
				 if(second>9){
					 timeStr.append(second); 
				 }else{
					 timeStr.append("0" + second); 
				 }
				 return timeStr.toString();
			 }
			 catch(Exception e) {
				 return longData;
			 }
			
		 }
		 return "";
	 }
	 
	 public static String transateData(String longData) {
		 if(longData!=null) {
			 try {
				 long time = Long.parseLong(longData);
				 Calendar ca = Calendar.getInstance();
				 ca.setTimeInMillis(time);
				 StringBuffer timeStr = new StringBuffer();
				 timeStr.append(ca.get(Calendar.YEAR));
				 timeStr.append("-");
				 int month = ca.get(Calendar.MONTH) + 1;
				 if(month>9){
					 timeStr.append(month); 
				 }else{
					 timeStr.append("0" + month); 
				 }
				 timeStr.append("-");
				 int day = ca.get(Calendar.DAY_OF_MONTH);
				 if(day>9){
					 timeStr.append(day); 
				 }else{
					 timeStr.append("0" + day); 
				 }
				 timeStr.append(" ");
				 int hour = ca.get(Calendar.HOUR_OF_DAY);
				 if(hour>9){
					 timeStr.append(hour); 
				 }else{
					 timeStr.append("0" + hour); 
				 }
				 timeStr.append(":");
				 int minute = ca.get(Calendar.MINUTE);
				 if(minute>9){
					 timeStr.append(minute); 
				 }else{
					 timeStr.append("0" + minute); 
				 }
				 timeStr.append(":");
				 int second = ca.get(Calendar.SECOND);
				 if(second>9){
					 timeStr.append(second); 
				 }else{
					 timeStr.append("0" + second); 
				 }
				 return timeStr.toString();
			 }
			 catch(Exception e) {
				 return longData;
			 }
			
		 }
		 return "";
	 }
	 
	 public static void clickView(final View v) {
		 v.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch(arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					v.setAlpha(0.5f);
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					v.setAlpha(1.0f);
					break;
				case MotionEvent.ACTION_CANCEL:
					v.setAlpha(1.0f);
					break;
				}
				return arg0.onTouchEvent(arg1);
			}
		});
	 }
	 
	 public static NoClickInterface setNoneClick(Activity activity) {
		 final Dialog dialog = new NoneClickDialog(activity); 
		 dialog.setCancelable(false);
		 dialog.setCanceledOnTouchOutside(false);
		 dialog.show();
		 NoClickInterface no = new NoClickInterface() {
			
			@Override
			public void execute() {
				dialog.dismiss();
			}
		};
		 return no;
	 }
}
