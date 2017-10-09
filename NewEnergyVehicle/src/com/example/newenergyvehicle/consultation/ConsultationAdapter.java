package com.example.newenergyvehicle.consultation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConsultationAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private String operateId;
	private String type;
	private int countGood = 0;
	private int countBad = 0;
	private List<ConsultationInfo> list;
	private Handler handler = new Handler();
	
	public ConsultationAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<ConsultationInfo>();
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
			convertView = inflater.inflate(R.layout.new_consultation_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setItem(holder, position);
		holder.item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("consultation", list.get(index));
				Consultation_message consultation_message = new Consultation_message();
				consultation_message.setArguments(bundle);
				((DrawerLayoutMenu)context).changeView(consultation_message);
			}
		});
		holder.thumbs_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if (countGood % 2 == 0) {
							setGood();
							int goodCount = Integer.parseInt(list.get(position).getBadNum()) + 1;
							
						}
							
						Toast.makeText(context, "点赞", 1).show();
					}
				});
				
				}
		});
		holder.point_of.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						setBad();
						Toast.makeText(context, "点踩", 1).show();
					}
				});
				}
		});
		holder.com.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(context, "评论", 1).show();
					}
				});
				}
		});
		return convertView;
	}
	private void setGood() {
		RequestParams param =new RequestParams();
		param.put("operateId",operateId);
		param.put("operateObject", 1);
		param.put("type",type);
		operate(param);
	}
	private void setBad() {
		RequestParams param =new RequestParams();
		param.put("operateId", operateId);
		param.put("operateObject", 1);
		param.put("type",type);
		operate(param);
	}
	private void operate(RequestParams param) {
		try{
			HttpUtil.getClient().post(HttpUtil.getURL("api/postAndReply/goodBad"),param, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					try {
						String response1 = response;
					} catch (Exception e) {
						
						Toast.makeText(context, "信息加载有误", 1).show();
					}
				}

				@Override
				public void onFailure(Throwable error) {
					
					super.onFailure(error);
				}
			});
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	class ViewHolder {
		private ImageView head_image;
		private TextView username;
		private TextView title;
		private TextView content;
		private TextView time;
		private TextView give_the_thumbs_up_size;
		private TextView on_the_point_of_size;
		private TextView comment_size;
		private TextView btt; //置顶
		private TextView cream; //精
		private TextView hot; //热
		private LinearLayout thumbs_up;
		private LinearLayout item;
		private LinearLayout point_of;
		private LinearLayout com;
		public ViewHolder(View view) {
			head_image = (ImageView) view.findViewById(R.id.head_image);
			username = (TextView) view.findViewById(R.id.consultation_name);
			title = (TextView) view.findViewById(R.id.question_title);
			content = (TextView) view.findViewById(R.id.question_content);
			time = (TextView) view.findViewById(R.id.publication_time);
			give_the_thumbs_up_size = (TextView) view.findViewById(R.id.give_the_thumbs_up_size);
			on_the_point_of_size = (TextView) view.findViewById(R.id.on_the_point_of_size);
			comment_size = (TextView) view.findViewById(R.id.comment_size);
			btt = (TextView) view.findViewById(R.id.consultation_btt);
			cream = (TextView) view.findViewById(R.id.consultation_cream);
			item = (LinearLayout) view.findViewById(R.id.item);
			hot = (TextView) view.findViewById(R.id.consultation_hot);
			thumbs_up = (LinearLayout) view.findViewById(R.id.thumbs_up);
			point_of = (LinearLayout) view.findViewById(R.id.point_of);
			com = (LinearLayout) view.findViewById(R.id.com);
			
		}
	}
	
	private void setItem(ViewHolder holder, int position){
		holder.username.setText(list.get(position).getPersonName());
		holder.title.setText(list.get(position).getTitle());
		holder.content.setText(list.get(position).getContent());
		holder.give_the_thumbs_up_size.setText(list.get(position).getGoodNum());
		holder.on_the_point_of_size.setText(list.get(position).getBadNum());
		holder.comment_size.setText(list.get(position).getReplyCount());
		holder.time.setText(calculationTime(Long.valueOf(list.get(position).getTime())));
		String state = list.get(position).getState();
		if(state.equals("3") || state.equals("4") || state.equals("5")){
			holder.btt.setVisibility(View.VISIBLE);
		}
		if(state.equals("2") || state.equals("4") || state.equals("5")){
			holder.hot.setVisibility(View.VISIBLE);
		}
		if(state.equals("4")){
			holder.cream.setVisibility(View.VISIBLE);
		}
		
		holder.item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				((DrawerLayoutMenu)context).changeView(new Consultation_message());
			}
		});
	}
	
	public void resetData(JSONArray data){
		if(data!=null) {
			int length = data.length();
			for(int i = 0;i < length;i++) {
				try {
					list.add(getEntity(data.getJSONObject(i)));
				} catch (JSONException e) {
				}
			}
		}
	}
	
	public ConsultationInfo getEntity(JSONObject joooo) {
		ConsultationInfo consultationInfo = new ConsultationInfo();
		
		try{
			consultationInfo.setBadNum(joooo.getString("badNum"));
		}catch (Exception e) {
			consultationInfo.setBadNum("0");
		}
		
		try{
			consultationInfo.setGoodNum(joooo.getString("goodNum"));
		}catch (Exception e) {
			consultationInfo.setGoodNum("0");
		}
		
		try{
			consultationInfo.setReplyCount(joooo.getString("replyCount"));
		}catch (Exception e) {
			consultationInfo.setReplyCount("0");
		}
		
		try{
			consultationInfo.setTime(joooo.getString("postTime"));
		}catch (Exception e) {
			consultationInfo.setTime("暂无数据");
		}
		
		try{
			consultationInfo.setPersonName(joooo.getString("personName"));
		}catch (Exception e) {
			consultationInfo.setPersonName("暂无数据");
		}
		
		try{
			consultationInfo.setTitle(joooo.getString("title"));
		}catch (Exception e) {
			consultationInfo.setTitle("暂无数据");
		}
		
		try{
			consultationInfo.setContent(joooo.getString("content"));
		}catch (Exception e) {
			consultationInfo.setContent("暂无数据");
		}
		
		try{
			consultationInfo.setId(joooo.getString("postId"));
		}catch (Exception e) {
			consultationInfo.setId("暂无数据");
		}
		
		try{
			consultationInfo.setState(joooo.getString("state"));
		}catch (Exception e) {
			consultationInfo.setState("暂无数据");
		}
		return consultationInfo;
	}
	
	public void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
	}
	
	private String calculationTime(long old){
		StringBuilder time = new StringBuilder();
		long now = System.currentTimeMillis();
		long min = now - old;
		if(min <= 0)
			time.append("刚刚");
		else{
			int day = calculationDay(old, now);
			if(now <= 0){
				int hour = calculationHour(now, old);
				if(hour <= 0){
					int minuter = calculationMin(now, old);
					if(minuter <= 0){
						time.append("刚刚");
					}else{
						time.append(minuter);
						time.append("分钟");
						time.append("前");
					}
				}else{
					time.append(hour);
					time.append("小时");
					time.append("前");
				}
			}else{
				time.append(day);
				time.append("天");
				time.append("前");
			}
		}
		
		return time.toString();
	}
	
	private int calculationDay(long old, long now){
		return (int) ((now - old)/(1000 * 60 * 60 * 24));
	}
	
	private int calculationHour(long old, long now){
		return (int) ((now - old)/(1000 * 60 * 60));
	}
	
	private int calculationMin(long old, long now){
		return (int) ((now - old)/(1000 * 60 * 60));
	}
}
