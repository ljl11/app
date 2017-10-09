package com.example.newenergyvehicle.search;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.emergencyVehicle.CarChooseInfo;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SearchPlatenumber extends Fragment implements IXListViewListener {
	private View view;
	private Context context;
	private XListView listView;
	private SearchPlatenumberAdapter mMsgAdapter;
	private Handler mHandler;
	private LayoutInflater mInflater;
	private TextView cancelBt;
	private EditText plateNumber;
	private String type;
	private String url;
	private CarChooseInfo carChoose;
	private int result = -1;

	public static Fragment fa;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.search_platenumber, container, false);
		context = inflater.getContext();
		mInflater = LayoutInflater.from(getActivity());
		initView();
		getData();

		return view;
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
	}

	private void initView() {
		if (getArguments() != null) {
			type = getArguments().getString("type");
			url = getArguments().getString("url");
			if (type.equals("3")) {
				carChoose = (CarChooseInfo) getArguments().getSerializable(
						"carChooseInfo");
			}
		}
		cancelBt = (TextView) view.findViewById(R.id.cancel_bt);
		plateNumber = (EditText) view
				.findViewById(R.id.search_plate_number_edit);

		plateNumber.setFocusable(true);
		plateNumber.requestFocus();
		hideKeyBoard();
		listView = (XListView) view.findViewById(R.id.platenumber_list);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		mMsgAdapter = new SearchPlatenumberAdapter(getActivity());
		listView.setAdapter(mMsgAdapter);
		if (carChoose != null)
			mMsgAdapter.setParameter(carChoose);
		listView.setFooterDividersEnabled(false);
		listView.setXListViewListener(this);
		mHandler = new Handler();

		cancelBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				closeKeyBoard();
				((DrawerLayoutMenu) context).back();
			}
		});
		plateNumber.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				getData();
				hideKeyBoard();
				if (result == 0)
					Toast.makeText(context, "暂无信息", 50).show();
				return true;
			}
		});
		plateNumber.addTextChangedListener(textChange);
	}

	public void getData() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Map<String, String> param = new HashMap<String, String>();
				param.put("plateNumber", plateNumber.getText().toString());
				HttpUtil.getClient().get(
						HttpUtil.getURL(url + ParamUtil.mapToString(param)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONArray array = new JSONArray(response);
									if (array.length() > 0 && array != null) {
										mMsgAdapter
												.resetSingleData(array, type);
									} else {
										mMsgAdapter
												.resetSingleData(array, type);
										result = array.length();
									}
									mMsgAdapter.notifyDataSetChanged();

								} catch (Exception e) {
									Common.dialogMark(getActivity(), null,
											"信息加载有误");
								}
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
								Common.dialogMark(getActivity(), null, "网络异常");
							}
						});
			}
		});
	}

	public void setFragment(Fragment fa) {
		this.fa = fa;
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
	
	//如果虚拟键盘打开则关闭虚拟键盘
		private void closeKeyBoard() {
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.hideSoftInputFromWindow(getActivity().getWindow()
						.getDecorView().getWindowToken(), 0);
			}
		}

	TextWatcher textChange = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			if (plateNumber.length() != 0)
				getData();
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

}
