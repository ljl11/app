package com.example.newenergyvehicle.consultation;

import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.ConsultationGroup;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePageGroup;
import com.example.newenergyvehicle.map.MapActivity;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.personal.Personal;
import com.example.newenergyvehicle.pile.PileActivity;
import com.example.newenergyvehicle.vehicle.VehicleMain;
import com.example.util.Common;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConsultationActivity extends Fragment implements OnClickListener{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	
	private TextView consultation;
	private TextView pile;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.consultation, null);
		context = inflater.getContext();
		
		consultation = (TextView)view.findViewById(R.id.consultation);
		pile = (TextView) view.findViewById(R.id.pile);
		consultation.setOnClickListener(this);
		pile.setOnClickListener(this);
		Common.clickView(consultation);
		return view;
	}
	
	@Override
	public void onClick(View arg0) {
		Fragment fragment = null;
		switch(arg0.getId()){
		case R.id.consultation: fragment = new ConsultationMain() ;break;
		case R.id.pile: /*fragment = new */ ;break;
		default:
			break;
		}
		
		if(fragment == null)
			Common.dialogMark(getActivity(), null, "网络异常");
		else
			((DrawerLayoutMenu) context).changeView(fragment);
	}
}
