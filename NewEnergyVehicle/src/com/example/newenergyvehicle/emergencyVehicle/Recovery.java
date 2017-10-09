package com.example.newenergyvehicle.emergencyVehicle;

import java.util.ArrayList;
import java.util.List;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.dialog.ChooseStation;
import com.example.newenergyvehicle.dialog.ChooseStationAdapter;
import com.example.newenergyvehicle.dialog.ChooseStationInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.newenergyvehicle.successPage.ISuccessPageL.ISuccessListener;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.String.StringUtil;
import com.example.util.buttonRepeatClick.NoFastClickUtils;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.timeDialog.DateDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Recovery extends Fragment implements DialogInfo, NeedRefresh,
		OnClickListener {
	private View view;
	private Context context;
	private TextView module_title;
	private TextView carRecoverNext;// 确定
	private TextView carRecoveryTime; // 回收时间
	private TextView carRecoveryName;// 客户名称
	private EditText carRecoveryPlace;// 回收地点
	private TextView carRecoveryNumber;// 车牌号
	private TextView carRecoveryMan; // 回收人
	private TextView carRecoveryMethod; // 回收类型
	private DatePickerDialog dateDialog;
	private LinearLayout recoveryTime; // 回收时间
	private TextView recoveryMethods;
	private LinearLayout displayChooseMan;
	private TextView recoveryMan;//接车人
	private String choosiedType;
	private String faultRecodId;
	private String id;//安排历史id
	private ImageView back;
	private List<View> viewList = new ArrayList<View>();
	JSONArray users;
	JSONArray cars;
	JSONArray employees;
	JSONArray warehouse;
	String userId, vehicleId, serverId =null;
	private int statue = 0;
	private JSONArray type;
	private ChooseStation dialog;
	ChooseStationAdapter adapter;
	private String deliveryObjectType; //派送对象类型
	private String deliveryObject; //派送对象id
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.car_receive, null);
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("应急车回收");
		context = inflater.getContext();

		initView();
		initType();
		init(); // 加载数据
		return view;
	}

	private void initType() {
		type = new JSONArray();
		JSONObject js1 = new JSONObject();
		try {
			js1.put("id", "0");
			js1.put("information", "负责人");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject js2 = new JSONObject();
		try {
			js2.put("id", "1");
			js2.put("information", "仓库");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		type.put(js1);
		type.put(js2);

	}

	private void initView() {
		carRecoveryMethod = (TextView) view
				.findViewById(R.id.car_recovery_method);
		recoveryTime = ((LinearLayout) view.findViewById(R.id.recovery_time));
		carRecoveryName = (TextView) view.findViewById(R.id.car_recovery_name);
		carRecoveryNumber = (TextView) view
				.findViewById(R.id.car_recovery_plate_number);
		carRecoveryPlace = (EditText) view
				.findViewById(R.id.car_recovery_place);
		carRecoveryTime = (TextView) view.findViewById(R.id.car_recovery_time);
		carRecoveryMan = (TextView) view.findViewById(R.id.car_recovery_man);
		carRecoverNext = (TextView) view
				.findViewById(R.id.car_arrangement_next);
		recoveryMethods = (TextView) view.findViewById(R.id.recovery_methods);
		displayChooseMan = (LinearLayout) view.findViewById(R.id.display_recovery_man);
		recoveryMan = (TextView) view.findViewById(R.id.recovery_pickup_man);
		back = (ImageView) view.findViewById(R.id.back);
		addViewToList(carRecoveryMethod);
		addViewToList(carRecoveryName);
		addViewToList(carRecoveryNumber);
		addViewToList(carRecoveryPlace);
		addViewToList(carRecoveryTime);
		addViewToList(carRecoveryMan);

		back.setOnClickListener(this);
		if (getArguments() != null) {
			if (getArguments().containsKey("plateNumber")) {
				carRecoveryNumber.setText(getArguments().getString(
						"plateNumber"));
			}

			if (getArguments().containsKey("applyPerson")) {
				carRecoveryName
						.setText(getArguments().getString("applyPerson"));
			}
			if (getArguments().containsKey("platenumber")) {
				carRecoveryNumber.setText(getArguments().getString(
						"platenumber"));
			}
			if (getArguments().containsKey("customer")) {
				carRecoveryName.setText(getArguments().getString("customer"));
			}
			if (getArguments().containsKey("userId"))
				userId = (getArguments().getString("userId"));
			if (getArguments().containsKey("vehicleId"))
				vehicleId = (getArguments().getString("vehicleId"));
			if (getArguments().containsKey("faultRecodId"))
				faultRecodId = (getArguments().getString("faultRecodId"));
			if (getArguments().containsKey("id"))
				id = (getArguments().getString("id"));
		} else {
			carRecoveryNumber.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					showDialog(cars, "选择车辆", 1);
				}

			});
		}

		recoveryTime.setOnClickListener(this);
		carRecoveryMethod.setOnClickListener(this);
		carRecoveryMan.setOnClickListener(this);
		carRecoverNext.setOnClickListener(this);
		recoveryMan.setOnClickListener(this);
		dateDialog = DateDialog.getDateDialog(context);
		dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						carRecoveryTime.setText(DateDialog.getTime());
					}
				});

		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);
	}

	private void submit() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				RequestParams param = validate();
				if (param != null) {
					param.put("taskType", "1");
					param.put("deliveryObjectType",deliveryObjectType);
					param.put("deliveryObject",deliveryObject);
					param.put("serverId",serverId);
					param.put("faultRecordId", faultRecodId);
					param.put("arrangementId", id);
					HttpUtil.getClient()
							.post(HttpUtil
									.getURL("api/emergencyCarManage/emergencyCarRecord"),
									param, new AsyncHttpResponseHandler() {
										@Override
										public void onSuccess(String response) {
											try {
												if (response != null) {
													if (new JSONObject(response)
															.getString("status")
															.equals("true")) {
														Bundle bundle = new Bundle();
														Fragment fa = new SuccessPage();
														bundle.putString(
																"Page",
																"Recovery");
														bundle.putString(
																"backPage",
																"toMain");
														fa.setArguments(bundle);
														((DrawerLayoutMenu) context)
																.changeView(fa);

													} else {
														Toast.makeText(context,
																"新增失败", Toast.LENGTH_SHORT)
																.show();
													}
												}
											} catch (Exception e) {
												Common.dialogMark(
														getActivity(), null,
														"信息加载有误");
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
		});
	}

	private RequestParams validate() {
		RequestParams params = new RequestParams();

		if (valid(params, "taskPlace", carRecoveryPlace.getText().toString())) {
			return null;
		}

		if (valid(params, "userId", userId)) {
			return null;
		}

		if (valid(params, "vehicleId", vehicleId)) {
			return null;
		}

		if (valid(params, "taskTime", carRecoveryTime.getText().toString())) {
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

	private void init() {

		handler.post(new Runnable() {
			@Override
			public void run() {
				// 应急车表
				HttpUtil.getClient()
						.get(HttpUtil
								.getURL("api/emergencyCarManage/emergencyCarsForRecovery"),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										try {
											cars = (new JSONArray(response)); // 给carList赋值
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
		});

	}

	@Override
	public void setInfo(ChooseStationInfo info) {
		switch (statue) {
		case 1: {
			carRecoveryNumber.setText(info.getInformation());
			vehicleId = info.getId();
			carRecoveryName.setText(info.getUserName());
			userId = info.getUserId();
		} 
			break;
		case 2:{
			carRecoveryMan.setText(info.getInformation());
			deliveryObject = info.getId();
		}
		case 3: {
			carRecoveryMan.setText(info.getInformation());
			deliveryObject = info.getId();
			if(choosiedType == "0")
				serverId = info.getId();
			break;
		}
		case 4: {
			carRecoveryMethod.setText(info.getInformation());
			carRecoveryMan.setText("");
			recoveryMan.setText("");
			choosiedType = info.getId();
			if (choosiedType == "0") {
				recoveryMethods.setText("回收人:");
				displayChooseMan.setVisibility(View.GONE);
			} else if (choosiedType == "1") {
				recoveryMethods.setText("仓库:");
				displayChooseMan.setVisibility(View.VISIBLE);
			}
			break;
		}
		case 5: {
			recoveryMan.setText(info.getInformation());
			serverId = info.getId();
		}
		default:
			break;
		}

	}

	public void showDialog(JSONArray array, String title, int index) {
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
			Toast.makeText(context, "暂无信息", Toast.LENGTH_SHORT).show();
		}
	}
	
	

	private void chooseRecoveryType() {
		adapter.resetSingleData(type);
		adapter.notifyDataSetChanged();
		showDialog(cars, "选择回收类型", 4);
	}

	private void chooseSendObject() {

		if (choosiedType == "0") {
			getRecoveryMan(3);
			deliveryObjectType = "2";
		} else if (choosiedType == "1") {
			getRecoveryWarehouse();
			deliveryObjectType = "1";
		} else {
			Toast.makeText(context, "请先选择回收类型", Toast.LENGTH_SHORT).show();
		}
	}
	

	private void getRecoveryWarehouse() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				HttpUtil.getClient().get(HttpUtil.getURL("api/warehouse/warehouses"), new AsyncHttpResponseHandler() {
					 @Override
					 public void onSuccess(String response) {
						 try{
							 warehouse = new JSONArray(response);
							 showDialog(warehouse, "选择回收仓库", 2);
							}catch (Exception e) {
									
								Common.dialogMark(getActivity(), null, "信息加载有误");
							}
					 }
					@Override
					 public void onFailure(Throwable error) {
						super.onFailure(error);
					}
					 });
			}
			
		});
		
	}

	private void getRecoveryMan(final int index) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/staffManagement/searchStaff"),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									employees = (new JSONArray(response));
									showDialog(employees, "选择人员", index);
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
		});
	}

	public void setData(JSONArray array) throws JSONException {
		switch (statue) {
		case 1: {
			adapter.resetSingleData(adapter.toInfo(array, "vehicle_id",
					"plateNumber", "user_id", "userName"));
			break;
		}
		
		case 2: {
			adapter.resetSingleData(adapter.toInfo(array, "id", "name"));
			break;
		}
		case 3: {
			adapter.resetSingleData(adapter.toInfo(array, "user_id", "name"));
			break;
		}
		case 5:
			adapter.resetSingleData(adapter.toInfo(array, "user_id","name"));
		default: {
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

	@Override
	public void redresh() {
		init();
		clearInput();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			((DrawerLayoutMenu) context).back();
			break;
		case R.id.car_arrangement_next:
			if(NoFastClickUtils.isFastClick()) {
				Common.dialogMark(getActivity(), null, "您已提交故障，请不要重复提交");
			}
			else
				submit();
			break;
		case R.id.car_recovery_man:
			chooseSendObject();
			break;
		case R.id.car_recovery_method:
			chooseRecoveryType();
			break;
		case R.id.recovery_time:
			dateDialog.show();
			break;
		case R.id.recovery_pickup_man:
			getRecoveryMan(5);
		default:
			break;
		}
	}
}
