package com.example.newenergyvehicle.salesPersonnel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Decoder.BASE64Decoder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.emergencyVehicle.Recovery;
import com.example.newenergyvehicle.failureReport.CameraActivy;
import com.example.newenergyvehicle.failureReport.ImageViewPaper;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.myWork.MyWorkHistory;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.base64.Base64Util;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.Utility;
import com.example.util.refreshListView.XListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FaultHandlingHistoryDetail extends Fragment implements
		OnClickListener {

	private View view;
	private Context context;
	private LayoutInflater mInflater;
	private TextView headContent;
	private ImageView back;
	private ImageView callPhone;
	private TextView faultApplicant;
	private TextView faultNumber;
	private TextView faultTime;
	private TextView faultDescribe;
	private TextView maintenanceComplete;
	private TextView faultState;
	private TextView emergencyRecoveryVehicle;
	private TextView plateNum;
	private XListView listView;
	private XListView XlistView;
	private LinearLayout noPicture;
	private LinearLayout noHistory;
	private LinearLayout pictureList;
	private LinearLayout historyList;
	private LinearLayout listHistoryEmergency;
	private String plateNumber;
	private FaultHandingAdapter mMsgAdapter;
	private FaultHandingHistoryEmergencyAdapter fAdapter;
	private LinearLayout mGallery;
	private Handler handler = new Handler();
	private String id;
	private String faultId;
	private String fileId;
	private String applicantId;
	private String leaseUserId;
	private int state;
	private Bitmap faultPic = null;
	private ImageView img = null;
	private static LinkedList<Bitmap> bitMap = new LinkedList<Bitmap>();

	private Dialog mShareDialog;
	private File file;
	private LinearLayout header_ll;
	private ImageView camera;
	private ImageViewPaper imagePaper;
	private LayoutInflater inflater;
	private LinkedList<String> list = new LinkedList<String>();
	private LinkedList<String> cameraList = new LinkedList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fault_handling_history, container,
				false);

		context = inflater.getContext();
		mInflater = LayoutInflater.from(getActivity());

		initView();
		back.setOnClickListener(this);
		addData();
		return view;
	}

	private void addData() {
		handler.post(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", id);
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/faultRecord/faultDetail"
								+ ParamUtil.mapToString(params)),
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable error) {
								Common.dialogMark(getActivity(), null, "网络异常");
								super.onFailure(error);
							}

							@Override
							public void onSuccess(String content) {
								try {
									dispalyFaultDetail(new JSONObject(content)
											.getJSONObject("fault"));
								} catch (JSONException e) {
									dispalyFaultDetail(null);
								}

								try {
									JSONArray isEmergency = new JSONObject(
											content).getJSONArray("emergencys");
									if (isEmergency == null
											|| isEmergency.length() == 0
											|| Login.roleType.toString() == "DRIVER") {
										emergencyRecoveryVehicle
												.setVisibility(View.GONE);
									} else {
										emergencyRecoveryVehicle
												.setVisibility(View.VISIBLE);
										disPalyFaultEmergencyHistory(isEmergency);
									}
								} catch (JSONException e) {
									disPalyFaultEmergencyHistory(null);
								}

								try {
									// 接收图片
									getPicture(new JSONObject(content).getJSONArray("frs"));
								} catch (JSONException e) {
									dispalyPicture(null);
								}

								try {
									dispalyFaultHistory(new JSONObject(content)
											.getJSONArray("history"));
								} catch (JSONException e) {
									dispalyFaultHistory(null);
								}

								super.onSuccess(content);
							}

						});
			}
		});

	}

	private void initView() {
		if (getArguments() != null) {
			id = getArguments().getString("id");
		}
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("故障处理历史详情");
		faultApplicant = (TextView) view.findViewById(R.id.fault_applicant_ht);
		faultDescribe = (TextView) view.findViewById(R.id.fault_describe_ht);
		faultTime = (TextView) view.findViewById(R.id.fault_time_ht);
		faultNumber = (TextView) view.findViewById(R.id.fault_number_ht);
		plateNum = (TextView) view.findViewById(R.id.fault_plate_number);
		pictureList = (LinearLayout) view.findViewById(R.id.picture_list_ht);
		historyList = (LinearLayout) view.findViewById(R.id.list_history_ht);
		listHistoryEmergency = (LinearLayout) view
				.findViewById(R.id.list_history_emergency);
		noPicture = (LinearLayout) view.findViewById(R.id.no_picture_ht);
		noHistory = (LinearLayout) view.findViewById(R.id.no_history_ht);
		faultState = (TextView) view.findViewById(R.id.fault_state_ht);
		back = (ImageView) view.findViewById(R.id.back);
		callPhone = (ImageView) view.findViewById(R.id.callPhone_ht);
		maintenanceComplete = (TextView) view
				.findViewById(R.id.maintenance_complete);
		emergencyRecoveryVehicle = (TextView) view.findViewById(R.id.emergency_recovery_vehicle);
		
		//隐藏处理完毕和回收应急车按钮
		maintenanceComplete.setVisibility(View.INVISIBLE);
		emergencyRecoveryVehicle.setVisibility(View.INVISIBLE);
		
		listView = (XListView) view.findViewById(R.id.history_list_ht);
		mMsgAdapter = new FaultHandingAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setFooterDividersEnabled(false);
		listView.setPullRefreshEnable(false);
		listView.setPullLoadEnable(false);

		XlistView = (XListView) view.findViewById(R.id.history_list_emergency);
		fAdapter = new FaultHandingHistoryEmergencyAdapter(getActivity());
		XlistView.setAdapter(fAdapter);
		XlistView.setFooterDividersEnabled(false);
		XlistView.setPullRefreshEnable(false);
		XlistView.setPullLoadEnable(false);
		callPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phoneNumber = faultNumber.getText().toString();
				if(phoneNumber == null || phoneNumber.equals(""))
					Toast.makeText(getActivity(), "无该联系人的联系方式", Toast.LENGTH_SHORT).show();
				else
				call(phoneNumber);
			}
		});
		// 维修完成
		maintenanceComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				maintenanceComplete();
			}
		});
		if (Login.roleType.equals("DRIVER"))
			maintenanceComplete.setVisibility(View.GONE);
		// 回收应急车
		emergencyRecoveryVehicle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				emergencyRecoveryVehicle();
			}
		});
	}

	private void maintenanceComplete() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				RequestParams params = new RequestParams();
				String roleType = null;
//				Integer faultRecordState = 5;
//				params.put("faultRecordState", "5");
//				params.put("faultDisState", "2");
				params.put("faultRecordId", id);
//				char type = Login.orgId.charAt(7);
//				if (type == '2')
//					roleType = "SALESSUPERVISOR";
//				params.put("roleType", roleType);

				HttpUtil.getClient().put(
						HttpUtil.getURL("api/faultRecord/faultHandleCompletedByAfterSales"),
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable error) {
								Common.dialogMark(getActivity(), null, "网络异常");
								super.onFailure(error);
							}

							@Override
							public void onSuccess(String content) {
								JSONObject jb = null;
								String boo = null;
								try {
									jb = new JSONObject(content);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								try {
									boo = jb.get("status").toString();
								} catch (JSONException e) {
									e.printStackTrace();
								}
								if (boo.equals("true")) {
									Fragment fragment = new MyWorkHistory();
									Bundle bundle = new Bundle();
									bundle.putInt("type", 0);
									bundle.putString("name", "处理历史");
									fragment.setArguments(bundle);
									((DrawerLayoutMenu) context)
											.changeView(fragment);
									sendNotice();
								} else {
									Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT);
								}

								super.onSuccess(content);
							}

						});
			}
		});

	}

	private void emergencyRecoveryVehicle() {
		String applyPerson = faultApplicant.getText().toString();
		Fragment fra = new Recovery();
		Bundle bundle = new Bundle();
		bundle.putString("faultRecodId", id);
		bundle.putString("applyPerson", applyPerson);
		bundle.putString("plateNumber", plateNumber);
		fra.setArguments(bundle);
		((DrawerLayoutMenu) context).changeView(fra);
	}
	
	private void call(String phone) {  
	    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));  
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	    startActivity(intent);  
	}    

	public void dispalyFaultDetail(JSONObject joooo) {
		if (joooo != null) {
			try {
				String applicant = joooo.getString("userName");
				faultApplicant.setText(applicant);
			} catch (JSONException e) {
				faultApplicant.setText("无数据");
			}
			try {
				faultId = joooo.getString("id");
			} catch (JSONException e) {
			}
			try {
				String phone = joooo.getString("phone");
				faultNumber.setText(phone);
			} catch (JSONException e) {
				faultNumber.setText("无数据");
			}
			try {
				int state = joooo.getInt("state");
				if (state < 5 && state >= 0)
					faultState.setText("维修中");
				else if (state == 5)
					faultState.setText("已修复");
			} catch (JSONException e) {
				faultState.setText("无数据");
			}
			try {
				String time = joooo.getString("time");
				faultTime.setText(time);
			} catch (JSONException e) {
				faultTime.setText("无数据");
			}
			try {
				String describe = joooo.getString("faultDescription");
				faultDescribe.setText(describe);
			} catch (JSONException e) {
				faultDescribe.setText("无数据");
			}
			try {
				state = joooo.getInt("state");
				if (state == 5)
					maintenanceComplete.setVisibility(View.GONE);
			} catch (JSONException e) {
			}
			try {
				plateNumber = joooo.getString("plateNumber");
				plateNum.setText(plateNumber);
			} catch (JSONException e) {
				plateNum.setText("");
			}
			try {
				leaseUserId = joooo.getString("leaseUserUid");
			} catch (JSONException e) {
				leaseUserId = null;
			}
			try {
				applicantId = joooo.getString("userId");
			} catch (JSONException e) {
				applicantId = null;
			}
		} else {
			faultDescribe.setText("无数据");
			faultTime.setText("无数据");
			faultNumber.setText("无数据");
			faultApplicant.setText("无数据");
		}

	}

	public void dispalyPicture(final Queue<String> q) {
		if(q.size() == 0)  return ;
		final String fileId= q.peek();
		
		String[] allowedTypes = new String[] { "application/octet-stream" };
		String format = "300*200";
		HttpUtil.getClient().get(
				HttpUtil.getURL("api/attachment/download/" + fileId + "/"
						+ faultId+ "/"+ format),
				new BinaryHttpResponseHandler(allowedTypes) {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.e("yy:", "onFailure: " + arg3);
					}

					@Override
					public void onSuccess(byte[] content) {
						try {
							
							faultPic = FaultHandlingHistoryDetail.getBitmapFromByte(content);
							View view = mInflater.inflate(R.layout.picture_item, mGallery, false);
							ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
							img.setImageBitmap(faultPic);
							img.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									checkBigPicture(fileId);
								}
							});
							mGallery.addView(view);
							q.poll();
							if(q.size() == 0) {
								return ;
							}
							dispalyPicture(q);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	public void checkBigPicture(String fileId) {
		String[] allowedTypes = new String[] { "application/octet-stream" };
		String format = "720*1280";
		
		HttpUtil.getClient().get(
				HttpUtil.getURL("api/attachment/download/" + fileId + "/"+ faultId + "/" + format),
				new BinaryHttpResponseHandler(allowedTypes) {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.e("yy:", "onFailure: " + arg3);
					}

					@Override
					public void onSuccess(byte[] content) {
						Bitmap bigPicture = null;
						try {
							
							bigPicture = FaultHandlingHistoryDetail.getBitmapFromByte(content);
							imagePaper = new ImageViewPaper(context);
							imagePaper.setBitmapCache(bigPicture);
							imagePaper.show();
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}
            


	public static Bitmap getBitmapFromByte(byte[] temp) {
		Bitmap bitmap = null;

		int length = temp.length;

		if (0 != length) {
			BitmapFactory.Options opt = new BitmapFactory.Options();   
            //这个isjustdecodebounds很重要      
            opt.inJustDecodeBounds = true;
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeByteArray(temp, 0, length,opt);  

             //isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2  
            opt.inSampleSize = 2;  
             //根据屏的大小和图片大小计算出缩放比例  

             //这次再真正地生成一个有像素的，经过缩放了的bitmap  
            opt.inJustDecodeBounds = false;  
			bitmap = BitmapFactory.decodeByteArray(temp, 0, length,opt);
			bitMap.add(bitmap);
		}
		return bitmap;
	}

	public void dispalyFaultHistory(JSONArray jsonArray) {
		
		if (jsonArray != null && jsonArray.length() > 0) {
			try {
				mMsgAdapter.resetSingleData(jsonArray);
				mMsgAdapter.notifyDataSetChanged();
				Utility.setListViewHeightBasedOnChildren(listView);
			} catch (Exception e) {
				Common.dialogMark(getActivity(), null, "信息加载有误");
			}
			noHistory.setVisibility(View.GONE);
		} else {
			// Common.dialogMark(getActivity(), null, "处理历史无数据");
			historyList.setVisibility(View.GONE);
		}
	}

	public void disPalyFaultEmergencyHistory(JSONArray jsonArray) {

		if (jsonArray != null && jsonArray.length() > 0) {
			try {
				fAdapter.resetSingleData(jsonArray);
				fAdapter.notifyDataSetChanged();
				Utility.setListViewHeightBasedOnChildren(XlistView);
			} catch (Exception e) {
				Common.dialogMark(getActivity(), null, "信息加载有误");
			}
			noHistory.setVisibility(View.GONE);
		} else {
			// Common.dialogMark(getActivity(), null, "处理历史无数据");
			listHistoryEmergency.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			((DrawerLayoutMenu) context).back();
			break;
		default:
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mShareDialog.dismiss();
		super.onActivityResult(requestCode, resultCode, data);
		addImage();
		mShareDialog.dismiss();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void addImage() {
		header_ll.removeAllViews();
		mGallery.removeAllViews();
		bitMap.clear();

		for (int i = 0; i < list.size(); i++) {
			final View view = inflater.inflate(R.layout.picture_item, mGallery,
					false);
			ImageView icon = (ImageView) view.findViewById(R.id.picture_item);
			final TextView text = (TextView) view
					.findViewById(R.id.path_history);
			text.setText(list.get(i));
			icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					imagePaper = new ImageViewPaper(context);
					imagePaper.setImageCache(list);
					imagePaper.setCurrent(text.getText().toString());
					imagePaper.show();
				}
			});
			ImageSize imageSize = getImageViewWidth(icon);
			int reqWidth = imageSize.width;
			int reqHeight = imageSize.height;
			Bitmap bitmap = decodeSampledBitmapFromResource(list.get(i),
					reqWidth, reqHeight);
			icon.setImageBitmap(bitmap);
			bitMap.add(bitmap);
			mGallery.addView(view);
		}
	}

	private int getImageViewFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (Exception e) {
		}
		return value;
	}

	private ImageSize getImageViewWidth(ImageView imageView) {
		ImageSize imageSize = new ImageSize();
		final DisplayMetrics displayMetrics = imageView.getContext()
				.getResources().getDisplayMetrics();
		final LayoutParams params = imageView.getLayoutParams();

		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getWidth(); // Get actual image width
		if (width <= 0)
			width = params.width; // Get layout width parameter
		if (width <= 0)
			width = getImageViewFieldValue(imageView, "mMaxWidth");
		if (width <= 0)
			width = displayMetrics.widthPixels;
		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getHeight(); // Get actual image height
		if (height <= 0)
			height = params.height; // Get layout height parameter
		if (height <= 0)
			height = getImageViewFieldValue(imageView, "mMaxHeight");
		if (height <= 0)
			height = displayMetrics.heightPixels;
		imageSize.width = width;
		imageSize.height = height;
		return imageSize;
	}

	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight) {
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}

	private Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
		return bitmap;
	}

	private class ImageSize {
		int width;
		int height;
	}

	private synchronized void removePicture(String path) {
		int index = list.indexOf(path);
		if (index >= 0) {
			list.remove(index);
			com.example.newenergyvehicle.imageloader.PhotoAdapter.mSelectedImage
					.remove(path);
		}
	}

	private void sendNotice() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String dataStr = formatter.format(curDate);
				String content = "您的车辆:" + plateNum.getText().toString() + ",于"
						+ dataStr + "已维修完毕！";
				String receiverIds = applicantId+","+leaseUserId;
				RequestParams params = new RequestParams();
				params.put("title", "车辆维修");
				params.put("content", content);
				params.put("noticeType", "0000000100000001");
				params.put("receiverIds", receiverIds);

				HttpUtil.getClient().post(
						HttpUtil.getURL("api/notice/noticeAdd"),
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}

							@Override
							public void onSuccess(String content) {
							}

						});
			}
		});
	}
	
	private void getPicture(JSONArray joooo){
		Queue<String> q =new  LinkedList<String>();
		try {
			int count=0;
			if (joooo != null && joooo.length() > 0) {
				mGallery = (LinearLayout) view.findViewById(R.id.id_gallery_ht);
				mGallery.removeAllViews();
				for (int i = 0; i < joooo.length(); i++) {
					if (!joooo.get(i).toString().equals("null")) {
						View view = mInflater.inflate(R.layout.picture_item_ht,
								mGallery, false);
						JSONObject jb = null;
						try {
							jb = joooo.getJSONObject(i);
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						try {
							q.offer(jb.getString("fileId"));
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						
					}
					else{
						count++;
					}
				}
				if(count==joooo.length()){
					pictureList.setVisibility(View.GONE);
					noPicture.setVisibility(View.VISIBLE);
					
				}
				else{
				    noPicture.setVisibility(View.GONE);
				    pictureList.setVisibility(View.VISIBLE);
				    dispalyPicture(q);
				}
			} else {

				pictureList.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
