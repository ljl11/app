package com.example.newenergyvehicle.vehicle;

import java.io.Serializable;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePageGroup;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.MapActivity;
import com.example.newenergyvehicle.map.VehicleMapInfo;
import com.example.newenergyvehicle.vehicleInfo.VehicleInfoActivity;
import com.example.util.Common;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class VehicleMain extends Fragment implements OnClickListener, Serializable{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private ImageView back;
	private LinearLayout vehicle_position;
	private LinearLayout vehicle_info;
	private LinearLayout vehicle_record;
	private static VehicleMain vehicleMain;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.vehicle, container, false);
		context = inflater.getContext();
		String roleType = Login.roleType;
		
		/*vehicle_position = (LinearLayout)view.findViewById(R.id.button2);*/
	/*	vehicle_position.setOnClickListener(this);*/
		vehicle_info = (LinearLayout) view.findViewById(R.id.button1);
		vehicle_info.setOnClickListener(this);
		vehicle_record = (LinearLayout) view.findViewById(R.id.button3);
		vehicle_record.setOnClickListener(this);
		/*if(roleType.equals("OM"))
			vehicle_position.setVisibility(View.GONE);
		if(roleType.equals("DRIVER"))
			vehicle_record.setVisibility(View.GONE);
		Common.clickView(vehicle_position);*/
		Common.clickView(vehicle_info);
		Common.clickView(vehicle_record);
		if(roleType.equals("DRIVER"))
			vehicle_record.setVisibility(View.INVISIBLE);
		
		return view;
	}

	@Override
	public void onClick(View arg0) {
		Fragment fragment = null;
		switch(arg0.getId()){
		/*case R.id.button2: 
			fragment = new VehicleMapInfo();
			break;*/
		case R.id.button1: fragment = new VehicleInfoActivity();
			break;
		case R.id.button3: fragment = new TransferRecord();
			break;
		default:
			break;
		}
		
		if(fragment == null)
			Common.dialogMark(getActivity(), null, "暂无数据");
		else
			((DrawerLayoutMenu)context).changeView(fragment);
	}
	
	public static VehicleMain getVehicleMain() {
		return vehicleMain;
	}
}
