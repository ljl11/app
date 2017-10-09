package com.example.newenergyvehicle.emergencyVehicle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.NotificationInfo;

public class CarChooseAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<CarChooseInfo> list;
	private Map<String,Object> user;
	
	public int getMax(){
		return list.size();
	}
	public CarChooseAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<CarChooseInfo>();
	}
	
	public synchronized void resetData(JSONArray data){
		if(data!=null) {
			int length = data.length();
			for(int i = 0;i < length;i++) {
				try {
					list.add(getEntity(data.getJSONObject(i)));
				} catch (JSONException e) {
				}
			}
		}
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final int index = position;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.car_choose_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		setItem(holder, position);
		return convertView;
	}

	private void setItem(ViewHolder holder, final int position){
		//赋值
		holder.plateNum.setText(list.get(position).getPlateNumber());
		holder.type.setText(list.get(position).getTypeName());
		
		holder.linear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				 if(user.get("userId") !=null)
					 list.get(position).setAssignObject(user.get("userId").toString());
				 if(user.get("userName") !=null)
				     list.get(position).setAssignObjectName(user.get("userName").toString());
				bundle.putString("type", user.get("type").toString());
				bundle.putSerializable("assignSave", list.get(position));
				Fragment fragment = new AssignSave();
				fragment.setArguments(bundle);
				((DrawerLayoutMenu)context).changeView(fragment);
			}
		});
	}
	class ViewHolder {
		TextView plateNum;
		TextView state;
		TextView type;
		LinearLayout linear;
		public ViewHolder(View view){
			plateNum = (TextView) view.findViewById(R.id.car_choose_plate_number);
			type = (TextView) view.findViewById(R.id.car_choose_typeName);
			
			linear = (LinearLayout) view.findViewById(R.id.car_choose_linear); //点击查看详情
		}
	}
	
	public CarChooseInfo getEntity(JSONObject joooo) {
		CarChooseInfo carChooseInfo = new CarChooseInfo();
		try{
			carChooseInfo.setVehicleId(joooo.getString("id"));
		}catch (Exception e) {
			carChooseInfo.setVehicleId("暂无数据");
		}
		
		try{
			carChooseInfo.setTypeName(joooo.getString("typeName"));
		}catch (Exception e) {
			carChooseInfo.setTypeName("暂无数据");
		}
		
		try{
			carChooseInfo.setPlateNumber(joooo.getString("plateNumber"));
		}catch (Exception e) {
			carChooseInfo.setPlateNumber("暂无数据");
		}
		
		try{
			carChooseInfo.setHoldernName(joooo.getString("storeName"));
		}catch (Exception e) {
			carChooseInfo.setHoldernName("暂无数据");
		}
		
		try{
			carChooseInfo.setAssignLocation(joooo.getString("storeLocation"));
		}catch (Exception e) {
			carChooseInfo.setAssignLocation("暂无数据");
		}

		return carChooseInfo;
	}
	
	public  void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
	}
	
	public static String minToCaleander(long now){
		DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);

		return formatter.format(calendar.getTime());
	}
	
	public void setUser(Map map){
		this.user =map;
	}
	
}
