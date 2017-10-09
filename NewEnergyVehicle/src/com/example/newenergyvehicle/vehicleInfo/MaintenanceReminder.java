package com.example.newenergyvehicle.vehicleInfo;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.array;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.check.CheckAdapter;
import com.example.newenergyvehicle.failureReport.FailureReport;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MaintenanceReminder extends Fragment implements IXListViewListener {
	private static MaintenanceReminder faultHistory;
	private View view;
	private Context context;
	private TextView module_title;
	private ImageView back;
	private TextView titleVehicleOperation;
	private String catInfo;
	private XListView listView;
	private LinearLayout illegalListPage;
	private MaintenanceReminderAdapter mMsgAdapter;
	private Handler mHandler;
	private final int size = 10;
	public int page = 1;
	private TextView illegalNum;
	private Handler handler = new Handler();
	private LinearLayout noData;
	private String vehicleId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.illegal_record, container, false);
		context = inflater.getContext();
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu)context).back();
			}
		});
		initView();
		addData(0);
		return view;

	}

	public void setUnreadNum(int num) {
		illegalNum.setText(num + "条");
	}

	public static MaintenanceReminder getFaultHistory() {
		return faultHistory;
	}

	private void initView() {

		if (getArguments() != null)
			vehicleId = getArguments().getString("id");
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("车辆保养提醒");
		illegalNum = (TextView) view.findViewById(R.id.illegalNum);
		titleVehicleOperation = (TextView) view.findViewById(R.id.titleVehicleOperation);
		titleVehicleOperation.setText("保养项");
		illegalListPage = (LinearLayout) view.findViewById(R.id.illegalListPage);
		listView = (XListView) view.findViewById(R.id.illegalRecordList);
		listView.setPullLoadEnable(true);
		mMsgAdapter = new MaintenanceReminderAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setXListViewListener(this);
		noData = (LinearLayout) view.findViewById(R.id.no_data);
		noData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				initView();
				onRefresh();
			}
		});
		mHandler = new Handler();

	}

	public void addData(final int index) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();
				param.put("curPage", page);
				param.put("limit", size);
				param.put("vehicleId", vehicleId);
				HttpUtil.getClient()
						.get(HttpUtil
								.getURL("api/vehicleMaintenance/maintenancesItemsInfosByVehicleId"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										if (response == null || response.equals("")) {
											illegalListPage.setVisibility(View.GONE);
											noData.setVisibility(View.VISIBLE);
										}
										
										else {
											try {

												JSONArray array = new JSONArray(response);
												setUnreadNum(array.length());

												if (index == 0) {

													if (array != null&& array.length() > 0) {
														mMsgAdapter.resetSingleData(new JSONArray(response));
														noData.setVisibility(View.GONE);
														illegalListPage.setVisibility(View.VISIBLE);
													} else {
														illegalListPage.setVisibility(View.GONE);
														noData.setVisibility(View.VISIBLE);
													}

													mMsgAdapter.resetSingleData(array);

												} else if (index == 1)
													mMsgAdapter.resetData(new JSONArray(response));

												mMsgAdapter.notifyDataSetChanged();

											} catch (Exception e) {
												illegalListPage.setVisibility(View.GONE);
												noData.setVisibility(View.VISIBLE);
												if (index == 1)
													page = 1;
												Common.dialogMark(getActivity(), "信息加载有误", 1);
											}
										}
										
									}

									@Override
									public void onFailure(Throwable error) {
										if (index == 1)
											page = 1;
										super.onFailure(error);
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

	@Override
	public void onRefresh() {
		page = 1;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad();
				addData(0);
			}
		}, 500);
	}

	@Override
	public void onLoadMore() {
		page++;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				addData(1);
				onLoad();
			}
		}, 500);

	}
}

