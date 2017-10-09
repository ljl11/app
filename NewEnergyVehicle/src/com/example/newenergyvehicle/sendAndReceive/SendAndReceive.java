package com.example.newenergyvehicle.sendAndReceive;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.dialog.ChooseStation;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.myWork.TroubleInfo;
import com.example.newenergyvehicle.salesPersonnel.ArrangeCar;
import com.example.util.timeDialog.DateDialog;

public class SendAndReceive extends Fragment {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private TextView module_title;
	private TextView emergency_submit;// 同意
	private EditText sendTime;
	private DatePickerDialog dateDialog;
	private SendAndReceive sendAndReceive;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		sendAndReceive = (SendAndReceive) getArguments().getSerializable("sendAndReceive");
		
		this.inflater = inflater;
		view = inflater.inflate(R.layout.vehicle_send, null);
		context = inflater.getContext();
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("派车任务");
		emergency_submit = (TextView) view.findViewById(R.id.emergency_submit);
		
		sendTime = (EditText) view.findViewById(R.id.send_time);
	
		
		
		sendTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dateDialog.show();
			}
		});
		emergency_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((DrawerLayoutMenu) context).changeView(new MyWork());
			}
		});
		
		dateDialog = DateDialog.getDateDialog(context);
		dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				sendTime.setText(DateDialog.getTime());
			}
		});
		return view;
	}
}
