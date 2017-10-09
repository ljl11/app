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
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.Notification;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class CarChoose extends Fragment implements IXListViewListener{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private XListView listView;
	private CarChooseAdapter mMsgAdapter;
	private Handler mHandler;
	private ImageView back;
	private final int size = 10;
	public int page = 1;
	private int maxSize = -1;
	private TextView module_title;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.car_choose, null);
		context = inflater.getContext();
		if(getArguments() != null){
			
		}
		initView();
		addData(0);
		getMaxCount();
		
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("选择车辆");
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
		listView = (XListView) view.findViewById(R.id.car_arrangement_list_item);
		listView.setPullLoadEnable(true);
		mMsgAdapter = new CarChooseAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		
		listView.setXListViewListener(this);
		mHandler = new Handler();
	}
	
	public void addData(final int index){
		AsyncTask task = new AsyncTask() {

			@Override
			protected Object doInBackground(Object... arg0) {
				HttpUtil.getClient().get(HttpUtil.getURL("api/contract/availableCars"), new AsyncHttpResponseHandler() {
					 @Override
					 public void onSuccess(String response) {
						 try{
							 if(index == 0){
									mMsgAdapter.resetSingleData(new JSONArray(response));
									
								}else if(index == 1)
									mMsgAdapter.resetData(new JSONArray(response));
								
								mMsgAdapter.notifyDataSetChanged();
							}catch (Exception e) {
									
								Common.dialogMark(getActivity(), null, "信息加载有误");
							}
					 }
					@Override
					 public void onFailure(Throwable error) {
						super.onFailure(error);
					}
					 });
				
				return null;
			}
		};
		task.execute();
	
	}
	
	private void getMaxCount(){
		HttpUtil.getClient().get(HttpUtil.url + "api/noticeAlert/noticeAlertCountAll", new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				if(response != null)
					try {
						maxSize = Integer.parseInt(response.getString("totalItems"));
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
		}, 500);
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
