package com.example.util.dropDownOrUp;

import android.widget.AbsListView;

public abstract class Scrolling {
	public abstract void onscroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount);
	public abstract void onScrollStateChanged(AbsListView arg0, int arg1) ;
}
