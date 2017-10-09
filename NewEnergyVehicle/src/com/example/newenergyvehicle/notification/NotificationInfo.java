package com.example.newenergyvehicle.notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.NotificationGroup;
import com.example.util.HttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class NotificationInfo extends Fragment{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private Handler handler = new Handler();
	private ImageView back;
	private TextView titel;
	private TextView time;
	private TextView type;
	private TextView content;
	private TextView moduleTitle;
	private String notificationId;
	Notification notification;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.notification_info, container, false);
		context = inflater.getContext();

		initView();
		addData();
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
		        ((DrawerLayoutMenu)context).back();
			}
		});
		return view;
	}
	
	private void initView(){
		titel = (TextView) view.findViewById(R.id.titledetail);
		time = (TextView) view.findViewById(R.id.timedetail);
		type = (TextView) view.findViewById(R.id.placedetail);
		content = (TextView) view.findViewById(R.id.contentdetail);
		content.setMovementMethod(ScrollingMovementMethod.getInstance());
		moduleTitle = (TextView) view.findViewById(R.id.module_title);
		moduleTitle.setText("通知提醒");
		if(getArguments() != null){
			notificationId = (String) getArguments().getString("id");
		}


	}
	private void addData() {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				HttpUtil.getClient().get(HttpUtil.url + "api/noticeAlert/noticeAlert/" + notificationId, new AsyncHttpResponseHandler() {
					@SuppressWarnings("unused")
					@Override
					public void onSuccess(String response) {
						notification = new Notification();
						try {
							JSONObject jsb = new JSONObject(response);
							if(jsb != null) {
								notification = initData(jsb);
								if(notification.getIs_read() == 0)
									notification.setIs_read(1);
								setView(notification);
							}
							else {
								Toast.makeText(context, "信息加载有误", 1).show();
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						
						try{
						}catch (Exception e) {
							Toast.makeText(context, "信息加载有误", 1).show();
						}
					}
					@SuppressWarnings("deprecation")
					@Override
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}
				});
			}
		});
	}
	
	private void setView(Notification notification) {
		titel.setText(notification.getTitle());
		time.setText(notification.getTime());
		type.setText(notification.getType());
		content.setText(notification.getContent());
	}
	
	private Notification initData(JSONObject joooo) {
		Notification notification = new Notification();
		try {
			notification.setId(joooo.getString("id"));
		} catch (Exception e) {
		}
		try {
			notification.setTitle(joooo.getString("title"));
		} catch (Exception e) {
			notification.setTitle("暂无数据");
		}

		try {
			notification.setIs_read(joooo.getInt("isread"));
		} catch (Exception e) {
			notification.setIs_read(null);
		}

		try {
			notification.setType(joooo.getString("paramText"));
		} catch (Exception e) {
			notification.setType("暂无数据");
		}
		try {
			notification.setTime(joooo.getString("time"));
		} catch (Exception e) {
			notification.setTime("暂无数据");
		}
		try {
			notification.setContent(joooo.getString("content"));
		} catch (Exception e) {
			notification.setContent("暂无数据");
		}

		try {
			notification.setIsDetele(joooo.getInt("isDelete"));
		} catch (Exception e) {
			notification.setIsDetele(null);
		}

		return notification;
	}
}
