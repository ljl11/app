package com.example.newenergyvehicle.consultation;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.consultation.ConsultationMessageAdapter.ViewHolder;
import com.example.newenergyvehicle.faultHand.FaultHandHistoryDetail;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.salesPersonnel.FaultHandlingHistoryDetail;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ConsultationReply extends Fragment implements IXListViewListener{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	
	private ImageView head_image;
	private TextView message_content_username;
	private TextView message_content_time;
	private TextView message_content_replywords;
	private TextView reply_other_user;
	private TextView more_reply_info;
	private TextView the_reply_num;
	private EditText reply_content;
	private TextView reply_srue;
	private XListView listView;
	private List<ConsultationInfo> list;
	private ConsultationMessageAdapter messageAdapter;
	private ConsultationInfo consultation;
	private ImageView back;
	private TextView module_title;
	private String floorNum;
	private Handler mHandler;
	private final int size = 10;
	public int page = 1;
	String id = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.reply, null);
		context = inflater.getContext();
		
		
		initView();
		addData(0);
		return view;
	}
	
	private void initView() {
		listView = (XListView) view.findViewById(R.id.consultation_message_itme_info);
		back = (ImageView) view.findViewById(R.id.back);
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("咨询回复");
		head_image = (ImageView) view.findViewById(R.id.head_image);
		message_content_username = (TextView)view.findViewById(R.id.message_content_username);
		message_content_replywords = (TextView)view.findViewById(R.id.message_content_replywords);
		message_content_time = (TextView)view.findViewById(R.id.message_content_time);
		reply_other_user = (TextView) view.findViewById(R.id.reply_other_user);
		more_reply_info = (TextView) view.findViewById(R.id.more_reply_info);
		the_reply_num = (TextView) view.findViewById(R.id.the_reply_num);
		reply_content = (EditText) view.findViewById(R.id.reply_content);
		reply_srue = (TextView) view.findViewById(R.id.reply_srue);
		messageAdapter = new ConsultationMessageAdapter(getActivity());
		listView.setAdapter(messageAdapter);
		listView.setXListViewListener(this);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		mHandler = new Handler();
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu)context).back();
			}
		});
		
	}
	
	

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚 :" + System.currentTimeMillis());
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad();
				messageAdapter.resetSingleData(new JSONArray());
			}
		}, 1000);
	}
	public void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
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
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad();
				addData(0);
			}
		}, 1000);
	}
	
	public void addData(final int index){
		AsyncTask task = new AsyncTask() {

			@Override
			protected Object doInBackground(Object... arg0) {
				Map param =new HashMap();
				param.put("curPage", page);
				param.put("limit", size);
				param.put("id",id);
				HttpUtil.getClient().get(HttpUtil.getURL("api/postAndReply/commentList"+ParamUtil.mapToString(param)), new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						try {
							if (index == 0) {
								messageAdapter.resetSingleData(new JSONArray(response));

							} else if (index == 1)
								messageAdapter.resetData(new JSONArray(response));
							messageAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							if (index == 1)
								page = 1;
							Toast.makeText(context, "信息加载有误", 1).show();
						}
					}

					@Override
					public void onFailure(Throwable error) {
						if (index == 1)
							page = 1;
						super.onFailure(error);
					}
				});
				return null;
			}
			
		};
		task.execute();
		
	}
	private void setItem(ViewHolder holder, int position) {
		consultation = list.get(position);
		System.out.println(consultation);
		holder.message_content_username.setText(consultation.getPersonName());
		holder.message_content_replywords.setText(consultation.getContent());
		holder.message_content_time.setText(consultation.getTime());
//		holder.the_reply_num.setText(floorNum);
//		holder.more_reply_info.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Bundle bundle = new Bundle();
//				bundle.putString("id", consultation.getId());
//				FaultHandHistoryDetail fh = new FaultHandHistoryDetail();
//				fh.setArguments(bundle);
//				((DrawerLayoutMenu) context).changeView(fh);
//			}
//		});
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
