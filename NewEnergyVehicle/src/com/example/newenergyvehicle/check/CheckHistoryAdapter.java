//package com.example.newenergyvehicle.check;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.example.newenergyvehicle.R;
//import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
//import com.example.newenergyvehicle.vehicle.TransferDetail;
//import com.example.newenergyvehicle.vehicle.TransferInfo;
//
//public class CheckHistoryAdapter extends BaseAdapter{
//	private Context context;
//	private LayoutInflater inflater;
//	private List<CheckInfo> list;
//
//	public int getMax() {
//		return list.size();
//	}
//
//	public CheckHistoryAdapter(Context context) {
//		this.context = context;
//		inflater = LayoutInflater.from(context);
//		list = new ArrayList<CheckInfo>();
//	}
//
//	public void resetData(JSONArray data) {
//		if (data != null) {
//			int length = data.length();
//			for (int i = 0; i < length; i++) {
//				try {
//					list.add(getEntity(data.getJSONObject(i)));
//				} catch (JSONException e) {
//				}
//			}
//		}
//	}
//
//	@Override
//	public int getCount() {
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return position;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		ViewHolder holder = null;
//		final int index = position;
//		convertView = inflater.inflate(R.layout.transfer_record_item, null);
//		holder = new ViewHolder(convertView);
//		convertView.setTag(holder);
//
//		setItem(holder, position);
//		return convertView;
//	}
//
//	private void setItem(ViewHolder holder, final int position) {
//		final CheckInfo checkInfo = list.get(position);
//		holder.title.setText(checkInfo.getApply_user());
//		holder.date.setText(checkInfo.getApply_time());
//		holder.contentfirst.setText(checkInfo.getResult() + ":");
//		holder.contentsecond.setText(checkInfo.getApply_reason());
//		holder.record_item.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("info", checkInfo);
//				CheckHistory checkHistory = new CheckHistory();
//				checkHistory.setArguments(bundle);
//				((DrawerLayoutMenu) context).changeView(checkHistory);
//			}
//		});
//	}
//
//	class ViewHolder {
//		TextView title;
//		TextView date;
//		TextView contentfirst;
//		TextView contentsecond;
//		LinearLayout record_item;
//
//		public ViewHolder(View view) {
//			title = (TextView) view.findViewById(R.id.title);
//			date = (TextView) view.findViewById(R.id.time);
//			contentfirst = (TextView) view.findViewById(R.id.contentfirst);
//			contentsecond = (TextView) view.findViewById(R.id.contentsecond);
//			record_item = (LinearLayout) view.findViewById(R.id.record_item);
//		}
//	}
//
//	public CheckInfo getEntity(JSONObject joooo) {
//		CheckInfo checkInfo = new CheckInfo();
//		try {
//			checkInfo.setId(joooo.getString("id"));
//		} catch (Exception e) {
//			checkInfo.setId("暂无数据");
//		}
//		try {
//			checkInfo.setApply_user(joooo.getString("apply_user"));
//		} catch (Exception e) {
//			checkInfo.setState("暂无数据");
//		}
//		try {
//			checkInfo.setApply_reason(joooo.getString("apply_reason"));
//		} catch (Exception e) {
//			checkInfo.setApply_reason("暂无数据");
//		}
//		try {
//			checkInfo.setApply_time(joooo.getString("apply_time"));
//		} catch (Exception e) {
//			checkInfo.setApply_time("暂无数据");
//		}
//		try {
//			checkInfo.setUnread(joooo.getInt("state"));
//		} catch (Exception e) {
//			checkInfo.setUnread(-1);
//		}
//		try {
//			checkInfo.setResult(joooo.getString("result"));
//		} catch (Exception e) {
//			checkInfo.setResult("暂无数据");
//		}
//		// 已读和未读
//		return checkInfo;
//	}
//
//	public void resetSingleData(JSONArray data) {
//		list.clear();
//		resetData(data);
//	}
//
//	public static String minToCaleander(long now) {
//		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(now);
//
//		return formatter.format(calendar.getTime());
//	}
//}
