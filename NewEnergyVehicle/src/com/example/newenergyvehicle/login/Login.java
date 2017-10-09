package com.example.newenergyvehicle.login;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.service.pushService.DemoIntentService;
import com.example.service.pushService.MyService;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.NoticeUtil;
import com.example.util.String.StringUtil;

import com.example.util.update.AppUpdateUtil;
import com.example.util.update.PackageUtil;
import com.example.util.update.Version;
import com.igexin.sdk.PushManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Login extends Activity implements OnClickListener,
		CompoundButton.OnCheckedChangeListener,TextView.OnEditorActionListener {
	public static String token;
	public static String orgId;
	public static String roleType;
	public static String operatorCode;
	public static String orgType;
	public static String company;
	public static String userId;
	private TextView login;
	private EditText username;
	private EditText passwordEdit;
	private TextView forgot_password;
	private ScrollView scroll;
	private RelativeLayout register;
	public static String userid;
	public static String password;
	public static SharedPreferences sp = null;
	private CheckBox checkboxButton = null;
	private static final String fileName = "sharedfile";// 定义保存的文件的名称
	public static Context context;
	private Boolean checkBoolean;
	private final static char charge = '2';
	private final static char driver = '4';
	public Version ver;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.login_temp);
		sp = this.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		initView();
		initListener();
		
        
		new Thread() {
			public void run() {
				try {
					if (AppUpdateUtil.isNetworkAvailable(context)) {
						checkVersion();
					}
					else{
						 Looper.prepare();

						Common.dialogMark(Login.this, null, "请确认网络是否可用");
						
						Looper.loop();
					}
				} catch (IOException e) {
					Common.dialogMark(Login.this, null, "无法与服务器连接");
				}
			}
		}.start();// 开启线程

	}

	private void initListener() {
		scroll.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				arg0.requestFocusFromTouch();
				return false;
			}
		});

		username.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				editViewFcous();
				return false;
			}
		});

		passwordEdit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				editViewFcous();
				return false;
			}
		});

		OnCheckedChangeListener myCheckChangelistener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
					checkBoolean = isChecked;			
			}
		};
	
		checkboxButton.setOnCheckedChangeListener(myCheckChangelistener);
		login.setOnClickListener(this);
		Common.clickView(login);
		forgot_password.setOnClickListener(this);
		register.setOnClickListener(this);
		passwordEdit.setOnEditorActionListener(this);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == 0){
			login();
		}
		return true;
	}

	private void initView() {
		login = (TextView) findViewById(R.id.login_temp);
		username = (EditText) findViewById(R.id.user_name1);
		checkboxButton = (CheckBox) findViewById(R.id.rememer_password);
		Drawable drawableU = getResources().getDrawable(R.drawable.username);
		drawableU.setBounds(0, 0, 40, 40);
		username.setCompoundDrawables(drawableU, null, null, null);
		Drawable drawableP = getResources().getDrawable(R.drawable.password);
		drawableP.setBounds(0, 0, 40, 40);
		passwordEdit = (EditText) findViewById(R.id.password_new1);
		passwordEdit.setCompoundDrawables(drawableP, null, null, null);
		forgot_password = (TextView) findViewById(R.id.Forgot_password);
		scroll = (ScrollView) findViewById(R.id.scroll);
		register = (RelativeLayout) findViewById(R.id.register_this);
	
		if (sp.getBoolean("checkboxBoolean", false)) {
			username.setText(sp.getString("username", null));
			passwordEdit.setText(sp.getString("passwordEdit", null));
			checkboxButton.setChecked(true);
		}

	}

	private void editViewFcous() {
		scroll.postDelayed(new Runnable() {

			@Override
			public void run() {
				scroll.smoothScrollTo(0, scroll.getChildAt(0).getHeight());

			}
		}, 500);
	}

	@Override
	protected void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	protected void onStart() {
		boolean boo = sp.getBoolean("checkboxBoolean", false);
		if (sp.getBoolean("checkboxBoolean", false)) {
			final String userNameText = sp.getString("username", username.getText().toString());
			final String passwordText = sp.getString("passwordEdit",passwordEdit.getText().toString());
			username.setText(userNameText);
			passwordEdit.setText(passwordText);
			checkboxButton.setChecked(true);
			checkBoolean = boo;

		} else {
			username.setText(null);
			passwordEdit.setText(null);
			checkboxButton.setChecked(false);
			checkBoolean = boo;
		}
		super.onStart();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void openOptionsMenu() {
		super.openOptionsMenu();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void login() {
		final String userNameText = username.getText().toString();
		final String passwordText = passwordEdit.getText().toString();
		if (vaild(userNameText, passwordText)) {
			sendMessage(userNameText, passwordText);
		}
	}

	private void sendMessage(final String userNameText,
			final String passwordText) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("userid", userNameText);
		params.put("password", passwordText);
		client.post(HttpUtil.getURL("api/loginForMobile"), params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						try {
							JSONObject response = new JSONObject(content);
							boolean statu = HttpUtil.isStatus(response);

							if (statu) {
								token = response.getString("token");
								orgId = response.getString("orgId");
								roleType = response.getString("roleType");
								try{
									orgType =  response.getString("orgType");
								}
								catch(JSONException e){
									
								}
								userId = response.getString("userId");
								userid = userNameText;
								password = passwordText;
								Editor editor = sp.edit();
								if (checkBoolean) {
									editor.putString("username", userNameText);
									editor.putString("passwordEdit",passwordText);
									editor.putBoolean("checkboxBoolean",true);
									editor.commit();
								} else {
									editor.clear();
									editor.commit();
								}
								Intent intent = new Intent();
								intent.setClass(Login.this,
										DrawerLayoutMenu.class);
								char type = orgId.charAt(7);
								if (type == '2') {
									if (response.getString("orgType").equals("afterSale")) {
										if (response.getString("charge") != null && response.getInt("charge") == 1) {
											operatorCode = "1";
											intent.putExtra("menuList", "1");
										} else {
											operatorCode = null;
											intent.putExtra("menuList", "2");
										}
									}else if(response.getString("orgType").equals("maintainSite")){
										operatorCode = "2";//维修站
										intent.putExtra("menuList", "5");
									}else if(response.getString("orgType").equals("vehicleMng")){
										operatorCode = "3";
										intent.putExtra("menuList", "6");
									}
								} else if (type == '4') {
									operatorCode = response
											.getString("operatorCode");
									intent.putExtra("menuList", "3");
								} else if (type == '3') {
									operatorCode = response
											.getString("operatorCode");
									intent.putExtra("menuList", "4");
								}
								startActivity(intent);
								startPushService();
								username.setText("");
								passwordEdit.setText("");
							} else {
								Common.dialogMark(Login.this, null,
										"用户名或密码错误！");
							}
						} catch (JSONException e) {
							Common.dialogMark(Login.this, null, "登录失败");
						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						Common.dialogMark(Login.this, null, "网络无法连接");
					}
				});
	}

	private boolean vaild(String userNameText, String passwordText) {
		boolean flag = true;
		if (StringUtil.isNullOrEmpty(userNameText)) {
			flag = false;
			Common.dialogMark(Login.this, null, "请输入账号！");
		} else if (StringUtil.isNullOrEmpty(passwordText)) {
			flag = false;
			Common.dialogMark(Login.this, null, "请输入密码！");
		}
		return flag;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login_temp:
			login();
			break;
		case R.id.Forgot_password:
			Common.dialogMark(Login.this, null, "请联系客服！");
			break;
		case R.id.register_this:
			Common.dialogMark(Login.this, null, "该功能未开放！");
			break;
		default:
			break;
		}
	}

	public void checkVersion() throws IOException {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					AppUpdateUtil.getServerVersion();
					ver = AppUpdateUtil.ver;
					if (PackageUtil.getLocalVersion(context) < ver.getVersionCode()) {
						showDialog();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	protected void downLoadApk() {
		// 进度条
		final ProgressDialog pd;
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getAPP(ver.getUrl(), pd);
					// 安装APK
					installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
				}
			}
		}.start();
	}

	public static File getAPP(String path, ProgressDialog pd) throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// 获取到文件的大小
			int totalLength = conn.getContentLength();
			pd.setMax(totalLength);
			float all = totalLength*1.0f/1024/1024;
			pd.setIndeterminate(false);
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(),
					"update.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total);
				
				float percent = total*1.0f/1024/1024;
				pd.setProgressNumberFormat(String.format("%.2fM/%.2fM", percent, all));
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
		}
	}

	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void showDialog() {
		mHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Login.this);
				// 监听下方button点击事件
				alertDialogBuilder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
								downLoadApk();
							}
						});
				alertDialogBuilder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
								dialogInterface.dismiss();
							}
						});

				// 设置对话框是可取消的

				alertDialogBuilder.setTitle("版本升级");
				alertDialogBuilder.setMessage("软件更新");
				// 当点确定按钮时从服务器上下载 新的apk 然后安装
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.show();
			}
		};

		Runnable mRunnable = new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				mHandler.sendMessage(message);
			}
		};
		mHandler.post(mRunnable);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}
	
	private void startPushService(){
		PushManager.getInstance().initialize(this.getApplicationContext(), com.example.service.pushService.DemoPushService.class);
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.example.service.pushService.DemoIntentService.class);
		PushManager.getInstance().turnOnPush(context);
		NoticeUtil.init(); //初始化推送基本数据
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
