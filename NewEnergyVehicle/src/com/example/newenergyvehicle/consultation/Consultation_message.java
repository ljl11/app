package com.example.newenergyvehicle.consultation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Consultation_message extends Fragment implements IXListViewListener{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	public static LinearLayout infomation_reply_layout;
	private ImageView head_image;
	private TextView original_poster;
	private TextView release_time;
	private TextView release_title;
	private TextView consultation_btt1;
	private TextView consultation_fine1;
	private TextView consultation_hot1;
	private TextView release_content;
	private XListView listView;
	public static EditText reply;
	public static ImageView sure_reply;
	private List<ConsultationInfo> list;
	private ConsultationMessageAdapter messageAdapter;
	private ConsultationInfo consultation_message;
	private ImageView back;
	private Handler mHandler;
	private final int size = 10;
	public int page = 1;
	String postId = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.consultation_message, container, false);
		context = inflater.getContext();
		
		initView();
		addData(0);
		return view;
	}
	
	private void initView() {
		listView = (XListView) view.findViewById(R.id.list_consultation_message);
		back = (ImageView) view.findViewById(R.id.back);
		head_image = (ImageView) view.findViewById(R.id.head_image);
		original_poster = (TextView) view.findViewById(R.id.original_poster);
		release_time = (TextView) view.findViewById(R.id.release_time);
		release_title = (TextView) view.findViewById(R.id.release_title);
		consultation_btt1 = (TextView) view.findViewById(R.id.consultation_btt1);
//		consultation_fine1 = (TextView) view.findViewById(R.id.consultation_fine1);
		consultation_hot1 = (TextView) view.findViewById(R.id.consultation_hot1);
		release_content = (TextView) view.findViewById(R.id.release_content);
		reply  = (EditText) view.findViewById(R.id.reply_content);
		sure_reply = (ImageView) view.findViewById(R.id.reply_srue);
		infomation_reply_layout = (LinearLayout) view.findViewById(R.id.infomation_reply_layout);
		listView.setPullLoadEnable(false);
		listView.setFooterDividersEnabled(false);
		listView.setPullRefreshEnable(true);
		messageAdapter = new ConsultationMessageAdapter(getActivity());
		listView.setAdapter(messageAdapter);
		listView.setXListViewListener(this);
		mHandler = new Handler();
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu)context).back();
			}
		});
		
		sure_reply.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!reply.getText().toString().equals("")&&reply.getText()!=null)
					addReply();
				else
					Common.dialogMark(getActivity(), null, "请输入评论内容");
			}
		});
		
		if (getArguments() != null) {
			consultation_message = (ConsultationInfo) getArguments().getSerializable("consultation");
			original_poster.setText(consultation_message.getPersonName());
			release_title.setText(consultation_message.getTitle());
			release_time.setText(calculationTime(Long.valueOf(consultation_message.getTime())));
			release_content.setText(consultation_message.getContent());
			postId = consultation_message.getId();
			String state = consultation_message.getState();
			if(state.equals("3") || state.equals("4") || state.equals("5")){
				consultation_btt1.setVisibility(View.VISIBLE);
			}
			if(state.equals("2") || state.equals("4") || state.equals("5")){
				consultation_hot1.setVisibility(View.VISIBLE);
			}
			if(state.equals("4")){
				consultation_fine1.setVisibility(View.VISIBLE);
			}
		}
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
				addData(0);
			}
		}, 50);
	}
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad();
				addData(1);
			}
		}, 50);
	}
	
	public void addData(final int index){
				Map param =new HashMap();
				param.put("id",postId);
				try{
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
							Common.dialogMark(getActivity(), null, "信息加载有误");
						}
					}

					@Override
					public void onFailure(Throwable error) {
						if (index == 1)
							page = 1;
						super.onFailure(error);
					}
				});
				}catch (Exception e) {
					e.printStackTrace();
				}
	}
	

	private void addReply() {
		String content = reply.getText().toString();
		RequestParams params = new RequestParams();
		params.put("post", postId);
		params.put("content", content);
		
		try {
			HttpUtil.getClient().post(
					HttpUtil.getURL("api/postAndReply/comment"), params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							try {
								String boo = new JSONObject(response).getString("status").toString();
								if(boo.equals("true")){
									closeKeyBoard();
									reply.setText("");
									onRefresh();
								}
								else
									Toast.makeText(context, "回复失败",1).show();
							} catch (Exception e) {
								Toast.makeText(context, "信息加载有误", 1).show();
							}
						}

						@Override
						public void onFailure(Throwable error) {
							Toast.makeText(context, "回复失败", 1);
							super.onFailure(error);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
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
	private void closeKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(getActivity().getWindow()
					.getDecorView().getWindowToken(), 0);
		}
	}
}
