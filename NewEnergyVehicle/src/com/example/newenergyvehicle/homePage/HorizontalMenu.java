package com.example.newenergyvehicle.homePage;


import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.login.Login;
import com.example.util.menu.HorizontalMenuScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class HorizontalMenu extends Activity{

	private HorizontalMenuScrollView mMenu;
	private Login login;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.horizontal_menu);
		mMenu = (HorizontalMenuScrollView) findViewById(R.id.id_menu);
	}

	public void toggleMenu(View view)
	{
		mMenu.toggle();
	}
}
