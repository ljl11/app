package com.example.newenergyvehicle.salesPersonnel;

import com.example.newenergyvehicle.homePage.DrawerLayoutMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class HorizontalScrollViewEx extends HorizontalScrollView {

	final int RIGHT = 0;  
    final int LEFT = 1;  
    private GestureDetector gestureDetector;
	
	public HorizontalScrollViewEx(Context context) {
		super(context);
	}
	
	public HorizontalScrollViewEx(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {  
        super(context, attrs, defStyleAttr);  
    }

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		return super.onGenericMotionEvent(event);
	}
	
	private GestureDetector.OnGestureListener onGestureListener =   
	        new GestureDetector.SimpleOnGestureListener() {  
	        @Override  
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
	                float velocityY) {  
	            float x = e2.getX() - e1.getX();  
	            float y = e2.getY() - e1.getY();  
	  
	            if (x > 0) {  
	                doResult(RIGHT);  
	            } else if (x < 0) {  
	                doResult(LEFT);  
	            }  
	            return true;  
	        }  
	    };
	    
	    public void doResult(int action) {  
	    	  
	        switch (action) {  
	        case RIGHT:  
	            break;  
	  
	        case LEFT:  
	            break;  
	  
	        }  
	    } 

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            DrawerLayoutMenu.isOpenMenu=false;
            break;  
        case MotionEvent.ACTION_CANCEL:  
            DrawerLayoutMenu.isOpenMenu=true;
            break;  
        case MotionEvent.ACTION_UP:  
            DrawerLayoutMenu.isOpenMenu=true;
            break;  
        case MotionEvent.ACTION_MOVE:  
        	DrawerLayoutMenu.isOpenMenu=false;
            break;  
        default:  
		}
		return super.onTouchEvent(ev);
	}

}
