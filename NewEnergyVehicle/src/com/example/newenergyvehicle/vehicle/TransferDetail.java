package com.example.newenergyvehicle.vehicle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;

public class TransferDetail extends Fragment {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private TextView module_title;
	private ImageView back;
	private TransferInfo transferInfo;
	private TextView plateNumber;
	private TextView provider;
	private TextView providerUnit;
	private TextView receiver;
	private TextView receiverUnit;
	private TextView time;
	private TextView backButton;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		view = inflater.inflate(R.layout.transfer_detail, container, false);
		context = inflater.getContext();

		initView();
		if(getArguments()!=null)
		    DisplayContent();
		return view;
	}

	private void initView() {
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("交接记录详情");
		back = (ImageView) view.findViewById(R.id.back);

		plateNumber = (TextView) view.findViewById(R.id.transfer_plate_number);
		provider = (TextView) view.findViewById(R.id.provider);
		providerUnit = (TextView) view.findViewById(R.id.provider_unit);
		receiver = (TextView) view.findViewById(R.id.receiver);
		receiverUnit = (TextView) view.findViewById(R.id.receiver_unit);
		time = (TextView) view.findViewById(R.id.time);
		backButton = (TextView) view.findViewById(R.id.Emergency_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
	}

	private void DisplayContent() {
		transferInfo = (TransferInfo) getArguments().getSerializable("info");
		plateNumber.setText(transferInfo.getPlateNumber());
		provider.setText(transferInfo.getProviderName());
		providerUnit.setText(transferInfo.getProviderUnitName());
		receiver.setText(transferInfo.getReceiverName());
		receiverUnit.setText(transferInfo.getReceiverUnitName());
		time.setText(transferInfo.getHandOverTime());
	}
}
