package com.example.newenergyvehicle.personal;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.String.StringUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Personal extends Fragment implements OnClickListener {
	private TextView logout;
	private EditText username;
	private EditText userNo;
	private TextView peuserName;
	private TextView pcompany;
	private TextView change;
	Dialog mShareDialog;
	TextView calen;
	private View view;
	private Context context;
	private String USERID;
	private String name;
	private String no;
	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.personal, container, false);
		context = inflater.getContext();

		initView();
		initData();
		initListener();
		return view;
	}

	private void initListener() {
		logout.setOnClickListener(this);
		change.setOnClickListener(this);

		((DrawerLayoutMenu) context).setMoreClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialog();
			}
		});
		username.addTextChangedListener(textChange);
		userNo.addTextChangedListener(textChange);
	}

	private void initData() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				HttpUtil.getClient().get(
						HttpUtil.url + "api/userManagement/partUserInfo",
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject response) {
								try {
									USERID = response.getString("id");
									name = response.getString("name");
									username.setText(name);
									peuserName.setText(response
											.getString("name"));
								} catch (Exception e) {
									username.setText("暂无数据");
								}
								try {
									no = response.getString("code");
									userNo.setText(no);
								} catch (Exception e) {
									userNo.setText("暂无数据");
								}
								try {
									pcompany.setText(response
											.getString("unitName"));
								} catch (Exception e) {
									pcompany.setText("暂无数据");
								}

							}

							@Override
							public void onFailure(String responseBody,
									Throwable error) {
								super.onFailure(responseBody, error);
							}
						});
			}
		});
	}

	private void changePassword() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				String oldPassword = ((EditText) mShareDialog
						.findViewById(R.id.change_firstinput)).getText()
						.toString();
				final String newPassword_1 = ((EditText) mShareDialog
						.findViewById(R.id.change_secondinput)).getText()
						.toString();
				String newPassword_2 = ((EditText) mShareDialog
						.findViewById(R.id.change_thirdinput)).getText()
						.toString();

				if (vaild(oldPassword, newPassword_1, newPassword_2)) {
					sendMessage(oldPassword, newPassword_1);
				}
			}
		});
	}

	private void sendMessage(String oldPassword, final String newPassword_1) {
		RequestParams params = new RequestParams();
		params.put("oldPassword", oldPassword);
		params.put("password", newPassword_1);
		HttpUtil.getClient().put(HttpUtil.url + "api/userManagement/password",
				params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						boolean statu = HttpUtil.isStatus(response);
						if (statu) {
							try {
								if (response.getString("status").equals("true")) {
									Login.password = newPassword_1;
									mShareDialog.dismiss();
									cleanEditText();
									Common.dialogMark(getActivity(), null,
											"修改成功");
								} else {
									Common.dialogMark(getActivity(), null,
											"初始密码不匹配");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
						}

					}

					@Override
					public void onFailure(String responseBody, Throwable error) {
					}
				});
	}

	private boolean vaild(String oldPassword, String newPassword_1,
			String newPassword_2) {
		boolean flag = false;

		if (StringUtil.isNullOrEmpty(oldPassword)) {
			Common.dialogMark(getActivity(), null, "请输入原密码");
			return flag;
		}
		if (StringUtil.isNullOrEmpty(newPassword_1)) {
			Common.dialogMark(getActivity(), null, "请输入要修改的密码");
			return flag;
		}
		if (StringUtil.isNullOrEmpty(newPassword_2)) {
			Common.dialogMark(getActivity(), null, "请输入要修改的密码");
			return flag;
		}
		if (!newPassword_1.equals(newPassword_2)) {
			Common.dialogMark(getActivity(), null, "输入的两次密码不匹配");
			return flag;
		}

		if (newPassword_1.equals(oldPassword)) {
			Common.dialogMark(getActivity(), null, "新密码与原密码相同");
			return flag;
		}

		return true;
	}

	private void initView() {
		logout = (TextView) view.findViewById(R.id.logout);
		username = (EditText) view.findViewById(R.id.puserName);
		userNo = (EditText) view.findViewById(R.id.userNo);
		peuserName = (TextView) view.findViewById(R.id.peuserName);
		pcompany = (TextView) view.findViewById(R.id.pcompany);
		change = (TextView) view.findViewById(R.id.change_person_message);
	}

	private void clean() {
		Login.token = null;
		Login.orgId = null;
		Login.userid = null;
		Login.roleType = null;
		Login.password = null;
		Login.operatorCode = null;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.logout:
			((DrawerLayoutMenu) context).closeApp();
			break;
		case R.id.change_password_cancel:
			mShareDialog.dismiss();
			break;
		case R.id.change_password_sure:
			changePassword();
			break;
		case R.id.change_person_message:
			if(username.getText().toString().equals("")||userNo.getText().toString().equals(""))
				Common.dialogMark(getActivity(), null, "信息未填写完整");
			else
				showNormalDialog();
			break;

		default:
			break;
		}
	}

	private void showNormalDialog() {
		Common.dialog(getActivity(), null, "确认修改为当前信息？", "取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}, "确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						changePersonMessage();
					}

				}, null, null);
	}

	private void changePersonMessage() {
		final String name = username.getText().toString();
		final String code = userNo.getText().toString();
		handler.post(new Runnable() {
			@Override
			public void run() {
				Map params = new HashMap();
				params.put("id",USERID);
				params.put("code", code);
				params.put("name", name);
				HttpUtil.getClient().put(
						HttpUtil.getURL("api/userManagement/baseInfo"
								+ ParamUtil.mapToString(params)),
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								try {
									JSONObject jo = new JSONObject(response);
									if (jo.getString("status").equals(
											"repetition")) {
										Common.dialogMark(getActivity(), null,
												"用户名或者账号重复");
									} else if (jo.getString("status")
											.equals("true")) {
										Common.dialogMark(getActivity(), null,
												"修改成功");
										initData();
									} else if (jo.getString("status")
											.equals("false")) {
										Common.dialogMark(getActivity(), null,
												"修改失败");
									}
								} catch (JSONException e) {
									e.printStackTrace();
									Common.dialogMark(getActivity(), null,
											"修改失败");
								}

							}

							@Override
							public void onFailure(Throwable error) {
								Common.dialogMark(getActivity(), null, "修改失败");
							}
						});
			}
		});
	}

	private void logout() {

		handler.post(new Runnable() {

			@Override
			public void run() {
				RequestParams params = new RequestParams();
				params.put("token", Login.token);
				new AsyncHttpClient().post(HttpUtil.url + "api/logout.action",
						params, new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject response) {
								try {
									if (HttpUtil.isStatus(response)) {
										clean();
									}
								} catch (Exception e) {
									clean();
								}
							}

							@Override
							public void onFailure(String responseBody,
									Throwable error) {
								clean();
							}
						});
			}
		});
	}

	private void cleanEditText() {
		((EditText) mShareDialog.findViewById(R.id.change_firstinput))
				.setText("");
		((EditText) mShareDialog.findViewById(R.id.change_secondinput))
				.setText("");
		((EditText) mShareDialog.findViewById(R.id.change_thirdinput))
				.setText("");
	}

	private void showDialog() {
		if (mShareDialog == null) {
			mShareDialog = new Dialog(context, R.style.dialog_bottom_full);
			mShareDialog.setContentView(R.layout.personnal_change_password);
			mShareDialog.getWindow().findViewById(R.id.change_password_cancel)
					.setOnClickListener(this);
			mShareDialog.getWindow().findViewById(R.id.change_password_sure)
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
		mShareDialog.getWindow().setAttributes(lp);
		mShareDialog.getWindow().setGravity(Gravity.BOTTOM);
	}
	
	TextWatcher textChange = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			String content = s.toString();
			if(content.equals(name)||content.equals(no))
				change.setVisibility(View.GONE);
			else
				change.setVisibility(View.VISIBLE);
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
