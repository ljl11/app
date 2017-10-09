package com.example.newenergyvehicle.vehicleInfo;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePage;
import com.example.newenergyvehicle.homePage.HomePageGroup;
import com.example.newenergyvehicle.homePage.NotificationGroup;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.MapActivity;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.notification.NotificationInfo;
import com.example.newenergyvehicle.notification.NotificationMain;
import com.example.newenergyvehicle.vehicle.VehicleMain;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class VehicleInfoActivity extends Fragment implements IXListViewListener {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private TextView headContent;
	private ImageView back;
	private XListView listView;
	private VehicleInfoAdapter mMsgAdapter;
	private Handler mHandler;
	private int page = 1;
	private int size = 10;
	private LinearLayout noData;
	private LinearLayout carInfoList;
	private Handler handler = new Handler();
	private Runnable runnable;
	private Point point ; //用来保存滑动的位置
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.verhicle_carlist_activity_main, null);
		context = inflater.getContext();
		
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
		       	((DrawerLayoutMenu)context).back();
			}
		});
		
		initView();
		addData(0);
		return view;
	}

	private void initView() {
		headContent = (TextView) view.findViewById(R.id.module_title);
	    headContent.setText("车辆列表");
	    noData = (LinearLayout) view.findViewById(R.id.no_data);
	    noData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onRefresh();
			}
		});
		carInfoList = (LinearLayout) view.findViewById(R.id.display_car_list);
		listView = (XListView) view.findViewById(R.id.car_info_list);
		listView.setPullLoadEnable(true);
		//point = new Point();
		//registerScrollListener();
		mMsgAdapter = new VehicleInfoAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setXListViewListener(this);
		mHandler = new Handler();
	}
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(NotificationAdapter.minToCaleander(System.currentTimeMillis()));
	}

	@Override
	public void onRefresh() {
		page = 1;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad();
				handler.removeCallbacks(runnable);// 停止定时器
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
			handler.removeCallbacks(runnable);// 停止定时器
			onLoad();
			addData(1);
			}
		}, 10);
	}
	
	public void addData(final int index){
		handler=new Handler();
		runnable=new Runnable() {
		   @Override
		   public void run() {
			   getVehicleInfo(index);
		       //handler.postDelayed(this, 15*1000);//boolean android.os.Handler.postDelayed(Runnable r, long delayMillis)
		   }
		};
		handler.postDelayed(runnable, 1);
		
	}
	
	//获取车辆信息
		public void getVehicleInfoByRole(final int index,Map param,String url){
					
					HttpUtil.getClient().get(HttpUtil.getURL(url+ParamUtil.mapToString(param)), new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							try{
								if(index == 0){
									JSONArray array = new JSONArray(response);
									if (array.length() > 0 && array != null) {
										mMsgAdapter.resetSingleData(new JSONArray(
												response));
										noData.setVisibility(View.GONE);
										carInfoList.setVisibility(View.VISIBLE);
									} else {
										carInfoList.setVisibility(View.GONE);
										noData.setVisibility(View.VISIBLE);
									}
									mMsgAdapter.notifyDataSetChanged();
									//listView.setSelectionFromTop(point.getListPos(),point.getOffSetY());  
								}else if(index == 1){
									JSONArray array = new JSONArray(response);
									if (array.length() > 0 && array != null) {
										mMsgAdapter.resetData(new JSONArray(
												response));
										noData.setVisibility(View.GONE);
										carInfoList.setVisibility(View.VISIBLE);
										mMsgAdapter.notifyDataSetChanged();
										//listView.setSelectionFromTop(point.getListPos(),point.getOffSetY());  
									} else{
										page--;
									}
								}
								
							}catch (Exception e) {
								carInfoList.setVisibility(View.GONE);
								noData.setVisibility(View.VISIBLE);
								if(index == 1)
									page = 1;
								Toast.makeText(context, "信息加载有误", 1).show();
							}
						}
						@Override
						public void onFailure(Throwable error) {
							carInfoList.setVisibility(View.GONE);
							noData.setVisibility(View.VISIBLE);
							if(index == 1)
								page = 1;
							super.onFailure(error);
						}
					});
		}
		
		
		
		/**
		 * 根据登陆角色获取车辆信息
		 */
		public void getVehicleInfo(int index){
			String roleType = Login.roleType;
			Map<String,Object> param = initParam();
			String url = "";
			 if (roleType.equals("DRIVER")){
				 url ="api/vehicle/vehiclesForApp";
				}
			 
			 else if (roleType.equals("BLOC")){
				 param.put("type", 1);
				 url = "api/vehicle/app/vehiclesForLeaseUser";
				
			 }
			 
			 else if(roleType.equals("OM")){
				 url = "api/vehicle/app/vehiclesForAfterSalesPersonnel";
				   if(Login.orgType != null){
						if(Login.orgType.equals("vehicleMng") || Login.orgType.equals("afterSale")){
							url = "api/vehicle/vehiclesForApp";
						}
				}
			 }
			 
			 getVehicleInfoByRole(index,param,url);
				
		}
		
		public Map<String,Object> initParam(){
			Map<String,Object> param =new HashMap<String,Object>();
			param.put("curPage", page);
			param.put("limit", size);
			
			return param;
		}
	
		
	@Override
	public void onHiddenChanged(boolean hidden) {
	    super.onHiddenChanged(hidden);
		if(hidden){
			handler.removeCallbacks(runnable);// 停止定时器
		} else {		
			addData(0);
		}
	}
	
	public void registerScrollListener(){
		//获取滚动到的位置  
        listView.setOnScrollListener(new OnScrollListener() {  
            @Override  
            public void onScrollStateChanged(AbsListView view, int scrollState) {  
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
                    ViewGroup item = (ViewGroup) listView.getChildAt(0);//此处必须为0  
                    point.setOffSetY(item.getTop());  
                }    
            }  
  
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem,  
                    int visibleItemCount, int totalItemCount) {  
            	point.setListPos(firstVisibleItem);  
            }  
        }); 
	}
	
}
