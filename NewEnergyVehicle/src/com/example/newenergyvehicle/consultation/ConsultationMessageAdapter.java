package com.example.newenergyvehicle.consultation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.consultation.ConsultationAdapter.ViewHolder;
import com.example.newenergyvehicle.failureReport.PhotoAdapter;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.notification.Notification;
import com.example.newenergyvehicle.salesPersonnel.ShuttleMissionAdapter;
import com.example.newenergyvehicle.search.SearchPlatenumber;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.InputManager.InputManager;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.Utility;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConsultationMessageAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<ConsultationInfo> list;
	private ConsultationInfo con;
	private String floorNum;
	private EditText replt_content;
	private ImageView sure_reply;
	private LinearLayout infomation_reply_layout;
	private final int size = 10;
	public int page = 1;
	private int count = 0;
	private String post;
	private String content;
	private Handler mHandler;
	private ConsultationMessageItemAdapter adapter;

	public ConsultationMessageAdapter(Context context) {
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
			convertView = inflater
					.inflate(R.layout.consultation_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setItem(holder, position);
		floorNum = getFloorNum(index);

		return convertView;
	}

	private String getFloorNum(int index) {
		StringBuffer sb = new StringBuffer();
		sb.append("第");
		sb.append(index + 1);
		sb.append("楼");
		return sb.toString();
	}

	private void openInputManager() {
		replt_content = Consultation_message.reply;
		replt_content.setFocusable(true);
		replt_content.setFocusableInTouchMode(true);
		replt_content.requestFocus();
		infomation_reply_layout = Consultation_message.infomation_reply_layout;
		infomation_reply_layout.setVisibility(View.VISIBLE);
		InputMethodManager imm = (InputMethodManager) replt_content
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		sure_reply = Consultation_message.sure_reply;
		sure_reply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				addReply();
			}
		});

	}

	private void addReply() {
		content = replt_content.getText().toString();
		RequestParams params = new RequestParams();
		params.put("post", post);
		params.put("content", content);
		
		try {
			HttpUtil.getClient().post(
					HttpUtil.getURL("api/postAndReply/comment"), params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							try {
								String boo = new JSONObject(response).getString("status").toString();
								if(boo.equals("true"))
									closeInputManager();
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

	private void closeInputManager() {
		replt_content = Consultation_message.reply;
		replt_content.setFocusable(true);
		replt_content.setFocusableInTouchMode(true);
		replt_content.requestFocus();
		infomation_reply_layout = Consultation_message.infomation_reply_layout;
		infomation_reply_layout.setVisibility(View.GONE);
		InputMethodManager imm = (InputMethodManager) replt_content
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(replt_content.getWindowToken(), 0);
	}

	private void setItem(ViewHolder holder, int position) {
		con = list.get(position);
		holder.message_content_username.setText(con.getPersonName());
		holder.message_content_replywords.setText(con.getContent());
		String time = con.getReplyTime().replace("\n\t\t", " ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date=sdf.parse(time);
			holder.message_content_time.setText(calculationTime(date.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		holder.the_reply_num.setText(floorNum);
 		post = con.getPost();
//		holder.reply_other_user.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				if (count % 2 == 0) {
//					openInputManager();
//					count++;
//				} else {
//					closeInputManager();
//					count--;
//				}
//
//			}
//		});
		holder.commentItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				addData(con.getId());
			}
		});
	}

	class ViewHolder implements IXListViewListener {
//		XListView listView;
		TextView message_content_username;
		TextView message_content_time;
		TextView message_content_replywords;
		TextView reply_other_user;
		LinearLayout commentItem;
//		TextView more_reply_info;
//		TextView the_reply_num;
//		private LinearLayout consultationMessageItem;

		public ViewHolder(View view) {
//			listView = (XListView) view
//					.findViewById(R.id.consultation_message_itme_info);
			message_content_username = (TextView) view
					.findViewById(R.id.message_content_username);
			message_content_replywords = (TextView) view
					.findViewById(R.id.message_content_replywords);
			message_content_time = (TextView) view
					.findViewById(R.id.message_content_time);
			reply_other_user = (TextView) view
					.findViewById(R.id.reply_other_user);
			commentItem = (LinearLayout)view.findViewById(R.id.comment_item);
//			more_reply_info = (TextView) view
//					.findViewById(R.id.more_reply_info);
//			the_reply_num = (TextView) view.findViewById(R.id.the_reply_num);
//			consultationMessageItem = (LinearLayout) view
//					.findViewById(R.id.consultation_message_item);

			adapter = new ConsultationMessageItemAdapter(context);
//			listView.setAdapter(adapter);
//			listView.setXListViewListener(this);
//			listView.setPullRefreshEnable(false);
//			listView.setPullLoadEnable(false);
			mHandler = new Handler();
//			onRefresh();
//			Utility.setListViewHeightBasedOnChildren(listView);
		}

		@Override
		public void onRefresh() {
			onLoad();
//			addData(0);
		}

		@Override
		public void onLoadMore() {
			onLoad();
//			addData(1);
		}

		private void onLoad() {
//			listView.stopRefresh();
//			listView.stopLoadMore();
//			listView.setRefreshTime("刚刚 :" + System.currentTimeMillis());
		}

	}
	

	public void addData(final String id) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
//				ConsultationInfo info = list.get(index);
				Map param = new HashMap();
				param.put("id", id);
				String a = id;
				HttpUtil.getClient().get(HttpUtil.getURL("api/postAndReply/comments/"+id)
						, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								String b = response;
//								try {
//									if (index == 0) {
//										adapter.resetSingleData(new JSONArray(
//												response));
//									} else if (index == 1)
//										adapter.resetData(new JSONArray(
//												response));
//									adapter.notifyDataSetChanged();
//								} catch (Exception e) {
//									if (index == 1)
//										page = 1;
//									Toast.makeText(context, "信息加载有误", 1)
//											.show();
//								}
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		}, 50);
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
		} else {
			list.clear();
		}
	}

	public ConsultationInfo getEntity(JSONObject data) {
		ConsultationInfo notification = new ConsultationInfo();
		try {
			notification.setPersonName(data.getString("replyPerson"));
		} catch (Exception e) {
			notification.setPersonName("暂无数据");
		}

		try {
			notification.setGoodNum(data.getString("goodNum"));
		} catch (Exception e) {
			notification.setGoodNum("暂无数据");
		}

		try {
			notification.setPost(data.getString("post"));
		} catch (Exception e) {
			notification.setPost("");
		}
		try {
			notification.setReplyCount(data.getString("replyCount"));
		} catch (Exception e) {
			notification.setReplyCount("暂无数据");
		}
		try {
			notification.setId(data.getString("id"));
		} catch (Exception e) {
			notification.setId("null");
		}
		try {
			notification.setContent(data.getString("content"));
		} catch (Exception e) {
			notification.setContent("暂无数据");
		}
		try {
			notification.setState(data.getString("state"));
		} catch (Exception e) {
			notification.setState("0");
		}
		try {
			notification.setReplyTime(data.getString("replyTime"));
		} catch (Exception e) {
			notification.setReplyTime("暂无数据");
		}
		return notification;
	}

	public void resetSingleData(JSONArray data) {
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
