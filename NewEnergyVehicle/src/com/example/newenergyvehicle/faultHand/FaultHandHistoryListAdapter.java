package com.example.newenergyvehicle.faultHand;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
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

public class FaultHandHistoryListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<FaultHandHistoryItemInfo> list;
	private int statue = -1;

	public FaultHandHistoryListAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<FaultHandHistoryItemInfo>();
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
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
       
		setItem(holder, position);
		
		return convertView;
	}
	@SuppressLint("ResourceAsColor")
	private void setItem(ViewHolder holder, int position) {
		final FaultHandHistoryItemInfo historyTroubleInfo = list.get(position);
		final int index = position;
		String state = historyTroubleInfo.getState();
		holder.history_content.setText(historyTroubleInfo.getHistory_content());
		holder.history_time.setText(historyTroubleInfo.getTime());
		if(state.equals("5")){
			holder.ellipse_history.setImageResource(R.drawable.ellipse);
			holder.history_type.setText("已修复");
			holder.history_type.setTextColor(R.color.text_color_b);
		}
		else{
			holder.ellipse_history.setImageResource(R.drawable.new_history);
			holder.history_type.setText("维修中");
			holder.history_type.setTextColor(R.color.text_color_yellow);
		}
		holder.fault_history_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("historyTroubleInfo", list.get(index));
				Fragment fh = new NewFaultHistoryDetail();
				fh.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fh);
			}
		});
	}

	class ViewHolder {
		TextView history_content;
		TextView history_time;
		TextView history_type;
		ImageView ellipse_history;
		LinearLayout fault_history_item;

		public ViewHolder(View view) {
			history_content = (TextView) view.findViewById(R.id.history_content);
			history_time = (TextView) view.findViewById(R.id.history_time);
			history_type = (TextView) view.findViewById(R.id.fault_state);
			ellipse_history = (ImageView) view.findViewById(R.id.ellipse_history);
			fault_history_item = (LinearLayout) view.findViewById(R.id.fault_history_item);
		}
	}
	
	public FaultHandHistoryItemInfo getEntity(JSONObject joooo) {
		FaultHandHistoryItemInfo troubleInfo = new FaultHandHistoryItemInfo();
		try {
			troubleInfo.setHistory_content(joooo.getString("description"));
		} catch (Exception e) {
			troubleInfo.setHistory_content("--");
		}
		try {
			troubleInfo.setState(joooo.getString("state"));
		} catch (Exception e) {
			troubleInfo.setState("0");
		}
		try {
			troubleInfo.setTime(joooo.getString("time"));
		} catch (Exception e) {
			troubleInfo.setTime("--");
		}
		try {
			troubleInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
			troubleInfo.setId("null");
		}

		return troubleInfo;
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
