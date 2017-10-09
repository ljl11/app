package com.example.newenergyvehicle.pile;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.model.LatLng;
import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.ConsultationGroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PileActivity extends Activity{
	private ImageView back;
	public MapView mapView = null;
	public BaiduMap baiduMap = null;
	private Marker marker;
	private InfoWindow mInfoWindow;
	private BitmapDescriptor bd;
	private LocationClient mLocationClient = null;
	private Marker myLocationMarker;
	private boolean isFirstLoc = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.pile_activity);

		mapView = (MapView) this.findViewById(R.id.bmapview);
		
		Bundle bundle = this.getIntent().getExtras();
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onDestroy();
				ConsultationGroup.BROWSE_GROUP.back();
			}
		});

	}

	private void initOfflineLayout() {
		baiduMap = mapView.getMap();
		baiduMap.setMyLocationEnabled(true);
//		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
//		baiduMap.setMapStatus(msu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		bd.recycle();
		mapView = null;
	}

	@Override
	protected void onStart() {
		initOfflineLayout();
		initOverlayListener();
		doLocation();
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	private void doLocation(){
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
		mLocationClient = new LocationClient(getApplicationContext());
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setOpenGps(true);
		option.setLocationMode(LocationMode.Hight_Accuracy);
		
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(new MyLocationListener());

		mLocationClient.start();
	}
	private void initOverlay(double x, double y) {
		LatLng lat = new LatLng(x, y);
		bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
		OverlayOptions ooA = new MarkerOptions().position(lat).icon(bd)
				.zIndex(9).draggable(true);

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(lat);
		marker = (Marker) baiduMap.addOverlay(ooA);
		baiduMap.setMapStatus(u);
	}

	private void initOverlayListener() {
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				OnInfoWindowClickListener listener = null;
				listener = new OnInfoWindowClickListener() {
					public void onInfoWindowClick() {
					}
				};
				LatLng ll = marker.getPosition();
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory
						.fromView(button), ll, -47, listener);
				baiduMap.showInfoWindow(mInfoWindow);
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
			}
		});
	}

	public void clearOverlay(View view) {
		baiduMap.clear();
	}

	@Override
	public void onBackPressed() {
		ConsultationGroup.BROWSE_GROUP.back();
		super.onBackPressed();
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			if (myLocationMarker!=null&&isFirstLoc == false) {
				myLocationMarker.remove();
			}
			LatLng cenpt = new LatLng(location.getLatitude(),
					location.getLongitude());
			try {
				LatLng point = new LatLng(cenpt.latitude, cenpt.longitude);
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.real_time_location);
				OverlayOptions option = new MarkerOptions().position(point).icon(
						bitmap);
				myLocationMarker = (Marker) baiduMap.addOverlay(option);
				isFirstLoc = false;
				updateLocationPosition(baiduMap, cenpt, 18);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void updateLocationPosition(BaiduMap eBikeAMap, LatLng cenpt,
			float zoom) {
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(zoom).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		baiduMap.setMapStatus(mMapStatusUpdate);
	}
}
