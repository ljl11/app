package com.example.newenergyvehicle.dialog;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.util.Common;
import com.example.util.DialogInfo;

public class ChooseStationAdapter extends BaseAdapter {
	private DialogInfo fragment;
	private Context context;
	private ChooseStation station;
	private LayoutInflater inflater;
	private List<ChooseStationInfo> list;

	public ChooseStationAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<ChooseStationInfo>();
	}

	public void resetData(JSONArray data) {
		if (data != null) {
			int length = data.length();
			for (int i = 0; i < length; i++) {
				try {
					ChooseStationInfo info = getEntity(data.getJSONObject(i));
					if (info != null)
						list.add(info);
					else {
						list.remove(i);
					}
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
		convertView = inflater.inflate(R.layout.station_checking_item, null);
		holder = new ViewHolder(convertView);
		convertView.setTag(holder);
		setItem(holder, position);
		holder = (ViewHolder) convertView.getTag();

		return convertView;
	}

	private void setItem(ViewHolder holder, final int position) {
		holder.station.setText(list.get(position).getInformation());
		holder.staion_linear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				fragment.setInfo(list.get(position));
				station.diss();
			}
		});
	}

	class ViewHolder {
		TextView station;
		LinearLayout staion_linear;

		public ViewHolder(View view) {
			station = (TextView) view.findViewById(R.id.station);
			staion_linear = (LinearLayout) view
					.findViewById(R.id.station_linear);
		}
	}

	public ChooseStationInfo getEntity(JSONObject joooo) {
		ChooseStationInfo chooseStationInfo = new ChooseStationInfo();

		try {
			chooseStationInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
		}

		try {
			chooseStationInfo.setInformation(joooo.getString("information"));
		} catch (Exception e) {
		}
		try {
			chooseStationInfo.setUserId(joooo.getString("userId"));
		} catch (Exception e) {
		}
		try {
			chooseStationInfo.setUserName(joooo.getString("userName"));
		} catch (Exception e) {
		}
		return chooseStationInfo;
	}

	public void resetSingleData(JSONArray data) {
		list.clear();
		resetData(data);
	}

	public void setFragment(DialogInfo fragment) {
		this.fragment = fragment;
	}

	public void setParent(ChooseStation station) {
		this.station = station;
	}

	public JSONArray toInfo(JSONArray array, String... fileds)
			throws JSONException {
		JSONArray info = new JSONArray();
		JSONObject json;
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				json = new JSONObject();
				try {
					json.put("id", array.getJSONObject(i).getString(fileds[0]));
				} catch (JSONException e) {
					json.put("id", "null");
				}
				try {
					json.put("information",
							array.getJSONObject(i).getString(fileds[1]));
				} catch (JSONException e) {
					json.put("information", "信息加载有误");
				}
				if (fileds.length > 3) {
					try {
						json.put("userId",
								array.getJSONObject(i).getString(fileds[2]));
					} catch (JSONException e) {
						json.put("userId", "null");
					}
					try {
						json.put("userName",
								array.getJSONObject(i).getString(fileds[3]));
					} catch (JSONException e) {
						json.put("userName", "信息加载有误");
					}
				}
				info.put(json);
			}
		}
		return info;
	}
}
