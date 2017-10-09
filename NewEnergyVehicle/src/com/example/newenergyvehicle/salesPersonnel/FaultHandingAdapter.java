package com.example.newenergyvehicle.salesPersonnel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.util.Common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FaultHandingAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<FaultHistoryInfo> list;
	 
	public int getMax(){
		return list.size();
	}
	public FaultHandingAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<FaultHistoryInfo>();
	}
	
	public synchronized void resetData(JSONArray data){
		if(data!=null) {
			int length = data.length();
			for(int i = length-1;i >= 0;i--) {
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fault_handing_history_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			setItem(holder, position);
			
		return convertView;
	}
	private void setItem(ViewHolder holder, final int position){
		FaultHistoryInfo faultHistoryInfo = list.get(position);
		holder.hour.setText(faultHistoryInfo.getHour());
		holder.year.setText(faultHistoryInfo.getYear());
		String type = list.get(position).getType();
		int newType = Integer.parseInt(type);
		switch(newType){
		case 0:
			holder.type.setText(list.get(position).getDistribution()+"报告");
			holder.next_detail_ft.setVisibility(View.INVISIBLE); 
			break;
		case 1 :
			holder.type.setText("客服 转交 "+list.get(position).getReception());
			break;
		case 2 :
			holder.type.setText("售后主管"+" 指派给 "+list.get(position).getReception());
			break;
		case 5 :
			holder.type.setText(list.get(position).getReception()+"处理完毕");
			break;
		default:
			holder.type.setText(list.get(position).getDistribution()+" 转交 "+list.get(position).getReception());
			break;
		}
		holder.fault_handing_history_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!list.get(position).getType().equals("0")){
					String message = "故障描述："+list.get(position).getDisDescription();
					showNormalDialog(message);
				}
			}
		});
		
	}
	class ViewHolder {
		TextView hour;
		TextView year;
		TextView type;
		LinearLayout fault_handing_history_list;
		LinearLayout next_detail_ft;
		public ViewHolder(View view){
			hour = (TextView) view.findViewById(R.id.fault_hour);
			year = (TextView) view.findViewById(R.id.fault_year);
			type = (TextView) view.findViewById(R.id.fault_type);
			fault_handing_history_list = (LinearLayout)view.findViewById(R.id.fault_handing_history_list);
			next_detail_ft = (LinearLayout)view.findViewById(R.id.next_detail_ft);
		}
	}
	public FaultHistoryInfo getEntity(JSONObject joooo) {
		FaultHistoryInfo faultHistoryInfo = new FaultHistoryInfo();
		try {
			faultHistoryInfo.setDistributionId(joooo.getString("distributionId"));
		} catch (JSONException e) {
			faultHistoryInfo.setDistributionId("");
		}
		
		try {
			faultHistoryInfo.setType(joooo.getString("hisType"));
		} catch (JSONException e) {
			faultHistoryInfo.setType("");
		}
		
		try {
			faultHistoryInfo.setFaultDistributionId(joooo.getString("id"));
		} catch (JSONException e) {
			faultHistoryInfo.setFaultDistributionId(null);
		}
		
		try {
			faultHistoryInfo.setDisDescription(joooo.getString("disDescription"));
		} catch (JSONException e) {
			faultHistoryInfo.setDisDescription("暂无描述");
		}
		
		try {
			faultHistoryInfo.setDistribution(joooo.getString("distributionName"));
		} catch (JSONException e) {
			faultHistoryInfo.setDistribution("--");
		}
		try {
			faultHistoryInfo.setReceptionId(joooo.getString("recipientId"));
		} catch (JSONException e) {
			faultHistoryInfo.setReceptionId("--");
		}
		
		try {
			faultHistoryInfo.setReception(joooo.getString("recipientName"));
		} catch (JSONException e) {
			faultHistoryInfo.setReception("--");
		}
		try {
			String[] times = joooo.getString("time").split(" ");
			faultHistoryInfo.setYear(times[0]);
			faultHistoryInfo.setHour(times[1]);
		} catch (JSONException e) {
			faultHistoryInfo.setYear("--");
			faultHistoryInfo.setHour("--");
		}
		return faultHistoryInfo;
	}
	
	public void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
	}
	
	public void showNormalDialog(String message) {
		Common.dialog((Activity)context, null, message, null,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}, "确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
					}

				}, null, null);
	}
}
