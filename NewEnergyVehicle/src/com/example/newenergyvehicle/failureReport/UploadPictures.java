package com.example.newenergyvehicle.failureReport;

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
import android.content.Intent;
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

public class UploadPictures extends Dialog {
	private Context context;

	private ImageView close;
	private TextView take_picture;// 拍照
	private TextView photo_album;// 相册

	private Handler mHandler;

	public UploadPictures(final Context context) {
		super(context);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		this.getWindow().getDecorView().setPadding(0, 0, 0, 0);
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.upload_pictures);
		close = (ImageView) findViewById(R.id.close);
		take_picture = (TextView) findViewById(R.id.take_picture);
		photo_album = (TextView) findViewById(R.id.photo_album);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (display.getWidth());
		getWindow().setAttributes(lp);
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, 400);

		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).changeView(new TransferStation());
			}
		});
		
		take_picture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				 Intent intent = new Intent();
//				 intent.setClass(getActivity(), CameraActivy.class);
//				 startActivityForResult(intent, 100);
			}
		});
		
		photo_album.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).changeView(new TransferStation());
			}
		});

	}

}
