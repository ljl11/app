package com.example.newenergyvehicle.salesPersonnel;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.failureReport.CameraActivy;
import com.example.newenergyvehicle.failureReport.ImageViewPaper;
import com.example.newenergyvehicle.failureReport.PhotoAdapter;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.imageloader.PhotoActivity;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.buttonRepeatClick.NoFastClickUtils;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FaultHandlingResults extends Fragment implements OnClickListener {
	private View view;
	private Context context;
	private TextView headContent;
	private TextView sureLead;
	private ImageView back;
	private EditText faultDescription;
	private View headerView;
	private String id;
	private String applicantId;
	private String leaseUserId;
	private String plateNum;
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.input_process_result, null);
		context = inflater.getContext();

		initView();

		back.setOnClickListener(this);
		sureLead.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			((DrawerLayoutMenu) context).back();
			break;
		case R.id.processed:
			if(!faultDescription.getText().toString().equals("")){
				if(NoFastClickUtils.isFastClick()) {
					Common.dialogMark(getActivity(), null, "请勿重复处理");
				}
				else
					sureSend();
			}else{
				Common.dialogMark(getActivity(), null, "请输入处理结果描述");
			}
			break;
		}
	}

	public void sureSend() {

		String handlingMethod = faultDescription.getText().toString();
		if(handlingMethod == null || handlingMethod.equals("")) {
			Toast.makeText(context, "请输入故障描述", Toast.LENGTH_SHORT).show();
			return ;
		}
		else {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String dataStr = formatter.format(curDate);
			String content = "您的车辆:" + plateNum + ",于"
					+ dataStr + "已维修完毕！";
			String receiverIds = applicantId+","+leaseUserId;
			RequestParams params = new RequestParams();
			params.put("title", "车辆维修");
			params.put("content", content);
		    params.put("noticeType", "0000000100000001");
			params.put("receiverIds", receiverIds);
			params.put("faultRecordId", id);
			if (Login.operatorCode != null && !"".equals(Login.operatorCode)) {
				params.put("isManage", "1");
			}
			params.put("handlingMethod", handlingMethod);
			
			HttpUtil.getClient().post(
					HttpUtil.getURL("api/faultHandling/faultHandling"), params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject response) {
							try {
								String isSuccess = response.getString("status");
								if (isSuccess.equals("true")) {
									((DrawerLayoutMenu) context)
											.changeView(new MyWork());
									sendNotice();
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
		} 
	}

	private void initView() {
		sureLead = (TextView) view.findViewById(R.id.processed);
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("故障处理结果");
		back = (ImageView) view.findViewById(R.id.back);
		faultDescription = (EditText) view
				.findViewById(R.id.input_fault_description);
		headerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.item_home_header, null);
		if (getArguments() != null) {
			id = getArguments().getString("id");
			applicantId = getArguments().getString("applicantId");
			leaseUserId = getArguments().getString("leaseUserId");
			plateNum = getArguments().getString("plateNum");
		}
	}
	
	private void sendNotice() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String dataStr = formatter.format(curDate);
				String content = "您的车辆:" + plateNum + ",于"
						+ dataStr + "已维修完毕！";
				String receiverIds = applicantId+","+leaseUserId;
				RequestParams params = new RequestParams();
				params.put("title", "车辆维修");
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
