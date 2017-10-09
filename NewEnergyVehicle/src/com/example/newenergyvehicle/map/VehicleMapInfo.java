package com.example.newenergyvehicle.map;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePageGroup;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.notification.Notification;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.notification.NotificationInfo;
import com.example.newenergyvehicle.notification.NotificationMain;
import com.example.newenergyvehicle.search.SearchPlatenumber;
import com.example.newenergyvehicle.vehicleInfo.VehicleInfoActivity;
import com.example.newenergyvehicle.vehicleInfo.Vehicle_carlist;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleMapInfo extends Fragment implements IXListViewListener{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private XListView listView;
	private VehicleMapAdapter mMsgAdapter;
	private Handler mHandler;
	private final int size = 10;
	public int page = 1;
	private ImageView back;
	private ImageView searchCars;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.map_carinfo_list, container, false);
		context = inflater.getContext();
		
		back = (ImageView) view.findViewById(R.id.back);
		searchCars = (ImageView) view.findViewById(R.id.search_cars);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu)context).back();
			}
		});
		
		searchCars.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SearchPlatenumber sp = new SearchPlatenumber();
				Bundle bundle = new Bundle();
				bundle.putString("url", "api/driver/vehiclesList");
				
				sp.setArguments(bundle);
				((DrawerLayoutMenu)context).changeView(sp);
			}
		});
		initView();
		addData(0);
		
		
		return view;
	}

	private void initView() {
		listView = (XListView) view.findViewById(R.id.car_infor_list);
		listView.setPullLoadEnable(true);
		mMsgAdapter = new VehicleMapAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setXListViewListener(this);
		mHandler = new Handler();
	}
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚 :" + NotificationAdapter.minToCaleander(System.currentTimeMillis()));
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
	
	public void addData(final int index){
		Map param =new HashMap();
		param.put("curPage", page);
		param.put("limit", size);
		String url ="api/driver/vehicleList";
		if(Login.roleType.equals("BLOC")){
			url ="api/vehicle/vehicleAndModel";
			param.put("type", 1);
		}
		HttpUtil.getClient().get(HttpUtil.getURL(url +ParamUtil.mapToString(param)), new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				try{
					if(index == 0){
						mMsgAdapter.resetSingleData(new JSONArray(response));
						
					}else if(index == 1)
						mMsgAdapter.resetData(new JSONArray(response));
					
					mMsgAdapter.notifyDataSetChanged();
				}catch (Exception e) {
					if(index == 1)
						page = 1;
					Toast.makeText(context, "信息加载有误", 1).show();
				}
			}
			@Override
			public void onFailure(Throwable error) {
				if(index == 1)
					page = 1;
				super.onFailure(error);
			}
		});
	}
}
