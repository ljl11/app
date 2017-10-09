package com.example.newenergyvehicle.myBill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.util.Double.DoubleUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MyBillListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<MyBill> list;
	
	public MyBillListAdapter(Context context){
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
	
	@SuppressLint({ "ResourceAsColor", "NewApi" })
	public void setItem(ViewHolder holder, final int position){
		final MyBill myBill = list.get(position);
		holder.amountOfMoney.setText(myBill.getAmountOfMoney());
		holder.contractNumber.setText(myBill.getContractNumber());
		holder.period.setText(myBill.getPeriod());
		holder.currentPeriod.setText(myBill.getCurrentPeriod());
		if(myBill.getState()==0){
			holder.cost.setText("已欠费用（元）：");
			holder.amountOfMoney.setTextColor(context.getResources().getColor(R.color.orange_font));
			holder.background.setBackground(context.getResources().getDrawable(R.drawable.orange_back));
		}else if(myBill.getState()==1){
			holder.cost.setText("需交费用（元）：");
			holder.amountOfMoney.setTextColor(context.getResources().getColor(R.color.yellow_font));
			holder.background.setBackground(context.getResources().getDrawable(R.drawable.yellow_back));
		}else if(myBill.getState()==2){
			holder.cost.setText("可缴费用（元）：");
			holder.amountOfMoney.setTextColor(context.getResources().getColor(R.color.button));
			holder.background.setBackground(context.getResources().getDrawable(R.drawable.green_back));
		}
		holder.billItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Fragment fa = new BillDetail();
				Bundle bundle = new Bundle();
				bundle.putSerializable("billDetail", myBill);
				fa.setArguments(bundle);
				changeView(fa);
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
			myBill.setId(joooo.getString("contractId"));
		} catch (JSONException e) {
			myBill.setId(null);
			e.printStackTrace();
		}
		try {
			BigDecimal big = new BigDecimal(joooo.getString("needPayInThisCycle"));
			double money = DoubleUtil.get2Double(big.doubleValue());
			myBill.setAmountOfMoney(String.valueOf(money));
		} catch (JSONException e) {
			myBill.setAmountOfMoney("--");
			e.printStackTrace();
		}
		try {
			myBill.setContractNumber(joooo.getString("contractNo"));
		} catch (JSONException e) {
			myBill.setContractNumber("--");
			e.printStackTrace();
		}
		try {
			myBill.setPeriod(joooo.getString("currentPeriod"));
		} catch (JSONException e) {
			myBill.setPeriod("--");
			e.printStackTrace();
		}
		try {
			myBill.setCurrentPeriod(joooo.getString("periodsNumber"));
		} catch (JSONException e) {
			myBill.setCurrentPeriod("--");
			e.printStackTrace();
		}
		try {
			myBill.setState(joooo.getInt("payState"));
		} catch (JSONException e) {
			myBill.setState(null);
			e.printStackTrace();
		}
		return myBill;
	}
	
	class ViewHolder{
		TextView contractNumber;
		TextView period;
		TextView currentPeriod;
		TextView cost;
		TextView amountOfMoney;
		LinearLayout billItem;
		RelativeLayout background;
		public ViewHolder(View view) {
			contractNumber = (TextView)view.findViewById(R.id.contract_number);
			period = (TextView)view.findViewById(R.id.period);
			currentPeriod = (TextView)view.findViewById(R.id.current_period);
			cost = (TextView)view.findViewById(R.id.cost);
			amountOfMoney = (TextView)view.findViewById(R.id.amount_of_money);
			billItem = (LinearLayout)view.findViewById(R.id.bill_itme);
			background = (RelativeLayout)view.findViewById(R.id.bgimg);
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

			convertView = inflater.inflate(R.layout.my_bill_list_item, null);
			holder = new ViewHolder(convertView);
			setItem(holder, position);
			convertView.setTag(holder);
		return convertView;
	}
	
	private void changeView(Fragment fragment) {
		((DrawerLayoutMenu) context).changeView(fragment);
	}

}
