package com.example.newenergyvehicle.sendAndReceive;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.salesPersonnel.ShuttleMissionInfo;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SendAndReceiveHistory extends Fragment{
	
	private View view;
	private Context context;
	private ShuttleMissionInfo shuttleMissionInfo;
	private TextView headContent;
	private TextView carNumber;
	private TextView taskType;
	private TextView carPlace;
	private TextView carTime;
	private TextView CompletionStatus;
	private ImageView back;
	private String id;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		view = inflater.inflate(R.layout.vehicle_send_pickup_history, null);
		context = inflater.getContext();

		initView();
		addData();
		return view;
	}
	private void addData() {
		Map param = new HashMap();
		param.put("carid", id);
		HttpUtil.getClient().get(
				HttpUtil.getURL("api/vehiclePickUp/taskHistory"
						+ ParamUtil.mapToString(param)),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						JSONObject js;
						try {
							js = new JSONArray(response).getJSONObject(0);
						} catch (JSONException e1) {
							js = null;
							e1.printStackTrace();
						}
						try {
							if(js.getInt("task_type")==0){
								taskType.setText("派车任务");
							}else{
								taskType.setText("接车任务");
							}
						} catch (JSONException e) {
							taskType.setText("--");
							e.printStackTrace();
						}
						try {
							if(js.getInt("type")==1){
								CompletionStatus.setText("已转交");
							}else if(js.getInt("type")==0){
								CompletionStatus.setText("已完成");
							}
						} catch (JSONException e) {
							CompletionStatus.setText("--");
							e.printStackTrace();
						}
						try {
							carTime.setText(js.getString("fullTime"));
						} catch (JSONException e) {
							carTime.setText("--");
							e.printStackTrace();
						}
						try {
							carPlace.setText(js.getString("taskPlace"));
						} catch (JSONException e) {
							carPlace.setText("--");
							e.printStackTrace();
						}
						try {
							carNumber.setText(js.getString("plate_number"));
						} catch (JSONException e) {
							carNumber.setText("--");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable error) {
						
					}
				});
	}
	private void initView() {
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("接送车历史详情");
		carTime = (TextView) view.findViewById(R.id.car_time_history);
		carPlace = (TextView) view.findViewById(R.id.car_place_history);
		carNumber = (TextView) view.findViewById(R.id.car_number_history);
		taskType = (TextView) view.findViewById(R.id.task_type_history);
		CompletionStatus = (TextView) view.findViewById(R.id.completion_status_history);
		if (getArguments() != null)
			id = getArguments().getString("id");
		CompletionStatus.setText("已完成");
		
	}
}
