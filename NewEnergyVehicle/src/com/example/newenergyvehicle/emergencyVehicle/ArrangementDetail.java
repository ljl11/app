package com.example.newenergyvehicle.emergencyVehicle;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ArrangementDetail extends Fragment implements NeedRefresh {
	private View view;
	private Context context;
	private TextView module_title;

	private LayoutInflater inflater;
	private XListView listView;
	private ArrangementDetailAdapter mMsgAdapter;
	private Handler mHandler;
	private ImageView back;
	private final int size = 10;
	public int page = 1;
	private int maxSize = -1;
	private TextView plateNumber;
	private TextView userName;
	private TextView taskTime;
	private TextView fullDate;
	private TextView serverName;
	private TextView taskPlace;
	private TextView recoveryVehicle;
	private String id;
	private String taskType;
	private String platenumber;
	private String customer;
	private String userId;
	private String vehicleId;
	private String faultRecodId;
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.arrangement_detail, null);
		context = inflater.getContext();
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("应急车使用详情");
		if (getArguments() != null) {
			id = getArguments().getString("id");
			taskType = getArguments().getString("taskType");
		}

		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});

		initView();
		getDetail();

		return view;
	}

	private void initView() {
		plateNumber = (TextView) view.findViewById(R.id.plate_number);
		userName = (TextView) view.findViewById(R.id.userName);
		taskTime = (TextView) view.findViewById(R.id.task_time);
		fullDate = (TextView) view.findViewById(R.id.full_date);
		serverName = (TextView) view.findViewById(R.id.server_name);
		taskPlace = (TextView) view.findViewById(R.id.task_place);
		recoveryVehicle = (TextView) view.findViewById(R.id.recovery_vehicle);

		if (taskType != null && taskType.equals("1"))
			recoveryVehicle.setVisibility(View.VISIBLE);
		else
			recoveryVehicle.setVisibility(View.INVISIBLE);

		recoveryVehicle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("customer", customer);
				bundle.putString("platenumber", platenumber);
				bundle.putString("userId", userId);
				bundle.putString("vehicleId", vehicleId);
				bundle.putString("faultRecodId", faultRecodId);
				bundle.putString("id",id);
				Recovery recovery = new Recovery();
				recovery.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(recovery);
			}
		});
	}

	public void getDetail() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();
				param.put("id", id);
				HttpUtil.getClient()
						.get(HttpUtil
								.getURL("api/emergencyCarManage/emergencyDetail"
										+ ParamUtil.mapToString(param)),
								new JsonHttpResponseHandler() {
									@Override
									public void onSuccess(JSONObject response) {

										try {
											setData(response);

										} catch (Exception e) {
											Common.dialogMark(getActivity(),
													null, "信息加载有误");
										}
									}

									@Override
									public void onFailure(String responseBody,
											Throwable error) {
										Common.dialogMark(getActivity(), null,
												"网络异常");
									}
								});

			}
		});

	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(NotificationAdapter.minToCaleander(System
				.currentTimeMillis()));
	}

	private void setData(JSONObject response) {

		if (response != null)

			try {
				platenumber = response.getString("plateNumber");
				plateNumber.setText(platenumber);
			} catch (Exception e) {
				plateNumber.setText("暂无数据");
			}
		try {
			taskTime.setText(response.getString("taskAddTime"));
		} catch (Exception e) {
			taskTime.setText("暂无数据");
		}
		try {
			fullDate.setText(response.getString("fullDate"));
		} catch (Exception e) {
			fullDate.setText("—— ——");
		}
		try {
			customer = response.getString("userName");
			userName.setText(customer);
		} catch (Exception e) {
			userName.setText("暂无数据");
		}
		try {
			serverName.setText(response.getString("serverName"));
		} catch (Exception e) {
			serverName.setText("暂无数据");
		}
		try {
			userId = response.getString("userId");
		} catch (Exception e) {
		}
		try {
			vehicleId = response.getString("vehicleId");
		} catch (Exception e) {
		}

		try {
			taskPlace.setText(response.getString("taskPlace"));
		} catch (Exception e) {
			taskPlace.setText("暂无数据");
		}
		try {
			faultRecodId = response.getString("fault_record");
		} catch (Exception e) {
			faultRecodId = null;
		}
	}

	@Override
	public void redresh() {
		initView();
		getDetail();
	}
}
