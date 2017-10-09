package com.example.newenergyvehicle.vehicle;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TransferRecord extends Fragment implements IXListViewListener {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private XListView listView;
	private ImageView back;
	private TextView headContent;
	private Handler mHandler;
	private final int size = 10;
	public int page = 1;
	private TransferInfoAdapter mMsgAdapter;
	private LinearLayout noData;
	private LinearLayout transferInfoList;
	private Handler handler = new Handler();
	private String vehicleId;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		page = 1;
		this.inflater = inflater;
		view = inflater.inflate(R.layout.vehicle_transfer_record, null);
		context = inflater.getContext();
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});

		initView();
		addData(0);

		return view;
	}

	private void initView() {
		if(getArguments() != null)
			vehicleId = getArguments().getString("vehicleId");
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("交接记录");
		noData = (LinearLayout) view.findViewById(R.id.no_data);
		transferInfoList = (LinearLayout) view
				.findViewById(R.id.display_car_list);
		listView = (XListView) view.findViewById(R.id.list_troubleInfo);
		listView.setPullLoadEnable(true);
		mMsgAdapter = new TransferInfoAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setXListViewListener(this);
		mHandler = new Handler();
		noData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				initView();
				addData(0);
			}
		});
	}

	// 获取转交记录
	public void addData(final int index) {
		handler.post(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams();
				if(vehicleId != null)
				    params.put("vehicleId", vehicleId);
				params.put("curPage", String.valueOf(page));
				params.put("limit", String.valueOf(size));
				HttpUtil.getClient().post(HttpUtil.getURL("api/handoverHistory"),params,
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray array = new JSONArray(response);
									if (index == 0) {

										if (array.length() > 0 && array != null) {
											mMsgAdapter.resetSingleData(new JSONArray(response));
				 							mMsgAdapter.resetSingleData(new JSONArray(
													response));
											noData.setVisibility(View.GONE);
											transferInfoList.setVisibility(View.VISIBLE);
										} else {
											transferInfoList.setVisibility(View.GONE);
											noData.setVisibility(View.VISIBLE);
										}
									} else if (index == 1) {
										if (array.length() > 0 && array != null) {
											mMsgAdapter.resetData(new JSONArray(response));
										} else {
											page--;
										}
									}

									mMsgAdapter.notifyDataSetChanged();
								} catch (Exception e) {
									if (index == 1)
										page = 1;
									Common.dialogMark(getActivity(), null, "信息加载有误");
								}
							}

							@Override
							public void onFailure(Throwable error) {
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
