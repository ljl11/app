package com.example.newenergyvehicle.serviceReview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.util.Common;

public class ServiceReviewDetail extends Fragment implements OnClickListener{
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private TextView module_title;
	private RelativeLayout vehicle_position;
	private RelativeLayout vehicle_info;
	private ImageView back;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.service_review_detail, null);
		context = inflater.getContext();
		
		module_title = (TextView) view.findViewById(R.id.module_title);
		module_title.setText("服务评价列表");
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		return view;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
