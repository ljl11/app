package com.example.newenergyvehicle.salesPersonnel;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.search.SearchInfo;
import com.example.newenergyvehicle.search.SearchPlatenumber;
import com.example.newenergyvehicle.search.SearchPlatenumberAdapter;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.refreshListView.XListView;
import com.example.util.refreshListView.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class LeadCar extends Fragment implements IXListViewListener,
		Serializable {
	private View view;
	private Context context;
	private XListView listView;
	private Handler mHandler;
	private SearchPlatenumberAdapter mMsgAdapter;

	private TextView sureLead;
	private TextView plateNumber;
	private SearchInfo info;
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.lead_car, container, false);
		context = inflater.getContext();
		initView();
		closeKeyBoard();

		return view;
	}

	private void initView() {
		plateNumber = (TextView) view.findViewById(R.id.input_car_number);
		sureLead = (TextView) view.findViewById(R.id.sure_lead_car);

		if (getArguments() != null) {
			info = (SearchInfo) getArguments().getSerializable("info");
			plateNumber.setText(info.getName());
		}

		plateNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("type", "2");
				bundle.putString("url", "api/vehicle/app/vehiclesForLeaseUser");
				SearchPlatenumber ca = new SearchPlatenumber();
				ca.setArguments(bundle);
				((DrawerLayoutMenu) context).changeView(ca);
			}
		});

		sureLead.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!plateNumber.getText().equals("")) {
					showNormalDialog();
				} else {
					Common.dialogMark(getActivity(), null, "请选择车牌号");
				}
			}
		});
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
	}

	public void sureLeadCar() {
		handler.post(new Runnable() {
			public void run() {
				RequestParams params = new RequestParams();
				params.put("vehicleId", info.getId());
				params.put("receiverType", 1);
				params.put("title", "领车成功");
				params.put("content",DrawerLayoutMenu.userName + "已经领取车辆" + plateNumber.getText().toString());
				HttpUtil.getClient().post(
						HttpUtil.getURL("api/collarCarOperator"), params,
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {

								try {
									if (new JSONObject(response).getString(
											"status").equals("true")) {
										Bundle bundle = new Bundle();
										bundle.putString("Page", "JSYLeadCar");
										bundle.putSerializable("JSYLeadCar",
												new LeadCar());
										SuccessPage success = new SuccessPage();
										success.setArguments(bundle);
										((DrawerLayoutMenu) context)
												.changeView(success);
										Common.dialogMark(getActivity(), null,
												"已成功领取车辆："
														+ plateNumber.getText()
																.toString());
									} else {
										Common.dialogMark(getActivity(), null,
												"领车失败");
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(Throwable error) {
								Common.dialogMark(getActivity(), null, "网络异常");
							}
						});
			}
		});
	}

	private void showNormalDialog() {
		String sureMessage = "确定领取" + plateNumber.getText().toString()
				+ "这辆车，领取后将由你负责这辆车";
		Common.dialog(getActivity(), null, sureMessage, "取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}, "确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						sureLeadCar();

					}

				}, null, null);
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
}
