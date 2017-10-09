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
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.buttonRepeatClick.NoFastClickUtils;
import com.example.util.params.ParamUtil;
import com.example.util.timeDialog.DateDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.JsonHttpResponseHandler;

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

public class TransferStation extends Fragment implements DialogInfo {
	private View view;
	private Context context;

	private TextView headContent;
	private TextView chooseStation;
	private TextView transferStation;
	private EditText faultDistribution;
	private ChooseStation dialog;
	ChooseStationAdapter adapter;
	private DatePickerDialog dateDialog;
	private ImageView back;
	private ChooseStationInfo info;
	private String faultRecordId;
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.transfer_station, null);
		context = inflater.getContext();

		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("转交第三方");
		transferStation = (TextView) view.findViewById(R.id.transfer_station);
		chooseStation = (TextView) view.findViewById(R.id.station_name);
		faultDistribution = (EditText) view.findViewById(R.id.fault_destribution_ts);
		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);
		Bundle b = getArguments();
		if (b != null) {
			faultRecordId = getArguments().getString("id");
		}

		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		
		chooseStation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getStation();
			}
		});
		transferStation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(NoFastClickUtils.isFastClick()) {
					Common.dialogMark(getActivity(), null, "请勿重复转交");
				}
				else {
					if (chooseStation.getText().toString() != "") {
						handler.post(new Runnable() {
							public void run() {
								RequestParams param = new RequestParams();
								param.put("recipientId", info.getId());
								param.put("faultRecordId", faultRecordId);
								param.put("acceptType", "4"); // 转交给维修站
								if(!faultDistribution.getText().toString().equals("")){
									param.put("distributionDescription", faultDistribution.getText().toString());
								}
								HttpUtil.getClient().post(HttpUtil.getURL("api/faultRecord/fault/Distribution"), param,
										new JsonHttpResponseHandler() {

											@Override
											public void onSuccess(JSONObject response) {
												try {
													String isSuccess = response.getString("status");
													if (isSuccess.equals("true")) {
														Common.dialogMark(getActivity(), null, "转交成功");
														((DrawerLayoutMenu) context).changeView(new MyWork());
													} else {
														Common.dialogMark(getActivity(), null, "请求失败！");
													}
												} catch (JSONException e) {
													Common.dialogMark(getActivity(), null, "请求错误！");
												}
											}

											@Override
											public void onFailure(String responseBody, Throwable error) {
												super.onFailure(responseBody, error);
												Common.dialogMark(getActivity(), null, "网络异常");
											}
										});
							}
						});

					} else {
						Common.dialogMark(getActivity(), null, "数据填写不完整！");
					}
				}
			}
		});

//		dateDialog = DateDialog.getDateDialog(context);
//		dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						completeTime.setText(DateDialog.getTime());
//					}
//				});

		return view;
	}

	private void getStation() {
		handler.post(new Runnable() {
			public void run() {
				Map param = new HashMap();
				param.put("unitType","0000000600000002");
				HttpUtil.getClient().get(
						// 获取维修站数据
						HttpUtil.getURL("api/passUnits"
						+ ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray array = adapter.toInfo(
											new JSONArray(response), "userId",
											"unitName");
									if (array.length() > 0 && array != null) {
										adapter.resetSingleData(array);
										adapter.notifyDataSetChanged();
										dialog.setTitle("选择第三方");
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
		this.info = info;
		chooseStation.setText(info.getInformation());
	}

}
