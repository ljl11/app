package com.example.newenergyvehicle.salesPersonnel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.util.Common;

/**
 * Created by Administrator on 2017/3/5.
 */

public class ShuttleMission extends Fragment implements OnClickListener {
	private View view;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = inflater.getContext();
		view = inflater.inflate(R.layout.shuttle_mission, container,
				false);

		view.findViewById(R.id.pick_up_car).setOnClickListener(this);
		view.findViewById(R.id.send_car).setOnClickListener(this);
//		view.findViewById(R.id.arrange_car_history);

		return view;
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		Fragment fragment = null;
		switch (id) {
		case R.id.pick_up_car:
			fragment = new VehiclePickUp();
			break;
		case R.id.send_car:
			fragment = new VehicleSend();
			break;
//		case R.id.arrange_car_history:
			
		default:
			break;
		}

		if (fragment == null)
			Common.dialogMark(getActivity(), null, "网络有误");
		else
			((DrawerLayoutMenu) context).changeView(fragment);
	}
}