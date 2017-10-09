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

public class IllegalRecordAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<IllegalRecordInfo> list;
	private int statue = -1;

	public IllegalRecordAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<IllegalRecordInfo>();
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
			convertView = inflater.inflate(R.layout.fault_history_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			setItem(holder, position);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		return convertView;
	}
	@SuppressLint("ResourceAsColor")
	private void setItem(ViewHolder holder, int position) {
		final IllegalRecordInfo illegalRecordInfo = list.get(position);
		final int index = position;
		String state = illegalRecordInfo.getIllegalAddress();
		holder.history_content.setText(illegalRecordInfo.getIllegalAction());
		holder.history_time.setText(illegalRecordInfo.getIllegalDate());
		holder.points.setText("-"+illegalRecordInfo.getIllegalPoint());
		holder.ellipse_history.setImageResource(R.drawable.points);
		holder.history_type.setText(state);
		holder.history_type.setTextColor(R.color.text_color_b);
		
		/*else{
			holder.ellipse_history.setImageResource(R.drawable.new_history);
			holder.history_type.setText("维修中");
			holder.history_type.setTextColor(R.color.text_color_yellow);
		}*/
		holder.fault_history_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("illegalRecordDetail", list.get(index));
				Fragment fh = new FaultHandHistoryDetail();
				fh.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fh);
			}
		});
	}

	class ViewHolder {
		TextView history_content;
		TextView history_time;
		TextView history_type;
		TextView points;
		ImageView faultHistoryItemPic;
		ImageView ellipse_history;
		LinearLayout fault_history_item;

		public ViewHolder(View view) {
			history_content = (TextView) view.findViewById(R.id.history_content);
			history_time = (TextView) view.findViewById(R.id.history_time);
			history_type = (TextView) view.findViewById(R.id.fault_state);
			points = (TextView) view.findViewById(R.id.points);
			ellipse_history = (ImageView) view.findViewById(R.id.ellipse_history);
			fault_history_item = (LinearLayout) view.findViewById(R.id.fault_history_item);
			faultHistoryItemPic = (ImageView) view.findViewById(R.id.faultHistoryItemPic);
			faultHistoryItemPic.setVisibility(View.GONE);
		}
	}
	
	public IllegalRecordInfo getEntity(JSONObject joooo) {
		IllegalRecordInfo illegalRecordInfo = new IllegalRecordInfo();
		try {
			illegalRecordInfo.setIllegalAction(joooo.getString("illegalAction"));
		} catch (Exception e) {
			illegalRecordInfo.setIllegalAction("暂无");
		}
		try {
			illegalRecordInfo.setIllegalAddress(joooo.getString("illegalAddress"));
		} catch (Exception e) {
			illegalRecordInfo.setIllegalAddress("暂无");
		}
		try {
			illegalRecordInfo.setIllegalDate(joooo.getString("illegalDate"));
		} catch (Exception e) {
			illegalRecordInfo.setIllegalDate("暂无");
		}
		try {
			illegalRecordInfo.setIllegalMoney(joooo.getString("illegalMoney"));
		} catch (Exception e) {
			illegalRecordInfo.setIllegalMoney("暂无");
		}
		try {
			illegalRecordInfo.setIllegalPoint(joooo.getString("illegalPoint"));
		} catch (Exception e) {
			illegalRecordInfo.setIllegalPoint("暂无");
		}
		try {
			illegalRecordInfo.setVehicleUserName(joooo.getString("vehicleUserName"));
		} catch (Exception e) {
			illegalRecordInfo.setVehicleUserName("暂无");
		}
		try {
			illegalRecordInfo.setVehicleUserPhone(joooo.getString("vehicleUserPhone"));
		} catch (Exception e) {
			illegalRecordInfo.setVehicleUserPhone("暂无");
		}

		return illegalRecordInfo;
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

