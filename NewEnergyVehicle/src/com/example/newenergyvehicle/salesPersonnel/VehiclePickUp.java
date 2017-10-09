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
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.search.SearchInfo;
import com.example.newenergyvehicle.search.SearchPlatenumber;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.buttonRepeatClick.NoFastClickUtils;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VehiclePickUp extends Fragment implements DialogInfo,
		OnClickListener {

	private View view;
	private Context context;
	private LayoutInflater mInflater;
	private TextView headContent;
	private TextView pickUpPlatenumber;
	private TextView pickUpPeople;
	private TextView pickUpTime;
	private EditText pickUpPlace;
	private TextView pickUpEmergencySubmit;
	private ImageView back;
	private DatePickerDialog dateDialog;
	private ChooseStation dialog;
	ChooseStationAdapter adapter;
	private SearchInfo plateInfo;
	private TextView pickUpObject;
	private int state;
	private String pickUpPeopleId;
	private String pickUpObjectId;
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.vehicle_pick_up, null);
		context = inflater.getContext();
		initView();
		return view;
	}

	private void initView() {
		mInflater = LayoutInflater.from(getActivity());
		pickUpPlatenumber = (TextView) view
				.findViewById(R.id.pick_up_platenumber);
		pickUpPeople = (TextView) view.findViewById(R.id.pick_up_people);
		pickUpPlace = (EditText) view.findViewById(R.id.pick_up_place);
		pickUpObject = (TextView) view.findViewById(R.id.pick_up_object);
		pickUpEmergencySubmit = (TextView) view
				.findViewById(R.id.pick_up_emergency_submit);
		pickUpTime = (TextView) view.findViewById(R.id.pick_up_time);
		back = (ImageView) view.findViewById(R.id.back);
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("接车任务");
		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);
		if (getArguments() != null) {
			plateInfo = (SearchInfo) getArguments().getSerializable("info");
			pickUpPlatenumber.setText(plateInfo.getName());

		}
		dateDialog = DateDialog.getDateDialog(context);
		dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						pickUpTime.setText(DateDialog.getTime());
					}
				});

		back.setOnClickListener(this);
		pickUpTime.setOnClickListener(this);
		pickUpPlatenumber.setOnClickListener(this);
		pickUpPeople.setOnClickListener(this);
		pickUpEmergencySubmit.setOnClickListener(this);
		pickUpObject.setOnClickListener(this);
	}

	public boolean getPerson() {
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

		return false;
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

	@Override
	public void setInfo(ChooseStationInfo info) {
		switch (state) {
		case 0: {
			pickUpPeople.setText(info.getInformation());
			pickUpPeopleId = info.getId();
		}
			break;
		case 1: {
			pickUpObject.setText(info.getInformation());
			pickUpObjectId = info.getId();
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
		case R.id.pick_up_time:
			dateDialog.show();
			break;
		case R.id.pick_up_people:
			showDialog("选择人员", 0);
			break;
		case R.id.pick_up_platenumber:
			Bundle bundle = new Bundle();
			bundle.putString("type", "1");
			bundle.putString("url", "api/vehicle/vehicles");
			SearchPlatenumber ca = new SearchPlatenumber();
			ca.setArguments(bundle);
			((DrawerLayoutMenu) context).changeView(ca);
			break;
		case R.id.pick_up_emergency_submit: {
			if(NoFastClickUtils.isFastClick())
				Common.dialogMark(getActivity(), null, "请勿重复派发任务");
			else
				submit();
		}
			
			break;
		case R.id.pick_up_object:
			showDialog("选择接送对象", 1);
			break;
		}
	}

	public void submit() {

		if (pickUpPlatenumber.getText().toString() != ""
				&& pickUpTime.getText().toString() != ""
				&& pickUpPlace.getText().toString() != ""
				&& pickUpPeople.getText().toString() != "") {
			handler.post(new Runnable() {
				public void run() {
					RequestParams params = new RequestParams();
					params.put("vehicleId", plateInfo.getId());
					params.put("taskType", "1");
					params.put("taskTime", pickUpTime.getText().toString());
					params.put("taskPlace", pickUpPlace.getText().toString());
					params.put("serverId", pickUpPeopleId);
					params.put("deliveryObject", pickUpObjectId);

					HttpUtil.getClient().post(
							HttpUtil.getURL("api/carDelivery/carDeliveryObject"),
							params, new JsonHttpResponseHandler() {

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
	
	private void showDialog(String title, int index) {
		state = index;
		dialog.setTitle(title);
		switch (state) {
		case 0: {
			getPerson();
			break;
		}
		case 1: {
			getWarehouse();
			break;
		}
		default:
			break;
		}
	}

}
