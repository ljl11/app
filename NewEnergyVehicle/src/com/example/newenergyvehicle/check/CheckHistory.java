package com.example.newenergyvehicle.check;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class CheckHistory extends Fragment {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private TextView module_title;
	private ImageView back;
	private Handler handler = new Handler();
	private String id;
	private CheckInfo checkInfo;
	private TextView application;
	private TextView location;
	private TextView date;
	private TextView application_resaon;
	private TextView result;
	private TextView backButton;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		view = inflater.inflate(R.layout.vehicle_check_history, container,
				false);
		context = inflater.getContext();

		initView();
		getData();

		return view;
	}

	private void initView() {
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("应急车审核历史详情");
		back = (ImageView) view.findViewById(R.id.back);

		application = (TextView) view.findViewById(R.id.application);
		location = (TextView) view.findViewById(R.id.location);
		date = (TextView) view.findViewById(R.id.time);
		application_resaon = (TextView) view
				.findViewById(R.id.application_reason);
		result = (TextView) view.findViewById(R.id.result);
		backButton = (TextView) view.findViewById(R.id.backButton);
	}

	private void DisplayContent() {
		application.setText(checkInfo.getApply_user());
		location.setText(checkInfo.getApply_address());
		date.setText(checkInfo.getApply_time());
		application_resaon.setText(checkInfo.getApply_reason());
		result.setText(checkInfo.getResult());

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
	}

	private void getData() {
		id = getArguments().getString("id");
		handler.post(new Runnable() {

			@Override
			public void run() {
				Map param = new HashMap();
				param.put("id", id);
				HttpUtil.getClient()
						.get(HttpUtil
								.getURL("api/emergencyCarManage/emergencyCheckHistorys"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										try {
											JSONArray jsonArr = new JSONArray(
													response);
											JSONObject jsonObj = jsonArr
													.getJSONObject(0);
											checkInfo = getEntity(jsonObj);
											DisplayContent();
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

	public CheckInfo getEntity(JSONObject joooo) {
		CheckInfo checkInfo = new CheckInfo();
		try {
			checkInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
			checkInfo.setId("暂无数据");
		}
		try {
			checkInfo.setApply_user(joooo.getString("name"));
		} catch (Exception e) {
			checkInfo.setState("暂无数据");
		}
		try {
			checkInfo.setApply_reason(joooo.getString("applyReason"));
		} catch (Exception e) {
			checkInfo.setApply_reason("暂无数据");
		}
		try {
			checkInfo.setApply_time(joooo.getString("applyTime"));
		} catch (Exception e) {
			checkInfo.setApply_time("暂无数据");
		}
		try {
			checkInfo.setApply_address(joooo.getString("applyAddress"));
		} catch (Exception e) {
			checkInfo.setApply_address("暂无数据");
		}
		try {
			String result = joooo.getString("state");
			if (result.equals("2"))
				checkInfo.setResult("同意");
			if (result.equals("3"))
				checkInfo.setResult("不同意");
		} catch (Exception e) {
			checkInfo.setResult("暂无数据");
		}
		// 已读和未读
		return checkInfo;
	}
}
