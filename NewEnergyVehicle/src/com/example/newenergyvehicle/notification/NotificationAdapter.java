package com.example.newenergyvehicle.notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.MainActivity;
import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.check.VehicleCheckInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.HomePageGroup;
import com.example.newenergyvehicle.homePage.NotificationGroup;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.map.MapActivity;
import com.example.newenergyvehicle.vehicle.VehicleMain;
import com.example.util.HttpUtil;
import com.example.util.menu.ScreenUtils;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class NotificationAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<Notification> list;
	private PopupWindow popupWindow;
	private PopupWindow PopupWindowIsRead; 
	private NotificationMain notificationMain;
	private int itemSize[] = new int[2];
	private Handler handler = new Handler();

	public NotificationAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<Notification>();
	}

	public void resetData(JSONArray data) {
		if (data != null) {
			int length = data.length();
			for (int i = 0; i < length; i++) {
				try {
					list.add(getEntity(data.getJSONObject(i)));
				} catch (JSONException e) {
				}
			}
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final int index = position;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_content_notification,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setItem(holder, position);

		return convertView;
	}

	private void showPopupWindow(final View view, final Notification notification, final int position,final int[] itemSize) {
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.pop_window, null);
		TextView theFirstButton = (TextView) contentView
				.findViewById(R.id.firstButton);
		TextView theSecondButton = (TextView) contentView
				.findViewById(R.id.secondButton);
		theFirstButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (notification.getIs_read() == 0) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							HttpUtil.getClient().get(
									HttpUtil.url
											+ "api/noticeAlert/noticeAlert/"
											+ notification.getId(),
									new AsyncHttpResponseHandler() {
										@Override
										public void onSuccess(String response) {
											notification.setIs_read(1);
											popupWindow.dismiss();
											notifyDataSetChanged();
											try {
											} catch (Exception e) {
												Toast.makeText(context,
														"操作失败", 1).show();
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
			}
		});

		theSecondButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (notification.getIsDetele() == 0) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							HttpUtil.getClient()
									.get(HttpUtil.url
											+ "api/noticeAlert/deleteNoticeAlert/"
											+ notification.getId(),
											new AsyncHttpResponseHandler() {
												@Override
												public void onSuccess(
														String response) {
													notification.setIsDetele(1);
													popupWindow.dismiss();
													list.remove(position);
													notifyDataSetChanged();
													try {
													} catch (Exception e) {
														Toast.makeText(context,
																"删除失败", 1)
																.show();
													}
												}

												@Override
												public void onFailure(
														Throwable error) {
													super.onFailure(error);
												}
											});
						}
					});
				}
			}
		});
		int popupwindowWidth = itemSize[1] * 2 / 5;
		int popupWindowHeight = itemSize[0] / 3;
		popupWindow = new PopupWindow(contentView, popupwindowWidth, popupWindowHeight, true);
		popupWindow.setTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		int windowPos[] = calculatePopWindowPos(view, contentView);
		int xOff ,yOff = 10;
		final int screenWidth = ScreenUtils.getScreenWidth(view.getContext());
		xOff = (int) (screenWidth * 0.63);
		windowPos[0] += xOff;
		windowPos[1] -= yOff;
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, windowPos[0], windowPos[1]);
	}
	
	private void showPopupWindowIsRead(final View view, final Notification notification,final int position,final int[] itemSize) {
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.layout_pop, null);
		TextView theSecondButton = (TextView) contentView.findViewById(R.id.deleteTheNotice);
		theSecondButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (notification.getIsDetele() == 0) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							HttpUtil.getClient()
									.get(HttpUtil.url
											+ "api/noticeAlert/deleteNoticeAlert/"
											+ notification.getId(),
											new AsyncHttpResponseHandler() {
												@Override
												public void onSuccess(
														String response) {
													notification.setIsDetele(1);
													PopupWindowIsRead.dismiss();
													list.remove(position);
													notifyDataSetChanged();
													try {
													} catch (Exception e) {
														Toast.makeText(context,
																"删除失败", 1)
																.show();
													}
												}

												@Override
												public void onFailure(
														Throwable error) {
													super.onFailure(error);
												}
											});
						}
					});
				}
			}
		});

		int popupwindowWidth = itemSize[1] * 2 / 5;
		int popupWindowHeight = itemSize[0] / 5;
		PopupWindowIsRead = new PopupWindow(contentView, popupwindowWidth, popupWindowHeight, true);
		PopupWindowIsRead.setTouchable(true);
		PopupWindowIsRead.setBackgroundDrawable(new ColorDrawable());
		PopupWindowIsRead.setOutsideTouchable(true);
		PopupWindowIsRead.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		int windowPos[] = calculatePopWindowPos(view, contentView);
		int xOff,yOff = 10;
		final int screenWidth = ScreenUtils.getScreenWidth(view.getContext());
		xOff = (int) (screenWidth * 0.63);
		windowPos[0] += xOff;
		windowPos[1] -= yOff;
		PopupWindowIsRead.showAtLocation(view, Gravity.NO_GRAVITY, windowPos[0], windowPos[1]);

	}
	
	private static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 获取contentView的测量高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }
	
	private void setItem(ViewHolder holder,final int position) {

		final Notification notification = list.get(position);
		holder.time.setText(notification.getTime());
		holder.notice_time.setText(notification.getTime());
		holder.txt_title.setText(notification.getTitle());
		holder.type.setText(notification.getType());
		if(notification.getIsDetele() == 1)
			holder.linearLayout.setVisibility(View.GONE);
		if (notification.getIs_read() == 0) {
			holder.is_read.setText("未读");
			holder.is_read.setBackgroundResource(R.color.noRead);
		} else if (notification.getIs_read() == 1) {
			holder.is_read.setBackgroundResource(R.color.isRead);
			holder.is_read.setText("已读");
		}
		holder.txt_content.setText(notification.getContent());
		for(int i = 0; i == 0; i++) {
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			holder.linearLayout.measure(w, h);
			itemSize[0] = holder.linearLayout.getMeasuredHeight();
			itemSize[1] = holder.linearLayout.getMeasuredWidth();
		}
	
		holder.moreOperations.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(notification.getIs_read() == 0)
					showPopupWindow(arg0,notification, position,itemSize);
				else
					showPopupWindowIsRead(arg0,notification,position,itemSize);
			}

		});

		holder.linearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						HttpUtil.getClient().get(
								HttpUtil.url + "api/noticeAlert/noticeAlert/"
										+ notification.getId(),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(String response) {
										notification.setIs_read(1);
										if(null != PopupWindowIsRead && PopupWindowIsRead.isShowing()){  
											 PopupWindowIsRead.dismiss();  
								                if(null == PopupWindowIsRead){  
								                    Log.e("MainActivity","null == PopupWindowIsRead");  
								                }  
								         }
										if(null != popupWindow && popupWindow.isShowing()){  
											popupWindow.dismiss();
								         }
										Bundle bundle = new Bundle();
										bundle.putString("id",notification.getId());
										Fragment fragment = new NotificationInfo();
										fragment.setArguments(bundle);
										((DrawerLayoutMenu) context)
												.changeView(fragment);
									}

									@Override
									public void onFailure(Throwable error) {
									}
								});
					}
				});
			}
		});

	}

	static class ViewHolder {
		private TextView time;
		private TextView txt_title;
		private TextView type;
		private TextView notice_time;
		private TextView is_read;
		private TextView txt_content;
		private LinearLayout moreOperations;
		private LinearLayout linearLayout;

		public ViewHolder(View view) {
			time = (TextView) view.findViewById(R.id.alerk);
			txt_title = (TextView) view.findViewById(R.id.title);
			type = (TextView) view.findViewById(R.id.type);
			notice_time = (TextView) view.findViewById(R.id.notice_time);
			is_read = (TextView) view.findViewById(R.id.is_read);
			txt_content = (TextView) view.findViewById(R.id.ncontent);
			moreOperations = (LinearLayout) view.findViewById(R.id.moreOperations);
			linearLayout = (LinearLayout) view
					.findViewById(R.id.notificationItem);
		}
	}

	public Notification getEntity(JSONObject joooo) {
		Notification notification = new Notification();

		try {
			notification.setId(joooo.getString("id"));
		} catch (Exception e) {
		}
		try {
			notification.setTitle(joooo.getString("title"));
		} catch (Exception e) {
			notification.setTitle("暂无数据");
		}

		try {
			notification.setIs_read(joooo.getInt("isread"));
		} catch (Exception e) {
			notification.setIs_read(null);
		}

		try {
			notification.setType(joooo.getString("paramtext"));
		} catch (Exception e) {
			notification.setType("暂无数据");
		}
		try {
			notification.setTime(joooo.getString("time"));
		} catch (Exception e) {
			notification.setTime("暂无数据");
		}
		try {
			notification.setContent(joooo.getString("content"));
		} catch (Exception e) {
			notification.setContent("暂无数据");
		}

		try {
			notification.setIsDetele(joooo.getInt("isDelete"));
		} catch (Exception e) {
			notification.setIsDetele(null);
		}

		return notification;
	}

	public void resetSingleData(JSONArray data) {
		list.clear();
		resetData(data);
	}

	public static String minToCaleander(long now) {
		DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);

		return formatter.format(calendar.getTime());
	}
}
