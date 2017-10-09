package com.example.newenergyvehicle.InfomationService;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.check.CheckMain;
import com.example.newenergyvehicle.consultation.ConsultationActivity;
import com.example.newenergyvehicle.myWork.TroubleInfoMain;
import com.example.newenergyvehicle.notification.NotificationMain;
import com.example.newenergyvehicle.sendAndReceive.SendAndReceiveMain;
import com.example.newenergyvehicle.vehicle.VehicleMain;

public class InfomationServiceMenu extends Fragment{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private FragmentTabHost tabHost;
	private String mTextviewArray[] = {"车辆管理", "通知提醒", "服务查询"};
	private int defaultImage[] = {R.drawable.clgl, R.drawable.bell, R.drawable.searcht};
	private int clickImage[] = {R.drawable.click_clgl, R.drawable.click_bell, R.drawable.click_searcht};
	private Class fragmentArray[] = {VehicleMain.class, NotificationMain.class, ConsultationActivity.class};
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.infomation_service_menu, null);
		context = inflater.getContext();
		tabHost = (FragmentTabHost)view.findViewById(android.R.id.tabhost);
		tabHost.setup(context, getChildFragmentManager(), R.id.realtabcontent);
		
		final int count = fragmentArray.length;	
		
		for(int i = 0; i < count; i++){	
			TabSpec tabSpec = tabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			tabHost.addTab(tabSpec, fragmentArray[i], null);
		}
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String arg0) {
				int current = tabHost.getCurrentTab();
				Log.d(current + "", current + "");
				View view = null;
				for(int i = 0; i < count; i++){
					view = tabHost.getTabWidget().getChildTabViewAt(i);
					if(i == current){
						((ImageView)view.findViewById(R.id.im_home)).setImageDrawable(getResources().getDrawable(clickImage[i]));
						((TextView)view.findViewById(R.id.tab_name)).setTextColor(Color.parseColor("#73b1d8"));
					} else{
						((ImageView)view.findViewById(R.id.im_home)).setImageDrawable(getResources().getDrawable(defaultImage[i]));
						((TextView)view.findViewById(R.id.tab_name)).setTextColor(Color.parseColor("#dedede"));
					}
				}
			}
		});
		((ImageView)tabHost.getTabWidget().getChildTabViewAt(0).findViewById(R.id.im_home)).setImageDrawable(getResources().getDrawable(clickImage[0]));
		((TextView)tabHost.getTabWidget().getChildTabViewAt(0).findViewById(R.id.tab_name)).setTextColor(Color.parseColor("#73b1d8"));
		return view;
	}
	private View getTabItemView(int index){
		View view = inflater.inflate(R.layout.infomation_service_tabhost, null);
	
		ImageView imgView = (ImageView) view.findViewById(R.id.im_home);
		imgView.setImageDrawable(getResources().getDrawable(defaultImage[index]));;
		TextView textView = (TextView) view.findViewById(R.id.tab_name);
		textView.setText(mTextviewArray[index]);
		return view;
	}
}
