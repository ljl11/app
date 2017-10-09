package com.example.newenergyvehicle.homePage;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.MapActivity;
import com.example.newenergyvehicle.notification.NotificationMain;
import com.example.newenergyvehicle.personal.Personal;
import com.example.newenergyvehicle.vehicle.VehicleMain;
import com.example.util.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class HomePage extends TabActivity {
	public boolean hasnotigication = false;
	private TabHost tabHost;
	private ImageView notification_potion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		tabHost = this.getTabHost();
		TabSpec tabSpec1 = tabHost.newTabSpec("vehicleMain")
				.setIndicator("vehicleMain")
				.setContent(new Intent(this, HomePageGroup.class));
		TabSpec tabSpec2 = tabHost.newTabSpec("notificationMain")
				.setIndicator("notificationMain")
				.setContent(new Intent(this, NotificationGroup.class));
		TabSpec tabSpec3 = tabHost.newTabSpec("appoint")
				.setIndicator("appoint")
				.setContent(new Intent(this, ConsultationGroup.class));
		TabSpec tabSpec4 = tabHost.newTabSpec("personal").setIndicator("personal")
				.setContent(new Intent(this, Personal.class));
		notification_potion = (ImageView) findViewById(R.id.test_notification_potion);
		updataNotifiction();
		init(findViewById(R.id.guid));
		// 设置点击底部导航按钮对应进入的页面
		tabHost.addTab(tabSpec1);
		tabHost.addTab(tabSpec2);
		tabHost.addTab(tabSpec3);
		tabHost.addTab(tabSpec4);
		// 设置最开始显示界面为第0个界面
		tabHost.setCurrentTab(0);
		
		((ImageView)findViewById(R.id.im_home)).setImageDrawable(getResources().getDrawable(R.drawable.click_clgl));
		((TextView)findViewById(R.id.bar_index)).setTextColor(getResources().getColor(R.color.button));
	}

	private void init(View v) {
		clreanImage(v.findViewById(R.id.bar_index_btn));
	}

	private void updataNotifiction(){
		RequestParams params = new RequestParams();
		params.put("userid", Login.userid);
		HttpUtil.getClient().get(HttpUtil.url + "api/noticeAlert/noticeNum.action", params,new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				int count = 0;
				if(response != null)
					try {
						count = Integer.parseInt(response.getString("count"));
					} catch (Exception e) {
						Toast.makeText(HomePage.this, "网络异常", Toast.LENGTH_LONG).show();
					}
				if(count <= 0 ){
					notification_potion.setVisibility(View.GONE);
				}
			}
			@Override
			public void onFailure(String responseBody, Throwable error) {
				Toast.makeText(HomePage.this, "网络异常", Toast.LENGTH_LONG).show();
			}
		});
	}
	public void onClick(View v) {
		clreanImage(v);
		updataNotifiction();
		switch (v.getId()) {
		case R.id.bar_index_btn:
			tabHost.setCurrentTab(0);
			((ImageView)v.findViewById(R.id.im_home)).setImageDrawable(getResources().getDrawable(R.drawable.click_clgl));
			((TextView)v.findViewById(R.id.bar_index)).setTextColor(this.getResources().getColor(R.color.button));
			break;
		case R.id.bar_message_btn:
			tabHost.setCurrentTab(1);
			((ImageView)v.findViewById(R.id.im_phone)).setImageDrawable(getResources().getDrawable(R.drawable.click_bell));
			((TextView)v.findViewById(R.id.bar_message)).setTextColor(this.getResources().getColor(R.color.button));
			break;
		case R.id.bar_reservation_btn:
			tabHost.setCurrentTab(2);
			((ImageView)v.findViewById(R.id.im_set)).setImageDrawable(getResources().getDrawable(R.drawable.click_searcht));
			((TextView)v.findViewById(R.id.bar_reservation)).setTextColor(this.getResources().getColor(R.color.button));
			break;
		case R.id.bar_myself_btn:
			tabHost.setCurrentTab(3);
			((ImageView)v.findViewById(R.id.my_img)).setImageDrawable(getResources().getDrawable(R.drawable.click_user));
			((TextView)v.findViewById(R.id.bar_myself)).setTextColor(this.getResources().getColor(R.color.button));
			break;
		default:
			break;
		}
	}

	
	private void clreanImage(View v){
		View view = (View) v.getParent();
		((ImageView)view.findViewById(R.id.im_home)).setImageDrawable(getResources().getDrawable(R.drawable.clgl));
		((ImageView)view.findViewById(R.id.im_phone)).setImageDrawable(getResources().getDrawable(R.drawable.bell));
		((ImageView)view.findViewById(R.id.im_set)).setImageDrawable(getResources().getDrawable(R.drawable.searcht));
		((ImageView)view.findViewById(R.id.my_img)).setImageDrawable(getResources().getDrawable(R.drawable.user));
		((TextView)view.findViewById(R.id.bar_index)).setTextColor(this.getResources().getColor(R.color.test_color));
		((TextView)view.findViewById(R.id.bar_message)).setTextColor(this.getResources().getColor(R.color.test_color));
		((TextView)view.findViewById(R.id.bar_reservation)).setTextColor(this.getResources().getColor(R.color.test_color));
		((TextView)view.findViewById(R.id.bar_myself)).setTextColor(this.getResources().getColor(R.color.test_color));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
