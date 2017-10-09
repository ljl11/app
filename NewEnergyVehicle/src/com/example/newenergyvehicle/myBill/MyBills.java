package com.example.newenergyvehicle.myBill;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyBills extends Fragment implements OnClickListener, IXListViewListener,NeedRefresh {
	private View view;
	private Context context;
	private XListView listView;
	private LinearLayout noData;
	private LinearLayout billList;
	private MyBillListAdapter billAdapter;
	private Handler handler = new Handler();
	
	@Override
	public void onClick(View arg0) {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.my_bill_main, container, false);
		context = inflater.getContext();

		initView();
		initData();
		initListener();
		return view;
	}

	private void initListener() {
		
	}

	private void initData() {
		handler.postDelayed(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/paymentRecord/payContracts"
								+ ParamUtil.mapToString(params)),
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable error) {
								Common.dialogMark(getActivity(), null, "网络异常");
								noData.setVisibility(View.VISIBLE);
								billList.setVisibility(View.GONE);
								super.onFailure(error);
							}

							@Override
							public void onSuccess(String content) {
								try {
									JSONArray array = new JSONArray(content);
									if(array.length()>0&&array!=null){
										billAdapter.resetSingleData(array);
										billAdapter.notifyDataSetChanged();
										noData.setVisibility(View.GONE);
										billList.setVisibility(View.VISIBLE);
									}else{
										noData.setVisibility(View.VISIBLE);
										billList.setVisibility(View.GONE);
									}
								} catch (JSONException e) {
									e.printStackTrace();
									noData.setVisibility(View.VISIBLE);
									billList.setVisibility(View.GONE);
								}
							}

						});
			}
		}, 100);

	}

	private void initView() {
		billList = (LinearLayout)view.findViewById(R.id.my_bill_list);
		noData = (LinearLayout)view.findViewById(R.id.no_data);
		listView = (XListView)view.findViewById(R.id.bill_list);
		listView.setPullLoadEnable(false);
		billAdapter = new MyBillListAdapter(getActivity());
		listView.setAdapter(billAdapter);
		listView.setXListViewListener(this);
		noData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				initView();
				onRefresh();
			}
		});
		((DrawerLayoutMenu) context).setMoreClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fragment paymentRecord = new PaymentRecord();
				changeView(paymentRecord);
			}
		});
	}

	@Override
	public void onRefresh() {
		initData();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		initData();
		onLoad();
	}
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(NotificationAdapter.minToCaleander(System
				.currentTimeMillis()));
	}
	
	private void changeView(Fragment fragment) {
		((DrawerLayoutMenu) context).changeView(fragment);
	}

	@Override
	public void redresh() {
		onRefresh();
	}

}
