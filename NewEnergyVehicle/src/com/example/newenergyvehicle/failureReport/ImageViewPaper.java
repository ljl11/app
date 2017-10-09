package com.example.newenergyvehicle.failureReport;

import java.util.ArrayList;
import java.util.List;

import com.example.newenergyvehicle.R;
import com.example.util.image.ZoomImageView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class ImageViewPaper extends Dialog {
	private Context context;
	private ViewPager mViewPaper;
	private int currentItem;
	//记录上一次点的位置
	private int oldPosition = 0;
	//存放图片的id
	private List<String> imageIds;
	private ViewPagerAdapter adapter;

	
	public ImageViewPaper(Context arg0) {
		super(arg0);
		this.context = arg0;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().getDecorView().setPadding(0, 0, 0, 0);
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.image_paper);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (display.getWidth());
		lp.height= (int) (display.getHeight() * 0.6f);
		getWindow().setAttributes(lp);
		getWindow().setGravity(Gravity.CENTER);
		initView();

	}
	private void initView() {
		mViewPaper = (ViewPager) findViewById(R.id.vp);
		adapter = new ViewPagerAdapter();
		mViewPaper.setAdapter(adapter);
		
		mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			

			@Override
			public void onPageSelected(int position) {
				oldPosition = position;
				currentItem = position;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
	}

	private class ViewPagerAdapter extends PagerAdapter{
		List<ImageView> images = new ArrayList<ImageView>();
		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			view.removeView(images.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			view.addView(images.get(position));
			return images.get(position);
		}
		
		 public void setDeviceList() {
			 images.clear();
		     if (imageIds != null) {
		    	 for(int i = 0; i < imageIds.size(); i++){
		        		ZoomImageView imageView = new ZoomImageView(context);
		    			imageView.setImageBitmap(decodeSampledBitmapFromResource(imageIds.get(i)));
		    			images.add(imageView);
		    		}
		        } 
		        notifyDataSetChanged();  
		 }
		 public void setDeviceBitmap(Bitmap bitmap) {
			 images.clear();
			 ZoomImageView imageView = new ZoomImageView(context);
			 imageView.setImageBitmap(bitmap);
			 images.add(imageView);
			 notifyDataSetChanged();
		 }
		 
		 private int mChildCount = 0;

		    @Override public void notifyDataSetChanged() {
		      mChildCount = getCount();
		      super.notifyDataSetChanged();
		    }

		    @Override
		    public int getItemPosition(Object object) {
		    	return POSITION_NONE;
		    }
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	public void setCurrent(String path){
		List<String> list = imageIds;
		int index = 0;
		if(path != null || !path.equals("")){
			index = list.indexOf(path);
		if(index < 0)
			index = 0;
		}
		mViewPaper.setCurrentItem(index, false);
	}
	
	public void setImageCache(List<String> list){
		imageIds = list;
		adapter.setDeviceList();
	}
	
	public void setBitmapCache(Bitmap bitmap) {
		adapter.setDeviceBitmap(bitmap);
	}
	
	private int calculateInSampleSize(BitmapFactory.Options options, int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (height > reqHeight) {
			int widthRatio = Math.round((float) width / (float) reqHeight);
			int heightRatio = Math.round((float) width / (float) reqHeight);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}

	private Bitmap decodeSampledBitmapFromResource(String pathName) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.outHeight = 400;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, 300);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
		return bitmap;
	}
}
