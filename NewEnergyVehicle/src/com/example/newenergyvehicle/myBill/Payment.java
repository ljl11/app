package com.example.newenergyvehicle.myBill;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.util.Common;
import com.example.util.HttpUtil;
import com.example.util.params.ParamUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Payment extends Fragment{
	private View view;
	private static Context context;
	private Handler handler = new Handler();
	private ImageView back;
	private TextView headContent;
	private TextView surePayment;
	private RegionNumberEditText paymentAmount;
	private RadioButton aliy_button;
	private RadioButton wechat_button;
	private RelativeLayout aliy_check;
	private RelativeLayout wechat_check;
	private EditText description;
	private String contractId;
	private Integer currentPeriod;
	private int payMethod;
	private String WXUrl = "api/paymentRecord/wechatToPayment";
	private String AlipayUrl = "api/paymentRecord/paymentOrderInfo";
	private String appId = "wx3461bc93f9712c4d";
	
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;
	
	private final int ALIY_PAY = 0;
	private final int WECHAT_PAY = 1;
	
	private IWXAPI api;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//沙箱
		EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
		view = inflater.inflate(R.layout.payment, container, false);
		context = inflater.getContext();

		initView();
		initListener();
		initDataFromLastPage();
		return view;
	}

	private void initDataFromLastPage() {
		if(getArguments()!=null){
			contractId = getArguments().getString("contractId");
			currentPeriod = getArguments().getInt("currentPeriod");
			paymentAmount.setText(getArguments().getString("payFee"));
			//设置可输入金额限制
			RegionNumberEditText edittext=new RegionNumberEditText(context); 
			edittext.setRegion(Double.parseDouble(getArguments().getString("payFee")),0); 
			edittext.setTextWatcher();
		}
	}

	private void initListener() {
		surePayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!paymentAmount.getText().toString().equals("")){
					surePayment.setEnabled(false);
					payMethod = judgePayType();
				    toPayment();
				}else{
					Common.dialogMark(getActivity(), null, "请输入缴费金额");
				}
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DrawerLayoutMenu) context).back();
			}
		});
		aliy_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				aliy_button.setChecked(true);
				wechat_button.setChecked(false);
			}
		});
		wechat_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				aliy_button.setChecked(false);
				wechat_button.setChecked(true);
			}
		});
		aliy_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				aliy_button.setChecked(true);
				wechat_button.setChecked(false);
			}
		});
		wechat_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				aliy_button.setChecked(false);
				wechat_button.setChecked(true);
			}
		});
		paymentAmount.setFilters(new InputFilter[]{new InputFilter() {
		    @Override
		    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		        if(source.equals(".") && dest.toString().length() == 0){
		            return "0.";
		        }
		        if(dest.toString().contains(".")){
		            int index = dest.toString().indexOf(".");
		            int mlength = dest.toString().substring(index).length();
		            if(mlength == 3){
		                return "";
		            }
		        }
		        return null;
		    }
		}});
	}

	private void initView() {
		back = (ImageView) view.findViewById(R.id.back);
		headContent = (TextView) view.findViewById(R.id.module_title);
		headContent.setText("缴费");
		surePayment = (TextView)view.findViewById(R.id.sure_payment);
		paymentAmount = (RegionNumberEditText)view.findViewById(R.id.payment_amount);
		aliy_button = (RadioButton)view.findViewById(R.id.alipay);
		wechat_button = (RadioButton)view.findViewById(R.id.wechat);
		aliy_check = (RelativeLayout)view.findViewById(R.id.check_aliy);
		wechat_check = (RelativeLayout)view.findViewById(R.id.check_wechat);
		description = (EditText) view.findViewById(R.id.description);
	}
	
	private void toPayment(){
		handler.postDelayed(new Runnable() {
			public void run() {
				String url = null;
				if(payMethod == WECHAT_PAY){
					url = WXUrl;
				}else if(payMethod == ALIY_PAY){
					url = AlipayUrl;
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("paymentAmount", Double.parseDouble(paymentAmount.getText().toString()));
				params.put("currentPeriod", currentPeriod);
				params.put("remark", description.getText().toString());
				params.put("contractId", contractId);
				HttpUtil.getClient().get(
						HttpUtil.getURL(url
								+ ParamUtil.mapToString(params)),
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable error) {
								Common.dialogMark(getActivity(), null, "网络异常");
								surePayment.setEnabled(true);
								super.onFailure(error);
							}

							@Override
							public void onSuccess(String content) {
								try {
									JSONObject jo = new JSONObject(content);
									if(payMethod == ALIY_PAY){
										payV2(jo.getString("paymentOrderInfo"));
									}else if(payMethod == WECHAT_PAY){
										payForWechat(jo);
									}
									surePayment.setEnabled(true);
								} catch (JSONException e) {
									e.printStackTrace();
									surePayment.setEnabled(true);
								}
								
							}

						});
			}
		}, 100);

	}
	
	private void payForWechat(JSONObject json) {
		api = WXAPIFactory.createWXAPI(getActivity(), appId);
		PayReq req = new PayReq();
		try {
			// req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
			req.appId = json.getString("appid");
			req.partnerId = json.getString("partnerid");
			req.prepayId = json.getString("prepayid");
			req.nonceStr = json.getString("noncestr");
			req.timeStamp = json.getString("timestamp");
			req.packageValue = json.getString("package");
			req.sign = json.getString("sign");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		api.registerApp(appId);
		api.sendReq(req);
	}
	
	private void payV2(final String orderInfo){
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(getActivity());
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());
				
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//				getPayResult(resultInfo);
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
					// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
					toSuccessPage();
					
				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case SDK_AUTH_FLAG: {
				@SuppressWarnings("unchecked")
				AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
				String resultStatus = authResult.getResultStatus();

				// 判断resultStatus 为“9000”且result_code
				// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
				if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
					// 获取alipay_open_id，调支付时作为参数extern_token 的value
					// 传入，则支付账户为该授权账户
					Toast.makeText(context,
							"授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
							.show();
				} else {
					// 其他状态值则为授权失败
					Toast.makeText(context,
							"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

				}
				break;
			}
			default:
				break;
			}
		};
	};
	
	private int judgePayType(){
		if(aliy_button.isChecked()){
			return ALIY_PAY;
		}else{
			return WECHAT_PAY;
		}
	}
	
	public static void toSuccessPage(){
		Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
		Fragment fa = new SuccessPage();
		Bundle bundle = new Bundle();
		bundle.putString("Page", "payMent");
		bundle.putString("backPage", "toBillDetail");
//		bundle.putString("paid", paymentAmount.getText().toString());
		fa.setArguments(bundle);
		((DrawerLayoutMenu) context).changeView(fa);
	}
	
	public static void back(){
		((DrawerLayoutMenu) context).back();
	}
}
