package com.example.newenergyvehicle.sendAndReceive;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.dialog.ChooseStation;
import com.example.newenergyvehicle.dialog.ChooseStationAdapter;
import com.example.newenergyvehicle.dialog.ChooseStationInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.salesPersonnel.ShuttleMissionInfo;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.newenergyvehicle.successPage.ISuccessPageL.ISuccessListener;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Transform extends Fragment implements DialogInfo, Serializable {
	private View view;
	private Context context;
	private Handler handler = new Handler();
	private TextView headContent;
	private TextView sureSendPerson;
	private ImageView back;
	private TextView choosePerson;
	private ChooseStation dialog;
	ChooseStationAdapter adapter;
	private ChooseStationInfo info;
	private ShuttleMissionInfo shuttleMissionInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.assigned_person, null);
		context = inflater.getContext();

		shuttleMissionInfo = (ShuttleMissionInfo) getArguments()
				.getSerializable("params");
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("转交人员");
		sureSendPerson = (TextView) view.findViewById(R.id.sure_send_person);
		choosePerson = (TextView) view.findViewById(R.id.choose_person);
		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);

		sureSendPerson.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (choosePerson.getText().toString() != "") {
					showNormalDialog();
				} else {
					Common.dialogMark(getActivity(), null, "请选择信息！");
				}

			}
		});

		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});

		choosePerson.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getPerson();
			}
		});
		return view;
	}

	@Override
	public void setInfo(ChooseStationInfo info) {
		this.info = info;
		choosePerson.setText(info.getInformation());
	}

	public void getPerson() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", Login.userId);
		HttpUtil.getClient().get(
				
				HttpUtil.getURL("api/staffManagement/searchStaff" + ParamUtil.mapToString(params)),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						try {
							JSONArray array = adapter.toInfo(new JSONArray(
									response), "user_id", "name");
							if (array.length() > 0 && array != null) {
								adapter.resetSingleData(array);
								adapter.notifyDataSetChanged();
								dialog.setTitle("选择人员");
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
	
	public void transform(){
		handler.postDelayed(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams();
				params.put("carDeliveryId",
						shuttleMissionInfo.getCarDeliveryId());
				params.put("userId", info.getId());
				HttpUtil.getClient()
						.put(HttpUtil
								.getURL("api/carDeliveryAssign/updateCarDeliveryExecute"),
								params,
								new JsonHttpResponseHandler() {

									@Override
									public void onSuccess(
											JSONObject response) {

										Bundle bundle = new Bundle();
										bundle.putString("Page",
												"Transform");
										bundle.putSerializable(
												"Transform",
												new Transform());
										SuccessPage success = new SuccessPage();
										success.setArguments(bundle);
										((DrawerLayoutMenu) context)
												.changeView(success);

										// 改状态
										Map param = new HashMap();
										param.put("id",
												shuttleMissionInfo
														.getId());
										param.put("state", 2);
										// 表示是转交
										param.put("type", 1);
										HttpUtil.getClient()
												.put(HttpUtil
														.getURL("api/emergencyCarManage/updateSARState"
																+ ParamUtil
																		.mapToString(param)),
														new AsyncHttpResponseHandler() {
															@Override
															public void onSuccess(
																	String response) {
															}

															@Override
															public void onFailure(
																	Throwable error) {
															}
														});

									}

									@Override
									public void onFailure(
											String responseBody,
											Throwable error) {
										super.onFailure(
												responseBody, error);
										Common.dialogMark(
												getActivity(),
												null, "网络异常");
									}
								});
			}
		}, 100);
	}
	
	private void showNormalDialog() {
		String sureMessage = "确定转交到" + choosePerson.getText().toString();
		Common.dialog(getActivity(), null, sureMessage, "取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}, "确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						transform();
					}

				}, null, null);
	}
}
