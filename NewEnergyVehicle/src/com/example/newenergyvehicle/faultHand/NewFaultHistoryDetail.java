package com.example.newenergyvehicle.faultHand;

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

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.dialog.ChooseStationInfo;
import com.example.newenergyvehicle.failureReport.CameraActivy;
import com.example.newenergyvehicle.failureReport.ImageViewPaper;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.imageloader.PhotoActivity;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.notification.NotificationAdapter;
import com.example.newenergyvehicle.salesPersonnel.FaultHandingAdapter;
import com.example.newenergyvehicle.salesPersonnel.FaultHandingHistoryEmergencyAdapter;
import com.example.newenergyvehicle.salesPersonnel.FaultHandlingHistoryDetail;
import com.example.util.Common;
import com.example.util.DialogInfo;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.Utility;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewFaultHistoryDetail extends Fragment implements IXListViewListener,
OnClickListener, DialogInfo, NeedRefresh{
	private View view;
	private Context context;
	
	private String faultRecordId;
	
	private ImageView addImage;
	private ImageView back;
	private ImageView callPhone;
	private TextView moduleTitle;
	private LinearLayout addFaultHistoryPicture;
	private Button finishedProcessing;
	private TextView faultApplicant;
	private TextView applicantCompany;
	private TextView contactNumber;
	private TextView reportingTime;
	private TextView faultDescribe;
	private TextView modelName;
	private TextView carNumber;
	private LinearLayout pictureList;
	private LinearLayout historyList;
	private LinearLayout noPicture;
	private LinearLayout noHistory;
	private LinearLayout listHistoryEmergency;
	private Bitmap faultPic = null;
	private ImageViewPaper imagePaper;
	
	private Dialog mShareDialog;
	private XListView listView;
	private XListView XlistView;
	private LinearLayout mGallery;
	private LayoutInflater mInflater;
	private FaultHandingAdapter mMsgAdapter;
	private FaultHandingHistoryEmergencyAdapter fAdapter;
	private Handler handler = new Handler();
	private File file;
	private ProgressDialog mProgressDialog;
	private LinkedList<String> cameraList = new LinkedList<String>();
	private LinkedList<String> list = new LinkedList<String>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.new_faulthistory_detail, container, false);
		context = inflater.getContext();
		mInflater = LayoutInflater.from(getActivity());
		
		initView();
		addData();
		
		return view;
	}
	
	private void initView() {
		moduleTitle = (TextView) view.findViewById(R.id.module_title);
		moduleTitle.setText("故障历史详情");
		
		addImage = (ImageView) view.findViewById(R.id.add_image);
		back = (ImageView) view.findViewById(R.id.back);
		addFaultHistoryPicture = (LinearLayout) view.findViewById(R.id.addFaultHistoryPicture);
		finishedProcessing = (Button) view.findViewById(R.id.finished_processing);
		faultApplicant = (TextView) view.findViewById(R.id.fault_applicant);
		applicantCompany = (TextView) view.findViewById(R.id.applicant_company);
		contactNumber = (TextView) view.findViewById(R.id.fault_number);
		reportingTime = (TextView) view.findViewById(R.id.fault_time);
		faultDescribe = (TextView) view.findViewById(R.id.fault_describe);
		modelName = (TextView) view.findViewById(R.id.model_name);
		carNumber = (TextView) view.findViewById(R.id.car_number);
		pictureList = (LinearLayout) view.findViewById(R.id.picture_list);
		historyList = (LinearLayout) view.findViewById(R.id.list_history);
		noPicture = (LinearLayout) view.findViewById(R.id.no_picture);
		noHistory = (LinearLayout) view.findViewById(R.id.no_history);
		callPhone = (ImageView) view.findViewById(R.id.callPhone);
		listHistoryEmergency = (LinearLayout) view.findViewById(R.id.list_emergency);
		
		addFaultHistoryPicture.setOnClickListener(this);
		back.setOnClickListener(this);
		callPhone.setOnClickListener(this);
		finishedProcessing.setVisibility(View.GONE);
		
		listView = (XListView) view.findViewById(R.id.history_list);
		listView.setPullLoadEnable(false);
		mMsgAdapter = new FaultHandingAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		listView.setFooterDividersEnabled(false);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(this);
		
		XlistView = (XListView) view.findViewById(R.id.listEmergency);
		fAdapter = new FaultHandingHistoryEmergencyAdapter(getActivity());
		XlistView.setAdapter(fAdapter);
		XlistView.setFooterDividersEnabled(false);
		XlistView.setPullRefreshEnable(false);
		XlistView.setPullLoadEnable(false);
		
		if (getArguments() != null) {
			if(getArguments().containsKey("historyTroubleInfo")) {
				FaultHandHistoryItemInfo faultHistoryInfo = (FaultHandHistoryItemInfo) getArguments().getSerializable("historyTroubleInfo");
				faultRecordId =faultHistoryInfo.getId();
			}
			if(getArguments().containsKey("id")) {
				faultRecordId = getArguments().getString("id");
			}
		}
	}
	
	private void addData() {
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
	
	public void dispalyFaultDetail(JSONObject joooo) {
		if (joooo != null) {
			try {
				String applicant = joooo.getString("reporterName");
				faultApplicant.setText(applicant);
			} catch (JSONException e) {
				faultApplicant.setText("无数据");
			}
			try {
				String phone = joooo.getString("reporterPhone");
				contactNumber.setText(phone);
			} catch (JSONException e) {
				contactNumber.setText("无数据");
			}
			try {
				String time = joooo.getString("time");
				reportingTime.setText(time);
			} catch (JSONException e) {
				reportingTime.setText("无数据");
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
				applicantCompany.setText(joooo.getString("unitName"));
			} catch (JSONException e) {
				applicantCompany.setText("无数据");
			}
//			try {
//				leaseUserId = joooo.getString("leaseUserUid");
//			} catch (JSONException e) {
//				leaseUserId = null;
//			}
		} else {
			modelName.setText("无数据");
			faultDescribe.setText("无数据");
			reportingTime.setText("无数据");
			contactNumber.setText("无数据");
			faultApplicant.setText("无数据");
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
	
	private void call(String phone) {  
	    Intent intentPhone = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));  
	    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	    startActivity(intentPhone);  
	}
	
	private void onClickCamera() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), CameraActivy.class);
		startActivityForResult(intent, 100);
	}

	private void onClickPhotoAlbum() {
		Intent intent = new Intent();
		intent.putExtra("image",cameraList.toArray(new String[cameraList.size()]));
		intent.setClass(getActivity(), PhotoActivity.class);
		startActivityForResult(intent, 10);
	}

	private void dialogClose() {
		mShareDialog.dismiss();
	}
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(NotificationAdapter.minToCaleander(System
				.currentTimeMillis()));
	}
    
	@Override
	public void redresh() {
		onLoad();
	}

	@Override
	public void setInfo(ChooseStationInfo info) {
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			((DrawerLayoutMenu) context).back();
			break;
		case R.id.addFaultHistoryPicture:
			showDialog();
			break;
		case R.id.close:
			dialogClose();
			break;
		case R.id.take_picture:
			onClickCamera();
			break;
		case R.id.photo_album:
			onClickPhotoAlbum();
			break;
		case R.id.callPhone: {
			String phoneNumber = contactNumber.getText().toString();
			if (phoneNumber == null || phoneNumber.equals(""))
				Toast.makeText(getActivity(), "无该联系人的联系方式", Toast.LENGTH_SHORT).show();
			else
				call(phoneNumber);
			break;
		}	
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		addData();
	}

	@Override
	public void onLoadMore() {
		onLoad();
	}
}
