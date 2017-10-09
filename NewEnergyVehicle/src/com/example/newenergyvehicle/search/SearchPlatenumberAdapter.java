package com.example.newenergyvehicle.search;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.emergencyVehicle.AssignSave;
import com.example.newenergyvehicle.emergencyVehicle.CarChooseInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.map.MapActivity;
import com.example.newenergyvehicle.salesPersonnel.CarAssignment;
import com.example.newenergyvehicle.salesPersonnel.LeadCar;
import com.example.newenergyvehicle.salesPersonnel.VehiclePickUp;
import com.example.newenergyvehicle.salesPersonnel.VehicleSend;

public class SearchPlatenumberAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<SearchInfo> list;
	private String type;
	private CarChooseInfo carChoose;

	public int getMax() {
		return list.size();
	}

	public SearchPlatenumberAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<SearchInfo>();
	}

	public synchronized void resetData(JSONArray data) {
		if (data != null) {
			int length = data.length();
			for (int i = 0; i < length; i++) {
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
			convertView = inflater.inflate(R.layout.search_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setItem(holder, position);
		return convertView;
	}

	private void setItem(ViewHolder holder, final int position) {
		final SearchInfo faultHistoryInfo = list.get(position);
		holder.name.setText(faultHistoryInfo.getName());
		holder.name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("info", faultHistoryInfo);
				if (type == "1") {
					VehiclePickUp ca = new VehiclePickUp();
					ca.setArguments(bundle);
					((DrawerLayoutMenu) context).changeView(ca);
				} else if (type == "0") {
					VehicleSend send = new VehicleSend();
					send.setArguments(bundle);
					((DrawerLayoutMenu) context).changeView(send);
				} else if (type == "2") {
					LeadCar lc = new LeadCar();
					lc.setArguments(bundle);
					((DrawerLayoutMenu) context).changeView(lc);
				}
				
				else if (type == "3") {
					toCarSave(position);
				}
			}
		});
	}

	class ViewHolder {
		TextView name;

		public ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.search_platenumber);
		}
	}

	public SearchInfo getEntity(JSONObject joooo) {
		SearchInfo faultHistoryInfo = new SearchInfo();
		try {
			faultHistoryInfo.setId(joooo.getString("id"));
		} catch (JSONException e) {
			try {
				faultHistoryInfo.setId(joooo.getString("vehicleId"));
			} catch (JSONException e1) {
				faultHistoryInfo.setId("暂无数据");
			}
			}
			
			
		try {
			faultHistoryInfo.setName(joooo.getString("plateNumber"));
		} catch (JSONException e) {
			try {
				faultHistoryInfo.setName(joooo.getString("plate_number"));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			faultHistoryInfo.setName("暂无数据");
		}
		
		try {
			faultHistoryInfo.setVin(joooo.getString("vin"));
		} catch (JSONException e) {
			faultHistoryInfo.setVin("暂无数据");
		}
		
		try {
			faultHistoryInfo.setTypeName(joooo.getString("typeName"));
		} catch (JSONException e) {
			faultHistoryInfo.setTypeName("暂无数据");
		}
		
		try {
			faultHistoryInfo.setHolderName(joooo.getString("storeName"));
		} catch (JSONException e) {
			faultHistoryInfo.setHolderName("暂无数据");
		}
		
		try {
			faultHistoryInfo.setStoreLocation(joooo.getString("storeLocation"));
		} catch (JSONException e) {
			faultHistoryInfo.setStoreLocation("暂无数据");
		}
		return faultHistoryInfo;
	}

	public void resetSingleData(JSONArray data, String type) {
		this.type = type;
		list.clear();
		resetData(data);
	}
	
	public void toCarMap(int position){
		Bundle bundle = new Bundle();
		bundle.putString("vin", list.get(position).getVin());
		Fragment fragment = new MapActivity();
		fragment.setArguments(bundle);
		((DrawerLayoutMenu)context).changeView(fragment);
	}
	
	public void toCarSave(int position){
		Bundle bundle = new Bundle();
		CarChooseInfo car =getParameter();
		car.setPlateNumber(list.get(position).getName());
		car.setTypeName(list.get(position).getTypeName());
		car.setVehicleId(list.get(position).getId());
		car.setHoldernName(list.get(position).getHolderName());
		car.setAssignLocation(list.get(position).getStoreLocation());
		bundle.putSerializable("car",car);
		Fragment fragment = new AssignSave();
		fragment.setArguments(bundle);
		((DrawerLayoutMenu)context).changeView(fragment);
	}
	public void setParameter(CarChooseInfo info){
		this.carChoose= info;
	}
	
	public CarChooseInfo getParameter(){
		return carChoose;
	}
}
