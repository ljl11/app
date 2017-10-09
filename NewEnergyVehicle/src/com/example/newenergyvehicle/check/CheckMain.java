package com.example.newenergyvehicle.check;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.SerachView;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class CheckMain extends Fragment implements IXListViewListener,
		OnClickListener, SerachView, NeedRefresh {
	private View view;
	private Context context;
	private LayoutInflater inflater;

	private XListView listView;
	private LinearLayout news;
	private LinearLayout undeal;
	private CheckAdapter mMsgAdapter;
	private Handler mHandler = new Handler();
	private final int size = 10;
	public int page = 1;
	private TextView newsNum;
	private TextView unreadNum;
	private int checkNews = 0;
	private int checkUntreated = 0;
	private String plateNumber = null;
	private LinearLayout troubleDetail;
	private LinearLayout noData;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		page = 1;
		this.inflater = inflater;
		context = inflater.getContext();
		view = inflater.inflate(R.layout.trouble_deal, null);
		view.setTag("CheckMain");
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
		listView = (XListView) view.findViewById(R.id.list_troubleInfo);
		listView.setPullLoadEnable(false);
		mMsgAdapter = new CheckAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setXListViewListener(this);
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
				if (plateNumber != null) {
					param.put("plateNumber", plateNumber);
				}
					
//				param.put("curPage", page);
//				param.put("limit", size);
				// 未处理和未查看
				HttpUtil.getClient()
						.get(HttpUtil
								.getURL("api/emergencyCarManage/emergencyCarsExamineList"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										try {
											if (index == 0) {
												JSONArray array = new JSONArray(
														response);
												if (array.length() > 0
														&& array != null) {
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
												mMsgAdapter
														.resetData(new JSONArray(
																response));

											mMsgAdapter.notifyDataSetChanged();
										} catch (Exception e) {
											troubleDetail
													.setVisibility(View.GONE);
											noData.setVisibility(View.VISIBLE);
											if (index == 1)
												page = 1;
											Common.dialogMark(getActivity(),
													null, "信息加载有误");
										}
									}

									@Override
									public void onFailure(Throwable error) {
										troubleDetail.setVisibility(View.GONE);
										noData.setVisibility(View.VISIBLE);
										if (index == 1)
											page = 1;
										super.onFailure(error);
									}
								});
			}
		}, 500);

	}

	private void getMaxCount() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				HttpUtil.getClient().get(
						HttpUtil.url + "api/falutType/vehicleCheck",
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								if (response != null)
									try {
										JSONArray array = new JSONArray(
												response);
										JSONObject json = null;
										int news = -1;
										int untreated = 0;// 总条数
										for (int i = 0; i < array.length(); i++) {
											json = array.getJSONObject(i);
											untreated += json.getInt("count");
											setUnreadNum(untreated);
											if (json.getInt("state") == 0) {
												news = json.getInt("count");
												checkNews = news;
												setNewsNum(checkNews);
												MyWork.getMyWork().setNotieNum(
														CheckMain.class,
														checkNews);
											} else if (json.getInt("state") == 1) {
											}
										}

										if (news == -1) {
											checkNews = 0;
											setNewsNum(checkNews);
											MyWork.getMyWork().setNotieNum(
													CheckMain.class, checkNews);
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
