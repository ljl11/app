package com.example.newenergyvehicle.myBill;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaymentRecord extends Fragment implements OnClickListener, IXListViewListener {
	private View view;
	private Context context;
	private XListView listView;
	private LinearLayout noData;
	private LinearLayout billList;
	private PaymentRecordAdapter paymentAdapter;
	private Handler handler = new Handler();
	private ImageView back;
	private TextView headContent;
	private String contractId,currentPeriod;
	
	@Override
	public void onClick(View arg0) {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.payment_record, container, false);
		context = inflater.getContext();

		initView();
		initData();
		initListener();
		return view;
	}

	private void initListener() {
		noData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				initView();
				onRefresh();
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
	}

	private void initData() {
		handler.postDelayed(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				if(currentPeriod!=null){
					params.put("currentPeriod", currentPeriod);
				}
				if(contractId!=null){
					params.put("contractId", contractId);
				}
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/paymentRecord/paymentRecords"
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
										paymentAdapter.resetSingleData(array);
										paymentAdapter.notifyDataSetChanged();
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
		billList = (LinearLayout)view.findViewById(R.id.my_payment_record_list);
		noData = (LinearLayout)view.findViewById(R.id.no_data);
		back = (ImageView) view.findViewById(R.id.back);
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("缴费记录");
		listView = (XListView)view.findViewById(R.id.payment_record_list);
		listView.setPullLoadEnable(false);
		paymentAdapter = new PaymentRecordAdapter(getActivity());
		listView.setAdapter(paymentAdapter);
		listView.setXListViewListener(this);
		if(getArguments() != null){
			currentPeriod = getArguments().getString("currentPeriod");
			contractId = getArguments().getString("contractId");
		}
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

}
