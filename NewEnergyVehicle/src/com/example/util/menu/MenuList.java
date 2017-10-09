package com.example.util.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.InfomationService.InfomationServiceMenu;
import com.example.newenergyvehicle.check.CheckMain;
import com.example.newenergyvehicle.consultation.ConsultationActivity;
import com.example.newenergyvehicle.consultation.ConsultationMain;
import com.example.newenergyvehicle.emergencyVehicle.Arrangement;
import com.example.newenergyvehicle.emergencyVehicle.ArrangementDetail;
import com.example.newenergyvehicle.emergencyVehicle.ArrangementList;
import com.example.newenergyvehicle.emergencyVehicle.CarAssign;
import com.example.newenergyvehicle.emergencyVehicle.EmergencyVehicleMain;
import com.example.newenergyvehicle.failureReport.FailureReport;
import com.example.newenergyvehicle.faultHand.FaultHandHistory;
import com.example.newenergyvehicle.myBill.MyBills;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.myWork.TroubleInfoMain;
import com.example.newenergyvehicle.notification.NotificationMain;
import com.example.newenergyvehicle.personal.Personal;
import com.example.newenergyvehicle.salesPersonnel.LeadCar;
import com.example.newenergyvehicle.salesPersonnel.ShuttleMission;
import com.example.newenergyvehicle.sendAndReceive.SendAndReceiveMain;
import com.example.newenergyvehicle.serviceReview.ServiceReview;
import com.example.newenergyvehicle.vehicle.VehicleMain;

import android.support.v4.app.Fragment;

public class MenuList {
	private static final MenuList menuList = new MenuList();
	private Map<String, MenuItemInfo> publicMenu = new HashMap<String, MenuItemInfo>();
	private Map<String, TabhostMenu> tabhostMent = new HashMap<String, TabhostMenu>();
	private final static String[] executiveDirector = {"1", "8", "9", "2", "4","7"};
	// 服务评价未加
	private final static String[] driver = {"1", "8", "9", "7","5", "11"};
	private final static String[] executive = {"1", "8", "9","2","7"};
	// 服务评价未加

	private final static String[] rentalUser = {"1", "8", "9", "5", "11", "7", "10", "12"};
	//private final static String[] rentalUser = {"1", "8", "9", "5", "11", "7", "10"};
	private final static String[] serviceCentre = {"1","2","7"};
	private final static String[] vehicleMng = {"1","2","3","7"};

	private MenuList() {
		init();
	}

	public static MenuList getMenuList() {
		return menuList;
	}

	private void init() {
		publicMenu.put("1", new MenuItemInfo("1", R.drawable.work_na, "个人中心", Personal.class, true, "修改密码"));
		publicMenu.put("2", new MenuItemInfo("2", R.drawable.my_work, "我的工作", MyWork.class, true, "处理历史"));
		publicMenu.put("3", new MenuItemInfo("3", R.drawable.emergency_vehicle_management, "应急车管理", EmergencyVehicleMain.class, false, null));
		publicMenu.put("4", new MenuItemInfo("4", R.drawable.car_mission, "接送车任务", ShuttleMission.class, false, null));
		publicMenu.put("5", new MenuItemInfo("5", R.drawable.fault_na, "故障报告", FailureReport.class, false, null));
		publicMenu.put("6", new MenuItemInfo("6", R.drawable.evaluate, "评价", ServiceReview.class, false, null));
		publicMenu.put("7", new MenuItemInfo("7", R.drawable.emergency_na, "我的车辆", VehicleMain.class , false, null));
		publicMenu.put("8", new MenuItemInfo("8", R.drawable.notice, "通知提醒", NotificationMain.class, false, null));
		publicMenu.put("9", new MenuItemInfo("9", R.drawable.server, "咨询服务", ConsultationMain.class, false, null));
		publicMenu.put("10", new MenuItemInfo("10", R.drawable.lead_car, "领车", LeadCar.class, false, null));
		publicMenu.put("11", new MenuItemInfo("11", R.drawable.fault_history, "故障历史", FaultHandHistory.class, false, null));
		publicMenu.put("12", new MenuItemInfo("12", R.drawable.my_bill, "我的账单", MyBills.class, true, "缴费记录"));

		
		tabhostMent.put("1", new TabhostMenu("1", "故障处理", TroubleInfoMain.class, "处理历史"));
		tabhostMent.put("2", new TabhostMenu("2", "应急车审核", CheckMain.class, "审核历史"));
		tabhostMent.put("3", new TabhostMenu("3", "接送车任务", SendAndReceiveMain.class, "任务历史"));
	}

	public Fragment getFragment(String id) {
		Fragment fragment = null;
		try {
			fragment = (Fragment) publicMenu.get(id).getCla().newInstance();;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return fragment;
	}
	
	public int getFragmetImage(String id){
		return publicMenu.get(id).getImage();
	}
	
	public TabhostMenu getTabhostMenu(String id){
		return tabhostMent.get(id);
	}
	
	public Class getModuleClass(String id) {
		return publicMenu.get(id).getCla();
	}
	
	public String getIdByClass(Class cla){ 
		Iterator<Entry<String, MenuItemInfo>> entry = publicMenu.entrySet().iterator();
		Entry<String, MenuItemInfo> en;
		while(entry.hasNext()){
			en = entry.next();
			if(en.getValue().getCla() == cla){
				return en.getKey();
			}
		}
		return null;
	}
	
	public String getNameById(String id){
		return publicMenu.get(id).getName();
	}
	
	public String[] getMenu(String type){
		if(type.equals("1")){
			return executiveDirector;
		}else if(type.equals("2")){
			return executive;
		}else if(type.equals("3")){
			return driver;
		}else if(type.equals("4")){
			return rentalUser;
		}else if(type.equals("5")){
			return serviceCentre;
		}else if(type.equals("6")) {
			return vehicleMng;
		}
			return null;
	}
	
	public MenuItemInfo getMenuItemInfo(String id){
		return publicMenu.get(id);
	}

}
