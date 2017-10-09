package com.example.newenergyvehicle.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePageGroup;
import com.example.newenergyvehicle.homePage.NotificationGroup;
import com.example.newenergyvehicle.notification.Notification;
import com.example.newenergyvehicle.notification.NotificationInfo;
import com.example.newenergyvehicle.vehicleInfo.Vehicle_carlist;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleMapAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<Vehicle_carlist> list;
	private Vehicle_carlist vehicleInfo;
	private String ak="fidYCXAUaUeIEARCYkh2A0cMWwUH0NDc";
	private String mcode="9D:57:A2:1F:D6:9B:1E:88:3D:09:7F:D5:0E:F3:AD:57:A7:81:3E:FC;com.example.newenergyvehicle";
	private Handler handler = new Handler();
	
	public VehicleMapAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<Vehicle_carlist>();
	}
	public void resetData(JSONArray data){
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
			convertView = inflater.inflate(R.layout.map_car_list, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.next = (LinearLayout) convertView.findViewById(R.id.next_page);
		holder.next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("vin", list.get(position).getVin());
				Fragment fragment = new MapActivity();
				fragment.setArguments(bundle);
				((DrawerLayoutMenu)context).changeView(fragment);
			}
		});
		setItem(holder, position);
		return convertView;
	}

	private void setItem(ViewHolder holder, int position){
		holder.car_num.setText(list.get(position).getCarNum());
		holder.location.setText(list.get(position).getPosition());
	}
	class ViewHolder {
		private TextView car_num;
		private TextView location;
		private LinearLayout next;
		public ViewHolder(View view) {
			car_num = (TextView) view.findViewById(R.id.plate_number);
			location = (TextView) view.findViewById(R.id.specific_location);
			next = (LinearLayout) view.findViewById(R.id.next_page);
		}
	}
	
	public Vehicle_carlist getEntity(JSONObject joooo) {
		vehicleInfo= new Vehicle_carlist();
		try{
			vehicleInfo.setId(joooo.getString("id"));
		}catch (Exception e) {
			vehicleInfo.setId("暂无数据");
		}
		try{
			vehicleInfo.setCarNum(joooo.getString("plate_number"));
		}catch (Exception e) {
			try {
				vehicleInfo.setCarNum(joooo.getString("plateNumber"));
			} catch (JSONException e1) {
				vehicleInfo.setCarNum("--");
			}
		}
		
		addPosition(joooo);
		
		return vehicleInfo;
	}
	
	public void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
	}
	
	public void addPosition(JSONObject joooo){
		try{
			String x = joooo.getString("cur_x_coordinate");
			String y = joooo.getString("cur_y_coordinate");
			if (x !=null &&  y !=null){
				Map param= new HashMap();
				param.put("coordtype","wgs84ll");
				param.put("location",y+","+x);
				param.put("output","json");
				param.put("pois","0");
				param.put("ak", ak);
				param.put("mcode", mcode);
				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://api.map.baidu.com/geocoder/v2/"+ParamUtil.mapToString(param),new AsyncHttpResponseHandler(){
					@Override
					public void onSuccess(String response){
						try{
							
							String r = response;
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
					@Override
					public void onFailure(Throwable error) {
						
						super.onFailure(error);
					}
				});
			}
		}
		catch(Exception e){
			vehicleInfo.setPosition("暂无");
		}
		
	}

}
