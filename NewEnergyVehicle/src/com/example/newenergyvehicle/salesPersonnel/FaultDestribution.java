package com.example.newenergyvehicle.salesPersonnel;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.buttonRepeatClick.NoFastClickUtils;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.DialogInterface;
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

public class FaultDestribution extends Fragment implements OnClickListener{

	private View view;
	private Context context;
	private TextView headContent;
	private TextView sureLead;
	private ImageView back;
	private EditText faultDescription;
	private String faultRecordId;
	private String faultDistributionUserId;
	private Handler handler = new Handler();
	private String type ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fault_destribution, null);
		context = inflater.getContext();

		initView();

		return view;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			((DrawerLayoutMenu) context).back();
			break;
		case R.id.change_destribution: {
			if(NoFastClickUtils.isFastClick()) {
				Common.dialogMark(getActivity(), null, "请勿重复修改");
			}
			else if(sureLead.getText().toString().equals("转交售后")){
				String message = "确认将该故障转回售后？";
				showNormalDialog(message);
			}
			else
				sureSend();
		}
		
			break;
		}
	}
	
	private void showNormalDialog(String content) {
		Common.dialog(getActivity(), null, content, "取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}, "确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						getDistributionUserId();
							
					}

				}, null, null);
	}
	
	private void sureSend() {

		String faultDescribe = faultDescription.getText().toString();
		if (faultDescribe != "") {
			RequestParams params = new RequestParams();
			params.put("faultRecordId", faultRecordId);
			params.put("faultDescribe", faultDescribe);
			HttpUtil.getClient().post(
					HttpUtil.getURL("api/faultHandling/changeFaultDescribe"), params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject response) {
							try {
								String isSuccess = response.getString("status");
								if (isSuccess.equals("true")) {
									((DrawerLayoutMenu) context).back();
									Common.dialogMark(getActivity(), null,
											"修改成功！");
								} else {
									Common.dialogMark(getActivity(), null,
											"请求失败！");
								}
							} catch (JSONException e) {
								Common.dialogMark(getActivity(), null, "请求错误！");
							}
						}

						@Override
						public void onFailure(String responseBody,
								Throwable error) {
							super.onFailure(responseBody, error);
							Common.dialogMark(getActivity(), null, "网络异常");
						}
					});
		} else {
			Toast.makeText(context, "请输入故障描述", Toast.LENGTH_SHORT).show();
		}
	}
	
	//转交售后
	private void sureAssign(final String recipientedId) {
		handler.postDelayed(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams();
				params.put("faultRecordId", faultRecordId);
				params.put("recipientId", recipientedId);
				params.put("acceptType", "3");// 表示转给售后人员
				params.put("distributionDescription", faultDescription.getText().toString());
				HttpUtil.getClient().post(
						HttpUtil.getURL("api/faultRecord/fault/Distribution"),
						params, new JsonHttpResponseHandler() {

							@Override
							public void onSuccess(JSONObject response) {
								try {
									String isSuccess = response
											.getString("status");
									if (isSuccess.equals("true")) {
										Toast.makeText(context, "转交成功",
												Toast.LENGTH_SHORT).show();
										((DrawerLayoutMenu) context)
												.changeView(new MyWork());
									} else {
										Common.dialogMark(getActivity(), null,
												"请求失败！");
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
								Common.dialogMark(getActivity(), null, "网络异常");
							}
						});
			}
		}, 100);

	}
	
	//获取故障分配人id
		private void getDistributionUserId(){
			handler.postDelayed(new Runnable() {
				public void run() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("faultRecordId", faultRecordId);
					HttpUtil.getClient().get(
							HttpUtil.getURL("api/faultHandling/getFaultDistributionPerson" + ParamUtil.mapToString(params)),
							new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(String response) {
									try {
										faultDistributionUserId = new JSONObject(response).getString("distribution_id");	
										sureAssign(faultDistributionUserId);
									} catch (Exception e) {
										Common.dialogMark(getActivity(), null,
												"请求错误！");
									}
								}

								@Override
								public void onFailure(Throwable error) {
									super.onFailure(error);
								}
							});
				}
			}, 100);
		}

	private void initView() {
		sureLead = (TextView) view.findViewById(R.id.change_destribution);
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("故障描述");
		back = (ImageView) view.findViewById(R.id.back);
		faultDescription = (EditText) view
				.findViewById(R.id.display_fault_description);
		if(getArguments() != null){
			faultRecordId = getArguments().getString("id");
//			faultDescription.setText(getArguments().getString("fd"));
			type = getArguments().getString("type");
		}
		if(type!=null&&type.equals("0")){
			faultDescription.setText(getArguments().getString("fd"));
		}else if(type!=null&&type.equals("1")){
			sureLead.setText("转交售后");
		}
		back.setOnClickListener(this);
		sureLead.setOnClickListener(this);
	}
}
