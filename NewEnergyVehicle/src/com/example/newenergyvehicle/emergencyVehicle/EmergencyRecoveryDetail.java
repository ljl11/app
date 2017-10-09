package com.example.newenergyvehicle.emergencyVehicle;

import java.io.Serializable;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class EmergencyRecoveryDetail extends Fragment implements OnClickListener,Serializable{
	
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private TextView headContent;
	private ImageView back;
	private TextView plateNumber;
	private TextView customerName;
	private TextView recoveryTime;
	private TextView recoveryPlace;
	private TextView releasePeople;
	private TextView recoveryState;
	private TextView backBt;
	private ArrangeItemInfo arrangeItemInfo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.emergency_recovery_detail, container, false);
		context = inflater.getContext();
		
		
		initView();
		
		return view;
	}
	private void initView() {
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("应急车回收详情");
		back = (ImageView) view.findViewById(R.id.back);
		backBt = (TextView) view.findViewById(R.id.back_bt);
		plateNumber = (TextView) view.findViewById(R.id.plate_num);
		customerName = (TextView) view.findViewById(R.id.customer_name);
		recoveryPlace = (TextView) view.findViewById(R.id.recovery_place);
		recoveryTime =(TextView) view.findViewById(R.id.recovery_time);
		releasePeople = (TextView) view.findViewById(R.id.release_people);
		recoveryState = (TextView) view.findViewById(R.id.recovery_state);
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(this);
		backBt.setOnClickListener(this);
		if(getArguments()!=null){
			arrangeItemInfo = (ArrangeItemInfo) getArguments().getSerializable("info");
			plateNumber.setText(arrangeItemInfo.getPlateNumber());
			customerName.setText(arrangeItemInfo.getName());
			recoveryPlace.setText(arrangeItemInfo.getTaskPlace());
			recoveryTime.setText(arrangeItemInfo.getTaskTime());
			releasePeople.setText(arrangeItemInfo.getReleasePeople());
			if(arrangeItemInfo.getState()==0)
				recoveryState.setText("回收中");
			else
				recoveryState.setText("已回收");
		}
		
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			((DrawerLayoutMenu)context).back();
			break;
		case R.id.back_bt:
			((DrawerLayoutMenu)context).back();
			break;
		default:
			break;
		}
	}
}
