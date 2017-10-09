package com.example.util.horizontalScrollView;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


public class PhotoHorizontalScrollView extends HorizontalScrollView implements OnClickListener {
	public interface CurrentImageChangeListener {
		void onCurrentImgChanged(int position, View viewIndicator);
	}

	public interface OnItemClickListener {
		void onClick(View view, int pos);
	}

	private CurrentImageChangeListener mListener;

	private OnItemClickListener mOnClickListener;

	private LinearLayout mContainer;

	private int mChildWidth;
	private int mChildHeight;
	private int mCurrentIndex;
	private int mFristIndex;
	private View mFirstView;
	private PhotohorizontalScrollAdapter mAdapter;
	private int mCountOneScreen;
	private int mScreenWitdh;

	private Map<View, Integer> mViewPos = new HashMap<View, Integer>();

	public PhotoHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWitdh = outMetrics.widthPixels;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer = (LinearLayout) getChildAt(0);
	}

	protected void loadNextImg() {
		if (mCurrentIndex == mAdapter.getCount() - 1) {
			return;
		}
		scrollTo(0, 0);
		mViewPos.remove(mContainer.getChildAt(0));
		mContainer.removeViewAt(0);

		View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
		view.setOnClickListener(this);
		mContainer.addView(view);
		mViewPos.put(view, mCurrentIndex);

		mFristIndex++;
		if (mListener != null) {
			notifyCurrentImgChanged();
		}

	}

	protected void loadPreImg() {
		if (mFristIndex == 0)
			return;
		int index = mCurrentIndex - mCountOneScreen;
		if (index >= 0) {
			int oldViewPos = mContainer.getChildCount() - 1;
			mViewPos.remove(mContainer.getChildAt(oldViewPos));
			mContainer.removeViewAt(oldViewPos);

			View view = mAdapter.getView(index, null, mContainer);
			mViewPos.put(view, index);
			mContainer.addView(view, 0);
			view.setOnClickListener(this);
			scrollTo(mChildWidth, 0);
			mCurrentIndex--;
			mFristIndex--;
			if (mListener != null) {
				notifyCurrentImgChanged();

			}
		}
	}

	public void notifyCurrentImgChanged() {
		
		for (int i = 0; i < mContainer.getChildCount(); i++) {
			mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
		}

		mListener.onCurrentImgChanged(mFristIndex, mContainer.getChildAt(0));

	}

	public void initDatas(PhotohorizontalScrollAdapter mAdapter) {
		this.mAdapter = mAdapter;
		mContainer = (LinearLayout) getChildAt(0);
		final View view = mAdapter.getView(0, null, mContainer);
		mContainer.addView(view);

		if (mChildWidth == 0 && mChildHeight == 0) {
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			view.measure(w, h);
			mChildHeight = view.getMeasuredHeight();
			mChildWidth = view.getMeasuredWidth();
			mChildHeight = view.getMeasuredHeight();
			mCountOneScreen = (mScreenWitdh / mChildWidth == 0) ? mScreenWitdh
					/ mChildWidth + 1 : mScreenWitdh / mChildWidth + 2;

		}
		initFirstScreenChildren(mCountOneScreen);
	}

	public void initFirstScreenChildren(int mCountOneScreen) {
		mContainer = (LinearLayout) getChildAt(0);
		mContainer.removeAllViews();
		mViewPos.clear();

		for (int i = 0; i < mCountOneScreen; i++) {
			View view = mAdapter.getView(i, null, mContainer);
			view.setOnClickListener(this);
			mContainer.addView(view);
			mViewPos.put(view, i);
			mCurrentIndex = i;
		}

		if (mListener != null) {
			notifyCurrentImgChanged();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:

			int scrollX = getScrollX();
			if (scrollX >= mChildWidth) {
				loadNextImg();
			}
			if (scrollX == 0) {
				loadPreImg();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		if (mOnClickListener != null) {
			for (int i = 0; i < mContainer.getChildCount(); i++) {
				mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
			}
			mOnClickListener.onClick(v, mViewPos.get(v));
		}
	}

	public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}

	public void setCurrentImageChangeListener(
			CurrentImageChangeListener mListener) {
		this.mListener = mListener;
	}
}
