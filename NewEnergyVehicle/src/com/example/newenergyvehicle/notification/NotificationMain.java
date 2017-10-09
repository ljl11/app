package com.example.newenergyvehicle.notification;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePage;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.vehicleInfo.VehicleInfoActivity;
import com.example.util.HttpUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NotificationMain extends Fragment implements IXListViewListener{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private ImageView headPortrait;
	private XListView listView;
	private NotificationAdapter mMsgAdapter;
	private Handler mHandler;
	private final int size = 10;
	public int page = 1;
	private LinearLayout noData;
	private LinearLayout displayList;
	int sizess =  0;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.activity_main_notification, container, false);
		context = inflater.getContext();
		
		initView();
		onRefresh();
		
		return view;
	}

	private void initView() {
		listView = (XListView) view.findViewById(R.id.list_notification);
		noData = (LinearLayout) view.findViewById(R.id.no_data);
		displayList = (LinearLayout) view.findViewById(R.id.display_notification_list);
		listView.setPullLoadEnable(true);
		mMsgAdapter = new NotificationAdapter(getActivity());
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
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(NotificationAdapter.minToCaleander(System.currentTimeMillis()));
	}

	@Override
	public void onRefresh() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onLoad();
				addData(1);
			}
		});
		
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
		@Override
		public void run() {
			addData(page);
			onLoad();
			}
		}, 10);
	}
	
	public void addData(final int index){
		RequestParams params = new RequestParams();
		params.put("curPage", index);
		params.put("limit", size);
		HttpUtil.getClient().get(HttpUtil.url + "api/noticeAlert/noticeAlertAll?curPage=" + index + "&limit=" + size, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				try{
					JSONArray array = new JSONArray(response);
					if(index == 1){
						if (array.length() > 0 && array != null) {
							mMsgAdapter.resetSingleData(array);
							noData.setVisibility(View.GONE);
							displayList.setVisibility(View.VISIBLE);
							page = 2;
						} else {
							displayList.setVisibility(View.GONE);
							noData.setVisibility(View.VISIBLE);
							page = 1;
						}
						
					}else {
						if(array.length() > 0 && array != null){
							page++;
							mMsgAdapter.resetData(array);
						}
					}
					
					Log.e("aaaaaaa", page + "");
					mMsgAdapter.notifyDataSetChanged();
				}catch (Exception e) {
					displayList.setVisibility(View.GONE);
					noData.setVisibility(View.VISIBLE);
					if(index == 1)
						page = 1;
					Toast.makeText(context, "信息加载有误", 1).show();
				}
			}
			@Override
			public void onFailure(Throwable error) {
				displayList.setVisibility(View.GONE);
				noData.setVisibility(View.VISIBLE);
				if(index == 1)
					page = 1;
				super.onFailure(error);
			}
		});
	}
	
//	private void getMaxCount(){
//		HttpUtil.getClient().get(HttpUtil.url + "api/noticeAlert/noticeAlertCountAll", new JsonHttpResponseHandler() {
//			@Override
//			public void onSuccess(JSONObject response) {
//				if(response != null)
//					try {
//						maxSize = Integer.parseInt(response.getString("totalItems"));
//					} catch (Exception e) {
//						Toast.makeText(context, "网络异常", Toast.LENGTH_LONG).show();
//					}
//			}
//			@Override
//			public void onFailure(String responseBody, Throwable error) {
//				Toast.makeText(context, "网络异常", Toast.LENGTH_LONG).show();
//			}
//		});
//	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if(!hidden){
			mMsgAdapter.notifyDataSetChanged();
			onRefresh();
		}
	}
}
