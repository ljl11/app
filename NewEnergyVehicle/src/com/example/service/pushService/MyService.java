package com.example.service.pushService;


import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.util.NoticeUtil;
import com.igexin.sdk.PushManager;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MyService extends Service {  
    
	public NotificationBroadcastReceiver receiver;
	public final static String URL = "ws://10.21.1.32:9080/NewCar/api/ws/appMessagePush?authorization="+Login.token;
	public final static int BUTTON_PALY_ID = 1;
	private final int NOTIFICATION_ID = 0xa01;
	private final int REQUEST_CODE = 0xb01;
	public WebSocketConnection	wsC	;
	public boolean isOpen = true;
	private final static String packageName = "com.example.newenergyvehicle";

  
    @Override  
    public void onCreate() {  
        super.onCreate();
        receiver = new NotificationBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_BUTTON");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);
        //startWebSocket();
    }
      
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        if(receiver !=null ){
        	unregisterReceiver(receiver);
        }
        if(wsC !=null){
        	wsC.disconnect();
        }
    }  
  
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
  
	public void startWebSocket() {
		wsC = new WebSocketConnection();
		final JSONObject  message  =new JSONObject ();
		try {
			message.put("userId", Login.userId);
			message.put("clientId", PushManager.getInstance().getClientid(this));
		} catch (JSONException e1) {
		}
		
		try {
			wsC.connect(URL, new WebSocketHandler() {
				@Override
				public void onOpen() {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							while (isOpen) {
								boolean connect = wsC.isConnected();
								if (connect)
									wsC.sendTextMessage(message.toString());
								
								try {
									Thread.sleep(10000);
								} catch (InterruptedException e) {

								}
							}
						}

					});
					thread.start();
				}

				@Override
				public void onClose(int code, String reason) {
					System.out.println("onClose reason=" + reason);
					isOpen = false;
					super.onClose(code, reason);
				}

			});
		} catch (WebSocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void onTaskRemoved(Intent rootIntent) {
		DrawerLayoutMenu.unbindClient();
		NoticeUtil.stopPush(this);
		removeAllNotification();
        super.onTaskRemoved(rootIntent);
    }

    //service异常停止的回调
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    public  void removeAllNotification(){
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE );
		for(int id :DemoIntentService.map.keySet()){
			manager.cancel(id);
		}
		
		DemoIntentService.map.clear();
	}
   
}  
