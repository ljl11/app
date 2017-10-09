package com.example.newenergyvehicle.salesPersonnel;

import java.util.ArrayList;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FaultHandingHistoryEmergencyAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<FaultHandingEmergencyInfo> list;
	 
	public int getMax(){
		return list.size();
	}
	public FaultHandingHistoryEmergencyAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<FaultHandingEmergencyInfo>();
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
			convertView = inflater.inflate(R.layout.fault_handing_history_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			setItem(holder, position);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}

	private void setItem(ViewHolder holder, int position){
		FaultHandingEmergencyInfo faultHandingEmergencyInfo = list.get(position);
		int state = faultHandingEmergencyInfo.getState();
		holder.hour.setText(faultHandingEmergencyInfo.getHour());
		holder.year.setText(faultHandingEmergencyInfo.getYear());
		holder.next_detail_ft.setVisibility(View.INVISIBLE); 
		if(state == 0)
			holder.type.setText(list.get(position).getReleasePeople()+" 安排了应急车"+"\n"+"车牌号: " + list.get(position).getPlateNumber());
		else if (state == 1) {
			holder.type.setText(list.get(position).getReleasePeople()+" 回收了应急车"+"\n"+"车牌号: " + list.get(position).getPlateNumber());
		}
		
	}
	class ViewHolder {
		TextView hour;
		TextView year;
		TextView type;
		LinearLayout next_detail_ft;
		public ViewHolder(View view){
			hour = (TextView) view.findViewById(R.id.fault_hour);
			year = (TextView) view.findViewById(R.id.fault_year);
			type = (TextView) view.findViewById(R.id.fault_type);
			next_detail_ft = (LinearLayout)view.findViewById(R.id.next_detail_ft);

		}
	}
	
	public FaultHandingEmergencyInfo getEntity(JSONObject joooo) {
		FaultHandingEmergencyInfo faultHandingEmergencyInfo = new FaultHandingEmergencyInfo();
		try {
			faultHandingEmergencyInfo.setPlateNumber(joooo.getString("plateNumber"));
		} catch (JSONException e) {
			faultHandingEmergencyInfo.setPlateNumber("--");
		}
		
		try {
			faultHandingEmergencyInfo.setReleasePeople(joooo.getString("releasePeople"));
		} catch (JSONException e) {
			faultHandingEmergencyInfo.setReleasePeople("--");
		}
		try {
			faultHandingEmergencyInfo.setState(joooo.getInt("task_type"));
		} catch (JSONException e) {
			faultHandingEmergencyInfo.setState(-1);
		}
		
		
		try {
			String[] times = joooo.getString("taskAddTime").split(" ");
			faultHandingEmergencyInfo.setYear(times[0]);
			faultHandingEmergencyInfo.setHour(times[1]);
		} catch (JSONException e) {
			faultHandingEmergencyInfo.setYear("--");
			faultHandingEmergencyInfo.setHour("--");
		}
		return faultHandingEmergencyInfo;
	}
	
	public void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
	}
}
