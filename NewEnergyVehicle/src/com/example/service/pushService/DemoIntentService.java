package com.example.service.pushService;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.successPage.SuccessPage;
import com.example.util.Common;
import com.example.util.DateUtil;
import com.example.util.HttpUtil;
import com.example.util.NoticeConstant;
import com.example.util.NoticeUtil;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */

public class DemoIntentService extends GTIntentService {

	private Context context;
	
	public static Map<Integer,Notice> map =new HashMap<Integer,Notice>();
	
	
	
    public DemoIntentService() {

    }
    
    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
    	this.context = context;
    	setData(new String(msg.getPayload()));
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        Intent startIntent = new Intent(this, MyService.class);  
        startService(startIntent);
      
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    	
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    
    }
    
    public void showNotification(Notice n){
    	int id = 0;
    	int curID = isExistId(n.getType());
    	//如果同类通知已经存在
    	if(curID !=-1){
    		id = curID;
    		n.setSize(n.getSize()+map.get(curID).getSize());
    		getNoticeByType(n);
    	}
    	else{
    		id= (int)(Math.random()*(9999-1000+1))+100;
    	}
    	RemoteViews views = new RemoteViews(getPackageName(),R.layout.getui_notification);
    	views.setImageViewResource(R.id.getui_notification_icon,R.drawable.start); 
    	views.setTextViewText(R.id.getui_notification_style1_title, n.getType());
    	views.setTextViewText(R.id.getui_notification_style1_content, n.getContent());
    	views.setTextViewText(R.id.getui_notification_date,DateUtil.getCurrentTime());
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    	mBuilder.setSmallIcon(R.drawable.push_small);
 
    	map.put(id,n);
    	setIntent(views,id);
    	mBuilder.setContent(views);
    	mBuilder.setOngoing(false);
    	mBuilder.setDefaults(Notification.DEFAULT_ALL);
    	mBuilder.setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
        
    	mBuilder.setTicker(n.getContent())  
        .setPriority(Notification.PRIORITY_DEFAULT);// 设置该通知优先级  
       
        mNotificationManager.notify(id, mBuilder.build());
    }
    
    public PendingIntent getDefalutIntent(Context context,int flags){
    	Intent intent = new Intent();  
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 0,intent, flags);  
        return pendingIntent;  
    }
    
    public void setIntent(RemoteViews contentView,int id){
         //设置点击的事件
         Intent intent = new Intent("ACTION_BUTTON");
         intent.putExtra("id", id);
         intent.putExtra("type", map.get(id).toString());
         PendingIntent intent_paly = PendingIntent.getBroadcast(context, id, intent,PendingIntent.FLAG_UPDATE_CURRENT );
         contentView.setOnClickPendingIntent(R.id.getui_root_view, intent_paly);
    }
    
    private void setData(String data){
    	try {
			JSONArray array = new JSONArray(data);
			if(array != null){
				String type =array.getJSONObject(0).getString("noticeType");
				String content =array.getJSONObject(0).getString("noticeContent");
				String taskId =array.getJSONObject(0).getString("taskId");
				
		    	Notice n = new Notice(type,array.length());
		    	n.setTaskId(taskId);
		    	n.setContent(content);
				showNotification(getNoticeByType(n));
			
			}
		} catch (JSONException e) {
			
		}
    }
    
    private Notice getNoticeByType(Notice n){
    	if(n.getSize() == 1){
    		n.setCla(NoticeUtil.mapDetail.get(n.getType()));
    	}
    	else{
    		n.setCla(NoticeUtil.mapList.get(n.getType()));
    		NoticeUtil.getNoticeBytype(n);
    	}
    	return n;
    }
    
    public int isExistId(String type){
		for (Entry<Integer, Notice> entry : map.entrySet()) {
			if (entry.getValue().getType().equals(type)) {
				return entry.getKey();
			}
		}
		return -1;
    }
   
       
}