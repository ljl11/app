package com.example.newenergyvehicle.consultation;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.ConsultationGroup;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePageGroup;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.VehicleMapInfo;
import com.example.newenergyvehicle.vehicle.VehicleMain;
import com.example.newenergyvehicle.vehicleInfo.VehicleInfoActivity;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Consultation_askquestion extends Fragment implements
		OnClickListener {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private Handler handler = new Handler();
	private ImageView cancle;
	private TextView Publish;
	private EditText question;
	private EditText question_content;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.activity_main_ask_question, null);
		context = inflater.getContext();
		question_content = (EditText) view.findViewById(R.id.question_content);

		question = (EditText) view.findViewById(R.id.question);
		question.setFocusable(true);
		question.setFocusableInTouchMode(true);
		question.requestFocus();

		cancle = (ImageView) view.findViewById(R.id.cancle);
		cancle.setOnClickListener(this);
		Publish = (TextView) view.findViewById(R.id.publish);
		Publish.setOnClickListener(this);

		Common.clickView(cancle);
		Common.clickView(Publish);
		return view;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.cancle:
			((DrawerLayoutMenu) context).back();
			break;
		case R.id.publish:
			submit();
			break;
		default:
			((DrawerLayoutMenu) context).back();
			break;
		}
	}

	private void submit() {
		final String titleText = question.getText().toString();
		final String contentText = question_content.getText()
				.toString();
		if (titleText.length() == 0) {
			Common.dialogMark(getActivity(), null, "请输入标题");
		} else if (contentText.length() == 0) {
			Common.dialogMark(getActivity(), null, "请输入内容");
		} else {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Map param = new HashMap();
					param.put("postTitle",titleText);
					param.put("postContent", contentText);
					param.put("postState", 0);
					HttpUtil.getClient().post(
							HttpUtil.getURL("api/postAndReply/postAdd"
									+ ParamUtil.mapToString(param)),
							new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(String response) {
									Common.dialogMark(getActivity(), null, "发表成功");
									((DrawerLayoutMenu) context).back();
								}

								@Override
								public void onFailure(Throwable error) {
									Common.dialogMark(getActivity(), null, "发表失败");
									((DrawerLayoutMenu) context).back();
								}
							});
				}
			});
		}
	}
}
