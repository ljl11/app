package com.example.util.dropDownOrUp;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class CommonDropInterfaceForAdd {
	private ListView list;
	private DownUp dr;
	public final static int SCROLL_TOP=0;
	public final static int SCROLL_BOTTOM=1;
	private int status=SCROLL_TOP;
	private Moving mo;
	private Scrolling sc;
	private float lastX = 0;
	private float lastY = 0;
	public boolean hasDone = true;
	public CommonDropInterfaceForAdd(ListView list,DownUp dr,Moving mo,Scrolling sc) {
		this.list = list;
		this.dr = dr;
		this.mo = mo;
		this.sc =sc;
		this.init();
	}
	public void init() {
		this.list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if(sc!=null)
					sc.onScrollStateChanged(arg0, arg1);
				
			}
			
			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {    
					status = SCROLL_BOTTOM;  
					hasDone = true;
	            }  
				else {
					if(hasDone)
						hasDone = false;
				}
				if(firstVisibleItem==0) {
					status=SCROLL_TOP;
				}
				if(sc!=null)
					sc.onscroll(arg0, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
		this.list.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(status== SCROLL_BOTTOM || status == SCROLL_TOP) {
					float x = arg1.getX();
					float y = arg1.getY();
					switch(arg1.getAction()) {
					case MotionEvent.ACTION_DOWN:
						lastX = x;
						lastY = y;
						if(mo!=null){
							mo.down(arg1);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if(mo!=null){
							mo.move(arg1);
						}
						break;
					case MotionEvent.ACTION_UP:
						if(mo!=null){
							mo.up(arg1);
						}
						if(dr!=null) {
							if(status==SCROLL_TOP) {
								if(y-lastY > Math.abs(x-lastX) && hasDone) {
									dr.moveDown();
								}
							}
							else if(status == SCROLL_BOTTOM) {
								if(lastY-y > Math.abs(x-lastX) && hasDone) {
									dr.moveUp();
								}
							}
						}
						break;
					}
				}
				
				return arg0.onTouchEvent(arg1);
			}
		});
	}
}
