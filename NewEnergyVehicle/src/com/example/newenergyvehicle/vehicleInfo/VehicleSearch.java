package com.example.newenergyvehicle.vehicleInfo;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.homePage.HomePageGroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class VehicleSearch extends Activity{
	private TextView query_button;
	private ListView listView;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_book_contents);
		
		query_button = (TextView) findViewById(R.id.query_button);
		query_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				HomePageGroup.BROWSE_GROUP.back();
			}
		});
	}
}
