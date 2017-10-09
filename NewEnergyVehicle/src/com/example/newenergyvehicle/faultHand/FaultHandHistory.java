package com.example.newenergyvehicle.faultHand;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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

public class FaultHandHistory extends Fragment implements IXListViewListener {
	private static FaultHandHistory faultHistory;
	private View view;
	private Context context;
	private TextView module_title;
	private ImageView back;
	private String catInfo;
	private XListView listView;
	private LinearLayout fault_history;
	private FaultHandHistoryListAdapter mMsgAdapter;
	private Handler mHandler;
	private final int size = 10;
	public int page = 1;
	private TextView repaired_num;
	private TextView total_repaired_num;
	private LinearLayout history_total;
	private static int historyTotal;
	private Handler handler = new Handler();
	private LinearLayout noData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fault_history, container, false);
		context = inflater.getContext();
		initView();
		getMaxCount();
		addData(0);
		return view;

	}

	public void setNewsNum(int num) {
		repaired_num.setText(num + "条");
	}

	public void setUnreadNum(int num) {
		total_repaired_num.setText(num + "条");
	}

	public static FaultHandHistory getFaultHistory() {
		return faultHistory;
	}

	private void initView() {
		total_repaired_num = (TextView) view
				.findViewById(R.id.total_repaired_num);
		fault_history = (LinearLayout) view.findViewById(R.id.fault_history_page);
		listView = (XListView) view.findViewById(R.id.fault_history_list);
		listView.setPullLoadEnable(true);
		mMsgAdapter = new FaultHandHistoryListAdapter(getActivity());
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
	private Map parameterLimit() {
		Map param = new HashMap();
		param.put("curPage", page);
		param.put("limit", size);
		param.put("roleType", Login.roleType);
		return param;
	}
	
	private Map parameterTotal() {
		Map param = new HashMap();
		param.put("roleType", Login.roleType);
		return param;
	}
	public void addData(final int index) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map param =parameterLimit();
				
				// 获取故障历史数据
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/faultRecord/faultHistorys"+
						ParamUtil.mapToString(param)), new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {

									JSONArray array = new JSONArray(response);
									
										if (index == 0) {

											if (array != null && array.length() > 0) {
												mMsgAdapter
														.resetSingleData(new JSONArray(
																response));
												noData.setVisibility(View.GONE);
												fault_history.setVisibility(View.VISIBLE);
											} else {
												fault_history.setVisibility(View.GONE);
												noData.setVisibility(View.VISIBLE);
											}

										} else if (index == 1){
											if(array != null && array.length() > 0)
											    mMsgAdapter.resetData(array);
											else
												page--;
										}

										mMsgAdapter.notifyDataSetChanged();

									}
									
								 catch (Exception e) {
									if (index != 1)
										page = 1;
									Toast.makeText(context, "信息加载有误", Toast.LENGTH_SHORT).show();
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
	
	public void getMaxCount() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map param =parameterTotal();
				
				// 获取故障历史数据
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/faultRecord/faultHistorys"+
						ParamUtil.mapToString(param)), new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {

									JSONArray array = new JSONArray(response);
									setUnreadNum(
											array ==null ? 0:array.length());
										
									}
									
								 catch (Exception e) {
									
									Toast.makeText(context, "信息加载有误", Toast.LENGTH_SHORT).show();
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

	public void setNotieNum(int num) {
		TextView numText = (TextView) view
				.findViewById(R.id.total_repaired_num);
		numText.setVisibility(View.VISIBLE);
		numText.setText(num);

	}

	// private void getMaxCount(){
	// Map param =new HashMap();
	// param.put("curPage", page);
	// param.put("limit", size);
	// param.put("roleType", Login.roleType);
	//
	// HttpUtil.getClient().get(HttpUtil.url +
	// "api/faultRecord/faultHistorys/count" + ParamUtil.mapToString(param), new
	// JsonHttpResponseHandler() {
	// @Override
	// public void onSuccess(JSONObject response) {
	// if(response != null)
	// try {
	// historyTotal = response.getInt("total");
	// setUnreadNum(historyTotal);
	// } catch (Exception e) {
	// Toast.makeText(context, "网络异常", 1).show();
	// }
	// }
	// @Override
	// public void onFailure(String responseBody, Throwable error) {
	// Toast.makeText(context, "网络异常", 1).show();
	// }
	// });
	// }

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
		}, 50);
	}

	@Override
	public void onLoadMore() {
		page++;
		mHandler.postDelayed(new Runnable() {
		@Override
		public void run() {
			onLoad();
			addData(1);
			}
		}, 50);

	}
}
