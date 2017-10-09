package com.example.newenergyvehicle.emergencyVehicle;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.example.newenergyvehicle.check.CheckInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.personal.Personal;

public class ArrangementListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<ArrangeItemInfo> list;
	private String type;
	private String taskType;

	public int getMax() {
		return list.size();
	}

	public ArrangementListAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<ArrangeItemInfo>();
	}

	public void resetData(JSONArray data) {
		if (data != null) {
			int length = data.length();
			for (int i = 0; i < length; i++) {
				try {
					if (type.equals("0"))
						list.add(getEntity(data.getJSONObject(i)));
					if (type.equals("1"))
						list.add(getRecoveryEntity(data.getJSONObject(i)));
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
			convertView = inflater
					.inflate(R.layout.arrangement_list_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setItem(holder, position);
		return convertView;
	}

	private void setItem(ViewHolder holder, final int position) {
		final ArrangeItemInfo historyInfo = list.get(position);
		holder.plateNumber.setText(list.get(position).getPlateNumber());
		holder.time.setText(list.get(position).getTaskTime());
		holder.state.setText("客户:" + list.get(position).getName());
		if (type.equals("0")) {
			if (list.get(position).getState()==1)
				holder.whetherRecovery.setText("未回收");
			else if (list.get(position).getState()==3)
					holder.whetherRecovery.setText("已回收");
			else if (list.get(position).getState()==2)
				holder.whetherRecovery.setText("回收中");
			else if (list.get(position).getState()==0)
				holder.whetherRecovery.setText("未领车");
		}
		// 派给用户
		// 点击获取详情
		holder.trouble_itme.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				if (type.equals("0")) {
					taskType = String.valueOf(list.get(position).getState());
					bundle.putString("id", list.get(position).getId());
					bundle.putString("taskType", taskType);
					Fragment fragment = new ArrangementDetail();
					fragment.setArguments(bundle);
					((DrawerLayoutMenu) context).changeView(fragment);
				}
				if (type.equals("1")) {
					bundle.putSerializable("info", historyInfo);
					Fragment fa = new EmergencyRecoveryDetail();
					fa.setArguments(bundle);
					((DrawerLayoutMenu) context).changeView(fa);
				}
			}
		});
	}

	class ViewHolder {
		TextView plateNumber;
		TextView time;
		TextView state;
		TextView whetherRecovery;
		LinearLayout trouble_itme;

		public ViewHolder(View view) {
			plateNumber = (TextView) view
					.findViewById(R.id.car_item_plate_number);
			state = (TextView) view.findViewById(R.id.car_item_state);
			time = (TextView) view.findViewById(R.id.arrange_time);
			whetherRecovery = (TextView) view
					.findViewById(R.id.whether_recovery);
			trouble_itme = (LinearLayout) view
					.findViewById(R.id.arrangement_item);
		}
	}

	public ArrangeItemInfo getEntity(JSONObject joooo) {
		ArrangeItemInfo arrangeItemInfo = new ArrangeItemInfo();
		try {
			arrangeItemInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
			arrangeItemInfo.setId("暂无数据");
		}
		try {
			arrangeItemInfo.setTaskPlace(joooo.getString("task_place"));
		} catch (Exception e) {
			arrangeItemInfo.setTaskPlace("暂无数据");
		}
		try {
			arrangeItemInfo.setVehicleId(joooo.getString("vehicle_id"));
		} catch (Exception e) {
			arrangeItemInfo.setVehicleId("暂无数据");
		}
		try {
			arrangeItemInfo.setPlateNumber(joooo.getString("plate_number"));
		} catch (Exception e) {
			arrangeItemInfo.setPlateNumber("暂无数据");
		}

		try {
			arrangeItemInfo.setTaskTime(joooo.getString("taskAddTime"));
		} catch (Exception e) {
			arrangeItemInfo.setTaskTime("暂无数据");
		}
		try {
			arrangeItemInfo.setTaskType(joooo.getString("task_type"));
		} catch (Exception e) {
			arrangeItemInfo.setTaskType("暂无数据");
		}
		try {
			arrangeItemInfo.setName(joooo.getString("NAME"));
		} catch (Exception e) {
			arrangeItemInfo.setName("暂无数据");
		}
		try {
			arrangeItemInfo.setFaultRcordId(joooo.getString("fault_record"));
		} catch (Exception e) {
			arrangeItemInfo.setFaultRcordId(null);
		}
		try {
			arrangeItemInfo.setState(joooo.getInt("state"));
		} catch (Exception e) {
			arrangeItemInfo.setState(-1);
		}
		try {
			arrangeItemInfo.setFullDate(joooo.getString("fullDate"));
		} catch (Exception e) {
			arrangeItemInfo.setFullDate(null);
		}

		return arrangeItemInfo;
	}

	public ArrangeItemInfo getRecoveryEntity(JSONObject joooo) {
		ArrangeItemInfo arrangeItemInfo = new ArrangeItemInfo();
		try {
			arrangeItemInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
			arrangeItemInfo.setId("暂无数据");
		}
		try {
			arrangeItemInfo.setVehicleId(joooo.getString("vehicle_id"));
		} catch (Exception e) {
			arrangeItemInfo.setVehicleId("暂无数据");
		}
		try {
			arrangeItemInfo.setPlateNumber(joooo.getString("plate_number"));
		} catch (Exception e) {
			arrangeItemInfo.setPlateNumber("暂无数据");
		}

		try {
			arrangeItemInfo.setTaskTime(joooo.getString("taskAddTime"));
		} catch (Exception e) {
			arrangeItemInfo.setTaskTime("暂无数据");
		}

		try {
			arrangeItemInfo.setName(joooo.getString("unit_name"));
		} catch (Exception e) {
			arrangeItemInfo.setName("暂无数据");
		}

		try {
			arrangeItemInfo.setLeglePeople(joooo.getString("legal_person"));
		} catch (Exception e) {
			arrangeItemInfo.setLeglePeople("暂无数据");
		}

		try {
			arrangeItemInfo.setReleasePeople(joooo.getString("name"));
		} catch (Exception e) {
			arrangeItemInfo.setReleasePeople("暂无数据");
		}

		try {
			arrangeItemInfo.setTaskPlace(joooo.getString("task_place"));
		} catch (Exception e) {
			arrangeItemInfo.setTaskPlace("暂无数据");
		}
		try {
			arrangeItemInfo.setTaskType(joooo.getString("task_type"));
		} catch (Exception e) {
			arrangeItemInfo.setTaskType("暂无数据");
		}
		try {
			arrangeItemInfo.setState(joooo.getInt("state"));
		} catch (Exception e) {
			arrangeItemInfo.setState(0);
		}

		return arrangeItemInfo;
	}

	public void resetSingleData(JSONArray data,String type) {
		this.type = type;
		list.clear();
		resetData(data);
	}

	public static String minToCaleander(long now) {
		DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);

		return formatter.format(calendar.getTime());
	}
}
