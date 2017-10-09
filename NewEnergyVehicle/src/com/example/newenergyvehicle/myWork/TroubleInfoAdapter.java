package com.example.newenergyvehicle.myWork;

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
import android.os.Bundle;
import android.os.Handler;
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
import com.example.newenergyvehicle.salesPersonnel.FaultHanding;
import com.example.util.HttpUtil;
import com.example.util.menu.MenuList;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class TroubleInfoAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<TroubleInfo> list;
	private List<TroubleInfo> showList;
	private int statue = -1;
	private boolean flag = false;
	private Handler handler = new Handler();
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public TroubleInfoAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<TroubleInfo>();
		showList = new ArrayList<TroubleInfo>();
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

		updateShowList();
	}

	private void updateShowList() {
		showList.clear();
		for (TroubleInfo ti : list) {
			if (ti.getUnread() == statue || statue == -1)
				showList.add(ti);
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
		final TroubleInfo troubleInfo = showList.get(position);
		if (troubleInfo.getUnread() == 1) {
			holder.imgCancle.setVisibility(View.INVISIBLE);
			holder.bgimg.setBackgroundResource(R.drawable.grey_back);
		}
		// if (troubleInfo.getIsHandle() == 1) {
		// holder.imgCancle.setVisibility(View.INVISIBLE);
		// holder.bgimg.setBackgroundResource(R.drawable.grey_back);
		// }
		holder.plateNumber.setText(showList.get(position).getPlateNumber());
		holder.time.setText(showList.get(position).getTime());
		holder.trouble_detail.setText(showList.get(position)
				.getTrouble_detail());
		holder.trouble_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						Map param = new HashMap();
						param.put("id", troubleInfo.getDistributionId());
						param.put("isread", 1);
						HttpUtil.getClient()
								.put(HttpUtil
										.getURL("api/faultRecord/updateDistrabutionIsRead"
												+ ParamUtil.mapToString(param)),
										new AsyncHttpResponseHandler() {
											@Override
											public void onSuccess(String response) {
												if (troubleInfo.getUnread() == 0)
													setFlag(true);
												troubleInfo.setUnread(1);
												Bundle bundle = new Bundle();
												bundle.putString("id",
														troubleInfo.getId());
												FaultHanding fh = new FaultHanding();
												fh.setArguments(bundle);
												((DrawerLayoutMenu) context)
														.changeView(fh);

												((DrawerLayoutMenu) context)
														.getAdapter()
														.changeNoite(
																MenuList.getMenuList()
																		.getIdByClass(
																				MyWork.class));
											}

											@Override
											public void onFailure(Throwable error) {
											}
										});
					}
				},10);
			}
		});
	}

	class ViewHolder {
		TextView plateNumber;
		TextView time;
		TextView trouble_detail;
		RelativeLayout bgimg;
		ImageView imgCancle;
		LinearLayout trouble_item;

		public ViewHolder(View view) {
			plateNumber = (TextView) view.findViewById(R.id.plateNumber);
			time = (TextView) view.findViewById(R.id.time);
			trouble_detail = (TextView) view.findViewById(R.id.trouble_detail);
			trouble_item = (LinearLayout) view.findViewById(R.id.trouble_itme);
			bgimg = (RelativeLayout) view.findViewById(R.id.bgimg);
			imgCancle = (ImageView) view.findViewById(R.id.imgCancle);
		}
	}

	public TroubleInfo getEntity(JSONObject joooo) {
		TroubleInfo troubleInfo = new TroubleInfo();
		try {
			troubleInfo.setPlateNumber(joooo.getString("plateNumber"));
		} catch (Exception e) {
			troubleInfo.setPlateNumber("暂无数据");
		}
		try {
			troubleInfo.setDistributionId(joooo.getString("fdId"));
		} catch (Exception e) {
			troubleInfo.setDistributionId("暂无数据");
		}
		try {
			troubleInfo.setTime(joooo.getString("time"));
		} catch (Exception e) {
			troubleInfo.setTime("暂无数据");
		}
		try {
			troubleInfo.setTrouble_detail(joooo.getString("faultDescription"));
		} catch (Exception e) {
			troubleInfo.setTrouble_detail("暂无数据");
		}
		try {
			troubleInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
			troubleInfo.setId("暂无数据");
		}
		try {
			troubleInfo.setUnread(joooo.getInt("isRead"));
		} catch (Exception e) {
			troubleInfo.setUnread(-1);
		}

		try {
			troubleInfo.setLocation(joooo.getString("location"));
		} catch (Exception e) {
			troubleInfo.setLocation("暂无位置信息");
		}

		try {
			troubleInfo.setVehicleId(joooo.getString("vehicleId"));
		} catch (Exception e) {
			troubleInfo.setVehicleId("暂无信息");
		}

		try {
			troubleInfo.setIsHandle(joooo.getInt("isHandle"));
		} catch (Exception e) {
			troubleInfo.setIsHandle(-1);
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

	public boolean setStatue(int statue) {
		this.statue = statue;
		updateShowList();
		notifyDataSetChanged();
		if(showList.size()==0)
			return true;
		return false;
	}
}
