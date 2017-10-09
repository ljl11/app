package com.example.newenergyvehicle.myWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.check.CheckMain;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.sendAndReceive.SendAndReceiveMain;
import com.example.service.pushService.DemoIntentService;
import com.example.service.pushService.Notice;
import com.example.util.HttpUtil;
import com.example.util.SerachView;
import com.example.util.menu.MenuList;
import com.example.util.menu.TabhostMenu;
import com.example.util.newDialog.CommonDialog;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.NeedRefresh;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyWork extends Fragment implements NeedRefresh {
	private static MyWork mywork;
	public static boolean haveUnread;
	private View view;
	private Context context;
	private LayoutInflater inflater;
	private FragmentTabHost tabHost;
	private EditText searchEdit;
	private List<String> mTextviewArray;
	private List<Class> fragmentArray;
	private List<String> mHeaderArray;
	private boolean isSearch = true;
	private String tabTag;
	private Handler handler = new Handler();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.my_work, container, false);
		context = inflater.getContext();
		mywork = this;

		if (Login.operatorCode == null)
			initData(new String[] { "1", "3" });
		else if(Login.operatorCode.equals("1"))
			initData(new String[] { "1", "3" });
		else if(Login.operatorCode.equals("2"))
			initData(new String[] {"1"});
		else if(Login.operatorCode.equals("3"))
			initData(new String[] {"2"});
		intiView();
		initNoiteNum();
		
		changeViewByNotice();
		
		clearNotification();

		return view;
	}

	private void initData(String[] ids) {
		mTextviewArray = new ArrayList<String>();
		fragmentArray = new ArrayList<Class>();
		mHeaderArray = new ArrayList<String>();
		TabhostMenu tabhostMenu = null;
		for (String str : ids) {
			tabhostMenu = MenuList.getMenuList().getTabhostMenu(str);
			mTextviewArray.add(tabhostMenu.getName());
			fragmentArray.add(tabhostMenu.getCla());
			mHeaderArray.add(tabhostMenu.getHeaderName());
		}
	}

	private void intiView() {
		tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup(context, getChildFragmentManager(), R.id.realtabcontent);
		searchEdit = (EditText) view.findViewById(R.id.search_edit);

		final int count = fragmentArray.size();

		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = tabHost.newTabSpec(mTextviewArray.get(i))
					.setIndicator(getTabItemView(i));
			tabHost.addTab(tabSpec, fragmentArray.get(i), null);
		} 
	
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				changeView(count);
				clearNotification();
			}
		});
		

		searchEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				String plateNumber = searchEdit.getText().toString();
				searchByPlateNumber(plateNumber, tabHost.getCurrentTabTag());
				hideKeyBoard();
				return true;
			}
		});
		searchEdit.addTextChangedListener(textChange);
        
		initColor();
		initClick();
	}

	public void setCurrentView(String tag) {
		tabHost.setCurrentTabByTag(tag);
	}

	private void changeColor(View view, int color, int temp) {
		((TextView) view.findViewById(R.id.faultDeal)).setTextColor(color);
		((View) view.findViewById(R.id.vo)).setBackgroundColor(temp);
	}

	private void initColor() {
		int color = Color.parseColor("#02c3ad");
		((TextView) tabHost.getTabWidget().getChildTabViewAt(0)
				.findViewById(R.id.faultDeal)).setTextColor(color);
		((View) tabHost.getTabWidget().getChildTabViewAt(0)
				.findViewById(R.id.vo)).setBackgroundColor(color);
	}

	private View getTabItemView(int index) {
		View view = inflater.inflate(R.layout.mywork_tab, null, false);

		TextView textView = (TextView) view.findViewById(R.id.faultDeal);
		textView.setText(mTextviewArray.get(index));
		return view;
	}

	public void initClick() {
		((DrawerLayoutMenu) context).setMoreClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int index = tabHost.getCurrentTab();
				Bundle bundle = new Bundle();
				bundle.putInt("type", index);
				bundle.putString("name", mHeaderArray.get(index));
				Fragment fragment = new MyWorkHistory();
				fragment.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(fragment);
			}
		});

		((DrawerLayoutMenu) context).changHeaderName(mHeaderArray.get(tabHost
				.getCurrentTab()));
	}

	public void searchByPlateNumber(String plateNumber, String tag) {
		FragmentManager f = getChildFragmentManager();
		SerachView view = (SerachView) f.findFragmentByTag(tag);
		view.searchRefresh(plateNumber);
		// searchEdit.setText("");
	}

	public static MyWork getMyWork() {
		return mywork;
	}

	public void setNotieNum(Class cla, int num) {
		int index = fragmentArray.indexOf(cla);
		if (index >= 0 && index < fragmentArray.size()) {
			TextView numText = (TextView) tabHost.getTabWidget()
					.getChildTabViewAt(index).findViewById(R.id.num);
			if (num > 0) {
				haveUnread = true;
				numText.setVisibility(View.VISIBLE);
				numText.setText(String.valueOf(num));
			} else {
				numText.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void initNoiteNum() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				Map param = new HashMap();
				if(Login.operatorCode == null){
					param.put("type", 3);
				}
				else if (Login.operatorCode.equals("1")) {
					param.put("type", 2);
				}
				else if (Login.operatorCode.equals("2")){
					param.put("type", 4);
				} else {
					param.put("type", 3);
				}
				
				HttpUtil.getClient().get(
						HttpUtil.getURL("api/falutType/faultUntreatNum"
								+ ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String content) {
								if (content != null)
									try {
										JSONObject json = new JSONObject(
												content);
										if (Login.operatorCode == null) {
											setNotieNum(TroubleInfoMain.class,
													Integer.parseInt(json.getString("faultUntreated")));
											setNotieNum(SendAndReceiveMain.class,
													Integer.parseInt(json.getString("vehivleReceive")));
											haveUnread = !((json.getInt("faultUntreated")
													+ json.getInt("vehivleReceive")) == 0);
										}
										else if (!Login.operatorCode.equals("3")) {
											setNotieNum(TroubleInfoMain.class,
													Integer.parseInt(json.getString("faultUntreated")));
											setNotieNum(SendAndReceiveMain.class,
													Integer.parseInt(json.getString("vehivleReceive")));
											haveUnread = !((json.getInt("faultUntreated")
													+ json.getInt("vehivleReceive")) == 0);
										} else {
											setNotieNum(CheckMain.class,
													Integer.parseInt(json.getString("checkUntreated")));
											haveUnread = !(json.getInt("checkUntreated") == 0);
										}
										DrawerLayoutMenu.setHeadNotice();
									} catch (Exception e) {
										CommonDialog.exceptionDialog(getActivity());
									}
							}

							@Override
							public void onFailure(Throwable error) {
								CommonDialog.exceptionDialog(getActivity());
							}
						});
			}
		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {

		if (!hidden) {
			List<Fragment> list = getChildFragmentManager().getFragments();
			Fragment fragment = list.get(list.size() - 1);

			((NeedRefresh) fragment).redresh();

			((DrawerLayoutMenu) context).changHeaderName(mHeaderArray
					.get(tabHost.getCurrentTab()));
		}
		super.onHiddenChanged(hidden);
	}

	private void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 得到InputMethodManager的实例
		if (imm.isActive()) {
			// 如果开启
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					InputMethodManager.HIDE_NOT_ALWAYS);
			// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
		}
	}

	@Override
	public void redresh() {
		haveUnread = false;
		initNoiteNum();
	}

	TextWatcher textChange = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			if(isSearch){
				searchByPlateNumber(searchEdit.getText().toString(), tabHost.getCurrentTabTag());
			}else{
				isSearch=true;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	};
	
	private void changeView(int count){
		int current = tabHost.getCurrentTab();
		View view = null;
		for (int i = 0; i < count; i++) {
			view = tabHost.getTabWidget().getChildTabViewAt(i);
			if (i == current) {
				((DrawerLayoutMenu) context)
						.changHeaderName(mHeaderArray.get(current));
				int color = Color.parseColor("#02c3ad");
				changeColor(view, color, color);
			} else {
				int color = Color.parseColor("#dedede");
				int lineColor = Color.parseColor("#ffffff");

				changeColor(view, color, lineColor);
			}
		}
		isSearch = false;
		searchEdit.setText("");
	}
	
	private void changeViewByNotice(){
		if(tabTag != null){
			for(int i=0;i<mTextviewArray.size();i++){
				if(tabTag.equals(mTextviewArray.get(i))){
					if( i != tabHost.getCurrentTab()){
						tabHost.setCurrentTab(i);
						changeView(i);
					}
				}
			}
			
			
		}
	}
	
	private void clearNotification(){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE );
		for (Entry<Integer, Notice> en : DemoIntentService.map.entrySet()) { 
            if (tabHost.getCurrentTabTag().equals(en.getValue().getType())) {  
            	manager.cancel(en.getKey());//nofify中第一个参数使用的id
            	DemoIntentService.map.remove(en.getKey());
            	return;
            }  
        }  
	}

	public String getTabTag() {
		return tabTag;
	}

	public void setTabTag(String tabTag) {
		this.tabTag = tabTag;
	}
	
	
}
