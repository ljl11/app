package com.example.newenergyvehicle.faultHand;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.Notification;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.vehicleInfo.IllegalRecord;
import com.example.newenergyvehicle.vehicleInfo.IllegalRecordInfo;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class FaultHandHistoryDetail extends Fragment{
	private View view;
	private Context context;
	
	private TextView fault_history_time;
	private TextView fault_history_location;
	private TextView fault_history_type;
	private TextView fault_history_content;
	private TextView fault_history_car;
	private TextView userName;
	private TextView userPhone;
	private TextView theFirst;
	private TextView theSecond;
	private TextView theThrid;
	private TextView theFouth;
	private TextView theFifth;
	private TextView theSixth;
	private TextView fault_history_person;
	private TextView reporterName;
	private TextView reporterPhone;
	private LayoutInflater inflater;
	private FaultHandHistoryItemInfo faultHistoryInfo;
	private IllegalRecordInfo  illegalRecordInfo;
	private ImageView back;
	private TextView module_title;
	private LinearLayout fault_person;
	
	private Handler handler = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fault_history_detail, container, false);
		context = inflater.getContext();

		initView();
		
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		return view;
	}
	private void initView() {
		fault_history_time = (TextView) view.findViewById(R.id.fault_history_time);
		fault_history_content= (TextView) view.findViewById(R.id.fault_history_content);
		fault_history_location = (TextView) view.findViewById(R.id.fault_history_location);
		fault_history_type = (TextView) view.findViewById(R.id.fault_history_type);
		fault_history_car = (TextView) view.findViewById(R.id.fault_history_car);
		fault_history_person = (TextView) view.findViewById(R.id.fault_history_person);
		module_title = (TextView) view.findViewById(R.id.module_title);
		userName = (TextView) view.findViewById(R.id.userName);
		userPhone = (TextView) view.findViewById(R.id.userPhone);
		theFirst = (TextView) view.findViewById(R.id.theFirst);
		theSecond = (TextView) view.findViewById(R.id.theSecond);
		theThrid = (TextView) view.findViewById(R.id.theThrid);
		theFouth = (TextView) view.findViewById(R.id.theFouth);
		theFifth = (TextView) view.findViewById(R.id.theFifth);
		theSixth = (TextView) view.findViewById(R.id.theSixth);
		reporterName = (TextView) view.findViewById(R.id.fault_history_user_name);
		reporterPhone = (TextView) view.findViewById(R.id.fault_history_user_phone);
		fault_person = (LinearLayout) view.findViewById(R.id.fault_person);
		if(getArguments() != null){
			if(getArguments().containsKey("historyTroubleInfo")){
			module_title.setText("故障历史详情");
			faultHistoryInfo = (FaultHandHistoryItemInfo) getArguments().getSerializable("historyTroubleInfo");
			fault_history_content.setText(faultHistoryInfo.getHistory_content());
			fault_history_time.setText(faultHistoryInfo.getTime());
			fault_history_car.setText(faultHistoryInfo.getPlate_num());
			fault_history_type.setText(faultHistoryInfo.getHistory_type());
			fault_history_location.setText(faultHistoryInfo.getHistory_place());
			fault_history_person.setText(faultHistoryInfo.getName());
			reporterName.setText(faultHistoryInfo.getUserName());
			reporterPhone.setText(faultHistoryInfo.getUserPhone());
			} else if(getArguments().containsKey("id")){
				final String id = getArguments().getString("id");
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						HttpUtil.getClient().get(HttpUtil.url + "api/faultRecord/faultDetail?id=" + id, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							try{
								module_title.setText("故障历史详情");
								JSONObject json = new JSONObject(response).getJSONObject("fault");
								fault_history_car.setText(json.getString("plateNumber"));
								fault_history_time.setText(json.getString("time"));
								fault_history_location.setText(json.getString("location"));
								fault_history_type.setText(json.getString("faultType"));
								fault_history_content.setText(json.getString("faultDescription"));
								fault_history_person.setText(json.getString("name"));
								reporterName.setText(json.getString("reporterName"));
								reporterPhone.setText(json.getString("reporterPhone"));
							}catch (Exception e) {
								Toast.makeText(context, "信息加载有误", 1).show();
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
			else if (getArguments().containsKey("illegalRecordDetail")) {
				module_title = (TextView) view.findViewById(R.id.module_title);
				fault_person.setVisibility(View.GONE);
				view.findViewById(R.id.display_line).setVisibility(View.GONE);
				module_title.setText("车辆违章详情");
				theFirst.setText("违章时间");
				theSecond.setText("违章地点");
				theThrid.setText("违章金额");
				theFouth.setText("违章扣分");
				theFifth.setText("违章内容");
				userName.setText("违章人");
				userPhone.setText("联系电话");
				illegalRecordInfo = (IllegalRecordInfo) getArguments().getSerializable("illegalRecordDetail");
				reporterName.setText(illegalRecordInfo.getVehicleUserName());
				reporterPhone.setText(illegalRecordInfo.getVehicleUserPhone());
				fault_history_car.setText(illegalRecordInfo.getIllegalDate());
				fault_history_time.setText(illegalRecordInfo.getIllegalAddress());
				fault_history_location.setText(illegalRecordInfo.getIllegalMoney());
				fault_history_type.setText(illegalRecordInfo.getIllegalPoint());
				fault_history_content.setText(illegalRecordInfo.getIllegalAction());
			}
		}
		
	}
	
}

