package com.example.newenergyvehicle.emergencyVehicle;

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

public class EmergencyVehicleMain extends Fragment implements OnClickListener {
	private View view;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = inflater.getContext();
		view = inflater.inflate(R.layout.emergency_vehicle_main, container,
				false);

		view.findViewById(R.id.arrangement).setOnClickListener(this);
		view.findViewById(R.id.recycle).setOnClickListener(this);
		/*view.findViewById(R.id.assign).setOnClickListener(this);*/
		view.findViewById(R.id.arrange_history).setOnClickListener(this);
		view.findViewById(R.id.recovery_history).setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		Fragment fragment = null;
		Bundle bundle = new Bundle();
		switch (id) {
		case R.id.arrangement:
			fragment = new Arrangement();
			break;
		case R.id.recycle:
			fragment = new Recovery();
			break;
		/*case R.id.assign:
			fragment = new CarAssign();
			break;*/
		case R.id.arrange_history:
			bundle.putString("type","0");
			bundle.putString("url", "api/emergencyCarManage/emergencyCarsHistory");
			fragment = new ArrangementList();
			fragment.setArguments(bundle);
			break;
		case R.id.recovery_history:
			bundle.putString("type","1");
			bundle.putString("url", "api/emergencyCarManage/emergencyRecoveryHistorys");
			fragment = new ArrangementList();
			fragment.setArguments(bundle);
		default:
			break;
		}

		if (fragment == null)
			Common.dialogMark(getActivity(), null, "网络有误");
		else
			((DrawerLayoutMenu) context).changeView(fragment);
	}
}
