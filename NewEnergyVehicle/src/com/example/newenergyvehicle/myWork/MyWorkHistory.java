package com.example.newenergyvehicle.myWork;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.String.StringUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MyWorkHistory extends Fragment implements IXListViewListener{
		private View view;
		private Context context;
		private TextView module_title;
		private ImageView back;
		private XListView listView;
		private MyWorkHistoryAdapter mMsgAdapter;
		private Handler mHandler;
		private final int size = 20;
		public int page = 1;
		private TextView total_repaired_num;
		private TextView repaired;
		private LinearLayout noData;
		private LinearLayout datil;
		private int historyTotal;
		private String title;
		private int type;
		private LinearLayout historyPicture;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.mywork_histroty, container, false);
			context = inflater.getContext();
			
			initData();
			initView();
			onRefresh();
			return view;

		}
		
		private void initData() {
			title = getArguments().getString("name");
			type = getArguments().getInt("type");
			if(title.equals("任务历史") && type == 1){
				type = 2;
			}
			else if(title.equals("审核历史") && type == 0){
				type = 1;
			}
		}
		
		public void setUnreadNum(int num){
			total_repaired_num.setText(num + "条");
		}
		
		private void initView() {
			total_repaired_num = (TextView) view.findViewById(R.id.total_repaired_num);
			module_title = (TextView) view.findViewById(R.id.module_title);
			back = (ImageView) view.findViewById(R.id.back);
			noData = (LinearLayout) view.findViewById(R.id.no_data);
			datil = (LinearLayout) view.findViewById(R.id.mywork_info);
			datil.setVisibility(View.GONE);
			noData.setVisibility(View.VISIBLE);
			repaired = (TextView) view.findViewById(R.id.repaired);
			historyPicture = (LinearLayout) view.findViewById(R.id.history_picture);
			listView = (XListView) view.findViewById(R.id.fault_history_list);
			listView.setPullLoadEnable(true);
			mMsgAdapter = new MyWorkHistoryAdapter(getActivity(), type);
			listView.setAdapter(mMsgAdapter);
			listView.setXListViewListener(this);
			mHandler = new Handler();
			
			back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					((DrawerLayoutMenu)context).back();
				}
			});
			
			noData.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					initView();
					onRefresh();
				}
			});
			module_title.setText(title);
			String title = "";
			switch (type) {
			case 0: title = "处理历史";
			break;
			case 1: title = "审核历史";
			historyPicture.setBackgroundResource(R.drawable.car_check);
			break;
			case 2: title = "接送历史";
			historyPicture.setBackgroundResource(R.drawable.send_pickup_arrangement);
			break;
			default:
				break;
			}
			repaired.setText(title);
		}
		
		public void addData(final int index){
			switch (type) {
			case 0: troubleInfoHistory(index);break;
			case 1: emergencyVehicleHistory(index);break;
			case 2: sendAndReceiveHistory(index);break;
			default:
				Common.dialogMark(getActivity(), null, "暂无数据");
			}
		}
		public void setNotieNum(int num){
				TextView numText = (TextView) view.findViewById(R.id.total_repaired_num);
				numText.setVisibility(View.VISIBLE);
				numText.setText(num);
			
		}
		private void getMaxCount(){
			switch (type) {
			case 0: getFaultHistorysNum();break;
			case 1: getEmergencyVehicleHistoryNum();break;
			case 2: getSendAndReceiveHistoryNum();break;
			default:
				setUnreadNum(0);
			}
		}
		
		private void getFaultHistorysNum(){
			Map param = getParams();
			if(!StringUtil.isNullOrEmpty(Login.operatorCode) && Login.operatorCode.equals("1"))
				param.put("roleType", "SALESSUPERVISOR");
			else if(Login.operatorCode == null){
				param.put("roleType", "OTHERS");
			}else if(Login.operatorCode.equals("2"))
				param.put("roleType", "OTHERS");
			else
				param.put("roleType", Login.roleType);
				
			HttpUtil.getClient().get(HttpUtil.url + "api/faultRecord/faultHistorys/count" + ParamUtil.mapToString(param), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					if(response != null)
						try {
							historyTotal = response.getInt("total");
							setUnreadNum(historyTotal);
						} catch (Exception e) {
							Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
						}
				}
				@Override
				public void onFailure(String responseBody, Throwable error) {
					Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		private void getEmergencyVehicleHistoryNum(){
			HttpUtil.getClient().get(HttpUtil.url + "api/emergencyCarManage/emergencyCheckHistorys/count", new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					if(response != null)
						try {
							historyTotal = response.getInt("total");
							setUnreadNum(historyTotal);
						} catch (Exception e) {
							Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
						}
				}
				@Override
				public void onFailure(String responseBody, Throwable error) {
					Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		private void getSendAndReceiveHistoryNum(){
			HttpUtil.getClient().get(HttpUtil.url + "api/vehiclePickUp/taskHistory/count", new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					if(response != null)
						try {
							historyTotal = response.getInt("total");
							setUnreadNum(historyTotal);
						} catch (Exception e) {
							Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
						}
				}
				@Override
				public void onFailure(String responseBody, Throwable error) {
					Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
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
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					onLoad();
					addData(0);
					getMaxCount();
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
		
		
		private void troubleInfoHistory(int index){
			Map param = getParams();
			if(!StringUtil.isNullOrEmpty(Login.operatorCode) && Login.operatorCode.equals("1"))
				param.put("roleType", "SALESSUPERVISOR");
			else if(Login.operatorCode == null){
				param.put("roleType", "OTHERS");
			}else if(Login.operatorCode.equals("2"))
				param.put("roleType", "OTHERS");
			else
				param.put("roleType", Login.roleType);
			sendMessage(index, param, "api/faultRecord/faultHistorys");
		}
		
		private void emergencyVehicleHistory(int index){
			sendMessage(index, getParams(), "api/emergencyCarManage/emergencyCheckHistorys");
		}
		
		private void sendAndReceiveHistory(int index){
			sendMessage(index, getParams(), "api/vehiclePickUp/taskHistory");
		}
		
		private void sendMessage(final int index, Map params, String url){
			HttpUtil.getClient().get(HttpUtil.getURL(url + ParamUtil.mapToString(params)) , new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					try{
						success(index, new JSONArray(response));
					} catch (Exception e) {
					}
				}
				@Override
				public void onFailure(Throwable error) {
					failure(index);
					super.onFailure(error);
				}
			});
		}
		private void success(int index, JSONArray array){
			try{
				if (index == 0) {
					if (array.length() > 0 && array != null) {
						mMsgAdapter.resetSingleData(array);
						noData.setVisibility(View.GONE);
						datil.setVisibility(View.VISIBLE);
					} else {
						datil.setVisibility(View.GONE);
						noData.setVisibility(View.VISIBLE);
					}
				} else if (index == 1) {
					mMsgAdapter.resetData(array);
				}
				mMsgAdapter.notifyDataSetChanged();
			}catch (Exception e) {
				if(index == 1)
					page = 1;
				datil.setVisibility(View.GONE);
				noData.setVisibility(View.VISIBLE);
				Common.dialogMark(getActivity(), null, "暂无数据");
			}
		}
		
		private void failure(int index){
			if(index == 1)
				page = 1;
			datil.setVisibility(View.GONE);
			noData.setVisibility(View.VISIBLE);
		}
		
		private Map getParams(){
			Map param =new HashMap();
			param.put("curPage", page);
			param.put("limit", size);
			
			return param;
		}
}
