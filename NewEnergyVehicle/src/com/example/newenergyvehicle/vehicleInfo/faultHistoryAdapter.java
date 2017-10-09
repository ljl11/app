package com.example.newenergyvehicle.vehicleInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.faultHand.NewFaultHistoryDetail;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.salesPersonnel.FaultHandlingHistoryDetail;

public class faultHistoryAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<FaultHistoryInfo> list;

	public int getMax() {
		return list.size();
	}

	public faultHistoryAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<FaultHistoryInfo>();
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
		convertView = inflater.inflate(R.layout.transfer_record_item, null);
		holder = new ViewHolder(convertView);
		convertView.setTag(holder);

		setItem(holder, position);
		return convertView;
	}

	@SuppressLint("ResourceAsColor")
	private void setItem(ViewHolder holder, final int position) {
		final FaultHistoryInfo faultHistoryInfo = list.get(position);
		holder.title.setText(faultHistoryInfo.getLocation());
		holder.date.setText(faultHistoryInfo.getFaulTime());
		String state = faultHistoryInfo.getState();
		if(state.equals("5")){
			holder.mainImg.setImageResource(R.drawable.ellipse);
			holder.contentfirst.setText("已修复");
			holder.contentfirst.setTextColor(R.color.text_color_b);
		}
		else{
			holder.mainImg.setImageResource(R.drawable.new_history);
			holder.contentfirst.setText("维修中");
			holder.contentfirst.setTextColor(R.color.text_color_yellow);
		}
		holder.record_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("id", faultHistoryInfo.getId());
				Fragment fh = new NewFaultHistoryDetail();
				fh.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fh);
			}
		});
	}

	class ViewHolder {
		TextView title;
		TextView date;
		TextView contentfirst;
		TextView contentsecond;
		ImageView mainImg;
		LinearLayout record_item;

		public ViewHolder(View view) {
			title = (TextView) view.findViewById(R.id.title);
			date = (TextView) view.findViewById(R.id.time);
			contentfirst = (TextView) view.findViewById(R.id.contentfirst);
			contentsecond = (TextView) view.findViewById(R.id.contentsecond);
			record_item = (LinearLayout) view.findViewById(R.id.record_item);
			mainImg = (ImageView) view.findViewById(R.id.mainImg);
		}
	}

	public FaultHistoryInfo getEntity(JSONObject joooo) {
		FaultHistoryInfo faultHistoryInfo = new FaultHistoryInfo();
		try {
			faultHistoryInfo.setId(joooo.getString("id"));
		} catch (JSONException e) {
			faultHistoryInfo.setId("无数据");
		}
		try {
			faultHistoryInfo.setVehicleId(joooo.getString("vehicle_id"));
		} catch (JSONException e) {
			faultHistoryInfo.setVehicleId("无数据");
		}
		try {
			faultHistoryInfo.setFaultDescription(joooo.getString("fault_description"));
		} catch (JSONException e) {
			faultHistoryInfo.setFaultDescription("无数据");
		}
		try {
			faultHistoryInfo.setLocation(joooo.getString("location"));
		} catch (JSONException e) {
			faultHistoryInfo.setLocation("无数据");
		}
		try {
			faultHistoryInfo.setFaulTime(minToCaleander(joooo.getLong("fault_time")));
		} catch (JSONException e) {
			faultHistoryInfo.setFaulTime("无数据");
		}
		try {
			faultHistoryInfo.setFaultType(joooo.getString("fault_type"));
		} catch (JSONException e) {
			faultHistoryInfo.setFaultType("无数据");
		}
		try {
			faultHistoryInfo.setFaultRecorder(joooo.getString("fault_recorder"));
		} catch (JSONException e) {
			faultHistoryInfo.setFaultRecorder("无数据");
		}
		try {
			faultHistoryInfo.setUserId(joooo.getString("user_id"));
		} catch (JSONException e) {
			faultHistoryInfo.setUserId("无数据");
		}
		try {
			faultHistoryInfo.setState(joooo.getString("state"));
		} catch (JSONException e) {
			faultHistoryInfo.setState("无数据");
		}
		return faultHistoryInfo;
	}

	public void resetSingleData(JSONArray data) {
		list.clear();
		resetData(data);
	}

	public static String minToCaleander(long now) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);

		return formatter.format(calendar.getTime());
	}
}
