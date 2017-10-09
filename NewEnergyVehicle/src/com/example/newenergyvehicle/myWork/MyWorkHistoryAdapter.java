package com.example.newenergyvehicle.myWork;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.check.CheckHistory;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.salesPersonnel.FaultHandlingHistoryDetail;
import com.example.newenergyvehicle.sendAndReceive.SendAndReceiveHistory;

public class MyWorkHistoryAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<HistoryInfo> list;
	private int type;

	public MyWorkHistoryAdapter(Context context, int type) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<HistoryInfo>();
		this.type = type;
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
			switch (type) {
			case 0: convertView = inflater.inflate(R.layout.fault_history_item, null);break;
			case 1: convertView = inflater.inflate(R.layout.transfer_record_item, null);break;
			case 2: convertView = inflater.inflate(R.layout.transfer_record_item, null);break;
			default:
				break;
			}
			holder = new ViewHolder(convertView);	 
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setItem(holder, position);
		return convertView;
	}
	
	private void setItem(ViewHolder holder, int position) {
		final HistoryInfo historyInfo = list.get(position);
		switch (type) {
		case 0: setfault(holder, historyInfo);break;
		case 1: setCheck(holder, historyInfo);break;
		case 2: setSend(holder, historyInfo);break;
		default:
			break;
		}
	}
	
	@SuppressLint("ResourceAsColor")
	private void setfault(ViewHolder holder, final HistoryInfo historyInfo){
		int state = historyInfo.getStatue();
		String  recipientName = historyInfo.getRecipientName();
		holder.title.setText(historyInfo.getTitle());
		holder.time.setText(historyInfo.getTime());
		if (recipientName == null)
			holder.content.setText("负责人: 客服");
		else  if(recipientName.equals("-1"))
			holder.content.setText("负责人: 售后主管");
		else {
			holder.content.setText("负责人: " + recipientName);
		}
		if(state == 5){
			holder.image.setImageResource(R.drawable.ellipse);
			holder.content.setTextColor(R.color.text_color_b);
		} else{
			holder.image.setImageResource(R.drawable.new_history);
			holder.content.setTextColor(R.color.text_color_yellow);
		}
		holder.item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("id", historyInfo.getId());
				Fragment fh = new FaultHandlingHistoryDetail();
				fh.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fh);
			}
		});
	}
	
	private void setCheck(ViewHolder holder, final HistoryInfo historyInfo){
		holder.title.setText(historyInfo.getTitle());
		holder.time.setText(historyInfo.getTime());
		holder.content.setText(historyInfo.getContent());
		int state = historyInfo.getStatue();
		if(state == 3){
			holder.image.setImageResource(R.drawable.ellipse);
		}else if(state == 2){
			holder.image.setImageResource(R.drawable.new_history);
		} 
		holder.item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("id", historyInfo.getId());
				Fragment fh = new CheckHistory();
				fh.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fh);
			}
		});
	}
	
	private void setSend(ViewHolder holder, final HistoryInfo historyInfo){
		holder.title.setText(historyInfo.getTitle());
		holder.time.setText(historyInfo.getTime());
		holder.content.setText(historyInfo.getContent());
		
		holder.item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("id", historyInfo.getId());
				Fragment fh = new SendAndReceiveHistory();
				fh.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fh);
			}
		});
	}
	class ViewHolder {
		TextView title;
		TextView time;
		TextView content;
		ImageView image;
		LinearLayout item;

		public ViewHolder(View view) {
			if(type == 0){
				title = (TextView) view.findViewById(R.id.history_content);
				time = (TextView) view.findViewById(R.id.history_time);
				content = (TextView) view.findViewById(R.id.fault_state);
				image = (ImageView) view.findViewById(R.id.ellipse_history);
				item = (LinearLayout) view.findViewById(R.id.fault_history_item);
			} else if(type == 1){
				title = (TextView) view.findViewById(R.id.title);
				time = (TextView) view.findViewById(R.id.time);
				content = (TextView) view.findViewById(R.id.contentfirst);
				image = (ImageView) view.findViewById(R.id.mainImg);
				item = (LinearLayout) view.findViewById(R.id.record_item);
			} else if(type == 2){
				title = (TextView) view.findViewById(R.id.title);
				time = (TextView) view.findViewById(R.id.time);
				content = (TextView) view.findViewById(R.id.contentfirst);
				image = (ImageView) view.findViewById(R.id.mainImg);
				item = (LinearLayout) view.findViewById(R.id.record_item);
			}
		}
	}

	public HistoryInfo getEntity(JSONObject joooo) {
		HistoryInfo historyInfo = null;
		switch (type) {
		case 0: historyInfo = getFaultEntity(joooo);break;
		case 1: historyInfo = getCheckEntity(joooo);break;
		case 2: historyInfo = getSendEntity(joooo);break;
		default:
			break;
		}
		
		return historyInfo;
	}
	
	private HistoryInfo getFaultEntity(JSONObject joooo){
		HistoryInfo historyInfo = new HistoryInfo();
		try {
			historyInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
			historyInfo.setId("null");
		}
		
		try{
			historyInfo.setTitle(joooo.getString("description"));
		}catch (Exception e) {
			historyInfo.setTitle("--");
		}

		try {
			historyInfo.setRecipientName(joooo.getString("recipientName"));
		} catch (Exception e) {
			historyInfo.setRecipientName(null);
		}
		try{
			historyInfo.setStatue(joooo.getInt("state"));
			
			if(historyInfo.getStatue() == 5){
				historyInfo.setContent("已修复");
			} else{
				historyInfo.setContent("维修中");
			}
		}catch (Exception e) {
			historyInfo.setStatue(0);
		}
		
		try{
			historyInfo.setTime(joooo.getString("time"));
		}catch (Exception e) {
			historyInfo.setTime("--");
		}
		return historyInfo;
	}

	private HistoryInfo getCheckEntity(JSONObject joooo){
		HistoryInfo historyInfo = new HistoryInfo();
		try {
			historyInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
			historyInfo.setId("null");
		}
		
		try {
			historyInfo.setTime(joooo.getString("applyTime"));
		} catch (Exception e) {
			historyInfo.setTime("--");
		}
		
		try {
			historyInfo.setStatue(joooo.getInt("state"));
			if(historyInfo.getStatue() == 3){
				historyInfo.setContent("不同意");
			}else if(historyInfo.getStatue() == 2){
				historyInfo.setContent("同意");
			} else{
				historyInfo.setContent("暂无数据");
			}
		} catch (Exception e) {
			historyInfo.setStatue(0);
			historyInfo.setContent("暂无数据");
		}
		try {
			historyInfo.setTitle(joooo.getString("name"));
		} catch (Exception e) {
			historyInfo.setTitle("--");
		}
		
		return historyInfo;
	}
	
	private HistoryInfo getSendEntity(JSONObject joooo){
		HistoryInfo historyInfo = new HistoryInfo();
		String taskType = "";
		try {
			historyInfo.setId(joooo.getString("id"));
		} catch (Exception e) {
			historyInfo.setId("null");
		}
		
		try {
			historyInfo.setTime(joooo.getString("fullTime"));
		} catch (Exception e) {
			historyInfo.setTime("--");
		}
		
		try {
			int task = joooo.getInt("task_type");
			if(task == 0){
				taskType = "派车任务:";
			}else if(task == 1){
				taskType = "接车任务:";
			}
		} catch (Exception e) {
			taskType = "任务:";
		}
		
		try {
			historyInfo.setStatue(joooo.getInt("type"));
			if(historyInfo.getStatue() == 1){
				historyInfo.setContent(taskType + "已转交");
			}else if(historyInfo.getStatue() == 0){
				historyInfo.setContent(taskType + "已完成");
			}
		} catch (Exception e) {
			historyInfo.setStatue(0);
			historyInfo.setContent(taskType + "暂无数据");
		}
		try {
			historyInfo.setTitle(joooo.getString("plate_number"));
		} catch (Exception e) {
			historyInfo.setTitle("--");
		}
		return historyInfo;
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
}
