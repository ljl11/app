package com.example.newenergyvehicle.emergencyVehicle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.check.CheckInfo;
import com.example.newenergyvehicle.dialog.ChooseStation;
import com.example.newenergyvehicle.dialog.ChooseStationAdapter;
import com.example.newenergyvehicle.dialog.ChooseStationInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.newenergyvehicle.successPage.ISuccessPageL.ISuccessListener;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.String.StringUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.timeDialog.DateDialog;
import com.example.util.timeDialog.DateTimePickDialogUtil;
import com.google.zxing.common.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Arrangement extends Fragment implements DialogInfo, NeedRefresh {
	private View view;
	private Context context;
	private TextView module_title;
	private TextView car_arrangement_next;// 同意
	private TextView carArrangementTime; // 派送时间
	private DatePickerDialog dateDialog;
	private TextView carArrangmentSendMan;
	private TextView carArrangmentName;// 客户名称
	private EditText carArrangmentPlace;// 派送地点
	private TextView carArrangmentPlateNumber;// 车牌号
	private List<View> viewList = new ArrayList<View>();
	private final int size = 5;
	String userId, vehicleId, serverId, faultRecordId = null;
	private int page = 1;
	private int statue = 0;
	// 派送人;
	private ImageView back;
	private ChooseStation dialog;
	private CheckInfo detailInfo;
	private boolean needUpstate = false; // 判断是否需要更新应急车审核
	private boolean notneedUpdate = false;
	private boolean isFromFaultHandling = true;
	private String applyId = null;
	private String applyTime;
	private boolean operator = false;//是否已经处理过这条任务 是 true 不是 false

	ChooseStationAdapter adapter;
	JSONArray users;
	JSONArray cars;
	JSONArray employees;

	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.car_arrangement, null);
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("应急车安排");
		context = inflater.getContext();
		back = (ImageView) view.findViewById(R.id.back);
		carArrangmentName = (TextView) view
				.findViewById(R.id.car_arrangment_name);
		addViewToList(carArrangmentName);
		carArrangmentPlateNumber = (TextView) view
				.findViewById(R.id.car_arrangment_plate_number);
		addViewToList(carArrangmentPlateNumber);
		carArrangmentPlace = (EditText) view
				.findViewById(R.id.car_arrangment_place);
		addViewToList(carArrangmentPlace);
		carArrangementTime = (TextView) view
				.findViewById(R.id.car_arrangement_time);
		addViewToList(carArrangementTime);
		carArrangmentSendMan = (TextView) view
				.findViewById(R.id.car_arrangment_send_man);
		addViewToList(carArrangmentSendMan);
		
		if (getArguments() != null)
			getDataFromVCI();
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		((LinearLayout) view.findViewById(R.id.arrangement_time))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dateDialog.show();
					}
				});

		car_arrangement_next = (TextView) view
				.findViewById(R.id.car_arrangement_next);
		car_arrangement_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				initState(faultRecordId);
				boolean s = operator;
				if(!operator)
					submit(); // 提交方法
				else {
					Toast.makeText(context, "该任务已经处理", Toast.LENGTH_SHORT).show();
					((DrawerLayoutMenu) context).changeView(new MyWork());
				}
					
				if (needUpstate)
					agree(); // 如果是从应急车审核页面跳转过来的需要更新审核未同意
			}
		});

		dateDialog = DateDialog.getDateDialog(context);
		dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						carArrangementTime.setText(DateDialog.getTime());
					}
				});

		carArrangmentPlateNumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getCar();
			}
		});

		carArrangmentName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getUser();
			}
		});

		carArrangmentSendMan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getEmployees();
			}
		});

		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);
		return view;
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
	
	private void getDataFromVCI() {
		if (getArguments().getString("Page") != null
				&& (getArguments().getString("Page").equals("fromMyWork")||
						getArguments().getString("Page").equals("fromFaultHandling"))) {
			if(getArguments().getString("Page").equals("fromMyWork"))
				isFromFaultHandling = false;
			notneedUpdate = true;
			carArrangmentName.setText(getArguments().getString("applicantName"));
			userId = getArguments().getString("applicantId");
			faultRecordId = getArguments().getString("faultRecordId");
		} else {
			isFromFaultHandling = false;
			needUpstate = true;
			detailInfo = (CheckInfo) getArguments().getSerializable(
					"detailInfo");
			faultRecordId = detailInfo.getFaultRecordId();
			carArrangmentName.setText(getArguments().getString("customerName"));
			carArrangementTime.setText(detailInfo.getApply_time());
			carArrangmentPlace.setText(detailInfo.getApply_address());
			userId = getArguments().getString("customerId");
			applyId = getArguments().getString("applyId");
			applyTime = getArguments().getString("applyTime");
		}
	}

	private void submit() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				RequestParams param = validate();
				if (param != null) {
					param.put("taskType", "0");
					param.put("faultRecordId", faultRecordId);
					HttpUtil.getClient()
							.post(HttpUtil
									.getURL("api/emergencyCarManage/emergencyCarRecord"),
									param, new AsyncHttpResponseHandler() {
										@Override
										public void onSuccess(String response) {
											try {
												if (response != null) {
													if (new JSONObject(response).getString("status").equals("true")) {
														if(applyId!=null)
															sendNotice();
														Bundle bundle = new Bundle();
														Fragment fa = new SuccessPage();
														if ((needUpstate || notneedUpdate)&& isFromFaultHandling==false) {
															bundle.putString("Page","fromMyWork");
															bundle.putString("backPage","toMyWork");
														}
														else if ((needUpstate || notneedUpdate)&& isFromFaultHandling==true) {
															bundle.putString("Page","fromMyWork");
															bundle.putString("backPage","toFaultHandling");
														}

														else {
															bundle.putString("Page","toArrangement");
															bundle.putString("backPage","toMain");
														}
														fa.setArguments(bundle);
														((DrawerLayoutMenu) context).changeView(fa);
													} else {
														Toast.makeText(context,"新增失败",Toast.LENGTH_SHORT).show();
													}
												}
											} catch (Exception e) {
												Common.dialogMark(getActivity(), null,"信息加载有误");
											}
										}

										@Override
										public void onFailure(Throwable error) {
											super.onFailure(error);
										}
									});
				} else {
					Toast.makeText(context, "信息未填写完整", Toast.LENGTH_SHORT).show();
				}
			}
		}, 10);

	}

	private RequestParams validate() {
		RequestParams params = new RequestParams();

		if (valid(params, "taskPlace", carArrangmentPlace.getText().toString())) {
			return null;
		}

		if (valid(params, "userId", userId)) {
			return null;
		}

		if (valid(params, "vehicleId", vehicleId)) {
			return null;
		}

		if (valid(params, "taskTime", carArrangementTime.getText().toString())) {
			return null;
		}

		if (valid(params, "serverId", serverId)) {
			return null;
		}

		return params;
	}

	private boolean valid(RequestParams params, String key, String value) {
		boolean flag = StringUtil.isNullOrEmpty(value);

		if (!flag) {
			params.put(key, value);
		}
		return flag;
	}

	public EmergencyInfo getEntity(JSONObject joooo) {
		EmergencyInfo emergencyInfo = new EmergencyInfo();
		try {
			emergencyInfo.setVehicleId(joooo.getString("vehicle_id"));
		} catch (Exception e) {
			emergencyInfo.setVehicleId("暂无数据");
		}
		try {
			emergencyInfo.setPlateNumber(joooo.getString("plateNumber"));
		} catch (Exception e) {
			emergencyInfo.setPlateNumber("暂无数据");
		}
		return emergencyInfo;
	}

	@Override
	public void setInfo(ChooseStationInfo info) {
		switch (statue) {
		case 1: {
			carArrangmentPlateNumber.setText(info.getInformation());
			vehicleId = info.getId();
		}
			break;
		case 2: {
			carArrangmentName.setText(info.getInformation());
			userId = info.getId();
			break;
		}
		case 3: {
			carArrangmentSendMan.setText(info.getInformation());
			serverId = info.getId();
			break;
		}
		default:
			break;
		}
	}

	public void showDialog(JSONArray array, String title, int index,String message) {
		if (array != null && array.length() > 0) {
			dialog.setTitle(title);
			statue = index;
			try {
				setData(array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dialog.show();
		} else {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	public void setData(JSONArray array) throws JSONException {
		switch (statue) {
		case 1: {
			adapter.resetSingleData(adapter.toInfo(array, "vehicle_id",
					"plateNumber"));
			break;
		}
		case 3: {
			adapter.resetSingleData(adapter.toInfo(array, "user_id", "name"));
			break;
		}
		default: {
			adapter.resetSingleData(adapter.toInfo(array, "uid", "name"));
			break;
		}
		}

	}

	public void addViewToList(View view) {
		viewList.add(view);
	}

	public void clearInput() {
		for (int i = 0, len = viewList.size(); i < len; i++) {
			if (viewList.get(i) instanceof TextView) {
				((TextView) viewList.get(i)).setText("");
			} else
				((EditText) viewList.get(i)).setText("");
		}
	}

	private void agree() {
		handler.postDelayed(new Runnable() {
			public void run() {
				Map param = new HashMap();
				param.put("id", detailInfo.getId());
				param.put("state", 2);
				HttpUtil.getClient()
						.put(HttpUtil
								.getURL("api/emergencyCarManage/updateCheckState"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
									}

									@Override
									public void onFailure(Throwable error) {
									}
								});
			}
		}, 10);

	}
	
	public void getCar(){
				HttpUtil.getClient()
				.get(HttpUtil
						.getURL("api/emergencyCarManage/emergencyCarsForArrange"),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {

									cars = (new JSONArray(response)); // 给carList赋值
									showDialog(cars, "选择车辆", 1,"暂无应急车");

								} catch (Exception e) {

									Common.dialogMark(getActivity(),
											null, "信息加载有误");
								}
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
	}
	
	public void getUser(){
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/leaseUser/leaseUsers"),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {

									users = (new JSONArray(response));
									showDialog(users, "选择客户", 2,"暂无客户信息");

								} catch (Exception e) {

									Common.dialogMark(getActivity(), null,
											"信息加载有误");
								}
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
	}
	
	public void getEmployees(){
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/staffManagement/searchStaff"),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {

									employees = (new JSONArray(response));
									showDialog(employees, "选择派送人", 3,"暂无派送人");
								} catch (Exception e) {
									Common.dialogMark(getActivity(), null,
											"信息加载有误");
								}
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
	}

	@Override
	public void redresh() {
		clearInput();
	}
	
	private void sendNotice() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				String content = "您于"+applyTime+"申请的应急车已同意！";
				String receiverIds = applyId;
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
