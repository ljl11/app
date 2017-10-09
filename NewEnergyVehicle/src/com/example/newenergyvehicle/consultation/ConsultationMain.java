package com.example.newenergyvehicle.consultation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.ConsultationGroup;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePage;
import com.example.newenergyvehicle.homePage.HomePageGroup;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.VehicleMapInfo;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.notification.NotificationMain;
import com.example.newenergyvehicle.vehicle.VehicleMain;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ConsultationMain extends Fragment implements IXListViewListener,
		NeedRefresh {
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private XListView listView;
	private ConsultationAdapter mMsgAdapter;
	private Handler mHandler;
	private TextView today;
	private TextView week;
	private final int size = 10;
	public int page = 1;
	private TextView consult_ask;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater
				.inflate(R.layout.consultation_service, container, false);
		context = inflater.getContext();

		initView();
		addData(0);

		return view;
	}

	private void initView() {
		listView = (XListView) view.findViewById(R.id.list_consultation);
		listView.setPullLoadEnable(true);
		mMsgAdapter = new ConsultationAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setXListViewListener(this);
		mHandler = new Handler();
		consult_ask = (TextView) view.findViewById(R.id.consult_ask);

		consult_ask.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context)
						.changeView(new Consultation_askquestion());
			}
		});
		today=(TextView)view.findViewById(R.id.today);
		week=(TextView)view.findViewById(R.id.week);
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);
        today.setText(dateNowStr);
        week.setText(getWeekOfDate(d));
	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(NotificationAdapter.minToCaleander(System
				.currentTimeMillis()));
	}

	@Override
	public void onRefresh() {
		page = 1;
		onLoad();
		addData(0);
	}

	@Override
	public void onLoadMore() {
		page++;
		addData(1);
		onLoad();
	}

	public void addData(final int index) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Map param = new HashMap();
				param.put("curPage", page);
				param.put("limit", size);
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/postAndReply/postList"
								+ ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									if (index == 0) {
										mMsgAdapter
												.resetSingleData(new JSONArray(
														response));

									} else if (index == 1)
										mMsgAdapter.resetData(new JSONArray(
												response));

									mMsgAdapter.notifyDataSetChanged();
								} catch (Exception e) {
									if (index == 1)
										page = 1;
									Toast.makeText(context, "信息加载有误", 1).show();
								}
							}

							@Override
							public void onFailure(Throwable error) {
								if (index == 1)
									page = 1;
								super.onFailure(error);
							}
						});
			}
		}, 50);
	}

	@Override
	public void redresh() {
		onRefresh();
	}
	
	public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
