package com.example.newenergyvehicle.homePage;

import java.util.ArrayList;

import com.example.newenergyvehicle.vehicle.VehicleMain;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class HomePageGroup extends AbstractActivityGroup{
	public static HomePageGroup BROWSE_GROUP;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewHistory = new ArrayList<View>();
        BROWSE_GROUP = this;

        View decorView = getLocalActivityManager().startActivity("first", new Intent(this, VehicleMain.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)).getDecorView();
        replaceContentView(decorView);
    }
}
