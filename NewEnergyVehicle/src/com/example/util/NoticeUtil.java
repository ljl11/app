package com.example.util;

import java.util.HashMap;
import java.util.Map;

import com.example.newenergyvehicle.login.Login;
import com.example.service.pushService.Notice;
import com.igexin.sdk.PushManager;

import android.content.Context;

public class NoticeUtil {
	public static Map<String,Class> mapList = new HashMap<String,Class>();
	
	public static Map<String,Class> mapDetail = new HashMap<String,Class>();
	
	
	public static void init(){
		if(mapList.isEmpty()){
			mapList.put(NoticeConstant.NOTICE_FAULT_APP, NoticeConstant.NOTICE_FAULT_APP_CLASS);
			mapList.put(NoticeConstant.EMERGENCY_APPLY, NoticeConstant.EMERGENCY_APPLY_CLASS);
			mapList.put(NoticeConstant.CARDELIVERY_TASK, NoticeConstant.CARDELIVERY_TASK_CLASS);
			mapList.put(NoticeConstant.NOTICE_REMINDER, NoticeConstant.NOTICE_REMINDER_CLASS);
	    }
		if(mapDetail.isEmpty()){
		   mapDetail.put(NoticeConstant.NOTICE_FAULT_APP, NoticeConstant.NOTICE_FAULT_APP_DETAIL_CLASS);
	       mapDetail.put(NoticeConstant.EMERGENCY_APPLY, NoticeConstant.EMERGENCY_APPLY_DETAIL_CLASS);
	       mapDetail.put(NoticeConstant.CARDELIVERY_TASK, NoticeConstant.CARDELIVERY_TASK_DETAIL_CLASS);
	       mapDetail.put(NoticeConstant.NOTICE_REMINDER, NoticeConstant.NOTICE_REMINDER_DETAIL_CLASS);
		}
	}
	
	public static void getNoticeBytype(Notice n){
		if(n.getType().equals(NoticeConstant.NOTICE_FAULT_APP)){
   		 n.setContent("您有"+n.getSize() +"条故障需及时处理");
		}
		else if(n.getType().equals(NoticeConstant.EMERGENCY_APPLY)){
			n.setContent("您有" + n.getSize() + "个应急车审核需处理");
		}
		
		else if (n.getType().equals(NoticeConstant.CARDELIVERY_TASK)){
    		n.setContent("您有"+n.getSize() +"个接送车任务需处理");
    	}
		
		else if (n.getType().equals(NoticeConstant.NOTICE_REMINDER)){
    		n.setContent("您有"+n.getSize() +"个通知提醒");
    	}
	}
	
	public static void stopPush(Context context){
		PushManager.getInstance().unBindAlias(context, Login.userid,true); //只对当前cid做解绑
 		PushManager.getInstance().turnOffPush(context);
 		PushManager.getInstance().stopService(context);
	}
	
}
