package com.example.newenergyvehicle.emergencyVehicle;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class ArrangementList extends Fragment implements IXListViewListener{
	private View view;
	private Context context;
	
	private LayoutInflater inflater;
	private XListView listView;
	private ArrangementListAdapter mMsgAdapter;
	private Handler mHandler;
	private ImageView back;
	private TextView repaired;
	private final int size = 10;
	public int page = 1;
	private int maxSize = -1;
	private TextView module_title;
	private LinearLayout noData;
	private LinearLayout emergencyArrangeHistoryPage;
	private Handler handler = new Handler();
	private String url;
	private String type;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.arrangement_list, null);
		context = inflater.getContext();

		initView(); 
		onRefresh();
		module_title = (TextView) view.findViewById(R.id.module_title);
		if(type.equals("0"))
		    module_title.setText("应急车安排历史");
		if(type.equals("1"))
			module_title.setText("应急车回收历史");
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		return view;
	}
	private void initView() {
		repaired = (TextView) view.findViewById(R.id.repaired);
		listView = (XListView) view.findViewById(R.id.car_arrangement_list);
		noData = (LinearLayout) view.findViewById(R.id.no_data);
		emergencyArrangeHistoryPage = (LinearLayout) view.findViewById(R.id.emergency_arrange_history_page);
		mMsgAdapter = new ArrangementListAdapter(getActivity());
		listView.setPullLoadEnable(true);
		listView.setAdapter(mMsgAdapter);
		listView.setXListViewListener(this);
		mHandler = new Handler();
		if(getArguments() != null){
			url = getArguments().getString("url");
			type = getArguments().getString("type");
			if(type.equals("1"))
				repaired.setText("应急车回收历史");
		}
	}
	public void setNotieNum(int num) {
		TextView numText = (TextView) view
				.findViewById(R.id.emergency_arrange_history_num);
		numText.setText(Integer.toString(num) + "条");

	}
	public void addData(final int index){
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Map param =new HashMap();
				param.put("curPage", page);
				param.put("limit", size);
				HttpUtil.getClient().get(HttpUtil.getURL(url +ParamUtil.mapToString(param)), new AsyncHttpResponseHandler() {
					 @Override
					 public void onSuccess(String response) {
						 try{
								if(index == 0){
									JSONArray array = new JSONArray(response);
									int arrayLength = (Integer) array.length();
									if (array.length() > 0 && array != null) {
										setNotieNum(arrayLength);
										mMsgAdapter.resetSingleData(new JSONArray(
												response),type);
										
										noData.setVisibility(View.GONE);
										listView.setVisibility(View.VISIBLE);
										emergencyArrangeHistoryPage.setVisibility(View.VISIBLE);
									} else {
										listView.setVisibility(View.GONE);
										noData.setVisibility(View.VISIBLE);
										emergencyArrangeHistoryPage.setVisibility(View.GONE);
									}
								}else if(index == 1)
									mMsgAdapter.resetData(new JSONArray(response));
								
								mMsgAdapter.notifyDataSetChanged();
							}catch (Exception e) {
								listView.setVisibility(View.GONE);
								noData.setVisibility(View.VISIBLE);
								if(index == 1)
									page = 1;
								Common.dialogMark(getActivity(), null, "信息加载有误");
							}
					 }
					@Override
					 public void onFailure(Throwable error) {
						listView.setVisibility(View.GONE);
						noData.setVisibility(View.VISIBLE);
						super.onFailure(error);
					}
					 });
			}
		}, 100);
				
	}
	
	private void getMaxCount(){
		handler.post(new Runnable() {
			@Override
			public void run() {
			HttpUtil.getClient().get(HttpUtil.url + "api/emergencyCarManage/emergencyCarsHistory/count", new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				if(response != null)
					try {
						maxSize = Integer.parseInt(response.getString("total"));
					} catch (Exception e) {
						Common.dialogMark(getActivity(), null, "网络异常");
					}
			}
			@Override
			public void onFailure(String responseBody, Throwable error) {
				Common.dialogMark(getActivity(), null, "网络异常");
			}
		});
		}
		});
	}
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(NotificationAdapter.minToCaleander(System.currentTimeMillis()));
	}
	
	@Override
	public void onRefresh() {
		page = 1;
		maxSize = 0;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad();
				addData(0);
				getMaxCount();
			}
		}, 	10);
	}

	@Override
	public void onLoadMore() {
		page++;
		mHandler.postDelayed(new Runnable() {
		@Override
		public void run() {
			if(maxSize > mMsgAdapter.getMax()){
				addData(1);
			}else
				Common.dialogMark(getActivity(), null, "暂无最新数据");
			onLoad();
			}
		}, 500);
	}
}
