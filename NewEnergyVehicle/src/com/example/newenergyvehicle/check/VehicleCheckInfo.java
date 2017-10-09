package com.example.newenergyvehicle.check;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.emergencyVehicle.Arrangement;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class VehicleCheckInfo extends Fragment implements OnClickListener {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private TextView module_title;
	private ImageView back;
	private TextView emergency_submit;// 同意
	private TextView emergency_disagree;// 不同意
	private TextView Emergency_back;// 返回
	private CheckInfo checkInfo;
	private int page;
	private Handler handler = new Handler();

	private TextView application;
	private TextView location;
	private TextView date;
	private TextView application_resaon;
	private String customerId;
	private String customerName;
	private boolean operator = false; // 该任务是否已经处理， 处理 true 未处理 false
	private CheckInfo detailInfo;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		view = inflater.inflate(R.layout.vehicle_check, container, false);
		context = inflater.getContext();

		initView();
		initData();
		getCustomer();
		
		emergency_submit.setOnClickListener(this);
		emergency_disagree.setOnClickListener(this);
		Emergency_back.setOnClickListener(this);
		back.setOnClickListener(this);
		return view;
	}

	private void initView() {
		if (getArguments() != null)
			checkInfo = (CheckInfo) getArguments().getSerializable("checkInfo");
		module_title = (TextView) view.findViewById(R.id.module_title);
		application = (TextView) view.findViewById(R.id.application);
		location = (TextView) view.findViewById(R.id.location);
		date = (TextView) view.findViewById(R.id.time);
		application_resaon = (TextView) view
				.findViewById(R.id.vehicle_application_reason);
		module_title.setText("应急车审核详情");

		emergency_submit = (TextView) view.findViewById(R.id.emergency_submit);
		emergency_disagree = (TextView) view.findViewById(R.id.disagree);
		Emergency_back = (TextView) view.findViewById(R.id.Emergency_back);
		back = (ImageView) view.findViewById(R.id.back);
	}

	private void agree() {
		detailInfo.setFaultRecordId(checkInfo.getFaultRecordId());
		Bundle bundle = new Bundle();
		bundle.putSerializable("detailInfo", detailInfo);
		bundle.putString("customerId", customerId);
		bundle.putString("customerName", customerName);
		bundle.putString("applyId", checkInfo.getuId());
		bundle.putString("applyTime", checkInfo.getApply_time());
		Arrangement arrangement = new Arrangement();
		arrangement.setArguments(bundle);
		((DrawerLayoutMenu) context).changeView(arrangement);
	}
	
	private void disagree() {
		handler.postDelayed(new Runnable() {
			public void run() {
				Map param = new HashMap();
				param.put("id", checkInfo.getId());
				param.put("state", 3);
				HttpUtil.getClient()
						.put(HttpUtil
								.getURL("api/emergencyCarManage/updateCheckState"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										back();
										sendNotice();
									}

									@Override
									public void onFailure(Throwable error) {
									}
								});
			}
		}, 100);

	}

	private void back() {
		((DrawerLayoutMenu) context).back();
	}

	public void initData() {
		handler.postDelayed(new Runnable() {
			public void run() {
				HttpUtil.getClient().get(
						HttpUtil.url + "api/emergencyCarManage/check/"
								+ checkInfo.getId(),
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject response) {
								try {
									getCheckDetailData(response);

								} catch (Exception e) {
									Common.dialogMark(getActivity(), null,
											"信息加载有误");
								}
							}

							@Override
							public void onFailure(String responseBody,
									Throwable error) {
								Common.dialogMark(getActivity(), null, "网络异常");
							}
						});
			}
		}, 100);

	}

	public void getCustomer() {
		handler.postDelayed(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", checkInfo.getFaultRecordId());
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/faultRecord/faultDetail"
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
									JSONObject jo = new JSONObject(content)
											.getJSONObject("fault");
									customerId = jo.getString("leaseUserUid");
									customerName = jo.getString("unitName");
								} catch (JSONException e) {
									customerId = null;
									customerName = null;
								}
								super.onSuccess(content);
							}

						});
			}
		}, 100);

	}

	public void getCheckDetailData(JSONObject joooo) {
		detailInfo = new CheckInfo();
		try {
			String data = joooo.getString("uName");
			application.setText(data);
			detailInfo.setApply_user(data);
		} catch (Exception e) {
			application.setText("无数据");
			detailInfo.setApply_user("");
		}
		try {
			String data = joooo.getString("applyAddress");
			location.setText(data);
			detailInfo.setApply_address(data);
		} catch (JSONException e) {
			location.setText("无数据");
			detailInfo.setApply_address("");
		}
		try {
			String data = joooo.getString("applyTime");
			date.setText(data);
			detailInfo.setApply_time(data);
		} catch (JSONException e) {
			date.setText("无数据");
			detailInfo.setApply_time("");
		}
		try {
			String data = joooo.getString("applyReason");
			application_resaon.setText(data);
			detailInfo.setApply_reason(data);
		} catch (JSONException e) {
			application_resaon.setText("无数据");
			detailInfo.setApply_reason("");
		}
		try {
			String data = joooo.getString("uId");
			detailInfo.setuId(data);
		} catch (JSONException e) {
			detailInfo.setuId("");
		}
		try {
			String data = joooo.getString("id");
			detailInfo.setId(data);
		} catch (JSONException e) {
			detailInfo.setId("");
		}
	}
	
	private void initState(final String faultRecordId) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();
				param.put("id", faultRecordId);
				HttpUtil.getClient()
						.get(HttpUtil
								.getURL("api/emergencyCarManage/emergencyApplyDetail"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										try {
											JSONObject json = new JSONObject(response);
											int state = json.getInt("state");
										if(state >= 2)
												operator = true;
											else
												operator = false;
										} catch (Exception e) {
											Toast.makeText(context, "信息加载有误", 1).show();
										}
									}

									@Override
									public void onFailure(Throwable error) {
										Toast.makeText(context, "mmp", 1).show();
 										super.onFailure(error);
									}
								});
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.emergency_submit:
			agree();
			break;
		case R.id.disagree:
			initState(checkInfo.getFaultRecordId());
			if(!operator)
				disagree();
			else {
				Toast.makeText(context, "该任务已经处理", Toast.LENGTH_SHORT).show();
				((DrawerLayoutMenu) context).changeView(new MyWork());
			}
			break;
		case R.id.Emergency_back:
			back();
			break;
		case R.id.back:
			back();
			break;
		default:
			break;
		}
	}
	
	private void sendNotice() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				String content = "您于"+checkInfo.getApply_time()+"申请的应急车已被拒绝！";
				String receiverIds = checkInfo.getuId();
				RequestParams params = new RequestParams();
				params.put("title", "应急车申请");
				params.put("content", content);
				params.put("noticeType", "0000000100000001");
				params.put("receiverIds", receiverIds);

				HttpUtil.getClient().post(
						HttpUtil.getURL("api/notice/noticeAdd"),
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}

							@Override
							public void onSuccess(String content) {
							}

						});
			}
		});
	}
}
