package com.example.newenergyvehicle.vehicleInfo;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.faultHand.FaultHandHistoryDetail;
import com.example.newenergyvehicle.faultHand.FaultHandHistoryItemInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MaintenanceReminderAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<MaintenanceReminderInfo> list;
	private int statue = -1;

	public MaintenanceReminderAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<MaintenanceReminderInfo>();
	}

	public void resetData(JSONArray data) {
		if (data != null) {
			int length = data.length();
			for (int i = 0; i < length; i++) {
				try {
					list.add(getEntity(data.getJSONObject(i)));
				} catch (JSONException e) {
					e.printStackTrace();
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
			convertView = inflater.inflate(R.layout.vehicle_maintenance_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		setItem(holder, position);
		
		return convertView;
	}
	@SuppressLint("ResourceAsColor")
	private void setItem(ViewHolder holder, int position) {
		final MaintenanceReminderInfo maintenanceReminderInfo = list.get(position);
		final int index = position;
		int state = maintenanceReminderInfo.getMaintenanceState();
		
		
	    if (state == 0){
			holder.vehicleMainenancePicture.setImageResource(R.drawable.overdue_item);
			holder.itemName.setText(maintenanceReminderInfo.getItemName());
			holder.lastMaintenanceTime.setText(maintenanceReminderInfo.getLastMaintainDate());
		}
		else {
			holder.vehicleMainenancePicture.setImageResource(R.drawable.not_overdue_item);
			holder.itemName.setText(maintenanceReminderInfo.getItemName());
			holder.lastMaintenanceTime.setText(maintenanceReminderInfo.getLastMaintainDate());
			
		}
	    
/*		holder.vehicleMaintenancePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("illegalRecordDetail", list.get(index));
				Fragment fh = new FaultHandHistoryDetail();
				fh.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fh);
			}
		});*/
	}

	class ViewHolder {
		TextView itemName;
		TextView lastMaintenanceTime;
		ImageView vehicleMainenancePicture;
		LinearLayout vehicleMaintenancePage;

		public ViewHolder(View view) {
			itemName = (TextView) view.findViewById(R.id.itemName);
			lastMaintenanceTime = (TextView) view.findViewById(R.id.lastMaintenanceTime);
			vehicleMaintenancePage = (LinearLayout) view.findViewById(R.id.vehicleMaintenancePage);
			vehicleMainenancePicture = (ImageView) view.findViewById(R.id.vehicleMainenancePicture);
		}
	}
	
	public MaintenanceReminderInfo getEntity(JSONObject joooo) {
		MaintenanceReminderInfo maintenanceReminderInfo = new MaintenanceReminderInfo();
		try {
			maintenanceReminderInfo.setItemName(joooo.getString("itemName"));
		} catch (Exception e) {
			maintenanceReminderInfo.setItemName("--");
		}
		try {
			maintenanceReminderInfo.setCurMileage(joooo.getString("curMileage"));
		} catch (Exception e) {
			maintenanceReminderInfo.setCurMileage("--");
		}
		try {
			maintenanceReminderInfo.setLastMaintainDate(joooo.getString("lastMaintainDate"));
		} catch (Exception e) {
			maintenanceReminderInfo.setLastMaintainDate("--");
		}
		try {
			maintenanceReminderInfo.setLastMaintainMileage(joooo.getString("lastMaintainMileage"));
		} catch (Exception e) {
			maintenanceReminderInfo.setLastMaintainMileage("--");
		}
		try {
			maintenanceReminderInfo.setMaintenanceState(joooo.getInt("maintenanceState"));
		} catch (Exception e) {
			maintenanceReminderInfo.setMaintenanceState(-1);
		}
		try {
			maintenanceReminderInfo.setMileageInterval(joooo.getString("mileageInterval"));
		} catch (Exception e) {
			maintenanceReminderInfo.setMileageInterval("--");
		}
		try {
			maintenanceReminderInfo.setOutTime(joooo.getString("outTime"));
		} catch (Exception e) {
			maintenanceReminderInfo.setOutTime("--");
		}
		try {
			maintenanceReminderInfo.setTimeInterval(joooo.getString("timeInterval"));
		} catch (Exception e) {
			maintenanceReminderInfo.setOutTime("--");
		}

		return maintenanceReminderInfo;
	}

	public void resetSingleData(JSONArray data) {
		list.clear();
		resetData(data);
	}

	public static String minToCaleander(long now) {
		DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);

		return formatter.format(calendar.getTime());
	}

	public void setStatue(int statue) {
		this.statue = statue;
		notifyDataSetChanged();
	}
}


