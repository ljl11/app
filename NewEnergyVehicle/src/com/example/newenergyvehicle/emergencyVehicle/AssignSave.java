package com.example.newenergyvehicle.emergencyVehicle;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.Notification;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.String.StringUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AssignSave extends Fragment{
	private View view;
	private Context context;
	private TextView module_title;
	private TextView car_arrangement_save;// 分配
	private ImageView back;
	private TextView plateNumber; //车牌号
	private TextView holderName; // 仓库名称
	private TextView assignLocation; //所在位置
	private TextView assignObject; //使用对象
	private TextView assignObjectName; //对象名称
	CarChooseInfo carChooseInfo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.assign_save, null);
		context = inflater.getContext();
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("分配");
		car_arrangement_save = (TextView)view.findViewById(R.id.car_arrangement_save);
		initView();
		
		car_arrangement_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				submit();
			}
		});
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		return view;
	}
	
	private void initView(){
	    plateNumber = (TextView) view.findViewById(R.id.assign_plate_number);
		holderName = (TextView) view.findViewById(R.id.holder_name);
		assignLocation = (TextView) view.findViewById(R.id.assign_location);
		assignObject = (TextView) view.findViewById(R.id.assign_object);
		assignObjectName = (TextView) view.findViewById(R.id.assign_object_name);
		
		if(getArguments() != null){
			carChooseInfo = (CarChooseInfo) getArguments().getSerializable("car");
			plateNumber.setText(carChooseInfo.getPlateNumber());
			holderName.setText(carChooseInfo.getHoldernName());
			assignLocation.setText(carChooseInfo.getAssignLocation());
			assignObjectName.setText(carChooseInfo.getAssignObjectName());
			if(carChooseInfo.getType().equals("0"))
			 assignObject.setText("使用人");
			else{
			 assignObject.setText("使用仓库");
			}
		}
	}
	
	private void submit(){
		AsyncTask task = new AsyncTask() {

			@Override
			protected Object doInBackground(Object... arg0) {
				RequestParams param = validate();
				if(param != null){
					HttpUtil.getClient().post(HttpUtil.getURL("api/vehicleUseAllocation/allocation"), param, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							if(response != null){
								try {
									if(new JSONObject(response).getString("status").equals("true")){
										((DrawerLayoutMenu) context).changeView(new SuccessPage());
									}
									else{
										Toast.makeText(context, "新增失败", 1).show();
									}
								} catch (JSONException e) {
									Common.dialogMark(getActivity(), null, "信息加载有误");
								}
							}
						}
						@Override
						public void onFailure(Throwable error) {
							super.onFailure(error);
						}
					});
				} else {
					
				}
				return null;
			}
		};
		task.execute();
		
	}
	
	private RequestParams validate(){
		RequestParams params = new RequestParams();
		
		if(valid(params, "vehicleId", carChooseInfo.getVehicleId())){
			return null;
		}
		
		if(valid(params, "userId", carChooseInfo.getAssignObject())){
			return null;
		}
		
		return params;
	}

	private boolean valid(RequestParams params, String key, String value){
		boolean flag = StringUtil.isNullOrEmpty(value);
		
		if(!flag){
			params.put(key, value);
		}
		params.put(key, value);
		return flag;
	}
	
}