package com.example.newenergyvehicle.consultation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;

public class ConsultationMessageItemAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<ConsultationMessageItemInfo> list;
	private ConsultationMessageItemInfo consultationInfo;

	public ConsultationMessageItemAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<ConsultationMessageItemInfo>();
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
			convertView = inflater.inflate(R.layout.consultation_message_item_info, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			setItem(holder, position);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder{
		TextView usernameBefore;
		TextView passerbyName;
		TextView landlordContent;
		TextView landlord;
		TextView landlord_reply;
		
		public ViewHolder(View view) {
			landlord = (TextView) view.findViewById(R.id.Landlord_louzhu);
			usernameBefore = (TextView) view.findViewById(R.id.more_reply_username_before_new);
			passerbyName = (TextView) view.findViewById(R.id.more_reply_passerby_name_new);
			landlordContent = (TextView) view.findViewById(R.id.more_reply_landlord_content_new);
			landlord_reply = (TextView) view.findViewById(R.id.Landlord_load);
		}
	}

	private void setItem(ViewHolder holder, int position){
		consultationInfo = list.get(position);
		holder.usernameBefore.setText(consultationInfo.getLandlordName());
		holder.passerbyName.setText(consultationInfo.getReplyUserName());
		holder.landlordContent.setText(consultationInfo.getReplyContent());
	}
	
	public void resetData(JSONArray data){
		if(data!=null) {
			int length = data.length();
			for(int i = 0;i < length;i++) {
				try {
					list.add(getEntity(data.getJSONObject(i)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ConsultationMessageItemInfo getEntity(JSONObject joooo) {	
		ConsultationMessageItemInfo notification = new ConsultationMessageItemInfo();
		try {
			notification.setId(joooo.getString("id"));
		} catch (Exception e) {
			notification.setId(null);
		}
		try {
			notification.setLandlordName(joooo.getString("id"));
		} catch (Exception e) {
			notification.setLandlordName(null);
		}
		try {
			notification.setReplyUserName(joooo.getString(""));
		} catch (Exception e) {
			notification.setReplyUserName("--");
		}
		try {
			notification.setReplyContent(joooo.getString(""));
		} catch (Exception e) {
			notification.setReplyContent("--");
		}
		try {
			notification.setReplyId(joooo.getString(""));
		} catch (Exception e) {
			notification.setReplyId(null);
		}
		return notification;
	}

	public void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
	}

}