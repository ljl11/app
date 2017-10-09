package com.example.newenergyvehicle.homePage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.GestureUtils.Screen;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.service.pushService.DemoIntentService;
import com.example.service.pushService.MyService;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.NoticeUtil;
import com.example.util.SuccessBack;
import com.example.util.String.StringUtil;
import com.example.util.menu.MenuContainer;
import com.example.util.menu.MenuItemInfo;
import com.example.util.menu.MenuList;
import com.example.util.refreshListView.NeedRefresh;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.example.util.update.AppUpdateUtil;
import com.igexin.sdk.PushManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class DrawerLayoutMenu extends FragmentActivity implements
		IXListViewListener {
	private static DrawerLayout mDrawerLayout;
	private String type;
	private XListView listView;
	private TextView company;
	public static String userName;
	private NavigationAdapater adapater;
	private FragmentManager fragmentManager;
	private static FragmentTransaction tramsactiom;
	private List<Fragment> fragment;
	private RelativeLayout header;
	private ImageView openDreaw;
	private LinearLayout openDraw;
	private TextView title;
	private TextView more;
	private static TextView headNotice;
	private LinearLayout head;
	private Handler handler = new Handler();
	public static int noticeNum;
	private GestureDetector gestureDetector;
    private Screen screen;
    public static boolean isOpenMenu=true;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.drawer_layout_menu);
		type = getIntent().getExtras().getString("menuList");
        //得到屏幕的大小
        screen = GestureUtils.getScreenPix(this);
		init();
		addData();

		if (fragment != null && fragment.size() > 0) {
			tramsactiom = fragmentManager.beginTransaction();
			int index = 3;

			if (!StringUtil.isNullOrEmpty(Login.operatorCode)
					&& !Login.operatorCode.equals("1")){
				index = 5;
				MyWork.haveUnread=false;
			}
			if (!StringUtil.isNullOrEmpty(Login.operatorCode)
					&& Login.operatorCode.equals("2")){
				index = 1;
				MyWork.haveUnread=false;
			}
			if(type.equals("3"))
				index = 3;
			if(type.equals("6"))
				index = 1;
			tramsactiom.replace(R.id.id_framelayout2, fragment.get(index))
					.commit();
			changeHeaderInfo(fragment.get(index));
			mDrawerLayout.closeDrawers();
		}
	}

	private void changeHeaderInfo(Fragment fragment) {
		MenuList menulist = MenuList.getMenuList();
		MenuItemInfo itemInfo = menulist.getMenuItemInfo(menulist
				.getIdByClass(fragment.getClass()));
		changeHeader(itemInfo.getName(), itemInfo.isMore(),
				itemInfo.getMoreString());
	}

	private void init() {
		head = (LinearLayout) findViewById(R.id.menu_head);
		company = (TextView) findViewById(R.id.person_company);

		header = (RelativeLayout) findViewById(R.id.menu_header);
		openDreaw = (ImageView) findViewById(R.id.back);
		openDraw = (LinearLayout) findViewById(R.id.open_draw);
		title = (TextView) findViewById(R.id.menu_header_title);
		more = (TextView) findViewById(R.id.module_history);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout2);
		headNotice = (TextView) findViewById(R.id.head_num_notie);
//		setDrawerLeftEdgeSize(this, mDrawerLayout, 0.8f);
//		mDrawerLayout.setDrawerListener(new DrawerListener() {
//			@Override
//			public void onDrawerStateChanged(int arg0) {
//			}
//
//			@Override
//			public void onDrawerSlide(View arg0, float arg1) {
//			}
//
//			@Override
//			public void onDrawerOpened(View arg0) {
//			}
//
//			@Override
//			public void onDrawerClosed(View arg0) {
//			}
//		});
		openDraw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				openDrawer();
			}
		});
		head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			}
		});
		 gestureDetector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
	            @Override
	            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	                //判断是否是右滑
//	            	if(e1!=null&&e2!=null){
//	                float offsetX=e2.getX()-e1.getX();
//	                float offsetY=e2.getY()-e1.getY();
//	                if ((offsetX > 0 && offsetX > Math.abs(offsetY)) || (velocityX > 0 && velocityX > Math.abs(velocityY))) {
//	                    return true;//返回true表示我们在dispatchTouchEvent中，就不把事件传递到子控件中了
//	                }
//	            	}
//	                return false;
	            	if(e1!=null&&e2!=null){
	            	float x = e2.getX() - e1.getX();
	                float y = e2.getY() - e1.getY();
	                //限制必须得划过屏幕的1/10才能算划过
	                float x_limit = screen.widthPixels / 10;
	                float y_limit = screen.heightPixels / 10;
	                float x_abs = Math.abs(x);
	                float y_abs = Math.abs(y);
	                if(x_abs >= y_abs){
	                    //gesture left or right
	                    if(x > x_limit || x < -x_limit){
	                        if(x>0){
	    	                    return true;//返回true表示我们在dispatchTouchEvent中，就不把事件传递到子控件中了
	                        }
	                    }
	                }
	            	}
	                return false;
	            }
	        });
		mDrawerLayout
				.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
					@Override
					public void onDrawerClosed(View drawerView) {
						onRefresh();
						super.onDrawerClosed(drawerView);
					}

					@Override
					public void onDrawerOpened(View drawerView) {
						onRefresh();
					}
				});

		RelativeLayout relative = (RelativeLayout) findViewById(R.id.id_linearlayout2);
		ViewGroup.LayoutParams params = relative.getLayoutParams();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		params.width = (int) (dm.widthPixels * 0.8f);
		relative.setLayoutParams(params);
		listView = (XListView) findViewById(R.id.id_lv);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		adapater = new NavigationAdapater(DrawerLayoutMenu.this, this);
		listView.setAdapter(adapater);
		listView.setXListViewListener(this);
		fragmentManager = getSupportFragmentManager();

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				HttpUtil.getClient().get(
						HttpUtil.url + "api/userManagement/partUserInfo",

						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String content) {
								JSONObject json = null;
								try {
									json = new JSONObject(content);
									company.setText(json.getString("unitName"));
								} catch (Exception e) {
									company.setText("暂无数据");
								}
								try {
									((TextView) findViewById(R.id.userId)).setText(json.getString("name"));
									userName = json.getString("name");
								} catch (Exception e) {
									((TextView) findViewById(R.id.userId))
											.setText("暂无数据");
								}
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		}, 500);

	}

	private void stop() {
		listView.stopLoadMore();
		listView.stopRefresh();
	}

	@Override
	public void onRefresh() {
		stop();
		init();
		addData();
	}

	@Override
	public void onLoadMore() {
		stop();
	}

	public void addData() {
		getNotificationNum();
		if (type != null) {
			if (!type.equals("")) {
				adapater.resetSingleData(MenuList.getMenuList().getMenu(type));
				adapater.notifyDataSetChanged();
			}
		}
	}

	public FragmentManager getMenuFragmentManager() {
		return fragmentManager;
	}

	public Fragment getMenuFragment(int position) {
		return fragment.get(position);
	}

	public DrawerLayout getMenuDrawerLayout() {
		return mDrawerLayout;
	}

	public void setMenuFragment(List<Fragment> fragment) {
		this.fragment = fragment;
	}

	public  void changeView(Fragment fragment) {
		if(!AppUpdateUtil.isNetworkAvailable(this)){
			Common.dialogMark(this,
					null, "网络不可用");
		}
		else{
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		List<Fragment> list = getNotNullFragmentList();
		if (StringUtil.isNullOrEmpty(MenuList.getMenuList().getIdByClass(
				fragment.getClass())))
			setHeaderVisibility(false);
		else
			setHeaderVisibility(true);
		ft.hide(list.get(list.size() - 1)).add(R.id.id_framelayout2, fragment)
				.commitAllowingStateLoss();
		}
	}
	
	/*
	 * 点击通知栏切换到相应的fragment
	 */
	public void changeViewByNotification(Fragment fragment,String id){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		List<Fragment> list = getNotNullFragmentList();
		if(id == null) {
			setHeaderVisibility(false);
		}
		else {
			MenuItemInfo info = MenuList.getMenuList().getMenuItemInfo(id);
			changeHeader(info.getName(),info.isMore(), info.getMoreString());
		}
		ft.hide(list.get(list.size() - 1)).add(R.id.id_framelayout2, fragment)
				.commitAllowingStateLoss();
		
	}

	public List<Fragment> getNotNullFragmentList() {
		ArrayList<Fragment> fr = (ArrayList<Fragment>) getSupportFragmentManager()
				.getFragments();
		int length = fr.size();
		List<Fragment> list = new ArrayList<Fragment>(length);
		Fragment par = null;
		for (int i = 0; i < length; i++) {
			par = fr.get(i);
			if (par != null)
				list.add(par);
		}
		return list;
	}

	public void back() {
		FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
		List<Fragment> list = getNotNullFragmentList();
		int length = list.size();
		Fragment fragment = list.get(length - 2);
		fm.remove(list.get(length - 1)).show(fragment).commit();

		if (fragment instanceof NeedRefresh) {
			((NeedRefresh) fragment).redresh();
		}
		if (!StringUtil.isNullOrEmpty(MenuList.getMenuList().getIdByClass(
				fragment.getClass()))) {
			changeHeaderInfo(fragment);
			setHeaderVisibility(true);
		}

		mDrawerLayout.closeDrawers();
	}

	public void back(Class cla) {
		FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
		List<Fragment> fr = getNotNullFragmentList();
		Fragment frag = null;
		int length = fr.size() - 1;
		boolean flag = false;
		for (int i = length; i >= 0; i--) {
			if (cla == fr.get(i).getClass()) {
				flag = true;
				frag = fr.get(i);
				break;
			}
			fm.remove(fr.get(i));
		}

		if (frag == null) {
			try {
				frag = (Fragment) cla.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (!flag) {
			fm.add(R.id.id_framelayout2, frag).commit();
		} else {
			fm.show(frag).commit();
		}
		// 如果继承了NeedRefresh接口
		if (frag instanceof NeedRefresh) {
			((NeedRefresh) frag).redresh();
		}
		if (!StringUtil.isNullOrEmpty(MenuList.getMenuList().getIdByClass(
				frag.getClass()))) {
			changeHeaderInfo(frag);
			setHeaderVisibility(true);
		}
	}

	public void openDrawer() {
		mDrawerLayout.openDrawer(Gravity.LEFT);

	}

	public void closeDrawer() {
		mDrawerLayout.closeDrawers();
	}

	public interface onBootCallBackListener {
		void onBootCallBack();
	}

	public NavigationAdapater getAdapter() {
		return adapater;
	}

	public void closeApp() {
		Common.dialog(DrawerLayoutMenu.this, null, "确认退出当前账号吗？", "取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}, "确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						onBack();
					}

				}, null, null);
	}

	@Override
	public void onBackPressed() {
		// 如果已经打开侧滑栏
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			Common.dialog(DrawerLayoutMenu.this, null, "确认退出当前账号吗？", "取消",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}

					}, "确定",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							onBack();
						}

					}, null, null);

		}

		if (getNotNullFragmentList().size() == 1) {
			openDrawer();
		}

		else {
			List<Fragment> list = getSupportFragmentManager().getFragments();
			Fragment fragment = list.get(list.size() - 1);
			if (fragment instanceof SuccessBack) {
				((SuccessBack) fragment).successBack();
			} else
				back();
		}
	}

	private void onBack() {
		if (!Login.sp.getBoolean("checkboxBoolean", false)){
			Login.operatorCode = null;
			Login.token = null;
			Login.orgId = null;
			Login.roleType = null;
			Login.userid = null;
			Login.password = null;
		}
		MenuContainer.getMenuContainer().clrean();
 		finish();
 		stopApp();
	}
	
	public static void unbindClient(){
    	Handler handler  = new Handler();
    	handler.postDelayed(new Runnable() {
			@Override
			public void run() {
					HttpUtil.getClient()
							.post(HttpUtil
									.getURL("api/bindForLogout"),
									new AsyncHttpResponseHandler() {
										@Override
										public void onSuccess(String response) {
											try {
												if (response != null) {
													
												}
											} catch (Exception e) {
												 
											}
										}

										@Override
										public void onFailure(Throwable error) {
											super.onFailure(error);
										}								
										});
				} 
		}, 0);
    }

	public void changeHeader(String title, boolean more, String moreString) {
		this.title.setText(title);
		this.more.setVisibility(View.INVISIBLE);
		if (more) {
			this.more.setVisibility(View.VISIBLE);
			this.more.setText(moreString);
		}
		setHeaderVisibility(true);
	}

	public void setMoreClick(OnClickListener listener) {
		this.more.setOnClickListener(listener);
	}

	public void setHeaderVisibility(boolean isVisibility) {
		if (isVisibility) {
			header.setVisibility(View.VISIBLE);
		} else {
			header.setVisibility(View.INVISIBLE);
		}
	}

	public void changHeaderName(String name) {
		more.setText(name);
	}

	public void getNotificationNum() {
		HttpUtil.getClient().get(
				HttpUtil.getURL("api/noticeAlert/noticeNum"),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						try {
							noticeNum = 0;
							JSONObject jo = new JSONObject(response);
							if(!jo.getString("count").equals("0"))
								noticeNum = 1;
							setHeadNotice();
						} catch (Exception e) {
							noticeNum = 0;
						}
					}

					@Override
					public void onFailure(Throwable error) {
						noticeNum = 0;
					}
				});
	}
	
	public static void setHeadNotice(){
		if (Login.operatorCode != null) {
			if (Login.operatorCode.equals("2") || Login.operatorCode.equals("3")) {
				noticeNum = 0;
			}
		}
		if(noticeNum>0||MyWork.haveUnread==true){
			headNotice.setVisibility(View.VISIBLE);
		}else{
			headNotice.setVisibility(View.INVISIBLE);
		}
	}
	
	private void setDrawerLeftEdgeSize(Activity activity,
			DrawerLayout drawerLayout, float displayWidthPercentage) {
		if (activity == null || drawerLayout == null)
			return;
		if (drawerLayout == null)
			return;
		try {
			// 找到 ViewDragHelper 并设置 Accessible 为true
			Field leftDraggerField = drawerLayout.getClass().getDeclaredField(
					"mLeftDragger");// Right
			leftDraggerField.setAccessible(true);
			ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField
					.get(drawerLayout);

			// 找到 edgeSizeField 并设置 Accessible 为true
			Field edgeSizeField = leftDragger.getClass().getDeclaredField(
					"mEdgeSize");
			edgeSizeField.setAccessible(true);
			int edgeSize = edgeSizeField.getInt(leftDragger);

			// 设置新的边缘大小
			// Point displaySize = new Point();
			// activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
			// edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int)
			// (displaySize.x *
			// displayWidthPercentage)));
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			edgeSizeField.setInt(leftDragger, Math.max(edgeSize,
					(int) (dm.widthPixels * displayWidthPercentage)));
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// 当向右滑动的时候，拦截事件，不传下去,通过GestureDetector辅助事件的判断
		if (gestureDetector.onTouchEvent(event)&&isOpenMenu==true) {
			// 打开侧边栏
			openDrawer();
			return true;
		}
		return super.dispatchTouchEvent(event);
	}
	
	  
    public void stopApp(){
    	unbindClient();
 		NoticeUtil.stopPush(this);
		Intent intent = new Intent(this,MyService.class);
		stopService(intent);
		removeAllNotification();
    }
	 public  void removeAllNotification(){
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE );
			for(int id :DemoIntentService.map.keySet()){
				manager.cancel(id);
			}
			DemoIntentService.map.clear();
		}
}
class GestureUtils {
    //获取屏幕的大小
    public static Screen getScreenPix(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return new Screen(dm.widthPixels,dm.heightPixels);
    }
   
    public static class Screen{
       
        public int widthPixels;
        public int heightPixels;
       
        public Screen(){
           
        }
       
        public Screen(int widthPixels,int heightPixels){
            this.widthPixels=widthPixels;
            this.heightPixels=heightPixels;
        }

        @Override
        public String toString() {
            return "("+widthPixels+","+heightPixels+")";
        }
       
    }
    
 
   
}