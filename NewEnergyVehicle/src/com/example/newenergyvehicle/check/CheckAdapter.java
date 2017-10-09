package com.example.newenergyvehicle.check;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.example.newenergyvehicle.myWork.TroubleInfo;
import com.example.newenergyvehicle.personal.Personal;
import com.example.newenergyvehicle.salesPersonnel.FaultHanding;
import com.example.newenergyvehicle.vehicleInfo.VehicleInfo;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CheckAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<CheckInfo> list;
	private List<CheckInfo> showList;
	private int statue = -1;
	private boolean flag = false;
	private Handler handler = new Handler();

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public CheckAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<CheckInfo>();
		showList = new ArrayList<CheckInfo>();
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
		final int index = position;

//		if (convertView == null) {
			convertView = inflater.inflate(R.layout.trouble_item, null);
			holder = new ViewHolder(convertView);
			setItem(holder, position);
//		} else {
			convertView.setTag(holder);
//		}
		
		return convertView;
	}

	private void setItem(ViewHolder holder, int position) {
		final CheckInfo checkInfo = showList.get(position);
		if (checkInfo.getUnread() == 1) {
			holder.imgCancle.setVisibility(View.INVISIBLE);
			holder.bgimg.setBackgroundResource(R.drawable.grey_back);
		}
		// if(checkInfo.getUnread() == 2){
		// holder.imgCancle.setVisibility(View.INVISIBLE);
		// holder.bgimg.setBackgroundResource(R.drawable.grey_back);
		// }
		holder.applyUser.setText(checkInfo.getApply_user());
		holder.time.setText(checkInfo.getApply_time());
		holder.applyResaon.setText(checkInfo.getApply_reason());
		holder.trouble_itme.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				handler.postDelayed(new Runnable() {
					public void run() {
						Map param = new HashMap();
						param.put("id", checkInfo.getId());
						param.put("state", 1);
						HttpUtil.getClient()
						.put(HttpUtil
								.getURL("api/emergencyCarManage/updateCheckState"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										if (checkInfo.getUnread() == 0)
											setFlag(true);
										checkInfo.setUnread(1);
										Bundle bundle = new Bundle();
										bundle.putSerializable("checkInfo",
												checkInfo);
										VehicleCheckInfo vc = new VehicleCheckInfo();
										vc.setArguments(bundle);
										((DrawerLayoutMenu) context)
												.changeView(vc);
									}

									@Override
									public void onFailure(Throwable error) {
									}
								});

					}
				},100);
				
			}
		});
	}

	class ViewHolder {
		TextView applyUser;
		TextView time;
		TextView applyResaon;
		RelativeLayout bgimg;
		ImageView imgCancle;
		LinearLayout trouble_itme;

		public ViewHolder(View view) {
			applyUser = (TextView) view.findViewById(R.id.plateNumber);
			applyResaon = (TextView) view.findViewById(R.id.trouble_detail);
			time = (TextView) view.findViewById(R.id.time);
			bgimg = (RelativeLayout) view.findViewById(R.id.bgimg);
			imgCancle = (ImageView) view.findViewById(R.id.imgCancle);
			trouble_itme = (LinearLayout) view.findViewById(R.id.trouble_itme);
		}
	}

	private void updateShowList() {
		showList.clear();
		for (CheckInfo ci : list) {
			if (ci.getUnread() == statue || statue == -1)
				showList.add(ci);
		}
	}

	public CheckInfo getEntity(JSONObject joooo) {
		CheckInfo checkInfo = new CheckInfo();
		try {
			checkInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
			checkInfo.setId("暂无数据");
		}
		try {
			checkInfo.setApply_user(joooo.getString("apply_user"));
		} catch (Exception e) {
			checkInfo.setState("暂无数据");
		}
		try {
			checkInfo.setApply_reason(joooo.getString("apply_reason"));
		} catch (Exception e) {
			checkInfo.setApply_reason("暂无数据");
		}

		try {
			checkInfo.setApply_time(joooo.getString("apply_time"));
		} catch (Exception e) {
			checkInfo.setApply_time("暂无数据");
		}

		try {
			checkInfo.setUnread(joooo.getInt("state"));
		} catch (Exception e) {
			checkInfo.setUnread(-1);
		}
		// 已读和未读
		try {
			checkInfo.setFaultRecordId(joooo.getString("fault_record"));
		} catch (Exception e) {
			checkInfo.setFaultRecordId(null);
		}
		//申请人id
		try {
			checkInfo.setuId(joooo.getString("apply_id"));
		} catch (Exception e) {
			checkInfo.setuId(null);
		}
		return checkInfo;
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
		if(showList.size()==0)
			return true;
		return false;
	}
}
