package com.example.newenergyvehicle.vehicleInfo;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.MapActivity;
import com.example.newenergyvehicle.vehicle.TransferDetail;
import com.example.newenergyvehicle.vehicle.TransferInfo;
import com.example.newenergyvehicle.vehicle.TransferInfoAdapter;
import com.example.newenergyvehicle.vehicle.TransferRecord;
import com.example.util.Common;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VehicleInfo extends Fragment implements OnClickListener {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private ImageView back;
	private TextView carType;
	private TextView carNum;
	private TextView vin;
	private TextView maker;
	private TextView type;
	private TextView mileage;
	private TextView electricity;
	private LinearLayout faultHistory;
	private TextView engine;
	private TextView carTransferHistory;
	private LinearLayout vehiclePosition;

	private TextView currentHolder;
	private TextView lockState;
	private TextView vehicleState;
	private Vehicle_carlist vehicleInfo;
	private TextView headContent;
	private Handler handler = new Handler();
	private String vehicleId;
	private TransferInfo transferInfo;
	private String roleType;
	private TextView illegalNum;
	private TextView lookIllegalDetail;
	private TextView maintenanceReminder;
	private TextView lastMaintenanceTime;
	private TextView paymentReminder;
	private TextView paymentReminderContent;
	private LinearLayout handoverRecord;
	private LinearLayout maintenanceList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.vehicle_detail, container, false);
		context = inflater.getContext();

		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(this);

		initView();

		return view;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			((DrawerLayoutMenu) context).back();
			break;
		case R.id.lookIllegalDetail: {
			Fragment fa = new IllegalRecord();
			Bundle bundle = new Bundle();
			bundle.putString("vehicleId", vehicleId);
			fa.setArguments(bundle);
			((DrawerLayoutMenu) context).changeView(fa);
		}
			break;
		default:
			break;
		}
	}

	private void toCarTransfer() {
		Fragment fa = new TransferRecord();
		Bundle bundle = new Bundle();
		bundle.putString("vehicleId", vehicleId);
		fa.setArguments(bundle);
		((DrawerLayoutMenu) context).changeView(fa);
	}

	private void initView() {
		vehicleInfo = (Vehicle_carlist) getArguments().getSerializable(
				"vehicleInfo");
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("车辆信息详情");
		carType = (TextView) view.findViewById(R.id.carType);
		carNum = (TextView) view.findViewById(R.id.plateNum);
		engine = (TextView) view.findViewById(R.id.engine);
		maker = (TextView) view.findViewById(R.id.producers);
		currentHolder = (TextView) view.findViewById(R.id.current_holder);
		lockState = (TextView) view.findViewById(R.id.lock_state);
		vehicleState = (TextView) view.findViewById(R.id.vehicle_state);
		faultHistory = (LinearLayout) view
				.findViewById(R.id.vehicle_fault_history);
		vehiclePosition = (LinearLayout) view
				.findViewById(R.id.vehicle_position);
		illegalNum = (TextView) view.findViewById(R.id.illegalNum);
		lookIllegalDetail = (TextView) view
				.findViewById(R.id.lookIllegalDetail);
		maintenanceReminder = (TextView) view
				.findViewById(R.id.maintenanceReminder);
		lastMaintenanceTime = (TextView) view
				.findViewById(R.id.lastMaintenanceTime);
		paymentReminder = (TextView) view.findViewById(R.id.paymentReminder);
		paymentReminderContent = (TextView) view
				.findViewById(R.id.paymentReminderContent);
		handoverRecord = (LinearLayout) view
				.findViewById(R.id.handoverRecordList);
		maintenanceList = (LinearLayout) view
				.findViewById(R.id.maintenanceList);
		roleType = Login.roleType;
		/*
		 * if (roleType.equals("DRIVER"))
		 * carTransferHistory.setVisibility(View.GONE); else if
		 * (roleType.equals("OM")) faultHistory.setVisibility(View.GONE);
		 * carTransferHistory.setOnClickListener(this);
		 */
		lookIllegalDetail.setOnClickListener(this);
		faultHistory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("id", vehicleInfo.getId());
				VehicleFaultHistory faultHistory = new VehicleFaultHistory();
				faultHistory.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(faultHistory);
			}
		});

		maintenanceList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("id", vehicleInfo.getId());
				MaintenanceReminder faultHistory = new MaintenanceReminder();
				faultHistory.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(faultHistory);
			}
		});
		handoverRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("vehicleId", vehicleInfo.getId());
				Fragment fragment = new TransferRecord();
				fragment.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fragment);
			}
		});
		vehiclePosition.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("vehicleInfo", vehicleInfo);
				Fragment fragment = new MapActivity();
				fragment.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fragment);
			}
		}); 

		if (vehicleInfo != null) {
			getDetail(vehicleInfo.getId()); // 车辆信息
			getIllegalNum(vehicleInfo.getCarNum());
			getVaintenance(vehicleInfo.getId());
			getPayment(vehicleInfo.getId());
			vehicleId = vehicleInfo.getId();
		}
	}

	public void getDetail(final String id) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();
				param.put("vehicleId", id);
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/vehicle/app/vehicleInfo"
								+ ParamUtil.mapToString(param)),
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject response) {

								try {
									setData(response);
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
		});
	}

	public void getIllegalNum(final String plateNumber) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();
				param.put("vehicleId", vehicleId);
				HttpUtil.getClient()
						.get(HttpUtil
								.getURL("api/illegalRecord/vehicleIllegalRecords"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String content) {
										JSONArray json = null;
										try {
											json = new JSONArray(content);
											
											int length = json == null ? 0 : json.length();
											
												illegalNum.setText("共有" + length+ "次违章记录未处理");
												if (length == 0)
													lookIllegalDetail.setVisibility(View.GONE);
											
										} catch (JSONException e) {
											e.printStackTrace();
										}

										super.onSuccess(content);
									}

									@Override
									public void onFailure(Throwable error) {
										super.onFailure(error);
									}
								});
			}
		});
	}

	public void getVaintenance(final String Id) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();
				param.put("vehicleId", Id);
				HttpUtil.getClient()
						.get(HttpUtil
								.getURL("api/vehicleMaintenance/maintenancesItemsInfosByVehicleId"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String content) {
										JSONArray jsonArray = null;
										JSONObject jsonObject = null;
										String status = null;
										if (content == null
												|| content.equals("")) {
											maintenanceReminder
													.setText("保养提醒(暂无)");
											lastMaintenanceTime.setText("暂无");
										} else {
											try {
												jsonArray = new JSONArray(content);

												for (int length = 0; length < jsonArray.length(); length++) {

													if (jsonArray.getJSONObject(length).getInt("maintenanceState") == 0) {
														jsonObject = (JSONObject) jsonArray.get(length);
														break;
													}
												}
												if (jsonObject == null) {

													maintenanceReminder.setText("保养提醒(暂无)");
													lastMaintenanceTime.setText("暂无");
												} else
													setOneVaintenance(jsonObject);
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}

										super.onSuccess(content);
									}

									@Override
									public void onFailure(Throwable error) {
										super.onFailure(error);
									}
								});
			}
		});
	}

	public void getPayment(final String Id) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();
				param.put("vehicleId", Id);
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/paymentRecord/urgesPayment"
								+ ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String content) {
								JSONArray jsonArray = null;
								JSONObject jsonObject = null;
								try {
									jsonArray = new JSONArray(content);
									if(jsonArray != null && jsonArray.length() > 0){
									 jsonObject = (JSONObject) jsonArray.get(0);
									 setPaymentDetail(jsonObject);
									}
									

								} catch (JSONException e) {
									paymentReminder.setText("缴费提醒(暂无)");
								    paymentReminderContent.setText("暂无数据");
									e.printStackTrace();
								}

								super.onSuccess(content);
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		});
	}

	private void setPaymentDetail(JSONObject paymentDetail) {
		String paymentMoney = null;
		String lastPaymentTime = null;
		if (paymentDetail != null) {
			try {
				paymentMoney = paymentDetail.getString("unitPrice");
				paymentReminder.setText("缴费提醒(" + paymentMoney + ")");
			} catch (JSONException e) {
				paymentReminder.setText("缴费提醒(暂无)");
			}
			try {
				lastPaymentTime = paymentDetail.getString("lastPayTime");
				paymentReminderContent.setText("该车辆将在" + lastPaymentTime
						+ "到期，请及时缴费!");
			} catch (JSONException e) {
				paymentReminderContent.setText("暂无");
			}
		} else {
			paymentReminder.setText("缴费提醒(暂无)");
			paymentReminderContent.setText("暂无");
		}
	}

	private void setOneVaintenance(JSONObject vaintenance) {
		String vaintenanceType = null;
		String lastMaintainDate = null;
		if (vaintenance != null) {
			try {
				vaintenanceType = vaintenance.getString("itemName");
				maintenanceReminder.setText("保养提醒(" + vaintenanceType + ")");
			} catch (JSONException e) {
				maintenanceReminder.setText("保养提醒(暂无)");
			}
			try {
				lastMaintainDate = vaintenance.getString("lastMaintainDate");
				lastMaintenanceTime.setText(lastMaintainDate);
			} catch (JSONException e) {
				lastMaintenanceTime.setText("暂无");
			}
		}

	}

	private void setData(JSONObject response) {

		if (response != null) {

			// 获取 x y坐标
			try {
				vehicleInfo.setXcoordinate(response.getString("xcoordinate"));
			} catch (JSONException e1) {
				vehicleInfo.setXcoordinate("-1");
			}

			try {
				vehicleInfo.setYcoordinate(response.getString("ycoordinate"));
			} catch (JSONException e1) {
				vehicleInfo.setYcoordinate("-1");
			}

			try {
				carNum.setText(response.getString("plateNumber"));
			} catch (Exception e) {
				carNum.setText("暂无数据");
			}
			try {
				carType.setText(response.getString("typeName"));
			} catch (Exception e) {
				carType.setText("暂无数据");
			}
			try {
				engine.setText(response.getString("engineNo"));
			} catch (Exception e) {
				engine.setText("—— ——");
			}

			try {

				currentHolder
						.setText(response.getString("vehicleReceiverName"));
			} catch (Exception e) {
				try {
					currentHolder.setText(response.getString("warehouseName"));
				} catch (JSONException e1) {
					currentHolder.setText("暂无数据");
				}

			}
			try {
				maker.setText(response.getString("sourceUnit"));
			} catch (Exception e) {
				maker.setText("暂无数据");
			}

			try {
				if (response.getString("lockState").equals("1"))
					lockState.setText("已锁车");
				else
					lockState.setText("未锁车");
			} catch (Exception e) {
				maker.setText("暂无数据");
			}

			try {
				if (response.getString("faultState").equals("1"))
					vehicleState.setText("正在维修");
				else
					vehicleState.setText("未维修");
			} catch (Exception e) {
				vehicleState.setText("暂无数据");
			}
			try {

				vehicleInfo.setVin(response.getString("sim"));
			} catch (Exception e) {
				vehicleInfo.setVin("-1");
			}
		}

	}
}
