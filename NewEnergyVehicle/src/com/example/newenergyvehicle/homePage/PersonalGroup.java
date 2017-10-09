package com.example.newenergyvehicle.homePage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newenergyvehicle.personal.Personal;

public class PersonalGroup extends AbstractActivityGroup{
	public static PersonalGroup BROWSE_GROUP;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewHistory = new ArrayList<View>();
        BROWSE_GROUP = this;

        View decorView = getLocalActivityManager().startActivity("first", new Intent(this, Personal.class)).getDecorView();
        replaceContentView(decorView);
    }
}
