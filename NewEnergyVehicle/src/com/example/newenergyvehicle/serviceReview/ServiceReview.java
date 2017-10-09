package com.example.newenergyvehicle.serviceReview;

import java.net.URL;
import java.util.LinkedList;

import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.failureReport.PhotoAdapter;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.VehicleMapInfo;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.newenergyvehicle.successPage.ISuccessPageL.ISuccessListener;
import com.example.newenergyvehicle.vehicle.VehicleMain;
import com.example.newenergyvehicle.vehicleInfo.VehicleInfoActivity;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceReview extends Fragment {
	private View view;
	private TextView module_title;
	private Context context;
	private ImageView back;

	private ImageView pentagram_one;
	private ImageView pentagram_two;
	private ImageView pentagram_three;
	private ImageView pentagram_four;
	private ImageView pentagram_five;
	private ImageView[] picture_pentagram;
	private String[] evaluations = new String[5];
	private TextView evaluation;
	private EditText evaluation_content;
	private TextView evaluation_source;
	private TextView Service_review_submission;
	private String evaluated_id = "新能源瑞康";
	private String user_id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.service_review, null);

		context = inflater.getContext();
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("服务评价");

		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).changeView(new VehicleMain());
			}
		});
		picture_pentagram = new ImageView[5];
		pentagram_one = (ImageView) view.findViewById(R.id.pentagram_one);
		picture_pentagram[0] = pentagram_one;
		pentagram_two = (ImageView) view.findViewById(R.id.pentagram_two);
		picture_pentagram[1] = pentagram_two;
		pentagram_three = (ImageView) view.findViewById(R.id.pentagram_three);
		picture_pentagram[2] = pentagram_three;
		pentagram_four = (ImageView) view.findViewById(R.id.pentagram_four);
		picture_pentagram[3] = pentagram_four;
		pentagram_five = (ImageView) view.findViewById(R.id.pentagram_five);
		picture_pentagram[4] = pentagram_five;
		evaluations[0] = "非常差";
		evaluations[1] = "差";
		evaluations[2] = "一般";
		evaluations[3] = "好";
		evaluations[4] = "非常好";
		evaluation = (TextView) view.findViewById(R.id.evaluation);
		evaluation_content = (EditText) view
				.findViewById(R.id.evaluation_content);
		evaluation_source = (TextView) view
				.findViewById(R.id.evaluation_source);
		Service_review_submission = (TextView) view
				.findViewById(R.id.Service_review_submission);

		pentagram_one.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				changePentagram(0);
				changeEvaluations(0);
				return false;
			}
		});
		pentagram_two.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				changePentagram(1);
				changeEvaluations(1);
				return false;
			}
		});
		pentagram_three.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				changePentagram(2);
				changeEvaluations(2);
				return false;
			}
		});
		pentagram_four.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				changePentagram(3);
				changeEvaluations(3);
				return false;
			}
		});
		pentagram_five.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				changePentagram(4);
				changeEvaluations(4);
				return false;
			}
		});
		Service_review_submission.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String content = evaluation_content.getText().toString();
						if (content== null || content.equals(""))
							Common.dialogMark(getActivity(), null, "信息未填写完整");
						else
							addData();
					}
				});
		init();
		return view;
	}

	public void init() {
		changePentagram(0);
		changeEvaluations(0);
		evaluation.setText("非常差");
	}

	public void addData() {

		RequestParams params = new RequestParams();

		String service_source = (String) evaluation_source.getText().toString();
		String content = evaluation_content.getText().toString();
		String service_type = evaluation.getText().toString();

		params.put("user_id", Login.userid);
		params.put("service_source", service_source);
		params.put("content", content);
		params.put("service_type", service_type);
		params.put("evaluated_id", evaluated_id);

		HttpUtil.getClient().post(
				HttpUtil.getURL("api/serviceEvaluation/serviceEvaluation"),
				params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {

						boolean statu = HttpUtil.isStatus(response);

						if (statu) {
							Bundle bundle =new Bundle();
							Fragment fa = new SuccessPage();
							bundle.putString("Page", "serviceEvaluation");
							bundle.putSerializable("backPage", "toVehicleMain");
							fa.setArguments(bundle);
							((DrawerLayoutMenu) context).changeView(fa);
						} else {
							Common.dialogMark(getActivity(), null, "请检查信息是否填写规范！");
						}

					}

					@Override
					public void onFailure(String responseBody, Throwable error) {
						super.onFailure(responseBody, error);
					}
				});

	}
	private void clearInput() {
		evaluation_content.setText("");
	}
	private void changePentagram(int num) {
		initPictureGrey();

		for (int i = 0; i <= num; i++) {
			picture_pentagram[i].setImageDrawable(getResources().getDrawable(
					R.drawable.pentagram_touch));
		}

	}

	private void changeEvaluations(int Evaluation) {
		evaluation.setText(evaluations[Evaluation]);
	}

	private void initPictureGrey() {
		for (ImageView view : picture_pentagram) {
			view.setImageDrawable(getResources().getDrawable(
					R.drawable.pentagram));
		}
	}
}
