package com.example.newenergyvehicle.sendAndReceive;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.check.CheckMain;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.myWork.TroubleInfoAdapter;
import com.example.newenergyvehicle.myWork.TroubleInfoMain;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.salesPersonnel.CarAssignment;
import com.example.newenergyvehicle.salesPersonnel.ShuttleMissionAdapter;
import com.example.newenergyvehicle.vehicleInfo.VehicleSearch;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.SerachView;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SendAndReceiveMain extends Fragment implements IXListViewListener,
		OnClickListener, SerachView, NeedRefresh {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private XListView listView;
	private LinearLayout receive;
	private LinearLayout send;
	private ShuttleMissionAdapter mMsgAdapter;
	private Handler mHandler;
	private final int size = 10;
	public int page = 1;
	private TextView newsNum;
	private TextView unreadNum;
	private int vehicleSend = 0;
	private int vehicleReceive = 0;
	private String plateNumber;
	private int sendVehicleNum;
	private LinearLayout troubleDetail;
	private LinearLayout noData;

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (!hidden) {
			if (mMsgAdapter != null) {
				mMsgAdapter.notifyDataSetChanged();
			}
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		page = 1;
		plateNumber = null;
		this.inflater = inflater;
		view = inflater.inflate(R.layout.trouble_deal, null);
		view.setTag("SendAndReceiveMain");
		context = inflater.getContext();
		initView();
		receive.setOnClickListener(this);
		send.setOnClickListener(this);

		onRefresh();

		return view;
	}

	private void initView() {
		receive = (LinearLayout) view.findViewById(R.id.news);
		send = (LinearLayout) view.findViewById(R.id.undeal);
		newsNum = (TextView) view.findViewById(R.id.news_num);
		unreadNum = (TextView) view.findViewById(R.id.unread_num);

		noData = (LinearLayout) view.findViewById(R.id.no_data);
		troubleDetail = (LinearLayout) view.findViewById(R.id.trouble_detail);
		listView = (XListView) view.findViewById(R.id.list_troubleInfo);
		listView.setPullLoadEnable(false);
		mMsgAdapter = new ShuttleMissionAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setXListViewListener(this);
		mHandler = new Handler();
		noData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				initView();
				onRefresh();
			}
		});
	}

	public void setNewsNum(int num) {
		newsNum.setText(num + "条");
		MyWork.getMyWork().setNotieNum(SendAndReceiveMain.class,num);
	}

	public void setUnreadNum(int num) {
		unreadNum.setText(num + "条");
	}

	public void addData(final int index) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();
				if (plateNumber != null) {
					param.put("plateNumber", plateNumber);
				}
//				param.put("curPage", page);
//				param.put("limit", size);
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/vehiclePickUp/tasks"
								+ ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									if (index == 0) {
										JSONArray array = new JSONArray(response);
										setNewsNum(getNewsdNum(array));
										sendVehicleNum = array.length();
										setUnreadNum(sendVehicleNum);
										if (array.length() > 0 && array != null) {
											mMsgAdapter.resetSingleData(array);
											noData.setVisibility(View.GONE);
											troubleDetail.setVisibility(View.VISIBLE);
										} else {
											troubleDetail
													.setVisibility(View.GONE);
											noData.setVisibility(View.VISIBLE);
										}
									} else if (index == 1)
										mMsgAdapter.resetData(new JSONArray(
												response));

									mMsgAdapter.notifyDataSetChanged();
								} catch (Exception e) {
									if (index == 1)
										page = 1;
									Common.dialogMark(getActivity(), null,
											"信息加载有误");
								}
							}

							@Override
							public void onFailure(Throwable error) {
								troubleDetail.setVisibility(View.GONE);
								noData.setVisibility(View.VISIBLE);
								super.onFailure(error);
							}
						});
			}
		}, 500);

	}
	
	private int getNewsdNum(JSONArray ja){
		int num = 0;
		int length = ja.length();
		for(int i = 0;i < length;i++){
			try {
				JSONObject jo = ja.getJSONObject(i);
				if(jo.getInt("isread")==0)
					num++;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return num;
	}

	private void getMaxCount() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				HttpUtil.getClient().get(
						HttpUtil.url + "api/falutType/vehicleSendAndReceive",
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								if (response != null)
									try {
										JSONArray array = new JSONArray(
												response);
										JSONObject json = null;
										int news = -1;
//										int untreated = 0;// 总条数
										for (int i = 0; i < array.length(); i++) {
											json = array.getJSONObject(i);
//											untreated += json.getInt("count");
//											if (untreated == sendVehicleNum)
//												setUnreadNum(untreated);
//											else {
//												setUnreadNum(sendVehicleNum);
//											}
											
											if (json.getInt("state") == 0) {
												news = json.getInt("count");
												vehicleSend = news;
												setNewsNum(vehicleSend);
												MyWork.getMyWork().setNotieNum(SendAndReceiveMain.class,vehicleSend);
											} else if (json.getInt("state") == 1) {
											}
											if (news == -1) {
												vehicleSend = 0;
												setNewsNum(vehicleSend);
												MyWork.getMyWork().setNotieNum(SendAndReceiveMain.class,vehicleSend);
											}
										}
									} catch (Exception e) {
										Common.dialogMark(getActivity(), null,
												"暂无数据");
									}
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		}, 500);

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
		onLoad();
		addData(0);
//		getMaxCount();
	}

	@Override
	public void onLoadMore() {
		page++;
		addData(1);
		onLoad();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.news:
			changeBackground(receive,send);
			if(mMsgAdapter.setStatue(0)){
		        noData.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.undeal:
			changeBackground(send,receive);
			if(mMsgAdapter.setStatue(-1)){
		        noData.setVisibility(View.VISIBLE);
			}else{
				noData.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}

	private void changeBackground(LinearLayout click, LinearLayout other) {
		click.setBackgroundResource(R.drawable.clarity);
		other.setBackgroundResource(R.drawable.background_green);
	}

	@Override
	public void searchRefresh(String plateNumber) {
		this.plateNumber = plateNumber;
		onRefresh();
	}

	@Override
	public void redresh() {
		onRefresh();
		mMsgAdapter.setFlag(false);
	}
}
