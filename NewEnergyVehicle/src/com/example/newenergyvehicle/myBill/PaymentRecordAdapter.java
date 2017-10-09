package com.example.newenergyvehicle.myBill;

import java.util.ArrayList;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.myBill.MyBillListAdapter.ViewHolder;

public class PaymentRecordAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<MyBill> list;
	
	public PaymentRecordAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<MyBill>();
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
	
	public void setItem(ViewHolder holder, final int position){
		final MyBill myBill = list.get(position);
		holder.contractedMoney.setText(myBill.getAmountOfMoney());
		String[] date = myBill.getContractTime().split(" ");
		holder.contractTime.setText(date[0]);
		holder.currentPeriod.setText(myBill.getCurrentPeriod());
		holder.period.setText(myBill.getPeriod());
		holder.billItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Fragment paymentDetail = new PaymentDetail();
				Bundle bundle = new Bundle();
				bundle.putSerializable("paymentDetail", myBill);
				paymentDetail.setArguments(bundle);
				changeView(paymentDetail);
			}
		});
	}
	
	public void resetSingleData(JSONArray data) {
		list.clear();
		resetData(data);
	}
	
	public void resetData(JSONArray data) {
		if (data != null) {
			int length = data.length();
			for (int i = 0; i < length; i++) {
				try {
					list.add(getEntity(data.getJSONObject(i)));
				} catch (JSONException e) {
				}
			}
		}
	}
	
	public MyBill getEntity(JSONObject joooo){
		MyBill myBill = new MyBill();
		try {
			myBill.setAmountOfMoney(joooo.getString("paymentAmount"));
		} catch (JSONException e) {
			myBill.setAmountOfMoney("--");
			e.printStackTrace();
		}
		try {
			myBill.setContractTime(joooo.getString("paymentDate"));
		} catch (JSONException e) {
			myBill.setContractTime("--");
			e.printStackTrace();
		}
		try {
			myBill.setPeriod(joooo.getString("periodsNumber"));
		} catch (JSONException e) {
			myBill.setPeriod("--");
			e.printStackTrace();
		}
		try {
			myBill.setCurrentPeriod(joooo.getString("currentPeriod"));
		} catch (JSONException e) {
			myBill.setCurrentPeriod("--");
			e.printStackTrace();
		}
		try {
			myBill.setDescription(joooo.getString("remark"));
		} catch (JSONException e) {
			myBill.setDescription("--");
			e.printStackTrace();
		}
		return myBill;
	}
	
	class ViewHolder{
		TextView contractTime;
		TextView period;
		TextView currentPeriod;
		TextView contractedMoney;
		LinearLayout billItem;
		public ViewHolder(View view) {
			period = (TextView)view.findViewById(R.id.pr_period);
			currentPeriod = (TextView)view.findViewById(R.id.pr_current_period);
			contractedMoney = (TextView)view.findViewById(R.id.contracted_money);
			contractTime = (TextView)view.findViewById(R.id.contract_time);
			billItem = (LinearLayout)view.findViewById(R.id.payment_record_list_item);
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

			convertView = inflater.inflate(R.layout.payment_record_list_item, null);
			holder = new ViewHolder(convertView);
			setItem(holder, position);
			convertView.setTag(holder);
		return convertView;
	}
	
	private void changeView(Fragment fragment) {
		((DrawerLayoutMenu) context).changeView(fragment);
	}

}