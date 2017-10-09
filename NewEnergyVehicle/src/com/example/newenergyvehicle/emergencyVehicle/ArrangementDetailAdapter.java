package com.example.newenergyvehicle.emergencyVehicle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.emergencyVehicle.ArrangementListAdapter.ViewHolder;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.personal.Personal;

public class ArrangementDetailAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<ArrangementDetailInfo> list;
	
	public int getMax(){
		return list.size();
	}
	public ArrangementDetailAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<ArrangementDetailInfo>();
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
			convertView = inflater.inflate(R.layout.arrangement_detail_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		setItem(holder, position);
		return convertView;
	}

	private void setItem(ViewHolder holder, int position){
		holder.plateNumber.setText(list.get(position).getPlateNumber());
		holder.userName.setText(list.get(position).getUserName());
		holder.taskTime.setText(list.get(position).getTaskTime());
		holder.fullDate.setText(list.get(position).getFullDate());
		holder.serverName.setText(list.get(position).getServerName());
		holder.taskPlace.setText(list.get(position).getTaskPlace());
	}
	class ViewHolder {
		TextView plateNumber;
		TextView userName;
		TextView taskTime;
		TextView fullDate;
		TextView serverName;
		TextView taskPlace;
		public ViewHolder(View view){
			plateNumber = (TextView) view.findViewById(R.id.plate_number);
			userName = (TextView) view.findViewById(R.id.userName);
			taskTime = (TextView) view.findViewById(R.id.task_time);
			fullDate = (TextView) view.findViewById(R.id.full_date);
			serverName = (TextView) view.findViewById(R.id.server_name);
			taskPlace = (TextView) view.findViewById(R.id.task_place);
		}
	}
	
	public ArrangementDetailInfo getEntity(JSONObject joooo) {
		ArrangementDetailInfo arrangementDetailInfo = new ArrangementDetailInfo();
		
		try{
			arrangementDetailInfo.setId(joooo.getString("id"));
		}catch (Exception e) {
			arrangementDetailInfo.setId("暂无数据");
		}
		
		try{
			arrangementDetailInfo.setPlateNumber(joooo.getString("plateNumber"));
		}catch (Exception e) {
			arrangementDetailInfo.setPlateNumber("暂无数据");
		}
		
		try{
			arrangementDetailInfo.setUserName(joooo.getString("userName"));
		}catch (Exception e) {
			arrangementDetailInfo.setUserName("暂无数据");
		}
		
		try{
			arrangementDetailInfo.setTaskTime(joooo.getString("taskTime"));
		}catch (Exception e) {
			arrangementDetailInfo.setTaskTime("- -");
		}

		try{
			arrangementDetailInfo.setFullDate(joooo.getString("fullDate"));
		}catch (Exception e) {
			arrangementDetailInfo.setFullDate("- -");
		}
		
		try{
			arrangementDetailInfo.setTaskPlace(joooo.getString("taskPlace"));
		}catch (Exception e) {
			arrangementDetailInfo.setTaskPlace("暂无数据");
		}
		
		try{
			arrangementDetailInfo.setServerName(joooo.getString("serverName"));
		}catch (Exception e) {
			arrangementDetailInfo.setServerName("暂无数据");
		}
	
		return arrangementDetailInfo;
	}
	
	public void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
	}
	
	public static String minToCaleander(long now){
		DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);

		return formatter.format(calendar.getTime());
	}
}
