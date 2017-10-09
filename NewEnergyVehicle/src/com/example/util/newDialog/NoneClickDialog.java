package com.example.util.newDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.example.newenergyvehicle.R;
public class NoneClickDialog extends Dialog{  
	  private RelativeLayout none_click_parent;
	  private Activity activity;
  
public NoneClickDialog(Activity activity) {  
    super(activity,R.style.Dialog);  
    this.activity = activity;
}  
  
@Override  
protected void onCreate(Bundle savedInstanceState) {  
    super.onCreate(savedInstanceState);  
    setContentView(R.layout.none_click_activity);
    none_click_parent = (RelativeLayout) this.findViewById(R.id.none_click_parent);
    DisplayMetrics metrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	none_click_parent.setBackgroundColor(0x00000000);
	GifView ii = new GifView(activity);
	ii.setGifImage(R.drawable.loading);
	ii.setShowDimension((int) (50 * metrics.density), (int) (50 * metrics.density));
	none_click_parent.addView(ii);
	none_click_parent.setGravity(Gravity.CENTER);
	RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams((int) (50 * metrics.density), (int) (50 * metrics.density));
	ii.setLayoutParams(rl);
}  
}  