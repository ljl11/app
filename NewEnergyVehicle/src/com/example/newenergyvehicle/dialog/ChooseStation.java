package com.example.newenergyvehicle.dialog;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.salesPersonnel.ArrangeCar;
import com.example.newenergyvehicle.salesPersonnel.TransferStation;
import com.example.util.HttpUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseStation extends Dialog implements IXListViewListener {
	private Context context;

	private ImageView close;
	private XListView listView;
	private TextView station;//维修站选择
	private TextView chooseStation;
	private ChooseStationAdapter mMsgAdapter;
	private Handler mHandler;
	private final int size = 10;
	public int page = 1;
	private int maxSize = -1;
	private TextView title;

	public ChooseStation(final Context context) {
		super(context);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		this.getWindow().getDecorView().setPadding(0, 0, 0, 0);
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.station_checking);
		close = (ImageView) findViewById(R.id.close);
		station = (TextView) findViewById(R.id.station);
		chooseStation = (TextView) findViewById(R.id.station_name);
		title =(TextView) findViewById(R.id.plateNumber);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (display.getWidth());
		lp.height= (int) (display.getHeight()/2);
		getWindow().setAttributes(lp);
		getWindow().setGravity(Gravity.BOTTOM);
		initView();
		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}

	private void initView() {
		listView = (XListView) findViewById(R.id.station_lv);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		mMsgAdapter = new ChooseStationAdapter(context);
		mMsgAdapter.setParent(this);
		listView.setAdapter(mMsgAdapter);
		listView.setXListViewListener(this);
		mHandler = new Handler();
	}

	public ChooseStationAdapter getAdapter(){
		return mMsgAdapter;
	}
	
	
	public void diss(){
		this.dismiss();
	}
	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
}
