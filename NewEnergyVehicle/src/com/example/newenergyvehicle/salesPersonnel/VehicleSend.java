package com.example.newenergyvehicle.salesPersonnel;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.dialog.ChooseStation;
import com.example.newenergyvehicle.dialog.ChooseStationAdapter;
import com.example.newenergyvehicle.dialog.ChooseStationInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.search.SearchInfo;
import com.example.newenergyvehicle.search.SearchPlatenumber;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.timeDialog.DateDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleSend extends Fragment implements DialogInfo,
		OnClickListener {
	private View view;
	private Context context;
	private LayoutInflater mInflater;
	private TextView headContent;
	private TextView sendPlatenumber;
	private TextView sendPeople;
	private TextView sendTime;
	private TextView sendObject;
	private TextView sendType;
	private TextView sendEmergencySubmit;
	private DatePickerDialog dateDialog;
	private ChooseStation dialog;
	ChooseStationAdapter adapter;
	private SearchInfo info;
	private ChooseStationInfo peopleInfo;
	private ImageView back;
	private int state;
	private JSONArray type;
	private String choosiedType;
	private String sendObjectId;
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.vehicle_send, null);
		context = inflater.getContext();
		initType();
		initView();
		return view;
	}

	private void initType() {
		type = new JSONArray();
		JSONObject js1 = new JSONObject();
		try {
			js1.put("id", "0");
			js1.put("information", "客户");
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
		JSONObject js3 = new JSONObject();
		try {
			js3.put("id", "2");
			js3.put("information", "维修站");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		type.put(js1);
		type.put(js2);
		type.put(js3);
	}

	private void initView() {
		mInflater = LayoutInflater.from(getActivity());
		sendPlatenumber = (TextView) view.findViewById(R.id.send_platenumber);
		sendPeople = (TextView) view.findViewById(R.id.send_people);
		sendObject = (TextView) view.findViewById(R.id.send_object);
		sendType = (TextView) view.findViewById(R.id.send_type);
		sendEmergencySubmit = (TextView) view
				.findViewById(R.id.send_emergency_submit);
		sendTime = (TextView) view.findViewById(R.id.send_time);
		headContent = (TextView) view.findViewById(R.id.module_title);
		back = (ImageView) view.findViewById(R.id.back);
		headContent.setText("送车任务");
		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);
		Bundle b = getArguments();
		if (b != null) {
			info = (SearchInfo) getArguments().getSerializable("info");
			sendPlatenumber.setText(info.getName());
		}
		back.setOnClickListener(this);
		sendTime.setOnClickListener(this);
		sendPlatenumber.setOnClickListener(this);
		sendPeople.setOnClickListener(this);
		sendEmergencySubmit.setOnClickListener(this);
		sendObject.setOnClickListener(this);
		sendType.setOnClickListener(this);
		dateDialog = DateDialog.getDateDialog(context);
		dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						sendTime.setText(DateDialog.getTime());
					}
				});
	}

	@Override
	public void setInfo(ChooseStationInfo info) {

		switch (state) {
		case 0: {
			this.peopleInfo = info;
			sendPeople.setText(info.getInformation());
		}
			break;
		case 1: {
			sendType.setText(info.getInformation());
			choosiedType = info.getId();
			break;
		}
		case 2: {
			sendObject.setText(info.getInformation());
			sendObjectId = info.getId();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			((DrawerLayoutMenu) context).back();
			break;
		case R.id.send_time:
			dateDialog.show();
			break;
		case R.id.send_people:
			showDialog("选择派送人员", 0);
			break;
		case R.id.send_platenumber:
			Bundle bundle = new Bundle();
			bundle.putString("type", "0");
			bundle.putString("url", "api/vehicle/vehicles");
			SearchPlatenumber ca = new SearchPlatenumber();
			ca.setArguments(bundle);
			((DrawerLayoutMenu) context).changeView(ca);
			break;
		case R.id.send_emergency_submit:
			submit();
			break;
		case R.id.send_type:
			showDialog("选择派送类型", 1);
			break;
		case R.id.send_object:
			showDialog("选择派送对象", 2);
			break;
		}
	}

	private void showDialog(String title, int index) {
		state = index;
		dialog.setTitle(title);
		switch (state) {
		case 0: {
			getPerson();
			break;
		}
		case 1: {
			chooseSendType();
			break;
		}
		case 2: {
			chooseSendObject();
			break;
		}
		default:
			break;
		}
	}

	private void chooseSendType() {
		adapter.resetSingleData(type);
		adapter.notifyDataSetChanged();
		dialog.show();
	}

	private void chooseSendObject() {
		if (choosiedType == "0") {
			getCustomer();
		} else if (choosiedType == "1") {
			getWarehouse();
		} else if (choosiedType == "2") {
			getStation();
		} else {
			Toast.makeText(context, "请先选择派送类型", Toast.LENGTH_SHORT).show();
		}
	}

	public void getPerson() {
		handler.post(new Runnable() {
			public void run() {
				HttpUtil.getClient().get(
						// 获取维修站数据
						HttpUtil.getURL("api/staffManagement/searchStaff"),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray array = adapter.toInfo(
											new JSONArray(response), "user_id",
											"name");
									if (array.length() > 0 && array != null) {
										adapter.resetSingleData(array);
										adapter.notifyDataSetChanged();
										dialog.show();
									} else {
										Toast.makeText(context, "暂无信息",
												Toast.LENGTH_SHORT).show();
									}
								} catch (Exception e) {

									Toast.makeText(context, "信息加载有误",
											Toast.LENGTH_SHORT).show();
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

	private void getStation() {
		handler.post(new Runnable() {
			public void run() {
				HttpUtil.getClient().get(
						// 获取维修站数据
						HttpUtil.getURL("api/passUnits"),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray array = adapter.toInfo(
											new JSONArray(response), "id",
											"unitName");
									if (array.length() > 0 && array != null) {
										adapter.resetSingleData(array);
										adapter.notifyDataSetChanged();
										dialog.show();
									} else {
										Toast.makeText(context, "暂无信息",
												Toast.LENGTH_SHORT).show();
									}
								} catch (Exception e) {

									Toast.makeText(context, "信息加载有误",
											Toast.LENGTH_SHORT).show();
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

	private void getWarehouse() {
		handler.post(new Runnable() {
			public void run() {
				HttpUtil.getClient().get(
						// 获取维修站数据
						HttpUtil.getURL("api/warehouse/warehouses"),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray array = adapter.toInfo(
											new JSONArray(response), "id",
											"name");
									if (array.length() > 0 && array != null) {
										adapter.resetSingleData(array);
										adapter.notifyDataSetChanged();
										dialog.show();
									} else {
										Toast.makeText(context, "暂无信息",
												Toast.LENGTH_SHORT).show();
									}
								} catch (Exception e) {

									Toast.makeText(context, "信息加载有误",
											Toast.LENGTH_SHORT).show();
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

	private void getCustomer() {
		handler.post(new Runnable() {
			public void run() {
				HttpUtil.getClient().get(
						// 获取维修站数据
						HttpUtil.getURL("api/leaseUser/leaseUsers"),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray array = adapter.toInfo(
											new JSONArray(response), "uid",
											"unitName");
									if (array.length() > 0 && array != null) {
										adapter.resetSingleData(array);
										adapter.notifyDataSetChanged();
										dialog.show();
									} else {
										Toast.makeText(context, "暂无信息",
												Toast.LENGTH_SHORT).show();
									}
								} catch (Exception e) {

									Toast.makeText(context, "信息加载有误",
											Toast.LENGTH_SHORT).show();
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

	private void submit() {

		if (sendPlatenumber.getText().toString() != ""
				&& sendTime.getText().toString() != ""
				&& sendObject.getText().toString() != ""
				&& sendPeople.getText().toString() != "") {
			handler.post(new Runnable() {
				public void run() {
					RequestParams param = new RequestParams();
					param.put("vehicleId", info.getId());
					param.put("taskType", "0");
					param.put("taskTime", sendTime.getText().toString());
					param.put("taskPlace", sendObject.getText().toString());
					param.put("serverId", peopleInfo.getId());
					param.put("deliveryObject", sendObjectId);

					HttpUtil.getClient().post(
							HttpUtil.getURL("api/carDelivery/carDeliveryObject"),
							param, new JsonHttpResponseHandler() {

								@Override
								public void onSuccess(JSONObject response) {
									try {
										int isSuccess = response
												.getInt("status");
										if(isSuccess == 2){
											Common.dialogMark(getActivity(),
													null, "车辆派送中");
										}
										else if (isSuccess>=0) {
											((DrawerLayoutMenu) context)
													.changeView(new ShuttleMission());
											Common.dialogMark(getActivity(),
													null, "新增成功");
										} 
										else {
											Common.dialogMark(getActivity(),
													null, "请求失败！");
										}
									} catch (JSONException e) {
										Common.dialogMark(getActivity(), null,
												"请求错误！");
									}
								}

								@Override
								public void onFailure(String responseBody,
										Throwable error) {
									super.onFailure(responseBody, error);
									Common.dialogMark(getActivity(), null,
											"网络异常");
								}
							});
				}
			});

		} else {
			Common.dialogMark(getActivity(), null, "信息填写未完整！");
		}
	}
}
