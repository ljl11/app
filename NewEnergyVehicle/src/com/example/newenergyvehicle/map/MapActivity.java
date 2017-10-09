package com.example.newenergyvehicle.map;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;
import com.example.newenergyvehicle.login.Login;
import com.example.newenergyvehicle.vehicleInfo.Vehicle_carlist;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MapActivity extends Fragment implements OnGetGeoCoderResultListener {
	private View view;
	private View dialogView;
	private Context context;
	private LayoutInflater inflater;
	private String sim;
	private ImageView back;
	public MapView mapView = null;
	public BaiduMap baiduMap = null;
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private Marker marker;
	private InfoWindow mInfoWindow;
	private Thread vehicle;
	private TextView position;
	private boolean isShow;
	
	
	private BitmapDescriptor bd;
	private BitmapDescriptor bdView;
	
    private Map<String, Object> map = new HashMap<String, Object>();  // 用来存储获取的定位信息  
	String url = "ws://123.207.235.33:9080/NewCar/api/ws/vehilce/rti?authorization="+Login.token;
	String url1 = "ws://localhost:9080/NewCar/api/ws/vehilce/rti?authorization="+Login.token;
	public WebSocketConnection	wsC	= new WebSocketConnection();
	private JSONArray json;
	private Vehicle_carlist vehicleInfo;
	private boolean isFrist = true;
	private boolean isOpen = true;
	private void initSocketClient(final String message){
		try {
			wsC.connect(url,  new WebSocketHandler()
			 {
			     @Override
			     public void onOpen() {
			    	 vehicle = new Thread(new Runnable() {
						@Override
						public void run() {
							while(isOpen){
								boolean connect =wsC.isConnected();
								if(connect)
									wsC.sendTextMessage(message);
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									
								}
							}
						}	
						
					});
			    	vehicle.start();

			     }

			     @Override
			     public void onTextMessage(String payload)  {
			    	 try {
					  JSONObject  msg = new JSONObject(payload);
					  if(baiduMap !=null)
						  baiduMap.clear();
						
						if(isFrist){
							isFrist = false;
						}
						
						if(msg.getString("vehiclesInfo") != null && msg.getString("vehiclesInfo").length() >0){
							
						json = new JSONArray(msg.getString("vehiclesInfo"));
						vehicleInfo.setElectricity(json.getJSONObject(0).getString("electricity"));
						vehicleInfo.setSpeed(json.getJSONObject(0).getString("speed"));
						vehicleInfo.setTime(json.getJSONObject(0).getString("time"));
						vehicleInfo.setMileage(json.getJSONObject(0).getString("mileage"));
						vehicleInfo.setVonline(json.getJSONObject(0).getString("vonline"));
						initOverlay(Double.parseDouble(json.getJSONObject(0).getString("ycoordinate")),
								Double.parseDouble(json.getJSONObject(0).getString("xcoordinate")));
						}
						else{
							position.setText("暂无信息");
							Toast.makeText(context, "数据暂未接入", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						try {
							vehicleInfo.setVonline(json.getJSONObject(0).getString("voline"));
							initOverlay(Double.parseDouble(json.getJSONObject(0).getString("ycoordinate")),
									Double.parseDouble(json.getJSONObject(0).getString("xcoordinate")));
							
						} catch (JSONException e1) {
							position.setText("暂无信息");
						}
					}
			     }

			     @Override
			     public void onClose(int code, String reason ) {
			    	 System.out.println("onClose reason=" + reason);
			    	 super.onClose(code,reason);
			     }
			 });
		} catch (WebSocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SDKInitializer.initialize(getActivity().getApplicationContext());
		this.inflater = inflater;
		try{
		view = inflater.inflate(R.layout.map_activity, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		context = inflater.getContext();
		JSONObject  message  =new JSONObject ();
		if (getArguments() != null){
			vehicleInfo = (Vehicle_carlist) getArguments().getSerializable("vehicleInfo");
			sim = vehicleInfo.getVin();
		}   
		try {
				message.put("roleType", Login.roleType);
				message.put("curOrgId", Login.orgId);
				message.put("sims", sim);
			    message.put("trackingMode","1");
		} catch (JSONException e) {
		
		}
		
		vehicleInfo.setSpeed("0");
		vehicleInfo.setVonline("-1");
		mapView = (MapView) view.findViewById(R.id.bmapview);
		position = (TextView) view.findViewById(R.id.position);
		back = (ImageView) view.findViewById(R.id.back);
		this.getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				mapView.onDestroy();
//				bd.recycle();
//				mapView = null;
//				if ( wsC.isConnected() )
//				{
//					wsC.disconnect();
//				}
				onBackPressed();
			}
		});
		bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
		initOfflineLayout();
     	initSocketClient(message.toString());
		initOverlayListener();
		return view;
	}
	
	
	private void initOfflineLayout() {
		baiduMap = mapView.getMap();
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
		baiduMap.setMapStatus(msu);
	}

	@Override
	public void onDetach() {
		super.onDestroy();
		mapView.onDestroy();
		bd.recycle();
		mapView = null;
		baiduMap = null;
		if ( wsC.isConnected() )
		{
			wsC.disconnect();
		}
	}

	
	@Override
	public void onStart() {
		
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	private void initOverlay(double x, double y) {
		CoordinateConverter converter  = new CoordinateConverter();  
		converter.from(CoordType.GPS);
		LatLng sourceLatLng = new LatLng(x, y);
		// sourceLatLng待转换坐标  
		//converter.coord(sourceLatLng);  
		//LatLng desLatLng = converter.convert();
		getAddress(sourceLatLng.latitude,sourceLatLng.longitude);
		OverlayOptions ooA = new MarkerOptions().anchor(0.5f, 0.5f).position(sourceLatLng).icon(bd)
				.zIndex(9).draggable(true);
        
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(sourceLatLng);
		if(baiduMap !=null){
			baiduMap.clear();
			marker = (Marker) baiduMap.addOverlay(ooA);
			baiduMap.setMapStatus(u);
				if(isShow)
					showInfoWindow();
		}
		
	}

	private void initOverlayListener() {
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				
				showInfoWindow();
				return true;
			}
 
		});

		baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				baiduMap.hideInfoWindow();
				isShow = false;
			}
		});
		
		baiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChange(MapStatus mkMapStatus) {
                
                float zoom = mkMapStatus.zoom;
               
            }

			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
				
				
			}

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				// TODO Auto-generated method stub
				
			}



        });
	}
	
	
	public void initDialogView(){
	
		dialogView = inflater.inflate(R.layout.car_detail_info, null);
		ViewHolder viewHolder = null;  
        if (dialogView.getTag() == null)  
        {  
            viewHolder = new ViewHolder();  
            viewHolder.plateNumber = (TextView) dialogView  
                    .findViewById(R.id.map_plate_number);  
            viewHolder.ele = (TextView) dialogView  
                    .findViewById(R.id.map_ele);  
            viewHolder.speed = (TextView) dialogView  
                    .findViewById(R.id.map_speed);  
            viewHolder.online = (TextView) dialogView  
                    .findViewById(R.id.map_online);
            viewHolder.mileage = (TextView) dialogView  
                    .findViewById(R.id.map_mileage);  
            viewHolder.time = (TextView) dialogView  
                    .findViewById(R.id.map_time);
            viewHolder.close = (TextView) dialogView  
                    .findViewById(R.id.map_close);
            viewHolder.close.setOnClickListener(new View.OnClickListener() {

    			@Override
    			public void onClick(View arg0) {
    				baiduMap.hideInfoWindow();
    			}
    		});
            dialogView.setTag(viewHolder);  
        }  
        viewHolder = (ViewHolder) dialogView.getTag();  
        viewHolder.plateNumber.setText(vehicleInfo.getCarNum());
        viewHolder.ele.setText(vehicleInfo.getElectricity()+"%");
        viewHolder.mileage.setText(vehicleInfo.getMileage()+"km");
        GregorianCalendar gc = new GregorianCalendar();
        String time = vehicleInfo.getTime();
        if(time.matches("^\\d+$")){
        	gc.setTime(new Date(Long.valueOf(vehicleInfo.getTime())));
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        	String sb=format.format(gc.getTime());  
        	viewHolder.time.setText(sb);
        }
        else
        	viewHolder.time.setText(time);
		viewHolder.speed.setText(vehicleInfo.getSpeed()+"km/h");
		String state = vehicleInfo.getVonline();
//		if(state.equals("0"))
//			viewHolder.online.setText("不在线");
//		else if(state.equals("1"))
//			viewHolder.online.setText("在线");
//		else
//			viewHolder.online.setText("充电中");
	}

	
	public void showInfoWindow(){
		initDialogView();
		OnInfoWindowClickListener listener = null;
		listener = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {
			}
		};
		 LatLng ll=marker.getPosition();
         Point p=baiduMap.getProjection().toScreenLocation(ll);
         p.y-=47;
         LatLng llinfo=baiduMap.getProjection().fromScreenLocation(p);
//		bdView = BitmapDescriptorFactory.fromView(dialogView);
         
		mInfoWindow = new InfoWindow(dialogView, ll, -20);
		
		baiduMap.showInfoWindow(mInfoWindow);
		isShow = true;
		
	}
	public void clearOverlay() {
		baiduMap.clear();
	}

	public void onBackPressed() {
		((DrawerLayoutMenu)context).back();
	}
	
	/** 
     * 进行反地理编码 
     *  
     * @param latitude 
     *            纬度信息  
     * @param lontitude 
     *            经度信息 
     */  
    public void getAddress(double latitude, double lontitude) {  
        LatLng mLatLng = new LatLng(latitude,lontitude);
        // 获取反地理编码对象  
        mSearch = GeoCoder.newInstance();  
        // 反地理编码请求参数对象  
        ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption();  
        // 设置请求参数  
        mReverseGeoCodeOption.location(mLatLng);   
        // 发起反地理编码请求(经纬度->地址信息)  
        mSearch.reverseGeoCode(mReverseGeoCodeOption);
        
        // 设置查询结果监听者  
        mSearch.setOnGetGeoCodeResultListener(this);  
    }
    
   
        @Override  
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  
            // 反地理编码查询结果回调函数  
            // 将地理位置信息载入到集合中 
            map.put("address", result.getAddress());
            position.setText(map.get("address").toString());
            if (mOnResultMapListener != null) {  
                mOnResultMapListener.onReverseGeoCodeResult(map);  
            }  
        }  
  
        @Override  
        public void onGetGeoCodeResult(GeoCodeResult result) {  
            // 地理编码查询结果回调函数  
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("latitude", result.getLocation().latitude);  
            map.put("longitude", result.getLocation().longitude);  
            map.put("address", result.getAddress());  
            if (mOnResultMapListener != null) {  
                mOnResultMapListener.onGeoCodeResult(map);  
            }  
        }  
  
  
    /** 
     * 注册结果监听回调 
     *  
     * @param mOnResultMapListener 
     */  
    public void registerOnResult(OnResultMapListener mOnResultMapListener) {  
        this.mOnResultMapListener = mOnResultMapListener;  
    }  
  
    private OnResultMapListener mOnResultMapListener;  
  
    /** 
     * 数据结果回传 
     *  
     * @author NapoleonBai 
     * 
     */  
    public interface OnResultMapListener {  
        /** 
         * 反地理编码执行回传 
         *  
         * @param map 
         */  
        public void onReverseGeoCodeResult(Map<String, Object> map);  
  
        /** 
         * 地理编码执行回传 
         *  
         * @param map 
         */  
        public void onGeoCodeResult(Map<String, Object> map);  
    }
    
    private class ViewHolder  
    {  
    	private TextView plateNumber;
    	private TextView ele;
    	private TextView speed;
    	private TextView online;
    	private TextView mileage;
    	private TextView time;
      	private TextView close;
    }
    @Override
	public void onHiddenChanged(boolean hidden) {
	    super.onHiddenChanged(hidden);
		if(hidden){
			isOpen =false;
		} else {
			
		}
	}
    
    @Override 
    public void onDestroy(){
    	super.onDestroy();
    }
}
