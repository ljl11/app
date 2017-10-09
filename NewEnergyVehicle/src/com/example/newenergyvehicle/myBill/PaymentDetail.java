package com.example.newenergyvehicle.myBill;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;

public class PaymentDetail extends Fragment{
	private View view;
	private Context context;
	private Handler handler = new Handler();
	private ImageView back;
	private TextView headContent;
	private TextView periods;
	private TextView paidAmount;
	private TextView receivingUnit;
	private TextView createTime;
	private TextView paidDescription;
	private MyBill paymentDetail;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.payment_detail, container, false);
		context = inflater.getContext();

		initView();
		initDate();
		return view;
	}

	private void initDate() {
		if(getArguments()!=null){
			paymentDetail = (MyBill) getArguments().getSerializable("paymentDetail");
		}
		String sPeriods = "第"+paymentDetail.getCurrentPeriod()+"/"+paymentDetail.getPeriod()+"期";
		periods.setText(sPeriods);
		paidAmount.setText(paymentDetail.getAmountOfMoney());
//		receivingUnit.setText("");
		createTime.setText(paymentDetail.getContractTime());
		paidDescription.setText(paymentDetail.getDescription());
	}

	private void initView() {
		back = (ImageView) view.findViewById(R.id.back);
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("缴费");
		periods = (TextView)view.findViewById(R.id.pd_periods);
		paidAmount = (TextView)view.findViewById(R.id.paid_amount);
		receivingUnit = (TextView)view.findViewById(R.id.receiving_unit);
		createTime = (TextView)view.findViewById(R.id.create_time);
		paidDescription = (TextView)view.findViewById(R.id.paid_description);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
	}
}
