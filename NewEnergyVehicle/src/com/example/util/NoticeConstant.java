package com.example.util;

import com.example.newenergyvehicle.check.VehicleCheckInfo;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.notification.NotificationInfo;
import com.example.newenergyvehicle.notification.NotificationMain;
import com.example.newenergyvehicle.salesPersonnel.CarAssignment;
import com.example.newenergyvehicle.salesPersonnel.FaultHanding;
import com.example.newenergyvehicle.sendAndReceive.SendAndReceive;

public class NoticeConstant {
	//type
	public final static String NOTICE_REMINDER = "通知提醒";
	public final static String NOTICE_FAULT_APP = "故障处理";
	public final static String EMERGENCY_APPLY = "应急车审核";
	public final static String CARDELIVERY_TASK = "接送车任务";
	
	//类
	public final static Class NOTICE_REMINDER_CLASS = NotificationMain.class;
	public final static Class NOTICE_FAULT_APP_CLASS = MyWork.class;
	public final static Class EMERGENCY_APPLY_CLASS = MyWork.class;
	public final static Class CARDELIVERY_TASK_CLASS = MyWork.class;
	
	//详情类
	public final static Class NOTICE_REMINDER_DETAIL_CLASS = NotificationInfo.class;
	public final static Class NOTICE_FAULT_APP_DETAIL_CLASS = FaultHanding.class;
	public final static Class EMERGENCY_APPLY_DETAIL_CLASS = VehicleCheckInfo.class;
	public final static Class CARDELIVERY_TASK_DETAIL_CLASS = CarAssignment.class;
}
