package com.example.newenergyvehicle.vehicleInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePageGroup;
import com.example.newenergyvehicle.homePage.NotificationGroup;
import com.example.newenergyvehicle.notification.NotificationInfo;
import com.example.util.photo.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VehicleInfoAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<Vehicle_carlist> list;
	public int getMax(){
		return list.size();
	}
	
	public VehicleInfoAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<Vehicle_carlist>();
	}
	
	class ViewHolder {
		private TextView carNum;
		private TextView mileage;
		private TextView electricity;
		private LinearLayout car_item;
		private LinearLayout carBg;
		private TextView carType;
		private ImageView emergencyCar;
		private ImageView carStateBg;
		private TextView carState;
		private ImageView carMaintanceBg;
		private TextView carMaintance;
		private ImageView carPaymentBg;
		private TextView carPayment;
		private ImageView illegleBg;
		private TextView carSpeed;
		private TextView illegle;
		private ImageView lock;
		private ImageView online;
		
		public ViewHolder(View view) {
			car_item = (LinearLayout) view.findViewById(R.id.car_item);
			carBg = (LinearLayout) view.findViewById(R.id.car_bg);
			carNum = (TextView) view.findViewById(R.id.car_num);
			emergencyCar = (ImageView) view.findViewById(R.id.emergency_car);
			mileage = (TextView) view.findViewById(R.id.car_mile);
			electricity = (TextView) view.findViewById(R.id.car_ele);
			carType = (TextView) view.findViewById(R.id.car_type);
			carStateBg = (ImageView) view.findViewById(R.id.car_state_bg);
			carState = (TextView) view.findViewById(R.id.car_state);
			carMaintanceBg = (ImageView) view
					.findViewById(R.id.car_maintance_bg);
			carMaintance = (TextView) view.findViewById(R.id.car_maintance);
			carPaymentBg = (ImageView) view.findViewById(R.id.payment_bg);
			carPayment = (TextView) view.findViewById(R.id.payment);
			illegleBg = (ImageView) view.findViewById(R.id.illegal_bg);
			illegle = (TextView) view.findViewById(R.id.illegal);
			lock = (ImageView) view.findViewById(R.id.car_lock);
			carSpeed = (TextView) view.findViewById(R.id.speed);
			online = (ImageView) view.findViewById(R.id.car_online); 
		}
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
	public View getView(final int position, View convertView, ViewGroup paren) {
		ViewHolder holder = null;
		final int index = position;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.vehicle_list, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setItem(holder, position);
		holder.car_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("vehicleInfo", list.get(position));
		        Fragment fragment = new VehicleInfo();
		        fragment.setArguments(bundle);
		        
		        ((DrawerLayoutMenu)context).changeView(fragment);
			}
		}); 
		return convertView;
	}
	
	private void setItem(ViewHolder holder, int position){
		holder.carNum.setText(list.get(position).getCarNum());
		
		String online =  list.get(position).getVonline();
		double mileage = Double.valueOf(list.get(position).getMileage()) / 10 ;
		holder.mileage.setText(mileage+"km");
		//int num = (int)(Math.random()*(9999-1000+1))+1000;
		holder.carSpeed.setText(list.get(position).getSpeed()+" km/h");
		
		if(online.equals("0"))
			holder.online.setImageResource(R.drawable.offline);
		else
			holder.online.setImageResource(R.drawable.online);
		if(list.get(position).getType().equals("1")){
				holder.emergencyCar.setImageResource(R.drawable.eme);
				holder.carType.setText("应急车辆");
				holder.carType.setTextColor(Color.parseColor("#e5ae30"));
		}
		else{
			holder.emergencyCar.setImageResource(R.drawable.plain_car);
			holder.carType.setText("普通车辆");
			holder.carType.setTextColor(Color.parseColor("#9f9f9f"));
		}
		try{
			int ele =Integer.valueOf(list.get(position).getElectricity());
			if(ele>50){
				holder.carBg.setBackgroundResource(R.drawable.ele_bg);
			}
			else if(ele < 50 && ele > 25){
				holder.carBg.setBackgroundResource(R.drawable.car_yellow_bg);
			}
			else {
				holder.carBg.setBackgroundResource(R.drawable.car_orange_bg);
			}
			
		}
		catch(Exception e){
			holder.electricity.setText(0 + "%电量");
		}
		holder.electricity.setText(list.get(position).getElectricity() + "%电量");
		
		if(Integer.valueOf(list.get(position).getRepairStatus())>0){
			holder.carStateBg.setBackgroundResource(R.drawable.warning);
			holder.carState.setText("维修中");
			holder.carState.setTextColor(Color.parseColor("#edab7f"));
		}
		else{
			holder.carStateBg.setBackgroundResource(R.drawable.pass);
			holder.carState.setText("无需维修");
			holder.carState.setTextColor(Color.parseColor("#9f9f9f"));
		}
		
		if(Integer.valueOf(list.get(position).getMaintenanceStatus())>0){
			holder.carMaintanceBg.setBackgroundResource(R.drawable.warning);
			holder.carMaintance.setText("需要保养");
			holder.carMaintance.setTextColor(Color.parseColor("#edab7f"));	
		}
		else{
			holder.carMaintanceBg.setBackgroundResource(R.drawable.pass);
			holder.carMaintance.setText("暂无保养");
			holder.carMaintance.setTextColor(Color.parseColor("#9f9f9f"));
		}
		
		if(Integer.valueOf(list.get(position).getPaymentStatus())>0){
			holder.carPaymentBg.setBackgroundResource(R.drawable.warning);
			holder.carPayment.setText("需要缴费");
			holder.carPayment.setTextColor(Color.parseColor("#edab7f"));	
		}
		else{
			holder.carPaymentBg.setBackgroundResource(R.drawable.pass);
			holder.carPayment.setText("无需缴费");
			holder.carPayment.setTextColor(Color.parseColor("#9f9f9f"));
		}
		
		if(Integer.valueOf(list.get(position).getViolationStatus()) > 0){
			holder.illegleBg.setBackgroundResource(R.drawable.warning);
			holder.illegle.setText("存在违章");
			holder.illegle.setTextColor(Color.parseColor("#edab7f"));	
		}
		else{
			holder.illegleBg.setBackgroundResource(R.drawable.pass);
			holder.illegle.setText("暂无违章");
			holder.illegle.setTextColor(Color.parseColor("#9f9f9f"));
		}
		
		if(Integer.valueOf(list.get(position).getLockState())>0){
			holder.lock.setVisibility(View.VISIBLE);
		}
		else{
			holder.lock.setVisibility(View.INVISIBLE);
		}
		
		
	}
	
	public Vehicle_carlist getEntity(JSONObject joooo) {
		Vehicle_carlist vehicleInfo = new Vehicle_carlist();
		try{
			vehicleInfo.setId(joooo.getString("vehicle_id"));
		}catch (Exception e) {
			try {
				vehicleInfo.setId(joooo.getString("vehicleId"));
			} catch (JSONException e1) {
				vehicleInfo.setId("--");
			}
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
		try{
			vehicleInfo.setEngine_no(joooo.getString("engine_no"));//发动机编号
		}catch (Exception e) {
			vehicleInfo.setEngine_no("--");
		}
		try{
			vehicleInfo.setVin(joooo.getString("sim"));
		}catch (Exception e) {
			vehicleInfo.setVin("--");
		}
		try{
			vehicleInfo.setCarType(joooo.getString("type_name"));
		}catch (Exception e) {
			try {
				vehicleInfo.setCarType(joooo.getString("typeName"));
			} catch (JSONException e1) {
				vehicleInfo.setCarType("--");
			}
		}
		try{
			vehicleInfo.setMaker(joooo.getString("manufacturer"));//生厂商
		}catch (Exception e) {
			vehicleInfo.setMaker("--");
		}
		try{
			vehicleInfo.setPossessor(joooo.getString("possessor"));//当前持有人
		}catch (Exception e) {
			vehicleInfo.setPossessor("--");
		}
		try{
			vehicleInfo.setType(joooo.getString("cur_state"));//状态
		}catch (Exception e) {
			vehicleInfo.setType("--");
		}
		try{
			vehicleInfo.setElectricity(joooo.getString("curElectricity"));//电量
		}catch (Exception e) {
			vehicleInfo.setElectricity("--");
		}
		try{
			vehicleInfo.setMileage(joooo.getString("curMileage"));//里程
		}catch (Exception e) {
			vehicleInfo.setMileage("0");
		}
		try{
			vehicleInfo.setType(joooo.getString("isEmergency"));//是否为应急车
		}catch (Exception e) {
			vehicleInfo.setType("--");
		}
		
		try{
			vehicleInfo.setRepairStatus(joooo.getString("repairStatus"));//是否在维修
		}catch (Exception e) {
			vehicleInfo.setRepairStatus("--");
		}
		
		try{
			vehicleInfo.setMaintenanceStatus(joooo.getString("maintenanceStatus"));//是否需保养
		}catch (Exception e) {
			vehicleInfo.setMaintenanceStatus("--");
		}
		try{
			vehicleInfo.setPaymentStatus(joooo.getString("paymentStatus"));//是否需交费
		}catch (Exception e) {
			vehicleInfo.setPaymentStatus("--");
		}
		try{
			vehicleInfo.setViolationStatus(joooo.getString("violationStatus"));//是否违章
		}catch (Exception e) {
			vehicleInfo.setViolationStatus("--");
		}
		
		try{
			vehicleInfo.setLockState(joooo.getString("lockStatus"));//是否锁车
		}catch (Exception e) {
			vehicleInfo.setLockState("--");
		}
		
		try{
			vehicleInfo.setSpeed(joooo.getString("curSpeed"));
		}catch (Exception e) {
			vehicleInfo.setSpeed("0");
		}
		
		try{
			vehicleInfo.setVonline(joooo.getString("vonline"));
		}catch (Exception e) {
			vehicleInfo.setVonline("0");
		}
		
		return vehicleInfo;
		
		
	}
	
	public void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
	}
}
