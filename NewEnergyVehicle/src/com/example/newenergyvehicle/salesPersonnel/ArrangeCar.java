package com.example.newenergyvehicle.salesPersonnel;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.dialog.ChooseStation;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.buttonRepeatClick.NoFastClickUtils;
import com.example.util.timeDialog.DateDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class ArrangeCar extends Fragment {
	private View view;
	private Context context;
	private Handler handler = new Handler();
	private TextView headContent;
	private TextView sureSendCar;
	private TextView deliveryTime;
	private EditText deliveryPlace;
	private EditText applicationReason;
	private DatePickerDialog dateDialog;
	private Dialog applicantNameDialog;
	private ImageView back;
	private String faultRecordId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.arrange_car, null);
		context = inflater.getContext();
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("应急车申请");
		sureSendCar = (TextView) view.findViewById(R.id.sure_send_car);
		deliveryTime = (TextView) view.findViewById(R.id.delivery_time);
		deliveryPlace = (EditText) view.findViewById(R.id.delivery_place);
		applicationReason = (EditText) view
				.findViewById(R.id.application_reason);
		back = (ImageView) view.findViewById(R.id.back);
		applicantNameDialog = new ChooseStation(context);

		if (getArguments() != null) {
			faultRecordId = getArguments().getString("faultRecordId");
			if(getArguments().getString("isApply")!=null){
				Common.dialogMark(getActivity(),null, "应急车已申请");
				deliveryTime.setText(getArguments().getString("applyTime"));
				deliveryPlace.setText(getArguments().getString("applyPlace"));
				applicationReason.setText(getArguments().getString("applyReason"));
				sureSendCar.setVisibility(View.GONE);
			}
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		deliveryTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dateDialog.show();
			}
		});
		sureSendCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (NoFastClickUtils.isFastClick()) {
					Common.dialogMark(getActivity(), null, "您已提交申请，请勿重复提交");
				}
				else {
					if (deliveryTime.getText().toString() != ""
							&& deliveryPlace.getText().toString() != "") {
						handler.post(new Runnable() {
							public void run() {
								RequestParams params = new RequestParams();
								params.put("applyDate", deliveryTime.getText()
										.toString());
								params.put("reason", applicationReason.getText()
										.toString());
								params.put("applyAddress", deliveryPlace.getText()
										.toString());
								params.put("faultRecordId", faultRecordId);
								HttpUtil.getClient()
										.post(HttpUtil
												.getURL("api/emergencyCarManage/emergencyApply"),
												params,
												new AsyncHttpResponseHandler() {
													@Override
													public void onSuccess(
															String response) {
														Log.d("success", response);
														System.out.println(response
																.toString());
														((DrawerLayoutMenu) context)
																.changeView(new MyWork());
													}

													@Override
													public void onFailure(
															Throwable error) {
														super.onFailure(error);
														Common.dialogMark(
																getActivity(),
																null, "网络异常");
													}
												});
							}
						});

					} else {
						Common.dialogMark(getActivity(), null, "地点与时间是必填项！");
					}
				}
			}

		});

		dateDialog = DateDialog.getDateDialog(context);
		dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deliveryTime.setText(DateDialog.getTime());
					}
				});
		return view;
	}

}
