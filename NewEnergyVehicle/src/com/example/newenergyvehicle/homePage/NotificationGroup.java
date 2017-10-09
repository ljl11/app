package com.example.newenergyvehicle.homePage;

import java.util.ArrayList;

import com.example.newenergyvehicle.notification.NotificationMain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class NotificationGroup extends AbstractActivityGroup{
	public static NotificationGroup BROWSE_GROUP;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewHistory = new ArrayList<View>();
        BROWSE_GROUP = this;

        View decorView = getLocalActivityManager().startActivity("first", new Intent(this, NotificationMain.class)).getDecorView();
        replaceContentView(decorView);
    }
}
