package com.example.newenergyvehicle.emergencyVehicle;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.dialog.ChooseStation;
import com.example.newenergyvehicle.dialog.ChooseStationAdapter;
import com.example.newenergyvehicle.dialog.ChooseStationInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.notification.NotificationInfo;
import com.example.newenergyvehicle.search.SearchPlatenumber;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.String.StringUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class CarAssign extends Fragment implements DialogInfo{
	private View view;
	private Context context;
	private TextView module_title;
	private ImageView back;
	
	private ChooseStation dialog;
	private ChooseStationInfo info;
	ChooseStationAdapter adapter;
	private TextView assignObjectName;
	JSONArray employees;
	JSONArray warehouse;
	private String type ="0";
	private Handler handler = new Handler();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.car_assignment, container, false);
		context = inflater.getContext();
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("选择使用对象");
		view.findViewById(R.id.car_arrangement_save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!StringUtil.isNullOrEmpty(assignObjectName.getText().toString())){
					Bundle bundle = new Bundle();
					CarChooseInfo carChoose =new CarChooseInfo();
					carChoose.setAssignObject(info.getId());
					carChoose.setAssignObjectName(info.getInformation());
					carChoose.setType(type);
					bundle.putString("type", "3");
					bundle.putSerializable("carChooseInfo", carChoose);
					bundle.putString("url", "api/contract/availableCars");
					Fragment fragment = new SearchPlatenumber();
					fragment.setArguments(bundle);
					((DrawerLayoutMenu)context).changeView(fragment);
					}
				
				else{
					Toast.makeText(context, "请选择使用人或者是仓库", 1).show();
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
		
		assignObjectName = (TextView) view.findViewById(R.id.assign_object_name);
		 ((LinearLayout)view.findViewById(R.id.assign)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.setTitle("使用人");
				dialog.show();
			}
		});
		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);
		getEmployee(); //获取员工信息
		getWareHouse(); //获取仓库信息
		/*adapter.resetSingleData(toInfo(employees,"id","name"));*/
		adapter.notifyDataSetChanged();
		return view;
	}
	
	public void getEmployee(){
		final Map<String, String> params = new HashMap<String, String>();
		params.put("userId", Login.userId);
		handler.post(new Runnable() {
			@Override
			public void run() {
				HttpUtil.getClient().get(HttpUtil.getURL("api/staffManagement/searchStaff" + ParamUtil.mapToString(params)), new AsyncHttpResponseHandler() {
					 @Override
					 public void onSuccess(String response) {
						 try{
							 employees = new JSONArray(response);
							 adapter.resetSingleData(toInfo(employees,"user_id","name"));
							 setType("0");
							}catch (Exception e) {
									
								Common.dialogMark(getActivity(), null, "信息加载有误");
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
	
	public void getWareHouse(){
		handler.post(new Runnable() {
			@Override
			public void run() {
				HttpUtil.getClient().get(HttpUtil.getURL("api/warehouse/warehouses"), new AsyncHttpResponseHandler() {
					 @Override
					 public void onSuccess(String response) {
						 try{
							 warehouse = new JSONArray(response);
							}catch (Exception e) {
									
								Common.dialogMark(getActivity(), null, "信息加载有误");
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
	private JSONArray toInfo(JSONArray array,String filed1,String filed2){
		JSONArray info= new JSONArray();
		JSONObject json;
		for(int i=0;i<array.length();i++){
			json=new JSONObject();
			try {
				json.put("id", array.getJSONObject(i).getString(filed1));
			} catch (JSONException e) {
				Common.dialogMark(getActivity(), null, "信息加载有误");
			}
			try {
				json.put("information", array.getJSONObject(i).getString(filed2));
			} catch (JSONException e) {
				Common.dialogMark(getActivity(), null, "信息加载有误");
			}
			info.put(json);
		}
		
		return info;
	}
	
	@Override
	public void setInfo(ChooseStationInfo info) {
		this.info = info;
		assignObjectName.setText(info.getInformation());
	}
	
	public void setType(String type){
		this.type = type ;
	}
}
