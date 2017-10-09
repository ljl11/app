package com.example.util;

import com.cqut.transacteSuccess.interfaces.MakeSuccess;
import com.example.newenergyvehicle.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class LRSlashAnim {
	public static void start(Activity activity,final ViewGroup parent,View view,boolean isleft) {
         final ImageView iv = new ImageView(activity);
        /* Bitmap bitmap = convertViewToBitmap(parent);
         iv.setImageBitmap(bitmap);*/
         parent.addView(iv);
         if(isleft) {
        	Animation translate_in_Left;
        	Animation translate_out_Left;
        	translate_in_Left = AnimationUtils.loadAnimation(activity, R.drawable.translate_in);  
          	translate_in_Left.setFillAfter(true);  
          	translate_in_Left.setDuration(500);  
          	translate_in_Left.setDetachWallpaper(true);  
      		translate_out_Left=AnimationUtils.loadAnimation(activity, R.drawable.translate_out); 
      		translate_out_Left.setAnimationListener(new AnimationListener(){  
                  @Override  
                  public void onAnimationEnd(Animation animation) {  
                	  parent.removeView(iv); 
                  }  

                  @Override  
                  public void onAnimationRepeat(Animation animation) {  
                  }  

                  @Override  
                  public void onAnimationStart(Animation animation) {  
                  }  
                 });  
     		translate_out_Left.setFillAfter(true);  
     		translate_out_Left.setDuration(500);  
     		translate_out_Left.setDetachWallpaper(true); 
        		view.setAnimation(translate_in_Left);
        		iv.setAnimation(translate_out_Left);   
        }
        else {
        	 Animation translate_in_Right;
        	 Animation translate_out_Right;
        	translate_in_Right=AnimationUtils.loadAnimation(activity, R.drawable.translate_in_right);
        	translate_in_Right.setFillAfter(true);  
        	translate_in_Right.setDuration(500);  
        	translate_in_Right.setDetachWallpaper(true);  
        		translate_out_Right =AnimationUtils.loadAnimation(activity, R.drawable.translate_out_right); 
            	
            	
            	
            	translate_out_Right.setAnimationListener(new AnimationListener(){  

            		 @Override  
            		 public void onAnimationEnd(Animation animation) {  
            			 parent.removeView(iv); 
            		 }  

            		 @Override  
            		 public void onAnimationRepeat(Animation animation) {  
            		     // TODO Auto-generated method stub   
            		       
            		 }  

            		 @Override  
            		 public void onAnimationStart(Animation animation) {  
            		     // TODO Auto-generated method stub   
            		       
            		 }  
            		   
            		});  
            	translate_out_Right.setFillAfter(true);  
            	translate_out_Right.setDuration(500);  
            	translate_out_Right.setDetachWallpaper(true);  
            	view.setAnimation(translate_in_Right);  
	            iv.setAnimation(translate_out_Right);   
        }
	}
	
	public static Bitmap convertViewToBitmap(View view){
	   view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	   int width =      view.getWidth();
	   int height  = view.getHeight();
	   view.layout(0, 0,width , height);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
	}
}
