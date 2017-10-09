package com.example.newenergyvehicle.salesPersonnel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.example.newenergyvehicle.sendAndReceive.Transform;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CarAssignment extends Fragment implements Serializable {
	private View view;
	private Context context;
	private String shuttleMissionInfoId;
	private ShuttleMissionInfo shuttleMissionInfo;
	private TextView headContent;
	private TextView carTime;
	private TextView sureButton;
	private TextView changButton;
	private TextView backButton;
	private TextView carNumber;
	private TextView taskType;
	private TextView carPlace;
	private TextView car_resource;
	private TextView carDeliveryrPhone;
	private TextView vehicleCollectorPhone;
	private LinearLayout callCarDeliveryrPhone;
	private LinearLayout callVehicleCollectorPhone;
	private LinearLayout pickUpInfo;
	private ImageView back;
	private TextView pickUpType;
	private LinearLayout sendType;
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.car_assignment_sales_personnel, null);
		context = inflater.getContext();

		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("接送车任务");
		initView();
		DisplayContent();

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});

		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showNormalDialog();
			}
		});

		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});

		changButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("params", shuttleMissionInfo);
				Transform transform = new Transform();
				transform.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(transform);
			}
		});
		
		callCarDeliveryrPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phoneNumber = carDeliveryrPhone.getText().toString();
				if (phoneNumber == null || phoneNumber.equals(""))
					Toast.makeText(getActivity(), "无该联系人的联系方式", Toast.LENGTH_SHORT).show();
				else
				call(phoneNumber);
			}
		});
		
		callVehicleCollectorPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phoneNumber = vehicleCollectorPhone.getText().toString();
				if (phoneNumber == null || phoneNumber.equals(""))
					Toast.makeText(getActivity(), "无该联系人的联系方式", Toast.LENGTH_SHORT).show();
				else
				call(phoneNumber);
			}
		});
		return view;

	}

	private void initView() {
		sendType = (LinearLayout) view.findViewById(R.id.send_type);
		sureButton = (TextView) view.findViewById(R.id.sure_lead_car);
		changButton = (TextView) view.findViewById(R.id.transform);
		backButton = (TextView) view.findViewById(R.id.backUp);
		pickUpType = (TextView) view.findViewById(R.id.pickUpType);
		callCarDeliveryrPhone = (LinearLayout) view.findViewById(R.id.callCarDeliveryrPhone);
		callVehicleCollectorPhone = (LinearLayout) view.findViewById(R.id.callVehicleCollectorPhone);
		pickUpInfo = (LinearLayout) view.findViewById(R.id.pickUpInfo);
		
	}

	public void DisplayContent() {
		carTime = (TextView) view.findViewById(R.id.car_time);
		carPlace = (TextView) view.findViewById(R.id.car_place);
		carNumber = (TextView) view.findViewById(R.id.car_number);
		taskType = (TextView) view.findViewById(R.id.task_type);
		car_resource = (TextView) view.findViewById(R.id.car_resource);
		carDeliveryrPhone = (TextView) view.findViewById(R.id.carDeliveryrPhone);
		vehicleCollectorPhone = (TextView) view.findViewById(R.id.vehicleCollectorPhone);
		if (getArguments() != null)
			shuttleMissionInfoId = getArguments().getString("id");
		// carTime.setText(getArguments().getString("time"));
		addData(shuttleMissionInfoId);
	}
	
	private void call(String phone) {  
	    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));  
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	    startActivity(intent);  
	}  
	
	private void addData(final String id) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("id", id);
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/vehiclePickUp/taskDetail"
								+ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
					@SuppressWarnings("unused")
					@Override
					public void onSuccess(String response) {
						shuttleMissionInfo = new ShuttleMissionInfo();
						try {
							JSONObject jsb = new JSONObject(response);
							if(jsb != null) {
								shuttleMissionInfo = initData(jsb);
								setView(shuttleMissionInfo);
							}
							else {
								Toast.makeText(context, "", 1).show();
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						
						try{
						}catch (Exception e) {
							Toast.makeText(context, "信息加载有误", 1).show();
						}
					}
					@SuppressWarnings("deprecation")
					@Override
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}
				});
			}
		});
	}
	private void setView(ShuttleMissionInfo shuttleMissionInfoDetail) {
		carTime.setText(shuttleMissionInfoDetail.getTime());
		carPlace.setText(shuttleMissionInfoDetail.getLocation());
		carNumber.setText(shuttleMissionInfoDetail.getPlateNumber());
		car_resource.setText(shuttleMissionInfoDetail.getDeliveryObjectNmae());
		vehicleCollectorPhone.setText(shuttleMissionInfoDetail.getCurrentPhone());
		
		if(vehicleCollectorPhone.equals("无数据")){
			callVehicleCollectorPhone.setVisibility(View.INVISIBLE); 
		}
		if(carDeliveryrPhone.equals("无数据")){
			callCarDeliveryrPhone.setVisibility(View.INVISIBLE);
		}
		sendType.setVisibility(View.GONE);
		pickUpType.setVisibility(View.GONE);
		if (shuttleMissionInfoDetail.getTaskType() == 0){
			taskType.setText("派车任务");
			carDeliveryrPhone.setText(shuttleMissionInfoDetail.getUserPhone());
		}
		if (shuttleMissionInfoDetail.getTaskType() == 1){
			taskType.setText("接车任务");
			carDeliveryrPhone.setText(shuttleMissionInfoDetail.getChargePhone());
		}
		if (shuttleMissionInfoDetail.getUnread() == 2) {
			sureButton.setVisibility(View.GONE);
			changButton.setVisibility(View.GONE);
			sendType.setVisibility(View.VISIBLE);
			pickUpType.setVisibility(View.VISIBLE);
			pickUpInfo.setVisibility(View.GONE);
		}
	}
	private ShuttleMissionInfo initData(JSONObject joooo) {
		ShuttleMissionInfo shuttleMissionInfo = new ShuttleMissionInfo();
		try {
			shuttleMissionInfo.setLocation(joooo.getString("task_place"));
		} catch (JSONException e) {
			shuttleMissionInfo.setLocation("无数据");
		}
		try {
			shuttleMissionInfo.setId(joooo.getString("id"));
		} catch (JSONException e) {
			shuttleMissionInfo.setId("无数据");
		}
		try {
			shuttleMissionInfo.setDeliveryObjectNmae(joooo.getString("deliveryObjectNmae"));
		} catch (JSONException e) {
			shuttleMissionInfo.setDeliveryObjectNmae("无数据");
		}
		try {
			shuttleMissionInfo.setUserPhone(joooo.getString("userPhone"));
		} catch (JSONException e) {
			shuttleMissionInfo.setUserPhone("无数据");
		}
		try {
			shuttleMissionInfo.setChargePhone(joooo.getString("chargePhone"));
		} catch (JSONException e) {
			shuttleMissionInfo.setChargePhone("无数据");
		}
		try {
			shuttleMissionInfo.setCurrentPhone(joooo.getString("currentPhone"));
		} catch (JSONException e) {
			shuttleMissionInfo.setCurrentPhone("无数据");
		}
		try {
			shuttleMissionInfo.setCarDeliveryId(joooo
					.getString("carDeliveryId"));
		} catch (JSONException e) {
			shuttleMissionInfo.setCarDeliveryId("无数据");
		}
		try {
			shuttleMissionInfo.setTime(joooo.getString("taskTime"));
		} catch (JSONException e) {
			shuttleMissionInfo.setTime("无数据");
		}
		try {
			shuttleMissionInfo.setPlateNumber(joooo.getString("plateNumber"));
		} catch (JSONException e) {
			shuttleMissionInfo.setPlateNumber("无数据");
		}
		try {
			shuttleMissionInfo.setTaskType(joooo.getInt("TaskType"));
		} catch (JSONException e) {
			shuttleMissionInfo.setTaskType(-1);
		}
		try {
			shuttleMissionInfo.setVehicleId(joooo.getString("vehicle_id"));
		} catch (JSONException e) {
			shuttleMissionInfo.setVehicleId("无数据");
		}
		try {
			shuttleMissionInfo.setUserId(joooo.getString("userId"));
		} catch (JSONException e) {
			shuttleMissionInfo.setUserId("无数据");
		}
		try {
			shuttleMissionInfo.setUnread(joooo.getInt("isread"));
		} catch (JSONException e) {
			shuttleMissionInfo.setUnread(-1);
		}
		return shuttleMissionInfo;
	}
	private void leadCar(final String noticeMessage) {
		handler.postDelayed(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams();
				params.put("vehicleId", shuttleMissionInfo.getVehicleId());
				params.put("receiverType", 1);
				params.put("receiverId", shuttleMissionInfo.getUserId());
				params.put("title", "领车成功");
				params.put("content",DrawerLayoutMenu.userName + "已经领取车辆" + carNumber.getText().toString());
				HttpUtil.getClient().post(
						HttpUtil.getURL("api/collarCarOperator"), params,
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {

								// 改状态
								Map param = new HashMap();
								param.put("id", shuttleMissionInfo.getId());
								param.put("state", 2);
								param.put("type", 0);
								HttpUtil.getClient()
										.put(HttpUtil
												.getURL("api/emergencyCarManage/updateSARState"
														+ ParamUtil
																.mapToString(param)),
												new AsyncHttpResponseHandler() {
													@Override
													public void onSuccess(String response) {
														try {
															String isSuccess = new JSONObject(response).getString("status");
															if (isSuccess.equals("true")) {
																Common.dialogMark(getActivity(),null,noticeMessage);
																Bundle bundle = new Bundle();
																bundle.putString("Page","LeadCar");
																bundle.putSerializable("LeadCar",new CarAssignment());
																SuccessPage success = new SuccessPage();
																success.setArguments(bundle);
																((DrawerLayoutMenu) context)
																		.changeView(success);
															} else {
																Common.dialogMark(getActivity(),null,"请求失败！");
															}
														} catch (JSONException e) {
															Common.dialogMark(
																	getActivity(),
																	null,
																	"请求错误！");
														}
													}

													@Override
													public void onFailure(
															Throwable error) {
														Common.dialogMark(
																getActivity(),
																null,
																"请求失败！");
													}
												});
							}

							@Override
							public void onFailure(Throwable error) {
								Common.dialogMark(
										getActivity(),
										null,
										"请求失败！");
							}
						});
			}
		}, 100);

	}

	private void showNormalDialog() {
		String sureMessage = "确定领取" + carNumber.getText().toString()
				+ "这辆车，领取后将由你负责这辆车";
		final String noticeMessage = "已成功领取车辆："
				+ carNumber.getText().toString();
		Common.dialog(getActivity(), null, sureMessage, "取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}, "确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						leadCar(noticeMessage);
					}

				}, null, null);
	}
}
