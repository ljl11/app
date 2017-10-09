package com.example.newenergyvehicle.vehicle;

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
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;

public class TransferInfoAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<TransferInfo> list;

	public int getMax() {
		return list.size();
	}

	public TransferInfoAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<TransferInfo>();
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

	private void setItem(ViewHolder holder, final int position) {
		final TransferInfo transferInfo = list.get(position);
		holder.title.setText(transferInfo.getPlateNumber());
		holder.date.setText(transferInfo.getHandOverTime());
		holder.contentfirst.setText(transferInfo.getReceiverName() + " 从 ");
		holder.contentsecond.setText(transferInfo.getProviderName() + " 领车");
		holder.record_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("info", transferInfo);
				TransferDetail transferDetail = new TransferDetail();
				transferDetail.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(transferDetail);
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

	public TransferInfo getEntity(JSONObject joooo) {
		TransferInfo transferInfo = new TransferInfo();
		try {
			transferInfo.setId(joooo.getString("id"));
		} catch (JSONException e) {
			transferInfo.setId("无数据");
		}
		try {
			transferInfo.setVehicleProviderId(joooo
					.getString("vehicle_provider_id"));
		} catch (JSONException e) {
			transferInfo.setVehicleProviderId("无数据");
		}
		try {
			transferInfo.setVehicleProviderType(joooo
					.getString("vehicle_provider_type"));
		} catch (JSONException e) {
			transferInfo.setVehicleProviderType("无数据");
		}
		try {
			transferInfo.setVehicleReceiverId(joooo
					.getString("vehicle_receiver_id"));
		} catch (JSONException e) {
			transferInfo.setVehicleReceiverId("无数据");
		}
		try {
			transferInfo.setVehicleReceiverType(joooo
					.getString("vehicle_receiver_type"));
		} catch (JSONException e) {
			transferInfo.setVehicleReceiverType("无数据");
		}
		try {
			transferInfo.setHandOverTime(minToCaleander(joooo.getLong("handover_time")));
		} catch (JSONException e) {
			transferInfo.setHandOverTime("无数据");
		}
		try {
			transferInfo.setProviderName(joooo.getString("providerName"));
		} catch (JSONException e) {
			transferInfo.setProviderName("无数据");
		}
		try {
			transferInfo.setProviderUnitName(joooo
					.getString("providerUnitName"));
		} catch (JSONException e) {
			transferInfo.setProviderUnitName("无数据");
		}
		try {
			transferInfo.setReceiverName(joooo.getString("receiverName"));
		} catch (JSONException e) {
			transferInfo.setReceiverName("无数据");
		}
		try {
			transferInfo.setReceiverUnitName(joooo.getString("receiverUnitName"));
		} catch (JSONException e) {
			transferInfo.setReceiverUnitName("无数据");
		}
		try {
			transferInfo.setType(joooo.getString("type"));
		} catch (JSONException e) {
			transferInfo.setType("无数据");
		}
		try {
			transferInfo.setPlateNumber(joooo.getString("plateNumber"));
		} catch (JSONException e) {
			transferInfo.setPlateNumber("没有车牌信息");
		}
		return transferInfo;
	}

	public void resetSingleData(JSONArray data) {
		list.clear();
		resetData(data);
	}

	public static String minToCaleander(long now) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);

		return formatter.format(calendar.getTime());
	}
}
