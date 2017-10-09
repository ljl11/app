package com.example.newenergyvehicle.successPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.emergencyVehicle.Arrangement;
import com.example.newenergyvehicle.emergencyVehicle.CarAssign;
import com.example.newenergyvehicle.emergencyVehicle.EmergencyVehicleMain;
import com.example.newenergyvehicle.emergencyVehicle.Recovery;
import com.example.newenergyvehicle.failureReport.FailureReport;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu.onBootCallBackListener;
import com.example.newenergyvehicle.myBill.BillDetail;
import com.example.newenergyvehicle.myBill.Payment;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.newenergyvehicle.salesPersonnel.FaultHanding;
import com.example.newenergyvehicle.sendAndReceive.SendAndReceiveMain;
import com.example.newenergyvehicle.sendAndReceive.Transform;
import com.example.newenergyvehicle.successPage.ISuccessPageL.ISuccessListener;
import com.example.newenergyvehicle.vehicle.VehicleMain;
import com.example.util.SuccessBack;
import com.example.util.menu.MenuItemInfo;
import com.example.util.menu.MenuList;

public class SuccessPage extends Fragment implements SuccessBack{
	private static final SuccessPage success = new SuccessPage();
	private View view;
	private Context context;
	private Fragment fragment;
	private TextView Continue;
	private TextView back_button;
	private ImageView back;
	private TextView module_title;
	private TextView tipContent;
	private String page;
	private String backPage;
	private Map<String, Class> con = new HashMap<String, Class>();

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.success_page, container, false);
		context = inflater.getContext();
		tipContent = (TextView) view.findViewById(R.id.tip_content);
		Continue = (TextView) view.findViewById(R.id.Continue);
		
		if (getArguments() != null) {
			page = getArguments().getString("Page");
			if (page.equals("LeadCar")) {
				Continue.setText("继续领车");
				Continue.setVisibility(View.GONE);
				con.put(page, MyWork.class);
			} else if (page.equals("Transform")) {
				Continue.setText("继续领车");
				Continue.setVisibility(View.GONE);
				con.put(page, MyWork.class);
			} else if (page.equals("JSYLeadCar")) {
				Continue.setVisibility(View.GONE);
				con.put(page, VehicleMain.class);
			} else if (page.equals("toArrangement")) {
				tipContent.setText("安排任务已下达");
				Continue.setText("继续安排");
				con.put(page, Arrangement.class);
				backPage = getArguments().getString("backPage");
				con.put(backPage, EmergencyVehicleMain.class);
			}
			else if (page.equals("fromMyWork")) {
				tipContent.setText("安排任务已下达");
				Continue.setVisibility(View.GONE);
				backPage = getArguments().getString("backPage");
				if(backPage.equals("toMyWork"))
				    con.put(backPage, MyWork.class);
				else
					con.put(backPage, FaultHanding.class);
			}
			else if (page.equals("Recovery")) {
				tipContent.setText("回收任务已下达");
				Continue.setText("继续回收");
				backPage = getArguments().getString("backPage");
				con.put(page, Recovery.class);
				con.put(backPage, EmergencyVehicleMain.class);
				
			} else if (page.equals("FailureReport")) {
				tipContent.setText("故障报告成功");
				Continue.setText("继续报告");
				con.put(page, FailureReport.class);
				backPage = getArguments().getString("backPage");
				con.put(backPage, VehicleMain.class);
			}
			else if (page.equals("serviceEvaluation")){
				tipContent.setText("评价成功");
				Continue.setText("继续评价");
				backPage = getArguments().getString("backPage");
				con.put(backPage, VehicleMain.class);
			}
			else if(page.equals("payMent")){
				tipContent.setText("缴费成功");
				Continue.setVisibility(view.GONE);
				backPage = getArguments().getString("backPage");
				con.put(backPage, BillDetail.class);
			}
			
		}

		back_button = (TextView) view.findViewById(R.id.back_button);
		
		Continue.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
//				linster.updateData();
				back(getClassInCon(page));
			}
		});
		back_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				successBack();
			}
		});

		return view;
	}

	private void back() {
		((DrawerLayoutMenu) context).back();
	}

	
	private void back(Class cla) {
		((DrawerLayoutMenu) context).back(cla);
	}

	public SuccessPage setChange(Fragment fragment) {
		this.fragment = fragment;
		return this;
	}
	
	public static SuccessPage getIntstance() {
		return success;
	}
	
	private Class getClassInCon(String key){
		return con.get(key);
	}

	@Override
	public void successBack() {
		Fragment fra = fragment;
		 if (page.equals("Transform")||page.equals("LeadCar")) {
				back(getClassInCon(page));
			MyWork.getMyWork().setCurrentView("接送车");
		} else if (page.equals("JSYLeadCar")) {
			back(getClassInCon(page));
		}
		else if (backPage.equals("toVehicleMain")){
			boolean boo = backPage.equals("toVehicleMain");
			Class c= getClassInCon(backPage);
			back(getClassInCon(backPage));
		}else if(backPage.equals("toMain")){
			back(getClassInCon(backPage));
		}
		else if(backPage.equals("toMyWork")){
			back(getClassInCon(backPage));
		}
		else if(backPage.equals("toFaultHandling")){
			back(getClassInCon(backPage));
		}
		else if(backPage.equals("toBillDetail")){
			back(getClassInCon(backPage));
		}
	}
	
}