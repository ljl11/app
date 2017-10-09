package com.example.newenergyvehicle.salesPersonnel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.salesPersonnel.FaultHandingHistoryEmergencyAdapter.ViewHolder;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ShuttleMissionAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<ShuttleMissionInfo> list;
	private List<ShuttleMissionInfo> showList;
	private int statue = -1;
	private boolean flag = false;
	private Handler handler = new Handler();

	public int getMax() {
		return list.size();
	}

	public ShuttleMissionAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<ShuttleMissionInfo>();
		showList = new ArrayList<ShuttleMissionInfo>();
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
		updateShowList();
	}

	private void updateShowList() {
		showList.clear();
		for (ShuttleMissionInfo ci : list) {
			if (ci.getUnread() == statue || statue == -1)
				showList.add(ci);
		}
	}

	@Override
	public int getCount() {
		return showList.size();
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

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.shuttle_mission_item, null);
			holder = new ViewHolder(convertView);
			setItem(holder, position);
		} else
			holder = (ViewHolder) convertView.getTag();
		
		return convertView;
	}

	private void setItem(ViewHolder holder, final int position) {
		final ShuttleMissionInfo shuttleMissionInfo = showList.get(position);
		if (shuttleMissionInfo.getUnread() == 0) {
			holder.imgCancle.setVisibility(View.VISIBLE);
			holder.bgimg.setBackgroundResource(R.drawable.green_back);
		}
		if (shuttleMissionInfo.getUnread() == 1) {
			holder.imgCancle.setVisibility(View.INVISIBLE);
			holder.bgimg.setBackgroundResource(R.drawable.green_back);
		}
		if (shuttleMissionInfo.getUnread() == 2) {
			holder.imgCancle.setVisibility(View.INVISIBLE);
			holder.bgimg.setBackgroundResource(R.drawable.grey_back);
		}
		if (shuttleMissionInfo.getTaskType() == 0)
			holder.taskType.setText("派车地点：");
		else if (shuttleMissionInfo.getTaskType() == 1)
			holder.taskType.setText("接车地点：");
		holder.place.setText(shuttleMissionInfo.getLocation());
		holder.time.setText(shuttleMissionInfo.getTime());
		holder.plateNumber.setText(shuttleMissionInfo.getPlateNumber());
		holder.trouble_itme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (shuttleMissionInfo.getUnread() == 0) {
					updateState(shuttleMissionInfo);
				} else {
					Bundle bundle = new Bundle();
					bundle.putSerializable("id", shuttleMissionInfo.getId());
					CarAssignment ca = new CarAssignment();
					ca.setArguments(bundle);
					((DrawerLayoutMenu) context).changeView(ca);
				}
			}
		});
	}

	class ViewHolder {
		TextView plateNumber;
		TextView time;
		TextView place;
		TextView taskType;
		RelativeLayout bgimg;
		ImageView imgCancle;
		LinearLayout trouble_itme;

		public ViewHolder(View view) {
			plateNumber = (TextView) view.findViewById(R.id.plateNumber);
			time = (TextView) view.findViewById(R.id.time);
			place = (TextView) view.findViewById(R.id.send_car_place);
			taskType = (TextView) view.findViewById(R.id.task_type_state);
			trouble_itme = (LinearLayout) view.findViewById(R.id.trouble_itme);
			bgimg = (RelativeLayout) view.findViewById(R.id.bgimg);
			imgCancle = (ImageView) view.findViewById(R.id.imgCancle);
		}
	}

	public void updateState(final ShuttleMissionInfo shuttleMissionInfo) {

		handler.postDelayed(new Runnable() {
			public void run() {
				Map param = new HashMap();
				param.put("id", shuttleMissionInfo.getId());
				param.put("state", 1);
				HttpUtil.getClient().put(
						HttpUtil.getURL("api/emergencyCarManage/updateSARState"
								+ ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								if (shuttleMissionInfo.getUnread() == 0)
									setFlag(true);
								shuttleMissionInfo.setUnread(1);
								Bundle bundle = new Bundle();
								bundle.putSerializable("info", shuttleMissionInfo);
								CarAssignment ca = new CarAssignment();
								ca.setArguments(bundle);
								((DrawerLayoutMenu) context).changeView(ca);
							}

							@Override
							public void onFailure(Throwable error) {
							}
						});
			}
		},10);
		
	}

	public ShuttleMissionInfo getEntity(JSONObject joooo) {
		ShuttleMissionInfo shuttleMissionInfo = new ShuttleMissionInfo();
		try {
			shuttleMissionInfo.setLocation(joooo.getString("task_place"));
		} catch (JSONException e) {
			shuttleMissionInfo.setLocation("无数据");
		}
		try {
			shuttleMissionInfo.setId(joooo.getString("carDeliveryId"));
		} catch (JSONException e) {
			shuttleMissionInfo.setId("无数据");
		}
		try {
			shuttleMissionInfo.setDeliveryObjectNmae(joooo.getString("deliveryObjectNmae"));
		} catch (JSONException e) {
			shuttleMissionInfo.setDeliveryObjectNmae("无数据");
		}
		try {
			shuttleMissionInfo.setUserPhone(joooo.getString("userPhone"));
		} catch (JSONException e) {
			shuttleMissionInfo.setUserPhone("无数据");
		}
		try {
			shuttleMissionInfo.setChargePhone(joooo.getString("chargePhone"));
		} catch (JSONException e) {
			shuttleMissionInfo.setChargePhone("无数据");
		}
		try {
			shuttleMissionInfo.setCurrentPhone(joooo.getString("currentPhone"));
		} catch (JSONException e) {
			shuttleMissionInfo.setCurrentPhone("无数据");
		}
		try {
			shuttleMissionInfo.setCarDeliveryId(joooo
					.getString("carDeliveryId"));
		} catch (JSONException e) {
			shuttleMissionInfo.setCarDeliveryId("无数据");
		}
		try {
			shuttleMissionInfo.setTime(joooo.getString("taskTime"));
		} catch (JSONException e) {
			shuttleMissionInfo.setTime("无数据");
		}
		try {
			shuttleMissionInfo.setPlateNumber(joooo.getString("plateNumber"));
		} catch (JSONException e) {
			shuttleMissionInfo.setPlateNumber("无数据");
		}
		try {
			shuttleMissionInfo.setTaskType(joooo.getInt("TaskType"));
		} catch (JSONException e) {
			shuttleMissionInfo.setTaskType(-1);
		}
		try {
			shuttleMissionInfo.setVehicleId(joooo.getString("vehicle_id"));
		} catch (JSONException e) {
			shuttleMissionInfo.setVehicleId("无数据");
		}
		try {
			shuttleMissionInfo.setUserId(joooo.getString("userId"));
		} catch (JSONException e) {
			shuttleMissionInfo.setUserId("无数据");
		}
		try {
			shuttleMissionInfo.setUnread(joooo.getInt("isread"));
		} catch (JSONException e) {
			shuttleMissionInfo.setUnread(-1);
		}
		return shuttleMissionInfo;
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

	public boolean setStatue(int statue) {
		this.statue = statue;
		updateShowList();
		notifyDataSetChanged();
		if(showList.size() == 0)
			return true;
		return false;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
