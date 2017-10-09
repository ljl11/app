package com.example.newenergyvehicle.failureReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
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
import com.example.newenergyvehicle.emergencyVehicle.EmergencyInfo;
import com.example.newenergyvehicle.faultHand.FaultHandHistory;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.imageloader.ListImageDirPopupWindow;
import com.example.newenergyvehicle.imageloader.PhotoActivity;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.MapActivity.OnResultMapListener;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.newenergyvehicle.successPage.ISuccessPageL.ISuccessListener;
import com.example.newenergyvehicle.vehicle.VehicleMain;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.buttonRepeatClick.NoFastClickUtils;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.example.util.timeDialog.DateDialog;
import com.example.util.timeDialog.DateTimePickDialogUtil;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import android.app.DatePickerDialog;
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
import android.provider.MediaStore.Files;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class FailureReport extends Fragment implements DialogInfo, NeedRefresh {
	public static String vId;
	private View view;
	private Context context;
	private ImageView camera;
	private TextView failure_submit;
	private TextView fault_types;
	private EditText fault_description;
	private TextView car_num;
	private EditText reporterName;
	private EditText reporterPhone;
	private EditText fault_location;
	private TextView yeat_month_day;
	private File file;
	private LinkedList<String> list = new LinkedList<String>();
	private LinkedList<String> cameraList = new LinkedList<String>();
	private View headerView;
	private LinearLayout car_infomation;
	private LinearLayout header_ll;
	private LinkedList<Bitmap> bitMap = new LinkedList<Bitmap>();
	private Dialog mShareDialog;
	private ImageViewPaper imagePaper;
	private ChooseStation dialog;
	private ChooseStationAdapter adapter;
	private ChooseStationInfo info;
	private LinearLayout mGallery;
	private LayoutInflater inflater;
	private DatePickerDialog dateDialog;
	private List<View> viewList = new ArrayList<View>();
	private Handler handler = new Handler();
	private int statue = 0;
	public static String vehicleId = null;
	String typeId = null;
	private String roleType = null;
	private String url = null;
	JSONArray cars;
	JSONArray stations;
	private ProgressDialog mProgressDialog;
	private boolean isRefresh = true;
    private Map<String, Object> map = new HashMap<String, Object>();  // 用来存储获取的定位信息 
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.failure_report, container, false);
		context = inflater.getContext();
		initView();
		init();
		
		return view;

	}  
	
	private void getVehicleLocation() {
		
		if (vehicleId == null || vehicleId.equals("")) {
			Common.dialogMark(getActivity(), null, "请选择车辆");
			return ;
		}
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
										fault_location.setText("暂无车辆位置信息");
									}
								} catch (Exception e) {
									fault_location.setText("暂无车辆位置信息");
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
	private void init() {
		final Map param = new HashMap();
		param.put("userId", Login.userId);
		handler.post(new Runnable() {
			@Override
			public void run() {
				// 获取登录人姓名和电话
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/faultRecord/getReporterInfo"
								+ ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray ja = new JSONArray(response);
									JSONObject jb  = (JSONObject) ja.get(0);
									reporterName.setText(jb.getString("reporterName").toString());
									reporterPhone.setText(jb.getString("reporterPhone").toString());
								} catch (Exception e) {
									Common.dialogMark(getActivity(), null,"用户信息加载有误");
									dialog.dismiss();
								}
							}
							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		});
		final Map params = new HashMap();
		params.put("curPage", 1);
		params.put("limit", 20);
		handler.post(new Runnable() {
			@Override
			public void run() {
				// 获取故障类型数据
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/falutType/falutTypes"
								+ ParamUtil.mapToString(params)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									stations = new JSONArray(response);
								} catch (Exception e) {
									Common.dialogMark(getActivity(), null,"信息加载有误");
									dialog.dismiss();
								}
							}
							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		});
//		获取驾驶员或者租赁用户的车辆
		roleType = Login.roleType;	
		url = "api/vehicle/vehicles/driverId";
		handler.post(new Runnable() {
			@Override
			public void run() {
				HttpUtil.getClient().get(HttpUtil.getURL(url),new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									cars = new JSONArray(response);
								} catch (Exception e) {
									Common.dialogMark(getActivity(), null,"信息加载有误");
									dialog.dismiss();
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
	public void onStart() {
		if (isRefresh) {
			initView();
			init();
			clearInput();
		}
		super.onStart();
	}
	@Override
	public void onDestroy() {  
		 	initView();
			clearInput();
	        super.onDestroy();  
	       
	    }  
	
	@SuppressWarnings("deprecation")
	private void initView() {
		dateDialog = DateDialog.getDateDialog(context);
		dialog = new ChooseStation(context);
		adapter = dialog.getAdapter();
		adapter.setFragment(this);
		mGallery = (LinearLayout) view.findViewById(R.id.id_gallery);
		car_infomation = (LinearLayout) view.findViewById(R.id.car_infomation);
		failure_submit = (TextView) view.findViewById(R.id.failure_submit);
		fault_types = (TextView) view.findViewById(R.id.fault_types1);
		addViewToList(fault_types);
		fault_description = (EditText) view.findViewById(R.id.fault_description1);
		addViewToList(fault_description);
		reporterName = (EditText) view.findViewById(R.id.reporterName);
		addViewToList(reporterName);
		reporterPhone = (EditText) view.findViewById(R.id.reporterPhone);
		addViewToList(reporterPhone);
		fault_location = (EditText) view.findViewById(R.id.fault_location_p);
		addViewToList(fault_location);
		yeat_month_day = (TextView) view.findViewById(R.id.year_month_day1);
		addViewToList(yeat_month_day);
		car_num = (TextView) view.findViewById(R.id.car_num1);
		addViewToList(car_num);
		camera = (ImageView) view.findViewById(R.id.camera1);
		car_num.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (roleType.equals("DRIVER"))
					showDialog(cars, "选择车辆", 1);
				else if (roleType.equals("BLOC"))
					showDialog(cars,"选择车辆",3);
			}
		});
		fault_types.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialog(stations, "选择故障类型", 2);
			}
		});
		failure_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(NoFastClickUtils.isFastClick()) {
					Common.dialogMark(getActivity(), null, "您已提交故障，请不要重复提交");
				}
				else
					sendImage();
			}
		});		
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (mShareDialog == null) {
					mShareDialog = new Dialog(context,R.style.dialog_bottom_full);
					mShareDialog.setContentView(R.layout.upload_pictures);
					isRefresh = false;
					mShareDialog.getWindow().findViewById(R.id.take_picture).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									Intent intent = new Intent();
									intent.setClass(getActivity(),CameraActivy.class);
									startActivityForResult(intent, 100);
								}
							});
					mShareDialog.getWindow().findViewById(R.id.photo_album).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									Intent intent = new Intent();
									intent.putExtra("image", cameraList.toArray(new String[cameraList.size()]));
									intent.setClass(getActivity(),PhotoActivity.class);
									startActivityForResult(intent, 10);
								}
							});

					mShareDialog.getWindow().findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									mShareDialog.dismiss();
								}
							});
				}
				mShareDialog.show();
				mShareDialog.setCanceledOnTouchOutside(true);
				WindowManager windowManager = mShareDialog.getWindow().getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				WindowManager.LayoutParams lp = mShareDialog.getWindow().getAttributes();
				lp.width = (int) (display.getWidth());
				lp.height = (int) (display.getHeight() * 0.3F);
				mShareDialog.getWindow().setAttributes(lp);
				mShareDialog.getWindow().setGravity(Gravity.BOTTOM);
			}
		});
		headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_header, null);
		header_ll = (LinearLayout) headerView.findViewById(R.id.header_ll);
		yeat_month_day.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dateDialog.show();
			}
		});

		dateDialog.setButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				yeat_month_day.setText(DateDialog.getTime());
			}
		});
		
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mShareDialog.dismiss();
		isRefresh = false;
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 80) {
			String path = data.getStringExtra(CameraActivy.CAMERA_RETURN_PATH);
			if (path != null && !path.equals("")) {
				cameraList.add(path);
				list.add(path);
			}
		} else if (requestCode == 100) {
			if (file != null)
				camera.setImageURI(Uri.fromFile(file));
		} else if (requestCode == 10) {
			try {
				Object obj = data.getExtras().get("images");
				if (obj != null) {
					String[] images = (String[]) obj;
					if (images.length > 0) {
						list.clear();
						for (String image : images)
							if (!list.contains(image))
								list.addLast(image);
					}
				}
			} catch (Exception e) {
			}
			for (String s : cameraList)
				if (!list.contains(s))
					list.add(s);
		}
		addImage();

		mShareDialog.dismiss();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void addImage() {
		header_ll.removeAllViews();
		mGallery.removeAllViews();
		bitMap.clear();
		for (int i = 0; i < list.size(); i++) {
			final View view = inflater.inflate(R.layout.photo_item, mGallery,
					false);
			ImageView icon = (ImageView) view.findViewById(R.id.picture_item);
			final TextView text = (TextView) view.findViewById(R.id.path_history);
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
			view.findViewById(R.id.cancel_picture).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mGallery.removeView(view);
							removePicture(text.getText().toString());
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
	
	private void sendImage(){
		isRefresh = true;
		final int length = list.size();
		String faultDescription = fault_description.getText().toString();
		String location = fault_location.getText().toString();
		String time = yeat_month_day.getText().toString();
		String reporter_name  = reporterName.getText().toString();
		String reporter_phone = reporterPhone.getText().toString();
		if (faultDescription.equals("") || location.equals("") || time.equals("") || reporter_name.equals("") ||reporter_phone.equals("")){
			Common.dialogMark(getActivity(), null, "信息未填写完整");
			return ;
		}
			
		if (faultDescription == null || location == null|| typeId == null || vehicleId == null || time == null || reporter_name==null || reporter_phone==null) {
			Common.dialogMark(getActivity(), null, "信息未填写完整");
			return ;
		}
		if(length == 0){
			submit(new JSONArray());
		}
		else {
			mProgressDialog = ProgressDialog.show(context, null, "正在上传");
			final JSONArray array = new JSONArray();
			uploadPicture(list,array);
		}
		
	}
	
	private void uploadPicture(final LinkedList<String> fileList,final JSONArray array) {
		final int length = list.size();
		final File file;
		if (fileList.size() > 0)
			file = new File(fileList.get(0));
		else
			return ;
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				
					InputStream files = null;
						try {
						RequestParams params = new RequestParams();
						if(file.exists()){
							files = new FileInputStream(file);
							SyncHttpClient client = new SyncHttpClient();
				 			client.addHeader("Authorization", Login.token);
							client.addHeader("curRoleType", Login.roleType);
							client.addHeader("curUserid", Login.userid);
							client.addHeader("curOrgId", Login.orgId);
							client.setTimeout(70000);
							
							params.put("file", files);
							HttpUtil.getClient().post(HttpUtil.getURL("api/attachment/uploadByformat"), params, new AsyncHttpResponseHandler(){
								
								public void onSuccess(int statusCode, String content) {
									try {
										JSONObject json = new JSONObject(content);
										String result = json.getString("fileId");
										if (result == null) {
											mProgressDialog.dismiss();
											Common.dialogMark(getActivity(), null,"上传失败");
											uploadPicture(fileList,array);
										}
										else {
											fileList.remove(0);
											array.put(json);
											uploadPicture(fileList,array);
										}
										if(fileList.size() == 0){
											submit(array);
										}
									} catch (JSONException e) {
										mProgressDialog.dismiss();
										Common.dialogMark(getActivity(), null, "上传失败");
										e.printStackTrace();
									}
								};
								
								
								@Override
								public void onFailure(int statusCode,
										Throwable error, String content) {
									mProgressDialog.dismiss();
									Common.dialogMark(getActivity(),null , "上传失败");
								}
							});
						}
						} catch (FileNotFoundException e) {
						}
					}
				
		});
	}
	public void submit(JSONArray array) {
				RequestParams params = new RequestParams();
				String faultDescription = fault_description.getText().toString();
				String location = fault_location.getText().toString();
				String time = yeat_month_day.getText().toString();
				String reporter_name  = reporterName.getText().toString();
				String reporter_phone = reporterPhone.getText().toString();
				final int arrayLength = array.length();
				if(arrayLength > 0) {
					cameraList.clear(); 
					list.clear();
				}
				if (faultDescription == null || location == null|| typeId == null || vehicleId == null || time == null) {
					Common.dialogMark(getActivity(), null, "信息未填写完整");
					return ;
				} else {
					params.put("location", location);
					params.put("faultDescription", faultDescription);
					params.put("faultType", typeId);
					params.put("vehicleId", vehicleId);
					params.put("time", time);
					params.put("userName", reporter_name);
					params.put("userPhone", reporter_phone);
					params.put("files", array.toString());
					HttpUtil.getClient().post(
							HttpUtil.getURL("api/faultRecord/fault"), params,
							new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									if(arrayLength > 0)
										mProgressDialog.dismiss();
									boolean statu = false;
									try {
										statu = HttpUtil.isStatus(new JSONObject(content));
										if (statu) {
											Bundle bundle = new Bundle();
											Fragment fa = new SuccessPage();
											bundle.putString("backPage", "toVehicleMain");
											bundle.putString("Page", "FailureReport");
											fa.setArguments(bundle);
											((DrawerLayoutMenu) context).changeView(fa);
										} else {
											mProgressDialog.dismiss();
											Common.dialogMark(getActivity(),null, "信息加载有误");
										}
									} catch (JSONException e) {
										mProgressDialog.dismiss();
									}
								}

								@Override
								public void onFailure(Throwable error) {
									if (arrayLength != 0)
										mProgressDialog.dismiss();
									Common.dialogMark(getActivity(),null, "信息加载有误");
								}
							});
				}

	}
	
	private void vehicleIsReport() {
		RequestParams params = new RequestParams();
		params.put("vehicleId", vehicleId);
		HttpUtil.getClient().get(HttpUtil.getURL("api/faultRecord/isReported"), params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode,
							String content) {
						boolean statu = false;
						try {
							statu = HttpUtil.isStatus(new JSONObject(content));
							if (statu) {
								showNormalDialog();
								failure_submit.setEnabled(false);
								failure_submit.setBackgroundResource(R.drawable.draw_gray);
							} else {
								failure_submit.setEnabled(true);
								failure_submit.setBackgroundResource(R.drawable.save_button);
							}
						} catch (JSONException e) {
							mProgressDialog.dismiss();
						}
					}

					@Override
					public void onFailure(Throwable error) {
						Common.dialogMark(getActivity(),null, "信息加载有误");
					}
				});
	}

	public void addViewToList(View view) {
		viewList.add(view);
	}

	public void clearInput() {
		for (int i = 0, len = viewList.size(); i < len; i++) {
			if (viewList.get(i) instanceof EditText) {
				((EditText) viewList.get(i)).setText(null);
			} else
				((TextView) viewList.get(i)).setText(null);
		}
		
	}

	public void showDialog(JSONArray array, String title, int index) {
		if (array != null && array.length() > 0) {
			dialog.setTitle(title);
			statue = index;
			try {
				setData(array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dialog.show();
		} else {
			Common.dialogMark(getActivity(), null, "暂无信息");
		}
	}

	public void setData(JSONArray array) throws JSONException {
		switch (statue) {
		case 1: {
			adapter.resetSingleData(adapter.toInfo(array, "id", "plateNumber"));
			break;
		}
		case 2: {
			adapter.resetSingleData(adapter.toInfo(array, "id", "name"));
			break;
		}
		case 3: {
			adapter.resetSingleData(adapter.toInfo(array, "id","plateNumber"));
		}
		default: {
			break;
		}
		}

	}

	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
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
		options.inSampleSize = 10;
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

	@Override
	public void setInfo(ChooseStationInfo info) {
		this.info = info;
		switch (statue) {
		case 1: {
			car_num.setText(info.getInformation());
			vehicleId = info.getId();
			getVehicleLocation();
			vehicleIsReport();
		}
			break;
		case 2: {
			fault_types.setText(info.getInformation());
			typeId = info.getId();
			break;
		}
		case 3: {
			car_num.setText(info.getInformation());
			vehicleId = info.getId();
			getVehicleLocation();
			vehicleIsReport();
		}
		default:
			break;
		}
	}

	@Override
	public void redresh() {
		init();
		clearInput();
		list = new LinkedList<String>();
		bitMap = new LinkedList<Bitmap>();
		cameraList = new LinkedList<String>();
		com.example.newenergyvehicle.imageloader.PhotoAdapter.mSelectedImage = new LinkedList<String>();
		addImage();
	}
	
	private void showNormalDialog() {
		String sureMessage = "该车正在维修中，不能进行故障报修";
		Common.dialog(getActivity(), null, sureMessage, null,
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
	            fault_location.setText(map.get("address").toString());
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
