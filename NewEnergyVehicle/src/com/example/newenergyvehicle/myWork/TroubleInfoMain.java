package com.example.newenergyvehicle.myWork;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.check.CheckMain;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.sendAndReceive.SendAndReceiveMain;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.SerachView;
import com.example.util.newDialog.CommonDialog;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TroubleInfoMain extends Fragment implements IXListViewListener,
		OnClickListener, SerachView, NeedRefresh {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private XListView listView;
	private TroubleInfoAdapter mMsgAdapter;
	private Handler mHandler;
	private Handler handler = new Handler();
	private LinearLayout news;
	private LinearLayout undeal;
	private final int size = 10;
	public int page = 1;
	private TextView newsNum;
	private TextView unreadNum;
	private int faultNews = 0;
	private int faultUntreated = 0;
	private String plateNumber;
	private LinearLayout noData;
	private LinearLayout troubleDetail;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		page = 1;
		plateNumber = null;
		this.inflater = inflater;
		view = inflater.inflate(R.layout.trouble_deal, container, false);
		initView();

		news.setOnClickListener(this);
		undeal.setOnClickListener(this);

		onRefresh();
		return view;
	}

	public void setNewsNum(int num) {
		newsNum.setText(num + "条");
	}

	public void setUnreadNum(int num) {
		unreadNum.setText(num + "条");
	}

	private void initView() {
		news = (LinearLayout) view.findViewById(R.id.news);
		undeal = (LinearLayout) view.findViewById(R.id.undeal);
		newsNum = (TextView) view.findViewById(R.id.news_num);
		unreadNum = (TextView) view.findViewById(R.id.unread_num);
		noData = (LinearLayout) view.findViewById(R.id.no_data);
		troubleDetail = (LinearLayout) view.findViewById(R.id.trouble_detail);
		context = inflater.getContext();

		listView = (XListView) view.findViewById(R.id.list_troubleInfo);
		listView.setPullLoadEnable(false);
		mMsgAdapter = new TroubleInfoAdapter(getActivity());
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

	public void addData(final int index) {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Map param = new HashMap();
				if(Login.operatorCode == null){
					param.put("type", 3);
				}
				else if (Login.operatorCode.equals("1")) {
					param.put("type", 2);
				}
				else if (Login.operatorCode.equals("2")){
					param.put("type", 4);
				} else {
					param.put("type", 3);
				}

				if (plateNumber != null) {
					param.put("numberPlate", plateNumber);
				}

//				param.put("curPage", page);
//				param.put("limit", size);

				// 未处理和未查看
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/faultRecord/recordListForCustomer"
								+ ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									if (index == 0) {
										JSONArray array = new JSONArray(
												response);
										if (array.length() > 0 && array != null) {
											mMsgAdapter
													.resetSingleData(new JSONArray(
															response));
											noData.setVisibility(View.GONE);
											troubleDetail
													.setVisibility(View.VISIBLE);
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
									troubleDetail.setVisibility(View.GONE);
									noData.setVisibility(View.VISIBLE);
									Common.dialogMark(getActivity(), null,
											"暂无数据");
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
		}, 1000);
	}

	private void getMaxCount() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();

				if(Login.operatorCode == null){
					param.put("type", 3);
				}
				else if (Login.operatorCode.equals("1")) {
					param.put("type", 2);
				}
				else if (Login.operatorCode.equals("2")){
					param.put("type", 4);
				} else {
					param.put("type", 3);
				}

				HttpUtil.getClient().get(
						HttpUtil.getURL("api/faultRecord/recordListForCustomer"
								+ ParamUtil.mapToString(param)),

						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								if (response != null)
									try {
										int untreated = 0;
										int news = 0;
										JSONArray array = new JSONArray(
												response);
										JSONObject json = null;
										for (int i = 0; i < array.length(); i++) {
											json = array.getJSONObject(i);
											if (json.getInt("isHandle") == 0) {
												untreated++;

											}
											if (json.getInt("isRead") == 0) {
												news++;
											}
										}
										faultUntreated = untreated;
										faultNews = news;
										setUnreadNum(untreated);
										setNewsNum(news);
//										Log.i("mywork", news + "");
										MyWork.getMyWork().setNotieNum(
												TroubleInfoMain.class, news);
									} catch (Exception e) {
										Common.dialogMark(getActivity(), null,
												"暂无数据");
									}
							}
						});
			}
		}, 1000);
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
		getMaxCount();
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
			changeBackground(news,undeal);
			if(mMsgAdapter.setStatue(0)){
				noData.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.undeal:
			changeBackground(undeal,news);
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

	public void searchRefresh(String plateNumber) {
		this.plateNumber = plateNumber;
		onRefresh();
	}

	@Override
	public void redresh() {
		onRefresh();
		mMsgAdapter.setFlag(false);
	}

	public void getHistory() {
		Map param = new HashMap();
		param.put("roleType", Login.roleType);

		HttpUtil.getClient().get(
				HttpUtil.getURL("api/faultRecord/faultHistorys"
						+ ParamUtil.mapToString(param)),

				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						if (response != null)
							try {

							} catch (Exception e) {
								Common.dialogMark(getActivity(), null, "暂无数据");
							}
					}

					@Override
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}
				});
	}

}
