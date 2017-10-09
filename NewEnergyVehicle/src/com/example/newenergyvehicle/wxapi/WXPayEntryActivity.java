package com.example.newenergyvehicle.wxapi;

import com.example.newenergyvehicle.MainActivity;
import com.example.newenergyvehicle.myBill.Payment;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			// Intent intent = new
			// Intent(WXPayEntryActivity.this,Payment.class);
			// startActivity(intent);
			if (resp.errCode == 0) {
				finish();
				Intent[] intents = new Intent[1];
				Intent intent = new Intent(this, MainActivity.class);
				intents[0] = intent;
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setAction(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				this.startActivities(intents);
				Payment.toSuccessPage();
			}else{
				finish();
				Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
			}

			// Payment.toSuccessPage();
			// Fragment fa = new SuccessPage();
			// Bundle bundle = new Bundle();
			// bundle.putString("Page", "payMent");
			// bundle.putString("backPage", "toBillDetail");
			// fa.setArguments(bundle);
			// ((DrawerLayoutMenu) Payment.context).changeView(fa);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// No call for super().
	}
}