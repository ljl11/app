package com.example.newenergyvehicle.salesPersonnel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.dialog.ChooseStation;
import com.example.newenergyvehicle.dialog.ChooseStationAdapter;
import com.example.newenergyvehicle.dialog.ChooseStationInfo;
import com.example.newenergyvehicle.emergencyVehicle.Arrangement;
import com.example.newenergyvehicle.emergencyVehicle.ArrangementDetail;
import com.example.newenergyvehicle.failureReport.CameraActivy;
import com.example.newenergyvehicle.failureReport.ImageViewPaper;
import com.example.newenergyvehicle.failureReport.FailureReport.OnResultMapListener;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.imageloader.PhotoActivity;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
//import com.example.util.callPhone.CallPhone;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.Utility;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.google.gson.JsonArray;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.LinearLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FaultHanding extends Fragment implements IXListViewListener,
		OnClickListener, DialogInfo, NeedRefresh {
	private View view;
	private Context context;
	private LinearLayout transfer_station_bt;// 转交维修站
	private LinearLayout arrange_car_bt;// 安排应急车
	private LinearLayout assign_peopel_bt;// 指派人员
	private LinearLayout add_picture_bt,trasferCustomerService,dragVehicle;// 追加图片，转交售后，拖车
	private LinearLayout pictureList;
	private LinearLayout historyList;
	private LinearLayout noPicture;
	private LinearLayout noHistory;
	private LinearLayout listHistoryEmergency;
	private Button finishedProcessing;// 处理完毕
	private TextView headContent;
	private ImageView back;
	private ImageView callPhone;
	private TextView faultApplicant;
	private String applicantId;
	private TextView faultNumber;
	private TextView faultTime;
	private TextView faultDescribe;
	private TextView modelName;
	private TextView carNumber;
	private TextView applicantCompany;
	private TextView vehicleLocation;
	private ImageView addImage;
	private TextView textOne,textTwo;
	private ImageView imgOne,imgTwo;
	private String faultRecordId;
	private String evarId;
	private String leaseUserId;
	private XListView listView;
	private XListView XlistView;
	private FaultHandingAdapter mMsgAdapter;
	private FaultHandingHistoryEmergencyAdapter fAdapter;
	private LinearLayout mGallery;
	private LayoutInflater mInflater;
	private Dialog mShareDialog;
	private LinkedList<String> cameraList = new LinkedList<String>();
	private LinkedList<String> list = new LinkedList<String>();
	private File file;
	private String isCharge;
	private Bundle bundle;
	private ChooseStation dialog;
	private ChooseStationAdapter adapter;
	private Handler handler = new Handler();
	private String applyTime;
	private String applyPlace;
	private String applyReason;
	private ProgressDialog mProgressDialog;
	private String fileId,faultDistributionUserId;
	private String vehicleId;
	private Bitmap faultPic = null;
	private ImageViewPaper imagePaper;
	private static LinkedList<Bitmap> bitMap = new LinkedList<Bitmap>();
	private Map<String, Object> map = new HashMap<String, Object>();  // 用来存储获取的定位信息 
	private Boolean isDragVehicle = false;//判断是否已经拖车
	private Handler mHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fault_handing1, container, false);
		context = inflater.getContext();
		mInflater = LayoutInflater.from(getActivity());

		initView();
	//	getFaultDescribeHandle();
		addData();
		isApply();
		return view;
	}
	
//	public void updateFaultDescribeHandle() {
//		handler.post(new Runnable() {
//			
//			@Override
//			public void run() {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("id", faultRecordId);
//				HttpUtil.getClient().get(HttpUtil.getURL("api/faultHandling/changeFaultDescribeHandle"
//						+ ParamUtil.mapToString(params)), new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(String response) {
//						
//					}
//					@Override
//					public void onFailure(Throwable error) {
//						super.onFailure(error);
//					}
//				});
//			}
//		});
//	}
	
//	public void getFaultDescribeHandle() {
//		handler.post(new Runnable() {
//			
//			@Override
//			public void run() {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("id", faultRecordId);
//				HttpUtil.getClient().get(HttpUtil.getURL("api/faultHandling/getFaultDescribeHandle"
//						+ ParamUtil.mapToString(params)), new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(String response) {
//						try {
//							JSONObject jb = new JSONObject(response);
//							String isHandle = jb.getString("isHandle");
//							
//							if (isHandle.equals("1")) {
//								Toast.makeText(getActivity(), "该故障正在处理中", Toast.LENGTH_SHORT).show();
//								((DrawerLayoutMenu) context).changeView(new MyWork());
//							}
//							else {
//								updateFaultDescribeHandle();
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//					@Override
//					public void onFailure(Throwable error) {
//						super.onFailure(error);
//					}
//				});
//			}
//		});
//	}

	public void addData() {

		handler.postDelayed(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", faultRecordId);
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
								try {
									dispalyEmergencys(new JSONObject(content)
											.getJSONArray("emergencys"));
								} catch (JSONException e) {
									dispalyFaultHistory(null);
								}
								super.onSuccess(content);
							}

						});
			}
		}, 100);

	}
	
	private void call(String phone) {  
	    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));  
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	    startActivity(intent);  
	}    
	
	public void dispalyFaultDetail(JSONObject joooo) {
		if (joooo != null) {
			try {
				String applicant = joooo.getString("reporterName");
				faultApplicant.setText(applicant);
			} catch (JSONException e) {
				faultApplicant.setText("无数据");
			}
			try {
				vehicleId = joooo.getString("vehicleId");
				if (isCharge != null) {
					if (isCharge.equals("2"))
						getStationVehicle();
				}
			} catch (JSONException e) {
				vehicleId=null;
			}
			try {
				final String phone = joooo.getString("reporterPhone");
				faultNumber.setText(phone);
				faultNumber.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						call(phone);
					}
				});
			} catch (JSONException e) {
				faultNumber.setText("无数据");
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
				String vehicleId = joooo.getString("type_name");
				modelName.setText(vehicleId);
			} catch (JSONException e) {
				modelName.setText("无数据");
			}
			try {
				String plateNumber = joooo.getString("plateNumber");
				carNumber.setText(plateNumber);
			} catch (JSONException e) {
				carNumber.setText("无数据");
			}
			try {
				applicantId = joooo.getString("userId");
			} catch (JSONException e) {
				applicantId = null;
			}
			try {
				applicantCompany.setText(joooo.getString("unitName"));
			} catch (JSONException e) {
				applicantCompany.setText("无数据");
			}
			try {
				leaseUserId = joooo.getString("leaseUserUid");
			} catch (JSONException e) {
				leaseUserId = null;
			}
		} else {
			modelName.setText("无数据");
			faultDescribe.setText("无数据");
			faultTime.setText("无数据");
			faultNumber.setText("无数据");
			faultApplicant.setText("无数据");
		}
		
		getVehicleLocation();

	}

	public void dispalyPicture(final Queue<String> q) {
		if(q.size() == 0)  return ;
		final String fileId= q.peek();
		
		String[] allowedTypes = new String[] { "application/octet-stream" };
		String format = "300*200";
		HttpUtil.getClient().get(
				HttpUtil.getURL("api/attachment/download/" + fileId + "/"
						+ faultRecordId+ "/"+ format),
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
				HttpUtil.getURL("api/attachment/download/" + fileId + "/"+ faultRecordId + "/" + format),
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

	public void dispalyEmergencys(JSONArray jsonArray) {
		String taskType;
		if (jsonArray != null && jsonArray.length() > 0) {
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = new JSONObject(jsonArray.getString(i));
					taskType = json.getString("task_type");
					if (taskType.equals("0"))
						evarId = json.getString("id");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
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

	private void initView() {
		if (getArguments() != null) {
			faultRecordId = getArguments().getString("id");
			bundle = new Bundle();
			bundle.putString("id", faultRecordId);
		}
		isCharge = Login.operatorCode;
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("故障处理");
		faultApplicant = (TextView) view.findViewById(R.id.fault_applicant);
		faultDescribe = (TextView) view.findViewById(R.id.fault_describe);
		faultTime = (TextView) view.findViewById(R.id.fault_time);
		faultNumber = (TextView) view.findViewById(R.id.fault_number);
		modelName = (TextView) view.findViewById(R.id.model_name);
		carNumber = (TextView) view.findViewById(R.id.car_number);
		vehicleLocation = (TextView) view.findViewById(R.id.fault_place);
		transfer_station_bt = (LinearLayout) view.findViewById(R.id.transfer_station_bt);
		arrange_car_bt = (LinearLayout) view.findViewById(R.id.arrange_car_bt);
		assign_peopel_bt = (LinearLayout) view.findViewById(R.id.assign_peopel_bt);
		listHistoryEmergency = (LinearLayout) view.findViewById(R.id.list_emergency);
		add_picture_bt = (LinearLayout) view.findViewById(R.id.add_picture_bt);
		pictureList = (LinearLayout) view.findViewById(R.id.picture_list);
		historyList = (LinearLayout) view.findViewById(R.id.list_history);
		noPicture = (LinearLayout) view.findViewById(R.id.no_picture);
		noHistory = (LinearLayout) view.findViewById(R.id.no_history);
		finishedProcessing = (Button) view.findViewById(R.id.finished_processing);
		back = (ImageView) view.findViewById(R.id.back);
		callPhone = (ImageView) view.findViewById(R.id.callPhone);
		addImage = (ImageView) view.findViewById(R.id.add_image);
		applicantCompany = (TextView) view.findViewById(R.id.applicant_company);
		trasferCustomerService = (LinearLayout) view.findViewById(R.id.trasfer_customerService_bt);
		dragVehicle = (LinearLayout) view.findViewById(R.id.drag_vehicle_bt);
		if (isCharge != null) {
			if (isCharge.equals("2")) {
				trasferCustomerService.setVisibility(View.VISIBLE);
				dragVehicle.setVisibility(View.VISIBLE);
				trasferCustomerService.setOnClickListener(this);
				dragVehicle.setOnClickListener(this);
				transfer_station_bt.setVisibility(View.GONE);
				arrange_car_bt.setVisibility(View.GONE);
				assign_peopel_bt.setVisibility(View.GONE);
				add_picture_bt.setVisibility(View.GONE);
			}
		}
		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);
		listView = (XListView) view.findViewById(R.id.history_list);
		listView.setPullLoadEnable(false);
		mMsgAdapter = new FaultHandingAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setFooterDividersEnabled(false);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(this);
		finishedProcessing.setOnClickListener(this);
		transfer_station_bt.setOnClickListener(this);
		back.setOnClickListener(this);
		arrange_car_bt.setOnClickListener(this);
		assign_peopel_bt.setOnClickListener(this);
		add_picture_bt.setOnClickListener(this);
		faultDescribe.setOnClickListener(this);
		callPhone.setOnClickListener(this);
		
		XlistView = (XListView) view.findViewById(R.id.listEmergency);
		fAdapter = new FaultHandingHistoryEmergencyAdapter(getActivity());
		XlistView.setAdapter(fAdapter);
		XlistView.setFooterDividersEnabled(false);
		XlistView.setPullRefreshEnable(false);
		XlistView.setPullLoadEnable(false);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.transfer_station_bt:
			TransferStation transferSt = new TransferStation();
			transferSt.setArguments(bundle);
			changeView(transferSt);
			break;
		case R.id.back:
			//updateFaultDescribeHandle();
			((DrawerLayoutMenu) context).back();
			break;
		case R.id.arrange_car_bt:
				//申请过
				if (evarId != null) {
					Bundle bundle = new Bundle();
					bundle.putString("id", evarId);
					Fragment fragment = new ArrangementDetail();
					fragment.setArguments(bundle);
					((DrawerLayoutMenu) context).changeView(fragment);
				}
				else {
					Bundle bundle = new Bundle();
					if (applyPlace != null && applyReason != null
							&& applyTime != null) {
						bundle.putString("applyPlace", applyPlace);
						bundle.putString("applyReason", applyReason);
						bundle.putString("applyTime", applyTime);
						bundle.putString("isApply", "true");
					}
					bundle.putString("faultRecordId", faultRecordId);
					ArrangeCar ac = new ArrangeCar();
					ac.setArguments(bundle);
					changeView(ac);
				}
				
			break;
		case R.id.assign_peopel_bt:
			// AssignedPerson assignedPerson = new AssignedPerson();
			// assignedPerson.setArguments(bundle);
			// changeView(assignedPerson);
			getPerson();
			break;
		case R.id.add_picture_bt:
			showDialog();
			break;
		case R.id.take_picture:
			onClickCamera();
			break;
		case R.id.photo_album:
			onClickPhotoAlbum();
			break;
		case R.id.close:
			dialogClose();
			break;
		case R.id.finished_processing:{
			Bundle bundle = new Bundle();
			bundle.putString("id", faultRecordId);
			bundle.putString("applicantId", applicantId);
			bundle.putString("leaseUserId", leaseUserId);
			bundle.putString("plateNum", carNumber.getText().toString());
			FaultHandlingResults faultHandlingResults = new FaultHandlingResults();
			faultHandlingResults.setArguments(bundle);
			changeView(faultHandlingResults);
			break;
		}
		case R.id.fault_describe:{
			Bundle bundle = new Bundle();
			bundle.putString("id", faultRecordId);
			bundle.putString("fd", faultDescribe.getText().toString());
			bundle.putString("type", "0");
			FaultDestribution destribution = new FaultDestribution();
			destribution.setArguments(bundle);
			changeView(destribution);
			break;
		}
		case R.id.drag_vehicle_bt:{
			if(!isDragVehicle){
				String message = "确认拖车，将由维修站负责";
				showNormalDialog(message,null);
			}else{
				String message = "已拖车，请勿重复操作!";
				showNormalDialog(message);
			}
			break;
		}
		case R.id.trasfer_customerService_bt:{
//			String message = "确认将该故障转回售后？";
//			showNormalDialog(message,"distribution");
			Bundle bundle = new Bundle();
			bundle.putString("id", faultRecordId);
			bundle.putString("type", "1");
			FaultDestribution destribution = new FaultDestribution();
			destribution.setArguments(bundle);
			changeView(destribution);
			break;
		}
		case R.id.callPhone: {
			String phoneNumber = faultNumber.getText().toString();
			if (phoneNumber == null || phoneNumber.equals(""))
				Toast.makeText(getActivity(), "无该联系人的联系方式", Toast.LENGTH_SHORT).show();
			else
			call(phoneNumber);
		}
		default:
			break;
		}
	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(NotificationAdapter.minToCaleander(System
				.currentTimeMillis()));
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad();
				//getFaultDescribeHandle();
				addData();
				isApply();
			}
		}, 50);
	}

	@Override
	public void onLoadMore() {
		onLoad();
	}

	private void changeView(Fragment fragment) {
		((DrawerLayoutMenu) context).changeView(fragment);
	}
	
	public void showNormalDialog(String message) {
		Common.dialog(getActivity(), null, message, null,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}, "确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
					}

				}, null, null);
	}

	private void showDialog() {
		if (mShareDialog == null) {
			mShareDialog = new Dialog(context, R.style.dialog_bottom_full);
			mShareDialog.setContentView(R.layout.upload_pictures);
			mShareDialog.getWindow().findViewById(R.id.take_picture)
					.setOnClickListener(this);
			mShareDialog.getWindow().findViewById(R.id.photo_album)
					.setOnClickListener(this);
			mShareDialog.getWindow().findViewById(R.id.close)
					.setOnClickListener(this);
		}
		mShareDialog.show();
		mShareDialog.setCanceledOnTouchOutside(true);
		WindowManager windowManager = mShareDialog.getWindow()
				.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = mShareDialog.getWindow()
				.getAttributes();
		lp.width = (int) (display.getWidth());
		lp.height = (int) (display.getHeight() * 0.3F);
		mShareDialog.getWindow().setAttributes(lp);
		mShareDialog.getWindow().setGravity(Gravity.BOTTOM);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 80) {
			String path = data.getStringExtra(CameraActivy.CAMERA_RETURN_PATH);
			if (path != null && !path.equals("")) {
				cameraList.add(path);
				list.add(path);
			}
		} else if (requestCode == 100) {
			if (file != null)
				addImage.setImageURI(Uri.fromFile(file));
		} else if (requestCode == 10) {
			list.clear();
			try {
				Object obj = data.getExtras().get("images");
				if (obj != null) {
					String[] images = (String[]) obj;
					for (String image : images)
						if (!list.contains(image))
							list.addLast(image);
				}
			} catch (Exception e) {
			}
			for (String s : cameraList)
				if (!list.contains(s))
					list.add(s);
		} else if (requestCode == 0) {
			list.clear();
		}

		if (list.size() != 0)
			sendImage();
		else
			Common.dialogMark(getActivity(), null, "未选择图片");

		mShareDialog.dismiss();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void onClickCamera() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), CameraActivy.class);
		startActivityForResult(intent, 100);
	}

	private void onClickPhotoAlbum() {
		Intent intent = new Intent();
		intent.putExtra("image",
				cameraList.toArray(new String[cameraList.size()]));
		intent.setClass(getActivity(), PhotoActivity.class);
		startActivityForResult(intent, 10);
	}

	private void dialogClose() {
		mShareDialog.dismiss();
	}

	public void getPerson() {
		handler.postDelayed(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", Login.userId);
				HttpUtil.getClient().get(
						// 获取维修站数据
						HttpUtil.getURL("api/staffManagement/searchStaff" + ParamUtil.mapToString(params)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray array = adapter.toInfo(
											new JSONArray(response), "user_id",
											"name");
									if (array.length() > 0 && array != null) {
										adapter.resetSingleData(array);
										adapter.notifyDataSetChanged();
										dialog.setTitle("选择人员");
										dialog.show();
									} else {
										Toast.makeText(context, "暂无信息",
												Toast.LENGTH_SHORT).show();
									}
								} catch (Exception e) {

									Toast.makeText(context, "信息加载有误",
											Toast.LENGTH_SHORT).show();
								}
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		}, 100);

	}

	private void showNormalDialog(String content, final String recipientId) {
		Common.dialog(getActivity(), null, content, "取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}, "确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if(recipientId==null){
							sureDragVehicle();
						}else{
							sureAssign(recipientId);
						}
					}

				}, null, null);
	}
	
	//确认拖车
	private void sureDragVehicle(){
		handler.postDelayed(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams();
				params.put("faultRecordId", faultRecordId);
				params.put("vehicleId", vehicleId);
				params.put("faultDisState", 1);
				params.put("faultRecordState", 1);
				params.put("roleType", "123");
				params.put("receiverType", 3);
//				params.put("distributionId", 3);
				params.put("recipientId", Login.userId);
				HttpUtil.getClient().post(
						HttpUtil.getURL("api/maintenanceTask/acceptTask"),
						params, new JsonHttpResponseHandler() {

							@Override
							public void onSuccess(JSONObject response) {
								try {
									String isSuccess = response
											.getString("status");
									if (isSuccess.equals("true")) {
										Toast.makeText(context, "领车成功",
												Toast.LENGTH_SHORT).show();
										onRefresh();
									} else {
										Common.dialogMark(getActivity(), null,
												"请求失败！");
									}
								} catch (JSONException e) {
									Common.dialogMark(getActivity(), null,
											"请求错误！");
								}
							}

							@Override
							public void onFailure(String responseBody,
									Throwable error) {
								super.onFailure(responseBody, error);
								Common.dialogMark(getActivity(), null, "网络异常");
							}
						});
			}
		}, 100);
	}
	
	//获取维修站持有车辆
	private void getStationVehicle(){
		handler.postDelayed(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/ownershipCar" + ParamUtil.mapToString(params)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray array = new JSONArray(response);
									int length = array.length();
									for(int i=0;i<length;i++){
										if(array.getJSONObject(i).getString("vehicle_id").equals(vehicleId)){
										    isDragVehicle = true;
										    ((TextView)view.findViewById(R.id.drag_vehicle_text)).setText("已拖车");
										}
									}
								} catch (Exception e) {
									Common.dialogMark(getActivity(), null,
											"请求错误！");
								}
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		}, 100);
	}
	
	//确认指派人员
	private void sureAssign(final String recipientedId) {
		handler.postDelayed(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams();
				params.put("faultRecordId", faultRecordId);
				params.put("recipientId", recipientedId);
				params.put("acceptType", "3");// 表示转给售后人员
				HttpUtil.getClient().post(
						HttpUtil.getURL("api/faultRecord/fault/Distribution"),
						params, new JsonHttpResponseHandler() {

							@Override
							public void onSuccess(JSONObject response) {
								try {
									String isSuccess = response
											.getString("status");
									if (isSuccess.equals("true")) {
										Toast.makeText(context, "转交成功",
												Toast.LENGTH_SHORT).show();
										((DrawerLayoutMenu) context)
												.changeView(new MyWork());
									} else {
										Common.dialogMark(getActivity(), null,
												"请求失败！");
									}
								} catch (JSONException e) {
									Common.dialogMark(getActivity(), null,
											"请求错误！");
								}
							}

							@Override
							public void onFailure(String responseBody,
									Throwable error) {
								super.onFailure(responseBody, error);
								Common.dialogMark(getActivity(), null, "网络异常");
							}
						});
			}
		}, 100);

	}

	public void isApply() {
		handler.postDelayed(new Runnable() {
			public void run() {
				Map param = new HashMap();
				param.put("id", faultRecordId);
				HttpUtil.getClient()
						.get(HttpUtil
								.getURL("api/emergencyCarManage/emergencyApplyDetail"
										+ ParamUtil.mapToString(param)),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										try {
											JSONObject jo = new JSONObject(
													response);
											if (jo != null) {
												applyTime = jo
														.getString("apply_time");
												applyReason = jo
														.getString("apply_reason");
												applyPlace = jo
														.getString("apply_address");
											}
										} catch (Exception e) {
											applyTime = null;
											applyReason = null;
											applyPlace = null;
										}
									}

									@Override
									public void onFailure(Throwable error) {
										super.onFailure(error);
									}
								});
			}
		}, 100);
	}

	private void sendImage() {
		final int length = list.size();
		if (length == 0) {
			submit(new JSONArray());
		} else {
			mProgressDialog = ProgressDialog.show(context, null, "正在上传");
			handler.post(new Runnable() {

				@Override
				public void run() {
					final JSONArray array = new JSONArray();
					InputStream files = null;
					File file = null;
					for (int i = 0; i < length; i++) {
						try {
							file = new File(list.get(i));
							RequestParams params = new RequestParams();
							if (file.exists()) {

								files = new FileInputStream(file);

								SyncHttpClient client = new SyncHttpClient();
								client.addHeader("Authorization", Login.token);
								client.addHeader("curRoleType", Login.roleType);
								client.addHeader("curUserid", Login.userid);
								client.addHeader("curOrgId", Login.orgId);
								client.setTimeout(60000);

								params.put("file", files);
								HttpUtil.getClient()
										.post(HttpUtil
												.getURL("api/attachment/uploadByformat"),
												params,
												new AsyncHttpResponseHandler() {

													public void onSuccess(
															int statusCode,
															String content) {
														try {
															JSONObject json = new JSONObject(
																	content);
															array.put(json);
															if (array.length() == length) {
																submit(array);
															}
														} catch (JSONException e) {
															e.printStackTrace();
														}
													};

													@Override
													public void onFailure(
															int statusCode,
															Throwable error,
															String content) {
													}
												});
							}
						} catch (FileNotFoundException e) {
						}
					}
				}
			});
		}

	}

	public void submit(JSONArray array) {
		RequestParams params = new RequestParams();
		if (array.length() == 0) {
			Common.dialogMark(getActivity(), null,"请检查信息是否完整!");
			return;
		} else {
			params.put("faultRecordId", faultRecordId);
			params.put("files", array.toString());
			HttpUtil.getClient().post(
					HttpUtil.getURL("api/faultRecord/addFaultPicture"), params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, String content) {
							mProgressDialog.dismiss();
							boolean statu = false;
							try {
								statu = HttpUtil.isStatus(new JSONObject(
										content));

								if (statu) {
									Common.dialogMark(getActivity(), null,
											"添加成功!");
									addData();
								} else {
									mProgressDialog.dismiss();
									Common.dialogMark(getActivity(), null,
											"信息加载有误");
								}
							} catch (JSONException e) {
								mProgressDialog.dismiss();
							}
						}

						@Override
						public void onFailure(Throwable error) {
							mProgressDialog.dismiss();
						}
					});
		}

	}
	
	
	private void getPicture(JSONArray joooo){
		Queue<String> q =new  LinkedList<String>();
		try {
			int count=0;
			if (joooo != null && joooo.length() > 0) {
				mGallery = (LinearLayout) view.findViewById(R.id.id_gallery_falut);
				mGallery.removeAllViews();
				for (int i = 0; i < joooo.length(); i++) {
					if (!joooo.get(i).toString().equals("null")) {
						View view = mInflater.inflate(R.layout.picture_item,
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
	
	public void getVehicleLocation() {
		final Map param = new HashMap();
		param.put("vehicleId", vehicleId);
		handler.post(new Runnable() {
			@Override
			public void run() {
				// 获取车辆地点
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/vehicle/app/vehicleInfo"
								+ ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								String x = null;
								String y = null;
								JSONObject jsonObject = null;
								try {
									jsonObject = new JSONObject(response);
									x = jsonObject.getString("xcoordinate");
									y = jsonObject.getString("ycoordinate");
									if(x != null && y != null) {
										double xLocation = Double.parseDouble(x);
										double yLocation = Double.parseDouble(y);
										getAddress(yLocation,xLocation);
									}
									else {
										vehicleLocation.setText("暂无车辆位置信息");
									}
								} catch (Exception e) {
									vehicleLocation.setText("暂无车辆位置信息");
								}
							}
							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		});
	}
	
	@Override
	public void setInfo(ChooseStationInfo info) {
		String message = "确认将该故障转交给" + info.getInformation();
		showNormalDialog(message, info.getId());
	}

	@Override
	public void redresh() {
		addData();
	}
	
	public void getAddress(double latitude, double lontitude) {  
        LatLng mLatLng = new LatLng(latitude,lontitude);
        // 获取反地理编码对象  
        GeoCoder mGeoCoder = GeoCoder.newInstance();  
        // 反地理编码请求参数对象  
        ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption();  
        // 设置请求参数  
        mReverseGeoCodeOption.location(mLatLng);  
        // 发起反地理编码请求(经纬度->地址信息)  
        mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);
        
        // 设置查询结果监听者  
        mGeoCoder.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);  
    }
    
    /** 
     * 编码查询结果监听者 
     */  
    private OnGetGeoCoderResultListener mOnGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {  
  
        @Override  
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  
            // 反地理编码查询结果回调函数  
            // 将地理位置信息载入到集合中 
            map.put("address", result.getAddress());
            vehicleLocation.setText(map.get("address").toString());
            if (mOnResultMapListener != null) {  
                mOnResultMapListener.onReverseGeoCodeResult(map);  
            }  
        }  
  
        @Override  
        public void onGetGeoCodeResult(GeoCodeResult result) {  
            // 地理编码查询结果回调函数  
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("latitude", result.getLocation().latitude);  
            map.put("longitude", result.getLocation().longitude);  
            map.put("address", result.getAddress());  
            if (mOnResultMapListener != null) {  
                mOnResultMapListener.onGeoCodeResult(map);  
            }  
        }  
    }; 
	 /** 
	 * 注册结果监听回调 
	 *  
	 * @param mOnResultMapListener 
	 */  
	public void registerOnResult(OnResultMapListener mOnResultMapListener) {  
	    this.mOnResultMapListener = mOnResultMapListener;  
	}  
	
	private OnResultMapListener mOnResultMapListener;  
	
	/** 
	 * 数据结果回传 
	 *  
	 * @author NapoleonBai 
	 * 
	 */  
	public interface OnResultMapListener {  
	    /** 
	     * 反地理编码执行回传 
	     *  
	     * @param map 
	     */  
	    public void onReverseGeoCodeResult(Map<String, Object> map);  
	
	    /** 
	     * 地理编码执行回传 
	     *  
	     * @param map 
	     */  
	    public void onGeoCodeResult(Map<String, Object> map);  
	}
	
}
