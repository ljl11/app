package com.example.newenergyvehicle.salesPersonnel;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.dialog.ChooseStation;
import com.example.newenergyvehicle.dialog.ChooseStationAdapter;
import com.example.newenergyvehicle.dialog.ChooseStationInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.buttonRepeatClick.NoFastClickUtils;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AssignedPerson extends Fragment implements DialogInfo {
	private View view;
	private Context context;

	private TextView headContent;
	private TextView sureSendPerson;
	private ImageView back;
	private TextView choosePerson;
	private ChooseStation dialog;
	ChooseStationAdapter adapter;
	private ChooseStationInfo info;
	private String id;
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.assigned_person, null);
		context = inflater.getContext();

		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("指派人员");
		sureSendPerson = (TextView) view.findViewById(R.id.sure_send_person);
		choosePerson = (TextView) view.findViewById(R.id.choose_person);
		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);
		if (getArguments() != null) {
			id = getArguments().getString("id");
		}
		sureSendPerson.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (NoFastClickUtils.isFastClick()) {
					Common.dialogMark(getActivity(), null, "你已指派，请勿重复指派");
				}
				else {
					if (choosePerson.getText().toString() != "") {
						handler.post(new Runnable() {
							public void run() {
								RequestParams params = new RequestParams();
								params.put("faultRecordId", id);
								params.put("recipientId", info.getId());
								params.put("acceptType", "3");// 表示转给售后人员
								HttpUtil.getClient()
										.post(HttpUtil
												.getURL("api/faultRecord/fault/Distribution"),
												params,
												new JsonHttpResponseHandler() {

													@Override
													public void onSuccess(
															JSONObject response) {
														((DrawerLayoutMenu) context)
																.changeView(new MyWork());
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
						});

					} else {
						Common.dialogMark(getActivity(), null, "请选择信息！");
					}
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
		final Map<String, String> params = new HashMap<String, String>();
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
}
