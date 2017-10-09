package com.example.newenergyvehicle.myBill;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.Double.DoubleUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.Utility;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class BillDetail extends Fragment implements OnClickListener, IXListViewListener,NeedRefresh {
	private View view;
	private Context context;
	private XListView listView;
	private BillDetailVehicleAdapter billDetailVehicleAdapter;
	private Handler handler = new Handler();
	private ImageView back;
	private TextView headContent;
	private TextView contractNo;
	private TextView periods;
	private TextView payable;
	private TextView lastPaymentDate;
	private TextView payFee;
	private TextView allPayFee;
	private TextView balance;
	private TextView contractVehicleNumber;
	private TextView toPayment;
	private LinearLayout payForThisMonth;
	private LinearLayout alwaysPaid;
	private String contractId;
	private String currentPeriod;
	private double paymentAmout;
	private MyBill myBill;
	
	@Override
	public void onClick(View arg0) {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.bill_detail, container, false);
		context = inflater.getContext();

		initView();
		initData();
		initListener();
		return view;
	}

	private void initListener() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		toPayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fragment payment = new Payment();
				Bundle bundle = new Bundle();
				bundle.putString("contractId", contractId);
				bundle.putString("payFee", payable.getText().toString());
				bundle.putInt("currentPeriod", Integer.parseInt(currentPeriod));
				payment.setArguments(bundle);
				changeView(payment);
			}
		});
		payForThisMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fragment payment = new PaymentRecord();
				Bundle bundle = new Bundle();
				bundle.putString("currentPeriod", currentPeriod);
				bundle.putString("contractId", contractId);
				payment.setArguments(bundle);
				changeView(payment);
			}
		});
		alwaysPaid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fragment payment = new PaymentRecord();
				Bundle bundle = new Bundle();
				bundle.putString("contractId", contractId);
				payment.setArguments(bundle);
				changeView(payment);
			}
		});
		listView.setOnTouchListener(new OnTouchListener() {

	        @Override
	        public boolean onTouch(View view, MotionEvent event) {
	            switch (event.getAction()) {
	            case MotionEvent.ACTION_MOVE:
	                return true;
	            default:
	                break;
	            }
	            return true;
	        }
	    });
	}

	private void initData() {
		handler.postDelayed(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("contractId", contractId);
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/paymentRecord/payContract"
								+ ParamUtil.mapToString(params)),
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable error) {
								Common.dialogMark(getActivity(), null, "网络异常");
								super.onFailure(error);
							}

							@Override
							public void onSuccess(String content) {
								try {
									JSONObject jo = new JSONObject(content);
									displayContractInfo(jo);
									displayBt();
									JSONArray ja = new JSONArray();
									ja = jo.getJSONArray("contractVehicleInfos");
									billDetailVehicleAdapter.resetSingleData(ja,jo.getString("unitPrice"));
									billDetailVehicleAdapter.notifyDataSetChanged();
									Utility.setListViewHeightBasedOnChildren(listView);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

						});
			}
		}, 100);

	}
	
	private void displayBt(){
		if(DoubleUtil.judgeDoubleIsZero(paymentAmout)){
			toPayment.setVisibility(View.GONE);
		}else{
			toPayment.setVisibility(View.VISIBLE);
		}
	}
	
	private void displayContractInfo(JSONObject jo){
		try {
			contractNo.setText(jo.getString("contractNo"));
		} catch (JSONException e) {
			contractNo.setText("--");
			e.printStackTrace();
		}
		try {
			String period = jo.getString("periodsNumber");
			currentPeriod = jo.getString("currentPeriod");
			String speriods = "第"+currentPeriod+"/"+period+"期";
			periods.setText(speriods);
		} catch (JSONException e) {
			periods.setText("--");
			e.printStackTrace();
		}
		try {
			BigDecimal big = new BigDecimal(jo.getString("needPayInThisCycle"));
			paymentAmout = big.doubleValue();
			paymentAmout = DoubleUtil.get2Double(paymentAmout);
			payable.setText(String.valueOf(paymentAmout));
		} catch (JSONException e) {
			payable.setText("--");
			e.printStackTrace();
		}
		try {
			lastPaymentDate.setText(jo.getString("normalPayDate"));
		} catch (JSONException e) {
			lastPaymentDate.setText("--");
			e.printStackTrace();
		}
		try {
			BigDecimal big = new BigDecimal(jo.getString("hasPayedAmountInThisCycle"));
			double money = DoubleUtil.get2Double(big.doubleValue());
			payFee.setText(String.valueOf(money));
		} catch (JSONException e) {
			payFee.setText("--");
			e.printStackTrace();
		}
		try {
			BigDecimal big = new BigDecimal(jo.getString("hasPayedAmount"));
			double money = DoubleUtil.get2Double(big.doubleValue());
			allPayFee.setText(String.valueOf(money));
		} catch (JSONException e) {
			allPayFee.setText("--");
			e.printStackTrace();
		}
		try {
			BigDecimal big = new BigDecimal(jo.getString("needPay"));
			double money = DoubleUtil.get2Double(big.doubleValue());
			balance.setText(String.valueOf(money));
		} catch (JSONException e) {
			balance.setText("--");
			e.printStackTrace();
		}
		try {
			contractVehicleNumber.setText(jo.getString("vehicleCount"));
		} catch (JSONException e) {
			contractVehicleNumber.setText("--");
			e.printStackTrace();
		}
	}

	private void initView() {
		back = (ImageView) view.findViewById(R.id.back);
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("账单信息");
		contractNo = (TextView)view.findViewById(R.id.contract_no);
		periods = (TextView)view.findViewById(R.id.periods);
		payable = (TextView)view.findViewById(R.id.payable);
		lastPaymentDate = (TextView)view.findViewById(R.id.last_payment_date);
		payFee = (TextView)view.findViewById(R.id.paid_fee);
		allPayFee = (TextView)view.findViewById(R.id.all_paid_fee);
		balance = (TextView)view.findViewById(R.id.balance);
		contractVehicleNumber = (TextView)view.findViewById(R.id.contract_vehicle_number);
		toPayment = (TextView)view.findViewById(R.id.to_payment);
		payForThisMonth = (LinearLayout)view.findViewById(R.id.pay_for_this_month);
		alwaysPaid = (LinearLayout)view.findViewById(R.id.always_paid);
		listView = (XListView)view.findViewById(R.id.vehicle_list);
		listView.setPullLoadEnable(false);
		listView.setFooterDividersEnabled(false);
		listView.setPullRefreshEnable(false);
		billDetailVehicleAdapter = new BillDetailVehicleAdapter(getActivity());
		listView.setAdapter(billDetailVehicleAdapter);
		listView.setXListViewListener(this);
		if (getArguments() != null) {
			myBill = (MyBill) getArguments().getSerializable("billDetail");
			contractId = myBill.getId();
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

	private void changeView(Fragment fragment) {
		((DrawerLayoutMenu) context).changeView(fragment);
	}

	@Override
	public void redresh() {
		initData();
	}
	
}
