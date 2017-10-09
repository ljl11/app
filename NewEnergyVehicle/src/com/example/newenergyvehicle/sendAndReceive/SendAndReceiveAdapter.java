package com.example.newenergyvehicle.sendAndReceive;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.myWork.TroubleInfo;
import com.example.newenergyvehicle.personal.Personal;
import com.example.newenergyvehicle.salesPersonnel.FaultHanding;

public class SendAndReceiveAdapter  extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<SendAndReceiveInfo> list;
	
	public int getMax(){
		return list.size();
	}
	public SendAndReceiveAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<SendAndReceiveInfo>();
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
			convertView = inflater.inflate(R.layout.trouble_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		setItem(holder, position);
		return convertView;
	}

	private void setItem(ViewHolder holder, int position){
		final SendAndReceiveInfo sendAndReceive = list.get(position);
		holder.plateNumber.setText(list.get(position).getPlateNumber());
		holder.time.setText(list.get(position).getTime());
		holder.trouble_detail.setText(list.get(position).getTrouble_detail());
		holder.trouble_itme.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("sendAndReceive",sendAndReceive);
				SendAndReceive sar = new SendAndReceive();
				sar.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(sar);
			}
		});
	}
	class ViewHolder {
		TextView plateNumber;
		TextView time;
		TextView trouble_detail;
		LinearLayout trouble_itme;
		public ViewHolder(View view){
			plateNumber = (TextView) view.findViewById(R.id.plateNumber);
			time = (TextView) view.findViewById(R.id.time);
			trouble_detail = (TextView) view.findViewById(R.id.trouble_detail);
			trouble_itme = (LinearLayout) view.findViewById(R.id.trouble_itme);
		}
	}
	
	public SendAndReceiveInfo getEntity(JSONObject joooo) {
		SendAndReceiveInfo sendAndReceiveInfo = new SendAndReceiveInfo();

		return sendAndReceiveInfo;
	}
	
	public void resetSingleData(JSONArray data){
		list.clear();
		resetData(data);
	}
	
	public static String minToCaleander(long now){
		DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);

		return formatter.format(calendar.getTime());
	}
}