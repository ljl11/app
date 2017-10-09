package com.example.newenergyvehicle.myBill;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.vehicleInfo.VehicleInfo;
import com.example.newenergyvehicle.vehicleInfo.Vehicle_carlist;

public class BillDetailVehicleAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<MyBill> list;
	private String unitPrice;
	
	public BillDetailVehicleAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<MyBill>();
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
	
	public void setItem(ViewHolder holder, final int position){
		final MyBill myBill = list.get(position);
		holder.models.setText(myBill.getModels());
		holder.plateNumber.setText(myBill.getPlateNumber());
		holder.vehicleFree.setText(myBill.getVehicleFree());
		holder.vehicleItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Vehicle_carlist vehicleInfo = new Vehicle_carlist();
				vehicleInfo.setCarNum(myBill.getPlateNumber());
				vehicleInfo.setId(myBill.getPlateNo());
				Fragment fr = new VehicleInfo();
				Bundle bundle = new Bundle();
				bundle.putSerializable("vehicleInfo", vehicleInfo);
				fr.setArguments(bundle);
				changeView(fr);
			}
		});
	}
	
	private void changeView(Fragment fragment) {
		((DrawerLayoutMenu) context).changeView(fragment);
	}
	
	public void resetSingleData(JSONArray data,String unitPrice) {
		list.clear();
		this.unitPrice = unitPrice;
		resetData(data);
	}
	
	public void resetData(JSONArray data) {
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
	
	public MyBill getEntity(JSONObject joooo){
		MyBill myBill = new MyBill();
		try {
			myBill.setPlateNo(joooo.getString("id"));
		} catch (JSONException e) {
			myBill.setPlateNo("--");
			e.printStackTrace();
		}
		try {
			myBill.setPlateNumber(joooo.getString("plate_number"));
		} catch (JSONException e) {
			myBill.setPlateNumber("--");
			e.printStackTrace();
		}
			myBill.setVehicleFree(unitPrice);
		try {
			myBill.setModels(joooo.getString("typeName"));
		} catch (JSONException e) {
			myBill.setModels("--");
			e.printStackTrace();
		}
		return myBill;
	}
	
	class ViewHolder{
		TextView plateNumber;
		TextView models;
		TextView vehicleFree;
		LinearLayout vehicleItem;
		public ViewHolder(View view) {
			plateNumber = (TextView)view.findViewById(R.id.bd_plate_number);
			models = (TextView)view.findViewById(R.id.bd_models);
			vehicleFree = (TextView)view.findViewById(R.id.vehicle_fee);
			vehicleItem = (LinearLayout)view.findViewById(R.id.bd_vehicle_list_item);
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

			convertView = inflater.inflate(R.layout.bill_detail_vehicle_item, null);
			holder = new ViewHolder(convertView);
			setItem(holder, position);
			convertView.setTag(holder);
		return convertView;
	}

}
